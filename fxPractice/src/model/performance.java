package model;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

public class performance {
	// 수행작업에 대한 정보를 저장하는 모델인 performance.java 생성
	// 수행작업 TABLE COLUMN -> 파일명, 파일경로, 시작, 진행률

	private StringProperty FileName;
	private StringProperty FilePath;
	private StringProperty Start;
	private DoubleProperty Progress;

	public performance(String fileName, String filePath, String start, double bar) {
		this.FileName = new SimpleStringProperty(fileName);
		this.FilePath = new SimpleStringProperty(filePath);
		this.Start = new SimpleStringProperty(start);
		this.Progress = new SimpleDoubleProperty(bar);
	}

	public performance() {
		FileName = new SimpleStringProperty();
		FilePath = new SimpleStringProperty();
		Start = new SimpleStringProperty();
		Progress = new SimpleDoubleProperty();
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
		return Start.get();
	}

	public void setStart(String start) {
		this.Start.set(start);
	}

	public StringProperty getStartProperty() {
		return Start;
	}

	public double getProgressBar() {
		return Progress.get();
	}

	public void setProgressBar(double bar) {
		this.Progress.set(bar);
	}

	public DoubleProperty getProgressProperty() {
		return Progress;
	}

	// // file progress
	// public double getProgress() {
	// return Progress.get();
	// }
	//
	// public void setProgress(double progress) {
	// this.Progress.set(progress);
	// }
	//
	// public DoubleProperty getProgressProperty() {
	// return Progress;
	// }

}
