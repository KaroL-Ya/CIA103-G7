package com.mall.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class CheckoutRequest {
    @JsonProperty("memId")
    private Integer memId; // 會員 ID

    @JsonProperty("cafes")
    private List<CafeOrder> cafes = new ArrayList<>(); // 默認設為空的 List，避免 null

    @JsonProperty("platformCoupon")
    private Integer platformCoupon; // 平台優惠券

    // Getter 和 Setter
    public Integer getMemId() {
        return memId;
    }

    public void setMemId(Integer memId) {
        this.memId = memId;
    }

    public List<CafeOrder> getCafes() {
        return cafes;
    }

    public void setCafes(List<CafeOrder> cafes) {
        this.cafes = cafes;
    }

    public Integer getPlatformCoupon() {
        return platformCoupon;
    }

    public void setPlatformCoupon(Integer platformCoupon) {
        this.platformCoupon = platformCoupon;
    }

    // 獲取所有商品 ID 的方法
    public List<Integer> getItemIds() {
        List<Integer> itemIds = new ArrayList<>();
        for (CafeOrder cafeOrder : cafes) {
            for (CartItemRequest item : cafeOrder.getItems()) {
                itemIds.add(item.getItemId());
            }
        }
        return itemIds;
    }

    // 內部類表示每間咖啡廳的訂單
    public static class CafeOrder {
        @JsonProperty("cafeId")
        private Integer cafeId; // 咖啡廳 ID

        @JsonProperty("items")
        private List<CartItemRequest> items; // 商品資料

        @JsonProperty("coupon")
        private Integer coupon; // 咖啡廳優惠券

        @JsonProperty("shippingFee")
        private Integer shippingFee; // 運費

        @JsonProperty("remark")
        private String remark; // 備註

        // Getter 和 Setter
        public Integer getCafeId() {
            return cafeId;
        }

        public void setCafeId(Integer cafeId) {
            this.cafeId = cafeId;
        }

        public List<CartItemRequest> getItems() {
            return items;
        }

        public void setItems(List<CartItemRequest> items) {
            this.items = items;
        }

        public Integer getCoupon() {
            return coupon;
        }

        public void setCoupon(Integer coupon) {
            this.coupon = coupon;
        }

        public Integer getShippingFee() {
            return shippingFee;
        }

        public void setShippingFee(Integer shippingFee) {
            this.shippingFee = shippingFee;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    // 商品的結構
    public static class CartItemRequest {
        @JsonProperty("itemId")
        private Integer itemId; // 商品 ID

        @JsonProperty("itemName")
        private String itemName; // 品名

        @JsonProperty("num")
        private Integer num; // 數量

        @JsonProperty("price")
        private Integer price; // 單價

        // Getter 和 Setter
        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }
}
