package com.example.homeworks;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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
import static com.example.homeworks.db.DatabaseHelper.COL_ID;
import static com.example.homeworks.db.DatabaseHelper.COL_IMAGE;
import static com.example.homeworks.db.DatabaseHelper.COL_START;
import static com.example.homeworks.db.DatabaseHelper.COL_SUBJECT;
import static com.example.homeworks.db.DatabaseHelper.COL_TITLE;
import static com.example.homeworks.db.DatabaseHelper.TABLE_NAME;

public class EditItemActivity extends AppCompatActivity {
    private Calendar mDate;
    private int day, month, year;

    private TextView startText;
    private TextView deadlineText;
    private EditText titleEdit;
    private EditText detailsEdit;

    private Spinner mSubjectSpinner;

    private String mfilename;
    private String msubject;
    private long mId;

    private DatabaseHelper mHelper;
    private SQLiteDatabase mDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mHelper = new DatabaseHelper(EditItemActivity.this);
        mDb = mHelper.getWritableDatabase();

        Intent intent = getIntent();
        mId = intent.getLongExtra("id", 0);
        String title = intent.getStringExtra("title");
        String subject = intent.getStringExtra("subject");
        String start = intent.getStringExtra("start");
        String deadline = intent.getStringExtra("deadline");
        String details = intent.getStringExtra("details");
        String image = intent.getStringExtra("image");

        titleEdit = findViewById(R.id.title_edit);
        startText = findViewById(R.id.start_view);
        deadlineText = findViewById(R.id.deadline_text);
        detailsEdit = findViewById(R.id.details_edit);

        titleEdit.setText(title);
        startText.setText(start);
        deadlineText.setText(deadline);
        detailsEdit.setText(details);

        mDate = Calendar.getInstance();
        day = mDate.get(Calendar.DAY_OF_MONTH);
        month = mDate.get(Calendar.MONTH);
        year = mDate.get(Calendar.YEAR);
        month += 1;

        startText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditItemActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditItemActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        final String[] subjectA = getResources().getStringArray(R.array.subject);
        final String[] imageA = getResources().getStringArray(R.array.image);

        for(int i=0 ;i<subjectA.length;i++){
            if(subjectA[i].equals(subject)){
                String temp = subjectA[0];
                subjectA[0]=subjectA[i];
                subjectA[i]=temp;

                temp =imageA[0];
                imageA[0]= imageA[i];
                imageA[i]=temp;
            }
        }
        ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, subjectA);
        mSubjectSpinner.setAdapter(adapterSubject);
        mSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ImageView imageView = findViewById(R.id.subject_image);
                msubject = subjectA[position];
                mfilename = imageA[position];

                AssetManager am = EditItemActivity.this.getAssets();
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

        Button saveButton = findViewById(R.id.edit_button);
        saveButton.setText("บันทึก");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdateHomeworkItem();
                finish();
            }
        });
    }

    private void doUpdateHomeworkItem() {

        String title = titleEdit.getText().toString();
        String start = startText.getText().toString();
        String deadline = deadlineText.getText().toString();
        String details = detailsEdit.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_SUBJECT, msubject);
        cv.put(COL_START, start);
        cv.put(COL_DEADLINE, deadline);
        cv.put(COL_DETAILS, details);
        cv.put(COL_IMAGE, mfilename);

        mDb.update(TABLE_NAME,cv,COL_ID+" = ?",new String[]{String.valueOf(mId)});

        Toast.makeText(EditItemActivity.this, "แก้ไขเรียบร้อย", Toast.LENGTH_SHORT).show();
    }
}
