package com.example.easyorder;

public class BusinessData {
    private int bizNo, martNo, prodNo, amount=0, price=0, uPrice, totAmount=0, totPrice=0;
    private String crtDate, prodNm, martNm;

    public int getBizNo() {
        return bizNo;
    }

    public void setBizNo(int bizNo) {
        this.bizNo = bizNo;
    }

    public int getMartNo() {
        return martNo;
    }

    public void setMartNo(int martNo) {
        this.martNo = martNo;
    }

    public int getProdNo() {
        return prodNo;
    }

    public void setProdNo(int prodNo) {
        this.prodNo = prodNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCrtDate() {
        return crtDate;
    }

    public void setCrtDate(String crtDate) {
        this.crtDate = crtDate;
    }

    public int getUPrice() {
        return uPrice;
    }

    public void setUPrice(int uPrice) {
        this.uPrice = uPrice;
    }

    public String getProdNm() {
        return prodNm;
    }

    public void setProdNm(String prodNm) {
        this.prodNm = prodNm;
    }

    public int getTotAmount() {
        return totAmount;
    }

    public void setTotAmount(int totAmount) {
        this.totAmount = totAmount;
    }

    public int getTotPrice() {
        return totPrice;
    }

    public void setTotPrice(int totPrice) {
        this.totPrice = totPrice;
    }

    public String getMartNm() {
        return martNm;
    }

    public void setMartNm(String martNm) {
        this.martNm = martNm;
    }
}
