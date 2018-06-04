package controller;


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
 * WatchService�� Ȱ���ϴ� Thread�� �������ִ� Ŭ����.
 * 
 * @author Joya
 *
 */
public class WatchServiceThread extends Thread {
	private WatchService watchService;
	// ���� ���� Watch ����
	private String inputFolder;
	// Watch���� ������ �ű� ��� ����
	private String outputFolder;
	private String today;
	private String day;
	private boolean started;
	// ENTRY_CREATE�̺�Ʈ �� �ٷ� ENTRY_MODIFY�̺�Ʈ�� �߻��� �� ����ó���� �ϱ� ���� ����.
	private String previusEventDate;
	
	/**
	 * 
	 * @param inputPath
	 *            Origin Watch ������ ���
	 * @param outputPath
	 *            Destination ������ ���
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
			System.out.println("Input Folder: " + inputFolder);
			System.out.println("Output Folder: " + outputFolder);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // �ð� ������ ( �� - �� - �� - �ð�
																						// - �� - �� )
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
			while (started) {
				WatchKey watchKey = watchService.take();
				List<WatchEvent<?>> list = watchKey.pollEvents();

				for (WatchEvent<?> watchEvent : list) {
					Kind<?> kind = watchEvent.kind();
					Path path = (Path) watchEvent.context();

					Calendar cal = Calendar.getInstance();
					today = formatter.format(cal.getTime()); // ����ð� ��Ÿ���� ǥ
					day = formatter2.format(cal.getTime()); // �α� ���峯¥ ����ϱ� ���� ��

					if (path.toString().contains("~")) {
					} else if (path.toString().contains("$")) {
					} else if (path.toString().contains(".BAK")) { // contains = ���õ� ���ڰ� ���� �� ó��
					} else {
						if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
							Platform.runLater(() -> Main.controller.addRowInloadList(path.toString(),
									directory.toString(), "����", today,day));
							
							
							System.out.println("���� �۾�..." + cal.getTimeInMillis());

							try { // ���� ���� try
								FileInputStream inFile = new FileInputStream(directory + "\\" + path);
								FileOutputStream outFile = new FileOutputStream(outpath + "\\" + path);

								File filter = new File(outFile.toString()); // ���� ��� �̻� �� �����߻��� �������� ��ġ
								if (!filter.exists()) {
									filter.mkdir();
								} else {
									File[] destory = filter.listFiles();
									for (File des : destory) {
										des.delete();
									}
								}
								int data = 0;
								while ((data = inFile.read()) != -1) {
									outFile.write(data);
								}
								inFile.close();
								outFile.close();
							} catch (IOException e) {
								e.printStackTrace();
							}

							// ���� �̺�Ʈ�� �۾��ð��� ����ϱ� ���ؼ� �۾����� Date�� ������ ����
							previusEventDate = today;

						} else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
							Platform.runLater(() -> Main.controller.addRowInloadList(path.toString(),
									directory.toString(), "����", today,day));
							
							
							File delete = new File(outpath + "\\" + path.toString()); // ���� ��� �̻� �� �����߻��� �������� ��ġ

							delete.delete(); // ���� ����

						} else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
							/*
							 * ENTRY_MODIFY�̺�Ʈ�� ENTRY_CREATE�̺�Ʈ ���Ŀ� �߻��ϰų� ENTRY_MODIFY�̺�Ʈ �αװ� ������ ȭ�鿡 �߰��Ǵ°�
							 * �����ϱ� ���ؼ� ����ó��. ���� �̺�Ʈ �۾��ð�(previusEventDate)�� ���� �ð��� ���ؼ� ���� �ð��� �Ͼ �̺�Ʈ�� ȭ�鿡
							 * ǥ�� ����. ����: ȭ��α׿��� ǥ�ø� ���һ� ���μ����۾��� ���� ��.
							 * 
							 * *�ش� �˰����� �ӽ� �����̸� �� ���� �������� ������ ���� �ٶ�. - Joya
							 */
							
							
							if (!today.equals(previusEventDate)) {
								Platform.runLater(() -> Main.controller.addRowInloadList(path.toString(),
										directory.toString(), "����", today,day));
							}
							System.out.println("���� �۾�..." + cal.getTimeInMillis());

							try { // ���� ���� try
								FileInputStream inFile = new FileInputStream(directory + "\\" + path);
								FileOutputStream outFile = new FileOutputStream(outpath + "\\" + path);

								File filter = new File(outFile.toString()); // ���� ��� �̻� �� �����߻��� �������� ��ġ
								if (!filter.exists()) {
									filter.mkdir();
								} else {
									File[] destory = filter.listFiles();
									for (File des : destory) {
										des.delete();
									}
								}

								int data = 0;
								while ((data = inFile.read()) != -1) {
									outFile.write(data);
								}
								inFile.close();
								outFile.close();
							} catch (IOException e) {
								e.printStackTrace();
							}

							// ���� �̺�Ʈ�� �۾��ð��� ����ϱ� ���ؼ� �۾����� Date�� ������ ����
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

			// Thread ���� �� ��¹� ���.
			System.out.println(inputFolder + "�� WatchService�� ����Ǿ����ϴ�.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �����带 �����ϱ� ���� watchService�� Close�ϴ� �Լ�.
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
