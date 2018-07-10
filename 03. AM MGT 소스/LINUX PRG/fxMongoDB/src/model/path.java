package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class path {
	//하나의 경로(path)에 대한 정보를 저장하는 모델인 path.java 생성
	
	private final StringProperty path ;
	
	public path(String path){
		this.path = new SimpleStringProperty(path);
	}

	public String getPath(){
		return path.get();
	}
	
	public void setPath(String path){
		this.path.set(path);
	}
	
	public StringProperty getPathProperty() {
		return path;
	}
}
