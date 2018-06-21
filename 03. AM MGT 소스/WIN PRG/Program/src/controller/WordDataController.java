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

private Main main;
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
  public void SetMainApplication(Main main) {
	  this.main = main;
  }
  @FXML
  private void confirmAction() { //�Է��� ���� ��Ȯ���� �ľ�, ��Ȯ�� ��� ��ȯ ���� 1�� �ְ� ���̾�α׸� �ݴ´�.
	  if(valid()) {
		  word.setNow(nowField.getText());
		  word.setNext(nextField.getText());
		  /**********************************************************************
		   * 1. Preferences�� ����Ǿ� �ִ� Path������ �����´�. 
		   **********************************************************************/
		  String all_path = this.main.settings.get(this.main.stringPathName, "");
//		  System.out.println("Get Settings - All Path: " + all_path);
//		  System.out.println(this.main.stringPathName);
	     /**********************************************************************
	      * 2. all_path�� ���� ������ Orgin, Dest ��θ� �߰��� �ش�. 
	      **********************************************************************/
		  if (all_path.length() > 0) {
				// �̹� ���ڰ� ������ "|"�� �ٿ��ش�.
				all_path += "|" + nowField.getText() + "|" + nextField.getText();
			} else {
				// ó���̸� "|"���� �߰����� �׳� �Է��Ѵ�. 
				all_path = nowField.getText() + "|" + nextField.getText();
			}
	      /**********************************************************************
	       * 3. �߰��� all_path�� Settings�� �ٽ� �����Ѵ�. 
	       **********************************************************************/
		  this.main.settings.put(this.main.stringPathName,all_path);
//		  System.out.println("Set Settings - All Path: " + all_path);
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
  private void PathAction2() { // ���� ��� ����
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
