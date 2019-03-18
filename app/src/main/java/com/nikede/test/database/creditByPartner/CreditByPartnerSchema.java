package com.nikede.test.database.creditByPartner;

public class CreditByPartnerSchema {
    public static final class CreditByPartnerTable {
        public static final String NAME = "creditsByPartner";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String SUM = "sum";
            public static final String PARTNER = "partner";
        }
    }
}
