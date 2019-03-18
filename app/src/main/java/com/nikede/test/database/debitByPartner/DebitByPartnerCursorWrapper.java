package com.nikede.test.database.debitByPartner;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nikede.test.Models.DebitByPartner;
import com.nikede.test.database.debitByPartner.DebitByPartnerSchema.DebitByPartnerTable;

import java.util.UUID;

public class DebitByPartnerCursorWrapper extends CursorWrapper {

    public DebitByPartnerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public DebitByPartner getDebitByPartner() {
        String uuidString = getString(getColumnIndex(DebitByPartnerTable.Cols.UUID));
        double sum = getDouble(getColumnIndex(DebitByPartnerTable.Cols.SUM));
        String partner = getString(getColumnIndex(DebitByPartnerTable.Cols.PARTNER));

        DebitByPartner debitByPartner = new DebitByPartner(UUID.fromString(uuidString), sum, partner);

        return debitByPartner;
    }
}
