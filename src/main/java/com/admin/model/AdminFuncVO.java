package com.admin.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
    
    @ManyToMany
	@JoinTable(
				name = "adminauth",
				joinColumns = { @JoinColumn(name = "FUNC_ID", referencedColumnName = "FUNC_ID") },
				inverseJoinColumns = { @JoinColumn(name = "ADMIN_ID", referencedColumnName = "ADMIN_ID") }
              )
    private Set<AdminVO> func_Admins;
    
    
    
//    @OneToMany(mappedBy = "adminFunc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<AdminAuthVO> adminAuths = new HashSet<>();
//
//    public Set<AdminAuthVO> getAdminAuths() {
//        return adminAuths;
//    }
//
//    public void setAdminAuths(Set<AdminAuthVO> adminAuths) {
//        this.adminAuths = adminAuths;
//    }


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
    
    public Set<AdminVO> getFunc_Admins() {
		return func_Admins;
	}

	public void setFunc_Admins(Set<AdminVO> func_Admins) {
		this.func_Admins = func_Admins;
	}

	@Override
    public String toString() {
        return "AdminFuncVO [func_Id=" + func_Id + ", func_Name=" + func_Name + "]";
    }



}