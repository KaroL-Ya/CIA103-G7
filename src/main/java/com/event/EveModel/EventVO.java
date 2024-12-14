package com.event.EveModel;

import java.io.IOException;
import java.time.LocalDate;		//SQL date與spring衝突

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;

//import org.hibernate.validator.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;


@Entity  
@Table(name = "event") //app-properties, ciag07, table = event 
public class EventVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public EventVO() { //基本建構子
	}
	
	@Id //@identity 
	@Column(name = "EVE_ID") 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Integer eveID;

//  待整合，一般會員創建
//	@ManyToOne
//	@JoinColumn(name = "mem_no")
//	private MemberVO memberVO;
	
//	待整合，商家會員創建
//  @ManyToOne(fetch = FetchType.EAGER)
//  @JoinColumn(name = "business_id", referencedColumnName = "id")
//  private BusinessMember businessMember;
	
	
	@Column(name = "cafe_id")
	private Integer cafeID;
	
	
	@Column(name ="mem_id")
	private Integer memID;
	
	@NotBlank(message = "請勿空白")
	@Column(name="EVE_NAME")
	private String eveName; 	//活動名稱
	
	@Column(name="dat")
	@Future(message="日期必須是在今日(不含)之後")
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private LocalDate dat;		//活動日期
	
	@Column(name="loc")
	private String loc;		//地點

	@NotBlank(message = "請勿空白")
	@Column(name="detail")
	@Size(max=500, message="字數限制500")
	private String detail;	//活動內容
	
	@Column(name="lim")
	private Integer lim;	//人數上限
	
	@Column(name="opd")
	@Future(message="日期必須是在今日(不含)之後")
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private LocalDate opd;		//報名日期
	
	@Column(name="trm")
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private LocalDate trm;		//截止報名
		
	@Column(name="stat", nullable=false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer stat=0;		//活動審核狀態。0→未審核(未上架) 1→審核通過 2→審核被拒 3→取消 4→已結束
	
	@Column(name="num", nullable=false)
	private Integer num=0;		//現在報名活動人數
	
	@Column(name="cost", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer cost=0;		//是否收費。(預設=0不收費，1為收費活動)
	
	
	@Column(name = "event_img", columnDefinition = "longblob")
	private byte[] eveImg;		//活動圖片

	
	
	

	public Integer getEveID() {
		return eveID;
	}

	public void setEveID(Integer eveID) {
		this.eveID = eveID;
	}
	
	public Integer getCafeID() {
		return cafeID;
	}

	public void setCafeID(Integer cafeID) {
		this.cafeID = cafeID;
	}

	public Integer getMemID() {
		return memID;
	}

	public void setMemID(Integer memID) {
		this.memID = memID;
	}

	public String getEveName() {
		return eveName;
	}

	public void setEveName(String eveName) {
		this.eveName = eveName;
	}

	public LocalDate getDat() {
		return dat;
	}

	public void setDat(LocalDate dat) {
		this.dat = dat;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Integer getLim() {
		return lim;
	}

	public void setLim(Integer lim) {
		this.lim = lim;
	}

	public LocalDate getOpd() {
		return opd;
	}

	public void setOpd(LocalDate opd) {
		this.opd = opd;
	}

	public LocalDate getTrm() {
		return trm;
	}

	public void setTrm(LocalDate trm) {
		this.trm = trm;
	}

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}
	
	public byte[] getEveImg() {
		return eveImg;
	}
	
	public void setEveImg(byte[] eveImg) {
		this.eveImg = eveImg;
	}
	
//	public EventVO(Integer eveID,
//			@NotBlank(message = "請勿空白")  String eveName,
//			@Future(message = "日期必須是在今日(不含)之後") LocalDate dat,
//			@Pattern(regexp = "^{1,100}$", message = "字數限制為100") String loc,
//			 String detail, Integer lim,
//			@Future(message = "日期必須是在今日(不含)之後") LocalDate opd, LocalDate trm, Integer stat, Integer num, Integer cost,
//			byte[] eveImg) {
//		super();
//		this.eveID = eveID;
//		this.eveName = eveName;
//		this.dat = dat;
//		this.loc = loc;
//		this.detail = detail;
//		this.lim = lim;
//		this.opd = opd;
//		this.trm = trm;
//		this.stat = stat;
//		this.num = num;
//		this.cost = cost;
//		this.eveImg = eveImg;
//	}

}
