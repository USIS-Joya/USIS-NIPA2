package controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.path;
import model.performance;

public class pathController {
	@FXML
	private TextField pathField;
	private int returnValue = 0;
	private Stage dialogStage;

	private path path;
	private performance perform;

	@FXML
	private void initialize() {
	}

	// 다이얼로그 무대(Stage)를 설정합니다.
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setPerform(performance perform) {
		this.perform = perform;
	}

	// 하나의 경로를 저장합니다.
	// 180525
	public void setPath(path path) {
		this.path = path;
		pathField.setText(path.getPath());
	}

	// 이벤트를 마치고 값을 반환하는 함수입니다.
	public int getReturnValue() {
		return returnValue;
	}

	// 현재 사용자가 입력한 값이 정확한 지 파악하고 정확한 경우 반환값에 1을 넣어주고 다이얼로그를 닫습니다.
	@FXML
	private void confirmAction() {
		File clsFolder = new File(pathField.getText());
		// getFileName - 180524
		if (isCorrect()) {
			path.setPath(pathField.getText());
			if (clsFolder.exists() == false) {
				System.out.println("folder is not found");
			} else {
				File[] arrFile = clsFolder.listFiles();
				perform.setFilePath(pathField.getText());
				SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Calendar cal = Calendar.getInstance();
				for (int i = 0; i < arrFile.length; ++i) {
					perform.setFileName(arrFile[i].getName());
					perform.setStart(dataFormat.format(cal.getTime()));
				}
			}
			returnValue = 1;
			dialogStage.close();
		}
	}

	// 다이얼로그 창을 닫습니다.
	@FXML
	private void cancelAction() {
		dialogStage.close();
	}

	// 모든 입력 값들을 확인한 뒤에 비어있으면 오류메세지를 띄우고 비어있지 않으면 성공적으로 True값을 반환
	private boolean isCorrect() {
		String errorMessage = "";
		if (pathField.getText() == null || pathField.getText().equals("")) {
			errorMessage += "경로를 입력하세요.\n";
		}
		if (errorMessage.equals("")) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("오류 메세지");
			alert.setHeaderText("정확한 경로를 입력해주세요.");
			alert.setContentText(errorMessage);
			alert.showAndWait();

			return false;
		}
	}

}
