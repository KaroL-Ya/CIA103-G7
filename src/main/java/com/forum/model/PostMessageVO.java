package com.forum.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_message")
public class PostMessageVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSTMG_ID") // 確保與資料庫中的列名稱一致
    private Integer postmgId; // 留言編號

    @Column(name = "POST_ID", nullable = false)
    private Integer postId; // 貼文編號

    @Column(name = "MEM_ID", nullable = false)
    private Integer memId; // 會員編號

    @Column(name = "CAFE_ID", nullable = false)
    private Integer cafeId; // 咖啡廳編號

    @Column(name = "MGCONTENT", nullable = false)
    private String mgContent; // 留言內容

    @Column(name = "MGUPDATE", nullable = false)
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

    // Getter 和 Setter 方法
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
