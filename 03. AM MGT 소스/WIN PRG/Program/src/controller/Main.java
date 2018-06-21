package controller;
	

import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	public static ObservableList<Word> wordList = FXCollections.observableArrayList();
	public static WordMainController controller;
	public Preferences settings;
	public final String node_Name= "Settings";
	public final String stringPathName = "PATH_ALL";
	public String value_Path_all = null;
	public static String[] splitPath;
	
	public Main() {
		
	}

	
	
	public ObservableList<Word> getWordList() {
		return wordList;
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AM Data Watch Program");
		setRootLayout();
		setWordMainView();
		GetApplicationSettings(node_Name);		
	} 	
	// ��Ʈ ���̾ƿ��� �ʱ�ȭ 
	public void setRootLayout() {
		try {
			//FXML ������ �̿��� ��Ʈ ���̾ƿ��� �����´�.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("..\\view\\RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			// ��Ʈ ���̾ƿ��� �����ϴ� scene�� �����ش�.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch (Exception e) {
			e.printStackTrace();
		}
	} // ��Ʈ ���̾ƿ� �ȿ� �������� �����ش�.
	public void setWordMainView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("..\\view\\WordMainView.fxml"));
			AnchorPane wordMainView = (AnchorPane) loader.load();
			rootLayout.setCenter(wordMainView);
			controller = loader.getController();
			primaryStage.setResizable(false); // ������ â ����	
			controller.setMain(this);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int setWordDataView(Word word) {   // ��� ���� ����â�� ���� ���� �� �� ��ȯ ���� �ٽ� ��ȯ
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("..\\view\\WordDataView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("��� ����");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);	
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			WordDataController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.SetMainApplication(this);
			controller.setWord(word);
			dialogStage.showAndWait();
			
			return controller.getReturnValue();
		}catch(IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	//������ ���� ���������� ��ȯ
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	public ObservableList<Word> wordList() {
		return wordList;
	}
	public void GetApplicationSettings(String nodeName) {
		Preferences prefsRoot = Preferences.userRoot();
		settings = prefsRoot.node(nodeName);
		value_Path_all = settings.get(stringPathName,"");
		/**
    	 * value_Path_all���ڸ� '|'�� split�ؼ� tableData�� ��´�.
    	 * ¦����(0,2,4...):  (���� ���)
    	 * Ȧ����(1,3,5...):  (���纻 ���)
    	 **/
		 splitPath = value_Path_all.split("\\|");
		if(splitPath.length >1) {
			for(int i =0; i< splitPath.length; i+=2) {
				wordList.add(new Word(splitPath[i], splitPath[i+1]));
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		launch(args);
	}
}	