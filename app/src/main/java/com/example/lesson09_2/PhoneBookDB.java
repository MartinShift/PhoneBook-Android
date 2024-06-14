package com.example.lesson09_2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1)
public abstract class PhoneBookDB extends RoomDatabase {
    public abstract ContactDAO contactDAO();
}
