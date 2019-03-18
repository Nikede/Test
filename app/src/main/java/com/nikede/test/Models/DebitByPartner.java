package com.nikede.test.Models;

import java.util.UUID;

public class DebitByPartner {

    private UUID mId;
    private double mSum;
    private String mPartner;

    public DebitByPartner(UUID id, double sum, String partner) {
        mId = id;
        mSum = sum;
        mPartner = partner;
    }

    public double getSum() {
        return mSum;
    }

    public String getPartner() {
        return mPartner;
    }

    public DebitByPartner() {
        mId = UUID.randomUUID();
    }

    public void setSum(double sum) {
        mSum = sum;
    }

    public void setPartner(String partner) {
        mPartner = partner;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }
}
