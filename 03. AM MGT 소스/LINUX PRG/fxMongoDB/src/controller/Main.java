package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import model.Connection;
import model.management;
import model.mongoThread;
import model.path;
import model.performance;

public class Main extends Application {
	public static Main main;
	private Stage primaryStage;

	// *****************환경설정*********************
	public Preferences settings;
	public final String node_Name = "Settings";
	public double value_window_size_X = 0.0;
	public double value_window_size_Y = 0.0;
	public final String window_size_X = "WINDOW_SIZE_X";
	public final String window_size_Y = "WINDOW_SIZE_Y";
	// *******************************************

	// 수행작업 Tab- Performance Table - 180515 CWJ
	@FXML
	private TableView<performance> performTable = new TableView<performance>();
	public ObservableList<performance> performList = FXCollections.observableArrayList();
	private performance perform = new performance();

	private List<mongoThread> threadList;

	// 화면관리 Tab- Management Table - 180515 CWJ
	@FXML
	private TableView<management> manageTable = new TableView<management>();
	public ObservableList<management> manageList = FXCollections.observableArrayList();
	private management manage = new management();

	//
	// 환경설정 Tab- Path Table - 180515 CWJ
	@FXML
	private TableView<path> pathTable;
	@FXML
	private TableColumn<path, String> folderName;
	public ObservableList<path> pathList = FXCollections.observableArrayList();
	private path path;

	// DB Connection Text Field
	@FXML
	private TextField txt_ip;
	@FXML
	private TextField txt_port;
	@FXML
	private TextField txt_usr;
	@FXML
	private TextField txt_pwd;
	//

	public static void main(String[] args) {
		try {
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Test");

		Preferences prefsRoot = Preferences.userRoot();
		settings = prefsRoot.node("Setting");
		value_window_size_X = settings.getDouble(window_size_X, 600.0);
		value_window_size_Y = settings.getDouble(window_size_Y, 400.0);
		setMainView();
	}

	@SuppressWarnings("unchecked")
	@FXML
	private void initialize() {
		TableColumn<performance, String> name = new TableColumn<>("파일명");
		name.setCellValueFactory(cellData -> cellData.getValue().getFileNameProperty());
		name.setPrefWidth(300);

		TableColumn<performance, String> path = new TableColumn<>("파일경로");
		path.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		path.setPrefWidth(120);

		TableColumn<performance, String> start = new TableColumn<>("시작");
		start.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
		start.setPrefWidth(150);

		TableColumn<performance, String> progress = new TableColumn<>("진행률");
		progress.setCellValueFactory(cellData -> cellData.getValue().getprogressProperty());
		progress.setPrefWidth(100);

		performTable.getSortOrder();
		performTable.getColumns().addAll(name, path, start, progress);

		TableColumn<management, String> m_name = new TableColumn<>("파일명");
		m_name.setCellValueFactory(cellData -> cellData.getValue().getFileNameProperty());
		m_name.setPrefWidth(250);

		TableColumn<management, String> m_path = new TableColumn<>("파일경로");
		m_path.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		m_path.setPrefWidth(120);

		TableColumn<management, String> m_start = new TableColumn<>("시작");
		m_start.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
		m_start.setPrefWidth(150);

		TableColumn<management, String> m_End = new TableColumn<>("종료");
		m_End.setCellValueFactory(cellData -> cellData.getValue().getEndProperty());
		m_End.setPrefWidth(150);

		performTable.getSortOrder();
		manageTable.getColumns().addAll(m_name, m_path, m_start, m_End);

		folderName.setCellValueFactory(cellData -> cellData.getValue().getPathProperty());

	}

	// 현재의 메인 스테이지를 반환합니다.
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setMain(Main main) {
		Main.main = main;
		getPathList().add(path);
		pathTable.setItems(main.getPathList());
		getPerformList().add(perform);
		performTable.setItems(main.getPerformList());
		getManageList().add(manage);
		manageTable.setItems(main.getManageList());

	}

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

			Main controller = loader.getController();
			controller.setMain(this);

			Scene scene = new Scene(MainView);
			primaryStage.setScene(scene);
			primaryStage.setWidth(value_window_size_X);
			primaryStage.setHeight(value_window_size_Y);
			primaryStage.show();

			ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
				settings.putDouble(window_size_X, primaryStage.getWidth());
				settings.putDouble(window_size_Y, primaryStage.getHeight());
			};
			/* Listener 연결 */
			primaryStage.widthProperty().addListener(stageSizeListener);
			primaryStage.heightProperty().addListener(stageSizeListener);
			// ****

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SetWindowSize(double x, double y) {
		this.primaryStage.setWidth(x);
		this.primaryStage.setHeight(y);
	}

	public ObservableList<performance> getPerformList() {
		return performList;
	}

	public ObservableList<management> getManageList() {
		return manageList;
	}

	public ObservableList<path> getPathList() {
		return pathList;
	}

	public void addRowInManageList(String filePath, String folderPath, String start, String end) {
		manageList.add(new management(filePath, folderPath, start, end));
	}

	public void addRowInPerformList(String filePath, String folderPath, String start, String pro) {
		performList.add(new performance(filePath, folderPath, start, pro));
	}

	// START thread
	public void startThread() {
		try {
			mongoThread mTh;
			threadList = new ArrayList<mongoThread>();

			for (int count = 0; count < main.getPathList().size(); count++) {
				if (count == main.getPathList().size() - 1) {
					mTh = new mongoThread(folderName.getCellData(count));
					mTh.start();

					// isStart = true;
					threadList.add(mTh);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// DB 연결
	@FXML
	private void displayValue(ActionEvent event) {
		try {
			Connection conn = new Connection();

			conn.setIP(txt_ip.getText());
			String IP = txt_ip.getText();
			String str_port = txt_port.getText();
			int Port = Integer.parseInt(str_port);
			conn.setIP(IP);
			conn.setPort(Port);
			if (Port == 27017) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initOwner(main.getPrimaryStage());
				alert.setTitle("Connection!");
				alert.setHeaderText("MongoDB가 연결되었습니다.");
				alert.setContentText("IP: " + conn.getIP() + " Port: " + conn.getPort());
				alert.showAndWait();
			} else {
				Alert alert1 = new Alert(AlertType.WARNING);
				alert1.initOwner(main.getPrimaryStage());
				alert1.setTitle("오류 메세지");
				alert1.setHeaderText("DB가 연결되지 않았습니다.");
				alert1.setContentText("IP와 Port를 재입력하세요.");
				alert1.showAndWait();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 경로 추가
	@FXML
	private void pathAction() {
		path path = new path("");
		performance perform = new performance("", "", "", "");
		int returnValue = main.setpathDataView(path, perform);
		if (returnValue == 1) {
			main.getPathList().add(path);
			startThread();
		}
	}

	// 경로 수정
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

	// 경로 삭제
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
			alert.setContentText("삭제할 path를 선택해주세요.");
			alert.showAndWait();
		}
	}

	// 강제종료
	@FXML
	private void exitAction() {
		mongoThread mTh;
		try {
			for (int i = 0; i < threadList.size(); i++) {
				mTh = (mongoThread) threadList.get(i);
				mTh.mongoClose();

				Thread.sleep(1000);
				mTh.interrupt();
				System.out.println("Thread 종료");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
