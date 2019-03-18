package com.nikede.test.Models;

import java.util.Date;
import java.util.UUID;

public class CreditByDate {

    private UUID mId;
    private double mSum;
    private Date mDate;

    public CreditByDate(UUID id, double sum, Date date) {
        mId = id;
        mSum = sum;
        mDate = date;
    }

    public CreditByDate() {
        mId = UUID.randomUUID();
    }

    public double getSum() {
        return mSum;
    }

    public Date getDate() {
        return mDate;
    }

    public void setSum(double sum) {
        mSum = sum;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }
}
