package com.riahi.savemytrip.repositories;

import android.arch.lifecycle.LiveData;

import com.riahi.savemytrip.database.dao.UserDao;
import com.riahi.savemytrip.models.User;

/**
 * Created by iSDT-Anas on 03/05/2018.
 */

public class UserDataRepository {

    private final UserDao userDao;

    public UserDataRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    // --- GET USER ---
    public LiveData<User> getUser(long userId) {
        return this.userDao.getUser(userId);
    }
}
