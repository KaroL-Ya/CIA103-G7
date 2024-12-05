package com.mall.cco.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;  // 使用 LocalDate 替代 java.sql.Date

@Entity
@Table(name = "CAFE_COUPON") // 對應資料庫的表名
public class CcoVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id // 主鍵
    @Column(name = "CCO_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成策略
    private Integer ccoId;

    @Column(name = "CAFE_ID")
    @NotNull(message = "咖啡廳編號: 請勿空白")
    private Integer cafeId;

    @Column(name = "NAME")
    @NotNull(message = "優惠券名稱: 請勿空白")
    @Size(min = 2, max = 50, message = "優惠券名稱: 長度必需在{min}到{max}之間")
    private String name;

    @Column(name = "DISCOUNT")
    @NotNull(message = "折扣金額: 請勿空白")
    @Min(value = 0, message = "折扣金額: 不能小於{value}")
    @Max(value = 1000, message = "折扣金額: 不能超過{value}")
    private Integer discount;

    @Column(name = "MINSPEND")
    @NotNull(message = "最低消費金額: 請勿空白")
    @Min(value = 0, message = "最低消費金額: 不能小於{value}")
    private Integer minSpend = 0;  // 預設為 0，表示無最低消費限制

    @Column(name = "STARTDATE")
    @NotNull(message = "開始有效日期: 請勿空白")
    @DateTimeFormat(pattern = "yyyy-MM-dd")  // 使用 LocalDate 格式
    private LocalDate startDate;

    @Column(name = "ENDDATE")
    @NotNull(message = "結束有效日期: 請勿空白")
    @DateTimeFormat(pattern = "yyyy-MM-dd")  // 使用 LocalDate 格式
    private LocalDate endDate;

    // 預設建構子
    public CcoVO() {}

    // 有參數的建構子
    public CcoVO(Integer ccoId, Integer cafeId, String name, Integer discount, Integer minSpend, LocalDate startDate, LocalDate endDate) {
        this.ccoId = ccoId;
        this.cafeId = cafeId;
        this.name = name;
        this.discount = discount;
        this.minSpend = minSpend;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Integer getCcoId() {
        return ccoId;
    }

    public void setCcoId(Integer ccoId) {
        this.ccoId = ccoId;
    }

    public Integer getCafeId() {
        return cafeId;
    }

    public void setCafeId(Integer cafeId) {
        this.cafeId = cafeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
