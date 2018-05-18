package controller;
	
import java.io.IOException;
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
	private ObservableList<Word> wordList = FXCollections.observableArrayList();
	private ObservableList<Load> loadList = FXCollections.observableArrayList();

	
	public Main() {
	
		
	loadList.add(new Load("�׽�Ʈ1","�׽�Ʈ2","�׽�Ʈ3","�׽�Ʈ4"));
	}
	
	public ObservableList<Word> getWordList() {
		return wordList;
	}
	public ObservableList<Load> getLoadList() {
		return loadList;
	}
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("�ȵ��ڳ�");
		setRootLayout();
		setWordMainView();
		
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
			WordMainController controller = loader.getController();
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
	
	
	
	public static void main(String[] args) throws Exception{
		launch(args);
	}
}	