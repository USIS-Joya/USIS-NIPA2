//package controller;
//import java.io.File;
//import java.io.FileFilter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
// 
//import org.eclipse.jetty.util.Scanner;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
// 
//public class FileChangeScanner {
//    private Scanner scanner;
//    
//    public FileChangeScanner(String targetDir){
//        List<File> scanFiles = new ArrayList<File>();
//         
//        searchSubDirs(targetDir, scanFiles);
//         
//        scanner = new Scanner();
//        scanner.setScanInterval(1);          // 1�� �������� ������� ��ĵ
//        scanner.setScanDirs(scanFiles);
//        scanner.setReportExistingFilesOnStartup(false);
//        scanner.addListener(new Scanner.DiscreteListener() {
//            public void fileRemoved(String filename) throws Exception {
//            	
//            	System.out.println(filename + " is deleted");
//            	
//            }
//             
//            public void fileChanged(String filename) throws Exception {
//            	
//            	System.out.println(filename + " is changed");
//            	
//            }
//             
//            public void fileAdded(String filename) throws Exception {
//                File f = new File(filename);
//                if(f.isDirectory()) scanner.addScanDir(f);
//            	
//                System.out.println(filename + " is added");
//              
//            }
//        });
//    }
//     
//    public void start(){
//        try {
//            scanner.start();
//        } catch (Exception e) {
//            new RuntimeException(e);
//        }
//    }
//     
//    /**
//     * ���� ���丮�� ã��
//     * @param targetDir
//     * @param dirs
//     * @return
//     */
//    private List<File> searchSubDirs(String targetDir, final List<File> dirs){
//        File target = new File(targetDir);
//        target.listFiles(new FileFilter() {
//            public boolean accept(File file) {
//                if( file.isDirectory() ) {
//                    dirs.add(file);
//                    searchSubDirs(file.toString(), dirs);
//                } 	
//                return false;
//            }
//        });
//         
//        return dirs;
//    }
// 
//    public static void main(String[] args) throws Exception{
//        // c:\test ���� ���� �� ���� ���� ����
//        new FileChangeScanner("C:\\test").start();
//         
//        // �������� ���� ���α׷��� ����Ǳ� ������ ���α׷� ��������� ���� Ÿ�̸� ����
//        // WAS ȯ�濡�� �������� Ÿ�̸� ������ �ʿ����
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            public void run() {}
//        }, 60*1000, 60*1000 );
//    }
//}
//  