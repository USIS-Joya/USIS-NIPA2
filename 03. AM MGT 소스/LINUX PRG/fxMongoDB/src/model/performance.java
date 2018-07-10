package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class performance {
	// �����۾��� ���� ������ �����ϴ� ���� performance.java ����
	// �����۾� TABLE COLUMN -> ���ϸ�, ���ϰ��, ���۽ð�, �����

	private StringProperty FileName;
	private StringProperty FilePath;
	private StringProperty startTime;
	private StringProperty progress;


	public performance(String fileName, String filePath, String startTime, String progress) {
		this.FileName = new SimpleStringProperty(fileName);
		this.FilePath = new SimpleStringProperty(filePath);
		this.startTime = new SimpleStringProperty(startTime);
		this.progress = new SimpleStringProperty(progress);
	}

	public performance() {
		FileName = new SimpleStringProperty();
		FilePath = new SimpleStringProperty();
		startTime = new SimpleStringProperty();
	    progress = new SimpleStringProperty();
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

	// file progress
	public String getprogress() {
		return progress.get();
	}

	public void setprogress(String progress) {
		this.progress.set(progress);
	}

	public StringProperty getprogressProperty() {
		return progress;
	}
}
