package com.nikede.test.Models;

import java.util.UUID;

public class CreditByPartner {

    private UUID mId;
    private double mSum;
    private String mPartner;

    public CreditByPartner(UUID id, double sum, String partner) {
        this.mId = id;
        this.mSum = sum;
        this.mPartner = partner;
    }

    public double getSum() {
        return mSum;
    }

    public String getPartner() {
        return mPartner;
    }

    public CreditByPartner() {
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
