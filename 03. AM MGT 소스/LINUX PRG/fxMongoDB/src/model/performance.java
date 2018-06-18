package model;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

public class performance extends Task {
	// 수행작업에 대한 정보를 저장하는 모델인 performance.java 생성
	// 수행작업 TABLE COLUMN -> 파일명, 파일경로, 시작시간, 진행률

	private static int MAX = 100;
	private static int SLEEP_TIME = 200;

	private StringProperty FileName;
	private StringProperty FilePath;
	private StringProperty startTime;
	// private DoubleProperty progress;

	public performance(String fileName, String filePath, String startTime) {
		this.FileName = new SimpleStringProperty(fileName);
		this.FilePath = new SimpleStringProperty(filePath);
		this.startTime = new SimpleStringProperty(startTime);
		// this.progress = new SimpleDoubleProperty(progress);

	}

	public performance() {
		FileName = new SimpleStringProperty();
		FilePath = new SimpleStringProperty();
		startTime = new SimpleStringProperty();
		// progress = new SimpleDoubleProperty();
	}

	// File Name
	public String getFileName() {
		return FileName.get();
	}

	public void setFileName(String fileName) {
		this.FileName.set(fileName);
	}

	public StringProperty getFileNameProperty() {
		return FileName;
	}

	// File Path
	public String getFilePath() {
		return FilePath.get();
	}

	public void setFilePath(String filePath) {
		this.FilePath.set(filePath);
	}

	public StringProperty getFilePathProperty() {
		return FilePath;
	}

	// File Start
	public String getStart() {
		return startTime.get();
	}

	public void setStart(String startTime) {
		this.startTime.set(startTime);
	}

	public StringProperty getStartProperty() {
		return startTime;
	}

	// // file progress
	// public double getprogress() {
	// return progress.get();
	// }
	//
	// public void setprogress(double progress) {
	// this.progress.set(progress);
	// }
	//
	// public DoubleProperty getprogressProperty() {
	// return progress;
	// }

	@Override
	protected Object call() throws Exception {
		this.updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, 1);
		Thread.sleep(3000);
		for (int i = 0; i < MAX; i++) {
			updateProgress(i, MAX);
			Thread.sleep(SLEEP_TIME);
		}
		this.updateProgress(1, 1);
		return null;
	}

	// public double getProgressBar() {
	// return Progress.get();
	// }
	//
	// public void setProgressBar(double bar) {
	// this.Progress.set(bar);
	// }
	//
	// public SimpleDoubleProperty getProgressProperty() {
	// return Progress;
	// }

}
