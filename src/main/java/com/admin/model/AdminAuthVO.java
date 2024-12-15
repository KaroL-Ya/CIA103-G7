//package com.admin.model;
//
//import java.io.Serializable;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//// 複合主鍵的永續類別 annotation 設定可參考以下程式碼與註解說明，有 @IdClass 與 @EmbeddedId 兩種設定方式都可以
//
//@Entity
//@Table(name = "adminauth")
////@IdClass(AdminAuthVO.CompositeDetail.class)
//public class AdminAuthVO {
//	
////	@ManyToOne
////	@JoinColumn(name = "admin_Id", insertable = false, updatable = false)
////	private AdminVO admin;
////
////	@ManyToOne
////	@JoinColumn(name = "func_Id", insertable = false, updatable = false)
////	private AdminFuncVO adminFunc;
////
////	public AdminVO getAdmin() {
////	    return admin;
////	}
////
////	public void setAdmin(AdminVO admin) {
////	    this.admin = admin;
////	}
////
////	public AdminFuncVO getAdminFunc() {
////	    return adminFunc;
////	}
////
////	public void setAdminFunc(AdminFuncVO adminFunc) {
////	    this.adminFunc = adminFunc;
////	}
//
//
//	@Id 
//	@Column(name = "admin_Id")
////	@ManyToMany
//	private Integer admin_Id;
//
//	@Id
//	@Column(name = "func_Id")
//	private Integer func_Id;
//
//	// 特別加上對複合主鍵物件的 getter / setter
//	public CompositeDetail getCompositeKey() {
//		return new CompositeDetail(admin_Id, func_Id);
//	}
//
//	public void setCompositeKey(CompositeDetail key) {
//		this.admin_Id = key.getAdmin_Id();
//		this.func_Id = key.getFunc_Id();
//	}
//	
//	public Integer getAdmin_Id() {
//		return admin_Id;
//	}
//
//	public void setAdmin_Id(Integer admin_Id) {
//		this.admin_Id = admin_Id;
//	}
//
//	public Integer getFunc_Id() {
//		return func_Id;
//	}
//
//	public void setFunc_Id(Integer func_Id) {
//		this.func_Id = func_Id;
//	}
//
//	// 需要宣告一個有包含複合主鍵屬性的類別，並一定要實作 java.io.Serializable 介面
//	static class CompositeDetail implements Serializable {
//		private static final long serialVersionUID = 1L;
//
//		private Integer admin_Id;
//		private Integer func_Id;
//		
//		// 一定要有無參數建構子
//		public CompositeDetail() {
//			super();
//		}
//
//		public CompositeDetail(Integer admin_Id, Integer func_Id) {
//			super();
//			this.admin_Id = admin_Id;
//			this.func_Id = func_Id;
//		}
//
//		public Integer getAdmin_Id() {
//			return admin_Id;
//		}
//
//		public void setAdmin_Id(Integer admin_Id) {
//			this.admin_Id = admin_Id;
//		}
//
//		public Integer getFunc_Id() {
//			return func_Id;
//		}
//
//		public void setFunc_Id(Integer func_Id) {
//			this.func_Id = func_Id;
//		}
//
//		// 一定要 override 此類別的 hashCode() 與 equals() 方法！
//		@Override
//		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + ((admin_Id == null) ? 0 : admin_Id.hashCode());
//			result = prime * result + ((func_Id == null) ? 0 : func_Id.hashCode());
//			return result;
//		}
//
//		@Override
//		public boolean equals(Object obj) {
//			if (this == obj)
//				return true;
//
//			if (obj != null && getClass() == obj.getClass()) {
//				CompositeDetail compositeKey = (CompositeDetail) obj;
//				if (admin_Id.equals(compositeKey.admin_Id) && func_Id.equals(compositeKey.func_Id)) {
//					return true;
//				}
//			}
//
//			return false;
//		}
//
//	}
//
//}