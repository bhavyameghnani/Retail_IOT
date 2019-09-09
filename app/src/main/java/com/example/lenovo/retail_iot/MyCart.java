package com.example.lenovo.retail_iot;

public class MyCart {
    String userId, productTitle;
    Long productId, productCost;


    public MyCart(String userId, String productTitle, Long productId, Long productCost) {
        this.userId = userId;
        this.productTitle = productTitle;
        this.productId = productId;
        this.productCost = productCost;
    }

    public MyCart() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductCost() {
        return productCost;
    }

    public void setProductCost(Long productCost) {
        this.productCost = productCost;
    }
}
