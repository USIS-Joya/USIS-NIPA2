package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class management {
	// ȭ������� ���� ������ �����ϴ� ���� management.java ����
	// �����۾� TABLE COLUMN -> ���ϸ�, ���ϰ��, ����, ����

	private final StringProperty FileName;
	private final StringProperty FilePath;
	private final StringProperty Start;
	private final StringProperty End;

	public management(String fileName, String filePath, String start, String end) {
		this.FileName = new SimpleStringProperty(fileName);
		this.FilePath = new SimpleStringProperty(filePath);
		this.Start = new SimpleStringProperty(start);
		this.End = new SimpleStringProperty(end);
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

	// File Progress
	public String getEnd() {
		return End.get();
	}

	public void setEnd(String end) {
		this.End.set(end);
	}

	public StringProperty getEndProperty() {
		return End;
	}
}
