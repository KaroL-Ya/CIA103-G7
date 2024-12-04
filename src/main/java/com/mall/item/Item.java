package com.mall.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name = "ITEM")
public class Item implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ITEM_ID", nullable = false)
	private Integer itemId; // 主鍵，自動遞增

	@Column(name = "CAFE_ID", nullable = false)
	private Integer cafeId; // 所屬咖啡廳 ID

	@Generated(GenerationTime.ALWAYS) // 值由資料庫觸發器生成
	@Column(name = "CAFE_ITEMID", insertable = false, updatable = false)
	private Integer cafeItemId; // 每個咖啡廳內的唯一商品 ID

	@NotBlank(message = "請勿空白")
	@Pattern(regexp = "^[\u4e00-\u9fa5a-zA-Z0-9_\s]{2,30}$", message = "只能是中、英文字母、數字和_ , 且長度必需在2到30之間")
	@Column(name = "NAME", nullable = false, length = 30)
	private String name; // 商品名稱

	@NotNull(message = "不能為空")
	@Positive(message = "必須大於0")
	@Column(name = "PRICE", nullable = false)
	private Integer price; // 商品價格，需大於等於0
	
	@NotNull(message = "不能為空")
	@Column(name = "NUM", nullable = false)
	private Integer num; // 商品數量

	@Column(name = "STATUS", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer status; // 商品狀態（0 表示下架，1 表示上架）

	@NotBlank(message = "請勿空白")
	@Size(max = 250, message = "不能超過 250 字")
	@Column(name = "CONTENT", nullable = false, length = 250)
	private String content; // 商品介紹

	@Column(name = "STARS")
	private Integer stars; // 總評分

	@Column(name = "COMMENTS")
	private Integer comments; // 評論數量

//	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
//	@Column(name = "IMGID")
//	private List<ItemImg> img; // 商品圖片列表

	@Lob
	@Column(name = "COVER_IMG")
	private byte[] coverImg; // 商品封面圖片

	@Transient
	private String previewImage; // 不保存至資料庫

	public String getPreviewImage() {
	    return previewImage;
	}

	public void setPreviewImage(String previewImage) {
	    this.previewImage = previewImage;
	}
	
	@Transient
    private String coverImgUrl;

    // Getter 和 Setter
    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }



	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	// Getters and Setters
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

	// 由DB觸發器管理，不提供setCafeItemId!

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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

//	public List<ItemImg> getImg() {
//		return img;
//	}
//
//	public void setImg(List<ItemImg> img) {
//		this.img = img;
//	}

	public byte[] getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(byte[] coverImg) {
		this.coverImg = coverImg;
	}

	public Item(Integer itemId, Integer cafeId, Integer cafeItemId, String name, Integer price, Integer status,
			String content, Integer stars, Integer comments,
//			List<ItemImg> img, 
			byte[] coverImg) {
		super();
		this.itemId = itemId;
		this.cafeId = cafeId;
		this.cafeItemId = cafeItemId;
		this.name = name;
		this.price = price;
		this.status = status;
		this.content = content;
		this.stars = stars;
		this.comments = comments;
//		this.img = img;
		this.coverImg = coverImg;
	}

	public Item() {
		super();
	}

}