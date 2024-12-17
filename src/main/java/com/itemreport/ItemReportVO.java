package com.itemreport;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "ITEM_REPORT")
public class ItemReportVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ITEM_REPORT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemReportId;

	@Column(name = "ITEM_ID")
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
	@NotNull(message = "商品編號: 請勿空白")
	private Integer itemId;

	@Column(name = "MEM_ID")
	@JoinColumn(name = "MEM_ID", referencedColumnName = "MEM_ID")
	private Integer memId;

	@Column(name = "REASON")
	@NotEmpty(message = "檢舉事由: 請勿空白")
	@Size(min = 1, max = 200, message = "檢舉事由: 長度必須在1到200字之間")
	private String reason;

	@Column(name = "TIME")
	@NotNull(message = "檢舉日期: 請勿空白")
	private Date time;

	@Column(name = "STATE")
	@NotNull(message = "檢舉狀態: 請選擇狀態")
	private Byte state; // 0: 未處理, 1: 檢舉通過, 2: 檢舉未通過

	public ItemReportVO() {
	}

	// Getters and Setters
	public Integer getItemReportId() {
		return itemReportId;
	}

	public void setItemReportId(Integer itemReportId) {
		this.itemReportId = itemReportId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer ItemId) {
		this.itemId = itemId;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer MemId) {
		this.memId = memId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

}
