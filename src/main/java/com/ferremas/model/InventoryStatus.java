package com.ferremas.model;

public enum InventoryStatus {
    INSTOCK("In Stock"),
    OUTOFSTOCK("Out of Stock"),
    LOWSTOCK("Low Stock");

    private String text;

    InventoryStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}