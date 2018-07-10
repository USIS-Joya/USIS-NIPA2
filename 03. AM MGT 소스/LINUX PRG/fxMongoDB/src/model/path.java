package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class path {
	//�ϳ��� ���(path)�� ���� ������ �����ϴ� ���� path.java ����
	
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
