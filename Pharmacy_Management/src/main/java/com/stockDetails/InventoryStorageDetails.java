package com.stockDetails;

public class InventoryStorageDetails {

    String location_id;
    int tablet_id;
    int remaining_quantity;

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public int getTablet_id() {
        return tablet_id;
    }

    public void setTablet_id(int tablet_id) {
        this.tablet_id = tablet_id;
    }

    public int getRemaining_quantity() {
        return remaining_quantity;
    }

    public void setRemaining_quantity(int remaining_quantity) {
        this.remaining_quantity = remaining_quantity;
    }
}
