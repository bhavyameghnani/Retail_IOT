package com.example.lenovo.retail_iot;


public class Zone {
    String zoneID , zoneTitle , zoneData , zoneSolution , zoneImage ;
    long zoneStatusUpVote, zoneStatusDownVote, solvedCount;
    public Zone() {
    }

    public Zone(String zoneID, String zoneTitle, String zoneData, String zoneSolution, long zoneStatusUpVote, long zoneStatusDownVote,long solvedCount, String zoneImage) {
        this.zoneID = zoneID;
        this.zoneTitle = zoneTitle;
        this.zoneData = zoneData;
        this.zoneSolution = zoneSolution;
        this.zoneStatusUpVote = zoneStatusUpVote;
        this.zoneStatusDownVote = zoneStatusDownVote;
        this.solvedCount = solvedCount;
        this.zoneImage = zoneImage;
    }

    public long getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(long solvedCount) {
        this.solvedCount = solvedCount;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getZoneTitle() {
        return zoneTitle;
    }

    public void setZoneTitle(String zoneTitle) {
        this.zoneTitle = zoneTitle;
    }

    public String getZoneData() {
        return zoneData;
    }

    public void setZoneData(String zoneData) {
        this.zoneData = zoneData;
    }

    public String getZoneSolution() {
        return zoneSolution;
    }

    public void setZoneSolution(String zoneSolution) {
        this.zoneSolution = zoneSolution;
    }



    public long getZoneStatusUpVote() {
        return zoneStatusUpVote;
    }

    public void setZoneStatusUpVote(long zoneStatusUpVote) {
        this.zoneStatusUpVote = zoneStatusUpVote;
    }

    public long getZoneStatusDownVote() {
        return zoneStatusDownVote;
    }

    public void setZoneStatusDownVote(long zoneStatusDownVote) {
        this.zoneStatusDownVote = zoneStatusDownVote;
    }

    public String getZoneImage() {
        return zoneImage;
    }

    public void setZoneImage(String zoneImage) {
        this.zoneImage = zoneImage;
    }
}