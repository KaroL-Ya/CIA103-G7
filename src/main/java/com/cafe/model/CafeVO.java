package com.cafe.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "cafe")
public class CafeVO implements Serializable {
	public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAFE_ID", insertable = false, updatable = false)
    private Integer cafeId;

    @Column(name = "TAXID")
    @NotEmpty(message = "統一編號: 請勿空白")
    @Pattern(regexp = "^\\d{8}$", message = "統一編號: 必須是8位數字")
    private String taxId;

    @Column(name = "NAME")
    @NotEmpty(message = "咖啡廳名稱: 請勿空白")
    private String name;

    @Column(name = "AC")
    @NotEmpty(message = "帳號: 請勿空白")
    @Pattern(regexp = "^[(a-zA-Z0-9)]{6,20}$", message = "帳號: 只能是英文字母、數字, 且長度必需在6到20之間")
    private String ac;

    @Column(name = "PW")
    @NotEmpty(message = "密碼: 請勿空白")
    @Pattern(regexp = "^[(a-zA-Z0-9)]{6,20}$", message = "密碼: 只能是英文字母、數字, 且長度必需在6到20之間")
    private String pw;

    @Column(name = "EMAIL")
    @NotEmpty(message = "信箱: 請勿空白")
    private String email;

    @Column(name = "STATE", columnDefinition = "tinyint", insertable = false)
    private Integer state;

    @Column(name = "PHONE")
    @Pattern(regexp = "^09[0-9]{8}$", message = "電話號碼: 必須是有效的台灣手機號碼格式")
    private String phone;

    @Column(name = "CITY")
    @NotEmpty(message = "城市: 請勿空白")
    private String city;

    @Column(name = "DISC")
    @NotEmpty(message = "地區: 請勿空白")
    private String disc;

    @Column(name = "ADDRESS")
    @NotEmpty(message = "地址: 請勿空白")
    private String address;

    @Column(name = "INTRODUCE")
    @NotEmpty(message = "商家介紹: 請勿空白")
    private String introduce;

    @Column(name = "IMG", columnDefinition = "longblob")
    private byte[] img;
    
    @Transient
    private String base64Img; //非持久化字段

    public CafeVO() {
        super();
    }

    public CafeVO(Integer cafeId, String taxId, String name, String ac, String pw, String email, Integer state, String phone,
                  String city, String disc, String address, String introduce, byte[] img) {
        this.cafeId = cafeId;
        this.taxId = taxId;
        this.name = name;
        this.ac = ac;
        this.pw = pw;
        this.email = email;
        this.state = state;
        this.phone = phone;
        this.city = city;
        this.disc = disc;
        this.address = address;
        this.introduce = introduce;
        this.img = img;
    }

    public Integer getCafeId() {
        return cafeId;
    }

    public void setCafeId(Integer cafeId) {
        this.cafeId = cafeId;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
    
    

    public String getBase64Img() {
		return base64Img;
	}

	public void setBase64Img(String base64Img) {
		this.base64Img = base64Img;
	}

	@Override
    public String toString() {
        return "CafeVO [cafeId=" + cafeId + ", taxid=" + taxId + ", name=" + name + ", ac=" + ac + ", pw=" + pw +
                ", email=" + email + ", state=" + state + ", phone=" + phone + ", city=" + city + ", disc=" + disc +
                ", address=" + address + ", introduce=" + introduce + "]";
    }
}
