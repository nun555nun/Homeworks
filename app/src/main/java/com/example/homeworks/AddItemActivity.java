package com.example.homeworks;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homeworks.db.DatabaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import static android.graphics.Color.BLACK;
import static com.example.homeworks.db.DatabaseHelper.COL_DEADLINE;
import static com.example.homeworks.db.DatabaseHelper.COL_DETAILS;
import static com.example.homeworks.db.DatabaseHelper.COL_IMAGE;
import static com.example.homeworks.db.DatabaseHelper.COL_START;
import static com.example.homeworks.db.DatabaseHelper.COL_SUBJECT;
import static com.example.homeworks.db.DatabaseHelper.COL_TITLE;
import static com.example.homeworks.db.DatabaseHelper.TABLE_NAME;

public class AddItemActivity extends AppCompatActivity {
    private Calendar mDate;
    private int day, month, year;
    private TextView startText;
    private TextView deadlineText;
    private Spinner mSubjectSpinner;
    private String mfilename;
    private String msubject;
    private DatabaseHelper mHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mHelper = new DatabaseHelper(AddItemActivity.this);
        mDb = mHelper.getWritableDatabase();

        startText = findViewById(R.id.start_text);
        deadlineText = findViewById(R.id.deadline_text);
        mDate = Calendar.getInstance();

        day = mDate.get(Calendar.DAY_OF_MONTH);
        month = mDate.get(Calendar.MONTH);
        year = mDate.get(Calendar.YEAR);

        month += 1;

        startText.setText(day + "/" + month + "/" + year);
        deadlineText.setText(day + "/" + month + "/" + year);
        startText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        startText.setText(dayOfMonth + "/" + month + "/" + year);
                        startText.setTextColor(BLACK);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        deadlineText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        deadlineText.setText(dayOfMonth + "/" + month + "/" + year);
                        startText.setTextColor(BLACK);
                        deadlineText.setTextColor(BLACK);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        mSubjectSpinner = findViewById(R.id.subject);
        final String[] subject = getResources().getStringArray(R.array.subject);
        final String[] image = getResources().getStringArray(R.array.image);
        ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, subject);
        mSubjectSpinner.setAdapter(adapterSubject);

        mSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ImageView imageView = findViewById(R.id.language_image);
                msubject = subject[position];
                mfilename = image[position];

                AssetManager am = AddItemActivity.this.getAssets();
                try {
                    InputStream is = am.open(mfilename);
                    Drawable drawable = Drawable.createFromStream(is, "");
                    imageView.setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button addButton = findViewById(R.id.save_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doInsertHomeworkItem();
            }
        });
    }

    private void doInsertHomeworkItem() {
        EditText titleEdit = findViewById(R.id.title_edit);
        EditText detailsEdit = findViewById(R.id.details_edit);

        String title = titleEdit.getText().toString();
        String start = startText.getText().toString();
        String deadline = deadlineText.getText().toString();
        String details = detailsEdit.getText().toString();
        if (title.length() == 0) {
            Toast.makeText(AddItemActivity.this, "กรุณาใส่หัวข้อ", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues cv = new ContentValues();
            cv.put(COL_TITLE, title);
            cv.put(COL_SUBJECT, msubject);
            cv.put(COL_START, start);
            cv.put(COL_DEADLINE, deadline);
            cv.put(COL_DETAILS, details);
            cv.put(COL_IMAGE, mfilename);
            mDb.insert(TABLE_NAME, null, cv);
            Toast.makeText(AddItemActivity.this, "เพิ่มเรียบร้อย", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
