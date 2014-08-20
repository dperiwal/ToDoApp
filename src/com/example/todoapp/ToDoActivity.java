package com.example.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

/**
 * This main activity helps in managing a list of TODO items. A user can add, 
 * delete, and persistently save a list of TODO items. By using {@link EditItemActivity}
 * activity, the user can also edit an existing TODO item.
 * <p>
 * @author Damodar Periwal
 *
 */
public class ToDoActivity extends ActionBarActivity {
	public final static String POSITION_KEY = "POSITION";
	public final static String CURR_ITEM_KEY = "CURR_ITEM";
	public final static String EDITED_ITEM_KEY = "EDITED_ITEM";
	
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	
	private final int REQUEST_CODE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		readItems();
		
		lvItems = (ListView) findViewById(R.id.lvItems);		
		todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
		lvItems.setAdapter(todoAdapter);
		
		etNewItem = (EditText) findViewById(R.id.etNewItem);
		setupListViewListener();
	}
	
	/**
	 * Sets up click listeners to delete or edit a TODO item in a list.
	 */
	private void setupListViewListener() {
		// Set up a long click listener for deleting an item.
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View item,
					int position, long id) {
				todoItems.remove(position);
				todoAdapter.notifyDataSetChanged();
				writeItems();
				return true;
			}
		});
		
		// Set up a click listener for editing an item in a separate activity. 
		lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				launchEditItemActivity(position);			
			}
			
			private void launchEditItemActivity(int position) {
				if (position < 0) { // possible?
					return;
				}
				// Set up an intent for EditItemActivity with the parameter values
				// of the position and the value of the item at the selected position
				Intent i = new Intent(ToDoActivity.this, EditItemActivity.class);
				i.putExtra(POSITION_KEY, position);
				i.putExtra(CURR_ITEM_KEY, todoItems.get(position));
				startActivityForResult(i, REQUEST_CODE);			
			}		
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			// Update the model with the edited item value
			int position = data.getExtras().getInt(POSITION_KEY); 
			String editedItem = data.getExtras().getString(EDITED_ITEM_KEY);
			if (null != editedItem) {
				todoItems.set(position, editedItem);
				todoAdapter.notifyDataSetChanged();
				writeItems(); // Persist the new value
			}
		}
	}
		
	/**
	 * Adds to the list of TODO items a new item specified by the user. This is 
	 * the OnClick listener method for the Add button.
	 * 
	 * @param v
	 */
	public void addTodoItem(View v) {
		todoAdapter.add(etNewItem.getText().toString());
		etNewItem.setText("");
		writeItems();
	}
	
	/**
	 * Reads a list of persisted TODO items from a local file (todo.txt).
	 */
	private void readItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
			
		} catch (IOException ex) {
			todoItems = new ArrayList<String>();
		};	
	}
	
	/**
	 * Saves (persists) a list of TODO items into a local file (todo.txt).
	 */
	private void writeItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			FileUtils.writeLines(todoFile, todoItems);
			
		} catch (IOException ex) {
			ex.printStackTrace();
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
