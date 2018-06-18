package model;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import controller.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class mongoThread extends Thread {

	private String inputFolder;
	private String value = "";
	private String key = "";

	private boolean isConn = false;

	public mongoThread(String inputPath) {
		inputFolder = inputPath;
	}

	public void run() {
		isConn = true;
		try {
			if (isConn) {
				Connection conn = new Connection();
				conn.DBConn();
				File dir = new File(inputFolder);
				File[] files = dir.listFiles();

				List<File> arrFile = new ArrayList<File>();

				for (File file : files) {
					if (file.isFile()) {

						System.out.println(file.getName());
						String[] arr = file.getName().split("-");
						DBCollection collection = conn.getDB().getCollection(arr[2]);
						BasicDBObject document = new BasicDBObject();

						List<BasicDBObject> documents = new ArrayList<BasicDBObject>();

						FileInputStream fis = new FileInputStream(dir.getAbsolutePath() + "\\" + file.getName());

						arrFile.add(file);
						HSSFWorkbook workbook = new HSSFWorkbook(fis);

						int rowIndex = 0;
						int columnIndex = 0;

						HSSFSheet sheet = workbook.getSheetAt(0);
						int rows = sheet.getPhysicalNumberOfRows();
						System.out.println("sheet.getFirstRowNumber = " + rows);

						for (rowIndex = 0; rowIndex < rows; rowIndex++) {
							// 행을 읽는다.
							HSSFRow first = sheet.getRow(0);
							HSSFRow row = sheet.getRow(rowIndex + 1);
							if (row != null) {
								int cells = row.getPhysicalNumberOfCells();
								System.out.println("cells = " + cells);

								for (columnIndex = 0; columnIndex < cells; columnIndex++) {
									// 셀 값을 읽는다.
									HSSFCell columnName = first.getCell(columnIndex);
									HSSFCell cell = row.getCell(columnIndex);
									System.out.println("columnName = " + columnName.getStringCellValue());

									key = columnName.getStringCellValue();

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
									document.append(key, value);
								}
								documents.add(document);
								collection.update(documents.get(rowIndex), document, true, true);

							}

						}
						DBCursor cursor = collection.find();
						while (cursor.hasNext()) {
							System.out.println(cursor.next());
						}
						
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

	public void mongoClose() {
		try {
			isConn = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
