
import java.util.ArrayList;
import java.util.HashMap;

/*
 * kNN is an abstarct class providing methods and variables for various kNN algorithms.
 * 
 * @author Darren
 * @version Milestone 2
 * 
 * @author Darren
 *
 */
public abstract class kNN {
	
	protected DimensionalSpace ds;
	
	/**
	 * Constructs a new kNN object
	 * 
	 * @param ds	The DimensionalSpace that the kNN algorithm is working with.
	 */
	public kNN (DimensionalSpace ds) {
		this.ds = ds;
	}
	
	/**
	 * findKNN finds the k nearest neighbours. This is an abstract class that is instantiated by the seperate kNN algorithms.
	 * 
	 * @param targetKey		The key of the value to be found.
	 * @param targetPoint	The point that the value is to be found for.
	 * @param neighbours	The number of neighbours to use
	 * @return				A Cell containing the information claculated value(s).
	 */
	public abstract Cell findKNN(String targetKey, Point targetPoint, int neighbours);
	
	/**
	 * findValue finds the target value given the k nearest neighbours, and the target key.
	 * 
	 * @param closestKNeighbours	An ArrayList of Tuples containing the distance and corresponding Cell of 
	 * 									each of the k nearest neighbours.
	 * @param targetKey				The key of the value to be found
	 * @return						A cell containing the calculated value(s)
	 */
	public Cell findValue(ArrayList<Tuple<Float, Cell>> closestKNeighbours, String targetKey) {
		if (closestKNeighbours.get(0).getValue2() instanceof SimpleCell) {
			if (((SimpleCell)closestKNeighbours.get(0).getValue2()).getValue() instanceof String) {
				return calculateSimpleCell(closestKNeighbours, "String", targetKey);
			} else if (((SimpleCell)closestKNeighbours.get(0).getValue2()).getValue() instanceof Integer) {
				return calculateSimpleCell(closestKNeighbours, "Integer", targetKey);
			} else if (((SimpleCell)closestKNeighbours.get(0).getValue2()).getValue() instanceof Float) {
				return calculateSimpleCell(closestKNeighbours, "Float", targetKey);
			}
		} else {
			return calculateCompositeCell(closestKNeighbours, targetKey);
		}
		return null;
	}
	
	/**
	 * calculateCompositeCell calculates the kNN value if the target key corresponds to a CompositeCell
	 * 
	 * @param closestKNeighbours	An ArrayList of Tuples containing the distance and corresponding Cell of 
	 * 									each of the k nearest neighbours.
	 * @param targetKey				The key of the value to be found
	 * @return						A CompositeCell containing the calculated value(s)
	 */

	public CompositeCell calculateCompositeCell(ArrayList<Tuple<Float, Cell>> closestKNeighbours, String targetKey) {
		
		ArrayList<Cell> cellsToCalc = ((CompositeCell)closestKNeighbours.get(0).getValue2()).getSubCells();
		CompositeCell target = new CompositeCell(targetKey);
		
		for (Cell c: cellsToCalc) {
			ArrayList<Tuple<Float, Cell>> subList = new ArrayList<Tuple<Float, Cell>>(closestKNeighbours.size());
			for(int i = 0; i < closestKNeighbours.size(); i++) {
				subList.add(i, new Tuple<Float, Cell>(closestKNeighbours.get(i).getValue1(), ((CompositeCell)closestKNeighbours.get(i).getValue2()).getSubCell(c.getKey()))); 
			}
			
			if (c instanceof SimpleCell) {
				if (((SimpleCell) c).getValue() instanceof String) {
					target.addCell(calculateSimpleCell(subList, "String", c.getKey()));
				} else if (((SimpleCell) c).getValue() instanceof Integer) {
					target.addCell(calculateSimpleCell(subList, "Integer", c.getKey()));
				} else if (((SimpleCell) c).getValue() instanceof Float) {
					target.addCell(calculateSimpleCell(subList, "Float", c.getKey()));
				}
			} else {
				target.addCell(calculateCompositeCell(subList, c.getKey()));
			}
		}
		
		return target;
	}

