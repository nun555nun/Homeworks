package com.example.homeworks;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class ItemInfoActivity extends AppCompatActivity {
    private long mId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        Intent intent = getIntent();
        mId = intent.getLongExtra("id", 0);
        final String title = intent.getStringExtra("title");
        final String subject = intent.getStringExtra("subject");
        final String start = intent.getStringExtra("start");
        final String deadline = intent.getStringExtra("deadline");
        final String details = intent.getStringExtra("details");
        final String image = intent.getStringExtra("image");

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

        Button editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemInfoActivity.this, EditItemActivity.class);

                intent.putExtra("id", mId);
                intent.putExtra("title",title);
                intent.putExtra("subject",subject);
                intent.putExtra("start",start);
                intent.putExtra("deadline",deadline);
                intent.putExtra("details",details);
                intent.putExtra("image",image);

                startActivity(intent);
            }
        });
    }
}
