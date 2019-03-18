package com.nikede.test.database.creditByPartner;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nikede.test.Models.CreditByPartner;

import java.util.UUID;

public class CreditByPartnerCursorWrapper extends CursorWrapper {

    public CreditByPartnerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CreditByPartner getCreditByPartner() {
        String uuidString = getString(getColumnIndex(CreditByPartnerSchema.CreditByPartnerTable.Cols.UUID));
        double sum = getDouble(getColumnIndex(CreditByPartnerSchema.CreditByPartnerTable.Cols.SUM));
        String partner = getString(getColumnIndex(CreditByPartnerSchema.CreditByPartnerTable.Cols.PARTNER));

        CreditByPartner creditByPartner = new CreditByPartner(UUID.fromString(uuidString), sum, partner);

        return creditByPartner;
    }
}
