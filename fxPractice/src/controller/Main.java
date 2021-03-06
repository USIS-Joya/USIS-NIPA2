package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Node;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connection;
import model.management;
import model.path;
import model.performance;

public class Main extends Application {
	private Main main;
	private Stage primaryStage;
	Random rng = new Random();
	public static String PathFile = "D:\\Usis-AM-Data-Extract\\";
	public static String value = "";
	public static String key = "";
	SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	Calendar cal = Calendar.getInstance();

	// 수행작업 Tab- Performance Table - 180515 CWJ
	private ObservableList<performance> performList = FXCollections.observableArrayList();
	private performance perform = new performance();

	@FXML
	private TableView<performance> performTable;
	@FXML
	private TableColumn<performance, String> p_FileName;
	@FXML
	private TableColumn<performance, String> p_FilePath;
	@FXML
	private TableColumn<performance, String> p_Start;
	@FXML
	private TableColumn<performance, Double> p_Progress;
	//

	// 화면관리 Tab- Management Table - 180515 CWJ
	private ObservableList<management> manageList = FXCollections.observableArrayList();
	private management manage;
	@FXML
	private TableView<management> manageTable;
	@FXML
	private TableColumn<management, String> m_FileName;
	@FXML
	private TableColumn<management, String> m_FilePath;
	@FXML
	private TableColumn<management, String> m_Start;
	@FXML
	private TableColumn<management, String> m_End;
	//
	//

	// 환경설정 Tab- FolderPath Table
	private ObservableList<path> pathList = FXCollections.observableArrayList();
	private path path;
	@FXML
	private TableView<path> pathTable;
	@FXML
	private TableColumn<path, String> folderName;
	@FXML
	private TextField txt_ip;
	@FXML
	private TextField txt_port;
	//

	Task<ObservableList<performance>> tasks = new GetTask();

	public ObservableList<path> getPathList() {
		return pathList;
	}

	// Performance data list - 180515 CWJ
	public ObservableList<performance> getPerformList() {
		return performList;
	}

	// Management data list - 180515 CWJ
	public ObservableList<management> getManagementList() {
		return manageList;
	}

	public Main() {
		pathList.add(new path(PathFile));

		// performList.add(new performance("", "", "", ""));
		// manageList.add(new management("", "", "", ""));
	}

	@FXML
	private void initialize() {

		/// m_FileName,m_FilePath,m_Start,m_End
		p_FileName.setCellValueFactory(cellData -> cellData.getValue().getFileNameProperty());
		p_FilePath.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		p_Start.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
		p_Progress.setCellValueFactory(cellData -> cellData.getValue().getProgressProperty().asObject());

		// p_Progress.setCellValueFactory(new PropertyValueFactory<performance,
		// Double>("progress"));	
		p_Progress.setCellFactory(ProgressBarTableCell.<performance>forTableColumn());
		//performTable.setItems(performList);

		m_FileName.setCellValueFactory(cellData -> cellData.getValue().getFileNameProperty());
		m_FilePath.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		m_Start.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
		m_End.setCellValueFactory(cellData -> cellData.getValue().getEndProperty());
		// manageTable.setItems(manageList);

		folderName.setCellValueFactory(cellData -> cellData.getValue().getPathProperty());
	}

	// 현재의 메인 스테이지를 반환합니다.
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setMain(Main main) {
		this.main = main;
		getPerformList().add(perform);
		getManagementList().add(manage);
		getPathList().add(path);

		// performance perform = new performance("","","","");
		// perform.setFileName(PathFile);
		// performTable.getItems().add(perform);

		performTable.setItems(main.getPerformList());
		manageTable.setItems(main.getManagementList());
		pathTable.setItems(main.getPathList());
	}

	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Test");
		// setRootLayout();
		setMainView();
		File clsFolder = new File(PathFile);
		File[] arrFile = clsFolder.listFiles();

		for (int i = 0; i < arrFile.length; ++i) {
			// perform.setStart(dataFormat.format(cal.getTime()));
			String a = dataFormat.format(cal.getTime());

			System.out.println("arrFile[" + i + "] = " + arrFile[i].getName());
			performList.add(new performance(arrFile[i].getName(), clsFolder.getPath(), a, tasks.getWorkDone()));
		}

