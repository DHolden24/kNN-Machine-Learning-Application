package CodersInc;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.Math;
import java.util.Iterator;
import java.util.Set;

/**
 * Class to hold the points from the dataset. This class calculates the sum, mean, and standard 
 * deviation of all the known points which are used to standardize the points to give more accurate
 * calculations. This class also contains the K-nearest neighbour function which calculates the value
 * of a new point.
 * 
 * @author Darren and Andrew
 * @version Milestone 2
 */

public class DimensionalSpace {
  private ConcurrentHashMap<String, Float> mean;
  private ConcurrentHashMap<String, Float> stddev;
  private ConcurrentHashMap<String, Float> sum;
  private int numberOfPoints;
  private ArrayList<Point> points;

  
  /**
   * Constructor for instances of the DimensionalSpace class. Initializes the class variables and sets the
   * current number of points to 0.
   */
  public DimensionalSpace(){
    mean = new ConcurrentHashMap<String, Float>();
    stddev = new ConcurrentHashMap<String, Float>();
    sum = new ConcurrentHashMap<String, Float>();
    points = new ArrayList<Point>();
    numberOfPoints = 0;
  }
  
  
  /**
   * Adds the given ArrayList of points to the DimensionalSpace's list of points. Calls the addPt funtion
   * on each point in the ArrayList.
   * 
   * @param pts 		An ArrayList of Point objects to be added to the space.
   */
  public void addPts(ArrayList<Point> pts){
	  if (pts.size() == 0) return;
	  
	  for (Point pt: pts) {
		  addPt(pt);
	  }
  }
  
  
  /**
   * Adds the given Point to the DimensionalSpace's list of points. Also adds the values from each of the
   * point's "coordinates" to the corresponding sum value.
   * 
   * @param pt 			The Point to be added to the space.
   */
  public void addPt(Point pt){
    points.add(pt);
    numberOfPoints++;
  }
  
  /**
   * Calculates the statistical values for the dimensional space.
   */
  public void findStatistics() {
	  if (numberOfPoints <= 1) return;
	  findSum();
	  findMean();
	  findStdDev();
	  normalize();
  }
  
  /**
   * Calculates all of the sums for numeric parameters.
   */
  public void findSum() {
	  HashMap<String, Cell> cellList;
	  for (Point pt: points) {
		  cellList = pt.getRawValues();
		  for (String k: cellList.keySet()) {
			  Cell c = cellList.get(k);
			  
			  if (c instanceof SimpleCell) {
				  if (!(((SimpleCell)c).getValue() instanceof String)) {
					  calcSimpleSum((SimpleCell)c);
				  }
			  } else {
				  calcComplexSum((CompositeCell)c);
			  }
		  }
	  }
  }
  
  /**
   * Increments the sum for all of the numeric values contained within a CompositeCell
   * 
   * @param c		The CompositeCell to be used to increment the corresponding sums.
   */
  private void calcComplexSum(CompositeCell c) {
	  ArrayList<Cell> subCells = c.getSubCells();
	  
	  for (Cell subC: subCells) {
		  if (subC instanceof SimpleCell) {
			  if (!(((SimpleCell)subC).getValue() instanceof String)) {
				  calcSimpleSum((SimpleCell)subC);
			  }
		  } else {
			  calcComplexSum((CompositeCell)subC);
		  }
	  }
  }

  /**
   * Increments the sum for the key corresponding to a numeric SimpleCell
   * 
   * @param c		The SimpleCell to use to increment the sum
   */
  private void calcSimpleSum(SimpleCell c) {
	  float val;
	  if (c.getValue() instanceof Integer) {
		  val = (float)(int)c.getValue();
	  } else {
		  val = (float)c.getValue();
	  }
	  
	  if (sum.containsKey(c.getKey())) {
		  sum.put(c.getKey(), val + sum.get(c.getKey()));
	  } else {
		  sum.put(c.getKey(), val);
	  }
  }


  /**
   * Finds the average value for each of the numeric values wich define the points.
   */
  public void findMean(){
      for (String key: sum.keySet()) {
    	  mean.put(key, sum.get(key) / numberOfPoints);
      }
  }
  
