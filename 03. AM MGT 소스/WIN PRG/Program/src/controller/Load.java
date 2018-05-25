package controller;

import java.nio.file.Path;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Load {
	private final StringProperty filename;
	private final StringProperty filepath;
	private final StringProperty content;
	private final StringProperty time;
	
	public Load(String id,String filepath,String content,String time) {
		this.filename = new SimpleStringProperty(id);
		this.filepath = new SimpleStringProperty(filepath);
		this.content = new SimpleStringProperty(content);
		this.time = new SimpleStringProperty(time);
	}
	public String getFilename() {
		return filename.get();
	}
	public void setFilename(String filename) {
		this.filename.set(filename);
	}
	public StringProperty getFilenameProperty() {
		return filename;
	}
	public String getFilepath() {
		return filepath.get();
	}
	public void setFilepath(String filepath) {
		this.filepath.set(filepath);
	}
	public StringProperty getFilepathProperty() {
		return filepath;
	}
	public String getContent() {
		return content.get();
	}
	public void setContent(String content) {
		this.content.set(content);
	}
	public StringProperty getContentProperty() {
		return content;
	}
	public String getTime() {
		return time.get();
	}
	public void setTime(String time) {
		this.time.set(time);
	}
	public StringProperty getTimeProperty() {
		return time;
	}

}
