package com.mall.item;

import javax.persistence.*;

@Entity
@Table(name = "ITEM_IMG")
public class ItemImg {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IMG_ID")
	private Integer imgId;

	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false)
	private Item item; // 所屬商品

	@Lob
	@Column(name = "IMG", nullable = false)
	private byte[] img; // 圖片二進制數據

	// Getters and Setters

	public Integer getImgId() {
		return imgId;
	}

	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

}
