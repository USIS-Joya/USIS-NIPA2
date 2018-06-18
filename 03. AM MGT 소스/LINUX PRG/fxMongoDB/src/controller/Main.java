package controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.CellTask;
import model.Connection;
import model.management;
import model.mongoThread;
import model.path;
import model.performance;

public class Main extends Application {
	public static Main main;
	private Stage primaryStage;
	private CellTask cellTask;
	private boolean isStart = false;
	public Preferences settings;
	private String today = "";
	private String preEventDate = "";
	// **************************************
	public final String node_Name = "Settings";
	public double value_window_size_X = 0.0;
	public double value_window_size_Y = 0.0;
	public final String window_size_X = "WINDOW_SIZE_X";
	public final String window_size_Y = "WINDOW_SIZE_Y";
	// **************************************

	// 수행작업 Tab- Performance Table - 180515 CWJ
	private ObservableList<performance> performList = FXCollections.observableArrayList();
	private performance perform = new performance();

	@FXML
	private TableView<performance> performTable = new TableView<performance>();
	//
	ProgressBar bar = new ProgressBar();
	private List<mongoThread> threadList;

	// 화면관리 Tab
	private ObservableList<management> manageList = FXCollections.observableArrayList();
	private management manage = new management();
	@FXML
	private TableView<management> manageTable = new TableView<management>();
	@FXML
	private TableColumn<management, String> m_FileName;
	@FXML
	private TableColumn<management, String> m_FilePath;
	@FXML
	private TableColumn<management, String> m_Start;
	@FXML
	private TableColumn<management, String> m_End;
	//
	// 환경설정 Tab
	private path path;
	@FXML
	private TableColumn<path, String> folderName;
	@FXML
	private TableView<path> pathTable;
	public ObservableList<path> pathList = FXCollections.observableArrayList();
	@FXML
	private TextField txt_ip;
	@FXML
	private TextField txt_port;

	public static void main(String[] args) {
		try {
			Main main = new Main();
			main.startThread();

		} catch (Exception e) {
			e.printStackTrace();
		}
		launch(args);
	}

	public Main() {
	}

	@FXML
	private void initialize() {
		TableColumn<performance, String> name = new TableColumn<>("파일명");
		name.setCellValueFactory(cellData -> cellData.getValue().getFileNameProperty());
		name.setPrefWidth(250);

		TableColumn<performance, String> path = new TableColumn<>("파일경로");
		path.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		path.setPrefWidth(120);

		TableColumn<performance, String> start = new TableColumn<>("시작");
		start.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
		start.setPrefWidth(150);

		performTable.getColumns().addAll(name, path, start, addCell());

		m_FileName.setCellValueFactory(cellData -> cellData.getValue().getFileNameProperty());
		m_FilePath.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		m_Start.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
		m_End.setCellValueFactory(cellData -> cellData.getValue().getEndProperty());

		folderName.setCellValueFactory(cellData -> cellData.getValue().getPathProperty());

	}

	public ObservableList<path> getPathList() {
		return pathList;
	}

	public ObservableList<management> getManageList() {
		return manageList;
	}

	public TableColumn<performance, ProgressBar> addCell() {
		TableColumn<performance, ProgressBar> progress = new TableColumn<>("진행률");
		progress.setCellValueFactory(
				new Callback<CellDataFeatures<performance, ProgressBar>, ObservableValue<ProgressBar>>() {
					@Override
					public ObservableValue<ProgressBar> call(CellDataFeatures<performance, ProgressBar> arg0) {
						// performance task = arg0.getValue();
						cellTask = new CellTask();
						bar.progressProperty().bind(cellTask.progressProperty());
						bar.setPrefWidth(155);
						// bar.progressProperty().bind(task.progressProperty()); //2
						return new SimpleObjectProperty<ProgressBar>(bar);
					}
				});
		new Thread(cellTask).start();

		progress.setPrefWidth(630);
		return progress;
	}

	// thread
	public void startThread() {
		try {
			mongoThread mTh;
			threadList = new ArrayList<mongoThread>();

			for (int count = 0; count < pathList.size(); count++) {
				File clsFolder = new File(folderName.getCellData(count));
				File[] arrFile = clsFolder.listFiles();

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				// 시간 데이터 (2018 - 01 - 01 - 11 - 11 - 11)
				Calendar cal = Calendar.getInstance();

				mTh = new mongoThread(folderName.getCellData(count));

				for (int i = 0; i < arrFile.length; i++) {
					today = formatter.format(cal.getTime());

					performList.add(new performance(arrFile[i].getName(), clsFolder.getPath(), today));
					preEventDate = today;
					manageList.add(new management(arrFile[i].getName(), clsFolder.getPath(), preEventDate, ""));

				}
				addCell();

				mTh.start();
				isStart = true;
				threadList.add(mTh);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 현재의 메인 스테이지를 반환합니다.
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setMain(Main main) {
		this.main = main;
		getPathList().add(path);
		pathTable.setItems(main.getPathList());
		manageTable.setItems(manageList);

	}

	public void addRowInManageList(String fileName, String dirPath, String action, String date) {
		manageList.add(new management(fileName, dirPath, action, date));
	}

	@Override
	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Test");

		Preferences prefsRoot = Preferences.userRoot();
		settings = prefsRoot.node("Setting");
		value_window_size_X = settings.getDouble(window_size_X, 600.0);
		value_window_size_Y = settings.getDouble(window_size_Y, 400.0);
		setMainView();
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
				// System.out.println("Height: " + primaryStage.getHeight() + " Width: " +
				// primaryStage.getWidth());
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

	@FXML
	private void displayValue(ActionEvent event) {
		try {
			Connection conn = new Connection();

			conn.setIP(txt_ip.getText());
			String str_port = txt_port.getText();
			int Port = Integer.parseInt(str_port);
			conn.setPort(Port);

			//
			System.out.println("MongoDB IP : " + conn.getIP().toString());
			System.out.println("MongoDB PORT : " + conn.getPort());
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void pathAction() {
		path path = new path("");
		performance perform = new performance("", "", "");
		int returnValue = main.setpathDataView(path, perform);
		if (returnValue == 1) {
			main.getPathList().add(path);
			startThread();
			performTable.setItems(performList);

			manageTable.setItems(manageList);

		}
	}

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
			alert.setContentText("삭제할 path를 선택해주세요.");
			alert.showAndWait();
		}
	}

	// 강제종료
	@FXML
	private void exitAction() {
		try {
			mongoThread mTh;
			if (isStart == true) {
				for (int i = 0; i < threadList.size(); i++) {
					mTh = (mongoThread) threadList.get(i);
					mTh.mongoClose();
					mTh.interrupt();
				}
				System.out.println("Thread 종료");
			}
			isStart = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
