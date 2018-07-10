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

	// *****************ȯ�漳��*********************
	public Preferences settings;
	public final String node_Name = "Settings";
	public double value_window_size_X = 0.0;
	public double value_window_size_Y = 0.0;
	public final String window_size_X = "WINDOW_SIZE_X";
	public final String window_size_Y = "WINDOW_SIZE_Y";
	// *******************************************

	// �����۾� Tab- Performance Table - 180515 CWJ
	@FXML
	private TableView<performance> performTable = new TableView<performance>();
	public ObservableList<performance> performList = FXCollections.observableArrayList();
	private performance perform = new performance();

	private List<mongoThread> threadList;

	// ȭ����� Tab- Management Table - 180515 CWJ
	@FXML
	private TableView<management> manageTable = new TableView<management>();
	public ObservableList<management> manageList = FXCollections.observableArrayList();
	private management manage = new management();

	//
	// ȯ�漳�� Tab- Path Table - 180515 CWJ
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
		TableColumn<performance, String> name = new TableColumn<>("���ϸ�");
		name.setCellValueFactory(cellData -> cellData.getValue().getFileNameProperty());
		name.setPrefWidth(300);

		TableColumn<performance, String> path = new TableColumn<>("���ϰ��");
		path.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		path.setPrefWidth(120);

		TableColumn<performance, String> start = new TableColumn<>("����");
		start.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
		start.setPrefWidth(150);

		TableColumn<performance, String> progress = new TableColumn<>("�����");
		progress.setCellValueFactory(cellData -> cellData.getValue().getprogressProperty());
		progress.setPrefWidth(100);

		performTable.getSortOrder();
		performTable.getColumns().addAll(name, path, start, progress);

		TableColumn<management, String> m_name = new TableColumn<>("���ϸ�");
		m_name.setCellValueFactory(cellData -> cellData.getValue().getFileNameProperty());
		m_name.setPrefWidth(250);

		TableColumn<management, String> m_path = new TableColumn<>("���ϰ��");
		m_path.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		m_path.setPrefWidth(120);

		TableColumn<management, String> m_start = new TableColumn<>("����");
		m_start.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
		m_start.setPrefWidth(150);

		TableColumn<management, String> m_End = new TableColumn<>("����");
		m_End.setCellValueFactory(cellData -> cellData.getValue().getEndProperty());
		m_End.setPrefWidth(150);

		performTable.getSortOrder();
		manageTable.getColumns().addAll(m_name, m_path, m_start, m_End);

		folderName.setCellValueFactory(cellData -> cellData.getValue().getPathProperty());

	}

	// ������ ���� ���������� ��ȯ�մϴ�.
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
			dialogStage.setTitle("Folder ã��");
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
			/* Listener ���� */
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

	// DB ����
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
				alert.setHeaderText("MongoDB�� ����Ǿ����ϴ�.");
				alert.setContentText("IP: " + conn.getIP() + " Port: " + conn.getPort());
				alert.showAndWait();
			} else {
				Alert alert1 = new Alert(AlertType.WARNING);
				alert1.initOwner(main.getPrimaryStage());
				alert1.setTitle("���� �޼���");
				alert1.setHeaderText("DB�� ������� �ʾҽ��ϴ�.");
				alert1.setContentText("IP�� Port�� ���Է��ϼ���.");
				alert1.showAndWait();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��� �߰�
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

	// ��� ����
	@FXML
	private void modifyAction() {
		path path = pathTable.getSelectionModel().getSelectedItem();
		if (path != null) {
			main.setpathDataView(path, null);
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("���� �޼���");
			alert.setHeaderText("���� ������ �߻��߽��ϴ�.");
			alert.setContentText("���� �ܾ �������ּ���.");
			alert.showAndWait();
		}
	}

	// ��� ����
	@FXML
	private void deleteAction() {
		int selectedIndex = pathTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			pathTable.getItems().remove(selectedIndex);
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("���� �޼���");
			alert.setHeaderText("���� ������ �߻��߽��ϴ�.");
			alert.setContentText("������ path�� �������ּ���.");
			alert.showAndWait();
		}
	}

	// ��������
	@FXML
	private void exitAction() {
		mongoThread mTh;
		try {
			for (int i = 0; i < threadList.size(); i++) {
				mTh = (mongoThread) threadList.get(i);
				mTh.mongoClose();

				Thread.sleep(1000);
				mTh.interrupt();
				System.out.println("Thread ����");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
