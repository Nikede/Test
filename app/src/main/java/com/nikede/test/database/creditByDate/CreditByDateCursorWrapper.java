package com.nikede.test.database.creditByDate;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nikede.test.Models.CreditByDate;
import com.nikede.test.database.creditByDate.CreditByDateSchema.CreditByDateTable;

import java.util.Date;
import java.util.UUID;

public class CreditByDateCursorWrapper extends CursorWrapper {

    public CreditByDateCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CreditByDate getCreditByDate() {
        String uuidString = getString(getColumnIndex(CreditByDateTable.Cols.UUID));
        double sum = getDouble(getColumnIndex(CreditByDateTable.Cols.SUM));
        Date date = new Date(getString(getColumnIndex(CreditByDateTable.Cols.DATE)));

        CreditByDate creditByDate = new CreditByDate(UUID.fromString(uuidString), sum, date);

        return creditByDate;
    }
}
