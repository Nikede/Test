package com.nikede.test.Models;

import java.util.Date;
import java.util.UUID;

public class DebitByDate {

    private UUID mId;
    private double mSum;
    private Date mDate;

    public DebitByDate(UUID id, double sum, Date date) {
        mId = id;
        mSum = sum;
        mDate = date;
    }

    public double getSum() {
        return mSum;
    }

    public Date getDate() {
        return mDate;
    }

    public DebitByDate() {
        mId = UUID.randomUUID();
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
