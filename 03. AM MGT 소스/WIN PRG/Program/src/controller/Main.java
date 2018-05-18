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
	
		
	loadList.add(new Load("테스트1","테스트2","테스트3","테스트4"));
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
		this.primaryStage.setTitle("안되자나");
		setRootLayout();
		setWordMainView();
		
	}
	
	// 루트 레이아웃을 초기화 
	public void setRootLayout() {
		try {
			//FXML 파일을 이요해 루트 레이아웃을 가져온다.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("..\\view\\RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			// 루트 레이아웃을 포함하는 scene을 보여준다.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch (Exception e) {
			e.printStackTrace();
		}
	} // 루트 레이아웃 안에 페이지를 보여준다.
	public void setWordMainView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("..\\view\\WordMainView.fxml"));
			AnchorPane wordMainView = (AnchorPane) loader.load();
			rootLayout.setCenter(wordMainView);
			WordMainController controller = loader.getController();
			primaryStage.setResizable(false); // 윈도우 창 고정	
			controller.setMain(this);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int setWordDataView(Word word) {   // 경로 설정 관리창을 띄우고 종료 될 시 반환 값을 다시 반환
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("..\\view\\WordDataView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("경로 설정");
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
	//현재의 메인 스테이지를 반환
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	
	
	public static void main(String[] args) throws Exception{
		launch(args);
	}
}	