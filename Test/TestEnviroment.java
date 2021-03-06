package Test;
import DataModel.Point;
import DataModel.CellSimple;
import DataModel.DimensionalSpace;
import View.View;

/**
 * Class to test the Maths.kNN algorithm using a dataset and a test point.
 *
 * @version Milestone 4
 * @author Benjamin
 */
public class TestEnviroment {

    private DimensionalSpace dataSet;

    /**
     * Initializes the test environment to a set of points for one of two cases.
     *
     * @param case1	True for the first dataset, false for the second.
     */
    public TestEnviroment(boolean case1) {
        dataSet = new DimensionalSpace();

    }

    /**
     * KNN test for the first dataset.
     *
     * @param k	The number of neighbours to use.
     * @return	The calculated point value.
     */


    /**
     * Determines how accurate the Maths.kNN function if a known value for the point
     * is available.
     *
     * @param actualValue	The actual value for the point
     * @param value	The calculated value for the point
     * @return	The accuracy value
     */
    public double accuracy(int actualValue, int value) {
        return ((actualValue - (actualValue - value)) / actualValue) * 100;
    }

    /**
     * @return	The max possible value of k (the size of the dataset).
     */
    public int getSize() {
        return dataSet.getNumberOfPoints();
    }

    /**
     * main function used to test the function based on the datasets.
     *
     * @param args
     */
    public static void main(String[] args) {
    	View view = new View();
    	
    	// The following is code to input the tabulated data provided. To start from scratch, comment it out

    }

	

}
