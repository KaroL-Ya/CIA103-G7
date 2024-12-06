package com.admin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="func")
public class AdminFuncVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="func_Id",updatable = false)
    private Integer func_Id;

    @Column(name ="func_name")
    private String func_Name;

    public AdminFuncVO() {
        super();
    }

    public AdminFuncVO(Integer func_Id, String func_Name) {
        super();
        this.func_Id = func_Id;
        this.func_Name = func_Name;
    }

    public Integer getFunc_Id() {
        return func_Id;
    }

    public void setFunc_Id(Integer func_Id) {
        this.func_Id = func_Id;
    }

    public String getFunc_Name() {
        return func_Name;
    }

    public void setFunc_Name(String func_Name) {
        this.func_Name = func_Name;
    }

    @Override
    public String toString() {
        return "AdminFuncVO [func_Id=" + func_Id + ", func_Name=" + func_Name + "]";
    }



}