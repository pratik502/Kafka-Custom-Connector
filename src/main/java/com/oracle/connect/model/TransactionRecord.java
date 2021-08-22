package com.oracle.connect.model;

public class TransactionRecord {

	private int id;
	private String day;
	private String month;
	private String year;
	private String note;
	
	public TransactionRecord(int id, String day, String month, String year, String note) {
		super();
		this.id = id;
		this.day = day;
		this.month = month;
		this.year = year;
		this.note = note;
	}
	
	public TransactionRecord() { }
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Override
	public String toString() {
		return "TransactionRecord [id=" + id + ", day=" + day + ", month=" + month + ", year=" + year + ", note=" + note
				+ "]";
	}
	
	
}
