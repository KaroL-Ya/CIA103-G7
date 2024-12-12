package com.member.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "member")
public class MemberVO implements Serializable{
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="mem_Id",updatable = false)
    private Integer mem_Id;
    
    @Column(name ="ac")
//    @NotEmpty(message="帳號:請勿空白")
    @Size(min=8,max=20,message="密碼: 長度必需在{min}到{max}之間")
    private String ac;
    
    @Column(name ="pw")
//	@NotEmpty(message="密碼: 請勿空白")
	@Size(min=8,max=20,message="密碼: 長度必需在{min}到{max}之間")
    private String pw;
    
    @Column(name ="email")
    @NotEmpty(message="信箱: 請勿空白")
    @Email(message="電子郵件格式無效")
    private String email;
    
    @Column(name ="status",columnDefinition = "bit", insertable = false)
    private Integer status;
    
    @Column(name ="registertime", insertable = false, updatable = false)
    private Timestamp registertime;
    
    @Column(name ="name")
//    @NotEmpty(message="姓名: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,20}$", message = "姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到20之間")
    private String name;
    
    @Column(name ="birth")
    @Past(message="生日不能是未來")
    private Date birth;
    
    @Column(name ="sex")
    @NotEmpty(message="性別: 請勿空白")
    private String sex;
    
    @Column(name ="phone")
//    @NotEmpty(message="行動電話: 請勿空白")
    @Pattern(regexp = "^09[0-9][0-9]\\d{6}$", message = "行動電話(10碼): 不正確的格式")    
    private String phone;
    
    @Column(name ="city")
    @NotEmpty(message="縣市: 請勿空白")
    private String city;
    
    @Column(name ="disc")
    @NotEmpty(message="鄉鎮市區: 請勿空白")
    private String disc;
    
    @Column(name ="address")
    @NotEmpty(message="地址: 請勿空白")
    private String address;
    
    @Column(name ="img",columnDefinition = "longblob")
    private byte[] img;
    
    public MemberVO() {
        super();
    }

    public MemberVO(Integer mem_Id, String ac, String pw, String email, Integer status, Timestamp registertime,
            String name, Date birth, String sex, String phone, String city, String disc, String address, byte[] img) {
        super();
        this.mem_Id = mem_Id;
        this.ac = ac;
        this.pw = pw;
        this.email = email;
        this.status = status;
        this.registertime = registertime;
        this.name = name;
        this.birth = birth;
        this.sex = sex;
        this.phone = phone;
        this.city = city;
        this.disc = disc;
        this.address = address;
        this.img = img;
    }

    public Integer getMem_Id() {
        return mem_Id;
    }

    public void setMem_Id(Integer mem_Id) {
        this.mem_Id = mem_Id;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getRegistertime() {
        return registertime;
    }
    
	@Transient
	public String getMemberStatusText() {
		switch (status) {
        case 0:
            return "未啟用";
        case 1:
            return "啟用";
        case 2:
            return "停權";
        default:
            return "未設定狀態"; // 處理無效的 status 值
    }
	}

    public void setRegistertime(Timestamp registertime) {
        this.registertime = registertime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "MemberVO [mem_Id=" + mem_Id + ", ac=" + ac + ", pw=" + pw + ", email=" + email + ", status=" + status
                + ", registertime=" + registertime + ", name=" + name + ", birth=" + birth + ", sex=" + sex + ", phone="
                + phone + ", city=" + city + ", disc=" + disc + ", address=" + address + "]";
    }
}