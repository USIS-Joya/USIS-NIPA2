package controller;


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
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		today = formatter.format(cal.getTime());  // 현재시간 나타내는 표
		
		
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
			alert.setContentText("삭제할 메뉴를 선택해주세요.");
			alert.showAndWait();
		}
	}
	public ObservableList<Load> getLoadList() {
		return loadList;
	}
	
	@FXML
	private void startAction(ActionEvent a)  {
		
		
//		String id1= "테스트1";
//		String id2= "테스트2";
//		String id3= "테스트3";
//		loadList.add(new Load(id1,id2,id3,today));
		
		started = true;
		Thread thread = new Thread() {
			
			@Override
			public void run() {
			try {
				WatchService watchService = FileSystems.getDefault().newWatchService();
                Path directory = Paths.get("C:\\test");
                directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                
                
                while (true) {
                	 WatchKey watchKey = watchService.take();
                     List<WatchEvent<?>> list = watchKey.pollEvents();
                     
                     for (WatchEvent<?> watchEvent : list) {
                         Kind<?> kind = watchEvent.kind();
                         Path path = (Path) watchEvent.context();
                         if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                             Platform.runLater(() -> loadList.add(new Load("테스트","테스트","테스트",today)));
                         } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                             Platform.runLater(() -> loadList.add(new Load("테스트","테스트","테스트",today)));
                         } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                             Platform.runLater(() -> loadList.add(new Load("테스트","테스트","테스트",today)));
                         } else if (kind == StandardWatchEventKinds.OVERFLOW) {
  
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
	
	@FXML
	private void stopAction() {
		System.exit(1);
	}
	
	
}
