package model;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import controller.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;

/**
 * 엑셀파일을 MongoDB에 import, Thread를 생성해주는 클래스
 * 
 * @author WOOJEONG
 * 
 */
public class mongoThread extends Thread {
	// 현재 설정 폴더
	private String inputFolder;
	// MongoDB Value
	private String value = "";
	// MongoDB Field
	private String key = "";

	public String Today = "";
	public String endTime = "";
	public String progressValue = "";
	// copyProgress.messageProperty().addListener이벤트가 발생 할 때 엑셀의 Row의 수로 진행률 파악
	public int MAX = 0;
	public boolean isConn = true;

	@SuppressWarnings("rawtypes")
	Task copyProgress;

	/**
	 * @param inputPath 엑셀파일 폴더의 경로
	 */
	public mongoThread(String inputPath) {
		inputFolder = inputPath;
	}

	@SuppressWarnings({ "deprecation", "resource" })
	@Override
	public void run() {
		try {
			if (isConn) {
				Connection conn = new Connection();
				conn.DBConn();
				File dir = new File(inputFolder);
				File[] files = dir.listFiles();

				for (File file : files) {
					if (file.isFile()) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // 시간 데이터 ( 년 - 월 - 일
																									// - 시간 - 분 - 초 )
						SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// 시간 데이터 ( 년 - 월 - 일
																									// - 시간 - 분 - 초 )
						Calendar cal = Calendar.getInstance();

						String[] arr = file.getName().split("-"); // 파일명을 '-'로 구분하는 문자 배열
						DBCollection collection = conn.getDB().getCollection(arr[2]);
						// 파일명이 Usis-AM-Structure-Data-Extract 일 때, arr[2] 이므로 Collection명은 Structure로
						// 저장
						BasicDBObject document = new BasicDBObject();

						List<BasicDBObject> documents = new ArrayList<BasicDBObject>(); // document 배열 생성

						FileInputStream fis = new FileInputStream(dir.getAbsolutePath() + "\\" + file.getName());

						// 강제종료 할 시에 Disconnect가 제대로 동작하지 않기 때문에 다시한번 isConn을 false 180621 CWJ
						if (isConn == false) {
							return;
						}

						HSSFWorkbook workbook = new HSSFWorkbook(fis); // fis경로의 엑셀파일

						int rowIndex = 0;
						int columnIndex = 0;

						HSSFSheet sheet = workbook.getSheetAt(0);
						int rows = sheet.getPhysicalNumberOfRows(); // 행의 개수

						MAX = rows; // 행에 개수에 따라 진행률을 파악할 수 있도록 MAX에 rows값 저장

						for (rowIndex = 0; rowIndex < rows; rowIndex++) {
							// 행을 읽는다.
							HSSFRow first = sheet.getRow(0); // key값이 첫 행에 존재하기 때문에 first 생성
							HSSFRow row = sheet.getRow(rowIndex + 1); // value값이 두번째 행부터 존재하기 때문에 rowIndex+1
							if (row != null) {
								int cells = row.getPhysicalNumberOfCells();// cell의 개수

								for (columnIndex = 0; columnIndex < cells; columnIndex++) {
									// 셀 값을 읽는다.
									HSSFCell columnName = first.getCell(columnIndex); // 첫 행의 셀 값
									HSSFCell cell = row.getCell(columnIndex); // 두번째 행부터의 셀 값

									key = columnName.getStringCellValue(); // 첫 행의 String 값을 key에 저장

									if (cell == null) {
										continue;
									} else {
										// 타입별로 내용 읽기
										switch (cell.getCellType()) {
										case HSSFCell.CELL_TYPE_FORMULA:
											value = cell.getCellFormula();
											break;
										case HSSFCell.CELL_TYPE_NUMERIC:
											value = cell.getNumericCellValue() + "";
											break;
										case HSSFCell.CELL_TYPE_STRING:
											value = cell.getStringCellValue() + "";
											break;
										case HSSFCell.CELL_TYPE_BLANK:
											value = cell.getBooleanCellValue() + "";
											break;
										case HSSFCell.CELL_TYPE_ERROR:
											value = cell.getErrorCellValue() + "";
											break;
										}
									}
									document.append(key, value); // document에 key, value값 put
								}
								Today = formatter.format(cal.getTime()); // 시작시간을 나타내는 표

								documents.add(document); // documents배열에 document add
								collection.update(documents.get(rowIndex), document, true, true);
								// collection에 documents배열 insert
							}

						}
						endTime = formatter2.format(cal.getTime()); // 종료시간을 나타내는 표
						
						copyProgress = createProgress(); //createProgress를 copyProgress task에 저장
						copyProgress.messageProperty().addListener(new ChangeListener<String>() {
							public void changed(ObservableValue<? extends String> observable, String oldValue,
									String newValue) {
								progressValue = newValue; //진행률 저장
								Platform.runLater(() -> Main.main.addRowInPerformList(file.getName(),
										dir.getAbsolutePath(), Today, progressValue)); 
								// 화면관리 Table에 파일명, 파일경로, 시작시간, 진행률 데이터 추가
							}
						});
						new Thread(copyProgress).start();
						//
						Platform.runLater(() -> Main.main.addRowInManageList(file.getName(), dir.getAbsolutePath(),
								Today, endTime)); // 화면관리 Table에 파일명, 파일경로, 시작시간, 종료시간 데이터 추가

//						DBCursor cursor = collection.find();
//						while (cursor.hasNext()) {
//							System.out.println(cursor.next());
//						}
					} else {
						System.out.println("file is not found");
					}
				}

			} else {
				System.out.println("connection fail");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//MongoDB Close
	public void mongoClose() {
		try {
			isConn = false;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 진행률을 나타내는 task
	 */
	@SuppressWarnings("rawtypes")
	public Task createProgress() {
		return new Task() {
			@Override
			protected Object call() throws Exception {
				for (int i = 1; i <= MAX; i++) {
					updateMessage(((i * 100) / MAX) + "%"); 
					//각각의 엑셀파일에서 행의 수에 따른 진행률을 MessageProperty에 update
				}
				return true;
			}
		};

	}
}
