package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class performance {
	// �����۾��� ���� ������ �����ϴ� ���� performance.java ����
	// �����۾� TABLE COLUMN -> ���ϸ�, ���ϰ��, ����, �����

	private final StringProperty FileName;
	private final StringProperty FilePath;
	private final StringProperty Start;
	private final StringProperty Progress;

	public performance(String fileName, String filePath, String start, String progress) {
		this.FileName = new SimpleStringProperty(fileName);
		this.FilePath = new SimpleStringProperty(filePath);
		this.Start = new SimpleStringProperty(start);
		this.Progress = new SimpleStringProperty(progress);
	}

	// file name
	public String getFileName() {
		return FileName.get();
	}

	public void setFileName(String fileName) {
		this.FileName.set(fileName);
	}

	public StringProperty getFileNameProperty() {
		return FileName;
	}

	// file path
	public String getFilePath() {
		return FilePath.get();
	}

	public void setFilePath(String filePath) {
		this.FilePath.set(filePath);
	}

	public StringProperty getFilePathProperty() {
		return FilePath;
	}

	// file start
	public String getStart() {
		return Start.get();
	}

	public void setStart(String start) {
		this.Start.set(start);
	}

	public StringProperty getStartProperty() {
		return Start;
	}

	// file progress
	public String getProgress() {
		return Progress.get();
	}

	public void setProgress(String progress) {
		this.Progress.set(progress);
	}

	public StringProperty getProgressProperty() {
		return Progress;
	}
}
