package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


	public class WordMainController {
	@FXML
	private TableView<Word> wordTable;
	@FXML
	private TableColumn<Word, String> now;
	@FXML
	private TableColumn<Word, String> next;
	@FXML
	private TableView<Load> loadTable;
	@FXML
	private TableColumn<Load, String> filename;
	@FXML
	private TableColumn<Load, String> filepath;
	@FXML
	private TableColumn<Load, String> content;
	@FXML
	private TableColumn<Load, String> time;
	private Main main;
	private ObservableList<Load> loadList = FXCollections.observableArrayList();
	private int checkNumber;
	private List<WatchServiceThread> threadList;
	public static String textData = "C:\\AMlog\\";
	@FXML
	private void initialize() {
		now.setCellValueFactory(cellData -> cellData.getValue().getNowProperty());
		next.setCellValueFactory(cellData -> cellData.getValue().getNextProperty());
		filename.setCellValueFactory(cellData -> cellData.getValue().getFilenameProperty());
		filepath.setCellValueFactory(cellData -> cellData.getValue().getFilepathProperty());
		content.setCellValueFactory(cellData -> cellData.getValue().getContentProperty());
		time.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());
	}
	public void setMain(Main main) {
		this.main = main;
		wordTable.setItems(main.getWordList());
		loadTable.setItems(loadList);
	}
	
	public WordMainController() {     }	
	@FXML
	private void addAction() {  // 추가 버튼 활성화
		Word word = new Word("","");
		int returnValue = main.setWordDataView(word);
		if(returnValue ==1) {
			main.getWordList().add(word);
			
			File filter = new File(textData); 
			if (!filter.exists()) {
				filter.mkdir();
			} else {
				BufferedWriter fw;
				try {
					fw = new BufferedWriter(new FileWriter(textData+"파일경로.txt",true));
					fw.append("현재경로 : "+word.getNow()+",  이동경로 : "+word.getNext()+"\r\n");
					fw.flush();
					fw.close();										
				} catch (IOException e) {	
					e.printStackTrace();
				}
			}	
		}
	}
	@FXML
	private void editAction() { // 수정 버튼 활성화
		Word word = wordTable.getSelectionModel().getSelectedItem();
		if (word != null) {
			main.setWordDataView(word);		
		}
	}
	@FXML
	private void deleteAction() { //삭제 버튼 활성화
		int selectedIndex = wordTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >=0) {
			
			// 1. value_Path_all문자에서 selectedIndex에 해당하는 문자를 찾는다. 
			// 2. this.main.value_Path_all.indexOf("")로 찾는다.
			// 3. this.main.value_Path_all.replace("C:\\test|C:\\test2", "");
			String path1 = now.getCellData(selectedIndex)+"|"+next.getCellData(selectedIndex);
			
			if(this.main.value_Path_all.indexOf(selectedIndex) == 0)
			{
				this.main.value_Path_all.replace(now.getCellData(selectedIndex)+"[|]"+next.getCellData(selectedIndex), "");
				
				if(this.main.value_Path_all.length() > 0) {
					// 맨앞에 있는 "|"빼고 다음부터 저장.
					this.main.value_Path_all = this.main.value_Path_all.substring(1);
				}
			} else {
				// 중간에 포함된 경로이기 때문에 앞에 "|"를 붙이고 같이 삭제.
				this.main.value_Path_all.replace(now.getCellData(selectedIndex)+"[|]"+next.getCellData(selectedIndex)," ");
			}
//			this.main.settings.put(this.main.value_Path_all,"");
			System.out.println(path1);
			System.out.println("메인 :"+main.value_Path_all);
			System.out.println("셀렉"+selectedIndex);

			wordTable.getItems().remove(selectedIndex);
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류 메시지");
			alert.setHeaderText("선택 오류가 발생했습니다.");
			alert.setContentText("삭제 할 메뉴를 선택해주세요");
			alert.showAndWait();
		}
	}
	public ObservableList<Load> getLoadList() {
		return loadList;
	}
	
	@FXML
	private void startAction() { // 액션 시작 버튼
		if(main.wordList().size()==0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류 메시지");
			alert.setHeaderText("지정된 경로가 없습니다.");
			alert.setContentText("경로를 추가해주세요");
			alert.showAndWait();
		}else if( checkNumber>1 ) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류 메시지");
			alert.setHeaderText("오류가 발생 했습니다");
			alert.setContentText("중지 버튼을 누른 후 실행해 주세요");
			alert.showAndWait();
		}else {
			checkNumber=2;
		WatchServiceThread wst;
		// thread 저장 ArrayList
		threadList = new ArrayList<WatchServiceThread>();
		
		for (int iCount = 0; iCount < Main.wordList.size(); iCount++) {
			// Thread 생성
			wst = new WatchServiceThread(now.getCellData(iCount), next.getCellData(iCount));
			// Thread 시작
			wst.start();
			
			// Thread 실행 후 ArrayList에 저장.
			threadList.add(wst);
		}
		}	
	}
	
	@FXML
	private void stopAction() { // 액션 정지버튼
		WatchServiceThread wst;
		checkNumber=0;
		// Stop 명령을 실행할 때 실행중인 Thread 모두 종료.
		for (int iCount = 0; iCount < threadList.size(); iCount++) {
			// Thread 생성
			wst = (WatchServiceThread)threadList.get(iCount);
			// Watch Service 닫기
			wst.watchServiceClose();
			// Thread 종료			
			wst.interrupt();
		}	
		System.out.println("stopAction()... 실행중인 쓰레드 모두 종료.");
	}
	// private 변수인 loadList에 String을 추가하는 함수.
	// WatchServerThread 클래스에서 참조해서 사용
	public void addRowInloadList(String fileName, String dirPath, String action, String date,String logtime){	
		loadList.add(new Load(fileName, dirPath, action, date));
				
		try { // 로그 저장
				BufferedWriter fw = new BufferedWriter(new FileWriter(textData+logtime+".txt",true));
				fw.append(fileName+", "+dirPath+", "+action+", "+date+"\r\n");
				fw.flush();
				fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		}
}