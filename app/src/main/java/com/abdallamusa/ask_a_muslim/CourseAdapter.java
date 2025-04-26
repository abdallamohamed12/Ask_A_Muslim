package com.abdallamusa.ask_a_muslim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

public class CourseAdapter extends ArrayAdapter<Course> {
    private final int resourceLayout;
    private final Context mContext;

    public CourseAdapter(@NonNull Context context, int resource, @NonNull List<Course> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @NonNull @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Inflate row if needed
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, parent, false);
        }

        // 2. Get our data item
        Course c = getItem(position);
        if (c != null) {
            // 3. Lookup subviews
            TextView titleTv = v.findViewById(R.id.courseTitle);
            TextView instrTv = v.findViewById(R.id.courseInstructor);
            Button actionBtn = v.findViewById(R.id.courseActionButton);

            // 4. Populate
            titleTv.setText(c.title);
            instrTv.setText(c.instructor);

            // 5. Handle clicks
            actionBtn.setOnClickListener(btn -> {
                // e.g. start the course, open detail, show a toast...
                Toast.makeText(mContext,
                        "Starting “" + c.title + "”",
                        Toast.LENGTH_SHORT).show();
            });
        }

        return v;
    }
}
