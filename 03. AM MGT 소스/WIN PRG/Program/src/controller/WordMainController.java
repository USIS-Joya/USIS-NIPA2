package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.List;



import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
	
	private int i;
	private String today;
	

	private boolean started;
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
	public WordMainController() {
		
		
		
	}
	
	
	@FXML
	private void addAction() {  // 추가 버튼 활성화
		Word word = new Word("","");
		int returnValue = main.setWordDataView(word);
		if(returnValue ==1) {
			main.getWordList().add(word);
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
			wordTable.getItems().remove(selectedIndex);
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("오류 메시지");
			alert.setHeaderText("선택 오류가 발생했습니다.");
			alert.setContentText("삭제 할 메뉴를 선태해주세요");
			alert.showAndWait();
		}
	}
	public ObservableList<Load> getLoadList() {
		return loadList;
	}
	
	@FXML
	private void startAction(ActionEvent a)  { // 액션 시작 버튼
		
//		for(i=0; Main.wordList.size()<i; i++) {
		
		started = true;
		Thread thread = new Thread() {
			
			@Override
			public void run() {
			try {
				WatchService watchService = FileSystems.getDefault().newWatchService();
                Path directory = Paths.get(now.getCellData(i));
                Path outpath = Paths.get(next.getCellData(i));
                directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                
                
                System.out.println(Main.wordList.size()); // WordList 사이즈
                System.out.println(now.getCellData(0)); // 테이블 데이터 보는 법
                System.out.println(next.getCellData(0)); // 테이블 데이터 보는 법
                
                
                
                
                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss"); // 시간 데이터 ( 년 - 월 - 일 - 시간 - 분 - 초 )
                
                
                while (started) {
                	 WatchKey watchKey = watchService.take();
                     List<WatchEvent<?>> list = watchKey.pollEvents();
                     
                     for (WatchEvent<?> watchEvent : list) {
                         Kind<?> kind = watchEvent.kind();
                         Path path = (Path) watchEvent.context();
                         
                   
                         Calendar cal = Calendar.getInstance();
                 		today = formatter.format(cal.getTime());  // 현재시간 나타내는 표
                 		
                        if(path.toString().contains("~")) {
                        }else if(path.toString().contains("$")){
                       }else if(path.toString().contains(".BAK")) { //  contains = 관련된 문자가 있을 시 처리
                     }else {
                    	 if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                             Platform.runLater(() -> loadList.add(new Load(path.toString(),directory.toString(),"생성",today)));
                             
                             
                             try {   // 파일 복사 try
                            	 FileInputStream inFile = new FileInputStream(directory+"\\"+path);
                            	 FileOutputStream outFile = new FileOutputStream(outpath+"\\"+path);
                            	                             	 
                            	 
                            	 File filter = new File(outFile.toString()); // 복사 경로 이상 시 에러발생을 막기위한 조치
                            	 if(!filter.exists()) {
                            		 filter.mkdir();
                            	 }else {
                            		 File[] destory = filter.listFiles();
                            		 for(File des : destory) {
                            			 des.delete();
                            		 }
                            	 }
                            	 int data = 0;
                            	 while((data=inFile.read())!=-1) {
                            	outFile.write(data);	 
                            	 }
                            	 inFile.close();
                            	 outFile.close();
                             }catch(IOException e) {
                        		 e.printStackTrace();
                        	 }


                             
                         } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                             Platform.runLater(() -> loadList.add(new Load(path.toString(),directory.toString(),"삭제",today)));
                             
                            File delete = new File(outpath+"\\"+path.toString()); // 복사 경로 이상 시 에러발생을 막기위한 조치 
                            
                            delete.delete(); // 파일 삭제

                         } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                             Platform.runLater(() -> loadList.add(new Load(path.toString(),directory.toString(),"수정",today)));
                            
                             
                             try { // 파일 복사 try
                            	 FileInputStream inFile = new FileInputStream(directory+"\\"+path);
                            	 FileOutputStream outFile = new FileOutputStream(outpath+"\\"+path);
                            	 
                            	 
                            	 File filter = new File(outFile.toString());  // 복사 경로 이상 시 에러발생을 막기위한 조치
                            	 if(!filter.exists()) {
                            		 filter.mkdir();
                            	 }else {
                            		 File[] destory = filter.listFiles();
                            		 for(File des : destory) {
                            			 des.delete();
                            		 }
                            	 }
                            	 
                            	 int data = 0;
                            	 while((data=inFile.read())!=-1) {
                            	outFile.write(data);	 
                            	 }
                            	 inFile.close();
                            	 outFile.close();
                             }catch(IOException e) {
                        		 e.printStackTrace();
                        	 }
                         } else if (kind == StandardWatchEventKinds.OVERFLOW) {
                        	 
                         }
                         
                     }
                     }
                     try {
                    	 Thread.sleep(10);
                     }catch(Exception b) {
                    	 b.printStackTrace();
                     }
  
                     boolean valid = watchKey.reset();
  
                     if (!valid) {
                         break;
                     }
                }
			}catch (Exception e) {
				e.printStackTrace();
			}
			}
		};
		thread.setDaemon(true);
		thread.start();
		}
//		}
					
	

	@FXML
	private void stopAction() { // 액션 정지버튼
		started = false;	
		}
	
	
	
}
