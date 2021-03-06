package DataModel;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * DataModel.CompositeCell provides functionality for a grouped set of attributes within a DataModel.Point.
 * 
 * @author Darren
 * @version Milestone 4
 *
 */
public class CompositeCell extends Cell implements Serializable {

	private ArrayList<Cell> value;
	
	/**
	 * Constructor for DataModel.CompositeCell. Sets the cell's key through the super class' constructor and initializes
	 * the subcell list.
	 * 
	 * @param key	The cell's key
	 */
	public CompositeCell(String key) {
		super(key);
		value = new ArrayList<Cell>();
	}
	
	/**
	 * getSubCells returns the list of Cells that are contained within the DataModel.CompositeCell
	 * 
	 * @return		The list of cells
	 */
	public ArrayList<Cell> getSubCells() {
		return value;
	}
	
	/**
	 * setSubCells sets the list of subCells to a given list of cells
	 * 
	 * @param features	The new list of subCells
	 */
	public void setSubCells(ArrayList<Cell> features) {
        this.value = features;
    }
	/**
	 * getSubCell returns a single cell that has the given key. Returns null if no such cell exists.
	 * 
	 * @param key	The key of the desired cell
	 * @return		The cell that has the given key. Null if there isn't a cell with that key.
	 */
	public Cell getSubCell(String key) {
		for (Cell c: value) {
			if(c.getKey().equals(key)) {
				return c;
			} else if (c instanceof CompositeCell){
				Cell d = ((CompositeCell)c).getSubCell(key);
				if (d != null) {
					return d;
				}
			}
		}
		return null;
	}
	
	/**
	 * Adds a single cell to the list of subCells.
	 * 
	 * @param f		The DataModel.Cell to be added to the list.
	 */
	public void addCell(Cell f) {
		value.add(f);
	}

    /**
     * Returns the Cell in a specified format that is given
     * @return finalString		The final string composed of all the cells in the value array
     */
	public String toString(){
        String finalString = "";

        System.out.println(value.size());

        for (Cell x: value) {

            System.out.println(finalString);
            SimpleCell i = (SimpleCell)x;

            if(finalString == ""){
                finalString = i.toString();
            }else{
                finalString = finalString + ", " + i.toString();
            }


        }

        return finalString;
    }
}
