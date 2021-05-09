package com.domos.balance.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.domos.balance.R;
import com.domos.balance.data.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolderTask> implements
        android.view.View.OnClickListener, View.OnLongClickListener {

    ArrayList<Task> tasks;

    private View.OnClickListener listener;
    private View.OnLongClickListener longClickListener;


    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolderTask onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_task,parent,false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return new ViewHolderTask(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTask holder, int position) {

        Task actualTask = tasks.get(position);

        holder.txtName.setText(actualTask.getName());
        holder.txtDuration.setText(actualTask.getDuration());
        String cantidadPomodoros = actualTask.getCount() == 1 ? " pomodoro" : " pomodoros";
        holder.txtCount.setText(""+actualTask.getCount() + cantidadPomodoros);
        holder.imgEmoji.setImageResource(actualTask.getEmoji());
        holder.taskRdButton.setEnabled(false);

        //Reviso si la tarea ya fue realizada
        if(actualTask.isStarted()){
            holder.imgEmoji.setBackgroundResource(R.drawable.emoji_rounded_background_gray);
            holder.txtName.setTextColor(Color.parseColor("#BFBFBF"));
            holder.txtDuration.setTextColor(Color.parseColor("#BFBFBF"));
            holder.txtCount.setTextColor(Color.parseColor("#BFBFBF"));
            holder.taskRdButton.setChecked(true);
        }
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

    @Override
    public boolean onLongClick(View v) {
        if (longClickListener != null){
            longClickListener.onLongClick(v);
            return true;
        }
            return false;
    }

    public void onLongClick(View.OnLongClickListener onLongClickListener) {
        this.longClickListener = onLongClickListener;
    }

    public class ViewHolderTask extends RecyclerView.ViewHolder {

        TextView txtName, txtDuration, txtCount;
        ImageView imgEmoji;
        RadioButton taskRdButton;

        public ViewHolderTask(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.taskTxtName);
            txtDuration = (TextView) itemView.findViewById(R.id.taskTxtDuration);
            txtCount = (TextView) itemView.findViewById(R.id.taskTxtCount);
            imgEmoji = (ImageView) itemView.findViewById(R.id.taskImgEmoji);
            taskRdButton = (RadioButton) itemView.findViewById(R.id.taskRdButton);


        }
    }

    public void filterList(ArrayList<Task> filteredList){
        tasks = filteredList;
        notifyDataSetChanged();
    }
}
