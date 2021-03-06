package controller;

import javafx.fxml.FXML;
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
	@SuppressWarnings("unused")
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
		// getFileName - 180524
		if (isCorrect()) {
			path.setPath(pathField.getText());
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
		if (pathField.getText() == null || pathField.getText().equals("") || !pathField.getText().contains("\\")) {
			errorMessage += "경로를 입력하세요.\n";
		}
		for (int index = 0; index < Main.main.pathList.size(); index++) {
			
			if (Main.main.pathList.get(index).getPath().equals(pathField.getText())) {
				errorMessage += "이미 추가된 경로입니다.\n";
			}
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
