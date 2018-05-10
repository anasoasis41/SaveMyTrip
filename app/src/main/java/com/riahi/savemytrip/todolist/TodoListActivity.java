package com.riahi.savemytrip.todolist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riahi.savemytrip.R;
import com.riahi.savemytrip.base.BaseActivity;
import com.riahi.savemytrip.injections.Injection;
import com.riahi.savemytrip.injections.ViewModelFactory;
import com.riahi.savemytrip.models.Item;
import com.riahi.savemytrip.models.User;
import com.riahi.savemytrip.utils.ItemClickSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TodoListActivity extends BaseActivity  implements ItemAdapter.Listener{

    // FOR DESIGN
    @BindView(R.id.todo_list_activity_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.todo_list_activity_spinner) Spinner spinner;
    @BindView(R.id.todo_list_activity_edit_text) EditText editText;
    @BindView(R.id.todo_list_activity_header_profile_image) ImageView profileImage;
    @BindView(R.id.todo_list_activity_header_profile_text) TextView profileText;

    // 1 - FOR DATA
    private ItemViewModel itemViewModel;
    private ItemAdapter adapter;
    private static int USER_ID = 1;

    @Override
    public int getLayoutContentViewID() { return R.layout.activity_todo_list; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();
        this.configureSpinner();

        // 8 - Configure RecyclerView & ViewModel
        this.configureRecyclerView();
        this.configureViewModel();

        // 9 - Get current user & items from Database
        this.getCurrentUser(USER_ID);
        this.getItems(USER_ID);
    }

    // -------------------
    // ACTIONS
    // -------------------

    @OnClick(R.id.todo_list_activity_button_add)
    public void onClickAddButton() {
        // 7 - Create item after user clicked on button
        this.createItem();
    }

    @Override
    public void onClickDeleteButton(int position) {
        // 7 - Delete item after user clicked on button
        this.deleteItem(this.adapter.getItem(position));
    }

    // -------------------
    // DATA
    // -------------------

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel.class);
        this.itemViewModel.init(USER_ID);
    }

    // ---

    // 3 - Get Current User AVEC LAMBDAS
    private void getCurrentUser(int userId){
        this.itemViewModel.getUser(userId).observe(this, this::updateHeader);
    }

    // 3 - Get Current User SANS LAMBDAS
    /*
    private void getCurrentUser(int userId){
        this.itemViewModel.getUser(userId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                updateHeader(user);
            }
        });
    }
    */

    // ---

    // 3 - Get all items for a user
    private void getItems(int userId){
        this.itemViewModel.getItems(userId).observe(this, this::updateItemsList);
    }

    // 3 - Create a new item
    private void createItem(){
        Item item = new Item(this.editText.getText().toString(), this.spinner.getSelectedItemPosition(), USER_ID);
        this.editText.setText("");
        this.itemViewModel.createItem(item);
    }

    // 3 - Delete an item
    private void deleteItem(Item item){
        this.itemViewModel.deleteItem(item.getId());
    }

    // 3 - Update an item (selected or not)
    private void updateItem(Item item){
        item.setSelected(!item.getSelected());
        this.itemViewModel.updateItem(item);
    }

    // -------------------
    // UI
    // -------------------

    private void configureSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // 4 - Configure RecyclerView
    private void configureRecyclerView(){
        this.adapter = new ItemAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemClickSupport.addTo(recyclerView, R.layout.activity_todo_list_item)
                .setOnItemClickListener((recyclerView1, position, v) -> this.updateItem(this.adapter.getItem(position)));
    }

    // 5 - Update header (username & picture)
    private void updateHeader(User user){
        this.profileText.setText(user.getUsername());
        Glide.with(this).load(user.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(this.profileImage);
    }

    // 6 - Update the list of items
    private void updateItemsList(List<Item> items){
        this.adapter.updateData(items);
    }
}
