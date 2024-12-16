package com.forum.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post") // 確保資料庫中表格名稱是 "post"
public class PostVO {

    @Id // 主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    @Column(name = "POST_ID") // 確保欄位名稱為 "POST_ID"
    private Integer postId; // 貼文編號

    @Column(name = "CAFE_ID")
    private Integer cafeId; // 咖啡廳編號

    @Column(name = "MEM_ID")
    private Integer memId; // 會員編號

    @Column(name = "TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time; // 發表時間

    @Column(name = "TITLE")
    private String title; // 貼文標題

    @Column(name = "CONTENT")
    private String content; // 貼文內容

    @Column(name = "COUNT", nullable = false)
    private Integer count = 0; // 按讚人數預設為 0

    @Column(name = "STATUS", nullable = false)
    private Byte status = 1; // 假設 1 表示可用狀態

    // 無參構造函數
    public PostVO() {
        this.time = new Date(); // 設置默認為當前時間
    }

    // 帶參構造函數
    public PostVO(Integer cafeId, Integer memId, Date time, String title, String content, Integer count, Byte status) {
        this.cafeId = cafeId;
        this.memId = memId;
        this.time = time;
        this.title = title;
        this.content = content;
        this.count = count;
        this.status = status;
    }

    // Getter 和 Setter 方法
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getCafeId() {
        return cafeId;
    }

    public void setCafeId(Integer cafeId) {
        this.cafeId = cafeId;
    }

    public Integer getMemId() {
        return memId;
    }

    public void setMemId(Integer memId) {
        this.memId = memId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
