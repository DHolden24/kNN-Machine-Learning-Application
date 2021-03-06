package Maths;

import DataModel.CellSimple;

/**
 * Calculates the distance between two numerical SimpleCells. Simply returns the absolute difference between the given values.
 * 
 * @author Darren
 * @version Milestone 4
 *
 */

public class NumericalDifference extends DistanceAlg {

	/* (non-Javadoc)
	 * @see Maths.DistanceAlg#calcDistance(DataModel.CellSimple, DataModel.CellSimple)
	 */
	@Override
	public float calcDistance(CellSimple target, CellSimple current) {

		float val1, val2;
		
		try {
			if (target.getValue() instanceof Integer){
				val1 = (float) ((int)target.getValue());
				val2 = (float) ((int)current.getValue());
			} else {
				val1 = (float) target.getValue();
				val2 = (float) current.getValue();
			}
			
			return Math.abs(val1 - val2);
		} catch (NullPointerException ne) {
			return 0;
		}

	}

}
