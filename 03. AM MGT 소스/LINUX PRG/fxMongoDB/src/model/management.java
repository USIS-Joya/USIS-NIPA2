package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class management {

	// 화면관리에 대한 정보를 저장하는 모델인 management.java 생성
	// 화면관리 TABLE COLUMN -> 파일명, 파일경로, 시작, 종료

	private StringProperty FileName;
	private StringProperty FilePath;
	private StringProperty startTime;
	private StringProperty endTime;

	public management(String fileName, String filePath, String StartTime, String EndTime) {
		this.FileName = new SimpleStringProperty(fileName);
		this.FilePath = new SimpleStringProperty(filePath);
		this.startTime = new SimpleStringProperty(StartTime);
		this.endTime = new SimpleStringProperty(EndTime);
	}

	public management() {
		FileName = new SimpleStringProperty();
		FilePath = new SimpleStringProperty();
		startTime = new SimpleStringProperty();
		endTime = new SimpleStringProperty();
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
	
	// File End
	public String getEnd() {
		return endTime.get();
	}

	public void setEnd(String endTime) {
		this.endTime.set(endTime);
	}

	public StringProperty getEndProperty() {
		return endTime;
	}
}
