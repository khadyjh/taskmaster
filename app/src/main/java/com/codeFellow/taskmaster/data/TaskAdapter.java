package com.codeFellow.taskmaster.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeFellow.taskmaster.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.CustomViewHolder> {

    List<com.amplifyframework.datastore.generated.model.Task> tasks;

    CustomClickListener listener;

    int taskNumber;


    public TaskAdapter(List<com.amplifyframework.datastore.generated.model.Task> tasks ,CustomClickListener listener,int taskNumber) {
        this.tasks = tasks;
        this.listener=listener;
        this.taskNumber=taskNumber;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listView=layoutInflater.inflate(R.layout.task_recycler_view,parent,false);
        return new CustomViewHolder(listView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.title.setText(tasks.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        if(taskNumber>tasks.size()){
            return tasks.size();
        }
        return taskNumber;

    }

    final static class CustomViewHolder extends RecyclerView.ViewHolder  {

        Button title;

        CustomClickListener listener;

        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);
            title=itemView.findViewById(R.id.text_view_recycler);

            this.listener=listener;

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onOneTaskClicked(getAdapterPosition());
                }
            });
//            itemView.setOnClickListener(view -> listener.onOneTaskClicked(getAdapterPosition()));
        }

    }

    public interface CustomClickListener {
        void onOneTaskClicked(int position);
    }
}
