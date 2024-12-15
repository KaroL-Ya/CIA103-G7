package com.news.model;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;


@Entity
@Table(name = "news") // 對應的資料表名稱
public class NewsVO implements java.io.Serializable {
	public static final long serialVersionUID = 1L;

	private Integer newsId; // 消息序號
	private String title; // 消息標題
	private Date time; // 發送日期
	private Byte status; // 消息狀態
	private String content; // 消息內容
	
	public NewsVO() {
		super();
	}
	
	public NewsVO(Integer newsId, String title, Timestamp time, Byte status, String content) {
		super();
		this.newsId = newsId;
		this.title = title;
		this.time = time;
		this.status = status;
		this.content = content;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成主鍵
	@Column(name = "NEWS_ID") // 對應的資料表欄位名稱
	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	@Column(name = "TITLE") // 對應的資料表欄位名稱
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "TIME") // 對應的資料表欄位名稱
	@Temporal(TemporalType.TIMESTAMP)
//	@NotNull(message="雇用日期: 請勿空白")	
//	@Future(message="日期必須是在今日(不含)之後")
//	@Past(message="日期必須是在今日(含)之前")
	@DateTimeFormat(pattern="yyyy-MM-dd") 
//	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "STATUS", nullable = false) // 對應的資料表欄位名稱
	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Column(name = "CONTENT") // 對應的資料表欄位名稱
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


}
