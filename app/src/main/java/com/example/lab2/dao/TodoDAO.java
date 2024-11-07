package com.example.lab2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lab2.database.DbHelper;
import com.example.lab2.models.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

    private DbHelper dbHelper;
    private SQLiteDatabase database;
    public TodoDAO(Context context){
        dbHelper=new DbHelper(context);
        database=dbHelper.getWritableDatabase();
    }
    public boolean insertTodo(Todo obj){
        ContentValues values=new ContentValues();
        values.put("title",obj.getTitle());
        values.put("content",obj.getContent());
        values.put("date",obj.getDate());
        values.put("type",obj.getType());
        values.put("status",obj.getStatus());
        long check=database.insert("TODO",null,values);
        return check!=-1;
    }
    public boolean updateTodo(Todo obj){
        ContentValues values=new ContentValues();
        values.put("title",obj.getTitle());
        values.put("content",obj.getContent());
        values.put("date",obj.getDate());
        values.put("type",obj.getType());
        values.put("status",obj.getStatus());
        int check=database.update("TODO",values,"id=?",new String[]{String.valueOf(obj.getId())});
        return check!=-1;
    }
    public boolean updateStatus(Integer id,boolean check){
        int stausValues=check ? 1 : 0;
        ContentValues values=new ContentValues();
        values.put("status",stausValues);
        long row=database.update("TODO",values,"id=?",new String[]{String.valueOf(id)});
        return row!=-1;
    }
    public boolean deleteTodo(int id){
        int check=database.delete("TODO","id=?",new String[]{String.valueOf(id)});
        return check!=-1;
    }
    public List<Todo> getAllTodo(){
        List<Todo> list=new ArrayList<>();
        Cursor cursor=database.rawQuery("select *from TODO",null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                Todo todo=new Todo();
                todo.setId(cursor.getInt(0));
                todo.setTitle(cursor.getString(1));
                todo.setContent(cursor.getString(2));
                todo.setDate(cursor.getString(3));
                todo.setType(cursor.getString(4));
                todo.setStatus(cursor.getInt(5));
                list.add(todo);
            }
            while (cursor.moveToNext());
        }
        return list;
    }
}
