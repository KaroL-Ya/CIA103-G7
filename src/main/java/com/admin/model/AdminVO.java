package com.admin.model;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.dept.model.DeptVO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.DecimalMin;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;

//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;


@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "admin")
public class AdminVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer admin_Id;
	private DeptVO deptVO;
	private String admin_Ac;
	private String admin_Pw;
	private String admin_Name;
	private Integer admin_Status;
	private Date hiredate;
	private byte[] admin_Img;
	
	public AdminVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}

	@Id
	@Column(name = "admin_Id",updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	public Integer getAdmin_Id() {
		return this.admin_Id;
	}

	public void setAdmin_Id(Integer admin_Id) {
		this.admin_Id = admin_Id;
	}

	// @ManyToOne  (雙向多對一/一對多) 的多對一
	//【此處預設為 @ManyToOne(fetch=FetchType.EAGER) --> 是指 lazy="false"之意】【注意: 此處的預設值與XML版 (p.127及p.132) 的預設值相反】
	//【如果修改為 @ManyToOne(fetch=FetchType.LAZY)  --> 則指 lazy="true" 之意】
	@ManyToOne
	@JoinColumn(name = "DEPTNO")   // 指定用來join table的column
	public DeptVO getDeptVO() {
		return this.deptVO;
	}

	public void setDeptVO(DeptVO deptVO) {
		this.deptVO = deptVO;
	}

	@Column(name = "Admin_Name")
	@NotEmpty(message="管理員姓名: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "管理員姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
	public String getAdmin_Name() {
		return this.admin_Name;
	}

	public void setAdmin_Name(String admin_Name) {
		this.admin_Name = admin_Name;
	}

	@Column(name = "admin_Ac")
	@NotEmpty(message="管理員帳號: 請勿空白")
	@Size(min=2,max=10,message="管理員帳號: 長度必需在{min}到{max}之間")
	public String getAdmin_Ac() {
		return this.admin_Ac;
	}

	public void setAdmin_Ac(String admin_Ac) {
		this.admin_Ac = admin_Ac;
	}
	
	@Column(name = "admin_Pw")
	@NotEmpty(message="管理員密碼: 請勿空白")
	@Size(min=2,max=10,message="管理員密碼: 長度必需在{min}到{max}之間")
	public String getAdmin_Pw() {
		return this.admin_Pw;
	}

	public void setAdmin_Pw(String admin_Pw) {
		this.admin_Pw = admin_Pw;
	}
		
	@Column(name ="admin_Status")
	public Integer getAdmin_Status() {
		return this.admin_Status;
	}
	
	@Transient
	public String getAdminStatusText() {
	    return admin_Status == 1 ? "啟用" : "停用";
	}
	
	public void setAdmin_Status(Integer admin_Status) {
		this.admin_Status = admin_Status;
	}

	@Column(name = "hiredate")
//	@NotNull(message="雇用日期: 請勿空白")	
//	@Future(message="日期必須是在今日(不含)之後")
//	@Past(message="日期必須是在今日(含)之前")
//	@DateTimeFormat(pattern="yyyy-MM-dd") 
//	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	public Date getHiredate() {
		return this.hiredate;
	}

	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
	
	@Column(name = "admin_Img")
//	@NotEmpty(message="員工照片: 請上傳照片") --> 由EmpController.java 第60行處理錯誤信息
	public byte[] getAdmin_Img() {
		return admin_Img;
	}
	public void setAdmin_Img(byte[] admin_Img) {
		this.admin_Img = admin_Img;
	}

	@Override
	public String toString() {
		return "AdminVO [admin_Id=" + admin_Id + ", deptVO=" + deptVO + ", admin_Ac=" + admin_Ac + ", admin_Pw="
				+ admin_Pw + ", admin_Name=" + admin_Name + ", admin_Status=" + admin_Status + ", hiredate=" + hiredate
				+ "]";
	}
	
	
	
}
