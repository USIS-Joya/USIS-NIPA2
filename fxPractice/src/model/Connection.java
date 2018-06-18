package model;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class Connection {

	public String MongoDB_IP;
	public int MongoDB_PORT;
	private String value = "";
	private String key = "";

	public void setIP(String IP) {
		MongoDB_IP = IP;
	}

	public String getIP() {
		return MongoDB_IP;
	}

	public void setPort(int Port) {
		MongoDB_PORT = Port;
	}

	public int getPort() {
		return MongoDB_PORT;
	}

	public void insertDummyDocs(DBCollection collection) {
		BasicDBObject document = new BasicDBObject();
		document.put(key, value);
		collection.insert(document);
		// System.out.println("Inserted Success..");
	}

	public void DBConnection(String PathName) {
		try {
			MongoClient mongoClient = new MongoClient(new ServerAddress(getIP(), getPort()));
			DB db = mongoClient.getDB("test");
			File clsFolder = new File(PathName);

			if (clsFolder.exists() == false) {
				System.out.println("folder is not found");
			} else {
				File[] arrFile = clsFolder.listFiles();

				for (int i = 0; i < arrFile.length; ++i) {
					System.out.println(arrFile[i].getName());
					String[] arr = arrFile[i].getName().split("-");
					DBCollection collection = db.getCollection(arr[2]);
					// File Data �б�- 180516 CWJ
					FileInputStream fis = new FileInputStream(PathName + arrFile[i].getName());
					HSSFWorkbook workbook = new HSSFWorkbook(fis);

					int rowindex = 0;
					int columnindex = 0;
					// ��Ʈ �� (ù��°���� �����ϹǷ� 0�� �ش�.)
					// ���� �� ��Ʈ�� �б� ���ؼ��� FOR���� �ѹ� �� �����ش�.
					HSSFSheet sheet = workbook.getSheetAt(0);
					// ���� ��
					int rows = sheet.getPhysicalNumberOfRows();
					
					for (rowindex = 0; rowindex < rows; rowindex++) {
						// ���� �д´�
						HSSFRow first = sheet.getRow(0);
						HSSFRow row = sheet.getRow(rowindex + 1);
						if (row != null) {
							int cells = row.getPhysicalNumberOfCells();
							for (columnindex = 0; columnindex < cells; columnindex++) {
								// �� ���� �д´�.
								HSSFCell cell = row.getCell(columnindex);
								HSSFCell columnName = first.getCell(columnindex);
								key = columnName.getStringCellValue();
								// ���� �� ���� ��츦 ���� NULL CHECK
								if (cell == null) {
									continue;
								} else {
									// Ÿ�Ժ��� ���� �б�
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
								// System.out.println(value);
								//insertDummyDocs(collection);
							}
							insertDummyDocs(collection);
						}
					}
				}
			}
			// //System.out.println("Find");
			// DBCursor cursor = collection.find();
			// while(cursor.hasNext()){
			// System.out.println(cursor.next());
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}