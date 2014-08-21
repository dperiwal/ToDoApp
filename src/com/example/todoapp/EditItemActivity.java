package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

/**
 * This activity helps in editing a passed TODO item and returns the 
 * modified values.
 * 
 * @author Damodar Periwal
 *
 */
public class EditItemActivity extends ActionBarActivity {
	private int position;
	private EditText etEditedName;
	private EditText etEditedPriority;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		etEditedName = (EditText) findViewById(R.id.etEditedName);
		etEditedPriority = (EditText) findViewById(R.id.etEditedPriority);
		position = getIntent().getIntExtra(ToDoActivity.POSITION_KEY, -1);
		if (position < 0) {
			returnData(position, null, 0, RESULT_CANCELED);
		}
		String currName = getIntent().getStringExtra(ToDoActivity.ITEM_NAME_KEY);
		int currPriority = (int) getIntent().getIntExtra(ToDoActivity.ITEM_PRIORITY_KEY, 0);
		etEditedName.setText(currName);
		etEditedPriority.setText(new Integer(currPriority).toString());
		etEditedName.setFocusable(true);		
	}
	
	public void saveEditedItem(View v) {
		returnData(position, etEditedName.getText().toString(), 
				Integer.valueOf(etEditedPriority.getText().toString()), 
				RESULT_OK);
	}
	
	private void returnData(int position, String editedName, int editedPriority, int resultCode) {	
		Intent data = new Intent();
		data.putExtra(ToDoActivity.POSITION_KEY, position);
		data.putExtra(ToDoActivity.EDITED_ITEM_NAME_KEY, editedName);
		data.putExtra(ToDoActivity.EDITED_ITEM_PRIORITY_KEY, editedPriority);
		setResult(resultCode, data);
		finish();
	}

}
