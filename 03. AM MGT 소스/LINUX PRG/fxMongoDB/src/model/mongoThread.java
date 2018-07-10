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
 * ���������� MongoDB�� import, Thread�� �������ִ� Ŭ����
 * 
 * @author WOOJEONG
 * 
 */
public class mongoThread extends Thread {
	// ���� ���� ����
	private String inputFolder;
	// MongoDB Value
	private String value = "";
	// MongoDB Field
	private String key = "";

	public String Today = "";
	public String endTime = "";
	public String progressValue = "";
	// copyProgress.messageProperty().addListener�̺�Ʈ�� �߻� �� �� ������ Row�� ���� ����� �ľ�
	public int MAX = 0;
	public boolean isConn = true;

	@SuppressWarnings("rawtypes")
	Task copyProgress;

	/**
	 * @param inputPath �������� ������ ���
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
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // �ð� ������ ( �� - �� - ��
																									// - �ð� - �� - �� )
						SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// �ð� ������ ( �� - �� - ��
																									// - �ð� - �� - �� )
						Calendar cal = Calendar.getInstance();

						String[] arr = file.getName().split("-"); // ���ϸ��� '-'�� �����ϴ� ���� �迭
						DBCollection collection = conn.getDB().getCollection(arr[2]);
						// ���ϸ��� Usis-AM-Structure-Data-Extract �� ��, arr[2] �̹Ƿ� Collection���� Structure��
						// ����
						BasicDBObject document = new BasicDBObject();

						List<BasicDBObject> documents = new ArrayList<BasicDBObject>(); // document �迭 ����

						FileInputStream fis = new FileInputStream(dir.getAbsolutePath() + "\\" + file.getName());

						// �������� �� �ÿ� Disconnect�� ����� �������� �ʱ� ������ �ٽ��ѹ� isConn�� false 180621 CWJ
						if (isConn == false) {
							return;
						}

						HSSFWorkbook workbook = new HSSFWorkbook(fis); // fis����� ��������

						int rowIndex = 0;
						int columnIndex = 0;

						HSSFSheet sheet = workbook.getSheetAt(0);
						int rows = sheet.getPhysicalNumberOfRows(); // ���� ����

						MAX = rows; // �࿡ ������ ���� ������� �ľ��� �� �ֵ��� MAX�� rows�� ����

						for (rowIndex = 0; rowIndex < rows; rowIndex++) {
							// ���� �д´�.
							HSSFRow first = sheet.getRow(0); // key���� ù �࿡ �����ϱ� ������ first ����
							HSSFRow row = sheet.getRow(rowIndex + 1); // value���� �ι�° ����� �����ϱ� ������ rowIndex+1
							if (row != null) {
								int cells = row.getPhysicalNumberOfCells();// cell�� ����

								for (columnIndex = 0; columnIndex < cells; columnIndex++) {
									// �� ���� �д´�.
									HSSFCell columnName = first.getCell(columnIndex); // ù ���� �� ��
									HSSFCell cell = row.getCell(columnIndex); // �ι�° ������� �� ��

									key = columnName.getStringCellValue(); // ù ���� String ���� key�� ����

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
									document.append(key, value); // document�� key, value�� put
								}
								Today = formatter.format(cal.getTime()); // ���۽ð��� ��Ÿ���� ǥ

								documents.add(document); // documents�迭�� document add
								collection.update(documents.get(rowIndex), document, true, true);
								// collection�� documents�迭 insert
							}

						}
						endTime = formatter2.format(cal.getTime()); // ����ð��� ��Ÿ���� ǥ
						
						copyProgress = createProgress(); //createProgress�� copyProgress task�� ����
						copyProgress.messageProperty().addListener(new ChangeListener<String>() {
							public void changed(ObservableValue<? extends String> observable, String oldValue,
									String newValue) {
								progressValue = newValue; //����� ����
								Platform.runLater(() -> Main.main.addRowInPerformList(file.getName(),
										dir.getAbsolutePath(), Today, progressValue)); 
								// ȭ����� Table�� ���ϸ�, ���ϰ��, ���۽ð�, ����� ������ �߰�
							}
						});
						new Thread(copyProgress).start();
						//
						Platform.runLater(() -> Main.main.addRowInManageList(file.getName(), dir.getAbsolutePath(),
								Today, endTime)); // ȭ����� Table�� ���ϸ�, ���ϰ��, ���۽ð�, ����ð� ������ �߰�

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
	 * ������� ��Ÿ���� task
	 */
	@SuppressWarnings("rawtypes")
	public Task createProgress() {
		return new Task() {
			@Override
			protected Object call() throws Exception {
				for (int i = 1; i <= MAX; i++) {
					updateMessage(((i * 100) / MAX) + "%"); 
					//������ �������Ͽ��� ���� ���� ���� ������� MessageProperty�� update
				}
				return true;
			}
		};

	}
}
