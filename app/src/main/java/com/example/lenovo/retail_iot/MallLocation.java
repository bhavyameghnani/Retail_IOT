package com.example.lenovo.retail_iot;

public class MallLocation {

    Long mallLat, mallLong;

    public MallLocation(Long mallLat, Long mallLong) {
        this.mallLat = mallLat;
        this.mallLong = mallLong;
    }

    public MallLocation() {
    }

    public Long getMallLat() {
        return mallLat;
    }

    public void setMallLat(Long mallLat) {
        this.mallLat = mallLat;
    }

    public Long getMallLong() {
        return mallLong;
    }

    public void setMallLong(Long mallLong) {
        this.mallLong = mallLong;
    }
}