		// run in background thread-180524 CWJ
		new Thread() {
			@Override
			public void run() {
				readFile();
			};
		}.start();

	}

	private void readFile() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		File clsFolder = new File(PathFile);

		if (clsFolder.exists() == false) {
			System.out.println("folder is not found");
		} else {
			File[] arrFile = clsFolder.listFiles();

			for (int i = 0; i < arrFile.length; ++i) {
				// perform.setStart(dataFormat.format(cal.getTime()));
				String a = dataFormat.format(cal.getTime());
				perform.setFileName(arrFile[i].getName());
				perform.setFilePath(clsFolder.getPath());
				perform.setStart(a);

				// performTable.getItems().add(perform);

				perform.setProgressBar(tasks.progressProperty().doubleValue());
				System.out.println("*Read* arrFile[" + i + "] = " + arrFile[i].getName());
				// performList.add(new performance(arrFile[i].getName(), clsFolder.getPath(), a,
				// 0.0));
			}
		}
	}

	// 매개변수로 넘어온 하나의 경로 데이터를 이용해서 path관리화면을 띄웁니다.
	// 이후에 종료되고 넘어온 반환 값을 그대로 다시 한 번 반환
	public int setpathDataView(path path, performance perform) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/pathView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Folder 찾기");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			pathController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPath(path);
			controller.setPerform(perform);
			dialogStage.showAndWait();
			return controller.getReturnValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void setMainView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../view/MainView.fxml"));
			AnchorPane MainView = (AnchorPane) loader.load();
			Scene scene = new Scene(MainView);
			primaryStage.setScene(scene);
			primaryStage.show();

			Main controller = loader.getController();
			controller.setMain(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void pathAction() {
		path path = new path("");
		performance perform = new performance("", "", "", 0);
		int returnValue = main.setpathDataView(path, perform);
		if (returnValue == 1) {
			main.getPathList().add(path);
			// PerformList 목록 초기화
			performList.clear();

			File clsFolder = new File(path.getPath());

			if (clsFolder.exists() == false) {
				System.out.println("folder is not found");
			} else {
				File[] arrFile = clsFolder.listFiles();

				/* 파일 수만큼 for문을 돌면서 파일의 이름을 찾고 해당 파일을 performList에 추가 */
				for (int i = 0; i < arrFile.length; ++i) {
					// 파일인지 확인
					if (arrFile[i].isFile() == true) {
						String b = dataFormat.format(cal.getTime());
						System.out.println("arrFile[" + i + "] = " + arrFile[i].getName());
						performList
								.add(new performance(arrFile[i].getName(), clsFolder.getPath(), b, 1.0));
					} else {
						System.out.println("arrFile[" + i + "] = " + arrFile[i].getName());
					}
				}
				// 테이블에 performList 정보 업데이트
				performTable.setItems(performList);
			}
		}
	}
	// if (returnValue == 1) {
	// main.getPathList().add(path);
	// // add columns 180524
	// main.getPerformList().add(perform);
	// }
	// }

	// 수정
	@FXML
	private void modifyAction() {
		path path = pathTable.getSelectionModel().getSelectedItem();
		if (path != null) {
			main.setpathDataView(path, null);
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류 메세지");
			alert.setHeaderText("선택 오류가 발생했습니다.");
			alert.setContentText("수정 단어를 선택해주세요.");
			alert.showAndWait();
		}
	}

	// 삭제
	@FXML
	private void deleteAction() {
		int selectedIndex = pathTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			pathTable.getItems().remove(selectedIndex);
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류 메세지");
			alert.setHeaderText("선택 오류가 발생했습니다.");
			alert.setContentText("삭제할 단어를 선택해주세요.");
			alert.showAndWait();
		}
	}

	@FXML
	private void displayValue(ActionEvent event) {
		try {
			Connection conn = new Connection();

			conn.setIP(txt_ip.getText());
			String str_port = txt_port.getText();
			int Port = Integer.parseInt(str_port);
			// conn.setPort(Port);

			//
			System.out.println("MongoDB IP : " + conn.getIP().toString());
			System.out.println("MongoDB PORT : " + conn.getPort());
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class GetTask extends Task<ObservableList<performance>> {

		@Override
		protected ObservableList<performance> call() throws Exception {

			for (int i = 0; i < 500; i++) {

				updateProgress(i, 500);

				Thread.sleep(5);

			}

			ObservableList<performance> per = FXCollections.observableArrayList();

			per.add(new performance());

			per.add(new performance());

			return per;

		}

	}
	// static class TestTask extends Task<Void> {
	// private ObservableList<performance> result =
	// FXCollections.observableArrayList();
	//
	// public final ObservableList<performance> getResult() {
	// return result;
	// }
	//
	// private final int waitTime; // milliseconds
	// private final int pauseTime; // milliseconds
	//
	// public static final int NUM_ITERATIONS = 100;
	//
	// public TestTask(int waitTime, int pauseTime) {
	// this.waitTime = waitTime;
	// this.pauseTime = pauseTime;
	// }
	//
	// @Override
	// protected Void call() throws Exception {
	// this.updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, 1);
	// Thread.sleep(waitTime);
	// for (int i = 0; i < NUM_ITERATIONS; i++) {
	// updateProgress((1.0 * i) / NUM_ITERATIONS, 1);
	// Thread.sleep(pauseTime);
	// }
	// performance per = new performance();
	// Platform.runLater(() -> result.add(per));
	//
	// this.updateProgress(1, 1);
	// return null;
	// }
	//
	// }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean isConn = false;
		try {
			Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
			mongoLogger.setLevel(Level.SEVERE);

			isConn = true;
			if (isConn) {
				System.out.println("MongoDB연결");
				Connection conn = new Connection();
				// db연결 및 insert
				conn.DBConnection(PathFile);
			} else {
				System.out.println("연결 실패");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		launch(args);
	}

}
