//package com.admin.model;
//
//import java.io.Serializable;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "admin")
//public class AdminOldVO implements Serializable{
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name ="admin_Id",updatable = false)
//    private Integer admin_Id;
//
//    @Column(name ="admin_ac")
//    private String admin_Ac;
//
//    @Column(name ="admin_pw")
//    private String admin_Pw;
//
//    @Column(name ="admin_name")
//    private String admin_Name;
//
//    @Column(name ="admin_status",columnDefinition = "bit")
//    private Integer admin_Status;
//
//    public AdminOldVO() {
//        super();
//    }
//
//    public AdminOldVO(String admin_Ac, String admin_Pw, String admin_Name) {
//        super();
//        this.admin_Ac = admin_Ac;
//        this.admin_Pw = admin_Pw;
//        this.admin_Name = admin_Name;
//    }
//
//    public AdminOldVO(Integer admin_Id, String admin_Ac, String admin_Pw, String admin_Name, Integer admin_Status) {
//        super();
//        this.admin_Id = admin_Id;
//        this.admin_Ac = admin_Ac;
//        this.admin_Pw = admin_Pw;
//        this.admin_Name = admin_Name;
//        this.admin_Status = admin_Status;
//    }
//    
//    public Integer getAdmin_Id() {
//        return admin_Id;
//    }
//
//    public void setAdmin_Id(Integer admin_Id) {
//        this.admin_Id = admin_Id;
//    }
//
//    public String getAdmin_Ac() {
//        return admin_Ac;
//    }
//
//    public void setAdmin_Ac(String admin_Ac) {
//        this.admin_Ac = admin_Ac;
//    }
//
//    public String getAdmin_Pw() {
//        return admin_Pw;
//    }
//
//    public void setAdmin_Pw(String admin_Pw) {
//        this.admin_Pw = admin_Pw;
//    }
//
//    public String getAdmin_Name() {
//        return admin_Name;
//    }
//
//    public void setAdmin_Name(String admin_Name) {
//        this.admin_Name = admin_Name;
//    }
//
//    public Integer getAdmin_Status() {
//        return admin_Status;
//    }
//
//    public void setAdmin_Status(Integer admin_Status) {
//        this.admin_Status = admin_Status;
//    }
//
//    @Override
//    public String toString() {
//        return "AdminVO [admin_Id=" + admin_Id + ", admin_Ac=" + admin_Ac + ", admin_Pw=" + admin_Pw + ", admin_Name="
//                + admin_Name + ", admin_Status=" + admin_Status + "]";
//    }
//
//}