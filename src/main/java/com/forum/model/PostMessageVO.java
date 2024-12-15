package com.forum.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_message")
public class PostMessageVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postmgId; // 留言編號

    @Column(name = "post_id", nullable = false)
    private Integer postId; // 貼文編號

    @Column(name = "mem_id", nullable = false)
    private Integer memId; // 會員編號

    @Column(name = "cafe_id", nullable = false)
    private Integer cafeId; // 咖啡廳編號

    @Column(name = "mgcontent", nullable = false)
    private String mgContent; // 留言內容

    @Column(name = "mgupdate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mgUpdate; // 留言時間

    // 無參構造函數
    public PostMessageVO() {
        this.mgUpdate = new Date(); // 設置默認為當前時間
    }

    // 帶參構造函數
    public PostMessageVO(Integer postId, Integer memId, Integer cafeId, String mgContent) {
        this.postId = postId;
        this.memId = memId;
        this.cafeId = cafeId;
        this.mgContent = mgContent;
        this.mgUpdate = new Date(); // 設置默認為當前時間
    }

	public Integer getPostmgId() {
		return postmgId;
	}

	public void setPostmgId(Integer postmgId) {
		this.postmgId = postmgId;
	}

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public Integer getCafeId() {
		return cafeId;
	}

	public void setCafeId(Integer cafeId) {
		this.cafeId = cafeId;
	}

	public String getMgContent() {
		return mgContent;
	}

	public void setMgContent(String mgContent) {
		this.mgContent = mgContent;
	}

	public Date getMgUpdate() {
		return mgUpdate;
	}

	public void setMgUpdate(Date mgUpdate) {
		this.mgUpdate = mgUpdate;
	}

    
}
