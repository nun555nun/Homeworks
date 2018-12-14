package com.example.homeworks;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homeworks.db.DatabaseHelper;
import com.example.homeworks.model.HomeworkItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.homeworks.db.DatabaseHelper.COL_DEADLINE;
import static com.example.homeworks.db.DatabaseHelper.COL_DETAILS;
import static com.example.homeworks.db.DatabaseHelper.COL_ID;
import static com.example.homeworks.db.DatabaseHelper.COL_IMAGE;
import static com.example.homeworks.db.DatabaseHelper.COL_START;
import static com.example.homeworks.db.DatabaseHelper.COL_SUBJECT;
import static com.example.homeworks.db.DatabaseHelper.COL_TITLE;
import static com.example.homeworks.db.DatabaseHelper.TABLE_NAME;

public class ItemInfoActivity extends AppCompatActivity {
    private long mId;

    private String title;
    private String subject;
    private String start;
    private String deadline;
    private String details;
    private String image;
    private DatabaseHelper mHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        Intent intent = getIntent();
        mId = intent.getLongExtra("id", 0);

        loadItemData();

        Button editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemInfoActivity.this, EditItemActivity.class);

                intent.putExtra("id", mId);
                intent.putExtra("title", title);
                intent.putExtra("subject", subject);
                intent.putExtra("start", start);
                intent.putExtra("deadline", deadline);
                intent.putExtra("details", details);
                intent.putExtra("image", image);
                startActivity(intent);

            }
        });
    }

    private void loadItemData() {

        mHelper = new DatabaseHelper(ItemInfoActivity.this);
        mDb = mHelper.getWritableDatabase();

        Cursor c = mDb.query(TABLE_NAME, null, null, null, null, null, null);
        while (c.moveToNext()) {
            if (c.getLong(c.getColumnIndex(COL_ID)) == mId) {
                title = c.getString(c.getColumnIndex(COL_TITLE));
                subject = c.getString(c.getColumnIndex(COL_SUBJECT));
                start = c.getString(c.getColumnIndex(COL_START));
                deadline = c.getString(c.getColumnIndex(COL_DEADLINE));
                details = c.getString(c.getColumnIndex(COL_DETAILS));
                image = c.getString(c.getColumnIndex(COL_IMAGE));
            }
        }
        TextView titleView = findViewById(R.id.title_view);
        TextView subjectView = findViewById(R.id.subject_view);
        TextView startView = findViewById(R.id.start_view);
        TextView deadlineView = findViewById(R.id.deadline_view);
        TextView detailsView = findViewById(R.id.detail_view);
        ImageView imageView = findViewById(R.id.subject_image);

        titleView.setText(title);
        subjectView.setText(subject);
        startView.setText(start);
        deadlineView.setText(deadline);
        detailsView.setText(details);

        AssetManager am = ItemInfoActivity.this.getAssets();
        try {
            InputStream is = am.open(image);
            Drawable drawable = Drawable.createFromStream(is, "");
            imageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
        loadItemData();
    }
}
