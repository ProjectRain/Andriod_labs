package com.example.andriodlabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


    public class ChatListAdapter extends BaseAdapter {
        private Context c;
        private ArrayList<Message> array;

        public ChatListAdapter(Context c, ArrayList<Message> messages){
            super();
            this.c = c;
            this.array = messages;
        }

        @Override
        public int getCount() {
            return array.size();
        } //This function tells how many objects to show
        @Override
        public Message getItem(int position) {
            return array.get(position);
        }  //This returns the string at position p
        @Override
        public long getItemId(int p) {
            return p;
        } //This returns the database id of the item at position p


        public ArrayList<Message> getArray() {
            return array;
        }


        @Override
        public View getView(int p, View view, ViewGroup parent) {
            TextView rowText;
            Message message = array.get(p);
            LayoutInflater inflater = LayoutInflater.from(c);
            View rowView = null;
            if(message.isSend()){
                rowView= inflater.inflate(R.layout.send_layout , null);
            }else{
                rowView= inflater.inflate(R.layout.receive_layout, null);
            }
            rowText = rowView.findViewById(R.id.rMessage);
            rowText.setText(message.getMessage());
            return rowView;


        }
    }

