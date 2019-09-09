package com.example.lenovo.retail_iot;

public class ProductDetails {
    Long productId, productCost;
    String productTitle ;

    public ProductDetails(Long productId, Long productCost, String productTitle) {
        this.productId = productId;
        this.productCost = productCost;
        this.productTitle = productTitle;
    }

    public ProductDetails() {
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

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
}
