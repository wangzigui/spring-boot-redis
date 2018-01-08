package com.nf.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.gson.Gson;

//@Entity
public class Stock implements Serializable {
	private static final long serialVersionUID = -1L;
//	@Id @GeneratedValue
    private int id;
	
	private int price;
	private int amount;

	public Stock(){}	

	public Stock(int price, int amount) {
		this.price = price;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}