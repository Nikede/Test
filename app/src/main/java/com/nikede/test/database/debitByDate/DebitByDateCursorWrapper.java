package com.nikede.test.database.debitByDate;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nikede.test.Models.DebitByDate;
import com.nikede.test.database.debitByDate.DebitByDateSchema.DebitByDateTable;

import java.util.Date;
import java.util.UUID;

public class DebitByDateCursorWrapper extends CursorWrapper {

    public DebitByDateCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public DebitByDate getDebitByDate() {
        String uuidString = getString(getColumnIndex(DebitByDateTable.Cols.UUID));
        double sum = getDouble(getColumnIndex(DebitByDateTable.Cols.SUM));
        Date date = new Date(getString(getColumnIndex(DebitByDateTable.Cols.DATE)));

        DebitByDate debitByDate = new DebitByDate(UUID.fromString(uuidString), sum, date);

        return debitByDate;
    }
}
