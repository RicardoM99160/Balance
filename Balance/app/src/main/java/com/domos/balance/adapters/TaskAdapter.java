package com.domos.balance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.domos.balance.R;
import com.domos.balance.data.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolderTask> implements
        android.view.View.OnClickListener {

    ArrayList<Task> tasks;

    private View.OnClickListener listener;


    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolderTask onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_task,parent,false);
        view.setOnClickListener(this);

        return new ViewHolderTask(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTask holder, int position) {

        holder.txtName.setText(tasks.get(position).getName());
        holder.txtDuration.setText(tasks.get(position).getDuration());

        String cantidadPomodoros = tasks.get(position).getCount() == 1 ? " pomodoro" : " pomodoros";
        holder.txtCount.setText(""+tasks.get(position).getCount() + cantidadPomodoros);

        holder.imgEmoji.setImageResource(tasks.get(position).getEmoji());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onClick(v);
    }

    public class ViewHolderTask extends RecyclerView.ViewHolder {

        TextView txtName, txtDuration, txtCount;
        ImageView imgEmoji;

        public ViewHolderTask(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.taskTxtName);
            txtDuration = (TextView) itemView.findViewById(R.id.taskTxtDuration);
            txtCount = (TextView) itemView.findViewById(R.id.taskTxtCount);
            imgEmoji = (ImageView) itemView.findViewById(R.id.taskImgEmoji);



        }
    }
}
