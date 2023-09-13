package com.example.wallet.entity;

public enum TransactionType {
    TRANSFER("Transfer"),
    TOP_UP("Top-Up"),
    REFUND("Refund");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }
}

