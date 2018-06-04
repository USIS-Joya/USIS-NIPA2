package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	public static String textData = "C:\\AMlog\\";

	
	
	private List<WatchServiceThread> threadList;
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
	private void addAction() {  // �߰� ��ư Ȱ��ȭ
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
					fw = new BufferedWriter(new FileWriter(textData+"���ϰ��.txt",true));
					fw.append("������ : "+word.getNow()+",  �̵���� : "+word.getNext()+"\r\n");
					fw.flush();
					fw.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				
				
			}	
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
	private void startAction(ActionEvent a) { // �׼� ���� ��ư
		WatchServiceThread wst;
		
		// thread ���� ArrayList
		threadList = new ArrayList<WatchServiceThread>();
		
		for (int iCount = 0; iCount < Main.wordList.size(); iCount++) {
			// Thread ����
			wst = new WatchServiceThread(now.getCellData(iCount), next.getCellData(iCount));
			// Thread ����
			wst.start();
			
			// Thread ���� �� ArrayList�� ����.
			threadList.add(wst);
		}
	}
	
	@FXML
	private void stopAction() { // �׼� ������ư
		WatchServiceThread wst;
		
		// Stop ����� ������ �� �������� Thread ��� ����.
		for (int iCount = 0; iCount < threadList.size(); iCount++) {
			// Thread ����
			wst = (WatchServiceThread)threadList.get(iCount);
			// Watch Service �ݱ�
			wst.watchServiceClose();
			// Thread ����			
			wst.interrupt();
		}	
		System.out.println("stopAction()... �������� ������ ��� ����.");
	}
	// private ������ loadList�� String�� �߰��ϴ� �Լ�.
	// WatchServerThread Ŭ�������� �����ؼ� ���
	public void addRowInloadList(String fileName, String dirPath, String action, String date,String logtime){	
		loadList.add(new Load(fileName, dirPath, action, date));
		
		
		try { // �α� ����
			
				
				BufferedWriter fw = new BufferedWriter(new FileWriter(textData+logtime+".txt",true));
				fw.append(fileName+", "+dirPath+", "+action+", "+date+"\r\n");
				fw.flush();
				fw.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		}
}
