package com.example.homeworks.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homeworks.R;
import com.example.homeworks.model.HomeworkItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class HomeWorkListAdapter extends ArrayAdapter<HomeworkItem> {

    private Context mContext;
    private int mResource;
    private List<HomeworkItem> mHomeworkItemList;

    public HomeWorkListAdapter(@NonNull Context context, int resource, @NonNull List<HomeworkItem> HomeworkItemList) {
        super(context, resource, HomeworkItemList);
        this.mContext = context;
        this.mResource = resource;
        this.mHomeworkItemList = HomeworkItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,@NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResource, parent, false);

        TextView titleTextView=view.findViewById(R.id.title_text_view);
        TextView startTextView = view.findViewById(R.id.start_text_view);
        TextView deadlineTextView = view.findViewById(R.id.deadline_text_view);
        ImageView imageView = view.findViewById(R.id.imageView);

        HomeworkItem homeworkItem = mHomeworkItemList.get(position);
        String title = homeworkItem.title;
        String start = homeworkItem.start;
        String deadline = homeworkItem.deadline;
        String filename = homeworkItem.image;

        titleTextView.setText(title);
        startTextView.setText(start);
        deadlineTextView.setText(deadline);
        AssetManager am = mContext.getAssets();
        InputStream is = null;
        try {
            is = am.open(filename);
            Drawable drawable= Drawable.createFromStream(is,"");
            imageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }
}
