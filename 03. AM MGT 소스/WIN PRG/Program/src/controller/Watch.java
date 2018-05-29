//package controller;
//import java.nio.file.FileSystems;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardWatchEventKinds;
//import java.nio.file.WatchEvent;
//import java.nio.file.WatchEvent.Kind;
//import java.nio.file.WatchKey;
//import java.nio.file.WatchService;
//import java.util.List;
// 
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.scene.Scene;
//import javafx.scene.control.TextArea;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//
//
//
//public class Watch extends Application{
//	class WatchThread extends Thread {
//		
//		@Override
//		public void run() {
//            try {
//                WatchService watchService = FileSystems.getDefault().newWatchService();
//                Path directory = Paths.get("C:\\test");
//                directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
//                        StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
// 
//                while (true) {
//                    WatchKey watchKey = watchService.take();
//                    List<WatchEvent<?>> list = watchKey.pollEvents();
// 
//                    for (WatchEvent<?> watchEvent : list) {
//                        Kind<?> kind = watchEvent.kind();
//                        Path path = (Path) watchEvent.context();
// 
//                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
//                            Platform.runLater(() -> textArea.appendText("颇老 积己凳 -> " + path.getFileName() +path+ "\n"));
//                        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
//                            Platform.runLater(() -> textArea.appendText("颇老 昏力凳 -> " + path.getFileName() +path+ "\n"));
//                        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
//                            Platform.runLater(() -> textArea.appendText("颇老 荐沥凳 -> " + path.getFileName() +path+ "\n"));
//                        } else if (kind == StandardWatchEventKinds.OVERFLOW) {
// 
//                        }
//                    }
// 
//                    boolean valid = watchKey.reset();
// 
//                    if (!valid) {
//                        break;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
// 
//    }
// 
//    TextArea textArea;
// 
//    @Override
//    public void start(Stage arg0) throws Exception {
//        BorderPane root = new BorderPane();
//        root.setPrefSize(500, 300);
// 
//        textArea = new TextArea();
//        textArea.setEditable(false);
//        root.setCenter(textArea);
// 
//        Scene scene = new Scene(root);
//        arg0.setScene(scene);
//        arg0.setTitle("WatchServiceExample");
//        arg0.show();
// 
//        WatchThread wst = new WatchThread();
//        wst.start();
//    }
// 
//    public static void main(String[] args) {
//        launch(args);
//    }
// 
//}
// 
//
