package com.example.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.example.todoapp.model.ToDoItem;

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
 * This main activity helps in managing a list of TODO items. A TODO item has a name
 * and a priority. A user can add, delete, and persistently save a list of TODO 
 * items. By using {@link EditItemActivity} activity, the user can also edit 
 * an existing TODO item.
 * <p>
 * @author Damodar Periwal
 *
 */
public class ToDoActivity extends ActionBarActivity {
	public final static String POSITION_KEY = "POSITION";
	public final static String ITEM_NAME_KEY = "ITEM_NAME";
	public final static String ITEM_PRIORITY_KEY = "ITEM_PRIORITY";
	public final static String EDITED_ITEM_NAME_KEY = "EDITED_ITEM_NAME";
	public final static String EDITED_ITEM_PRIORITY_KEY = "EDITED_ITEM_PRIORITY";
	
	private ArrayList<ToDoItem> toDoItems;
	private ArrayAdapter<ToDoItem> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	private EditText etNewPriority;
	
	private final int REQUEST_CODE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		// Construct the data source
		readItems();
		
		createTodoItemArrayAdapter();		
		etNewItem = (EditText) findViewById(R.id.etNewItem);
		etNewPriority = (EditText) findViewById(R.id.etNewPriority);
		setupListViewListener();
	}
	
	private void createTodoItemArrayAdapter() {				
		// Create the adapter to convert the array to views
		todoAdapter = new ToDoItemsAdapter(this, toDoItems);
		// Attach the adapter to a ListView
		lvItems = (ListView) findViewById(R.id.lvItems);
		lvItems.setAdapter(todoAdapter);
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
				toDoItems.remove(position);
				// There is no need to unnecessarily sort the list because removing 
				// an item from a sorted list does not change the ordering of the remaining items.
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
				ToDoItem toDoItem = toDoItems.get(position);
				Intent i = new Intent(ToDoActivity.this, EditItemActivity.class);
				i.putExtra(POSITION_KEY, position);
				i.putExtra(ITEM_NAME_KEY, toDoItem.getName());
				System.out.println("passed priority=" + toDoItem.getPriority());
				i.putExtra(ITEM_PRIORITY_KEY, toDoItem.getPriority());
				startActivityForResult(i, REQUEST_CODE);			
			}		
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			// Update the model with the edited item value
			int position = data.getExtras().getInt(POSITION_KEY); 
			String editedItemName = data.getExtras().getString(EDITED_ITEM_NAME_KEY);
			int editedItemPriority = data.getExtras().getInt(EDITED_ITEM_PRIORITY_KEY);
			ToDoItem toDoItem = new ToDoItem(editedItemName, editedItemPriority);
			if (null != editedItemName) {
				toDoItems.set(position, toDoItem);
				sortToDoItems(true);
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
		String newItemName = etNewItem.getText().toString();
		if (newItemName.isEmpty()) {
			return;
		}
		
		int newItemPriority = Integer.valueOf(etNewPriority.getText().toString());
		todoAdapter.add(new ToDoItem(newItemName, newItemPriority));
		sortToDoItems(true);
		// todoAdapter.notifyDataSetChanged(); // needed?
		
		etNewItem.setText("");
		etNewPriority.setText("");
		
		writeItems();
	}
	
	/**
	 * Sorts the the current list of ToDo items based on the priority value
	 * 
	 * @param descending If true, sort in descending order
	 */
	private void sortToDoItems(final boolean descending) {
		Collections.sort(toDoItems, new Comparator<ToDoItem>() {
	        @Override
	        public int compare(ToDoItem  item1, ToDoItem  item2) {
	        	int multiplier = 1;
	        	if (descending) {
	        		multiplier = -1;
	        	}
	            return multiplier * (item1.getPriority() - item2.getPriority());
	        }
	    });
	}
	
	/**
	 * Reads a list of persisted TODO items from a local file (todo.txt).
	 */
	private void readItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			toDoItems = createToDoItemArrayList(FileUtils.readLines(todoFile));			
		} catch (IOException ex) {
			toDoItems = new ArrayList<ToDoItem>();
		};	
	}
	
	/**
	 * Converts an encoded list of strings to a list of TODO items - one item per string.
	 * @param toDoItemsStringList
	 * @return An ArrayList of TODO items.
	 */
	private ArrayList<ToDoItem> createToDoItemArrayList(List<String> toDoItemsStringList) {
		ArrayList<ToDoItem> todoItems = new ArrayList<ToDoItem>();
		for (String todoItemStr : toDoItemsStringList) {
			todoItems.add(ToDoItem.fromString(todoItemStr));		
		}		
		return todoItems; 
	}
	
	/**
	 * Saves (persists) a list of TODO items into a local file (todo.txt).
	 */
	private void writeItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			FileUtils.writeLines(todoFile, toDoItems);
			
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
