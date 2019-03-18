package com.nikede.test.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.nikede.test.database.debitByDate.DebitByDateSchema.DebitByDateTable;
import com.nikede.test.database.debitByPartner.DebitByPartnerSchema.DebitByPartnerTable;
import com.nikede.test.database.creditByDate.CreditByDateSchema.CreditByDateTable;
import com.nikede.test.database.creditByPartner.CreditByPartnerSchema.CreditByPartnerTable;

public class DebitAndCreditBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "debitAndCredit.db";

    public DebitAndCreditBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DebitByDateTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                DebitByDateTable.Cols.UUID + ", " +
                DebitByDateTable.Cols.SUM + ", " +
                DebitByDateTable.Cols.DATE + ")"
        );

        db.execSQL("create table " + DebitByPartnerTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                DebitByPartnerTable.Cols.UUID + ", " +
                DebitByPartnerTable.Cols.SUM + ", " +
                DebitByPartnerTable.Cols.PARTNER + ")"
        );

        db.execSQL("create table " + CreditByDateTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CreditByDateTable.Cols.UUID + ", " +
                CreditByDateTable.Cols.SUM + ", " +
                CreditByDateTable.Cols.DATE + ")"
        );

        db.execSQL("create table " + CreditByPartnerTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CreditByPartnerTable.Cols.UUID + ", " +
                CreditByPartnerTable.Cols.SUM + ", " +
                CreditByPartnerTable.Cols.PARTNER + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
