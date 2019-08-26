package com.example.demo;
import java.io.Serializable;

public class Syain implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private  String currentURL;
	private  String id;
	
	public String getCurrentURL() {
		return currentURL;
	}
	public void setCurrentURL(String currentURL) {
		this.currentURL = currentURL;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}