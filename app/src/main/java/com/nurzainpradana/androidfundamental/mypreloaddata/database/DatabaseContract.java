package com.nurzainpradana.androidfundamental.mypreloaddata.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_NAME = "table_mahasiswa";

    public static final class MahasiswaColumns implements BaseColumns {
        // Mahasiswa Nama
        public static String NAMA = "nama";

        //Mahasiswa NIM
        public static String NIM = "nim";

    }
}
