package com.cognizant;

public class Fund {
    private String name;
    private long current_amount;


    public Fund(long current_amount,  String name) {
        this.current_amount = current_amount;
        this.name=name;

    }

    public long getCurrent_amount() {
        return current_amount;
    }

    public void setCurrent_amount(long current_amount) {
        this.current_amount = current_amount;
    }


    public String getName() {
        return name;
    }

    public long getTotal() {
        return current_amount;
    }

    public void addDeposit(long deposit_amount) {
        current_amount+=deposit_amount;
    }
}
