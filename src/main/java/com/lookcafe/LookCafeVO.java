package com.lookcafe;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table(name = "CAFE")
public class LookCafeVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CAFE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cafeId;

    @Column(name = "TAXID")
    @NotEmpty(message = "咖啡廳統編: 請勿空白")
    @Size(min = 8, max = 8, message = "咖啡廳統編: 長度必須為8")
    private String taxId;

    @Column(name = "NAME")
    @NotEmpty(message = "咖啡廳名稱: 請勿空白")
    @Size(min = 1, max = 20, message = "咖啡廳名稱: 長度必須在1到20之間")
    private String name;

    @Column(name = "AC")
    @NotEmpty(message = "咖啡廳帳號: 請勿空白")
    @Size(min = 1, max = 20, message = "咖啡廳帳號: 長度必須在1到20之間")
    private String ac;

    @Column(name = "PW")
    @NotEmpty(message = "咖啡廳密碼: 請勿空白")
    @Size(min = 6, max = 20, message = "密碼長度必須在6到20之間")
    private String pw;

    @Column(name = "EMAIL")
    @Email(message = "請輸入有效的電子郵件地址")
    @NotEmpty(message = "電子郵件: 請勿空白")
    @Size(max = 100, message = "電子郵件: 長度必須在100字以內")
    private String email;

    @Column(name = "STATE")
    @NotNull(message = "帳號狀態: 請選擇狀態")
    private Byte state;  // 0: 未啟用, 1: 啟用, 2: 停權

    @Column(name = "PHONE")
    @NotEmpty(message = "手機: 請勿空白")
    @Pattern(regexp = "^\\d{10}$", message = "手機: 請輸入有效的10位數字電話")
    private String phone;

    @Column(name = "CITY")
    @NotEmpty(message = "地址縣市: 請勿空白")
    @Size(min = 1, max = 3, message = "地址縣市: 長度必須為3個字")
    private String city;

    @Column(name = "DISC")
    @NotEmpty(message = "地址區域: 請勿空白")
    @Size(min = 1, max = 5, message = "地址區域: 長度必須為5個字以內")
    private String disc;

    @Column(name = "ADDRESS")
    @NotEmpty(message = "地址: 請勿空白")
    @Size(min = 1, max = 100, message = "地址: 長度必須在1到100字之間")
    private String address;
//
    @Column(name = "INTRODUCE")
    @NotBlank(message = "咖啡廳介紹: 請勿空白")
    @Size(min = 1, max = 1000, message = "咖啡廳介紹: 長度必須在1到1000字之間")
    private String introduce;

    @Column(name = "IMG")
    private byte[] img;  

    @Transient
    private String base64Img; // 非持久化字段，

    public LookCafeVO() {
    }

    // Getters and Setters
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

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
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
}
