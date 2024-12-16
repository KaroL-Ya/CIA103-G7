package com.mall.memOrder;

import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
@Entity
@Table(name = "ORDER_DETAILS")
public class OrderDetailVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    // 訂單編號
    @Id
    @Column(name = "ORDER_ID")
    @NotNull(message = "訂單編號: 請勿空白")
    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    // 商品編號
    @Id
    @Column(name = "ITEM_ID")
    @NotNull(message = "商品編號: 請勿空白")
    private Integer itemId;

    public Integer getItemId() {
        return itemId;
    } 

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    // 購買數量
    @Column(name = "SALESNUM")
    @NotNull(message = "購買數量: 請勿空白")
    @Min(value = 1, message = "購買數量: 必須大於0")
    private Integer salesNum;

    public Integer getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(Integer salesNum) {
        this.salesNum = salesNum;
    }

    // 商品單價
    @Column(name = "PRICE")
    @NotNull(message = "單價: 請勿空白")
    @Min(value = 1, message = "單價: 必須大於0")
    private Integer price;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    // 是否退貨/退款
    @Column(name = "IS_RETURN")
    @NotNull(message = "是否退貨/退款: 請選擇")
    private Byte isReturn;

    public Byte getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Byte isReturn) {
        this.isReturn = isReturn;
    }

    // 退貨/退款原因
    @Column(name = "RETURN_REASON")
    private Byte returnReason;

    public Byte getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(Byte returnReason) {
        this.returnReason = returnReason;
    }

    // 退貨退款圖片
    @Column(name = "RETURN_IMG")
    private byte[] returnImg;

    public byte[] getReturnImg() {
        return returnImg;
    }

    public void setReturnImg(byte[] returnImg) {
        this.returnImg = returnImg;
    }

    // 用於顯示圖片的Base64編碼（非持久化字段）
    @Transient
    private String base64ReturnImg;

    public String getBase64ReturnImg() {
        return base64ReturnImg;
    }

    public void setBase64ReturnImg(String base64ReturnImg) {
        this.base64ReturnImg = base64ReturnImg;
    }
    
}
