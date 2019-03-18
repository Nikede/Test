package com.nikede.test.database.debitByDate;

public class DebitByDateSchema {
    public static final class DebitByDateTable {
        public static final String NAME = "debitsByDate";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String SUM = "sum";
            public static final String DATE = "date";
        }
    }
}
