package com.riahi.savemytrip.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.riahi.savemytrip.database.dao.ItemDao;
import com.riahi.savemytrip.database.dao.UserDao;
import com.riahi.savemytrip.models.Item;
import com.riahi.savemytrip.models.User;

/**
 * Created by iSDT-Anas on 02/05/2018.
 */

@Database(entities = {Item.class, User.class}, version = 1, exportSchema = false)
public abstract class SaveMyTripDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile SaveMyTripDatabase INSTANCE;

    // --- DAO ---
    public abstract ItemDao itemDao();
    public abstract UserDao userDao();

    // --- INSTANCE ---
    public static SaveMyTripDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SaveMyTripDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SaveMyTripDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ---

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 1);
                contentValues.put("username", "Anas");
                contentValues.put("urlPicture","https://media.licdn.com/dms/image/C4E03AQHLwbWBmqLB7Q/profile-displayphoto-shrink_200_200/0?e=1531353600&v=beta&t=0adlxJmkjH3yeigpSRZleSr1cE6w-MRdOs5fkqoYGlo");

                db.insert("User", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }
}
