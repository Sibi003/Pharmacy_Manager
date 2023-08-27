package com.stockDetails;

public class IncomingStocks {
    int stock_id;
    int tablet_id;
    int quantity;
    String incoming_date;
    float total_stock_price;

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public int getTablet_id() {
        return tablet_id;
    }

    public void setTablet_id(int tablet_id) {
        this.tablet_id = tablet_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIncoming_date() {
        return incoming_date;
    }

    public void setIncoming_date(String incoming_date) {
        this.incoming_date = incoming_date;
    }

    public float getTotal_stock_price() {
        return total_stock_price;
    }

    public void setTotal_stock_price(float total_stock_price) {
        this.total_stock_price = total_stock_price;
    }
}
