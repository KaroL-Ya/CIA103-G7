package com.mall.memOrder;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.DecimalMax;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "ORDERS")
public class MemOrderVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer orderId;
    private Integer memId;
    private Integer cafeId;
    private Date date;
    private Integer amount;
    private Integer pcoId;
    private Integer ccoId;
    private Integer discount;
    private Integer paid;
    private String memo;
    private Date shipDate;
    private String trackNo;
    private Byte status;

    @Id
    @Column(name = "ORDER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEM_ID", referencedColumnName = "MEM_ID")
    public Integer getMemId() {
        return memId;
    }

    public void setMemId(Integer memId) {
        this.memId = memId;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAFE_ID", referencedColumnName = "CAFE_ID")
    public Integer getCafeId() {
        return cafeId;
    }

    public void setCafeId(Integer cafeId) {
        this.cafeId = cafeId;
    }

    @Column(name = "DATE")
    @NotNull(message = "訂單日期: 請勿空白")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "AMOUNT")
    @NotNull(message = "總金額: 請勿空白")
    @DecimalMin(value = "1", message = "總金額: 不能小於{value}")
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Column(name = "PCO_ID")
    public Integer getPcoId() {
        return pcoId;
    }

    public void setPcoId(Integer pcoId) {
        this.pcoId = pcoId;
    }

    @Column(name = "CCO_ID")
    public Integer getCcoId() {
        return ccoId;
    }

    public void setCcoId(Integer ccoId) {
        this.ccoId = ccoId;
    }

    @Column(name = "DISCOUNT")
    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    @Column(name = "PAID")
    @NotNull(message = "實付金額: 請勿空白")
    @DecimalMin(value = "1", message = "實付金額: 不能小於1")
    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    @Column(name = "MEMO")
    @Size(max = 100, message = "訂單備註: 不能超過100字")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Column(name = "SHIPDATE")
    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    @Column(name = "TRACKNO")
    @Size(max = 50, message = "貨運追蹤號碼: 不能超過50字")
    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    @Column(name = "STATUS")
    @NotNull
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

}