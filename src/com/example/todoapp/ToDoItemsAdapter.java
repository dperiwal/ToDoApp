package com.example.todoapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.todoapp.model.ToDoItem;

public class ToDoItemsAdapter extends ArrayAdapter<ToDoItem> {
	
    public ToDoItemsAdapter(Context context, ArrayList<ToDoItem> toDoItems) {
        super(context, R.layout.todo_item, toDoItems);
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
    	 ToDoItem toDoItem = getItem(position);    
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        // Populate the data into the template view using the data object
        tvName.setText(toDoItem.getName());
        tvPriority.setText( Integer.valueOf(toDoItem.getPriority()).toString());
        // Return the completed view to render on screen
        return convertView;
    }

}
