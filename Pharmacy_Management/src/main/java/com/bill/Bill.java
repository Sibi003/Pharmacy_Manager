package com.bill;

public class Bill {
    int bill_id;
    String bill_date;
    float bill_amount;

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public float getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(float bill_amount) {
        this.bill_amount = bill_amount;
    }
}
