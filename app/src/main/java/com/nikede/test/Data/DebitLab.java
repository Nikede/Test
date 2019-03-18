package com.nikede.test.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nikede.test.Models.DebitByDate;
import com.nikede.test.Models.DebitByPartner;
import com.nikede.test.database.DebitAndCreditBaseHelper;
import com.nikede.test.database.debitByDate.DebitByDateCursorWrapper;
import com.nikede.test.database.debitByDate.DebitByDateSchema.DebitByDateTable;
import com.nikede.test.database.debitByPartner.DebitByPartnerCursorWrapper;
import com.nikede.test.database.debitByPartner.DebitByPartnerSchema;
import com.nikede.test.database.debitByPartner.DebitByPartnerSchema.DebitByPartnerTable;

import java.util.ArrayList;
import java.util.List;

public class DebitLab {

    private static DebitLab sDebitLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public Context getContext() {
        return mContext;
    }

    public static DebitLab get(Context context) {
        if (sDebitLab == null) {
            sDebitLab = new DebitLab(context);
        }
        return sDebitLab;
    }

    private DebitLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DebitAndCreditBaseHelper(mContext).getWritableDatabase();
    }

    public List<DebitByPartner> getDebitsByPartner() {

        List<DebitByPartner> debitsByPartner = new ArrayList<>();

        DebitByPartnerCursorWrapper cursor = queryDebitByPartner(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                debitsByPartner.add(cursor.getDebitByPartner());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return debitsByPartner;
    }

    public void addDebitByPartner(DebitByPartner debitByPartner) {
        ContentValues values = getContentValues(debitByPartner);

        mDatabase.insert(DebitByPartnerTable.NAME, null, values);
    }

    public List<DebitByDate> getDebitsByDate() {
        List<DebitByDate> debitsByDate = new ArrayList<>();

        DebitByDateCursorWrapper cursor = queryDebitByDate(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                debitsByDate.add(cursor.getDebitByDate());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return debitsByDate;
    }

    public void addDebitByDate(DebitByDate debitByDate) {
        ContentValues values = getContentValues(debitByDate);

        mDatabase.insert(DebitByDateTable.NAME, null, values);
    }

    private static ContentValues getContentValues(DebitByDate debitByDate) {
        ContentValues values = new ContentValues();
        values.put(DebitByDateTable.Cols.UUID, debitByDate.getId().toString());
        values.put(DebitByDateTable.Cols.SUM, debitByDate.getSum());
        values.put(DebitByDateTable.Cols.DATE, debitByDate.getDate().toString());
        return values;
    }

    private static ContentValues getContentValues(DebitByPartner debitByPartner) {
        ContentValues values = new ContentValues();
        values.put(DebitByPartnerTable.Cols.UUID, debitByPartner.getId().toString());
        values.put(DebitByPartnerTable.Cols.SUM, debitByPartner.getSum());
        values.put(DebitByPartnerTable.Cols.PARTNER, debitByPartner.getPartner());
        return values;
    }

    public void clear() {
        mDatabase.delete(DebitByDateTable.NAME, null, null);
        mDatabase.delete(DebitByPartnerTable.NAME, null, null);
    }

    private DebitByDateCursorWrapper queryDebitByDate(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DebitByDateTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new DebitByDateCursorWrapper(cursor);
    }

    private DebitByPartnerCursorWrapper queryDebitByPartner(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DebitByPartnerTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new DebitByPartnerCursorWrapper(cursor);
    }
}
