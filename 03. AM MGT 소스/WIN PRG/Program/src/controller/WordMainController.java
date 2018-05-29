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
	private void addAction() {  // �߰� ��ư Ȱ��ȭ
		Word word = new Word("","");
		int returnValue = main.setWordDataView(word);
		if(returnValue ==1) {
			main.getWordList().add(word);
		}
	}
	@FXML
	private void editAction() { // ���� ��ư Ȱ��ȭ
		Word word = wordTable.getSelectionModel().getSelectedItem();
		if (word != null) {
			main.setWordDataView(word);
		}
	}
	@FXML
	private void deleteAction() { //���� ��ư Ȱ��ȭ
		int selectedIndex = wordTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >=0) {
			wordTable.getItems().remove(selectedIndex);
		}else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("���� �޽���");
			alert.setHeaderText("���� ������ �߻��߽��ϴ�.");
			alert.setContentText("���� �� �޴��� �������ּ���");
			alert.showAndWait();
		}
	}
	public ObservableList<Load> getLoadList() {
		return loadList;
	}
	
	@FXML
	private void startAction(ActionEvent a)  { // �׼� ���� ��ư
		
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
                
                
                System.out.println(Main.wordList.size()); // WordList ������
                System.out.println(now.getCellData(0)); // ���̺� ������ ���� ��
                System.out.println(next.getCellData(0)); // ���̺� ������ ���� ��
                
                
                
                
                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss"); // �ð� ������ ( �� - �� - �� - �ð� - �� - �� )
                
                
                while (started) {
                	 WatchKey watchKey = watchService.take();
                     List<WatchEvent<?>> list = watchKey.pollEvents();
                     
                     for (WatchEvent<?> watchEvent : list) {
                         Kind<?> kind = watchEvent.kind();
                         Path path = (Path) watchEvent.context();
                         
                   
                         Calendar cal = Calendar.getInstance();
                 		today = formatter.format(cal.getTime());  // ����ð� ��Ÿ���� ǥ
                 		
                        if(path.toString().contains("~")) {
                        }else if(path.toString().contains("$")){
                       }else if(path.toString().contains(".BAK")) { //  contains = ���õ� ���ڰ� ���� �� ó��
                     }else {
                    	 if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                             Platform.runLater(() -> loadList.add(new Load(path.toString(),directory.toString(),"����",today)));
                             
                             
                             try {   // ���� ���� try
                            	 FileInputStream inFile = new FileInputStream(directory+"\\"+path);
                            	 FileOutputStream outFile = new FileOutputStream(outpath+"\\"+path);
                            	                             	 
                            	 
                            	 File filter = new File(outFile.toString()); // ���� ��� �̻� �� �����߻��� �������� ��ġ
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
                             Platform.runLater(() -> loadList.add(new Load(path.toString(),directory.toString(),"����",today)));
                             
                            File delete = new File(outpath+"\\"+path.toString()); // ���� ��� �̻� �� �����߻��� �������� ��ġ 
                            
                            delete.delete(); // ���� ����

                         } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                             Platform.runLater(() -> loadList.add(new Load(path.toString(),directory.toString(),"����",today)));
                            
                             
                             try { // ���� ���� try
                            	 FileInputStream inFile = new FileInputStream(directory+"\\"+path);
                            	 FileOutputStream outFile = new FileOutputStream(outpath+"\\"+path);
                            	 
                            	 
                            	 File filter = new File(outFile.toString());  // ���� ��� �̻� �� �����߻��� �������� ��ġ
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
	private void stopAction() { // �׼� ������ư
		started = false;	
		}
	
	
	
}
