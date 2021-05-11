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

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolderTask> implements
        View.OnClickListener, View.OnLongClickListener {

    ArrayList<String> notes;

    private View.OnClickListener listener;
    private View.OnLongClickListener longClickListener;


    public NoteAdapter(ArrayList<String> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolderTask onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_note,parent,false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return new ViewHolderTask(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTask holder, int position) {

        String[] actualNote = notes.get(position).split("/");
        String noteName = actualNote[0];
        String noteHour = "Registrada a las " + actualNote[1];

        holder.txtNoteName.setText(noteName);
        holder.txtNoteHour.setText(noteHour);

    }

    @Override
    public int getItemCount() {
        return notes.size();
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

        TextView txtNoteName, txtNoteHour;
        RadioButton noteRdButton;

        public ViewHolderTask(@NonNull View itemView) {
            super(itemView);
            txtNoteName = (TextView) itemView.findViewById(R.id.noteName);
            txtNoteHour = (TextView) itemView.findViewById(R.id.noteHour);
            noteRdButton = (RadioButton) itemView.findViewById(R.id.noteRdButton);
        }
    }
}
