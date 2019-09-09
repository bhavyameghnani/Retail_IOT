package com.example.lenovo.retail_iot;

public class CrowdStatus {
    Long clothing, footwear, grocery, kids;

    public CrowdStatus(Long clothing, Long footwear, Long grocery, Long kids) {
        this.clothing = clothing;
        this.footwear = footwear;
        this.grocery = grocery;
        this.kids = kids;
    }

    public CrowdStatus() {
    }

    public Long getClothing() {
        return clothing;
    }

    public void setClothing(Long clothing) {
        this.clothing = clothing;
    }

    public Long getFootwear() {
        return footwear;
    }

    public void setFootwear(Long footwear) {
        this.footwear = footwear;
    }

    public Long getGrocery() {
        return grocery;
    }

    public void setGrocery(Long grocery) {
        this.grocery = grocery;
    }

    public Long getKids() {
        return kids;
    }

    public void setKids(Long kids) {
        this.kids = kids;
    }
}
