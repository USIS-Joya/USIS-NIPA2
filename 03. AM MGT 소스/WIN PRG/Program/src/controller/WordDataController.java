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
private TextField nowField; // 현재경로
@FXML
private TextField nextField; // 다음 경로

private Stage dialogStage;
private Word word;
private int returnValue = 0;
public static String[][] data;

private Main main;
@FXML
private void initialize() {
}
  public void setDialogStage(Stage dialogStage) { // 다이얼로그 Stage를 설정
	  this.dialogStage = dialogStage;
  }
  public void setWord(Word word) { // 하나의 단어를 저장한다
	  this.word = word;
	  nowField.setText(word.getNow());
	  nextField.setText(word.getNext());
  }
  public int getReturnValue() { // 이벤트를 마치고 값을 반환하는 함수
	  return returnValue;
  }
  public void SetMainApplication(Main main) {
	  this.main = main;
  }
  @FXML
  private void confirmAction() { //입력한 값이 정확한지 파악, 정확한 경우 반환 값에 1을 넣고 다이얼로그를 닫는다.
	  if(valid()) {
		  word.setNow(nowField.getText());
		  word.setNext(nextField.getText());
		  /**********************************************************************
		   * 1. Preferences에 저장되어 있는 Path정보를 가져온다. 
		   **********************************************************************/
		  String all_path = this.main.settings.get(this.main.stringPathName, "");
//		  System.out.println("Get Settings - All Path: " + all_path);
//		  System.out.println(this.main.stringPathName);
	     /**********************************************************************
	      * 2. all_path에 현재 지정된 Orgin, Dest 경로를 추가해 준다. 
	      **********************************************************************/
		  if (all_path.length() > 0) {
				// 이미 문자가 있으면 "|"를 붙여준다.
				all_path += "|" + nowField.getText() + "|" + nextField.getText();
			} else {
				// 처음이면 "|"문자 추가없이 그냥 입력한다. 
				all_path = nowField.getText() + "|" + nextField.getText();
			}
	      /**********************************************************************
	       * 3. 추가된 all_path를 Settings에 다시 저장한다. 
	       **********************************************************************/
		  this.main.settings.put(this.main.stringPathName,all_path);
//		  System.out.println("Set Settings - All Path: " + all_path);
		  returnValue=1;
		  dialogStage.close();
	}
 }
  @FXML
  private void cancelAction() { //다이얼로그 창을 닫는다.
	  dialogStage.close();
 }
  @FXML
  private void PathAction1() { // 폴더 경로 선택
		DirectoryChooser dc1 = new DirectoryChooser();
		File selectedDc1 = dc1.showDialog(dialogStage);
		String selectedDcPath1 = selectedDc1.getPath();
		nowField.setText(selectedDcPath1);
}
  @FXML
  private void PathAction2() { // 폴더 경로 선택
		DirectoryChooser dc2 = new DirectoryChooser();
		File selectedDc2 = dc2.showDialog(dialogStage);
		String selectedDcPath2 = selectedDc2.getPath();
		nextField.setText(selectedDcPath2);
}
  
  private boolean valid() {  // 모든 입력값들을 확인한 뒤 비어 있으면 오류 메시지 발생
	  String errorMessage = "";
	  if(nowField.getText() ==null || nowField.getText().equals(""))  {
		  errorMessage += "현재경로를 입력하세요. \n";
	  }
	  if(nextField.getText() ==null || nextField.getText().equals("")) {
		  errorMessage += "이동경로를 입력하세요. \n";
	  }
	  if(!nowField.getText().contains("\\")) {
		  errorMessage += "현재경로를 재대로 입력해 주세요 \n" ;
	  }if(!nextField.getText().contains("\\")) {
		  errorMessage += "이동경로를 재대로 입력해 주세요 \n";
	  }if(errorMessage.equals("")) {
		  return true;
	  }
	  	
	  else {
		  Alert alert = new Alert(AlertType.ERROR);
		  alert.initOwner(dialogStage);
		  alert.setTitle("오류 메시지");
		  alert.setHeaderText("값을 제대로 입력해주세요.");
		  alert.setContentText(errorMessage);
		  alert.showAndWait();
		  return false;
	  }
  }
}
