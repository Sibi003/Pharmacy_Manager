package com.bill;

public class BillDetails {
    int bill_id;
    int tablet_id;
    int quantity;
    float total_tablet_price;
    String tablet_name;

    float tablet_price;

    public float getTablet_price() {
        return tablet_price;
    }

    public void setTablet_price(float tablet_price) {
        this.tablet_price = tablet_price;
    }

    public String getTablet_name() {
        return tablet_name;
    }

    public void setTablet_name(String tablet_name) {
        this.tablet_name = tablet_name;
    }

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
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

    public float getTotal_tablet_price() {
        return total_tablet_price;
    }

    public void setTotal_tablet_price(float total_tablet_price) {
        this.total_tablet_price = total_tablet_price;
    }
}
