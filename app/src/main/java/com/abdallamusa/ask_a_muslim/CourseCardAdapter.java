package com.abdallamusa.ask_a_muslim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CourseCardAdapter
        extends RecyclerView.Adapter<CourseCardAdapter.ViewHolder> {

    public interface OnCourseAction {
        void onCourseContinue(String courseId);
        void onCourseUnenroll(String courseId);
    }

    private final Context ctx;
    private final List<CourseSummary> data;
    private final OnCourseAction listener;

    public CourseCardAdapter(Context ctx,
                             List<CourseSummary> data,
                             OnCourseAction listener) {
        this.ctx      = ctx;
        this.data     = data;
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_listview_profile, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        CourseSummary cs = data.get(pos);
        h.title .setText(cs.title);
        h.instr .setText(cs.instructor);
        Glide.with(ctx)
                .load(cs.thumbnailUrl)
                .placeholder(R.drawable.placholder_image)
                .into(h.thumb);

        h.continueBtn.setOnClickListener(v -> {
            // forward to Activity
            listener.onCourseContinue(cs.id);
        });

        h.unenrollBtn.setOnClickListener(v -> {
            // forward to Activity
            listener.onCourseUnenroll(cs.id);
        });
    }

    @Override public int getItemCount() { return data.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView title, instr;
        AppCompatButton continueBtn, unenrollBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb         = itemView.findViewById(R.id.thumbnailUrl_ID_profile);
            title         = itemView.findViewById(R.id.courseTitle_profile);
            instr         = itemView.findViewById(R.id.courseInstructor_profile);
            continueBtn   = itemView.findViewById(R.id.courseContinueButton_profile);
            unenrollBtn   = itemView.findViewById(R.id.courseUnenrollButton_profile);
        }
    }
}
