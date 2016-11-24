package com.example.to_do_list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.List_View);
        items = new ArrayList<String>();
        loadData();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        AlertDialog.Builder welcome = new AlertDialog.Builder(MainActivity.this);
        welcome.setTitle("Welcome!");
        welcome.setMessage("Use the text box on bottom of screen to type and add items." +
                " Click the item to edit it. Long click the item to remove it. Enjoy!");
        welcome.setNegativeButton("OK", null);
        welcome.show();
        ListViewLongListener();
        ListViewListener();

    }

    public void onAdd(View view) {
        EditText itemText = (EditText) findViewById(R.id.Item_Text);
        String Text = itemText.getText().toString();
        itemsAdapter.add(Text);
        itemText.setText("");
        saveData(items);
    }

    private void ListViewLongListener() {
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, final int pos, long id) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Remove Item");
                        alert.setMessage("Are you sure you want to delete the selected item?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                items.remove(pos);
                                itemsAdapter.notifyDataSetChanged();
                                saveData(items);
                            }
                        });
                        alert.setNegativeButton("No", null);
                        alert.show();

                        return true;
                    }
                });
    }

    private void ListViewListener() {
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, final int pos, long id) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Edit Item");
                        alert.setMessage("Enter new text.");
                        final EditText editText = new EditText(MainActivity.this);
                        alert.setView(editText);
                        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                items.set(pos, editText.getText().toString());
                                itemsAdapter.notifyDataSetChanged();
                                saveData(items);
                            }
                        });
                        alert.setNegativeButton("Cancel", null);
                        alert.show();
                    }
                });
    }


    public void saveData(ArrayList<String> array) {
        SharedPreferences data = getSharedPreferences("ToDoData", Context.MODE_PRIVATE);
        Set<String> set = new HashSet<String>();
        set.addAll(array);
        SharedPreferences.Editor editor = data.edit();
        editor.putStringSet("ToDoData", set);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences data = getSharedPreferences("ToDoData", Context.MODE_PRIVATE);
        Set<String> itemsSet= new HashSet<String>();
        itemsSet = data.getStringSet("ToDoData",itemsSet);
        items.addAll(itemsSet);
    }


}

