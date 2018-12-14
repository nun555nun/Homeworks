package com.example.homeworks;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.homeworks.adapter.HomeWorkListAdapter;
import com.example.homeworks.db.DatabaseHelper;
import com.example.homeworks.model.HomeworkItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.homeworks.db.DatabaseHelper.COL_DEADLINE;
import static com.example.homeworks.db.DatabaseHelper.COL_DETAILS;
import static com.example.homeworks.db.DatabaseHelper.COL_ID;
import static com.example.homeworks.db.DatabaseHelper.COL_IMAGE;
import static com.example.homeworks.db.DatabaseHelper.COL_START;
import static com.example.homeworks.db.DatabaseHelper.COL_SUBJECT;
import static com.example.homeworks.db.DatabaseHelper.COL_TITLE;
import static com.example.homeworks.db.DatabaseHelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity {


    private DatabaseHelper mHelper;
    private SQLiteDatabase mDb;
    private List<HomeworkItem> mHomeworkItemList;
    private Spinner mSortSpinner;
    private int format =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new DatabaseHelper(this);
        mDb = mHelper.getWritableDatabase();
        loadHomeworkData(format);
        setupListView();

        mSortSpinner = findViewById(R.id.sort);

        String[] sort = getResources().getStringArray(R.array.sort);
        ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,sort);

        mSortSpinner.setAdapter(adapterSort);
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                format=position;
                loadHomeworkData(format);
                setupListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button addHomeworkButton = findViewById(R.id.add_hw_button);
        addHomeworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(i);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        loadHomeworkData(format);
        setupListView();
    }

    private void loadHomeworkData(int format) {
        Cursor c = mDb.query(TABLE_NAME, null, null, null, null, null, null);
        mHomeworkItemList = new ArrayList<>();

        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(COL_ID));
            String title = c.getString(c.getColumnIndex(COL_TITLE));
            String subject = c.getString(c.getColumnIndex(COL_SUBJECT));
            String start = c.getString(c.getColumnIndex(COL_START));
            String deadline= c.getString(c.getColumnIndex(COL_DEADLINE));
            String details = c.getString(c.getColumnIndex(COL_DETAILS));
            String image = c.getString(c.getColumnIndex(COL_IMAGE));
            HomeworkItem item = new HomeworkItem(id, title,subject, start, deadline,details,image);
            mHomeworkItemList.add(item);
        }
        switch (format) {
            case 0:
                break;
            case 1:
                Collections.sort(mHomeworkItemList, new Comparator<HomeworkItem>() {
                    @Override
                    public int compare(HomeworkItem o1, HomeworkItem o2) {
                            return o1.getTitle().compareTo(o2.getTitle());
                    }
                });
                break;
            case 2:
                Collections.sort(mHomeworkItemList, new Comparator<HomeworkItem>() {
                    DateFormat f = new SimpleDateFormat("MM/dd/yyyy");
                    @Override
                    public int compare(HomeworkItem o1, HomeworkItem o2) {
                        try {
                            return f.parse(o1.getStart()).compareTo(f.parse(o2.getStart()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                break;
            case 3:
                Collections.sort(mHomeworkItemList, new Comparator<HomeworkItem>() {
                    DateFormat f = new SimpleDateFormat("MM/dd/yyyy");
                    @Override
                    public int compare(HomeworkItem o1, HomeworkItem o2) {
                        try {
                            return f.parse(o1.getDeadline()).compareTo(f.parse(o2.getDeadline()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                break;
        }
        c.close();
    }

    //adapter
    private void setupListView() {

       HomeWorkListAdapter adapter = new HomeWorkListAdapter(
                MainActivity.this,
                R.layout.item_homework,
                mHomeworkItemList
        );
        ListView l = findViewById(R.id.result_list_view);
        l.setAdapter(adapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final HomeworkItem item = mHomeworkItemList.get(position);
                Intent intent = new Intent(MainActivity.this, ItemInfoActivity.class);

                intent.putExtra("id", item._id);
                startActivity(intent);

            }
        });
        l.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String[] items = new String[]{
                        "แก้ไข", "ลบ"
                };

                new AlertDialog.Builder(MainActivity.this)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final HomeworkItem item = mHomeworkItemList.get(position);

                                switch (which) {
                                    case 0://edit
                                        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);

                                        intent.putExtra("id", item._id);
                                        intent.putExtra("title", item.title);
                                        intent.putExtra("subject", item.subject);
                                        intent.putExtra("start", item.start);
                                        intent.putExtra("deadline", item.deadline);
                                        intent.putExtra("details", item.details);
                                        intent.putExtra("image", item.image);

                                        startActivity(intent);

                                        break;
                                    case 1://Delete
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setMessage("ต้องการลบข้อมูลนี้ ใช่หรือไม่")
                                                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mDb.delete(
                                                                TABLE_NAME,
                                                                COL_ID+" = ?",
                                                                new String[]{String.valueOf(item._id)}
                                                        );
                                                        loadHomeworkData(format);
                                                        setupListView();
                                                    }
                                                })
                                                .setNegativeButton("ไม่",null)
                                                .show();

                                        break;
                                }
                            }
                        })
                        .show();

                loadHomeworkData(format);
                setupListView();

                return true;
            }
        });

    }
}
