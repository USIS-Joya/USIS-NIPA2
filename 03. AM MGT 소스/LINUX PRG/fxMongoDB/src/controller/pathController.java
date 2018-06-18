package controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
	private performance perform;

	@FXML
	private void initialize() {
	}

	// ���̾�α� ����(Stage)�� �����մϴ�.
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setPerform(performance perform) {
		this.perform = perform;
	}

	// �ϳ��� ��θ� �����մϴ�.
	// 180525
	public void setPath(path path) {
		this.path = path;
		pathField.setText(path.getPath());
	}

	// �̺�Ʈ�� ��ġ�� ���� ��ȯ�ϴ� �Լ��Դϴ�.
	public int getReturnValue() {
		return returnValue;
	}

	// ���� ����ڰ� �Է��� ���� ��Ȯ�� �� �ľ��ϰ� ��Ȯ�� ��� ��ȯ���� 1�� �־��ְ� ���̾�α׸� �ݽ��ϴ�.
	@FXML
	private void confirmAction() {
		// getFileName - 180524
		if (isCorrect()) {
			path.setPath(pathField.getText());
			returnValue = 1;
			dialogStage.close();
		}
	}

	// ���̾�α� â�� �ݽ��ϴ�.
	@FXML
	private void cancelAction() {
		dialogStage.close();
	}

	// ��� �Է� ������ Ȯ���� �ڿ� ��������� �����޼����� ���� ������� ������ ���������� True���� ��ȯ
	private boolean isCorrect() {
		String errorMessage = "";
		if (pathField.getText() == null || pathField.getText().equals("") || !pathField.getText().contains("\\")) {
			errorMessage += "��θ� �Է��ϼ���.\n";
		}
		if (errorMessage.equals("")) {
			return true;
			
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("���� �޼���");
			alert.setHeaderText("��Ȯ�� ��θ� �Է����ּ���.");
			alert.setContentText(errorMessage);
			alert.showAndWait();

			return false;
		}
	}

}
