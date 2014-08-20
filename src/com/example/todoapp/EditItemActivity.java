package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

/**
 * This activity helps in editing a passed TODO item and returns the 
 * modified value.
 * 
 * @author Damodar Periwal
 *
 */
public class EditItemActivity extends ActionBarActivity {
	private int position;
	private EditText etEditedItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		etEditedItem = (EditText) findViewById(R.id.etEditedItem);
		position = getIntent().getIntExtra(ToDoActivity.POSITION_KEY, -1);
		if (position < 0) {
			returnData(position, null, RESULT_CANCELED);
		}
		String currItem = getIntent().getStringExtra(ToDoActivity.CURR_ITEM_KEY);
		etEditedItem.setText(currItem);
		etEditedItem.setFocusable(true);		
	}
	
	public void saveEditedItem(View v) {
		returnData(position, etEditedItem.getText().toString(), RESULT_OK);
	}
	
	private void returnData(int position, String editedData, int resultCode) {	
		Intent data = new Intent();
		data.putExtra(ToDoActivity.POSITION_KEY, position);
		data.putExtra(ToDoActivity.EDITED_ITEM_KEY, editedData);
		setResult(resultCode, data);
		finish();
	}

}
