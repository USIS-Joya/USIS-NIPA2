package controller;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Word {
	private final StringProperty now;
	private final StringProperty next;
	
	
	public Word(String now,String next) {
		this.now = new SimpleStringProperty(now);
		this.next = new SimpleStringProperty(next);
	
	}
	public String getNow() {
		return now.get();
	}
	public void setNow(String now) {
		this.now.set(now);
	}
	public StringProperty getNowProperty() {
		return now;
	}
	public String getNext() {
		return next.get();
	}
	public void setNext(String next) {
		this.next.set(next);
	}
	public StringProperty getNextProperty() {
		return next;
	}
	

	
}
