package com.example.lesson09_2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {
    @Query("SELECT * FROM contact")
    List<Contact> getAll();
    @Query("SELECT * FROM contact WHERE id = :contactId")
    Contact getContactById(int contactId);

    @Insert
    void insert(Contact... contacts);

    @Update
    void update(Contact... contacts);

    @Delete
    void delete(Contact... contacts);
}
