// ToDoItemRepository.java
package com.libertymutual.goforcode.todolist.services;

import java.io.*;
import java.util.*;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.libertymutual.goforcode.todolist.models.ToDoItem;

@Service
public class ToDoItemRepository {

    private int nextId = 1;
    private Map<Integer, ToDoItem> items = new HashMap<Integer, ToDoItem>();
  
    /**
     * Get all the items from the file. 
     * @return A list of the items. If no items exist, returns an empty list.
     */
    public List<ToDoItem> getAll() {
    	List<ToDoItem> allItemsAsList = new ArrayList<ToDoItem>();
    	try (Reader reader = new FileReader("todolist.csv"); CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);) {
    		List<CSVRecord> records = parser.getRecords();
    		int largestID = 0;
    		for (CSVRecord c : records) {
    			String id = c.get(0);
    			String text = c.get(1);
    			String bool = c.get(2);
    			ToDoItem t = new ToDoItem(); 
            	t.setId(Integer.valueOf(id));
            	t.setText(text);
            	t.setComplete(Boolean.valueOf(bool));
            	items.put(t.getId(), t);
            	if (t.getId() > largestID) {
            		largestID = t.getId();
            	}
            	nextId = largestID + 1;
    		}
    		Collection<ToDoItem> allItems = items.values();
    		allItemsAsList.addAll(allItems);	
    	} catch (FileNotFoundException e) {	
    		System.out.println("File wasn't found when you ran getAll()");
    	} catch (IOException e) {
    		System.out.println("IO Exception when you ran getAll()");
    	}
       return allItemsAsList;
    }

    /**
     * Assigns a new id to the ToDoItem and saves it to the file.
     * @param item The to-do item to save to the file.
     * @throws IOException 
     */
    public void create(ToDoItem item) {
		try (FileWriter writer = new FileWriter("todolist.csv"); 
				CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);) {
			
			item.setId(nextId);
			items.put(nextId, item);
			nextId += 1;
			save();
		} catch (IOException e) {
			System.out.println("Creating a record didn't work for some reason");
			e.printStackTrace();
		}
    }
    
    // Helper method
    private void save() {
    	try (FileWriter writer = new FileWriter("todolist.csv"); 
				CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);) {
    		List<ToDoItem> list = getAll();
        	for (ToDoItem t : list) {
        		Object[] arrayInCreate = new String[] {String.valueOf(t.getId()),t.getText(),String.valueOf(t.isComplete())};
        		printer.printRecord(arrayInCreate);
        	}
    	} catch (IOException e) {
			System.out.println("Didn't work");
		}
    }
    
    /**
     * Gets a specific ToDoItem by its id.
     * @param id The id of the ToDoItem.
     * @return The ToDoItem with the specified id or null if none is found.
     */
    public ToDoItem getById(int id) {
        return items.get(id);
    }

    /**
     * Updates the given to-do item in the file.
     * @param item The item to update.
     */
    public void update(ToDoItem item) {
       save();
    }
    
  
    

}