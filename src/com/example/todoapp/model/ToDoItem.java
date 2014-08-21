package com.example.todoapp.model;

/**
 * This class represents a TODO item which consists of a task name and a priority.
 * 
 * @author Damodar Periwal
 *
 */
public class ToDoItem {	
	private static String SEMICOLON = ";";
	
	private String name;
	private int priority;
	
	public ToDoItem() {
		super();
	}
	
	public ToDoItem(String name, int priority) {
		super();
		this.name = name;
		this.priority = priority;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	/* 
	 * Creates a semicolon separated string from the field values
	 * of the current object.
	 */
	public String toString() {
		return name + SEMICOLON + priority;		
	}
	
	/* 
	 * Builds an object from a semicolon separated list of values 
	 * for the object fields.
	 */
	public static ToDoItem fromString(String todoItemStr) {
		//System.out.println("todoItemStr is " + todoItemStr);
		String[] parts = todoItemStr.split(SEMICOLON);		
		String name = parts[0]; 
		int priority = Integer.valueOf(parts[1]);
		return new ToDoItem(name, priority);		
	}

}


