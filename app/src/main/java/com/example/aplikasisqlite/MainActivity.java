package com.example.aplikasisqlite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.aplikasisqlite.adapter.Adapter;
import com.example.aplikasisqlite.helper.DbHelper;
import com.example.aplikasisqlite.model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    AlertDialog.Builder dialog;
    List<Data> itemList = new ArrayList<>();
    Adapter adapter;
    DbHelper SQLite;

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_ADDRESS = "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SQLite = new DbHelper(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);
        listView = findViewById(R.id.list_view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEdit.class);
                startActivity(intent);
            }
        });

        DbHelper dbHelper = new DbHelper(this);

        int idToUpdate = 1;
        String newName = "Updated Name";
        String newAddress = "Updated Address";
        dbHelper.update(idToUpdate, newName, newAddress);

        int idToDelete = 1;
        dbHelper.delete(idToDelete);

        adapter = new Adapter(this, itemList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String idx = itemList.get(position).getId();
                final String name = itemList.get(position).getName();
                final String address = itemList.get(position).getAddress();

                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, (dialogInterface, which) -> {
                    switch (which) {
                        case 0:
                            Intent intent = new Intent(MainActivity.this, AddEdit.class);
                            intent.putExtra(TAG_ID, idx);
                            intent.putExtra(TAG_NAME, name);
                            intent.putExtra(TAG_ADDRESS, address);
                            startActivity(intent);
                            break;
                        case 1:
                            SQLite.delete(Integer.parseInt(idx));
                            itemList.clear();
                            getAllData();
                            break;
                    }
                }).show();
                return true;
            }
        });
        getAllData();
    }

    private void getAllData() {
        itemList.clear(); // Ensure the list is clear before adding new items
        ArrayList<HashMap<String, String>> row = SQLite.getAllData();
        for (HashMap<String, String> map : row) {
            String id = map.get(TAG_ID);
            String name = map.get(TAG_NAME);
            String address = map.get(TAG_ADDRESS);

            Data data = new Data();
            data.setId(id);
            data.setName(name);
            data.setAddress(address);

            itemList.add(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllData();
    }
}
