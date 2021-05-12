package com.domos.balance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.domos.balance.R;

import java.util.ArrayList;

public class EmojiAdapter extends BaseAdapter {

    private Context context;
    private  ArrayList<Integer> arrayList;

    public EmojiAdapter(Context context, ArrayList<Integer> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.emoji_item,parent, false);
        }

        ImageView imgEmoji = (ImageView) convertView.findViewById(R.id.imgEmoji);
        imgEmoji.setImageResource(arrayList.get(position));
        return convertView;
    }
}