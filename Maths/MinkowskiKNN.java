package Maths;

import java.util.ArrayList;
import java.util.HashMap;

import DataModel.Cell;
import DataModel.CompositeCell;
import DataModel.DimensionalSpace;
import DataModel.Point;
import DataModel.SimpleCell;
import Maths.kNN.Tuple;

/**
 * Maths.MinkowskiKNN extends the Maths.kNN abstract method and uses an Minkowski distance metric for the calculations.
 * The formula is [distance] = (sum((endPoint - origin) ^ order)) ^ (1/order) where the sum adds the values for every 
 * dimension, and order is the power to use.
 * 
 * @author Darren
 * @version Milestone 3
 *
 */
public class MinkowskiKNN extends kNN {

	private int order;
	
	/**
	 * Constructor for Maths.EuclideanKNN. Takes a DataModel.DimensionalSpace object that it will work in and the order
	 * to be used for the calculation. Calls the Maths.kNN constructor.
	 * 
	 * @param ds	The DataModel.DimensionalSpace the the funciton will work in.
	 * @param n		The order for the Minkowski function.
	 */
	public MinkowskiKNN(DimensionalSpace ds, int n) {
		super(ds);
		this.order = n;
	}

	/**
	   * findKNN finds the target value for the given point using a Minkowski KNN algorithm
	   * with the k nearest points. Calls the super class's findValue funtion once the nearest neighbours
	   * are found.
	   * 
	   * @param targetKey		The key to be found
	   * @param targetPoint		The point whose value is to be found
	   * @param neighbours		The number of nearest neighbours to query
	   * 
	   * @return				The unknown value of the given point
	   */
	@Override
	public Cell findKNN(String targetKey, Point targetPoint, int neighbours) {
		NumericalDistance nDist = new NumericalDistance();
		StringDistance sDist = new StringDistance();
		
		ds.findStatistics();
		targetPoint.normalize(ds.getMean(), ds.getStdDev());
		
		ArrayList<Point> points = ds.getPoints();
		
		if (neighbours <= 0) neighbours = 1;
	    if (neighbours > points.size()) neighbours = points.size();
		
	    ArrayList<Tuple<Float, Cell>> closestKNeighbours = new ArrayList<Tuple<Float, Cell>>(neighbours);
		Tuple<Float, Cell> displacedPoint, placeHolder;
		float distance;
		boolean displacement;
		HashMap<String, Cell> currentPtValues;
		HashMap<String, Cell> targetPtValues = targetPoint.getStdValues();
		
		for (int i = 0; i < neighbours; i++) {
			closestKNeighbours.add(i, null);
		}
		
		for (Point pt: points) {
			if (!(pt.equals(targetPoint))) {
				displacement = false;
				displacedPoint = null;
				distance = 0;
				currentPtValues = pt.getStdValues();
				
				for (String key: currentPtValues.keySet()) {
					if (!(key.equals(targetKey))) {
						if (currentPtValues.get(key) instanceof SimpleCell) {
							if (((SimpleCell)currentPtValues.get(key)).getValue() instanceof String) {
								distance += Math.pow(sDist.calcDistance((SimpleCell)targetPtValues.get(key), (SimpleCell)currentPtValues.get(key)), order);
							} else {
								distance += Math.pow(nDist.calcDistance((SimpleCell)targetPtValues.get(key), (SimpleCell)currentPtValues.get(key)), order);
							}
						} else {
							distance += Math.pow(MinkowskiComposite((CompositeCell) currentPtValues.get(key), (CompositeCell) targetPtValues.get(key)), order);
						}
					}
				}
		
				distance = (float) Math.pow(distance, 1.0 / order);

				
				for (int i = 0; i < neighbours; i ++) {
					if (!displacement) {
						// If the current spot has not been used yet

						if (closestKNeighbours.get(i) == null) {

							closestKNeighbours.set(i, new Tuple<Float, Cell>(distance, pt.getCell(targetKey)));
							break;
							
						// If the current point is displacing other point(s)
						} else if (distance < closestKNeighbours.get(i).getValue1()) {
							displacedPoint = closestKNeighbours.get(i);
							closestKNeighbours.set(i, new Tuple<Float, Cell>(distance, pt.getCell(targetKey)));
							displacement = true;
						}
					} else {
						// If the current spot has not been used yet

						if (closestKNeighbours.get(i) == null) {

							closestKNeighbours.set(i, new Tuple<Float, Cell>(distance, pt.getCell(targetKey)));
							break;
							
						// If the current point is displacing other point(s)
						} else if (distance < closestKNeighbours.get(i).getValue1()) {
							placeHolder = closestKNeighbours.get(i);
							closestKNeighbours.set(i, displacedPoint);
							displacedPoint = placeHolder;
						}
					}
				}
			}
		}
		

		Cell c = findValue(closestKNeighbours, targetKey);
		targetPoint.addAttribute(c);
		
		return c;
	}

	/**
	 * Iterates through all of the sub cells in a DataModel.CompositeCell and calculates the distance from that DataModel.CompositeCell to
	 * the target point's corresponding DataModel.CompositeCell.
	 * 
	 * @param current	The DataModel.CompositeCell of the non-target point
	 * @param target	The DataModel.CompositeCell of the target point
	 * @return			The distance between the Cells
	 */
	public float MinkowskiComposite(CompositeCell current, CompositeCell target) {
	  	
		NumericalDistance nDist = new NumericalDistance();
		StringDistance sDist = new StringDistance();
		ArrayList<Cell> subCells = current.getSubCells();
		float distance = 0;
		
		for (Cell c: subCells) {
			if (c instanceof SimpleCell) {
				if (((SimpleCell) c).getValue() instanceof String) {
					distance += Math.pow(sDist.calcDistance((SimpleCell) target.getSubCell(c.getKey()), (SimpleCell) c), order);
				} else {
					distance += Math.pow(nDist.calcDistance((SimpleCell) target.getSubCell(c.getKey()), (SimpleCell) c), order);
				}
			} else {
				distance += MinkowskiComposite((CompositeCell) c, (CompositeCell) target.getSubCell(c.getKey()));
			}
		}
		
		return (float) Math.pow(distance, 1.0 / order);
	}
}
