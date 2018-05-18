//package controller;
//
//import java.io.IOException;
//import java.nio.file.FileSystems;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardWatchEventKinds;
//import java.nio.file.WatchEvent;
//import java.nio.file.WatchKey;
//import java.nio.file.WatchService;
//
//public class FileChange {
//	public static void main(String args[]) throws IOException, InterruptedException{
//		monitoringDir("C:\\test");
//	}
//	 static void monitoringDir(String dir) throws IOException, InterruptedException
//	   {
//	    /**
//	     * public abstract WatchService newWatchService() throws IOException;
//	     * WatchService란  파일변화, 이벤트변화 등에 대해 오브젝트를 등록하는 것
//	     * 예를들면 디렉토리등의 파일 변화를 감지하기에 적합하다.
//	     */
//	       WatchService myWatchService = FileSystems.getDefault().newWatchService(); 
//	       
//	       //모니터링을 원하는 디렉토리 Path를 얻는다.
//	       Path path = Paths.get(dir); // Get the directory to be monitored       
//	      
//	       
//	       //모니터링 서비스를 할 path에 의해 파일로케이션을 등록
//	       WatchKey watchKey = path.register(myWatchService, 
//	            StandardWatchEventKinds.ENTRY_CREATE,
//	            StandardWatchEventKinds.ENTRY_MODIFY,
//	            StandardWatchEventKinds.ENTRY_DELETE); // Register the directory
//	       
//	       //무한루프
//	       while(true)
//	       {
//	    	   //변화가 감지되는 경우 이벤트 종류와 파일명을 출력
//	          for (WatchEvent event : watchKey.pollEvents())
//	          {
//	             System.out.println(event.kind() + ": "+ event.context()); 
//	          }
//	         
//	          
//	           //* Resets this watch key.
//	           //* @return  {@code true} if the watch key is valid and has been reset 
//	           //*          {@code false} if the watch key could not be reset because it is
//	           //*          no longer {@link #isValid valid}, 디렉토리등이 삭제되는 경우           
//	          if (!watchKey.reset())
//	          {
//	             break; // 디렉토리등이 삭제되는 경우
//	          }
//	       }
//	   }
//	}