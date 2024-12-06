package com.member.model;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "member")
public class MemberVO implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="mem_Id",insertable = false,updatable = false)
    private Integer mem_Id;
    
    @Column(name ="ac")
    @NotEmpty(message="會員帳號: 請勿空白")
	@Pattern(regexp = "^[(a-zA-Z0-9)]{6,20}$", message = "會員帳號: 只能是英文字母、數字, 且長度必需在6到20之間")
    private String ac;
    
    @Column(name ="pw")
    @NotEmpty(message="會員密碼: 請勿空白")
	@Pattern(regexp = "^[(a-zA-Z0-9)]{6,20}$", message = "會員密碼: 只能是英文字母、數字, 且長度必需在6到20之間")
    private String pw;
    
    @Column(name ="email")
    @NotEmpty(message="信箱:請勿空白")
    private String email;
    
    @Column(name ="status",columnDefinition = "bit", insertable = false)
    private Integer status;
    
    @Column(name ="registertime", insertable = false, updatable = false)
    private Timestamp registertime;
    
    @Column(name ="name")
    @NotEmpty(message="會員姓名: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "會員姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
    private String name;
    
    @Column(name ="birth")
    private Date birth;
    
    @Column(name ="sex")
    private String sex;
    
    @Column(name ="phone")
    @Pattern(regexp ="^09[0-9][0-9]\\d{6}$" , message="請輸入符合規則的電話號碼")
    private String phone;
    
    @Column(name ="city")
    @NotEmpty(message="市:請勿空白")
    private String city;
    
    @Column(name ="disc")
    @NotEmpty(message="區:請勿空白")
    private String disc;
    
    @Column(name ="address")
    @NotEmpty(message="住址:請勿空白")
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