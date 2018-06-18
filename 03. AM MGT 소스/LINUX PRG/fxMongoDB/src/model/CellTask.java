package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;

public class CellTask extends Task<List<File>> {
	@Override
	protected List<File> call() throws Exception {

		File dir = new File("D:\\1002");
		File[] files = dir.listFiles();
		int count = files.length;

		List<File> copied = new ArrayList<File>();
		int i = 0;
		for (File file : files) {
			if (file.isFile()) {
				copied.add(file);
			}
			i++;
			this.updateProgress(i, count);
		}
		return copied;
	}

}
