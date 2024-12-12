package com.admin.model;

import java.sql.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import com.dept.model.DeptVO;

/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "admin")
public class AdminVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "ADMIN_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	private Integer admin_Id;
	
	// @ManyToOne  (雙向多對一/一對多) 的多對一
	//【此處預設為 @ManyToOne(fetch=FetchType.EAGER) --> 是指 lazy="false"之意】【注意: 此處的預設值與XML版 (p.127及p.132) 的預設值相反】
	//【如果修改為 @ManyToOne(fetch=FetchType.LAZY)  --> 則指 lazy="true" 之意】
	@ManyToOne
	@JoinColumn(name = "DEPTNO")   // 指定用來join table的column
	private DeptVO deptVO;
	
	@Column(name = "ADMIN_AC")
	@NotEmpty(message="管理員帳號: 請勿空白")
	@Size(min=2,max=10,message="管理員帳號: 長度必需在{min}到{max}之間")
	private String admin_Ac;
	
	@Column(name = "ADMIN_PW")
	@NotEmpty(message="管理員密碼: 請勿空白")
	@Size(min=2,max=10,message="管理員密碼: 長度必需在{min}到{max}之間")
	private String admin_Pw;
	
	@Column(name = "ADMIN_NAME")
	@NotEmpty(message="管理員姓名: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "管理員姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
	private String admin_Name;
	
	@Column(name ="ADMIN_STATUS")
	@NotNull(message="請勿空白")
	private Integer admin_Status;
	
	@Column(name = "HIREDATE")
	@Past(message="日期必須是在今日(含)之前")
	private Date hiredate;
	
	@Column(name = "ADMIN_IMG")
	private byte[] admin_Img;
	
	@ManyToMany
	@JoinTable(
				name = "adminauth",
				joinColumns = { @JoinColumn(name = "ADMIN_ID", referencedColumnName = "ADMIN_ID") },
				inverseJoinColumns = { @JoinColumn(name = "FUNC_ID", referencedColumnName = "FUNC_ID") }
              )
	@OrderBy()
	private Set<AdminFuncVO> admin_Func;
	
//	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	private Set<AdminAuthVO> adminAuths = new HashSet<>();
//
//	public Set<AdminAuthVO> getAdminAuths() {
//	    return adminAuths;
//	}
//
//	public void setAdminAuths(Set<AdminAuthVO> adminAuths) {
//	    this.adminAuths = adminAuths;
//	}

	public AdminVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}

	public Integer getAdmin_Id() {
		return this.admin_Id;
	}

	public void setAdmin_Id(Integer admin_Id) {
		this.admin_Id = admin_Id;
	}

	public DeptVO getDeptVO() {
		return this.deptVO;
	}

	public void setDeptVO(DeptVO deptVO) {
		this.deptVO = deptVO;
	}

	public String getAdmin_Name() {
		return this.admin_Name;
	}

	public void setAdmin_Name(String admin_Name) {
		this.admin_Name = admin_Name;
	}

	public String getAdmin_Ac() {
		return this.admin_Ac;
	}

	public void setAdmin_Ac(String admin_Ac) {
		this.admin_Ac = admin_Ac;
	}
	
	public String getAdmin_Pw() {
		return this.admin_Pw;
	}

	public void setAdmin_Pw(String admin_Pw) {
		this.admin_Pw = admin_Pw;
	}
		
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

	public Date getHiredate() {
		return this.hiredate;
	}

	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
	
	public byte[] getAdmin_Img() {
		return admin_Img;
	}
	public void setAdmin_Img(byte[] admin_Img) {
		this.admin_Img = admin_Img;
	}
	
	public Set<AdminFuncVO> getAdmin_Func() {
		return admin_Func;
	}

	public void setAdmin_Func(Set<AdminFuncVO> admin_Func) {
		this.admin_Func = admin_Func;
	}

	@Override
	public String toString() {
		return "AdminVO [admin_Id=" + admin_Id + ", deptVO=" + deptVO + ", admin_Ac=" + admin_Ac + ", admin_Pw="
				+ admin_Pw + ", admin_Name=" + admin_Name + ", admin_Status=" + admin_Status + ", hiredate=" + hiredate
				+ "]";
	}
	
}
