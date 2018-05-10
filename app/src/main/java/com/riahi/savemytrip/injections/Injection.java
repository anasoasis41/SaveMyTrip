package com.riahi.savemytrip.injections;

import android.content.Context;

import com.riahi.savemytrip.database.SaveMyTripDatabase;
import com.riahi.savemytrip.repositories.ItemDataRepository;
import com.riahi.savemytrip.repositories.UserDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by iSDT-Anas on 03/05/2018.
 */

public class Injection {

    public static ItemDataRepository provideItemDataSource(Context context) {
        SaveMyTripDatabase database = SaveMyTripDatabase.getInstance(context);
        return new ItemDataRepository(database.itemDao());
    }

    public static UserDataRepository provideUserDataSource(Context context) {
        SaveMyTripDatabase database = SaveMyTripDatabase.getInstance(context);
        return new UserDataRepository(database.userDao());
    }

    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        ItemDataRepository dataSourceItem = provideItemDataSource(context);
        UserDataRepository dataSourceUser = provideUserDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceItem, dataSourceUser, executor);
    }
}
