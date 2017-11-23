package View;

import Controlers.FeaturePanelSimpleController;
import View.View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import Controlers.*;

/**
 * A JPanel allowing a user to create a simple feature
 * @author Gabrielle
 *
 */
public class FeaturePanelSimple extends FeaturePanel{

	//Things specialized for this particular input
	private JLabel  value;
	private JComboBox<String> featureClass;

	/**
	 * Default constructor for a View.FeaturePanelSimple with no parent
	 * @param view - the View.View which the panel will appear in
	 * @param types - the primitive types the user will choose from when creating a feature
	 * @param superFeatureName - the name of the parent panel. Empty if does not exist
	 * @param tab - the placement of the panel, linked to whether or not it is a subfeature panel
	 */
	public FeaturePanelSimple(View view, ArrayList<String> types, String superFeatureName, int tab)
	{
		super(view, types, superFeatureName, tab);
		String[] typesArray = types.toArray(new String[0]);
		featureClass = new JComboBox<String>(typesArray);
		value = new JLabel("Feature type: ");

		innerPanel.add(value);
		innerPanel.add(featureClass);
        innerPanel.add(add);
		controller = new FeaturePanelSimpleController(this);
		add.addActionListener(controller);
	}
	
	/**
	 * Calls the default constructor, and initialized the parentComplex variable
	 * 
	 * @param view
	 * @param types
	 * @param superFeatureName
	 * @param tab
	 * @param fp
	 */
	public FeaturePanelSimple(View view, ArrayList<String> types, String superFeatureName, int tab, FeaturePanelComplex fp)
	{
		this(view, types, superFeatureName, tab);
		if(fp != null)
		{
			parent = fp;
		}
	}
	
	
	/**
	 * Returns the value/feature class denoted by the user's choice the JComboBox featureClass
	 * @return String
	 */
	public String getValue()
	{
		String s="";
		try
		{
			s = (String) featureClass.getSelectedItem();
		}
		catch(Exception e)
		{
			view.sendErrorFrame("Not a valid feature type");
		}
		return s;
	}


	
	/**
	 * Disables the JTextField and JComBobox
	 */
	public void disable()
    {
		featureName.setEnabled(false);
		featureClass.setEnabled(false);
		add.setEnabled(false);
	}

	public String getKeyParentName() {
		// TODO Auto-generated method stub
		return null;
	}
}
	


