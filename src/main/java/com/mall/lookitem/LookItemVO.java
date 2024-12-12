package com.mall.lookitem;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ITEM")
public class LookItemVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ITEM_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer itemId;

    @Column(name="CAFE_ID")
    @NotEmpty(message="咖啡廳編號: 請勿空白")
    private Integer cafeId;

    @Column(name="CAFE_ITEMID")
    @NotEmpty(message="本店商品標號:請勿空白")
    private Integer cafeItemId;

    @Column(name="NAME")
    @NotEmpty(message="商品名稱:請勿空白")
    @Size(min = 1, max = 20, message = "商品名稱: 長度必須在1到20之間")
    private String name;

    @Column(name="PRICE")
    @NotEmpty(message="商品單價:請勿空白")
    @Min(value = 1, message = "商品價格: 不能低於1")
    private Integer price;

    @Column(name="NUM")
    @NotEmpty(message="商品數量:請勿空白")
    private Integer num;

    @Column(name = "STATUS")
    @NotEmpty(message = "商品狀態: 請選擇狀態")
    private Byte status;

    @Column(name = "CONTENT")
    @NotEmpty(message = "商品敘述: 請勿空白")
    private String content;

    @Column(name = "STARS")
    private Integer stars;

    @Column(name = "COMMENTS")
    private Integer comments;

    @Column(name = "COVER_IMG")
    @NotNull(message=": 請上傳照片")
    private byte[] coverImg;

    @Transient
    private String base64CoverImg; //非持久化字段
    
    private String categoryId; 

    public LookItemVO() {
    }

   
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getCafeId() {
        return cafeId;
    }

    public void setCafeId(Integer cafeId) {
        this.cafeId = cafeId;
    }

    public Integer getCafeItemId() {
        return cafeItemId;
    }

    public void setCafeItemId(Integer cafeItemId) {
        this.cafeItemId = cafeItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public byte[] getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(byte[] coverImg) {
        this.coverImg = coverImg;
    }

    
    public String getBase64CoverImg() {
        return base64CoverImg;
    }

    public void setBase64CoverImg(String base64CoverImg) {
        this.base64CoverImg = base64CoverImg;
    }

    public String getCategoryId() {
    	return categoryId;
    }
    public void setCategoryId() {
    	this.categoryId = categoryId;
    }
    	
}