	/**
	 * calculateSimpleCell calculates the kNN value if the target key corresponds to a SimpleCell. This is called by
	 * calculateCompositeCell when it needs to calculate SimpleCell values. For numeric values, this funcitons returns
	 * the average as the value. For String values, this function returnseither the most common String among the nearest 
	 * neighbours, or in the case of a tie, the closest String of the nighbours involved. For example, if every String 
	 * appears once, this function will return the String of the nearest neighbour.
	 * 
	 * @param closestKNeighbours	An ArrayList of Tuples containing the distance and corresponding Cell of 
	 * 									each of the k nearest neighbours.
	 * @param targetKey				The key of the value to be found
	 * @return						A SimpleCell containing the calculated value(s)
	 */

	public Cell calculateSimpleCell(ArrayList<Tuple<Float, Cell>> closestKNeighbours, String type, String targetKey) {
		
		switch (type){
			case "String":
				HashMap<String, Integer> stringCount = new HashMap<String, Integer>();
				String s;
				
				for (int i= 0; i < closestKNeighbours.size(); i++) {
					s = (String)((SimpleCell)closestKNeighbours.get(i).getValue2()).getValue();
					if (stringCount.containsKey(s)) {
						stringCount.put(s, stringCount.get(s) + 1);
					} else {
						stringCount.put(s, 1);
					}
				}
				
				s = (String)((SimpleCell)closestKNeighbours.get(0).getValue2()).getValue();
				int count = stringCount.get(s);
				
				for (String key: stringCount.keySet()) {
					if (stringCount.get(key) > count) {
						s = key;
						count = stringCount.get(key);
					} else if (stringCount.get(key) == count) {
						for (Tuple<Float, Cell> t: closestKNeighbours) {

							if (((String)((SimpleCell)t.getValue2()).getValue()).equals(s)) {
								break;
							} else if (((String)((SimpleCell)t.getValue2()).getValue()).equals(key)) {

								s = key;
								break;
							}
						}
					}
				}
				return new SimpleCell<String>(targetKey, s);
				
			case "Integer":
				int iValue = 0;
				
				for (Tuple<Float, Cell> t: closestKNeighbours) {
					iValue += (int)((SimpleCell)t.getValue2()).getValue();
				}
				return new SimpleCell<Integer>(targetKey, iValue/closestKNeighbours.size());
				
			case "Float":
				float fValue = 0;
				
				for (Tuple<Float, Cell> t: closestKNeighbours) {
					fValue += (float)((SimpleCell)t.getValue2()).getValue();
				}
				return new SimpleCell<Float>(targetKey, fValue/closestKNeighbours.size());
		}
		
		return null;
	}
	
	/**
	   * Simple Tuple class to hold 2 ordered values for use in the findKNN function.
	   * 
	   * @author Darren
	   *
	   * @param <E>	Type of the first value.
	   * @param <K>	Type of the second value
	   */

	public static class Tuple<E, K> {
		private E value1;
		private K value2;
		
		/**
		 * Tuple constructor.
		 * 
		 * @param val1		The first value of type E.
		 * @param val2		The second value of type K.
		 */
		public Tuple(E val1, K val2) {
			this.value1 = val1;
			this.value2 = val2;
		}

		/**
		 * @return		The first value.
		 */
		public E getValue1() {
			return value1;
		}

		/**
		 * @param x	The new value for the first value (of type E).
		 */
		public void setValue1(E x) {
			this.value1 = x;
		}
		
		/**
		 * @return		The second value.
		 */
		public K getValue2() {
			return value2;
		}
		
		/**
		 * @param x	The new value for the second value (of type K).
		 */
		public void setValue2(K x) {
			this.value2 = x;
		}
	  }
}
