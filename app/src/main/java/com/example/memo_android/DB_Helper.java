package com.example.memo_android;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB_Helper extends SQLiteOpenHelper {

    public DB_Helper(Context context) {
        super(context, "styleDB.db", null, 1);
    }

    /**
     * 테이블 생성
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE  styleDB (_num INTEGER PRIMARY KEY AUTOINCREMENT, filename TEXT, isBold TEXT, size TEXT);");
    }
    //DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    /**
     * 등록
     * @param fileName 파일 이름
     * @param isBold 스타일
     * @param size 크기
     */
    public void insert(String fileName, String isBold, String size){
        //읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        //DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO styleDB VALUES(null, '"+ fileName + "', '" + isBold + "', '" + size + "');");
        db.close();
    }

    /**
     * 수정
     * @param fileName 파일 이름
     * @param size 크기
     */
    public void update(String fileName, String isBold, String size ){
        SQLiteDatabase db = getWritableDatabase();
        //입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("styleDB styleDB SET isBold= '"+ isBold +"' SET size= '"+ size +"' WHERE id='" + fileName + "';");
        db.close();
    }

    public void delete(String fileName){
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제

        db.execSQL("DELETE FROM styleDB WHERE fileName= '"+ fileName + "';");
        db.close();
    }

    /**
     *
     */
    public Cursor getUserList(){
        //읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        //DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT fileName, isBold, size FROM USER_INFO ORDER BY fileName", null);

        return cursor;
    }


}

