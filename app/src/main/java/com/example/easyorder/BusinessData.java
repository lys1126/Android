package com.example.easyorder;

public class BusinessData {
    private int bizNo, martNo, prodNo, amount, price, uPrice;
    private String crtDate, prodNm;

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
}
