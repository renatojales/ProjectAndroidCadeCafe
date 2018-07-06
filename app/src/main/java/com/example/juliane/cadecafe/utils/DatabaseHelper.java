package com.example.juliane.cadecafe.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.juliane.cadecafe.modelo.Cafe;

public class DatabaseHelper extends SQLiteOpenHelper {

        private static final String TAG = "DatabaseHelper";

        private static final String DATABASE_NAME = "cafe.db";
        private static final String TABLE_NAME = "cafes_table";

        public static final String COL0 = "ID";
        public static final String COL1 = "NOME";
        public static final String COL2 = "TELEFONE";
        public static final String COL3 = "DEVICE";
        public static final String COL4 = "ENDERECO";
        public static final String COL5 = "FOTO_PERFIL";




    public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " TEXT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT )";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        //db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);

    }

   //Inserir um novo cafe no banco
    public boolean addCafe(Cafe cafe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, cafe.getNome());
        contentValues.put(COL2, cafe.getTelefone());
        contentValues.put(COL3, cafe.getDevice());
        contentValues.put(COL4, cafe.getEndereco());
        contentValues.put(COL5, cafe.getProfileImage());

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    //Att todos cafes no banco
    public Cursor getAllCafes(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


     //Att cafe quando o id = @param 'id'
    public boolean updateCafe(Cafe cafe, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, cafe.getNome());
        contentValues.put(COL2, cafe.getTelefone());
        contentValues.put(COL3, cafe.getDevice());
        contentValues.put(COL4, cafe.getEndereco());
        contentValues.put(COL5, cafe.getProfileImage());;

        int update = db.update(TABLE_NAME, contentValues, COL0 + " = ? ", new String[] {String.valueOf(id)} );

        if(update != 1) {
            return false;
        }
        else{
            return true;
        }
    }


     // Recuperar the cafe pelo id unico @param

    public Cursor getCafeID(Cafe cafe){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME  +
                " WHERE " + COL1 + " = '" + cafe.getNome() + "'" +
                " AND " + COL2 + " = '" + cafe.getTelefone() + "'";
        return db.rawQuery(sql, null);
    }

    public Integer deleteCafe(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {String.valueOf(id)});
    }

}







