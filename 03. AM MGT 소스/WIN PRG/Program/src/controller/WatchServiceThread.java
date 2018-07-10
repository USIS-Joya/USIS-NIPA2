package controller;


import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
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

/**
 * WatchService를 활용하는 Thread를 생성해주는 클래스.
 * 
 * @author Joya
 *
 */
public class WatchServiceThread extends Thread {
	private WatchService watchService;
	// 현재 설정 Watch 폴더
	private String inputFolder;
	// Watch에서 파일을 옮길 대상 폴더
	private String outputFolder;
	private String today;
	private String day;
	private boolean started;
	// ENTRY_CREATE이벤트 후 바로 ENTRY_MODIFY이벤트가 발생할 시 예외처리를 하기 위한 변수.
	private String previusEventDate;
	
	/**
	 * 
	 * @param inputPath
	 *            Origin Watch 폴더의 경로
	 * @param outputPath
	 *            Destination 폴더의 경로
	 */
	public WatchServiceThread(String inputPath, String outputPath) {
		inputFolder = inputPath;
		outputFolder = outputPath;
	}
	
	@Override
	public void run() {
		started = true;

		try {
			watchService = FileSystems.getDefault().newWatchService();
			Path directory = Paths.get(inputFolder);
			Path outpath = Paths.get(outputFolder);
			directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);

			/* For Debug Output Statement */
//			System.out.println("Input Folder: " + inputFolder);
//			System.out.println("Output Folder: " + outputFolder);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 시간 데이터 ( 년 - 월 - 일 - 시간
																						// - 분 - 초 )
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
			while (started) {
				WatchKey watchKey = watchService.take();
				List<WatchEvent<?>> list = watchKey.pollEvents();

				for (WatchEvent<?> watchEvent : list) {
					Kind<?> kind = watchEvent.kind();
					Path path = (Path) watchEvent.context();
					Calendar cal = Calendar.getInstance();
					today = formatter.format(cal.getTime()); // 현재시간 나타내는 표
					day = formatter2.format(cal.getTime()); // 로그 저장날짜 기록하기 위한 값

					if (path.toString().contains("~")) {
					} else if (path.toString().contains("$")) {
					} else if (path.toString().contains(".BAK")) { // contains = 관련된 문자가 있을 시 처리
					} else {
						if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
							Platform.runLater(() -> Main.controller.addRowInloadList(path.toString(),
							directory.toString(), "생성", today,day));							
//							System.out.println("생성 작업..." + cal.getTimeInMillis());
							
							try { // 파일 복사 try
								
								File filter = new File(outpath.toString()); // 복사 경로 이상 시 에러발생을 막기위한 조치
								File filtering = new File(outpath + "\\" + path);
								File check = new File(directory + "\\" + path);
								
								if (!filter.exists()) { // 폴더가 없을시 생성
									filter.mkdirs();
								
								} else if(check.isDirectory()){ // check 가 만약 디렉토리(폴더)라면 생성
									if(!filtering.exists()) {
										filtering.mkdirs();
									}
									}else { //  파일 복사
										FileInputStream inFile = new FileInputStream(directory + "\\" + path);
										FileOutputStream outFile = new FileOutputStream(outpath + "\\" + path);
										
										BufferedInputStream in = new BufferedInputStream(inFile);
										BufferedOutputStream out = new BufferedOutputStream(outFile);
										int data = 0;
										byte[] loading = new byte[1024]; 
										while ((data = in.read(loading, 0,1024)) != -1) {
											out.write(loading,0,data);
								}
										inFile.close();
										outFile.close();
										inFile.close();
										outFile.close();
							}
							} catch (IOException e) {
								e.printStackTrace();
							}

							// 이전 이벤트의 작업시간을 계산하기 위해서 작업수행 Date를 변수에 저장
							previusEventDate = today;

						} else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
							Platform.runLater(() -> Main.controller.addRowInloadList(path.toString(),
							directory.toString(), "삭제", today,day));							
							File delete = new File(outpath + "\\" + path.toString()); // 복사 경로 이상 시 에러발생을 막기위한 조치
							delete.delete(); // 파일 삭제
						} else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
							/*
							 * ENTRY_MODIFY이벤트가 ENTRY_CREATE이벤트 이후에 발생하거나 ENTRY_MODIFY이벤트 로그가 여러번 화면에 추가되는걸
							 * 방지하기 위해서 예외처리. 이전 이벤트 작업시간(previusEventDate)가 현재 시간을 비교해서 같은 시간에 일어난 이벤트는 화면에
							 * 표시 제외. 주의: 화면로그에만 표시를 안할뿐 내부수행작업은 수행 함.
							 * 
							 * *해당 알고리즘은 임시 방편이면 더 좋은 개선안이 있으면 수정 바람. - Joya
							 */
							
							
							if (!today.equals(previusEventDate)) {
								Platform.runLater(() -> Main.controller.addRowInloadList(path.toString(),
										directory.toString(), "수정", today,day));
							}
//							System.out.println("수정 작업..." + cal.getTimeInMillis());

							try { // 파일 복사 try
							
								
								File filter = new File(outpath.toString()); // 복사 경로 이상 시 에러발생을 막기위한 조치
								File filtering = new File(outpath + "\\" + path);
								File check = new File(directory + "\\" + path);
								
								if (!filter.exists()) { // 폴더가 없을시 생성
									filter.mkdirs();
								
								}else if(check.isDirectory()){ // check가 디렉토리(폴더)라면 생성
									if(!filtering.exists()) {
										filtering.mkdirs();
										}
									}else { // 복사 기능
										FileInputStream inFile = new FileInputStream(directory + "\\" + path);
										FileOutputStream outFile = new FileOutputStream(outpath + "\\" + path);
										
										BufferedInputStream in = new BufferedInputStream(inFile);
										BufferedOutputStream out = new BufferedOutputStream(outFile);
										int data = 0;
										byte[] loading = new byte[1024]; 
										while ((data = in.read(loading, 0,1024)) != -1) {
											out.write(loading,0,data);
								}
										inFile.close();
										outFile.close();
										inFile.close();
										outFile.close();
									}
							} catch (IOException e) {
								e.printStackTrace();
							}

							// 이전 이벤트의 작업시간을 계산하기 위해서 작업수행 Date를 변수에 저장
							previusEventDate = today;

						} else if (kind == StandardWatchEventKinds.OVERFLOW) {

						}
					}
				}

				boolean valid = watchKey.reset();

				if (!valid) {
					break;
				}
			}
			watchService.close();

			// Thread 종료 시 출력문 출력.
//			System.out.println(inputFolder + "의 WatchService가 종료되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 쓰레드를 종료하기 전에 watchService를 Close하는 함수.
	 */
	public void watchServiceClose() {
		try {
			watchService.close();
			started = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}