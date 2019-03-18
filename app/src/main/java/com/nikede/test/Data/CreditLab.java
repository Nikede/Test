package com.nikede.test.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nikede.test.Models.CreditByDate;
import com.nikede.test.Models.CreditByPartner;
import com.nikede.test.database.DebitAndCreditBaseHelper;
import com.nikede.test.database.creditByDate.CreditByDateCursorWrapper;
import com.nikede.test.database.creditByDate.CreditByDateSchema;
import com.nikede.test.database.creditByDate.CreditByDateSchema.CreditByDateTable;
import com.nikede.test.database.creditByPartner.CreditByPartnerCursorWrapper;
import com.nikede.test.database.creditByPartner.CreditByPartnerSchema;
import com.nikede.test.database.creditByPartner.CreditByPartnerSchema.CreditByPartnerTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreditLab {

    private static CreditLab sCreditLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public Context getContext() {
        return mContext;
    }

    public static CreditLab get(Context context) {
        if (sCreditLab == null) {
            sCreditLab = new CreditLab(context);
        }
        return sCreditLab;
    }

    private CreditLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DebitAndCreditBaseHelper(mContext).getWritableDatabase();
    }

    public List<CreditByPartner> getCreditsByPartner() {

        List<CreditByPartner> creditsByPartner = new ArrayList<>();

        CreditByPartnerCursorWrapper cursor = queryCreditByPartner(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                creditsByPartner.add(cursor.getCreditByPartner());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return creditsByPartner;
    }

    public void addCreditByPartner(CreditByPartner creditByPartner) {
        ContentValues values = getContentValues(creditByPartner);

        mDatabase.insert(CreditByPartnerTable.NAME, null, values);
    }

    public List<CreditByDate> getCreditsByDate() {
        List<CreditByDate> creditsByDate = new ArrayList<>();

        CreditByDateCursorWrapper cursor = queryCreditByDate(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                creditsByDate.add(cursor.getCreditByDate());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return creditsByDate;
    }

    public void addCreditByDate(CreditByDate creditByDate) {
        ContentValues values = getContentValues(creditByDate);

        mDatabase.insert(CreditByDateTable.NAME, null, values);
    }



    private static ContentValues getContentValues(CreditByDate creditByDate) {
        ContentValues values = new ContentValues();
        values.put(CreditByDateTable.Cols.UUID, creditByDate.getId().toString());
        values.put(CreditByDateTable.Cols.SUM, creditByDate.getSum());
        values.put(CreditByDateTable.Cols.DATE, creditByDate.getDate().toString());
        return values;
    }

    private static ContentValues getContentValues(CreditByPartner creditByPartner) {
        ContentValues values = new ContentValues();
        values.put(CreditByPartnerTable.Cols.UUID, creditByPartner.getId().toString());
        values.put(CreditByPartnerTable.Cols.SUM, creditByPartner.getSum());
        values.put(CreditByPartnerTable.Cols.PARTNER, creditByPartner.getPartner());
        return values;
    }

    private CreditByDateCursorWrapper queryCreditByDate(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CreditByDateTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CreditByDateCursorWrapper(cursor);
    }

    private CreditByPartnerCursorWrapper queryCreditByPartner(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CreditByPartnerTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CreditByPartnerCursorWrapper(cursor);
    }

    public void clear() {
        mDatabase.delete(CreditByDateTable.NAME, null, null);
        mDatabase.delete(CreditByPartnerTable.NAME, null, null);
    }
}