  /**
   * findStdDev calculates the standard deviation for every numeric value.
   */
  public void findStdDev(){
	  float val, u;
	  for (String k: mean.keySet()) {
		  val = 0;
		  u = mean.get(k);
		  if (((SimpleCell)points.get(0).getCell(k)).getValue() instanceof Integer) {
			  for (Point pt: points) {
				  val += Math.pow(((float)(int)((SimpleCell)pt.getCell(k)).getValue()) - u, 2);
			  }
		  } else {
			  for (Point pt: points) {
				  val += Math.pow(((float)((SimpleCell)pt.getCell(k)).getValue()) - u, 2);
			  }
		  }
		  stddev.put(k, (float) Math.sqrt(val / (numberOfPoints - 1)));
	  }
  }
  
  /**
   * normalize normalizes every numerical valuepoint in the space based on the calculated means and standard deviations.
   */
  public void normalize() {
	  for (Point pt: points) {
		  pt.normalize(mean, stddev);
	  }
  }
  
  /**
   * FindkNN finds the "cost" value for the given point using a KNN algorithm
   * with the k nearest points
   * 
   * @param targetKey		The key of the value to be found.
   * @param targetPoint		The point whose value is to be found
   * @param k				The number of nearest neighbours to query
   * 
   * @return				The unknown value of the given point
   */
  public String findkNN(String targetKey, Point targetPoint, int neighbours){
	  String resultStr = "";
	  EuclideanKNN eucKNN = new EuclideanKNN(this);
	  
	  Cell resultCell = eucKNN.findKNN(targetKey, targetPoint, neighbours);
	  
	  if (resultCell instanceof SimpleCell) {
		  resultStr = "The " + neighbours + " nearest neighbours to the target point gave a value of " + ((SimpleCell)resultCell).getValue() + " for the " + targetKey + " parameter.";
	  } else {
		  resultStr = "The " + neighbours + " nearest neighbours to the target point gave a composite value for the " + targetKey + " parameter:\n";
		  
		  for (Cell c: ((CompositeCell)resultCell).getSubCells()) {
			  resultStr += c.getKey() + ": " + ((SimpleCell)c).getValue();
		  }
	  }
	  
	  return resultStr;	    
  }
  
  /**
   * Sets the mean values to the given HashMap.
   * 
   * @param newMean			HashMap containing the mean for each key.
   */
  public void setMean(ConcurrentHashMap<String, Float> newMean){
    mean = newMean;
  }

  /**
   * @return				HashMap containing the the mean values for the dataset.
   */
  public ConcurrentHashMap<String, Float> getMean(){
    return mean;
  }
  
  /**
   * Sets the standard deviation values to the given HashMap.
   * 
   * @param newDev		HashMap containing the standard deviation for each key.
   */
  public void setStdDev(ConcurrentHashMap<String, Float> newDev){
    stddev = newDev;
  }
  
  /**
   * @return				HashMap containing the the standard deviation values for the dataset.
   */
  public ConcurrentHashMap<String, Float> getStdDev(){
    return stddev;
  }

  /**
   * Sets the summed values to the given HashMap.
   * 
   * @param newSum		HashMap containing the sum for each key.
   */
  public void setSum(ConcurrentHashMap<String, Float> newSum){
    sum = newSum;
  }

  /**
   * @return				HashMap containing the the summed values for the dataset.
   */
  public ConcurrentHashMap<String, Float> getSum(){
    return sum;
  }

  
  /**
   * Sets the space's points to the given ArrayList
   * 
   * @param pts				ArrayList of the points to be used for the space.
   */
  public void setPoints(ArrayList<Point> pts){
    points = pts;
    numberOfPoints = pts.size();
  }
  
  /**
   * @return				The ArrayList of Points in the space.
   */
  public ArrayList<Point> getPoints(){
    return points;
  }
  
  
  /**
   * @return				The current number of points in the space.
   */
  public int getNumberOfPoints() {
	  return numberOfPoints;
  }
}
