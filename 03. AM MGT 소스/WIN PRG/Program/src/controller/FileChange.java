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
//	     * WatchService��  ���Ϻ�ȭ, �̺�Ʈ��ȭ � ���� ������Ʈ�� ����ϴ� ��
//	     * ������� ���丮���� ���� ��ȭ�� �����ϱ⿡ �����ϴ�.
//	     */
//	       WatchService myWatchService = FileSystems.getDefault().newWatchService(); 
//	       
//	       //����͸��� ���ϴ� ���丮 Path�� ��´�.
//	       Path path = Paths.get(dir); // Get the directory to be monitored       
//	      
//	       
//	       //����͸� ���񽺸� �� path�� ���� ���Ϸ����̼��� ���
//	       WatchKey watchKey = path.register(myWatchService, 
//	            StandardWatchEventKinds.ENTRY_CREATE,
//	            StandardWatchEventKinds.ENTRY_MODIFY,
//	            StandardWatchEventKinds.ENTRY_DELETE); // Register the directory
//	       
//	       //���ѷ���
//	       while(true)
//	       {
//	    	   //��ȭ�� �����Ǵ� ��� �̺�Ʈ ������ ���ϸ��� ���
//	          for (WatchEvent event : watchKey.pollEvents())
//	          {
//	             System.out.println(event.kind() + ": "+ event.context()); 
//	          }
//	         
//	          
//	           //* Resets this watch key.
//	           //* @return  {@code true} if the watch key is valid and has been reset 
//	           //*          {@code false} if the watch key could not be reset because it is
//	           //*          no longer {@link #isValid valid}, ���丮���� �����Ǵ� ���           
//	          if (!watchKey.reset())
//	          {
//	             break; // ���丮���� �����Ǵ� ���
//	          }
//	       }
//	   }
//	}