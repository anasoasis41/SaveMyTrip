package com.riahi.savemytrip.todolist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.riahi.savemytrip.models.Item;
import com.riahi.savemytrip.models.User;
import com.riahi.savemytrip.repositories.ItemDataRepository;
import com.riahi.savemytrip.repositories.UserDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by iSDT-Anas on 03/05/2018.
 */

public class ItemViewModel extends ViewModel {

    // REPOSITORIES
    private final ItemDataRepository itemDataSource;
    private final UserDataRepository userDataSource;
    private final Executor executor;

    // DATA
    @Nullable
    private LiveData<User> currentUser;

    public ItemViewModel(ItemDataRepository itemDataSource, UserDataRepository userDataSource, Executor executor) {
        this.itemDataSource = itemDataSource;
        this.userDataSource = userDataSource;
        this.executor = executor;
    }

    // initialiser notre ViewModel dès que l'activité se crée et qui sera donc appelée à l'intérieur de sa méthode onCreate()
    public void init(long userId) {
        if (this.currentUser != null) {
            return;
        }
        currentUser = userDataSource.getUser(userId);
    }

    // -------------
    // FOR USER
    // -------------

    public LiveData<User> getUser(long userId) {
        return this.currentUser;
    }

    // -------------
    // FOR ITEM
    // -------------

    public LiveData<List<Item>> getItems(long userId) {
        return itemDataSource.getItems(userId);
    }

    public void createItem(Item item) {
        executor.execute(() -> {
            itemDataSource.createItem(item);
        });
    }

    public void deleteItem(long itemId) {
        executor.execute(() -> {
            itemDataSource.deleteItem(itemId);
        });
    }

    public void updateItem(Item item) {
        executor.execute(() -> {
            itemDataSource.updateItem(item);
        });
    }
}
