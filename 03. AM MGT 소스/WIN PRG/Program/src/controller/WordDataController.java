package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class WordDataController {

@FXML
private TextField nowField; // ������
@FXML
private TextField nextField; // ���� ���

private Stage dialogStage;
private Word word;
private int returnValue = 0;
public static String[][] data;

@FXML
private void initialize() {
}
  public void setDialogStage(Stage dialogStage) { // ���̾�α� Stage�� ����
	  this.dialogStage = dialogStage;
  }
  public void setWord(Word word) { // �ϳ��� �ܾ �����Ѵ�
	  this.word = word;
	  nowField.setText(word.getNow());
	  nextField.setText(word.getNext());
  }
  public int getReturnValue() { // �̺�Ʈ�� ��ġ�� ���� ��ȯ�ϴ� �Լ�
	  return returnValue;
  }
  @FXML
  private void confirmAction() { //�Է��� ���� ��Ȯ���� �ľ�, ��Ȯ�� ��� ��ȯ ���� 1�� �ְ� ���̾�α׸� �ݴ´�.
	  if(valid()) {
		  word.setNow(nowField.getText());
		  word.setNext(nextField.getText());
		  
		  for(int i=0; i<=100; i++) {
			  
		  }
		  returnValue=1;
		  dialogStage.close();
	  }
  }
  @FXML
  private void cancelAction() { //���̾�α� â�� �ݴ´�.
	  dialogStage.close();
 }
  @FXML
  private void PathAction1() { // ���� ��� ����
		DirectoryChooser dc1 = new DirectoryChooser();
		File selectedDc1 = dc1.showDialog(dialogStage);
		String selectedDcPath1 = selectedDc1.getPath();
		
		nowField.setText(selectedDcPath1);
}
  @FXML
	private void PathAction2() {
		DirectoryChooser dc2 = new DirectoryChooser();
		File selectedDc2 = dc2.showDialog(dialogStage);
		String selectedDcPath2 = selectedDc2.getPath();
		nextField.setText(selectedDcPath2);
		
	}
  
  private boolean valid() {  // ��� �Է°����� Ȯ���� �� ��� ������ ���� �޽��� �߻�
	  String errorMessage = "";
	  if(nowField.getText() ==null || nowField.getText().equals(""))  {
		  errorMessage += "�����θ� �Է��ϼ���. \n";
	  }
	  if(nextField.getText() ==null || nextField.getText().equals("")) {
		  errorMessage += "�̵���θ� �Է��ϼ���. \n";
	  }
	  if(!nowField.getText().contains("\\")) {
		  errorMessage += "�����θ� ���� �Է��� �ּ��� \n" ;
	  }if(!nextField.getText().contains("\\")) {
		  errorMessage += "�̵���θ� ���� �Է��� �ּ��� \n";
	  }if(errorMessage.equals("")) {
		  return true;
	  }
	  	
	  else {
		  Alert alert = new Alert(AlertType.ERROR);
		  alert.initOwner(dialogStage);
		  alert.setTitle("���� �޽���");
		  alert.setHeaderText("���� ����� �Է����ּ���.");
		  alert.setContentText(errorMessage);
		  alert.showAndWait();
		  return false;
	  }
  }
}
