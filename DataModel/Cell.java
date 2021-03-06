package DataModel;

import java.io.Serializable;

/**
 * DataModel.Cell is an abstract class providing functions and a key variable for the DataModel.SimpleCell and DataModel.CompositeCell subclasses.
 * 
 * @author Darren
 * @version Milestone 4
 *
 */
public abstract class Cell implements Serializable {
	private String key;
	
	/**
	 * Constructer for a DataModel.Cell. Sets the key of the cell to the given value.
	 * 
	 * @param key	The DataModel.Cell's key value.
	 */
	public Cell (String key) {
		this.key = key;
	}
	
	/**
	 * Returns the key value for this cell.
	 * 
	 * @return The DataModel.Cell's key value.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Sets the key of hte cell to a new value.
	 * 
	 * @param key	The new key for the cell
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Generates a string representation of the cell.
	 */
	public abstract String toString();
}