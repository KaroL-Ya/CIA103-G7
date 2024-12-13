//package com.mall.uco.model;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Column;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "USER_COUPON") // 對應資料庫的表名
//public class UcoVO implements java.io.Serializable {
//    private static final long serialVersionUID = 1L;
//
//    @Id // 主鍵
//    @Column(name = "UCO_NO")
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成策略
//    private Integer ucoNo;
//
//    @Column(name = "MEM_ID")
//    @NotNull(message = "會員編號: 請勿空白")
//    private Integer memId; // 會員編號
//
//    @Column(name = "PCO_ID")
//    private Integer pcoId; // 平台優惠券編號
//
//    @Column(name = "CCO_ID")
//    private Integer ccoId; // 商家優惠券編號
//
//    @Column(name = "ORDER_ID")
//    private Integer orderId; // 訂單編號
//
//    @Column(name = "NAME")
//    @NotNull(message = "優惠券名稱: 請勿空白")
//    private String name; // 優惠券名稱
//
//    @Column(name = "GETDATE")
//    @NotNull(message = "領取日期: 請勿空白")
//    private LocalDate getDate; // 領取日期
//
//    @Column(name = "STATUS")
//    @NotNull(message = "使用狀態: 請勿空白")
//    private Integer status; // 使用狀態
//
//    @Column(name = "TYPE")
//    @NotNull(message = "優惠券類型: 請勿空白")
//    private String type; // 優惠券類型
//
//    @Column(name = "DISCOUNT")
//    private Integer discount; // 折扣金額
//
//    @Column(name = "MINSPEND")
//    private Integer minSpend; // 最低消費限制
//
//    // 預設建構子
//    public UcoVO() {}
//
//    // 有參數的建構子(若您需要，則保留)
//    public UcoVO(Integer memId, Integer pcoId, Integer ccoId, Integer orderId, String name, LocalDate getDate, Integer status, String type, Integer discount, Integer minSpend) {
//        this.memId = memId;
//        this.pcoId = pcoId;
//        this.ccoId = ccoId;
//        this.orderId = orderId;
//        this.name = name;
//        this.getDate = getDate;
//        this.status = status;
//        this.type = type;
//        this.discount = discount;
//        this.minSpend = minSpend;
//    }
//
//    // Getters and Setters
//    public Integer getUcoNo() {
//        return ucoNo;
//    }
//
//    public void setUcoNo(Integer ucoNo) {
//        this.ucoNo = ucoNo;
//    }
//
//    public Integer getMemId() {
//        return memId;
//    }
//
//    public void setMemId(Integer memId) {
//        this.memId = memId;
//    }
//
//    public Integer getPcoId() {
//        return pcoId;
//    }
//
//    public void setPcoId(Integer pcoId) {
//        this.pcoId = pcoId;
//    }
//
//    public Integer getCcoId() {
//        return ccoId;
//    }
//
//    public void setCcoId(Integer ccoId) {
//        this.ccoId = ccoId;
//    }
//
//    public Integer getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(Integer orderId) {
//        this.orderId = orderId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public LocalDate getGetDate() {
//        return getDate;
//    }
//
//    public void setGetDate(LocalDate getDate) {
//        this.getDate = getDate;
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public Integer getDiscount() {
//        return discount;
//    }
//
//    public void setDiscount(Integer discount) {
//        this.discount = discount;
//    }
//
//    public Integer getMinSpend() {
//        return minSpend;
//    }
//
//    public void setMinSpend(Integer minSpend) {
//        this.minSpend = minSpend;
//    }
//}

package com.mall.uco.model;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "USER_COUPON") // 對應資料庫的表名
public class UcoVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id // 主鍵
    @Column(name = "UCO_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成策略
    private Integer ucoNo;

    @Column(name = "MEM_ID")
    @NotNull(message = "會員編號: 請勿空白")
    private Integer memId; // 會員編號

    @Column(name = "PCO_ID")
    private Integer pcoId; // 平台優惠券編號

    @Column(name = "CCO_ID")
    private Integer ccoId; // 商家優惠券編號

    @Column(name = "ORDER_ID")
    private Integer orderId; // 訂單編號

    @Column(name = "NAME")
    @NotNull(message = "優惠券名稱: 請勿空白")
    private String name; // 優惠券名稱

    @Column(name = "GETDATE")
    @NotNull(message = "領取日期: 請勿空白")
    private LocalDate getDate; // 領取日期

    @Column(name = "STATUS")
    @NotNull(message = "使用狀態: 請勿空白")
    private Integer status; // 使用狀態

    @Column(name = "TYPE")
    @NotNull(message = "優惠券類型: 請勿空白")
    private String type; // 優惠券類型

    @Column(name = "DISCOUNT")
    private Integer discount; // 折扣金額

    @Column(name = "MINSPEND")
    private Integer minSpend; // 最低消費限制

    @Transient // 不映射到資料庫的欄位
    private LocalDate startDate; // 起始日期

    @Transient // 不映射到資料庫的欄位
    private LocalDate endDate; // 結束日期

    // 預設建構子
    public UcoVO() {}

    // Getters 和 Setters
    public Integer getUcoNo() {
        return ucoNo;
    }

    public void setUcoNo(Integer ucoNo) {
        this.ucoNo = ucoNo;
    }

    public Integer getMemId() {
        return memId;
    }

    public void setMemId(Integer memId) {
        this.memId = memId;
    }

    public Integer getPcoId() {
        return pcoId;
    }

    public void setPcoId(Integer pcoId) {
        this.pcoId = pcoId;
    }

    public Integer getCcoId() {
        return ccoId;
    }

    public void setCcoId(Integer ccoId) {
        this.ccoId = ccoId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getGetDate() {
        return getDate;
    }

    public void setGetDate(LocalDate getDate) {
        this.getDate = getDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getMinSpend() {
        return minSpend;
    }

    public void setMinSpend(Integer minSpend) {
        this.minSpend = minSpend;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
