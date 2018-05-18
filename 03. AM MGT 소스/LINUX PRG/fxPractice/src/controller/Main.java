package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connection;
import model.management;
import model.path;
import model.performance;

public class Main extends Application {
	private Main main;
	private Stage primaryStage;
	// private static String value = "";
	// private BorderPane rootLayout;

	// 수행작업 Tab- Performance Table - 180515 CWJ
	private ObservableList<performance> performList = FXCollections.observableArrayList();
	private performance perform;
	@FXML
	private TableView<performance> performTable;
	@FXML
	private TableColumn<performance, String> p_FileName;
	@FXML
	private TableColumn<performance, String> p_FilePath;
	@FXML
	private TableColumn<performance, String> p_Start;
	@FXML
	private TableColumn<performance, String> p_Progress;
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

	public ObservableList<path> getPathList() {
		// try {
		// pathList.add(path);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
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
		pathList.add(new path("D:\\Usis-AM-Data-Extract"));
	}

	@FXML
	private void initialize() {

		/// m_FileName,m_FilePath,m_Start,m_End
		p_FileName.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		p_FilePath.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		p_Start.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		p_Progress.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());

		m_FileName.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		m_FilePath.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		m_Start.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());
		m_End.setCellValueFactory(cellData -> cellData.getValue().getFilePathProperty());

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
		// pathController controller = new pathController();
		// controller.getPathList().add(path);
		performTable.setItems(main.getPerformList());
		manageTable.setItems(main.getManagementList());
		pathTable.setItems(main.getPathList());
	}

	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Test");
		// setRootLayout();
		setMainView();
	}

	// 매개변수로 넘어온 하나의 단어 데이터를 이용해서 path관리화면을 띄웁니다.
	// 이후에 종료되고 넘어온 반환 값을 그대로 다시 한 번 반환
	public int setpathDataView(path path) {
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
		int returnValue = main.setpathDataView(path);

		if (returnValue == 1) {
			main.getPathList().add(path);
			// pathController controller = new pathController();
			// controller.getPathList().add(path);
		}
	}

	// 수정
	@FXML
	private void modifyAction() {
		path path = pathTable.getSelectionModel().getSelectedItem();
		if (path != null) {
			main.setpathDataView(path);
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
			conn.setPort(Port);

			//
			System.out.println("MongoDB IP : " + conn.getIP().toString());
			System.out.println("MongoDB PORT : " + conn.getPort());
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean isConn = false;
		try {
			Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
			mongoLogger.setLevel(Level.SEVERE);
			Connection conn = new Connection();
			//db연결 및 insert
			conn.DBConnection();
			isConn = true;
			if (isConn) {
				System.out.println("MongoDB연결");

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
