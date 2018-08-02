package com.nf.controller;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.gson.Gson;

@Entity
public class User implements Serializable {
	private static final long serialVersionUID = -1L;
	@Id @GeneratedValue
    private String id;
	
	private String username;
	private String age;

	public User(){}
	
	public User(String username, String age) {
		this.username = username;
		this.age = age;
	}
	// 省略getter和setter

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}