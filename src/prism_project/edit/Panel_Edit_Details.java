/*******************************************************************************
 * Copyright (C) 2016-2018 PRISM Development Team
 * 
 * PRISM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * PRISM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with PRISM.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package prism_project.edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatter;

import prism_convenience.FilesHandle;
import prism_convenience.IconHandle;
import prism_convenience.MixedRangeCombinationIterable;
import prism_convenience.NumberHandle;
import prism_convenience.PrismGridBagLayoutHandle;
import prism_convenience.PrismTableModel;
import prism_convenience.PrismTextAreaReadMe;
import prism_convenience.PrismTitleScrollPane;
import prism_convenience.TableColumnsHandle;
import prism_convenience.ToolBarWithBgImage;
import prism_project.data_process.Read_Database;
import prism_root.Prism2Main;

public class Panel_Edit_Details extends JLayeredPane implements ActionListener {
	private JSplitPane GUI_Text_splitpane;
	private JPanel radio_panel;
	private ButtonGroup radio_buttongroup;
	private JRadioButton[] radio_button;
	
	private File currentRunFolder;
	private File file_database;
	private Read_Database read_database;
	
	private int total_period;
	private int total_replacing_disturbance;
	
	// panels for the selected Run
	private General_Inputs_GUI panel_General_Inputs_GUI;
	private Model_Strata_GUI panel_Model_Strata_GUI;
	private Non_EA_Management_GUI panel_Non_EA_Management_GUI;
	private EA_Management_GUI panel_EA_Management_GUI;
	private Non_SR_Disturbances_GUI panel_Non_SR_Disturbances_GUI;
	private SR_Disturbances_GUI panel_SR_Disturbances_GUI;
	private Management_Cost_GUI panel_Management_Cost_GUI;
	private Basic_Constraints_GUI panel_Basic_Constraints_GUI;
	private Flow_Constraints_GUI panel_Flow_Constraints_GUI;
	private Area_Merging_GUI panel_Area_Merging_GUI;

	// table model overView
	private boolean is_table_overview_loaded = false;
	private int rowCount_overview, colCount_overview;
	private String[] columnNames_overview;
	private JTable table_overview;
	private PrismTableModel model_overview;
	private Object[][] data_overview;
	private double modeledAcres, availableAcres;
	
	// table input_01_general_inputs.txt
	private boolean is_table1_loaded = false;
	private int rowCount1, colCount1;
	private String[] columnNames1;
	private JTable table1;
	private PrismTableModel model1;
	private Object[][] data1;
	
	// table input_02_model_strata.txt
	private boolean is_table3_loaded = false;
	private int rowCount3, colCount3;
	private String[] columnNames3;
	private JTable table3;
	private PrismTableModel model3;
	private Object[][] data3;
	
	// table input_03_prescription_assignment.txt
	private boolean is_table2_loaded = false;
	private int rowCount2, colCount2;
	private String[] columnNames2;
	private JTable table2;
	private PrismTableModel model2;
	private Object[][] data2;
	
	// table input_04_ea_management.txt
	private boolean is_table4_loaded = false;
	private int rowCount4, colCount4;
	private String[] columnNames4;
	private JTable table4;
	private PrismTableModel model4;
	private Object[][] data4;
	
	// table input_04a
	private boolean is_table4a_loaded = false;
	private int rowCount4a, colCount4a;
	private String[] columnNames4a;
	private JTable table4a;
	private PrismTableModel model4a;
	private Object[][] data4a;

	// table input_05_non_sr_disturbances.txt
	private boolean is_table5_loaded = false;
	private int rowCount5, colCount5;
	private String[] columnNames5;
	private JTable table5;
	private PrismTableModel model5;
	private Object[][] data5;
	
	// table input_06_sr_disturbances.txt
	private boolean is_table6_loaded = false;
	private int rowCount6, colCount6;
	private String[] columnNames6;
	private JTable table6;
	private PrismTableModel model6;
	private Object[][] data6;
	
	// table input_06a --> loss probability mean
	private boolean is_table6a_loaded = false;
	private int rowCount6a, colCount6a;
	private String[] columnNames6a;
	private JTable table6a;
	private PrismTableModel model6a;
	private Object[][] data6a;
	
	// table input_06b --> loss probability standard deviation
	private boolean is_table6b_loaded = false;
	private int rowCount6b, colCount6b;
	private String[] columnNames6b;
	private JTable table6b;
	private PrismTableModel model6b;
	private Object[][] data6b;
	
	// table input_06c --> conversion rate mean
	private boolean is_table6c_loaded = false;
	private int rowCount6c, colCount6c;
	private String[] columnNames6c;
	private JTable table6c;
	private PrismTableModel model6c;
	private Object[][] data6c;	
	
	// table input_06d --> conversion rate standard deviation
	private boolean is_table6d_loaded = false;
	private int rowCount6d, colCount6d;
	private String[] columnNames6d;
	private JTable table6d;
	private PrismTableModel model6d;
	private Object[][] data6d;	
	
	// table input_07_management_cost.txt
	private boolean is_table7_loaded = false;
	private int rowCount7, colCount7;
	private String[] columnNames7;
	private JTable table7;
	private PrismTableModel model7;
	private Object[][] data7;
	
	// table input_07a --> action_cost
	private boolean is_table7a_loaded = false;
	private int rowCount7a, colCount7a;
	private String[] columnNames7a;
	private JTable table7a;
	private PrismTableModel model7a;
	private Object[][] data7a;
	
	// table input_07b --> conversion_cost
	private boolean is_table7b_loaded = false;
	private int rowCount7b, colCount7b;
	private String[] columnNames7b;
	private JTable table7b;
	private PrismTableModel model7b;
	private Object[][] data7b;	
	
	// table input_08_basic_constraints.txt
	private boolean is_table8_loaded = false;
	private int rowCount8, colCount8;
	private String[] columnNames8;
	private JTable table8;
	private PrismTableModel model8;
	private Object[][] data8;
	
	// table input_09_flow_constraints.txt
	private boolean is_table9_loaded = false;
	private int rowCount9, colCount9;
	private String[] columnNames9;
	private JTable table9;
	private PrismTableModel model9;
	private Object[][] data9;	
	
	// table input_10_area_merging.txt
	private boolean is_table10_loaded = false;
	private int rowCount10, colCount10;
	private String[] columnNames10;
	private JTable table10;
	private PrismTableModel model10;
	private Object[][] data10;
	
	// table input_11_state_id.txt		This is a special input that will not be loaded when we load the run. It will be generated and saved when we save the run
	private String[] columnNames11;
	private Object[][] data11;
		
	// others
	private PrismTextAreaReadMe readme = new PrismTextAreaReadMe("icon_tree.png", 70, 70);

	
	public Panel_Edit_Details(File RunFolder) {
		super.setLayout(new BorderLayout());	
		currentRunFolder = RunFolder;		// Get information from the run

		
		// Create the interface ---------------------------------------------------------------------------------------------------------------------
		// Add 10 input options to radioPanel and add that panel to scrollPane_Right at combinePanel NORTH
		radio_panel = new JPanel();
		radio_panel.setLayout(new FlowLayout());	
		radio_panel.setPreferredSize(new Dimension(0, 45));
		radio_buttongroup = new ButtonGroup();
		
		radio_button  = new JRadioButton[10];
		radio_button[0]= new JRadioButton("General Inputs");
		radio_button[1]= new JRadioButton("Model Strata");
		radio_button[2]= new JRadioButton("Prescription Assignment");
		radio_button[3]= new JRadioButton("EA Management");
		radio_button[4]= new JRadioButton("Non-SR Disturbances");
		radio_button[5]= new JRadioButton("SR Disturbances");
		radio_button[6]= new JRadioButton("Management Cost");
		radio_button[7]= new JRadioButton("Basic Constraints");
		radio_button[8]= new JRadioButton("Flow Constraints");
		radio_button[9]= new JRadioButton("Area Merging");
		radio_button[0].setSelected(true);
		for (int i = 0; i < radio_button.length; i++) {
				radio_buttongroup.add(radio_button[i]);
				radio_panel.add(radio_button[i]);
				radio_button[i].addActionListener(this);
		}	
		
		GUI_Text_splitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		GUI_Text_splitpane.setDividerSize(0);
			
	
		// Create all new 9 panels for the selected Run--------------------------------------------------
		reload_inputs();	
		
		
		// Add all components to The Panel------------------------------------------------------------
		super.add(radio_panel, BorderLayout.NORTH);
		super.add(GUI_Text_splitpane, BorderLayout.CENTER);
		super.setOpaque(false);
		ToolTipManager.sharedInstance().setInitialDelay(0);	// Show toolTip immediately
	}

		
	// Listener for radio buttons------------------------------------------------------------------------------------------------
    public void actionPerformed(ActionEvent e) {
		for (int j = 0; j < radio_button.length; j++) {
			if (radio_button[j].isSelected()) {		
				if (j == 0) {
					GUI_Text_splitpane.setLeftComponent(panel_General_Inputs_GUI);
					GUI_Text_splitpane.setRightComponent(null);
				} else if (j == 1) {
					GUI_Text_splitpane.setLeftComponent(panel_Model_Strata_GUI);
					GUI_Text_splitpane.setRightComponent(null);
				} else if (j == 2) {
					GUI_Text_splitpane.setLeftComponent(panel_Non_EA_Management_GUI);
					GUI_Text_splitpane.setRightComponent(null);
				} else if (j == 3) {
					GUI_Text_splitpane.setLeftComponent(panel_EA_Management_GUI);
					GUI_Text_splitpane.setRightComponent(null);
				} else if (j == 4) {
					GUI_Text_splitpane.setLeftComponent(panel_Non_SR_Disturbances_GUI);
					GUI_Text_splitpane.setRightComponent(null);
				} else if (j == 5) {
					GUI_Text_splitpane.setLeftComponent(panel_SR_Disturbances_GUI);
					GUI_Text_splitpane.setRightComponent(null);
				} else if (j == 6) {
					GUI_Text_splitpane.setLeftComponent(panel_Management_Cost_GUI);
					GUI_Text_splitpane.setRightComponent(null);
				} else if (j == 7) {
					GUI_Text_splitpane.setLeftComponent(panel_Basic_Constraints_GUI);
					GUI_Text_splitpane.setRightComponent(null);
				} else if (j == 8) {
					GUI_Text_splitpane.setLeftComponent(panel_Flow_Constraints_GUI);
					GUI_Text_splitpane.setRightComponent(null);
				} else if (j == 9) {
					GUI_Text_splitpane.setLeftComponent(panel_Area_Merging_GUI);
					GUI_Text_splitpane.setRightComponent(null);
				}
				
				// Get everything show up nicely
				Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());	//this can replace the below 2 lines
//				PrismMain.get_main().revalidate();
//		    	PrismMain.get_main().repaint(); 
			}
		}
	}


    // Reload inputs of the run------------------------------------------------------------------------------------------------ 
	public void reload_inputs() {
		// These are for reload current edit after fail importation of a new database using the "browse" button
		is_table_overview_loaded = false;
		is_table1_loaded = false;
		is_table3_loaded = false;
		is_table2_loaded = false;
		is_table4_loaded = false;
		is_table4a_loaded = false;
		is_table5_loaded = false;
		is_table6_loaded = false;
		is_table6a_loaded = false;
		is_table6c_loaded = false;
		is_table7_loaded = false;
		is_table7a_loaded = false;
		is_table7b_loaded = false;
		is_table8_loaded = false;
		is_table9_loaded = false;
		is_table10_loaded = false;


		// Load tables---------------------------------------------------------------------------------
		File table_file;
		Reload_Table_Info tableLoader;


		table_file = new File(currentRunFolder.getAbsolutePath() + "/input_01_general_inputs.txt");
		if (table_file.exists()) {        //Load from input
			tableLoader = new Reload_Table_Info(table_file);
			rowCount1 = tableLoader.get_rowCount();
			colCount1 = tableLoader.get_colCount();
			data1 = tableLoader.get_data();
			columnNames1 = tableLoader.get_columnNames();
			is_table1_loaded = true;
		} else { // Create a fresh new if Load fail
			System.err.println("File not exists: " + table_file.getName() + " - New interface is created");
		}


		table_file = new File(currentRunFolder.getAbsolutePath() + "/input_02_model_strata.txt");
		if (table_file.exists()) {        //Load from input
			tableLoader = new Reload_Table_Info(table_file);
			rowCount3 = tableLoader.get_rowCount();
			colCount3 = tableLoader.get_colCount();
			data3 = tableLoader.get_data();
			columnNames3 = tableLoader.get_columnNames();
			is_table3_loaded = true;
		} else { // Create a fresh new if Load fail
			System.err.println("File not exists: " + table_file.getName() + " - New interface is created");
		}


		table_file = new File(currentRunFolder.getAbsolutePath() + "/input_03_prescription_assignment.txt");
		if (table_file.exists()) {        //Load from input
			tableLoader = new Reload_Table_Info(table_file);
			rowCount2 = tableLoader.get_rowCount();
			colCount2 = tableLoader.get_colCount();
			data2 = tableLoader.get_data();
			columnNames2 = tableLoader.get_columnNames();
			is_table2_loaded = true;
		}
		else { // Create a fresh new if Load fail
			System.err.println("File not exists: " + table_file.getName() + " - New interface is created");
		}


		table_file = new File(currentRunFolder.getAbsolutePath() + "/input_04_ea_management.txt");
		if (table_file.exists()) {		//Load from input
			tableLoader = new Reload_Table_Info(table_file);
			rowCount4 = tableLoader.get_rowCount();
			colCount4 = tableLoader.get_colCount();
			data4 = tableLoader.get_data();
			columnNames4 = tableLoader.get_columnNames();
			is_table4_loaded = true;
		} else { // Create a fresh new if Load fail
			System.err.println("File not exists: " + table_file.getName() + " - New interface is created");
		}
		

		table_file = new File(currentRunFolder.getAbsolutePath() + "/input_05_non_sr_disturbances.txt");
		if (table_file.exists()) { // Load from input
			tableLoader = new Reload_Table_Info(table_file);
			rowCount5 = tableLoader.get_rowCount();
			colCount5 = tableLoader.get_colCount();
			data5 = tableLoader.get_data();
			columnNames5 = tableLoader.get_columnNames();
			is_table5_loaded = true;
		} else { // Create a fresh new if Load fail
			System.err.println("File not exists: " + table_file.getName() + " - New interface is created");
		}
		
		
		table_file = new File(currentRunFolder.getAbsolutePath() + "/input_06_sr_disturbances.txt");
		if (table_file.exists()) { // Load from input
			tableLoader = new Reload_Table_Info(table_file);
			rowCount6 = tableLoader.get_rowCount();
			colCount6 = tableLoader.get_colCount();
			data6 = tableLoader.get_data();
			columnNames6 = tableLoader.get_columnNames();
			is_table6_loaded = true;
		} else { // Create a fresh new if Load fail
			System.err.println("File not exists: " + table_file.getName() + " - New interface is created");
		}		

		
		table_file = new File(currentRunFolder.getAbsolutePath() + "/input_07_management_cost.txt");
		if (table_file.exists()) { // Load from input
			tableLoader = new Reload_Table_Info(table_file);
			rowCount7 = tableLoader.get_rowCount();
			colCount7 = tableLoader.get_colCount();
			data7 = tableLoader.get_data();
			columnNames7 = tableLoader.get_columnNames();
			is_table7_loaded = true;
		} else { // Create a fresh new if Load fail
			System.err.println("File not exists: " + table_file.getName() + " - New interface is created");
		}		
		
		table_file = new File(currentRunFolder.getAbsolutePath() + "/input_08_basic_constraints.txt");
		if (table_file.exists()) { // Load from input
			tableLoader = new Reload_Table_Info(table_file);
			rowCount8 = tableLoader.get_rowCount();
			colCount8 = tableLoader.get_colCount();
			data8 = tableLoader.get_data();
			columnNames8 = tableLoader.get_columnNames();
			is_table8_loaded = true;
		} else { // Create a fresh new if Load fail
			System.err.println("File not exists: " + table_file.getName() + " - New interface is created");
		}     
		
		table_file = new File(currentRunFolder.getAbsolutePath() + "/input_09_flow_constraints.txt");
		if (table_file.exists()) { // Load from input
			tableLoader = new Reload_Table_Info(table_file);
			rowCount9 = tableLoader.get_rowCount();
			colCount9 = tableLoader.get_colCount();
			data9 = tableLoader.get_data();
			columnNames9 = tableLoader.get_columnNames();
			is_table9_loaded = true;
		} else { // Create a fresh new if Load fail
			System.err.println("File not exists: " + table_file.getName() + " - New interface is created");
		}  		
		
		table_file = new File(currentRunFolder.getAbsolutePath() + "/input_10_area_merging.txt");
		if (table_file.exists()) { // Load from input
			tableLoader = new Reload_Table_Info(table_file);
			rowCount10 = tableLoader.get_rowCount();
			colCount10 = tableLoader.get_colCount();
			data10 = tableLoader.get_data();
			columnNames10 = tableLoader.get_columnNames();
			is_table10_loaded = true;
		} else { // Create a fresh new if Load fail
			System.err.println("File not exists: " + table_file.getName() + " - New interface is created");
		}  
		
		
		
		
		
		panel_General_Inputs_GUI = new General_Inputs_GUI();
		
		// Load database of the run if exist---------------------------------------------------------------------
		File database_to_load = new File(currentRunFolder.getAbsolutePath() + "/database.db");
		if (database_to_load.exists()) {	// Load if the file exists
			file_database = database_to_load;
			
			// Read the tables (strata_definition, existing_strata, yield_tables) of the database-------------------
			read_database = Prism2Main.get_databases_linkedlist().return_read_database_if_exist(file_database);
			if (read_database == null) {
				read_database = new Read_Database(file_database);	// Read the database
				Prism2Main.get_databases_linkedlist().update(file_database, read_database);			
			}
			
			panel_General_Inputs_GUI.get_database_directory_textfield().setText(file_database.getAbsolutePath());
			panel_Model_Strata_GUI = new Model_Strata_GUI();
			panel_Non_EA_Management_GUI = new Non_EA_Management_GUI();
			panel_EA_Management_GUI = new EA_Management_GUI();
			panel_Non_SR_Disturbances_GUI = new Non_SR_Disturbances_GUI();
			panel_SR_Disturbances_GUI = new SR_Disturbances_GUI();
			panel_Management_Cost_GUI = new Management_Cost_GUI();
			panel_Basic_Constraints_GUI = new Basic_Constraints_GUI();
			panel_Flow_Constraints_GUI = new Flow_Constraints_GUI();
			panel_Area_Merging_GUI = new Area_Merging_GUI();

			// Matching data types after finishing reloads
			model2.match_DataType();		//a smart way to retrieve the original data type :))))))
			model3.match_DataType();		//a smart way to retrieve the original data type :))))))
			model4.match_DataType();		//a smart way to retrieve the original data type :))))))
			model4a.match_DataType();		//a smart way to retrieve the original data type :))))))
			model5.match_DataType();		//a smart way to retrieve the original data type :))))))
			model6.match_DataType();		//a smart way to retrieve the original data type :))))))
			model6a.match_DataType();		//a smart way to retrieve the original data type :))))))
			model6c.match_DataType();		//a smart way to retrieve the original data type :))))))
			model7.match_DataType();		//a smart way to retrieve the original data type :))))))
			model7a.match_DataType();		//a smart way to retrieve the original data type :))))))
			model7b.match_DataType();		//a smart way to retrieve the original data type :))))))
			model8.match_DataType();		//a smart way to retrieve the original data type :))))))
			model9.match_DataType();		//a smart way to retrieve the original data type :))))))
			model10.match_DataType();		//a smart way to retrieve the original data type :))))))
		} else { 	// If file does not exist then use null database
			file_database = null;
			radio_button[1].setEnabled(false);
			radio_button[2].setEnabled(false);
			radio_button[3].setEnabled(false);
			radio_button[4].setEnabled(false);
			radio_button[5].setEnabled(false);
			radio_button[6].setEnabled(false);
			radio_button[7].setEnabled(false);
			radio_button[8].setEnabled(false);
			radio_button[9].setEnabled(false);
			System.out.println("File not exists: database.db - New interface is created");					
		}
		
		GUI_Text_splitpane.setLeftComponent(panel_General_Inputs_GUI);	// Show the General_Inputs of the selected Run
		Prism2Main.get_Prism_DesktopPane().getSelectedFrame().revalidate();
		Prism2Main.get_Prism_DesktopPane().getSelectedFrame().repaint();
    }
    
	
	// Class to reload all tables------------------------------------------------------------------------------------------------ 
	private class Reload_Table_Info {
		private int input_colCount;
		private int input_rowCount;
		private Object[][] input_data;
		private String[] input_columnNames;
    	
		private Reload_Table_Info(File table_file) {
			//Load table to get its 4 attributes
			try {
				String delimited = "\t";		// tab delimited
				List<String> lines_list = Files.readAllLines(Paths.get(table_file.getAbsolutePath()), StandardCharsets.UTF_8);			
												
				// Setup the table---------------------------------
				input_columnNames = lines_list.get(0).split(delimited);		//tab delimited		//Read the first row	
				lines_list.remove(0); 	// remove the  first line which is the column name
				input_rowCount = lines_list.size();
				input_colCount = input_columnNames.length;
				input_data = new Object[input_rowCount][input_colCount];
				
				// Populate the input_data matrix-----------------
				for (int row = 0; row < input_rowCount; row++) {
					String[] rowValue = lines_list.get(row).split(delimited);	//tab delimited
					for (int col = 0; col < input_colCount; col++) {
						input_data[row][col] = rowValue[col];		// cannot make it the same as line #478 in Panel_Project (need to understand why and fix in the future)
					}	
				}
				
				
				// The below code is for adding column "model_condition" at the ending column of the old runs so the old runs have new format (we could and should delete all the below in the future)
				// -----------------------------------------------------------------------------
				// -------------------Begin of where we could delete----------------------------
				// -----------------------------------------------------------------------------
				List<String> list_of_modified_tables = Arrays.asList("input_03_prescription_assignment.txt", "input_04_ea_management.txt", "input_05_non_sr_disturbances.txt", "input_06_sr_disturbances.txt", "input_07_management_cost.txt");
				if (list_of_modified_tables.contains(table_file.getName())) {
					if (!input_columnNames[input_columnNames.length - 1].equals("model_condition")) {
						// Modify old input
						int input_rowCount_temp = input_rowCount;
						int input_colCount_temp = input_colCount + 1;
						
						String[] input_columnNames_temp = new String[input_colCount_temp];
						for (int col = 0; col < input_colCount_temp - 1; col++) {
							input_columnNames_temp[col] = input_columnNames[col];
						}
						input_columnNames_temp[input_colCount_temp - 1] = "model_condition";
						
						Object[][] input_data_temp = new Object[input_rowCount_temp][input_colCount_temp];
						for (int row = 0; row < input_rowCount_temp; row++) {
							for (int col = 0; col < input_colCount_temp - 1; col++) {
								input_data_temp[row][col] = input_data[row][col];
							}
							input_data_temp[row][input_colCount_temp - 1] = true;
						}
						
						// apply the modification
						input_rowCount = input_rowCount_temp;
						input_colCount = input_colCount_temp;
						input_columnNames = new String[input_colCount];
						input_columnNames = input_columnNames_temp;
						input_data = new Object[input_rowCount][input_colCount];
						input_data = input_data_temp;
					}
				}
				// The below code is for adding column "lr_std" and "cr_std" as the first 2 columns of the old runs so the old runs have new format ("input_06_sr_disturbances.txt") (we could and should delete all the below in the future)
				// -----------------------------------------------------------------------------
				// -----------------------------------------------------------------------------
				if (table_file.getName().equals("input_06_sr_disturbances.txt") && !input_columnNames[2].equals("lr_mean")) {
					// Modify old input by adding 2 columns
					int input_rowCount_temp = input_rowCount;
					int input_colCount_temp = input_colCount + 2;
					
					// old:  columnNames6 = new String[] {"condition_id", "condition_description", "probability_info", "regeneration_info", "static_identifiers", "dynamic_identifiers", "original_dynamic_identifiers", "model_condition"};
					// new:  probability_info = loss_pr_mean	regeneration_info = cr_mean
					String[] input_columnNames_temp = new String[] {"condition_id", "condition_description", "lr_mean", "lr_std", "cr_mean", "cr_std", "static_identifiers", "dynamic_identifiers", "original_dynamic_identifiers", "model_condition"};
										
					Object[][] input_data_temp = new Object[input_rowCount_temp][input_colCount_temp];
					for (int row = 0; row < input_rowCount_temp; row++) {
						for (int col = 0; col <= 2; col++) {
							input_data_temp[row][col] = input_data[row][col];
						}
						input_data_temp[row][3] = input_data_temp[row][2];	// set lr_std = lr_mean
						input_data_temp[row][4] = input_data[row][3];		// cr_mean
						input_data_temp[row][5] = input_data_temp[row][4];	// set cr_std = cr_mean
						
						
						// read regeneration_info
						String[] array = input_data[row][3].toString().split(";");		// example 2 disturbance types:       B B 0.0 0.0;B C 0.0 0.0
						int total_SRs = array[0].split("\\s+").length - 2;
						int total_covertype = (int) Math.sqrt(array.length);
						double[][][] regeneration_info = new double[total_SRs][total_covertype][total_covertype];
						for (int i = 0; i < total_covertype; i++) {
							for (int j = 0; j < total_covertype; j++) {
								int current_array_position = total_covertype * i + j;
								String[] info = array[current_array_position].split("\\s+");    // example:       B B 0.0 0.0
								for (int k = 2; k < info.length; k++) {
									regeneration_info[k - 2][i][j] = Double.parseDouble(info[k]);
								}
							}
						}
						
						// make it lr_mean
						String[] lr_mean_array = new String[total_covertype];
						array = input_data_temp[row][2].toString().split(";");		// example 2 disturbance types:       B All 0.0 0.0;C All 0.0 0.0
						for (int i = 0; i < total_covertype; i++) {
							String[] info = array[i].split("\\s+");    // example:       B All 0.0 0.0
							for (int k = 2; k < info.length; k++) {
								double total_percentage = 0;
								for (int j = 0; j < total_covertype; j++) {
									total_percentage = total_percentage + regeneration_info[k - 2][i][j];
								}
								info[k] = String.valueOf(total_percentage);
							}
							lr_mean_array[i] = String.join(" ", info);
						}
						input_data_temp[row][2] = String.join(";", lr_mean_array);
						
						// make lr_std = 20% lr_mean
						String[] lr_std_array = new String[total_covertype];
						array = input_data_temp[row][2].toString().split(";");		// example 2 disturbance types:       B All 0.0 0.0;C All 0.0 0.0
						for (int i = 0; i < total_covertype; i++) {
							String[] info = array[i].split("\\s+");    // example:       B All 0.0 0.0
							for (int k = 2; k < info.length; k++) {
								double total_percentage = 0;
								for (int j = 0; j < total_covertype; j++) {
									total_percentage = total_percentage + regeneration_info[k - 2][i][j];
								}
								info[k] = String.valueOf(total_percentage * 20 / 100);
							}
							lr_std_array[i] = String.join(" ", info);
						}
						input_data_temp[row][3] = String.join(";", lr_std_array);
						
						// adjust cr_mean
						int square_total_covertype = total_covertype * total_covertype;
						String[] cr_mean_array = new String[square_total_covertype];
						array = input_data_temp[row][4].toString().split(";");		// example 2 disturbance types:       B B 0.0 0.0;B C 0.0 0.0
						for (int i = 0; i < square_total_covertype; i++) {
							String[] info = array[i].split("\\s+");    // example:       B All 0.0 0.0
							for (int k = 2; k < info.length; k++) {
								double total_percentage = 0;
								for (int j = 0; j < total_covertype; j++) {
									total_percentage = total_percentage + regeneration_info[k - 2][(int) i / total_covertype][j];
								}
								double new_rate = 0;
								if (Double.valueOf(info[k]) != 0) new_rate = Double.valueOf(info[k]) / total_percentage * 100;
								info[k] = String.valueOf(new_rate);
							}
							cr_mean_array[i] = String.join(" ", info);
						}
						input_data_temp[row][4] = String.join(";", cr_mean_array);
						
						// let cr_std = 20% cr_mean
						String[] cr_std_array = new String[square_total_covertype];
						array = input_data_temp[row][5].toString().split(";");		// example 2 disturbance types:       B B 0.0 0.0;B C 0.0 0.0
						for (int i = 0; i < square_total_covertype; i++) {
							String[] info = array[i].split("\\s+");    // example:       B All 0.0 0.0
							for (int k = 2; k < info.length; k++) {
								double total_percentage = 0;
								for (int j = 0; j < total_covertype; j++) {
									total_percentage = total_percentage + regeneration_info[k - 2][(int) i / total_covertype][j];
								}
								double new_rate = 0;
								if (Double.valueOf(info[k]) != 0) new_rate = Double.valueOf(info[k]) / total_percentage * 100 * 20 / 100;
								info[k] = String.valueOf(new_rate);
							}
							cr_std_array[i] = String.join(" ", info);
						}
						input_data_temp[row][5] = String.join(";", cr_std_array);

						
						for (int col = 6; col <= 9; col++) {
							input_data_temp[row][col] = input_data[row][col - 2];
						}
					}
					
					// apply the modification
					input_rowCount = input_rowCount_temp;
					input_colCount = input_colCount_temp;
					input_columnNames = new String[input_colCount];
					input_columnNames = input_columnNames_temp;
					input_data = new Object[input_rowCount][input_colCount];
					input_data = input_data_temp;
				}
				// -----------------------------------------------------------------------------
				// -------------------End of where we could delete----------------------------
				// -----------------------------------------------------------------------------
			} catch (IOException e1) {
				System.err.println(e1.getClass().getName() + ": " + e1.getMessage() + " - Cannot load. New interface is created");
			}
		}

		private int get_colCount() {
			return input_colCount;
		}
		
		private int get_rowCount() {
			return input_rowCount;
		}
		
		private Object[][] get_data() {
			return input_data;
		}
		
		private String[] get_columnNames() {
			return input_columnNames;
		}
	}
	
    //--------------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------  
    //--------------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table_overview() {
		//Setup the table--------------------------------------------------------------------------------
		rowCount_overview = 5;
		colCount_overview = 2;
		data_overview = new Object[rowCount_overview][colCount_overview];
        columnNames_overview= new String[] {"Description" , "Value"};
		
		// Populate the data matrix
		data_overview[0][0] = "existing strata   --o--   existing acres";
		data_overview[1][0] = "model strata   --o--   model acres";
		data_overview[2][0] = "highlighted strata   --o--   highlighted acres";
		data_overview[3][0] = "prescriptions in your database";
		data_overview[4][0] = "existing strata without NG_E_0 prescriptions";
		
		
		
		//Create a table
        model_overview = new PrismTableModel(rowCount_overview, colCount_overview, data_overview, columnNames_overview);
        table_overview = new JTable(model_overview) {
        	@Override
			protected void paintComponent(Graphics g) {					
				Graphics2D g2d = (Graphics2D) g.create();
				// Fill the background, this is VERY important. Fail to do this and you will have major problems
				g2d.setColor(getBackground());
				g2d.fillRect(0, 0, getWidth(), getHeight());
				// Draw the background
				ImageIcon bgImage = IconHandle.get_scaledImageIcon(70, 70, "icon_tree.png");
				Dimension size = this.getSize();
				g2d.drawImage(bgImage.getImage(), size.width - bgImage.getIconWidth(), size.height - bgImage.getIconHeight(), this);
				// Paint the component content, i.e. the text
				getUI().paint(g2d, this);
				g2d.dispose();
			}
        };
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)table_overview.getDefaultRenderer(Object.class);
        renderer.setHorizontalAlignment(SwingConstants.LEFT);		// Set alignment of values in the table to the left side
//      table_overview.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table_overview.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table_overview.getTableHeader().setReorderingAllowed(false);		//Disable columns move
        table_overview.getColumnModel().getColumn(0).setPreferredWidth(200);	//Set width of 1st Column bigger
        table_overview.setTableHeader(null);
        table_overview.setPreferredScrollableViewportSize(new Dimension(350, 120));
        table_overview.setFillsViewportHeight(true);
	}
    
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table1() {	
		//Setup the table------------------------------------------------------------	
		if (is_table1_loaded == false) { // Create a fresh new if Load fail
			rowCount1 = 7;
			colCount1 = 2;
			data1 = new Object[rowCount1][colCount1];
			columnNames1 = new String[] { "description", "selection" };
			
			// Populate the data matrix
			data1[0][0] = "Total planning periods (decades)";	
			data1[1][0] = "Total replacing disturbances";
			data1[2][0] = "Annual discount rate (%)";
			data1[3][0] = "Solver for optimization";
			data1[4][0] = "Maximum solving time (minutes)";
			data1[5][0] = "Export original problem file";
			data1[6][0] = "Export original solution file";
			
			data1[0][1] = "5";	
			data1[1][1] = "1";
			data1[2][1] = "0";
			data1[3][1] = "CPLEX";
			data1[4][1] = "6000";
			data1[5][1] = "false";
			data1[6][1] = "false";
		}
		
			
		//Create a table-------------------------------------------------------------
        model1 = new PrismTableModel(rowCount1, colCount1, data1, columnNames1);
        table1 = new JTable(model1);
		
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)table1.getDefaultRenderer(Object.class);
        renderer.setHorizontalAlignment(SwingConstants.LEFT);		// Set alignment of values in the table to the left side
		table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table1.getTableHeader().setReorderingAllowed(false);		//Disable columns move     
		table1.getColumnModel().getColumn(0).setPreferredWidth(250);	//Set width of 1st Column bigger
		table1.getColumnModel().getColumn(1).setPreferredWidth(100);	//Set width of 2nd Column bigger
//		table1.setTableHeader(null);
		table1.setPreferredScrollableViewportSize(new Dimension(400, 100));
//		table1.setFillsViewportHeight(true);
	}
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table2() {		
		//Setup the table------------------------------------------------------------
		if (is_table2_loaded == false) { // Create a fresh new if Load fail				
			rowCount2 = 0;
			colCount2 = 5;
			data2 = new Object[rowCount2][colCount2];
			columnNames2 = new String[] {"condition_id", "condition_description", "static_identifiers", "method_choice", "model_condition"};
		}
					
		
		//Create a table-------------------------------------------------------------		
		model2 = new PrismTableModel(rowCount2, colCount2, data2, columnNames2) {
			@Override
			public Class getColumnClass(int c) {
				if (c == 0) return Integer.class; // column 0 accepts only Integer
				else if (c == 4) return Boolean.class;
				else return String.class;
			}
			
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col == 1 || col == 4) { 	// Only the 2nd column is editable
					return true;
				} else {
					return false;
				}
			}
			
			@Override
			public void setValueAt(Object value, int row, int col) {
				data2[row][col] = value;
			}
			
			@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount2; row++) {
					for (int col = 0; col < colCount2; col++) {
						if (String.valueOf(data2[row][col]).equals("null")) {
							data2[row][col] = null;
						} else {					
							if (col == 0) {		// column 0 is Integer
								try {
									data2[row][col] = Integer.valueOf(String.valueOf(data2[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Integer values in create_table2");
								}
							} else if (col == 4) {	// column 4 is Boolean
								try {
									data2[row][col] = Boolean.valueOf(String.valueOf(data2[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Boolean values in create_table2");
								}
							} else {	//All other columns are String
								data2[row][col] = String.valueOf(data2[row][col]);
							}
						}	
					}	
				}	
			}
		};
		
		
		
		table2 = new JTable(model2) {
			@Override			//These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table2.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table2,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				if (column != 1) {
					tableColumn.setPreferredWidth(maxWidth);
				} else {
					tableColumn.setMinWidth(500);
				}
				return component;
			}	
			
			@Override	// Implement table cell tool tips           
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int row = rowAtPoint(p);
				int col = columnAtPoint(p);
				String tip = (table2.getColumnName(col).equals("condition_description") && row >= 0 && getValueAt(row, col) != null) ? getValueAt(row, col).toString() : null;
				return tip;
			}	
		};

		((JComponent) table2.getDefaultRenderer(Boolean.class)).setOpaque(true);	// It's a bug in the synth-installed renderer, quick hack is to force the rendering checkbox opacity to true
		((AbstractButton) table2.getDefaultRenderer(Boolean.class)).setSelectedIcon(IconHandle.get_scaledImageIcon(12, 12, "icon_check.png"));
//		((AbstractButton) table3.getDefaultRenderer(Boolean.class)).setIcon(IconHandle.get_scaledImageIcon(15, 15, "icon_whitebox.png"));
		
		
		// 
		TableColumnsHandle table_handle = new TableColumnsHandle(table2);
		table_handle.setColumnVisible("static_identifiers", false);
		table_handle.setColumnVisible("method_choice", false);

		table2.setAutoResizeMode(0);		// 0 = JTable.AUTO_RESIZE_OFF
		table2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);  
		table2.getTableHeader().setReorderingAllowed(false);		//Disable columns move
		table2.setPreferredScrollableViewportSize(new Dimension(250, 20));
	}		
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table3() {
		//Setup the table------------------------------------------------------------	
		if (is_table3_loaded == false) { // create a fresh new if Load fail		
			List<String> layers_title = read_database.get_layers_title();
			rowCount3 = 0;
			colCount3 = layers_title.size() + 4;
			columnNames3 = new String[colCount3];
			columnNames3[0] = "strata_id";		// add for the name of strata
			for (int i = 0; i < layers_title.size(); i++) {
				columnNames3[i + 1] = layers_title.get(i);	// add 6 layers to the column header name
			}
			columnNames3[colCount3 - 3] = "area";	// add 3 more columns
			columnNames3[colCount3 - 2] = "age_class";
			columnNames3[colCount3 - 1] = "model_strata";	
			
			// get the raw existing_strata from the database------------------------------------------------------
			String[][] existing_strata_values = read_database.get_existing_strata_values();
			rowCount3 = existing_strata_values.length;	// refresh total rows based on existing strata, we don't need to refresh the total columns
			int existing_strata_colCount = existing_strata_values[0].length;

			data3 = new Object[rowCount3][colCount3];
			for (int row = 0; row < rowCount3; row++) {
				for (int column = 0; column < existing_strata_colCount; column++) {		// loop all existing strata columns (strata_id, layer 1 ... 6, acres). This do ntot have the last 2 columns as seen in the GUI (ageclass & model_strata)
					data3[row][column] = existing_strata_values[row][column];
				}
				data3[row][0] = String.join("_", 
						existing_strata_values[row][1], existing_strata_values[row][2], existing_strata_values[row][3],
						existing_strata_values[row][4], existing_strata_values[row][5], existing_strata_values[row][6]);	// ignore the strata_id column and re-create it
			}					
			
			// update ""age_class" column
			for (int row = 0; row < rowCount3; row++) {						
				String s5 = data3[row][5].toString();
				String s6 = data3[row][6].toString();
				if (read_database.get_starting_ageclass(s5, s6, "NG", "0") != null) {
					data3[row][colCount3 - 2] = Integer.valueOf(read_database.get_starting_ageclass(s5, s6, "A", "0"));	
				}												
			}

			for (int row = 0; row < rowCount3; row++) {
				data3[row][colCount3 - 1] = (data3[row][colCount3 - 2] != null) ? true : false;		// select all strata as model_strata if age class is  found
			}
		}		
					
		
		//Create a table-------------------------------------------------------------
		model3 = new PrismTableModel(rowCount3, colCount3, data3, columnNames3) {
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col < colCount3 - 1) { // Only the last column is editable
					return false;
				} else {
					return true;
				}
			}
			
			@Override
			public void setValueAt(Object value, int row, int col) {
				data3[row][col] = value;
				update_model_overview();
			}
			
			@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount3; row++) {
					for (int col = 0; col < colCount3; col++) {
						if (String.valueOf(data3[row][col]).equals("null")) {
							data3[row][col] = null;
						} else {					
							if (col == colCount3 - 3) {			//column "Total Acres" accepts only Double
								try {
									data3[row][col] = Double.valueOf(String.valueOf(data3[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table3");
								}	
							} else if (col == colCount3 - 2) {			//column "Age Class" accepts only Integer
								try {
									data3[row][col] = Integer.valueOf(String.valueOf(data3[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Integer values in create_table3");
								}	
							} else if (col == colCount3 - 1) {			//last column "model_strata" accepts only Boolean
								try {
									data3[row][col] = Boolean.valueOf(String.valueOf(data3[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Boolean values in create_table3");
								}	
							} else {	//All other columns are String
								data3[row][col] = String.valueOf(data3[row][col]);
							}
						}	
					}	
				}
				model3.update_model_overview();		// this is just to trigger the update_model_overview
			}
			
			public void update_model_overview() {  
				// de-select all model_strata that has not found starting age-class
				for (int row = 0; row < rowCount3; row++) {
					if (data3[row][colCount3 - 2] == null) {
						data3[row][colCount3 - 1] = false;
					}
				}
				
				// Update Model OverView table
				int modeledStrata = 0;
				for (int row = 0; row < rowCount3; row++) {
					if (data3[row][colCount3 - 1] != null && (boolean) data3[row][colCount3 - 1] == true)	modeledStrata = modeledStrata + 1;
				}
				
				
				modeledAcres = 0;
				availableAcres = 0;
				for (int row = 0; row < rowCount3; row++) {
					if (data3[row][colCount3 - 1] != null && (boolean) data3[row][colCount3 - 1] == true)	modeledAcres = modeledAcres + Double.parseDouble(data3[row][colCount3 - 3].toString());
					availableAcres = availableAcres + Double.parseDouble(data3[row][colCount3 - 3].toString());
				}
				
				
				int total_strata_without_NG_E_0_prescription = 0;
		        for (int row = 0; row < rowCount3; row++) {				        	
		        	if (data3[row][colCount3 - 2] == null) {
		        		total_strata_without_NG_E_0_prescription = total_strata_without_NG_E_0_prescription + 1;
		        	}
				}
		        
		        
		        DecimalFormat formatter = new DecimalFormat("###,###.###");
				formatter.setMinimumFractionDigits(0);
		        data_overview[0][1] = rowCount3 + "   --o--   " + formatter.format((Number) availableAcres);
				data_overview[1][1] = modeledStrata + "   --o--   " + formatter.format((Number) modeledAcres);
		        data_overview[3][1] = read_database.get_yield_tables_values().length;
		        data_overview[4][1] = total_strata_without_NG_E_0_prescription;
				model_overview.fireTableDataChanged();
			}
		};

		
		table3 = new JTable(model3) {
		};

		
		((JComponent) table3.getDefaultRenderer(Boolean.class)).setOpaque(true);	// It's a bug in the synth-installed renderer, quick hack is to force the rendering checkbox opacity to true
		((AbstractButton) table3.getDefaultRenderer(Boolean.class)).setSelectedIcon(IconHandle.get_scaledImageIcon(12, 12, "icon_check.png"));
//		((AbstractButton) table3.getDefaultRenderer(Boolean.class)).setIcon(IconHandle.get_scaledImageIcon(15, 15, "icon_whitebox.png"));
		
		
//		table3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table3.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);	  
		table3.getTableHeader().setReorderingAllowed(false);		//Disable columns move
//		table3.createDefaultColumnsFromModel(); // Very important code to refresh the number of Columns	shown
        table3.getColumnModel().getColumn(colCount3 - 1).setPreferredWidth(100);	//Set width of Column "Strata in optimization model" bigger
		table3.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table3.setFillsViewportHeight(true);
		TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>(model3);
		table3.setRowSorter(sorter);
	}	
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table4() {		
		//Setup the table------------------------------------------------------------	
		if (is_table4_loaded == false) { // Create a fresh new if Load fail				
			rowCount4 = 0;
			colCount4 = 5;
			data4 = new Object[rowCount4][colCount4];
			columnNames4 = new String[] {"condition_id", "condition_description", "static_identifiers", "conversion_and_rotation", "model_condition"};
		}
					
		
		//Create a table-------------------------------------------------------------		
		model4 = new PrismTableModel(rowCount4, colCount4, data4, columnNames4) {
			@Override
			public Class getColumnClass(int c) {
				if (c == 0) return Integer.class;	// column 0 accepts only Integer
				else if (c ==  4) return Boolean.class;
				else return String.class;				
			}
			
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col == 1 || col == 4) { 	// the 2nd column and the 5th columns are editable
					return true;
				} else {
					return false;
				}
			}
			
			@Override
			public void setValueAt(Object value, int row, int col) {
				data4[row][col] = value;
			}
			
			@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount4; row++) {
					for (int col = 0; col < colCount4; col++) {
						if (String.valueOf(data4[row][col]).equals("null")) {
							data4[row][col] = null;
						} else {
							if (col == 0) {		// column 0 is Integer
								try {
									data4[row][col] = Integer.valueOf(String.valueOf(data4[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Integer values in create_table4");
								}	
							} else if (col == 4){
								try {
									data4[row][col] = Boolean.valueOf(String.valueOf(data4[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Boolean values in create_table4");
								}

							} else
							 {	//All other columns are String
								data4[row][col] = String.valueOf(data4[row][col]);
							}
						}	
					}	
				}	
			}
		};
		
		
		
		table4 = new JTable(model4) {
			@Override			//These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table4.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table4,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				if (column != 1) {
					tableColumn.setPreferredWidth(maxWidth);
				} else {
					tableColumn.setMinWidth(200);
				}
				return component;
			}	
			
			@Override	// Implement table cell tool tips           
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int row = rowAtPoint(p);
				int col = columnAtPoint(p);
				String tip = (table4.getColumnName(col).equals("condition_description") && row >= 0 && getValueAt(row, col) != null) ? getValueAt(row, col).toString() : null;
				return tip;
			}	
		};

		((JComponent) table4.getDefaultRenderer(Boolean.class)).setOpaque(true);	// It's a bug in the synth-installed renderer, quick hack is to force the rendering checkbox opacity to true
		((AbstractButton) table4.getDefaultRenderer(Boolean.class)).setSelectedIcon(IconHandle.get_scaledImageIcon(12, 12, "icon_check.png"));


		// Hide columns
		TableColumnsHandle table_handle = new TableColumnsHandle(table4);
		table_handle.setColumnVisible("static_identifiers", false);
		table_handle.setColumnVisible("conversion_and_rotation", false);

		table4.setAutoResizeMode(0);		// 0 = JTable.AUTO_RESIZE_OFF
		table4.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);  
		table4.getTableHeader().setReorderingAllowed(false);		//Disable columns move
		table4.setPreferredScrollableViewportSize(new Dimension(250, 20));
	}	
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table4a() {
		ArrayList<String>[] rotation_ranges = read_database.get_rotation_ranges();
		List<List<String>> all_layers = read_database.get_all_layers();
		List<List<String>> all_layers_tooltips = read_database.get_all_layers_tooltips();
		int total_CoverType = all_layers.get(4).size();		// total number of elements - 1 in layer5 Cover Type (0 to...)
	
		//Setup the table------------------------------------------------------------	
		if (is_table4a_loaded == false) { // Create a fresh new if Load fail				
			rowCount4a = total_CoverType * total_CoverType;
			colCount4a = 7;
			data4a = new Object[rowCount4a][colCount4a];
	        columnNames4a = new String[] {"layer5", "layer5_regen", "EA_E_min_ra", "EA_E_max_ra", "EA_R_min_ra", "EA_R_max_ra", "implementation"};
			
			// Populate the data matrix
	        int table_row = 0;
			for (int i = 0; i < total_CoverType; i++) {
				for (int j = 0; j < total_CoverType; j++) {
					data4a[table_row][0] = all_layers.get(4).get(i);
					data4a[table_row][1] = all_layers.get(4).get(j);	
					
					String covertype = all_layers.get(4).get(i);
					int min_age_cut_existing = (rotation_ranges[0].indexOf(covertype) >= 0 && rotation_ranges[1].get(rotation_ranges[0].indexOf(covertype)) != null) ? Integer.valueOf(rotation_ranges[1].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    int max_age_cut_existing = (rotation_ranges[0].indexOf(covertype) >= 0 && rotation_ranges[2].get(rotation_ranges[0].indexOf(covertype)) != null) ? Integer.valueOf(rotation_ranges[2].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    int min_age_cut_regeneration = (rotation_ranges[0].indexOf(covertype) >= 0 && rotation_ranges[3].get(rotation_ranges[0].indexOf(covertype)) != null) ? Integer.valueOf(rotation_ranges[3].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    int max_age_cut_regeneration = (rotation_ranges[0].indexOf(covertype) >= 0 && rotation_ranges[4].get(rotation_ranges[0].indexOf(covertype)) != null) ? Integer.valueOf(rotation_ranges[4].get(rotation_ranges[0].indexOf(covertype))) : -9999;
					
					data4a[table_row][2] = min_age_cut_existing;
					data4a[table_row][3] = max_age_cut_existing;
					data4a[table_row][4] = min_age_cut_regeneration;
					data4a[table_row][5] = max_age_cut_regeneration;
					data4a[table_row][6] = (i==j) ? true : false; 
					table_row++;
				}
			}
		}
		
		
        // header tool-tip
		String[] headerToolTips = new String[colCount4a];
		headerToolTips[0] = "cover type (not dynamic) before clear-cut";
        headerToolTips[1] = "cover type after clear-cut";
        headerToolTips[2] = "minimum age class for clear-cutting existing forest";
        headerToolTips[3] = "maximum age class for clear-cutting existing forest";
        headerToolTips[4] = "minimum age class for clear-cutting regenerated forest";
        headerToolTips[5] = "maximum age class for clear-cutting regenerated forest";
        headerToolTips[6] = "unchecked options will be ignored";
			
		
		//Create a table-------------------------------------------------------------
		model4a = new PrismTableModel(rowCount4a, colCount4a, data4a, columnNames4a) {			
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col < 2) { // Only the last 5 columns are editable
					return false;
				} else {
					return true;
				}
			}

			@Override
			public void setValueAt(Object value, int row, int col) {
				data4a[row][col] = value;
				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
				if (table4a.getSelectedRows().length == 1) {	// only fire the change when the table has only one row selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
					int currentRow = table4a.getSelectedRow();		        	
					fireTableDataChanged();		// This will clear the selection 
					table4a.setRowSelectionInterval(currentRow, currentRow);	// This will add the selection back
				}
			}
			
			@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount4a; row++) {
					for (int col = 0; col < colCount4a; col++) {
						if (String.valueOf(data4a[row][col]).equals("null")) {
							data4a[row][col] = null;
						} else {					
							if (col >= 2 && col <= 5) {			//Column 2 to 5 are Integer
								try {
									data4a[row][col] = Integer.valueOf(String.valueOf(data4a[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Integer values in create_table4a");
								}	
							} else if (col == 6) {			//column "implementation" accepts only Boolean
								try {
									data4a[row][col] = Boolean.valueOf(String.valueOf(data4a[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Boolean values in create_table4a");
								}							
							} else {	//All other columns are String
								data4a[row][col] = String.valueOf(data4a[row][col]);
							}
						}	
					}	
				}	
			}
		};
		
		
		
		table4a = new JTable(model4a) {
			@Override			//These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table4a.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table4a,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width + 2);
				
				tableColumn.setPreferredWidth(maxWidth);
				return component;
			}	
			
			@Override		// implement table header tool tips
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
						if (columnModel.getColumnIndexAtX(p.x) >= 0) {
                        	int index = columnModel.getColumnIndexAtX(p.x);
                            int realIndex = columnModel.getColumn(index).getModelIndex();
                            tip = headerToolTips[realIndex];
                        }
                        return tip;
                    }
                };
            }
			
			// Implement table cell tool tips
			public String getToolTipText(MouseEvent e) {
				String tip = null;
				java.awt.Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				int colIndex = columnAtPoint(p);
				if (colIndex < 2) {
					try {
						tip = getValueAt(rowIndex, colIndex).toString();
						for (int i = 0; i < total_CoverType; i++) {
							if (tip.equals(all_layers.get(4).get(i))) tip = all_layers_tooltips.get(4).get(i);						
						}
					} catch (RuntimeException e1) {
						System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
					}
				}
				return tip;
			}
		};
		
		// Define a set of icon for some columns
		ImageIcon[] imageIconArray = new ImageIcon[colCount4a];
		for (int i = 0; i < colCount4a; i++) {
			if (i >= 2) {
				imageIconArray[i] = IconHandle.get_scaledImageIcon(3, 3, "icon_main.png");
			}
		}
		
		
		// Define a set of background color for all rows
		Color[] rowColor = new Color[rowCount4a];
		Color color1 = new Color(160, 160, 160);
		Color color2 = new Color(192, 192, 192);
		Color currentColor = color2;
		int rCount = 0;

		for (int i = 0; i < total_CoverType; i++) {
			if (currentColor == color2) {
				currentColor = color1;
			} else {
				currentColor = color2;
			}
			for (int j = 0; j < total_CoverType; j++) {
				rowColor[rCount] = currentColor;
				rCount++;
			}
		}

		//Set Color and Alignment for Cells
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// setForeground(Color.RED);
				setHorizontalAlignment(JLabel.LEFT);
				// setFont(getFont().deriveFont(Font.BOLD));
                	
				setBackground(rowColor[row]);		//Set cell background color	
				if (isSelected) {
					setBackground(table.getSelectionBackground());		//Set background color	for selected row
				}
				setIcon(imageIconArray[column]);	// Set icons for cells in some columns
				setIconTextGap(15);		// Set the distance between icon and the actual data value
                return this;
            }
        };
		for (int i = 0; i < 2; i++) {	// first 2 columns only
			table4a.getColumnModel().getColumn(i).setCellRenderer(r);
		}		
				
		// Show cells will value of -9999 as blank cells  
		DefaultTableCellRenderer shown_as_blank_render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (Double.valueOf(String.valueOf(value)) == -9999){
		            super.setValue(null);        
		        }
                return this;
            }
        };
        for (int i = 2; i < colCount4a - 1; i++) {	// except first 2 columns & last column
			table4a.getColumnModel().getColumn(i).setCellRenderer(shown_as_blank_render);
		}
		
		

//		// Set up Icon for column headers
//		class JComponentTableCellRenderer implements TableCellRenderer {
//			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//					boolean hasFocus, int row, int column) {
//				return (JComponent) value;
//			}
//		}
//        
//		TableCellRenderer r2 = new JComponentTableCellRenderer();
//       
//		for (int i = 0; i < columnNames4a.length; i++) {			
//			if (i == 2 || i == 3 || i == 4) {
//				table4a.getColumnModel().getColumn(i).setHeaderRenderer(r2);
//				table4a.getColumnModel().getColumn(i).setHeaderValue(new JLabel(columnNames4a[i], icon_scale, JLabel.CENTER));
//			} 
//		}	

		// Set up Types for each  Columns-------------------------------------------------------------------------------
		class CustomComboBoxEditor extends DefaultCellEditor {
			private DefaultComboBoxModel model;

			public CustomComboBoxEditor() {
				super(new JComboBox());
				this.model = (DefaultComboBoxModel) ((JComboBox) getComponent()).getModel();
			}

			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			    String covertype = table.getValueAt(row, 0).toString();
				int min_age_cut_existing = (rotation_ranges[0].indexOf(covertype) >= 0 && rotation_ranges[1].get(rotation_ranges[0].indexOf(covertype)) != null) ? Integer.valueOf(rotation_ranges[1].get(rotation_ranges[0].indexOf(covertype))) : -9999;
			    int max_age_cut_existing = (rotation_ranges[0].indexOf(covertype) >= 0 && rotation_ranges[2].get(rotation_ranges[0].indexOf(covertype)) != null) ? Integer.valueOf(rotation_ranges[2].get(rotation_ranges[0].indexOf(covertype))) : -9999;
			    int min_age_cut_regeneration = (rotation_ranges[0].indexOf(covertype) >= 0 && rotation_ranges[3].get(rotation_ranges[0].indexOf(covertype)) != null) ? Integer.valueOf(rotation_ranges[3].get(rotation_ranges[0].indexOf(covertype))) : -9999;
			    int max_age_cut_regeneration = (rotation_ranges[0].indexOf(covertype) >= 0 && rotation_ranges[4].get(rotation_ranges[0].indexOf(covertype)) != null) ? Integer.valueOf(rotation_ranges[4].get(rotation_ranges[0].indexOf(covertype))) : -9999;
			    
			    model.removeAllElements();
				if (column == 2 || column == 3) {
					if (rotation_ranges[0].contains(covertype)) {
						for (int i = min_age_cut_existing; i <= max_age_cut_existing; i++) {		
							model.addElement(i);
						}
					} else {
						model.addElement((int) -9999);
					}
				} else if (column == 4 || column == 5) {
					if (rotation_ranges[0].contains(covertype)) {
						for (int i = min_age_cut_regeneration; i <= max_age_cut_regeneration; i++) {		
							model.addElement(i);
						}
					} else {
						model.addElement((int) -9999);
					}
				}
			      
				return super.getTableCellEditorComponent(table, value, isSelected, row, column);
			}
		}
		
//		// Save to work on these code later which I want to show the -9999 value as blank in the combo box dropdown
//		class ItemRenderer extends BasicComboBoxRenderer {
//			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//				if (Double.valueOf(String.valueOf(value)) == -9999) {
//					setText(null);
//				}
//				return this;
//			}
//		}
		
		table4a.getColumnModel().getColumn(2).setCellEditor(new CustomComboBoxEditor());
		table4a.getColumnModel().getColumn(3).setCellEditor(new CustomComboBoxEditor());
		table4a.getColumnModel().getColumn(4).setCellEditor(new CustomComboBoxEditor());
		table4a.getColumnModel().getColumn(5).setCellEditor(new CustomComboBoxEditor());
		((JComponent) table4a.getDefaultRenderer(Boolean.class)).setOpaque(true);	// It's a bug in the synth-installed renderer, quick hack is to force the rendering checkbox opacity to true		
		// End of Set up Types for each  Columns------------------------------------------------------------------------

//		table4a.setAutoResizeMode(0);		// 0 = JTable.AUTO_RESIZE_OFF
		table4a.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table4a.getTableHeader().setReorderingAllowed(false);		//Disable columns move
        table4a.setPreferredScrollableViewportSize(new Dimension(400, 100));
//      table4a.setFillsViewportHeight(true);
        TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>(model4a);	//Add sorter
		for (int i = 1; i < colCount4a; i++) {
			sorter.setSortable(i, false);
			if (i == 0 || i == 1) {			//Only the first 2 columns can be sorted
				sorter.setSortable(i, true);	
			}
		}
		table4a.setRowSorter(sorter);
        
	}
		
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table5() {
		//Setup the table------------------------------------------------------------	
		if (is_table5_loaded == false) { // Create a fresh new if Load fail				
			rowCount5 = 0;
			colCount5 = 6;
			data5 = new Object[rowCount5][colCount5];
			columnNames5 = new String[] {"condition_id", "condition_description", "static_identifiers", "MS_E_percentage", "BS_E_percentage", "model_condition"};
		}
					
		
		//Create a table-------------------------------------------------------------		
		model5 = new PrismTableModel(rowCount5, colCount5, data5, columnNames5) {
			@Override
			public Class getColumnClass(int c) {
				if (c == 0) return Integer.class;	// column 0 accepts only Integer
				else if (c == 3) return Double.class;
				else if (c == 4) return Double.class;
				else if (c == 5) return Boolean.class;
				else return String.class;				
			}
			
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col != 0 && col != 2) {
					return true;
				} else {
					return false;
				}
			}
			
			@Override
			public void setValueAt(Object value, int row, int col) {
				if (value != null && (col == 3 || col == 4) && (((Number) value).doubleValue() < 0 || ((Number) value).doubleValue() > 100)) {	// allow null to be set
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Only null or double values in the range 0-100 (%) would be allowed.");
    			} else {
    				data5[row][col] = value;
    				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
    				if (table5.getSelectedRows().length == 1) {	// only fire the change when the table has only one cell selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
    					int currentRow = table5.getSelectedRow();		        	
    					int currentCol = table5.getSelectedColumn();	
    					fireTableDataChanged();		// This will clear the selection 
    					table5.changeSelection(currentRow, currentCol, true, false);	// This will add the selection back (reselect previous cell)
    				}
    			}
			}
			
			@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount5; row++) {
					for (int col = 0; col < colCount5; col++) {
						if (String.valueOf(data5[row][col]).equals("null")) {
							data5[row][col] = null;
						} else {					
							if (col == 0) {		// column 0 is Integer
								try {
									data5[row][col] = Integer.valueOf(String.valueOf(data5[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Integer values in create_table5");
								}	
							} else if (col == 3 || col == 4) {	// Double
								try {
									data5[row][col] = Double.valueOf(String.valueOf(data5[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table5");
								}	
							} else if (col == 5) {
								try {
									data5[row][col] = Boolean.valueOf(String.valueOf(data5[row][col]));
								} catch(NumberFormatException e){
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Boolean values in create_table5");
								}
							} else {	//All other columns are String
								data5[row][col] = String.valueOf(data5[row][col]);
							}
						}	
					}	
				}	
			}
		};
		
		
		
		table5 = new JTable(model5) {
			@Override			//These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table5.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table5,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				if (column != 1) {
					tableColumn.setPreferredWidth(maxWidth);
				} else {
					tableColumn.setMinWidth(300);
				}
				
				// Set icon for cells: when total percentage of MS_E and BS_E exceed 100% 		NOTE: we do not need to use getValueAt because we do not allow changing row and column position
				if (column == 2 || column == 3) {	// use 2 and 3 because we hide the "static_identifiers" colum
					double total_percentage = 0;
					if (data5[row][3] != null) total_percentage = total_percentage + Double.parseDouble(data5[row][3].toString());
					if (data5[row][4] != null) total_percentage = total_percentage + Double.parseDouble(data5[row][4].toString());
					if (total_percentage > 100) {		// check if the total_percentage > 100% --> problem icon for this cell
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(14, 14, "icon_problem.png"));
					}
					else {
						((DefaultTableCellRenderer) component).setIcon(null);
					}
				}
				return component;
			}	
			
			@Override	// Implement table cell tool tips           
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int row = rowAtPoint(p);
				int column = columnAtPoint(p);
				String tip = (table5.getColumnName(column).equals("condition_description") && row >= 0 && getValueAt(row, column) != null) ? getValueAt(row, column).toString() : null;
				
				// Show problem tip 		NOTE: we do not need to use getValueAt because we do not allow changing row and column position
				if (column == 2 || column == 3) {	// use 2 and 3 because we hide the "static_identifiers" colum
					double total_percentage = 0;
					if (data5[row][3] != null) total_percentage = total_percentage + Double.parseDouble(data5[row][3].toString());
					if (data5[row][4] != null) total_percentage = total_percentage + Double.parseDouble(data5[row][4].toString());
					if (total_percentage > 100) {		// check if the total_percentage > 100% --> problem icon for this cell
						tip = "INFEASIBLE - The sum of MS_E_percentage and BS_E_percentage must not exceed 100";
					}
				}
				return tip;
			}	
		};
		
		((JComponent) table5.getDefaultRenderer(Boolean.class)).setOpaque(true);	// It's a bug in the synth-installed renderer, quick hack is to force the rendering checkbox opacity to true
		((AbstractButton) table5.getDefaultRenderer(Boolean.class)).setSelectedIcon(IconHandle.get_scaledImageIcon(12, 12, "icon_check.png"));

		
		// Hide columns
		TableColumnsHandle table_handle = new TableColumnsHandle(table5);
		table_handle.setColumnVisible("static_identifiers", false);

		table5.setAutoResizeMode(0);		// 0 = JTable.AUTO_RESIZE_OFF
		table5.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);  
		table5.getTableHeader().setReorderingAllowed(false);		//Disable columns move
		table5.setPreferredScrollableViewportSize(new Dimension(250, 20));
	}
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table6() {
		//Setup the table------------------------------------------------------------	
		if (is_table6_loaded == false) { // Create a fresh new if Load fail				
			rowCount6 = 0;
			colCount6 = 10;
			data6 = new Object[rowCount6][colCount6];
			columnNames6 = new String[] {"condition_id", "condition_description", "lr_mean", "lr_std", "cr_mean", "cr_std", "static_identifiers", "dynamic_identifiers", "original_dynamic_identifiers", "model_condition"};
		}
					
		
		//Create a table-------------------------------------------------------------		
		model6 = new PrismTableModel(rowCount6, colCount6, data6, columnNames6) {
			@Override
			public Class getColumnClass(int c) {
				if (c == 9) return Boolean.class;
				else return String.class;
			}
			
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col == 1 || col == 9) { //  columns "description" and "model_condition" are editable
					return true;
				} else {
					return false;
				}
			}
			
			@Override
			public void setValueAt(Object value, int row, int col) {
				data6[row][col] = value;
			}
			
			@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount6; row++) {
					for (int col = 0; col < colCount6; col++) {
						if (String.valueOf(data6[row][col]).equals("null")) {
							data6[row][col] = null;
						} else {	
							if (col == 9) {
								try {
									data6[row][col] = Boolean.valueOf(String.valueOf(data6[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Boolean values in create_table6");
								}
							} else {
								data6[row][col] = String.valueOf(data6[row][col]);
							}
						}
					}	
				}	
			}
		};
		
		
		
		table6 = new JTable(model6) {
			@Override			//These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table6.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table6,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				if (column != 1) {
					tableColumn.setPreferredWidth(maxWidth);
				} else {
					tableColumn.setMinWidth(200);
				}
				return component;
			}
			
			@Override	// Implement table cell tool tips           
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int row = rowAtPoint(p);
				int col = columnAtPoint(p);
				String tip = (table6.getColumnName(col).equals("condition_description") && row >= 0 && getValueAt(row, col) != null) ? getValueAt(row, col).toString() : null;
				return tip;
			}			
		};
		
		((JComponent) table6.getDefaultRenderer(Boolean.class)).setOpaque(true);
		((AbstractButton) table6.getDefaultRenderer(Boolean.class)).setSelectedIcon(IconHandle.get_scaledImageIcon(12, 12, "icon_check.png"));
		
		
		// Hide columns
		TableColumnsHandle table_handle = new TableColumnsHandle(table6);
		table_handle.setColumnVisible("lr_mean", false);
		table_handle.setColumnVisible("lr_std", false);
		table_handle.setColumnVisible("cr_mean", false);
		table_handle.setColumnVisible("cr_std", false);
		table_handle.setColumnVisible("static_identifiers", false);
		table_handle.setColumnVisible("dynamic_identifiers", false);
		table_handle.setColumnVisible("original_dynamic_identifiers", false);
  
		table6.setAutoResizeMode(0);		// 0 = JTable.AUTO_RESIZE_OFF
		table6.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);  
		table6.getTableHeader().setReorderingAllowed(false);		//Disable columns move
		table6.setPreferredScrollableViewportSize(new Dimension(150, 100));
//		table6.setFillsViewportHeight(true);
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table6a() {
		List<List<String>> all_layers = read_database.get_all_layers();
		List<List<String>> all_layers_tooltips = read_database.get_all_layers_tooltips();
		int total_CoverType = all_layers.get(4).size();	
		
		// Setup the table------------------------------------------------------------	
		if (is_table6a_loaded == false) { // Create a fresh new if Load fail				
			rowCount6a = total_CoverType;
			colCount6a = total_replacing_disturbance + 2;
			data6a = new Object[rowCount6a][colCount6a];
	        columnNames6a = new String[colCount6a];
	        columnNames6a[0] = "layer5";
	        columnNames6a[1] = "layer5_regen";
	        for (int col = 2; col < colCount6a; col++) {
	        	int disturbance_index = col - 1;
	        	if (disturbance_index < 10) columnNames6a[col] = "SR_0" + disturbance_index; else columnNames6a[col] = "SR_" + disturbance_index;
	        }
	        
			// Populate the data matrix
			for (int row = 0; row < rowCount6a; row++) {
				data6a[row][0] = all_layers.get(4).get(row);
				data6a[row][1] = "All";	
				for (int col = 2; col < colCount6a; col++) {
					data6a[row][col] = (double) 0;
				}
			}			
		}
		
		
        // Header tool-tip
		String[] headerToolTips = new String[colCount6a];
		headerToolTips[0] = "layer5 (not dynamic) before the occurrence of stand replacing disturbances";
        headerToolTips[1] = "layer5 regenerated after the occurrence of stand replacing disturbances";
        for (int col = 2; col < colCount6a; col++) {
        	String disturbance_name = columnNames6a[col].replaceAll("probability_", "");
			headerToolTips[col] = "loss rate mean of " + disturbance_name;
        }
        
		
		// Create a table-------------------------------------------------------------		
        model6a = new PrismTableModel(rowCount6a, colCount6a, data6a, columnNames6a) {
        	@Override
    		public boolean isCellEditable(int row, int col) {
    			if (col >= 2) { // Only column >=2 is editable
    				return true;
    			} else {
    				return false;
    			}
    		}

        	@Override
    		public void setValueAt(Object value, int row, int col) {
    			if (/*value != null && */col >= 2 && (((Number) value).doubleValue() < 0 || ((Number) value).doubleValue() > 100)) {		// not allow null to be set
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Only double values in the range 0-100 (%) would be allowed.");
    			} else {
    				data6a[row][col] = value;
    				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
    				if (table6a.getSelectedRows().length == 1) {	// only fire the change when the table has only one cell selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
    					int currentRow = table6a.getSelectedRow();		        	
    					int currentCol = table6a.getSelectedColumn();	
    					fireTableDataChanged();		// This will clear the selection 
    					table6a.changeSelection(currentRow, currentCol, true, false);	// This will add the selection back (reselect previous cell)
    				}
    			}
    		}
        	
        	@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount6a; row++) {
					for (int col = 0; col < colCount6a; col++) {
						if (String.valueOf(data6a[row][col]).equals("null")) {
							data6a[row][col] = null;
						} else {					
							if (col >= 2) {			// Column >=2 are Double
								try {
									data6a[row][col] = Double.valueOf(String.valueOf(data6a[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table6a");
								}	
							} else {	//All other columns are String
								data6a[row][col] = String.valueOf(data6a[row][col]);
							}
						}	
					}	
				}	
			}
        };
        
        
		table6a = new JTable(model6a) {
			@Override			// These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table6a.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table6a,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				tableColumn.setPreferredWidth(maxWidth);
				
				// Set icon for cells: when total percentage of a given block 		NOTE: we need to use getValueAt because of the compact view feature which makes mismatching between full data and displayed data
				double total_percentage = 0;
				// loop all columns & add to total percentage
				for (int j = 1; j < getColumnCount(); j++) {					
					total_percentage = total_percentage + Double.parseDouble(getValueAt(row, j).toString());
				} 
					
				if (total_percentage > 100 && column >= 1) {		// check if the total_percentage > 100% --> problem icon for this cell because it is in the set of cells which make total_percentage > 100%
					((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(14, 14, "icon_problem.png"));
				} else {
					((DefaultTableCellRenderer) component).setIcon(null);
				}
				
				return component;
			}		
			
			@Override		// implement table header tool tips
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
						if (columnModel.getColumnIndexAtX(p.x) >= 0) {
                        	int index = columnModel.getColumnIndexAtX(p.x);
                            int realIndex = columnModel.getColumn(index).getModelIndex();
                            tip = headerToolTips[realIndex];
                        }
                        return tip;
                    }
                };
            }
			
			@Override		// implement table header tool tips         
			public String getToolTipText(MouseEvent e) {
				String tip = null;
				java.awt.Point p = e.getPoint();
				int row = rowAtPoint(p);
				int column = columnAtPoint(p);
				if (table6a.getColumnName(column).equals("layer5") || table6a.getColumnName(column).equals("layer5_regen")) {
					try {
						tip = getValueAt(row, column).toString();
						for (int i = 0; i < total_CoverType; i++) {
							if (tip.equals(all_layers.get(4).get(i))) tip = all_layers_tooltips.get(4).get(i);							
						}
					} catch (RuntimeException e1) {
						System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
					}
				}
				
				if (!table6a.getColumnName(column).equals("layer5") && !table6a.getColumnName(column).equals("layer5_regen")) {
					try {
						DecimalFormat formatter = new DecimalFormat("###,###.###");
						formatter.setMinimumFractionDigits(0);
						formatter.setMaximumFractionDigits(2);
						String percentage = formatter.format((Number) getValueAt(row, column));
						String disturbance_name = table6a.getColumnName(column);
						tip = percentage + "% of the area with cover type = " + getValueAt(row, 0).toString() + " would be destroyed by "  + disturbance_name;
					
						// Show problem tip 		NOTE: we need to use getValueAt because of the compact view feature which makes mismatching between full data and displayed data
						double total_percentage = 0;
						for (int j = 1; j < getColumnCount(); j++) {					
							total_percentage = total_percentage + Double.parseDouble(getValueAt(row, j).toString());
						}
						if (total_percentage > 100 && column >= 1) {		// check if the total_percentage > 100% 
							tip = "INFEASIBLE - The sum of all cells with the same layer5 = " + getValueAt(row, 0).toString() + " must not exceed 100";
						}
					} catch (RuntimeException e1) {
						System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
					}
				}	
				
				return tip;
			}
		};			
        
		
		// Define a set of icon for some columns
		ImageIcon[] imageIconArray = new ImageIcon[colCount6a];
		for (int i = 0; i < colCount6a; i++) {
			if (i == 2) {
				imageIconArray[i] = IconHandle.get_scaledImageIcon(3, 3, "icon_main.png");
			}
		}
		
		
		// Define a set of background color for all rows
		Color[] rowColor = new Color[rowCount6a];
		Color color1 = new Color(160, 160, 160);
		Color color2 = new Color(192, 192, 192);
		Color currentColor = color2;
		int rCount = 0;

		for (int i = 0; i < total_CoverType; i++) {
			if (currentColor == color2) {
				currentColor = color1;
			} else {
				currentColor = color2;
			}
			for (int j = 0; j < 1 /*total_CoverType*/; j++) {
				rowColor[rCount] = currentColor;
				rCount++;
			}
		}
		
				
		// Set Color and Alignment for Cells
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// setForeground(Color.RED);
				setHorizontalAlignment(JLabel.LEFT);
				// setFont(getFont().deriveFont(Font.BOLD));               	
				setBackground(rowColor[row]);		//Set cell background color
				if (isSelected) {
					setBackground(table.getSelectionBackground());		//Set background color	for selected row
				}
				setIcon(imageIconArray[column]);	// Set icons for cells in some columns
				setIconTextGap(15);		// Set the distance between icon and the actual data value
//				setHorizontalAlignment(rowAlignment[row]);			
                return this;
            }
        };						
		
        
        // Set Double precision for cells
        DefaultTableCellRenderer r2 = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {			
				setHorizontalAlignment(JLabel.RIGHT);			
				DecimalFormat formatter = new DecimalFormat("###,###.###");
				formatter.setMinimumFractionDigits(0);
				formatter.setMaximumFractionDigits(10);	// show value with max 10 digits after the dot if it is double value
				if (value instanceof Double) {
					value = formatter.format((Number) value);
				}
				setHorizontalAlignment(JLabel.RIGHT);
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
			
		
		for (int i = 0; i < columnNames6a.length; i++) {
			if (i < 2) {
        		table6a.getColumnModel().getColumn(i).setCellRenderer(r);
        	} else {
        		table6a.getColumnModel().getColumn(i).setCellRenderer(r2);
        	}
		}		
				
		table6a.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table6a.setCellSelectionEnabled(true);
        table6a.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table6a.getTableHeader().setReorderingAllowed(false);		// Disable columns move
        table6a.setPreferredScrollableViewportSize(new Dimension(400, 100));
//      table6a.setFillsViewportHeight(true);
//      TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>(model6a);	//Add sorter
//		for (int i = 1; i < colCount6a; i++) {
//			sorter.setSortable(i, false);
//			if (i == 0) {			// Only the first column can be sorted
//				sorter.setSortable(i, true);	
//			}
//		}
//		table6a.setRowSorter(sorter);
	}	

	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table6b() {
		List<List<String>> all_layers = read_database.get_all_layers();
		List<List<String>> all_layers_tooltips = read_database.get_all_layers_tooltips();
		int total_CoverType = all_layers.get(4).size();	
		
		// Setup the table------------------------------------------------------------	
		if (is_table6b_loaded == false) { // Create a fresh new if Load fail				
			rowCount6b = total_CoverType;
			colCount6b = total_replacing_disturbance + 2;
			data6b = new Object[rowCount6b][colCount6b];
	        columnNames6b = new String[colCount6b];
	        columnNames6b[0] = "layer5";
	        columnNames6b[1] = "layer5_regen";
	        for (int col = 2; col < colCount6b; col++) {
	        	int disturbance_index = col - 1;
	        	if (disturbance_index < 10) columnNames6b[col] = "SR_0" + disturbance_index; else columnNames6b[col] = "SR_" + disturbance_index;
	        }
	        
			// Populate the data matrix
			for (int row = 0; row < rowCount6b; row++) {
				data6b[row][0] = all_layers.get(4).get(row);
				data6b[row][1] = "All";	
				for (int col = 2; col < colCount6b; col++) {
					data6b[row][col] = (double) 0;
				}
			}			
		}
		
		
        // Header tool-tip
		String[] headerToolTips = new String[colCount6b];
		headerToolTips[0] = "layer5 (not dynamic) before the occurrence of stand replacing disturbances";
        headerToolTips[1] = "layer5 regenerated after the occurrence of stand replacing disturbances";
        for (int col = 2; col < colCount6b; col++) {
        	String disturbance_name = columnNames6b[col].replaceAll("probability_", "");
			headerToolTips[col] = "loss rate standard deviation of " + disturbance_name;
        }
        
		
		// Create a table-------------------------------------------------------------		
        model6b = new PrismTableModel(rowCount6b, colCount6b, data6b, columnNames6b) {
        	@Override
    		public boolean isCellEditable(int row, int col) {
    			if (col >= 2) { // Only column >=2 is editable
    				return true;
    			} else {
    				return false;
    			}
    		}

        	@Override
    		public void setValueAt(Object value, int row, int col) {
    			if (/*value != null && */col >= 2 && (((Number) value).doubleValue() < 0 || ((Number) value).doubleValue() > 100)) {		// not allow null to be set
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Only double values in the range 0-100 (%) would be allowed.");
    			} else {
    				data6b[row][col] = value;
    				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
    				if (table6b.getSelectedRows().length == 1) {	// only fire the change when the table has only one cell selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
    					int currentRow = table6b.getSelectedRow();		        	
    					int currentCol = table6b.getSelectedColumn();	
    					fireTableDataChanged();		// This will clear the selection 
    					table6b.changeSelection(currentRow, currentCol, true, false);	// This will add the selection back (reselect previous cell)
    				}
    			}
    		}
        	
        	@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount6b; row++) {
					for (int col = 0; col < colCount6b; col++) {
						if (String.valueOf(data6b[row][col]).equals("null")) {
							data6b[row][col] = null;
						} else {					
							if (col >= 2) {			// Column >=2 are Double
								try {
									data6b[row][col] = Double.valueOf(String.valueOf(data6b[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table6b");
								}	
							} else {	//All other columns are String
								data6b[row][col] = String.valueOf(data6b[row][col]);
							}
						}	
					}	
				}	
			}
        };
        
        
		table6b = new JTable(model6b) {
			@Override			// These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table6b.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table6b,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				tableColumn.setPreferredWidth(maxWidth);
				
				((DefaultTableCellRenderer) component).setIcon(null);
				return component;
			}		
			
			@Override		// implement table header tool tips
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
						if (columnModel.getColumnIndexAtX(p.x) >= 0) {
                        	int index = columnModel.getColumnIndexAtX(p.x);
                            int realIndex = columnModel.getColumn(index).getModelIndex();
                            tip = headerToolTips[realIndex];
                        }
                        return tip;
                    }
                };
            }
			
			@Override		// implement table header tool tips         
			public String getToolTipText(MouseEvent e) {
				String tip = null;
				java.awt.Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				int colIndex = columnAtPoint(p);
				if (table6b.getColumnName(colIndex).equals("layer5") || table6b.getColumnName(colIndex).equals("layer5_regen")) {
					try {
						tip = getValueAt(rowIndex, colIndex).toString();
						for (int i = 0; i < total_CoverType; i++) {
							if (tip.equals(all_layers.get(4).get(i))) tip = all_layers_tooltips.get(4).get(i);							
						}
					} catch (RuntimeException e1) {
						System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
					}
				}
				return tip;
			}
		};			
        
		
		// Define a set of icon for some columns
		ImageIcon[] imageIconArray = new ImageIcon[colCount6b];
		for (int i = 0; i < colCount6b; i++) {
			if (i == 2) {
				imageIconArray[i] = IconHandle.get_scaledImageIcon(3, 3, "icon_main.png");
			}
		}
		
		
		// Define a set of background color for all rows
		Color[] rowColor = new Color[rowCount6b];
		Color color1 = new Color(160, 160, 160);
		Color color2 = new Color(192, 192, 192);
		Color currentColor = color2;
		int rCount = 0;

		for (int i = 0; i < total_CoverType; i++) {
			if (currentColor == color2) {
				currentColor = color1;
			} else {
				currentColor = color2;
			}
			for (int j = 0; j < 1 /*total_CoverType*/; j++) {
				rowColor[rCount] = currentColor;
				rCount++;
			}
		}
		
				
		// Set Color and Alignment for Cells
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// setForeground(Color.RED);
				setHorizontalAlignment(JLabel.LEFT);
				// setFont(getFont().deriveFont(Font.BOLD));               	
				setBackground(rowColor[row]);		//Set cell background color
				if (isSelected) {
					setBackground(table.getSelectionBackground());		//Set background color	for selected row
				}
				setIcon(imageIconArray[column]);	// Set icons for cells in some columns
				setIconTextGap(15);		// Set the distance between icon and the actual data value
//				setHorizontalAlignment(rowAlignment[row]);			
                return this;
            }
        };						
		
        
        // Set Double precision for cells
        DefaultTableCellRenderer r2 = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {			
				setHorizontalAlignment(JLabel.RIGHT);			
				DecimalFormat formatter = new DecimalFormat("###,###.###");
				formatter.setMinimumFractionDigits(0);
				formatter.setMaximumFractionDigits(10);	// show value with max 10 digits after the dot if it is double value
				if (value instanceof Double) {
					value = formatter.format((Number) value);
				}
				setHorizontalAlignment(JLabel.RIGHT);
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
			
		
		for (int i = 0; i < columnNames6b.length; i++) {
			if (i < 2) {
        		table6b.getColumnModel().getColumn(i).setCellRenderer(r);
        	} else {
        		table6b.getColumnModel().getColumn(i).setCellRenderer(r2);
        	}
		}		
				
		table6b.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table6b.setCellSelectionEnabled(true);
        table6b.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table6b.getTableHeader().setReorderingAllowed(false);		// Disable columns move
        table6b.setPreferredScrollableViewportSize(new Dimension(400, 100));
//      table6b.setFillsViewportHeight(true);
//      TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>(model6b);	//Add sorter
//		for (int i = 1; i < colCount6b; i++) {
//			sorter.setSortable(i, false);
//			if (i == 0) {			// Only the first column can be sorted
//				sorter.setSortable(i, true);	
//			}
//		}
//		table6b.setRowSorter(sorter);
	}		
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table6c() {	
		List<List<String>> all_layers = read_database.get_all_layers();
		List<List<String>> all_layers_tooltips = read_database.get_all_layers_tooltips();
		int total_CoverType = all_layers.get(4).size();	
		
		// Setup the table------------------------------------------------------------	
		if (is_table6c_loaded == false) { // Create a fresh new if Load fail				
			rowCount6c = total_CoverType * total_CoverType;
			colCount6c =  total_replacing_disturbance + 2;
			data6c = new Object[rowCount6c][colCount6c];
	        columnNames6c = new String[colCount6c];
	        columnNames6c[0] = "layer5";
	        columnNames6c[1] = "layer5_regen";
	        for (int col = 2; col < colCount6c; col++) {
	        	int disturbance_index = col - 1;
	        	if (disturbance_index < 10) columnNames6c[col] = "SR_0" + disturbance_index; else columnNames6c[col] = "SR_" + disturbance_index;
	        }
	        
			// Populate the data matrix
	        int row = 0;
			for (int i = 0; i < total_CoverType; i++) {
				for (int j = 0; j < total_CoverType; j++) {
					data6c[row][0] = all_layers.get(4).get(i);
					data6c[row][1] = all_layers.get(4).get(j);	
					for (int col = 2; col < colCount6c; col++) {
						if (i==j) data6c[row][col] = (double) 0; else data6c[row][col] = (double) 0;
					}
					row++;
				}
			}			
		}
		
		
        // Header tool-tip
		String[] headerToolTips = new String[colCount6c];
		headerToolTips[0] = "layer5 (not dynamic) before the occurrence of stand replacing disturbances";
        headerToolTips[1] = "layer5 regenerated after the occurrence of stand replacing disturbances";
        for (int col = 2; col < colCount6c; col++) {
        	String disturbance_name = columnNames6c[col].replaceAll("percentage_", "");
			headerToolTips[col] = "conversion rate mean of " + disturbance_name;
        }
        
		
		// Create a table-------------------------------------------------------------		
        model6c = new PrismTableModel(rowCount6c, colCount6c, data6c, columnNames6c) {
        	@Override
    		public boolean isCellEditable(int row, int col) {
    			if (col >= 2) { // Only column >=2 is editable
    				return true;
    			} else {
    				return false;
    			}
    		}

        	@Override
    		public void setValueAt(Object value, int row, int col) {
    			if (/*value != null && */col >= 2 && (((Number) value).doubleValue() < 0 || ((Number) value).doubleValue() > 100)) {		// not allow null to be set
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Only double values in the range 0-100 (%) would be allowed.");
    			} else {
    				data6c[row][col] = value;
    				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
    				if (table6c.getSelectedRows().length == 1) {	// only fire the change when the table has only one cell selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
    					int currentRow = table6c.getSelectedRow();		        	
    					int currentCol = table6c.getSelectedColumn();	
    					fireTableDataChanged();		// This will clear the selection 
    					table6c.changeSelection(currentRow, currentCol, true, false);	// This will add the selection back (reselect previous cell)
    				}
    			}
    		}
        	
        	@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount6c; row++) {
					for (int col = 0; col < colCount6c; col++) {
						if (String.valueOf(data6c[row][col]).equals("null")) {
							data6c[row][col] = null;
						} else {					
							if (col >= 2) {			// Column >=2 are Double
								try {
									data6c[row][col] = Double.valueOf(String.valueOf(data6c[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table6c");
								}	
							} else {	//All other columns are String
								data6c[row][col] = String.valueOf(data6c[row][col]);
							}
						}	
					}	
				}	
			}
        };
        
        
		table6c = new JTable(model6c) {
			@Override			// These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table6c.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table6c,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				tableColumn.setPreferredWidth(maxWidth);
				
				// Set icon for cells: when total percentage of a given block 		NOTE: we need to use getValueAt because of the compact view feature which makes mismatching between full data and displayed data
				boolean has_at_least_one_positive_loss_rate = false;
				for (int i = 0; i < data6a.length; i++) {
					if (!has_at_least_one_positive_loss_rate && data6a[i][0].toString().equals(getValueAt(row, 0).toString())) {
						for (int j = 2; j < data6a[0].length; j++) {
							if (!has_at_least_one_positive_loss_rate && 
									(Double.valueOf(data6a[i][j].toString()) != 0 || Double.valueOf(data6b[i][j].toString()) != 0)) {
								has_at_least_one_positive_loss_rate = true;
							}
						}
					}	
				}

				((DefaultTableCellRenderer) component).setIcon(null);
				if (has_at_least_one_positive_loss_rate) {
					double total_percentage = 0;
					for (int i = 0; i < getRowCount(); i++) {	// loop all rows in a column && add to total percentage if the rows has the same covertype as the row at cursor
						if (column >= 2 && getValueAt(i, 0).toString().equals(getValueAt(row, 0).toString()) && table6c.convertColumnIndexToView(column) != -1) {	// -1 means the column is invisible
						total_percentage = total_percentage + Double.parseDouble(getValueAt(i, column).toString());
						}
					}
					
					if (total_percentage != 100 && column >= 2) {		// check if the total_percentage <> 100% --> problem icon for this cell because it is in the set of cells which make total_percentage <> 100%
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(14, 14, "icon_problem.png"));
					}
				};
				table6c.revalidate();
				table6c.repaint();
				
				return component;
			}		
			
			@Override		// make the width of the cell fit all contents of the cell	@Override
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
						if (columnModel.getColumnIndexAtX(p.x) >= 0) {
                        	int index = columnModel.getColumnIndexAtX(p.x);
                            int realIndex = columnModel.getColumn(index).getModelIndex();
                            tip = headerToolTips[realIndex];
                        }
                        return tip;
                    }
                };
            }	
			
			@Override		// implement table header tool tips         
			public String getToolTipText(MouseEvent e) {
				String tip = null;
				java.awt.Point p = e.getPoint();
				int row = rowAtPoint(p);
				int column = columnAtPoint(p);
				if (table6c.getColumnName(column).equals("layer5") || table6c.getColumnName(column).equals("layer5_regen")) {
					try {
						tip = getValueAt(row, column).toString();
						for (int i = 0; i < total_CoverType; i++) {
							if (tip.equals(all_layers.get(4).get(i))) tip = all_layers_tooltips.get(4).get(i);							
						}
					} catch (RuntimeException e1) {
						System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
					}
				}
				
				if (!table6c.getColumnName(column).equals("layer5") && !table6c.getColumnName(column).equals("layer5_regen")) {
					try {
						DecimalFormat formatter = new DecimalFormat("###,###.###");
						formatter.setMinimumFractionDigits(0);
						formatter.setMaximumFractionDigits(2);
						String percentage = formatter.format((Number) getValueAt(row, column));
						String disturbance_name = table6c.getColumnName(column).replaceAll("percentage_", "");
						tip = "For the total area with cover type = " + getValueAt(row, 0).toString() + " destroyed by "  + disturbance_name + ", " + percentage + "% of this area will be regenerated as cover type = " + getValueAt(row, 1).toString();
					
						// Show problem tip 		NOTE: we need to use getValueAt because of the compact view feature which makes mismatching between full data and displayed data
						boolean has_at_least_one_positive_loss_rate = false;
						for (int i = 0; i < data6a.length; i++) {
							if (!has_at_least_one_positive_loss_rate && data6a[i][0].toString().equals(getValueAt(row, 0).toString())) {
								for (int j = 2; j < data6a[0].length; j++) {
									if (!has_at_least_one_positive_loss_rate && 
											(Double.valueOf(data6a[i][j].toString()) != 0 || Double.valueOf(data6b[i][j].toString()) != 0)) {
										has_at_least_one_positive_loss_rate = true;
									}
								}
							}	
						}

						if (has_at_least_one_positive_loss_rate) {
							double total_percentage = 0;
							for (int i = 0; i < getRowCount(); i++) {	// loop all rows in a block && add to total percentage if the rows has the same covertype as the row at cursor
								for (int j = 2; j < getColumnCount(); j++) {					
									if (column >= 2 && getValueAt(i, 0).toString().equals(getValueAt(row, 0).toString()) && table6c.convertColumnIndexToView(column) != -1) {	// -1 means the column is invisible
										total_percentage = total_percentage + Double.parseDouble(getValueAt(i, column).toString());
									}
								}	
							}
							
							if (total_percentage != 100 && column >= 2) {		// check if the total_percentage <> 100%
								tip = "INFEASIBLE - The sum of all cells in this column with the same layer5 = " + getValueAt(row, 0).toString() + " must be exactly 100";
							}
						};
					} catch (RuntimeException e1) {
						System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
					}
				}	
				
				return tip;
			}
		};			
        
		
		// Define a set of icon for some columns
		ImageIcon[] imageIconArray = new ImageIcon[colCount6c];
		for (int i = 0; i < colCount6c; i++) {
			if (i == 2) {
				imageIconArray[i] = IconHandle.get_scaledImageIcon(3, 3, "icon_main.png");
			}
		}
		
		
		// Define a set of background color for all rows
		Color[] rowColor = new Color[rowCount6c];
		Color color1 = new Color(160, 160, 160);
		Color color2 = new Color(192, 192, 192);
		Color currentColor = color2;
		int rCount = 0;

		for (int i = 0; i < total_CoverType; i++) {
			if (currentColor == color2) {
				currentColor = color1;
			} else {
				currentColor = color2;
			}
			for (int j = 0; j < total_CoverType; j++) {
				rowColor[rCount] = currentColor;
				rCount++;
			}
		}
		
				
		// Set Color and Alignment for Cells
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// setForeground(Color.RED);
				setHorizontalAlignment(JLabel.LEFT);
				// setFont(getFont().deriveFont(Font.BOLD));               	
				setBackground(rowColor[row]);		//Set cell background color
				if (isSelected) {
					setBackground(table.getSelectionBackground());		//Set background color	for selected row
				}
				setIcon(imageIconArray[column]);	// Set icons for cells in some columns
				setIconTextGap(15);		// Set the distance between icon and the actual data value
//				setHorizontalAlignment(rowAlignment[row]);			
                return this;
            }
        };						
		
        
        // Set Double precision for cells
        DefaultTableCellRenderer r2 = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {			
				setHorizontalAlignment(JLabel.RIGHT);			
				DecimalFormat formatter = new DecimalFormat("###,###.###");
				formatter.setMinimumFractionDigits(0);
				formatter.setMaximumFractionDigits(10);	// show value with max 10 digits after the dot if it is double value
				if (value instanceof Double) {
					value = formatter.format((Number) value);
				}
				setHorizontalAlignment(JLabel.RIGHT);
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
			
		
		for (int i = 0; i < columnNames6c.length; i++) {
			if (i < 2) {
        		table6c.getColumnModel().getColumn(i).setCellRenderer(r);
        	} else {
        		table6c.getColumnModel().getColumn(i).setCellRenderer(r2);
        	}
		}		
		
		
		table6c.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table6c.setCellSelectionEnabled(true);
        table6c.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table6c.getTableHeader().setReorderingAllowed(false);		//Disable columns move
        table6c.setPreferredScrollableViewportSize(new Dimension(400, 100));
//      table6c.setFillsViewportHeight(true);
//      TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>(model6c);	//Add sorter
//		for (int i = 1; i < colCount6c; i++) {
//			sorter.setSortable(i, false);
//			if (i == 0) {			//Only the first column can be sorted
//				sorter.setSortable(i, true);	
//			}
//		}
//		table6c.setRowSorter(sorter);
	}		
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table6d() {	
		List<List<String>> all_layers = read_database.get_all_layers();
		List<List<String>> all_layers_tooltips = read_database.get_all_layers_tooltips();
		int total_CoverType = all_layers.get(4).size();	
		
		// Setup the table------------------------------------------------------------	
		if (is_table6d_loaded == false) { // Create a fresh new if Load fail				
			rowCount6d = total_CoverType * total_CoverType;
			colCount6d =  total_replacing_disturbance + 2;
			data6d = new Object[rowCount6d][colCount6d];
	        columnNames6d = new String[colCount6d];
	        columnNames6d[0] = "layer5";
	        columnNames6d[1] = "layer5_regen";
	        for (int col = 2; col < colCount6d; col++) {
	        	int disturbance_index = col - 1;
	        	if (disturbance_index < 10) columnNames6d[col] = "SR_0" + disturbance_index; else columnNames6d[col] = "SR_" + disturbance_index;
	        }
	        
			// Populate the data matrix
	        int row = 0;
			for (int i = 0; i < total_CoverType; i++) {
				for (int j = 0; j < total_CoverType; j++) {
					data6d[row][0] = all_layers.get(4).get(i);
					data6d[row][1] = all_layers.get(4).get(j);	
					for (int col = 2; col < colCount6d; col++) {
						if (i==j) data6d[row][col] = (double) 0; else data6d[row][col] = (double) 0;
					}
					row++;
				}
			}			
		}
		
		
        // Header tool-tip
		String[] headerToolTips = new String[colCount6d];
		headerToolTips[0] = "layer5 (not dynamic) before the occurrence of stand replacing disturbances";
        headerToolTips[1] = "layer5 regenerated after the occurrence of stand replacing disturbances";
        for (int col = 2; col < colCount6d; col++) {
        	String disturbance_name = columnNames6d[col].replaceAll("percentage_", "");
			headerToolTips[col] = "conversion rate standard deviation of " + disturbance_name;
        }
        
		
		// Create a table-------------------------------------------------------------		
        model6d = new PrismTableModel(rowCount6d, colCount6d, data6d, columnNames6d) {
        	@Override
    		public boolean isCellEditable(int row, int col) {
    			if (col >= 2) { // Only column >=2 is editable
    				return true;
    			} else {
    				return false;
    			}
    		}

        	@Override
    		public void setValueAt(Object value, int row, int col) {
    			if (/*value != null && */col >= 2 && (((Number) value).doubleValue() < 0 || ((Number) value).doubleValue() > 100)) {		// not allow null to be set
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Only double values in the range 0-100 (%) would be allowed.");
    			} else {
    				data6d[row][col] = value;
    				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
    				if (table6d.getSelectedRows().length == 1) {	// only fire the change when the table has only one cell selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
    					int currentRow = table6d.getSelectedRow();		        	
    					int currentCol = table6d.getSelectedColumn();	
    					fireTableDataChanged();		// This will clear the selection 
    					table6d.changeSelection(currentRow, currentCol, true, false);	// This will add the selection back (reselect previous cell)
    				}
    			}
    		}
        	
        	@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount6d; row++) {
					for (int col = 0; col < colCount6d; col++) {
						if (String.valueOf(data6d[row][col]).equals("null")) {
							data6d[row][col] = null;
						} else {					
							if (col >= 2) {			// Column >=2 are Double
								try {
									data6d[row][col] = Double.valueOf(String.valueOf(data6d[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table6d");
								}	
							} else {	//All other columns are String
								data6d[row][col] = String.valueOf(data6d[row][col]);
							}
						}	
					}	
				}	
			}
        };
        
        
		table6d = new JTable(model6d) {
			@Override			// These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table6d.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table6d,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				tableColumn.setPreferredWidth(maxWidth);
				
				// Set icon for cells: when total percentage of a given block 		NOTE: we need to use getValueAt because of the compact view feature which makes mismatching between full data and displayed data
				double total_percentage = 0;
				for (int i = 0; i < getRowCount(); i++) {	// loop all rows in a block && add to total percentage if the rows has the same covertype as the row at cursor
					for (int j = 2; j < getColumnCount(); j++) {					
						if (getValueAt(i, 0).toString().equals(getValueAt(row, 0).toString()) && table6d.convertColumnIndexToView(j) != -1) {	// -1 means the column is invisible
							total_percentage = total_percentage + Double.parseDouble(getValueAt(i, j).toString());
						}
					}	
				}
				if (total_percentage > 100 && column >= 2) {		// check if the total_percentage > 100% --> problem icon for this cell because it is in the set of cells which make total_percentage > 100%
					((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(14, 14, "icon_problem.png"));
				} else {
					((DefaultTableCellRenderer) component).setIcon(null);
				}
				
				return component;
			}		
			
			@Override		// make the width of the cell fit all contents of the cell	@Override
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
						if (columnModel.getColumnIndexAtX(p.x) >= 0) {
                        	int index = columnModel.getColumnIndexAtX(p.x);
                            int realIndex = columnModel.getColumn(index).getModelIndex();
                            tip = headerToolTips[realIndex];
                        }
                        return tip;
                    }
                };
            }	
			
			@Override		// implement table header tool tips         
			public String getToolTipText(MouseEvent e) {
				String tip = null;
				java.awt.Point p = e.getPoint();
				int row = rowAtPoint(p);
				int column = columnAtPoint(p);
				if (table6d.getColumnName(column).equals("layer5") || table6d.getColumnName(column).equals("layer5_regen")) {
					try {
						tip = getValueAt(row, column).toString();
						for (int i = 0; i < total_CoverType; i++) {
							if (tip.equals(all_layers.get(4).get(i))) tip = all_layers_tooltips.get(4).get(i);							
						}
					} catch (RuntimeException e1) {
						System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
					}
				}
				
				if (!table6d.getColumnName(column).equals("layer5") && !table6d.getColumnName(column).equals("layer5_regen")) {
					try {
						DecimalFormat formatter = new DecimalFormat("###,###.###");
						formatter.setMinimumFractionDigits(0);
						formatter.setMaximumFractionDigits(2);
						String percentage = formatter.format((Number) getValueAt(row, column));
						String disturbance_name = table6d.getColumnName(column).replaceAll("percentage_", "");
						tip = "For the total area with cover type = " + getValueAt(row, 0).toString() + " destroyed by "  + disturbance_name + ", " + percentage + "% of this area will be regenerated as cover type = " + getValueAt(row, 1).toString();
					
						// Show problem tip 		NOTE: we need to use getValueAt because of the compact view feature which makes mismatching between full data and displayed data
						double total_percentage = 0;
						for (int i = 0; i < getRowCount(); i++) {	// loop all rows in a block && add to total percentage if the rows has the same covertype as the row at cursor
							for (int j = 2; j < getColumnCount(); j++) {					
								if (getValueAt(i, 0).toString().equals(getValueAt(row, 0).toString()) && table6d.convertColumnIndexToView(j) != -1) {	// -1 means the column is invisible
									total_percentage = total_percentage + Double.parseDouble(getValueAt(i, j).toString());
								}
							}	
						}
						if (total_percentage > 100 && column >= 2) {		// check if the total_percentage > 100% 
							tip = "INFEASIBLE - The sum of all cells with the same layer5 = " + getValueAt(row, 0).toString() + " must not exceed 100";
						}
					} catch (RuntimeException e1) {
						System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
					}
				}	
				
				return tip;
			}
		};			
        
		
		// Define a set of icon for some columns
		ImageIcon[] imageIconArray = new ImageIcon[colCount6d];
		for (int i = 0; i < colCount6d; i++) {
			if (i == 2) {
				imageIconArray[i] = IconHandle.get_scaledImageIcon(3, 3, "icon_main.png");
			}
		}
		
		
		// Define a set of background color for all rows
		Color[] rowColor = new Color[rowCount6d];
		Color color1 = new Color(160, 160, 160);
		Color color2 = new Color(192, 192, 192);
		Color currentColor = color2;
		int rCount = 0;

		for (int i = 0; i < total_CoverType; i++) {
			if (currentColor == color2) {
				currentColor = color1;
			} else {
				currentColor = color2;
			}
			for (int j = 0; j < total_CoverType; j++) {
				rowColor[rCount] = currentColor;
				rCount++;
			}
		}
		
				
		// Set Color and Alignment for Cells
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// setForeground(Color.RED);
				setHorizontalAlignment(JLabel.LEFT);
				// setFont(getFont().deriveFont(Font.BOLD));               	
				setBackground(rowColor[row]);		//Set cell background color
				if (isSelected) {
					setBackground(table.getSelectionBackground());		//Set background color	for selected row
				}
				setIcon(imageIconArray[column]);	// Set icons for cells in some columns
				setIconTextGap(15);		// Set the distance between icon and the actual data value
//				setHorizontalAlignment(rowAlignment[row]);			
                return this;
            }
        };						
		
        
        // Set Double precision for cells
        DefaultTableCellRenderer r2 = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {			
				setHorizontalAlignment(JLabel.RIGHT);			
				DecimalFormat formatter = new DecimalFormat("###,###.###");
				formatter.setMinimumFractionDigits(0);
				formatter.setMaximumFractionDigits(10);	// show value with max 10 digits after the dot if it is double value
				if (value instanceof Double) {
					value = formatter.format((Number) value);
				}
				setHorizontalAlignment(JLabel.RIGHT);
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
			
		
		for (int i = 0; i < columnNames6d.length; i++) {
			if (i < 2) {
        		table6d.getColumnModel().getColumn(i).setCellRenderer(r);
        	} else {
        		table6d.getColumnModel().getColumn(i).setCellRenderer(r2);
        	}
		}		
		
		
		table6d.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table6d.setCellSelectionEnabled(true);
        table6d.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table6d.getTableHeader().setReorderingAllowed(false);		//Disable columns move
        table6d.setPreferredScrollableViewportSize(new Dimension(400, 100));
//      table6d.setFillsViewportHeight(true);
//      TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>(model6d);	//Add sorter
//		for (int i = 1; i < colCount6d; i++) {
//			sorter.setSortable(i, false);
//			if (i == 0) {			//Only the first column can be sorted
//				sorter.setSortable(i, true);	
//			}
//		}
//		table6d.setRowSorter(sorter);
	}
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table7() {
		//Setup the table------------------------------------------------------------	
		if (is_table7_loaded == false) { // Create a fresh new if Load fail				
			rowCount7 = 0;
			colCount7 = 8;
			data7 = new Object[rowCount7][colCount7];
			columnNames7 = new String[] {"condition_id", "condition_description", "action_cost", "conversion_cost", "static_identifiers", "dynamic_identifiers", "original_dynamic_identifiers", "model_condition"};
		}
					
		
		//Create a table-------------------------------------------------------------		
		model7 = new PrismTableModel(rowCount7, colCount7, data7, columnNames7) {
			@Override
			public Class getColumnClass(int c) {
				if (c == 7) return Boolean.class;
				else return String.class;
			}
			
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col == 1 || col == 7) { //  Only column "description" and "model_condition" is editable
					return true;
				} else {
					return false;
				}
			}

			@Override
			public void setValueAt(Object value, int row, int col) {
				data7[row][col] = value;
			}
			
			@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount7; row++) {
					for (int col = 0; col < colCount7; col++) {
						if (String.valueOf(data7[row][col]).equals("null")) {
							data7[row][col] = null;
						} else {	
							if (col == 7) {
								try {
									data7[row][col] = Boolean.valueOf(String.valueOf(data7[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Boolean values in create_table7");
								}
							} else {
								data7[row][col] = String.valueOf(data7[row][col]);
							}
						}
					}	
				}	
			}
		};
		
		
		
		table7 = new JTable(model7) {
			@Override			//These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table7.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table7,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				if (column != 1) {
					tableColumn.setPreferredWidth(maxWidth);
				} else {
					tableColumn.setMinWidth(200);
				}
				return component;
			}
			
			@Override	// Implement table cell tool tips           
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int row = rowAtPoint(p);
				int col = columnAtPoint(p);
				String tip = (table7.getColumnName(col).equals("condition_description") && row >= 0 && getValueAt(row, col) != null) ? getValueAt(row, col).toString() : null;
				return tip;
			}		
		};

		((JComponent) table7.getDefaultRenderer(Boolean.class)).setOpaque(true);
		((AbstractButton) table7.getDefaultRenderer(Boolean.class)).setSelectedIcon(IconHandle.get_scaledImageIcon(12, 12, "icon_check.png"));


		// Hide columns
		TableColumnsHandle table_handle = new TableColumnsHandle(table7);
		table_handle.setColumnVisible("action_cost", false);
		table_handle.setColumnVisible("conversion_cost", false);
		table_handle.setColumnVisible("static_identifiers", false);
		table_handle.setColumnVisible("dynamic_identifiers", false);
		table_handle.setColumnVisible("original_dynamic_identifiers", false);
  
		table7.setAutoResizeMode(0);		// 0 = JTable.AUTO_RESIZE_OFF
		table7.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);  
		table7.getTableHeader().setReorderingAllowed(false);		//Disable columns move
		table7.setPreferredScrollableViewportSize(new Dimension(150, 100));
//		table7.setFillsViewportHeight(true);
	}		

	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table7a() {
		//Setup the table------------------------------------------------------------	
		String[] yield_tables_column_names = read_database.get_yield_tables_column_names();
		if (is_table7a_loaded == false) { // Create a fresh new if Load fail	
			// This all_actions List contains all actions loaded from yield tables------------------------------------------------------------
			List<String> action_list = new ArrayList<String>();
			if (yield_tables_column_names != null) {	//create table with column include yield tables columns
				for (String action: read_database.get_action_type()) {
					action_list.add(action);					
				}	
				
				rowCount7a = action_list.size();			
				colCount7a = 2 + yield_tables_column_names.length;
				data7a = new Object[rowCount7a][colCount7a];
				columnNames7a = new String[2 + yield_tables_column_names.length];
				columnNames7a[0] = "action_list";
				columnNames7a[1] = "acres";
				for (int i = 2; i < columnNames7a.length; i++) {
					columnNames7a[i] = yield_tables_column_names[i - 2];				
				}	
			} else {
				rowCount7a = action_list.size();			
				colCount7a = 2;
				data7a = new Object[rowCount7a][colCount7a];
				columnNames7a= new String[] {"action_list", "acres"};
			}			
			
	       			
			// Populate the data matrix
			for (int i = 0; i < rowCount7a; i++) {
				data7a[i][0] = action_list.get(i);
				data7a[i][1] = (action_list.get(i).equalsIgnoreCase("no-action")) ? (double) 0 : (double) 360; 
			}
		}		
		
		
        // header tool-tip
		String[] headerToolTips = new String[colCount7a];
		headerToolTips[0] = "all unique actions found from  yield_tables in your selected database";
        headerToolTips[1] = "currency per acre where an action is implemented";
		if (yield_tables_column_names != null) {      
	        for (int i = 2; i < colCount7a; i++) {
	        	int yt_col = i - 2;
	        	headerToolTips[i] = "currency per " + read_database.get_ParameterToolTip(yield_tables_column_names[yt_col]) + " (Column index: " + yt_col + ")";	
			}
		}
	
		
		//Create a table-------------------------------------------------------------			
        model7a = new PrismTableModel(rowCount7a, colCount7a, data7a, columnNames7a) {
        	@Override
			public Class getColumnClass(int c) {
				if (c > 0) return Double.class;      // columns > 0 accept only Double  
				else return String.class;				
			}
        	
        	@Override
    		public boolean isCellEditable(int row, int col) {
				if (col == 0) { // the 1st column is not editable
    				return false;
    			} else {
    				return true;
    			}
    		}

        	@Override
    		public void setValueAt(Object value, int row, int col) {
        		if (value != null && col > 0 && ((Number) value).doubleValue() < 0) {			// allow null to be set
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Cost cannot be negative.");
    			} else {
    				data7a[row][col] = value;
    				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
    				if (table7a.getSelectedRows().length == 1) {	// only fire the change when the table has only one cell selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
    					int currentRow = table7a.getSelectedRow();		        	
    					int currentCol = table7a.getSelectedColumn();	
    					fireTableDataChanged();		// This will clear the selection 
    					table7a.changeSelection(currentRow, currentCol, true, false);	// This will add the selection back (reselect previous cell)
    				}
    			}
    		}
        	
        	@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount7a; row++) {
					for (int col = 0; col < colCount7a; col++) {
						if (String.valueOf(data7a[row][col]).equals("null")) {
							data7a[row][col] = null;
						} else {					
							if (col > 0) {			//Columns except the 1st columns are Double
								try {
									data7a[row][col] = Double.valueOf(String.valueOf(data7a[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table7");
								}	
							} else {	//All other columns are String
								data7a[row][col] = String.valueOf(data7a[row][col]);
							}
						}	
					}	
				}	
			}
        };
        
        
		table7a = new JTable(model7a) {
			@Override		// make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table7a.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table7a,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				tableColumn.setPreferredWidth(maxWidth);
				return component;
			}		
			
			@Override		// implement table header tool tips
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
						if (columnModel.getColumnIndexAtX(p.x) > 0) {
                        	int index = columnModel.getColumnIndexAtX(p.x);
                            int realIndex = columnModel.getColumn(index).getModelIndex();
                            tip = headerToolTips[realIndex];
                        }
                        return tip;
                    }
                };
            }	
		};				
		

		// Define a set of background colors
		Color[] rowColor = new Color[rowCount7a];
		Color color1 = new Color(160, 160, 160);
		Color color2 = new Color(192, 192, 192);					
		Color currentColor = color1;

		for (int i = 0; i < rowCount7a; i++) {
			if (currentColor == color2) {
				currentColor = color1;
			} else {
				currentColor = color2;
			}
			rowColor[i] = currentColor;
		}
		
		// Define a set of icon for some columns
 		ImageIcon[] imageIconArray = new ImageIcon[colCount6];
 		for (int i = 0; i < colCount6; i++) {
 			if (i >= 1) {
 				imageIconArray[i] = IconHandle.get_scaledImageIcon(3, 3, "icon_main.png");
 			}
 		}	
				
		//Set Color and Alignment for Cells
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// setForeground(Color.RED);
				setHorizontalAlignment(JLabel.LEFT);
				// setFont(getFont().deriveFont(Font.BOLD));               	
				setBackground(rowColor[row]);		//Set cell background color
				if (isSelected) {
					setBackground(table.getSelectionBackground());		//Set background color	for selected row
				}
//				setHorizontalAlignment(rowAlignment[row]);			
                return this;
            }
        };						
		
        
		// Set Double precision for cells
		DefaultTableCellRenderer r2 = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {			
				setHorizontalAlignment(JLabel.RIGHT);			
				DecimalFormat formatter = new DecimalFormat("###,###.###");
				formatter.setMinimumFractionDigits(0);
				formatter.setMaximumFractionDigits(10);	// show value with max 10 digits after the dot if it is double value
				if (value instanceof Double) {
					value = formatter.format((Number) value);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        
		
		for (int i = 0; i < columnNames7a.length; i++) {
			if (i == 0) {
				table7a.getColumnModel().getColumn(i).setCellRenderer(r);		// first column is shaded
			} else {
				table7a.getColumnModel().getColumn(i).setCellRenderer(r2);
			}
		}
			
		
		if (yield_tables_column_names != null) table7a.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table7a.setCellSelectionEnabled(true);
        table7a.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table7a.getTableHeader().setReorderingAllowed(false);		//Disable columns move
        
//      table7a.setTableHeader(null);
        table7a.setPreferredScrollableViewportSize(new Dimension(200, 100));
//      table7a.setFillsViewportHeight(true);
        TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>(model7a);	//Add sorter
		for (int i = 1; i < colCount7a; i++) {
			sorter.setSortable(i, false);
			if (i == 0) {			//Only the first column can be sorted
				sorter.setSortable(i, true);	
			}
		}
//		table7a.setRowSorter(sorter);
	}	

	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table7b() {	
		String[] yield_tables_column_names = read_database.get_yield_tables_column_names();
		List<List<String>> all_layers = read_database.get_all_layers();
		int total_CoverType = all_layers.get(4).size();		// total number of elements - 1 in layer5 Cover Type (0 to...)	
		
		//Setup the table------------------------------------------------------------	
		if (is_table7b_loaded == false) { // Create a fresh new if Load fail	
			if (yield_tables_column_names != null) {	//create table with column include yield tables columns
				rowCount7b = total_CoverType * total_CoverType;		
				colCount7b = 4;
				data7b = new Object[rowCount7b][colCount7b];
				columnNames7b = new String[] {"layer5", "layer5_regen", "action", "disturbance"};
			}		
			       			
			// Populate the data matrix
			 int table_row = 0;
				for (int i = 0; i < total_CoverType; i++) {
					for (int j = 0; j < total_CoverType; j++) {
						data7b[table_row][0] = all_layers.get(4).get(i);
						data7b[table_row][1] = all_layers.get(4).get(j);
						// data7b[table_row][2] = (double) 240; 
						table_row++;
				}
			}
		}
		
		
        // header tool-tip
		String[] headerToolTips = new String[colCount7b];
		headerToolTips[0] = "cover type (not dynamic) before the occurence of EA clear-cut or stand replacing disturbance";
        headerToolTips[1] = "cover type after the occurence of EA clear-cut or stand replacing disturbance";
        headerToolTips[2] = "currency per area unit of cover type conversion by EA cleat-cut";
        headerToolTips[3] = "currency per area unit of cover type conversion by stand replacing disturbance";
		
        
		//Create a table-------------------------------------------------------------			
        model7b = new PrismTableModel(rowCount7b, colCount7b, data7b, columnNames7b) {
        	@Override
			public Class getColumnClass(int c) {
				if (c > 1) return Double.class;      // columns > 1 accept only Double  
				else return String.class;				
			}
        	
        	@Override
    		public boolean isCellEditable(int row, int col) {
				if (col < 2) { // the first 2 column is not editable
					return false;
    			} else {
    				return true;
    			}
    		}

        	@Override
    		public void setValueAt(Object value, int row, int col) {
        		if (value != null && col >= 2 && ((Number) value).doubleValue() < 0) {
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Cost cannot be negative.");
    			} else {
    				data7b[row][col] = value;   
    				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
    				if (table7b.getSelectedRows().length == 1) {	// only fire the change when the table has only one cell selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
    					int currentRow = table7b.getSelectedRow();		        	
    					int currentCol = table7b.getSelectedColumn();	
    					fireTableDataChanged();		// This will clear the selection 
    					table7b.changeSelection(currentRow, currentCol, true, false);	// This will add the selection back (reselect previous cell)
    				}
    			}
    		}
        	
        	@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount7b; row++) {
					for (int col = 0; col < colCount7b; col++) {
						if (String.valueOf(data7b[row][col]).equals("null")) {
							data7b[row][col] = null;
						} else {					
							if (col >= 2) {			//Columns except the 1st 2 columns are Double
								try {
									data7b[row][col] = Double.valueOf(String.valueOf(data7b[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table7b");
								}	
							} else {	//All other columns are String
								data7b[row][col] = String.valueOf(data7b[row][col]);
							}
						}	
					}	
				}	
			}
        };
        
        
		table7b = new JTable(model7b) {
			@Override		// make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table7b.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table7b,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				tableColumn.setPreferredWidth(maxWidth);
				return component;
			}		
			
			@Override		// implement table header tool tips
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
						if (columnModel.getColumnIndexAtX(p.x) >= 0) {
                        	int index = columnModel.getColumnIndexAtX(p.x);
                            int realIndex = columnModel.getColumn(index).getModelIndex();
                            tip = headerToolTips[realIndex];
                        }
                        return tip;
                    }
                };
            }
		};
		

		// Define a set of background color for all rows
		Color[] rowColor = new Color[rowCount6c];
		Color color1 = new Color(160, 160, 160);
		Color color2 = new Color(192, 192, 192);
		Color currentColor = color2;
		int rCount = 0;

		for (int i = 0; i < total_CoverType; i++) {
			if (currentColor == color2) {
				currentColor = color1;
			} else {
				currentColor = color2;
			}
			for (int j = 0; j < total_CoverType; j++) {
				rowColor[rCount] = currentColor;
				rCount++;
			}
		}
		
		// Define a set of icon for some columns
 		ImageIcon[] imageIconArray = new ImageIcon[colCount6];
 		for (int i = 0; i < colCount6; i++) {
 			if (i >= 1) {
 				imageIconArray[i] = IconHandle.get_scaledImageIcon(3, 3, "icon_main.png");
 			}
 		}	
				
		//Set Color and Alignment for Cells
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// setForeground(Color.RED);
				setHorizontalAlignment(JLabel.LEFT);
				// setFont(getFont().deriveFont(Font.BOLD));               	
				setBackground(rowColor[row]);		//Set cell background color
				if (isSelected) {
					setBackground(table.getSelectionBackground());		//Set background color	for selected row
				}
//				setHorizontalAlignment(rowAlignment[row]);			
                return this;
            }
        };						
		
        
        // Set Double precision for cells
        DefaultTableCellRenderer r2 = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {			
				setHorizontalAlignment(JLabel.RIGHT);			
				DecimalFormat formatter = new DecimalFormat("###,###.###");
				formatter.setMinimumFractionDigits(0);
				formatter.setMaximumFractionDigits(10);	// show value with max 10 digits after the dot if it is double value
				if (value instanceof Double) {
					value = formatter.format((Number) value);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
             
		
		for (int i = 0; i < columnNames7b.length; i++) {
			if (i < 2) {
				table7b.getColumnModel().getColumn(i).setCellRenderer(r);		// first 2 columns is shaded
			} else {
				table7b.getColumnModel().getColumn(i).setCellRenderer(r2);
			}
		}
			
		
//		table7b.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table7b.setCellSelectionEnabled(true);
        table7b.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table7b.getTableHeader().setReorderingAllowed(false);		//Disable columns move
        
//      table7b.setTableHeader(null);
        table7b.setPreferredScrollableViewportSize(new Dimension(200, 100));
//      table7b.setFillsViewportHeight(true);
        TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>(model7b);	//Add sorter
		for (int i = 0; i < colCount7b; i++) {
			sorter.setSortable(i, false);
			if (i < 2) {			//Only the first 2 columns can be sorted
				sorter.setSortable(i, true);	
			}
		}
//		table7b.setRowSorter(sorter);
	}		
			
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table8() {
		class comboBox_constraint_type extends JComboBox {	
			public comboBox_constraint_type() {
				addItem("SOFT");
				addItem("HARD");
				addItem("FREE");
				addItem("IDLE");
				setSelectedIndex(0);
			}
		}
		
			
		//Setup the table------------------------------------------------------------	
		if (is_table8_loaded == false) { // Create a fresh new if Load fail				
			rowCount8 = 0;
			colCount8 = 12;
			data8 = new Object[rowCount8][colCount8];
			columnNames8 = new String[] {"bc_id", "bc_description", "bc_type",  "bc_multiplier", "lowerbound", "lowerbound_perunit_penalty", "upperbound", "upperbound_perunit_penalty", "parameter_index", "static_identifiers", "dynamic_identifiers", "original_dynamic_identifiers"};	         				
		}

		
		//Create a table-------------------------------------------------------------		
		model8 = new PrismTableModel(rowCount8, colCount8, data8, columnNames8) {
			@Override
			public Class getColumnClass(int c) {
				if (c == 0) return Integer.class;      //column 0 accepts only Integer
				else if (c >= 3 && c <= 7) return Double.class;      //column 3 to 7 accept only Double values   
				else return String.class;				
			}
			
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col == 0 || col >= colCount8 - 4) { //  The first and the last 4 columns are un-editable
					return false;
				} else {
					return true;
				}
			}
			
			@Override
			public void setValueAt(Object value, int row, int col) {
				if (value != null && (col >= 3 && col <= 7) && ((Number) value).doubleValue() < 0) {		// allow null to be set, and not allow negative numbers
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Only null or positive values are allowed");
    			} else {
    				data8[row][col] = value;
    				if (col == 2) {
    					is_IDLE_basic_constraints_used_in_flow_constraints();
    				}
    				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
    				if (table8.getSelectedRows().length == 1) {	// only fire the change when the table has only one cell selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
    					int currentRow = table8.getSelectedRow();		        	
    					int currentCol = table8.getSelectedColumn();	
    					fireTableDataChanged();		// This will clear the selection 
    					table8.changeSelection(currentRow, currentCol, true, false);	// This will add the selection back (reselect previous cell)
    				}
    			}
			}
			
			@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount8; row++) {
					for (int col = 0; col < colCount8; col++) {
						if (String.valueOf(data8[row][col]).equals("null")) {
							data8[row][col] = null;
						} else {					
							if (col == 0) {			//Column 0 is Integer
								try {
									data8[row][col] = Integer.valueOf(String.valueOf(data8[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Integer values in create_table8");
								}	
							} else if (col >= 3 && col <= 7) {			//Column 3 to 7 are Double
								try {
									data8[row][col] = Double.valueOf(String.valueOf(data8[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table8");
								}
							} else {	//All other columns are String
								data8[row][col] = String.valueOf(data8[row][col]);
							}
						}	
					}	
				}	
			}
		};
		
		
		
		table8 = new JTable(model8) {
			@Override		// These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cell width								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column header width
				TableCellRenderer renderer2 = table8.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table8,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				if (column != 1) {
					tableColumn.setPreferredWidth(maxWidth);
				} else {
					tableColumn.setMinWidth(140);
				}
								
				// Set icon for cells
				if (column == 2) {
					if (getValueAt(row, 2) == null || getValueAt(row, 2).toString().equals("IDLE")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_gray.png"));
					} else if (getValueAt(row, 2).toString().equals("FREE")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_blue.png"));
					} else if (getValueAt(row, 2).toString().equals("SOFT")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_yellow.png"));
					} else if (getValueAt(row, 2).toString().equals("HARD")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_red.png"));
					}
				} else if (column == 4 || column == 6) {
					// Set icon for cells: when UB > LB		NOTE: it is OK to use the number for column because we do not allow changing column position (row could be changed due to the SORT button --> need to use getValueAt)
					double bound_difference = Double.MAX_VALUE;
					if (getValueAt(row, 6) != null) bound_difference = Double.parseDouble(getValueAt(row, 6).toString());
					if (getValueAt(row, 4) != null) bound_difference = bound_difference - Double.parseDouble(getValueAt(row, 4).toString());
					if (getValueAt(row, 2).toString().equals("HARD") && bound_difference < 0) {		// check if the LB - UB < 0
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(14, 14, "icon_problem.png"));
					} else {
						((DefaultTableCellRenderer) component).setIcon(null);
					}
				} else {
					((DefaultTableCellRenderer) component).setIcon(null);
				}
						
				return component;
			}
			
			@Override	// Implement table cell tool tips           
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int row = rowAtPoint(p);
				int column = columnAtPoint(p);
				String tip = (table8.getColumnName(column).equals("bc_description") && row >= 0 && getValueAt(row, column) != null) ? getValueAt(row, column).toString() : null;
				
				if (column == 4 || column == 6) {
					// Set icon for cells: when UB > LB		NOTE: it is OK to use the number for column because we do not allow changing column position (row could be changed due to the SORT button --> need to use getValueAt)
					double bound_difference = Double.MAX_VALUE;
					if (getValueAt(row, 6) != null) bound_difference = Double.parseDouble(getValueAt(row, 6).toString());
					if (getValueAt(row, 4) != null) bound_difference = bound_difference - Double.parseDouble(getValueAt(row, 4).toString());
					if (getValueAt(row, 2).toString().equals("HARD") && bound_difference < 0) {		// check if the LB - UB < 0
						tip = "INFEASIBLE - lowerbound should not exceed upperbound";
					}
				} 
				return tip;
			}	
		};

		
        // Set Double precision for cells
        DefaultTableCellRenderer r2 = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {			
				setHorizontalAlignment(JLabel.RIGHT);			
				DecimalFormat formatter = new DecimalFormat("###,###.###");
				formatter.setMinimumFractionDigits(0);
				formatter.setMaximumFractionDigits(10);	// show value with max 10 digits after the dot if it is double value
				if (value instanceof Double) {
					value = formatter.format((Number) value);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
             
		
		for (int i = 0; i < columnNames8.length; i++) {
			if (i < 3 || i > 8) {

			} else {
				table8.getColumnModel().getColumn(i).setCellRenderer(r2);
			}
		}
		
    

        // Set up Type for each column 2
		table8.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new comboBox_constraint_type()));
					
		// Hide columns
		TableColumnsHandle table_handle = new TableColumnsHandle(table8);
		table_handle.setColumnVisible("parameter_index", false);
		table_handle.setColumnVisible("static_identifiers", false);
		table_handle.setColumnVisible("dynamic_identifiers", false);
		table_handle.setColumnVisible("original_dynamic_identifiers", false);
         
		table8.setAutoResizeMode(0);		// 0 = JTable.AUTO_RESIZE_OFF
		table8.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);  
		table8.getTableHeader().setReorderingAllowed(false);		//Disable columns move
		table8.setPreferredScrollableViewportSize(new Dimension(200, 100));
//		table8.setFillsViewportHeight(true);
	}
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table9() {
		class comboBox_constraint_type extends JComboBox {	
			public comboBox_constraint_type() {
//				addItem("SOFT");
				addItem("HARD");
				addItem("FREE");
				setSelectedIndex(0);
			}
		}
		
			
		//Setup the table------------------------------------------------------------	
		if (is_table9_loaded == false) { // Create a fresh new if Load fail				
			rowCount9 = 0;
			colCount9 = 6;
			data9 = new Object[rowCount9][colCount9];
			columnNames9 = new String[] {"flow_id", "flow_description", "flow_arrangement", "flow_type", "lowerbound_percentage", "upperbound_percentage"};	         				
		}
					
		
		//Create a table-------------------------------------------------------------		
		model9 = new PrismTableModel(rowCount9, colCount9, data9, columnNames9) {
			@Override
			public Class getColumnClass(int c) {
				if (c == 0) return Integer.class;      //column 0 accepts only Integer
				else if (c == 1 || c == 2) return String.class;      
				else if (c == 4 || c == 5) return Double.class;       
				else return String.class;				
			}
			
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col == 0 || col == 2) { 	// Columns 0 and 2 are un-editable
					return false;
				} else {
					return true;
				}
			}
			
			@Override
			public void setValueAt(Object value, int row, int col) {
				if (value != null && (col >= 4 && col <= 5) && ((Number) value).doubleValue() < 0) {		// allow null to be set, and not allow negative numbers
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Only null or positive values are allowed");
    			} else {
    				data9[row][col] = value;
    				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
    				if (table9.getSelectedRows().length == 1) {	// only fire the change when the table has only one cell selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
    					int currentRow = table9.getSelectedRow();		        	
    					int currentCol = table9.getSelectedColumn();	
    					fireTableDataChanged();		// This will clear the selection 
    					table9.changeSelection(currentRow, currentCol, true, false);	// This will add the selection back (reselect previous cell)
    				}
    			}
			}
			
			@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount9; row++) {
					for (int col = 0; col < colCount9; col++) {
						if (String.valueOf(data9[row][col]).equals("null")) {
							data9[row][col] = null;
						} else {					
							if (col == 0) {			//Column 0 is Integer
								try {
									data9[row][col] = Integer.valueOf(String.valueOf(data9[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Integer values in create_table9");
								}	
							} else if (col == 4 || col == 5) {			//Column 4 and 5 are Double
								try {
									data9[row][col] = Double.valueOf(String.valueOf(data9[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table9");
								}
							} else {	//All other columns are String
								data9[row][col] = String.valueOf(data9[row][col]);
							}
						}	
					}	
				}	
			}
		};
		
		
		
		table9 = new JTable(model9) {
			@Override			//These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cells in table								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column names
				TableCellRenderer renderer2 = table9.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table9,
			            tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				if (column != 1 && column != 2) {
					tableColumn.setPreferredWidth(maxWidth);
				} else {
					tableColumn.setMinWidth(200);
				}
				
				// Set icon for cells
				if (column == 3) {
					if (getValueAt(row, 3) == null || getValueAt(row, 3).toString().equals("FREE")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_blue.png"));
					} else if (getValueAt(row, 3).toString().equals("HARD")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_red.png"));
					}
				} else if (column == 4 || column == 5) {
					// Set icon for cells: when UB > LB		NOTE: it is OK to use the number for column because we do not allow changing column position (row could be changed due to the SORT button --> need to use getValueAt)
					double percentage_difference = Double.MAX_VALUE;
					if (getValueAt(row, 5) != null) percentage_difference = Double.parseDouble(getValueAt(row, 5).toString());
					if (getValueAt(row, 4) != null) percentage_difference = percentage_difference - Double.parseDouble(getValueAt(row, 4).toString());
					if (getValueAt(row, 3).toString().equals("HARD") && percentage_difference < 0 && column >= 4) {		// check if the LB - UB < 0
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(14, 14, "icon_problem.png"));
					} else {
						((DefaultTableCellRenderer) component).setIcon(null);
					}
				} else {
					((DefaultTableCellRenderer) component).setIcon(null);
				}
				
				return component;
			}
			
			@Override	// Implement table cell tool tips           
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int row = rowAtPoint(p);
				int column = columnAtPoint(p);
				String tip = (table9.getColumnName(column).equals("flow_description") && row >= 0 && getValueAt(row, column) != null) ? getValueAt(row, column).toString() : null;
				
				// Set tooltip for cells: when UB > LB		NOTE: it is OK to use the number for column because we do not allow changing column position (row could be changed due to the SORT button --> need to use getValueAt)
				double percentage_difference = Double.MAX_VALUE;
				if (getValueAt(row, 5) != null) percentage_difference = Double.parseDouble(getValueAt(row, 5).toString());
				if (getValueAt(row, 4) != null) percentage_difference = percentage_difference - Double.parseDouble(getValueAt(row, 4).toString());
				if (getValueAt(row, 3).toString().equals("HARD") && percentage_difference < 0 && column >= 4) {		// check if the LB - UB < 0
					tip = "INFEASIBLE (likely) - lowerbound_percentage should not exceed upperbound_percentage";
				}
				
				return tip;
			}			
		};

        // Set Double precision for cells
        DefaultTableCellRenderer r2 = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object
			value, boolean isSelected, boolean hasFocus, int row, int column) {			
				setHorizontalAlignment(JLabel.RIGHT);			
				DecimalFormat formatter = new DecimalFormat("###,###.###");
				formatter.setMinimumFractionDigits(0);
				formatter.setMaximumFractionDigits(10);	// show value with max 10 digits after the dot if it is double value
				if (value instanceof Double) {
					value = formatter.format((Number) value);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
             
		
		for (int i = 0; i < columnNames9.length; i++) {
			if (i < 4 || i > 5) {

			} else {
				table9.getColumnModel().getColumn(i).setCellRenderer(r2);
			}
		}
    

        // Set up Type for column 3
		table9.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new comboBox_constraint_type()));        
		table9.setAutoResizeMode(0);		// 0 = JTable.AUTO_RESIZE_OFF
		table9.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);  
		table9.getTableHeader().setReorderingAllowed(false);		//Disable columns move
		table9.setPreferredScrollableViewportSize(new Dimension(250, 100));
	}	
	
	
	//--------------------------------------------------------------------------------------------------------------------------
	public void create_table10() {
		class Combo_merging_method extends JComboBox {
			public Combo_merging_method() {
				addItem("exact");
				addItem("relaxed percentage (RP)");
				addItem("relaxed number (RN)");
				addItem("RP and RN");
				addItem("RP or RN");
				setSelectedIndex(0);
			}
		}
		
		
		//Setup the table------------------------------------------------------------	
		if (is_table10_loaded == false) { // Create a fresh new if Load fail
			String[] yield_tables_column_names = read_database.get_yield_tables_column_names();
			String[] yield_tables_column_types = read_database.get_yield_tables_column_types();
			
			rowCount10 = yield_tables_column_names.length;
			colCount10 = 9;
			data10 = new Object[rowCount10][colCount10];
			columnNames10 = new String[] {"attribute", "data_type", "min_value", "max_value", "unique_values", "merging_method", "relaxed_percentage", "relaxed_number", "implementation"};
			
			// Populate the data matrix
			for (int row = 0; row < rowCount10; row++) {
				List<String> unique_values_list = read_database.get_col_unique_values_list(row);	
				data10[row][0] = yield_tables_column_names[row];
				data10[row][1] = yield_tables_column_types[row];	
				data10[row][2] = unique_values_list.get(0);
				data10[row][3] = unique_values_list.get(unique_values_list.size() - 1);
				data10[row][4] = unique_values_list.size();
				data10[row][5] = "exact";
				data10[row][6] = null;
				data10[row][7] = null;
				data10[row][8] = false;
			}	
		}
					
		
		//Create a table-------------------------------------------------------------		
		model10 = new PrismTableModel(rowCount10, colCount10, data10, columnNames10) {
			@Override
			public Class getColumnClass(int c) {
				if (c <= 5) return String.class;
				else if (c == 6 || c == 7) return Double.class;
				else if (c == 8) return Boolean.class;
				else return String.class;				
			}
			
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col >= 5) {
					if (data10[row][1].equals("TEXT") && (col == 5 || col == 6 || col == 7)) {		// Not allowing edit when data_type is TEXT
						return false;
					}
					return true;
				} else {
					return false;
				}
			}
			
			@Override
			public void setValueAt(Object value, int row, int col) {
				if (value != null && (col == 6) && (((Number) value).doubleValue() < 0 || ((Number) value).doubleValue() > 100)) {	// allow null to be set
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Only null or double values in the range 0-100 (%) would be allowed.");
    			} else if (value != null && (col == 7) && ((Number) value).doubleValue() < 0) {		// allow null to be set, and not allow negative numbers
    				JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "Your input has not been accepted. Only null or positive values are allowed");
    			} else {
    				data10[row][col] = value;
    				// this is to address the case when the changes are made for only one cell. Without the if then, when we use quick edit to change multiple cells there would be many fireTableDataChanged() are called --> very slow
    				if (table10.getSelectedRows().length == 1) {	// only fire the change when the table has only one cell selected, need to store the current row and column before fire the change. Otherwise the re-selection would be fail
    					int currentRow = table10.getSelectedRow();		        	
    					int currentCol = table10.getSelectedColumn();	
    					fireTableDataChanged();		// This will clear the selection 
    					table10.changeSelection(currentRow, currentCol, true, false);	// This will add the selection back (reselect previous cell)
    				}
    			}
			}
			
			@Override
			public void match_DataType() {
				for (int row = 0; row < rowCount10; row++) {
					for (int col = 0; col < colCount10; col++) {
						if (String.valueOf(data10[row][col]).equals("null")) {
							data10[row][col] = null;
						} else {					
							if (col == 6 || col == 7) {	// Double
								try {
									data10[row][col] = Double.valueOf(String.valueOf(data10[row][col]));
								} catch (NumberFormatException e) {
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Double values in create_table10");
								}	
							} else if (col == 8) {
								try {
									data10[row][col] = Boolean.valueOf(String.valueOf(data10[row][col]));
								} catch(NumberFormatException e){
									System.err.println(e.getClass().getName() + ": " + e.getMessage() + " Fail to convert String to Boolean values in create_table10");
								}
							} else {	//All other columns are String
								data10[row][col] = String.valueOf(data10[row][col]);
							}
						}	
					}	
				}	
			}
		};
		
		
		
		table10 = new JTable(model10) {
			@Override		// These override is to make the width of the cell fit all contents of the cell
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// For the cell width								
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
				
				// For the column header width
				TableCellRenderer renderer2 = table10.getTableHeader().getDefaultRenderer();	
				Component component2 = renderer2.getTableCellRendererComponent(table10, tableColumn.getHeaderValue(), false, false, -1, column);
				maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
				
				if (column == 0 || column == 2 || column == 3) {
					tableColumn.setMinWidth(100);
				} else if (column == 5) {
					tableColumn.setMinWidth(160);
				} else {
					tableColumn.setPreferredWidth(maxWidth);
				}
								
				// Set icon for cells
				if (column == 5) {
					if (getValueAt(row, 5) == null || getValueAt(row, 5).toString().equals("exact")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_red.png"));
					} else if (getValueAt(row, 5).toString().equals("relaxed percentage (RP)")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_orange.png"));
					} else if (getValueAt(row, 5).toString().equals("relaxed number (RN)")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_yellow.png"));
					} else if (getValueAt(row, 5).toString().equals("RP and RN")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_green.png"));
					} else if (getValueAt(row, 5).toString().equals("RP or RN")) {
						((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_blue.png"));
					}else {
						((DefaultTableCellRenderer) component).setIcon(null);
					}
				}
						
				return component;
			}
		};
		
		
		// Set Color for text in some columns where data_type = "TEXT"
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
        	Color default_color = getForeground();
            @Override
            public Component getTableCellRendererComponent(JTable table, Object	value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setForeground(table.getValueAt(row, 1).toString().equals("TEXT") ? Color.RED : default_color);
                return this;
            }
        };
		for (int col = 0; col <= 1; col++) {
			 table10.getColumnModel().getColumn(col).setCellRenderer(r);
		}

        
		// Set up checkbox and combobox for column 4 and 7
		table10.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new Combo_merging_method()));
		((JComponent) table10.getDefaultRenderer(Boolean.class)).setOpaque(true);	// It's a bug in the synth-installed renderer, quick hack is to force the rendering checkbox opacity to true
		((AbstractButton) table10.getDefaultRenderer(Boolean.class)).setSelectedIcon(IconHandle.get_scaledImageIcon(12, 12, "icon_check.png"));
		
		
		table10.setAutoResizeMode(0);		// 0 = JTable.AUTO_RESIZE_OFF
		table10.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);  
		table10.getTableHeader().setReorderingAllowed(false);		//Disable columns move
		table10.setPreferredScrollableViewportSize(new Dimension(400, 20));
	}
	
	
	
	
	
    //--------------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------  
    //--------------------------------------------------------------------------------------------------------------------------
    //----------------------------------Functions to Create Repetitive Buttons and Functions-------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------	
	// Mass Check model_condition Button
	private JButton create_mass_check_button(JPanel panel, GridBagConstraints c, int gridx, int gridy, int weightx,	int weighty) {
		JButton button_check = new JButton();
		button_check.setText("check");
		button_check.setVerticalTextPosition(SwingConstants.BOTTOM);
		button_check.setHorizontalTextPosition(SwingConstants.CENTER);
//		button_check.setToolTipText("check");
		button_check.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_check.png"));
		button_check.setRolloverIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_check.png"));
		button_check.setContentAreaFilled(false);
		c.gridx = gridx;
		c.gridy = gridy;
		c.weightx = weightx;
		c.weighty = weighty;
		panel.add(button_check, c);
		return button_check;
	}
	
	// Mass Uncheck model_condition Button
	private JButton create_mass_uncheck_button(JPanel panel, GridBagConstraints c, int gridx, int gridy, int weightx, int weighty) {
		JButton button_uncheck = new JButton();
		button_uncheck.setText("uncheck");
		button_uncheck.setVerticalTextPosition(SwingConstants.BOTTOM);
		button_uncheck.setHorizontalTextPosition(SwingConstants.CENTER);
//		button_uncheck.setToolTipText("uncheck");
		button_uncheck.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_uncheck.png"));
		button_uncheck.setRolloverIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_uncheck.png"));
		button_uncheck.setContentAreaFilled(false);
		c.gridx = gridx;
		c.gridy = gridy;
		c.weightx = weightx;
		c.weighty = weighty;
		panel.add(button_uncheck, c);
		return button_uncheck;
	}

	// Apply check or uncheck
	private void apply_mass_check_or_uncheck(String check_option, PrismTableModel model, JTable table, Object[][] data, int colCount) {		
		boolean check_or_uncheck = check_option.equals("mass_check") ? true : false;		
		int[] selectedRow = table.getSelectedRows();
		for (int i = 0; i < selectedRow.length; i++) {
			selectedRow[i] = table.convertRowIndexToModel(selectedRow[i]);		// convert row index because "Sort" causes problems
		}
		table.clearSelection();    //To help trigger the row refresh: clear then add back the rows
		for (int i : selectedRow) {
			data[i][colCount - 1] = check_or_uncheck;
			table.addRowSelectionInterval(table.convertRowIndexToView(i), table.convertRowIndexToView(i));
		}
		if (table == table3) model3.update_model_overview();	// Do not remove this line because it would deselect strata without NG_E_0 prescription. This is important
	}

	// Sort Button
	private JToggleButton create_sort_button(JPanel panel, GridBagConstraints c, int gridx, int gridy, int weightx, int weighty) { 
		JToggleButton button_sort = new JToggleButton();
		button_sort.setSelected(false);
		button_sort.setFocusPainted(false);
		button_sort.setFont(new Font(null, Font.BOLD, 12));
		button_sort.setText("OFF");
		button_sort.setToolTipText("Sorter mode: 'ON' click columns header to sort rows. 'OFF' retrieve original rows position");
		button_sort.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_sort.png"));
		c.gridx = gridx;
		c.gridy = gridy;
		c.weightx = weightx;
		c.weighty = weighty;
		panel.add(button_sort, c);
		return button_sort;
	};
	
	// Apply sort or nosort
	private void apply_sort_or_nosort(JToggleButton btn, PrismTableModel model, JTable table) {
		if (table.isEditing()) {
			table.getCellEditor().stopCellEditing();
		}
		if (btn.getText().equals("ON")) {
			table.setRowSorter(null);
			btn.setText("OFF");
			btn.repaint();
		} else if (btn.getText().equals("OFF")) {
			TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>(model); // Add sorter
			table.setRowSorter(sorter);
			btn.setText("ON");
			btn.repaint();
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------



	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------	
	class General_Inputs_GUI extends JLayeredPane {
		private JLabel totalPeriodsLabel, replacingDisturbancesLabel, discountRateLabel, solverLabel, solvingTimeLabel, exportProblemLabel, exportSolutionLabel;
		private JComboBox totalPeriodsCombo, replacingDisturbancesCombo, discountRateCombo, solverCombo;
		private JSpinner solvingTimeSpinner;
		private JCheckBox exportProblemCheck, exportSolutionCheck;
		private JTextField database_directory_textfield;
		
		public General_Inputs_GUI() {
			setLayout(new GridBagLayout());
			create_table1();
			
			
			//-----------------------------------------------------
			totalPeriodsLabel = new JLabel("Total planning periods (decades)");
			totalPeriodsCombo = new JComboBox();		
			for (int i = 1; i <= 99; i++) {
				totalPeriodsCombo.addItem(i);
			}
			//-----------------------------------------------------
			replacingDisturbancesLabel = new JLabel("Total replacing disturbances");
			replacingDisturbancesCombo = new JComboBox();		
			for (int i = 1; i <= 99; i++) {
				replacingDisturbancesCombo.addItem(i);
			}
			//-----------------------------------------------------
			discountRateLabel = new JLabel("Annual discount rate (%)");
			discountRateCombo = new JComboBox();		
			for (int i = 0; i <= 99; i++) {
				double value = (double) i / 10;
				discountRateCombo.addItem(value);
			}
			//-----------------------------------------------------						
			solverLabel = new JLabel("Solver for optimization");
			class DisabledJComboBoxRenderer extends BasicComboBoxRenderer {	// I played a trick with this class by changing Enabled List to Disabled List, The reason is to show my desired color for items in the combo boxes.
				private final ListSelectionModel disabledItems;

				// Constructs a new renderer for a JComboBox which enables/disables items based upon the parameter model.
				public DisabledJComboBoxRenderer(ListSelectionModel disabled) {
					super();
					this.disabledItems = disabled;
				}

				// Custom implementation to color items as enabled or disabled.
				@Override
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

					if (disabledItems.isSelectedIndex(index)) { // not enabled
						if (isSelected) {
							c.setBackground(UIManager.getColor("Tree.selectionBackground"));	// this works to have the desired color
							c.setForeground(Color.RED);
						} else {
							c.setBackground(super.getBackground());
							c.setForeground(Color.LIGHT_GRAY);
						}
					} else {
						if (isSelected) {
							c.setBackground(UIManager.getColor("Tree.selectionBackground"));	// this works to have the desired color
							c.setForeground(Color.WHITE);
						} else {
							c.setBackground(super.getBackground());
							c.setForeground(Color.BLACK);
						}
					}
					
					if (String.valueOf(value).equals("CPLEX")) {
						list.setToolTipText("active when CPLEX jar and dll files are properly set up");
					} else if (String.valueOf(value).equals("LPSOLVE")) {
						list.setToolTipText("active");
					} else {
						list.setToolTipText("inactive. " + String.valueOf(value) + " will be integrated in future PRISM updates");
					}
					
					return c;
				}
			}

			solverCombo = new JComboBox() {
				@Override
				public void setSelectedIndex(int index) {
					if (index <= 1) {
						super.setSelectedIndex(index);	// 0 and 1 are Cplex and Lpsolve, if other solver is selected --> do not change the current selected item
					}
				}
			};
			solverCombo.addItem("CPLEX");
			solverCombo.addItem("LPSOLVE");
			solverCombo.addItem("CBC");
			solverCombo.addItem("CLP");
			solverCombo.addItem("GUROBI");
			solverCombo.addItem("GLPK");
			solverCombo.addItem("SPCIP");
			solverCombo.addItem("SOPLEX");
			solverCombo.addItem("XPRESS");	
			DefaultListSelectionModel model = new DefaultListSelectionModel();
			model.addSelectionInterval(2, 8);	// These are disabled items in the Combo Box
			DisabledJComboBoxRenderer disableRenderer = new DisabledJComboBoxRenderer(model);
			solverCombo.setRenderer(disableRenderer);	
			//-----------------------------------------------------
			solvingTimeLabel = new JLabel("Maximum solving time (minutes)");
			solvingTimeSpinner = new JSpinner (new SpinnerNumberModel(20, 1, 9999, 1));
			JFormattedTextField SpinnerText = ((DefaultEditor) solvingTimeSpinner.getEditor()).getTextField();
			SpinnerText.setHorizontalAlignment(JTextField.LEFT);
			//-----------------------------------------------------
			exportProblemLabel = new JLabel("Export original problem file (.lp)");
			exportProblemCheck = new JCheckBox();
			//-----------------------------------------------------
			exportSolutionLabel = new JLabel("Export original solution file (.sol)");
			exportSolutionCheck = new JCheckBox();
			//-----------------------------------------------------
			// ToolBar Panel
			ToolBarWithBgImage helpToolBar = new ToolBarWithBgImage("Project Tools", JToolBar.HORIZONTAL, null);
			helpToolBar.setFloatable(false);	//to make a tool bar immovable
			helpToolBar.setRollover(true);	//to visually indicate tool bar buttons when the user passes over them with the cursor
			helpToolBar.setBorderPainted(false);
									
			// button Help
			JButton btnHelp = new JButton();
			btnHelp.setToolTipText("Help");
			btnHelp.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_help.png"));
			btnHelp.addActionListener(e -> {
								
			});
			
			// Add all buttons to helpToolBar
			helpToolBar.add(Box.createGlue());	//Add glue for Right alignment
			helpToolBar.add(btnHelp);
			//-----------------------------------------------------
			// Load info from input to GUI
			total_period = Integer.valueOf((String) data1[0][1]);		
			total_replacing_disturbance = Integer.valueOf((String) data1[1][1]);
			totalPeriodsCombo.setSelectedItem(Integer.valueOf((String) data1[0][1]));
			replacingDisturbancesCombo.setSelectedItem(Integer.valueOf((String) data1[1][1]));
			discountRateCombo.setSelectedItem(Double.valueOf((String) data1[2][1]));
			solverCombo.setSelectedItem(String.valueOf(data1[3][1]));
			solvingTimeSpinner.setValue(Integer.valueOf((String) data1[4][1]));					
			exportProblemCheck.setSelected(Boolean.valueOf(String.valueOf(data1[5][1])));
			exportSolutionCheck.setSelected(Boolean.valueOf(String.valueOf(data1[6][1])));
			//-----------------------------------------------------
			
			
			// Add listener
			Action apply = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					total_period = Integer.parseInt(totalPeriodsCombo.getSelectedItem().toString());
					replacingDisturbancesCombo.getUI().setPopupVisible(replacingDisturbancesCombo, false);	// This would close the drop down to avoid 1 click to close it
					int total_replacing_disturbance_combo_value = Integer.parseInt(replacingDisturbancesCombo.getSelectedItem().toString());
					if (total_replacing_disturbance_combo_value != total_replacing_disturbance) { 
						String ExitOption[] = {"Apply", "Cancel"};
						String message = (total_replacing_disturbance > total_replacing_disturbance_combo_value)
								? "You are decreasing the total number of stand replacing disturbances.\n"
										+ "Some data in the SR Disturbances screen will be removed and can not be reverted.\n"
										+ "Apply change?"
								: "You are increasing the total number of stand replacing disturbances.\n"
										+ "The added disturbances are ready to be defined in the SR Disturbances screen.\n"
										+ "Apply change?";
						int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), message, "Change the total number of stand replacing disturbances",
								JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[0]);
						if (response == 0) {
							for (int i = 0; i < rowCount6; i++) {
								data6[i][2] = get_adjusted_replacing_disturbances_infor(total_replacing_disturbance_combo_value, data6[i][2], "0");	// loss rate mean
								data6[i][3] = get_adjusted_replacing_disturbances_infor(total_replacing_disturbance_combo_value, data6[i][3], "0");	// loss rate std
								data6[i][4] = get_adjusted_replacing_disturbances_infor(total_replacing_disturbance_combo_value, data6[i][4], "0");	// conversion rate mean
								data6[i][5] = get_adjusted_replacing_disturbances_infor(total_replacing_disturbance_combo_value, data6[i][5], "0");	// conversion rate std
							}
				    		total_replacing_disturbance = total_replacing_disturbance_combo_value;
				    		is_table6_loaded = true;	// to have the old data stay (create_table6 would not create new empty data)
				    		panel_SR_Disturbances_GUI = new SR_Disturbances_GUI();	// Renew the entire SR_Disturbances screen
						} else {
							replacingDisturbancesCombo.setSelectedItem((int) total_replacing_disturbance);
						}
					} 
					
					// Apply any change in the GUI to the table
					data1[0][1] = totalPeriodsCombo.getSelectedItem().toString();	
					data1[1][1] = replacingDisturbancesCombo.getSelectedItem().toString();
					data1[2][1] = discountRateCombo.getSelectedItem().toString();
					data1[3][1] = solverCombo.getSelectedItem().toString();
					data1[4][1] = (Integer) solvingTimeSpinner.getValue();
					data1[5][1] = (exportProblemCheck.isSelected()) ? "true" : "false";
					data1[6][1] = (exportSolutionCheck.isSelected()) ? "true" : "false";
					model1.fireTableDataChanged();
				}
				
				public Object get_adjusted_replacing_disturbances_infor(int total_replacing_disturbance_combo_value, Object data6_cell, String new_value) {
					String cell_info = (String) data6_cell;
					if (cell_info.length() > 0) {
						String[] info_array = cell_info.split(";");	
						String[] info_array_adjusted = new String[info_array.length];
						for (int row = 0; row < info_array.length; row++) {			
							String[] sub_info = info_array[row].split(" ");
							String[] sub_info_adjusted = new String[2 + total_replacing_disturbance_combo_value];
							if (total_replacing_disturbance > total_replacing_disturbance_combo_value) {	// Case when decreasing the number of SRs --> Clear the unused data/column (keep the old data)
								for (int col = 0; col < 2 + total_replacing_disturbance_combo_value; col++) {
									sub_info_adjusted[col] = sub_info[col];
								}
							} else {	// Case when increasing the number of SRs --> Clear the unused data/column (keep the old data)
								for (int col = 0; col < 2 + total_replacing_disturbance; col++) {
									sub_info_adjusted[col] = sub_info[col];
								}
								for (int col = 2 + total_replacing_disturbance; col < 2 + total_replacing_disturbance_combo_value; col++) {
									sub_info_adjusted[col] = new_value;
								}
							}
							info_array_adjusted[row] = String.join(" ", sub_info_adjusted);		
						} 
						String cell_info_adjusted = String.join(";", info_array_adjusted);
						return cell_info_adjusted;
					}
					return cell_info;
				}
			};
			totalPeriodsCombo.addActionListener(apply);
			replacingDisturbancesCombo.addActionListener(apply);
			discountRateCombo.addActionListener(apply);
			solverCombo.addActionListener(apply);
			exportProblemCheck.addActionListener(apply);
			exportSolutionCheck.addActionListener(apply);
			
			
			DefaultFormatter formatter = (DefaultFormatter) SpinnerText.getFormatter();
		    formatter.setCommitsOnValidEdit(true);
		    solvingTimeSpinner.addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
		        	solvingTimeSpinner.setValue(solvingTimeSpinner.getValue());
		        	total_period = Integer.parseInt(totalPeriodsCombo.getSelectedItem().toString());
		        	total_replacing_disturbance = Integer.parseInt(replacingDisturbancesCombo.getSelectedItem().toString());
		        	
		        	// Apply any change in the GUI to the table
		        	data1[0][1] = totalPeriodsCombo.getSelectedItem().toString();	
					data1[1][1] = replacingDisturbancesCombo.getSelectedItem().toString();
					data1[2][1] = discountRateCombo.getSelectedItem().toString();
					data1[3][1] = solverCombo.getSelectedItem().toString();
					data1[4][1] = (Integer)solvingTimeSpinner.getValue();
					data1[5][1] = (exportProblemCheck.isSelected()) ? "true" : "false";
					data1[6][1] = (exportSolutionCheck.isSelected()) ? "true" : "false";
					model1.fireTableDataChanged();
		        }
		    });
		    

		    
		    
		    
		    
		    // Import Database Panel -----------------------------------------------------------------------
		 	// Import Database Panel -----------------------------------------------------------------------
			JPanel importPanel = new JPanel();
			importPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			database_directory_textfield = new JTextField();
			database_directory_textfield.setEditable(false);
			importPanel.add(database_directory_textfield, PrismGridBagLayoutHandle.get_c(c, "HORIZONTAL", 
					0, 0, 1, 1, 1, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
					0, 0, 0, 0));	// insets top, left, bottom, right	
			
			
			
			
			JButton button_import_database = new JButton();
			button_import_database.setText("Browse");
			button_import_database.setVerticalTextPosition(SwingConstants.BOTTOM);
			button_import_database.setHorizontalTextPosition(SwingConstants.CENTER);
			button_import_database.setIcon(IconHandle.get_scaledImageIcon(40, 40, "icon_browse.png"));	
			button_import_database.setRolloverIcon(IconHandle.get_scaledImageIcon(48, 48, "icon_browse.png"));
			button_import_database.setContentAreaFilled(false);
			button_import_database.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Thread thread = new Thread() {		// Not use join thread, if use then all the code after thread start will have to wait for the thread finished to be implemented
						public void run() {
							File old_database = file_database;
							
							try {	
								button_import_database.setEnabled(false);
								radio_button[1].setEnabled(false);
								radio_button[2].setEnabled(false);
								radio_button[3].setEnabled(false);
								radio_button[4].setEnabled(false);
								radio_button[5].setEnabled(false);
								radio_button[6].setEnabled(false);
								radio_button[7].setEnabled(false);
								radio_button[8].setEnabled(false);
								radio_button[9].setEnabled(false);
								
								File new_database = FilesHandle.chosenDatabase();
								if (new_database != null) {
									file_database = new_database;
									change_database();
								}
							} catch (Exception e) {
								e.printStackTrace();
								String warningText = "Importation failed. \"" + file_database.getName() + "\" needs revision.\n";
								warningText = warningText + "Data will be reverted to your last save.";
								String ExitOption[] = {"OK"};
								int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), warningText, "Database importation warning",
										JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_warning.png"), ExitOption, ExitOption[0]);
								
								// revert when changing database fails
								file_database = old_database;
								reload_inputs();
							} finally {
								button_import_database.setEnabled(true);
								if (file_database != null) {
									radio_button[1].setEnabled(true);
									radio_button[2].setEnabled(true);
									radio_button[3].setEnabled(true);
									radio_button[4].setEnabled(true);
									radio_button[5].setEnabled(true);
									radio_button[6].setEnabled(true);
									radio_button[7].setEnabled(true);
									radio_button[8].setEnabled(true);
									radio_button[9].setEnabled(true);
									database_directory_textfield.setText(file_database.getAbsolutePath());
								}
							}
							
							// remember the new database when the database change is successful and the database is not remembered yet
							if (Prism2Main.get_databases_linkedlist().return_read_database_if_exist(file_database) == null) {
								Prism2Main.get_databases_linkedlist().update(file_database, read_database);	
							}		
									
							this.interrupt();
						}
					};					
					thread.start();	
				}

				private void change_database() {
					// read the tables (strata_definition, existing_strata, yield_tables) of the database-------------------
					if (Prism2Main.get_databases_linkedlist().return_read_database_if_exist(file_database) != null) {
						read_database = Prism2Main.get_databases_linkedlist().return_read_database_if_exist(file_database);
					} else {
						read_database = new Read_Database(file_database);	// Read the database
					}
					
					// Reset all panels except General Inputs----------------------------------------------------------------		
					is_table_overview_loaded = false;
					is_table1_loaded = false;
					is_table2_loaded = false;
					is_table3_loaded = false;
					is_table4_loaded = false;
					is_table4a_loaded = false;
					is_table5_loaded = false;
					is_table6_loaded = false;
					is_table7a_loaded = false;
					is_table7b_loaded = false;
					is_table7_loaded = false;
					is_table8_loaded = false;
					is_table9_loaded = false;
					is_table10_loaded = false;
							
					// create new instances
					panel_Model_Strata_GUI = new Model_Strata_GUI();
					panel_Non_EA_Management_GUI = new Non_EA_Management_GUI();
					panel_EA_Management_GUI = new EA_Management_GUI();
					panel_Non_SR_Disturbances_GUI = new Non_SR_Disturbances_GUI();
					panel_SR_Disturbances_GUI = new SR_Disturbances_GUI();
					panel_Management_Cost_GUI = new Management_Cost_GUI();
					panel_Basic_Constraints_GUI = new Basic_Constraints_GUI();
					panel_Flow_Constraints_GUI = new Flow_Constraints_GUI();
					panel_Area_Merging_GUI = new Area_Merging_GUI();
					
					// We do not need match data type here. Note that  model3.match_DataType has the update view for table_overview --> we manually update the info of this tale by following line
					model3.update_model_overview();		// this is just to trigger the update_model_overview
					
					// update readme.txt in General Inputs
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   -   HH:mm:ss");
					readme.setText(null);
					readme.append("Model is last edited by:     " + Prism2Main.get_prism_version()  + "     on     " + dateFormat.format(new Date()) + "\n");
					readme.append("Model is created by:     " + Prism2Main.get_prism_version()   + "     on     " + dateFormat.format(new Date()) + "\n");
					readme.append("Model location:     " + currentRunFolder + "\n");
					readme.append("Model database:     " + currentRunFolder.getAbsolutePath() + "\\database.db" + "\n");
					readme.append("Original database:     " + file_database.getAbsolutePath() + "\n");
					readme.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
					readme.append("----------------------------------------------------- ADDITIONAL MODEL DESCRIPTION -----------------------------------------------------\n");
					readme.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
				}
			});	

			importPanel.add(button_import_database, PrismGridBagLayoutHandle.get_c(c, "HORIZONTAL", 
					1, 0, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
					0, 0, 4, 0));	// insets top, left, bottom, right	
						
			// Add empty Label for everything above not resize
			importPanel.add(new JLabel(), PrismGridBagLayoutHandle.get_c( c, "HORIZONTAL", 
					0, 1, 1, 1, 0, 1, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
					0, 0, 0, 0));		// insets top, left, bottom, right	


			// End of Import Database Panel -----------------------------------------------------------------------
			// End of Import Database Panel -----------------------------------------------------------------------		    
		    
		    
			
		    // Load readme file-----------------------------------------------------------------
 			// Load readme file-----------------------------------------------------------------
			File readme_file = new File(currentRunFolder.getAbsolutePath() + "/readme.txt");
			readme.activate_clicktosave_feature(readme_file);
			try {
				FileReader reader = new FileReader(readme_file.getAbsolutePath());
				readme.read(reader, readme_file);
				reader.close();
			} catch (IOException e1) {
				System.err.println("File not exists: readme.txt - New interface is created");
				readme.append("Browse & Import a database before writting here");
			}
			PrismTitleScrollPane readme_scrollpane = new PrismTitleScrollPane("Model Description", "CENTER", readme);
 			readme_scrollpane.setPreferredSize(new Dimension((int) (Prism2Main.get_main().getPreferredSize().width * 0.55), 100));
 			// End of Load readme file-----------------------------------------------------------------
 			// End of Load readme file-----------------------------------------------------------------
		    
		        

 			
		    
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
 		    // Add	
			super.add(helpToolBar, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
					0, 0, 6, 1, 1, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
					0, 0, 0, 0));		// insets top, left, bottom, right
		    		    
 			// Add 	
			super.add(totalPeriodsLabel, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					0, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right
 									
 			// Add	
 			super.add(totalPeriodsCombo, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					1, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right		
 			
 			// Add 
 			super.add(replacingDisturbancesLabel, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					0, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right	

 			// Add 
 			super.add(replacingDisturbancesCombo, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					1, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right	
	
 			// Add 
 			super.add(discountRateLabel, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					0, 3, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right	
 			
 			// Add 
 			super.add(discountRateCombo, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					1, 3, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right
 			
 			// Add 
 			super.add(solverLabel, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					0, 4, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right	

 			// Add 
 			super.add(solverCombo, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					1, 4, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right
		
 			// Add 
 			super.add(solvingTimeLabel, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					0, 5, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right	

 			// Add 
 			super.add(solvingTimeSpinner, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					1, 5, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right			
 			
 			// Add 
 			super.add(exportProblemLabel, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					2, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right
 			
 			// Add 
 			super.add(exportProblemCheck, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					3, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right
 			
 			// Add 
 			super.add(exportSolutionLabel, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					2, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right
 			
 			// Add 
 			super.add(exportSolutionCheck, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					3, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 12, 10, 30));		// insets top, left, bottom, right

 			// Add 
 			super.add(new JLabel("Import database - Data will be reset to default (successful) or reverted to your last save (fail)"), PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					0, 6, 4, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					5, 12, 0, 30));		// insets top, left, bottom, right
 						
 			// Add 
 			super.add(importPanel, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					0, 7, 4, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 10, 10, 30));		// insets top, left, bottom, right
 						
 			// Add 
 			super.add(readme_scrollpane, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					0, 8, 5, 1, 0, 1, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 5, 0, 30));		// insets top, left, bottom, right
		}
		
		private JTextField get_database_directory_textfield() {
			return database_directory_textfield;
		}
	}

	
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------------------------------------------------

	class Model_Strata_GUI extends JLayeredPane implements ItemListener {
		List<List<JCheckBox>> checkboxStaticIdentifiers;
		ScrollPane_StaticIdentifiers static_identifiers_scrollpane;
		
		Panel_QuickEdit_ModelStrata quick_edit;
		JScrollPane scrollpane_QuickEdit;
		
		public Model_Strata_GUI() {
			setLayout(new BorderLayout());
			// 1st grid -----------------------------------------------------------------------
			// 1st grid -----------------------------------------------------------------------	
			String panel_name = "Strata Attributes - Filter";
			static_identifiers_scrollpane = new ScrollPane_StaticIdentifiers(read_database, 0, panel_name);
			checkboxStaticIdentifiers = static_identifiers_scrollpane.get_CheckboxStaticIdentifiers();	
			
			for (int i = 0; i < checkboxStaticIdentifiers.size(); i++) {
				for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
					checkboxStaticIdentifiers.get(i).get(j).setSelected(true);
					checkboxStaticIdentifiers.get(i).get(j).addItemListener(this);
				}
			}
			// End of 1st grid -----------------------------------------------------------------------
			// End of 1st grid -----------------------------------------------------------------------

			
			
			
			// 2nd grid -----------------------------------------------------------------------
			// 2nd grid -----------------------------------------------------------------------			
			create_table_overview();      
			PrismTitleScrollPane overviewScrollPane = new PrismTitleScrollPane("Model Overview", "CENTER", table_overview);
			overviewScrollPane.setPreferredSize(new Dimension(0, 250));
			// End of 2nd grid -----------------------------------------------------------------------
			// End of 2nd grid -----------------------------------------------------------------------
			
					
			
			// 3rd grid -----------------------------------------------------------------------
			// 3rd grid -----------------------------------------------------------------------
			create_table3();	
			table3.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
				public void valueChanged(ListSelectionEvent event) {
					int[] selectedRow = table3.getSelectedRows();	
					for (int i = 0; i < selectedRow.length; i++) {
						selectedRow[i] = table3.convertRowIndexToModel(selectedRow[i]);	///Convert row index because "Sort" causes problems
					}
					
					int highlighted_strata = 0;
					double highlighted_acres = 0;
					for (int i: selectedRow) {
						highlighted_strata++;
						highlighted_acres = highlighted_acres + Double.parseDouble(data3[i][colCount3 - 3].toString());
					}	

					DecimalFormat formatter = new DecimalFormat("###,###.###");
					formatter.setMinimumFractionDigits(0);
//					formatter.setMaximumFractionDigits(10);	// show value with max 10 digits after the dot if it is double value						        
					data_overview[2][1] = highlighted_strata + "   --o--   " + formatter.format((Number) highlighted_acres);
					model_overview.fireTableDataChanged();
				}
			});
			 
			PrismTitleScrollPane table_scrollPane = new PrismTitleScrollPane(
					"Existing strata at the start of planning horizon. This model includes only strata with green checks", "CENTER", table3);
			// End of 3rd grid -----------------------------------------------------------------------
			// End of 3rd grid -----------------------------------------------------------------------
			
					
			
			
			
			// 2 buttons------------------------------------------------------------------------------
			// 2 buttons------------------------------------------------------------------------------
			// button 1
			JButton button_remove_Strata = new JButton();
			button_remove_Strata.setText("uncheck");
			button_remove_Strata.setVerticalTextPosition(SwingConstants.BOTTOM);
			button_remove_Strata.setHorizontalTextPosition(SwingConstants.CENTER);
			button_remove_Strata.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_uncheck.png"));
			button_remove_Strata.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_uncheck.png"));
			button_remove_Strata.setContentAreaFilled(false);
			button_remove_Strata.addActionListener(e -> apply_mass_check_or_uncheck("mass_uncheck", model3, table3, data3, colCount3));
			
			// button 2	
			JButton button_select_Strata = new JButton();
			button_select_Strata.setText("check");
			button_select_Strata.setVerticalTextPosition(SwingConstants.BOTTOM);
			button_select_Strata.setHorizontalTextPosition(SwingConstants.CENTER);
			button_select_Strata.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_check.png"));
			button_select_Strata.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_check.png"));
			button_select_Strata.setContentAreaFilled(false);
			button_select_Strata.addActionListener(e -> apply_mass_check_or_uncheck("mass_check", model3, table3, data3, colCount3));

			// End of 2 buttons------------------------------------------------------------------------------
			// End of 2 buttons------------------------------------------------------------------------------
						
			
			
			
			// scrollPane Quick Edit ----------------------------------------------------------------------	
			// scrollPane Quick Edit ----------------------------------------------------------------------	
			quick_edit = new Panel_QuickEdit_ModelStrata(table3, data3);
			quick_edit.setLayout(new FlowLayout());
			quick_edit.add(button_select_Strata);
			quick_edit.add(button_remove_Strata);		
 			scrollpane_QuickEdit = new JScrollPane(quick_edit);
 			TitledBorder border = new TitledBorder("Quick Edit");
 			border.setTitleJustification(TitledBorder.CENTER);
 			scrollpane_QuickEdit.setBorder(border);
 			scrollpane_QuickEdit.setVisible(false);	
 			
 			
			
			
			// ToolBar Panel ----------------------------------------------------------------------------
			// ToolBar Panel ----------------------------------------------------------------------------
			ToolBarWithBgImage helpToolBar = new ToolBarWithBgImage("Project Tools", JToolBar.HORIZONTAL, null);
			helpToolBar.setFloatable(false);	//to make a tool bar immovable
			helpToolBar.setRollover(true);	//to visually indicate tool bar buttons when the user passes over them with the cursor
			helpToolBar.setBorderPainted(false);
			
			// button Quick Edit
			JToggleButton btnQuickEdit = new JToggleButton();
			btnQuickEdit.setToolTipText("Show Quick Edit Tool");
			btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
			btnQuickEdit.addActionListener(e -> {
				if (btnQuickEdit.getToolTipText().equals("Show Quick Edit Tool")) {
						btnQuickEdit.setToolTipText("Hide Quick Edit Tool");
						btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_hide.png"));
						scrollpane_QuickEdit.setVisible(true);
						// Get everything show up nicely
						GUI_Text_splitpane.setLeftComponent(panel_Model_Strata_GUI);
						Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				} else {
					btnQuickEdit.setToolTipText("Show Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
					scrollpane_QuickEdit.setVisible(false);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_Model_Strata_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				}
			});
			
			// button Help
			JButton btnHelp = new JButton();
			btnHelp.setToolTipText("Help");
			btnHelp.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_help.png"));
			btnHelp.addActionListener(e -> {

			});
			
			// Add all buttons to flow_panel
			helpToolBar.add(Box.createGlue());	//Add glue for Right alignment
			helpToolBar.add(btnQuickEdit);
			helpToolBar.add(btnHelp);
			// End of ToolBar Panel -----------------------------------------------------------------------
			// End of ToolBar Panel -----------------------------------------------------------------------
					
			
			
			
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			JSplitPane split_pane = new JSplitPane();
			split_pane.setBorder(null);
			split_pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			split_pane.setDividerSize(3);
						
			JPanel upper_panel = new JPanel();
			upper_panel.setBorder(null);
			upper_panel.setLayout(new GridBagLayout());			
			
			JPanel lower_panel = new JPanel();
			lower_panel.setBorder(null);
			lower_panel.setLayout(new GridBagLayout());
						
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
		    c.weighty = 1;
		    	    
		    // Add helpToolBar	
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0;
		    c.weighty = 0;
			c.gridwidth = 2;
			c.gridheight = 1;
			upper_panel.add(helpToolBar, c);				
			
			// Add the 1st grid - checkPanel to the main Grid
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 0.4;
		    c.weighty = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			upper_panel.add(static_identifiers_scrollpane, c);
			
			// Add the 2nd grid - overviewScrollPane to the main Grid	
			c.gridx = 1;
			c.gridy = 1;
			c.weightx = 0.5;
		    c.weighty = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			upper_panel.add(overviewScrollPane, c);
									
			// Add the table_scrollPane & scrollpane_QuickEdit to a new Panel then add that panel to the main Grid
			JPanel table_qedit_panel = new JPanel();
			table_qedit_panel.setLayout(new BorderLayout());
			table_qedit_panel.add(table_scrollPane, BorderLayout.CENTER);
			table_qedit_panel.add(scrollpane_QuickEdit, BorderLayout.EAST);			
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 2; 
			c.gridheight = 1;
			c.weightx = 1;
		    c.weighty = 1;
		    lower_panel.add(table_qedit_panel, c);
			

			split_pane.setLeftComponent(upper_panel);
			split_pane.setRightComponent(lower_panel);
			super.add(split_pane, BorderLayout.CENTER);			
		}
		
		
		//Listeners for checkBox Filter--------------------------------------------------------------------
		public void itemStateChanged(ItemEvent e) {

			if (data3 != null) {		//Only allow sorter if the data of existing strata is loaded
				//This help filter to get the strata as specified by the CheckBoxes
				TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>(model3);
				table3.setRowSorter(sorter);
				List<RowFilter<PrismTableModel, Object>> filters, filters2;
				filters2 = new ArrayList<RowFilter<PrismTableModel, Object>>();
				for (int i = 0; i < checkboxStaticIdentifiers.size(); i++) {
					RowFilter<PrismTableModel, Object> layer_filter = null;
					filters = new ArrayList<RowFilter<PrismTableModel, Object>>();
					for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
						if (checkboxStaticIdentifiers.get(i).get(j).isSelected()) {
							filters.add(RowFilter.regexFilter(checkboxStaticIdentifiers.get(i).get(j).getText(), i + 1)); // i+1 is the table column containing the first layer	
						}
					}
					layer_filter = RowFilter.orFilter(filters);

					filters2.add(layer_filter);
				}
				RowFilter<PrismTableModel, Object> combine_AllFilters = null;
				combine_AllFilters = RowFilter.andFilter(filters2);
				sorter.setRowFilter(combine_AllFilters);
			}
		}
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	class Non_EA_Management_GUI extends JLayeredPane {
		List<List<JCheckBox>> checkboxStaticIdentifiers;
		ScrollPane_StaticIdentifiers static_identifiers_scrollpane;
		
		List<List<JCheckBox>> checkboxStaticIdentifiers_silviculture;
		ScrollPane_StaticIdentifiers static_identifiers_scrollpane_silviculture;
		
		JPanel button_table_Panel;	
		Panel_QuickEdit_Non_EA quick_edit;
		JScrollPane scrollpane_QuickEdit;
		
		public Non_EA_Management_GUI() {
			setLayout(new BorderLayout());	
			// 1st grid -----------------------------------------------------------------------
			// 1st grid -----------------------------------------------------------------------	
			String panel_name = "Strata Attributes";
			static_identifiers_scrollpane = new ScrollPane_StaticIdentifiers(read_database, 0, panel_name);
			checkboxStaticIdentifiers = static_identifiers_scrollpane.get_CheckboxStaticIdentifiers();	
			
			for (int i = 0; i < checkboxStaticIdentifiers.size(); i++) {
				for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
					checkboxStaticIdentifiers.get(i).get(j).setSelected(true);
				}
			}
			// End of 1st grid -----------------------------------------------------------------------
			// End of 1st grid -----------------------------------------------------------------------						
			
			
			
			
			// 2nd grid -----------------------------------------------------------------------
			// 2nd grid -----------------------------------------------------------------------	
			panel_name = "Prescription Method & Choice to implement (select only necessary options for optimal solution time)";
			static_identifiers_scrollpane_silviculture = new ScrollPane_StaticIdentifiers(read_database, 3, panel_name);
			checkboxStaticIdentifiers_silviculture = static_identifiers_scrollpane_silviculture.get_CheckboxStaticIdentifiers();	
			
			for (int i = 0; i < checkboxStaticIdentifiers_silviculture.size(); i++) {
				for (int j = 0; j < checkboxStaticIdentifiers_silviculture.get(i).size(); j++) {
					checkboxStaticIdentifiers_silviculture.get(i).get(j).setSelected(true);
				}
			}
			// End of 2nd grid -----------------------------------------------------------------------
			// End of 2nd grid -----------------------------------------------------------------------			
			
			
			
			
			// 4th Grid ------------------------------------------------------------------------------		// Buttons	
			// 4th Grid -----------------------------------------------------------------------------
			// Add all buttons to a Panel----------------------------------
			button_table_Panel = new JPanel(new GridBagLayout());
			TitledBorder border = new TitledBorder("Aggregation Conditions (no row = disable all prescriptions)");
			border.setTitleJustification(TitledBorder.CENTER);
			button_table_Panel.setBorder(border);
			GridBagConstraints c2 = new GridBagConstraints();
			c2.fill = GridBagConstraints.BOTH;
			c2.insets = new Insets(0, 5, 10, 10); // padding top 0, left 5, bottom 10, right 10
			
			
			JButton btn_NewSingle = new JButton();
			btn_NewSingle.setFont(new Font(null, Font.BOLD, 14));
//			btn_NewSingle.setText("NEW SINGLE");
			btn_NewSingle.setToolTipText("New");
			btn_NewSingle.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_add.png"));
			c2.gridx = 0;
			c2.gridy = 0;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_NewSingle, c2);
			
			
			JSpinner spin_move_rows = new JSpinner (new SpinnerNumberModel(1, 0, 2, 1));
			spin_move_rows.setToolTipText("Move");
			JFormattedTextField SpinnerText = ((DefaultEditor) spin_move_rows.getEditor()).getTextField();
			SpinnerText.setHorizontalAlignment(JTextField.LEFT);
			SpinnerText.setEditable(false);
			SpinnerText.setFocusable(false);
//			DefaultFormatter formatter = (DefaultFormatter) SpinnerText.getFormatter();
//		    formatter.setCommitsOnValidEdit(true);
		    spin_move_rows.setEnabled(false);
		    c2.gridx = 0;
			c2.gridy = 1;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(spin_move_rows, c2);
			
			
			JButton btn_Edit = new JButton();
//			btn_Edit.setText("EDIT");
			btn_Edit.setToolTipText("Modify");
			btn_Edit.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_swap.png"));
			btn_Edit.setEnabled(false);
			c2.gridx = 0;
			c2.gridy = 2;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Edit, c2);
			
			
			JButton btn_Delete = new JButton();
			btn_Delete.setFont(new Font(null, Font.BOLD, 14));
//			btn_Delete.setText("DELETE");
			btn_Delete.setToolTipText("Delete");
			btn_Delete.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_erase.png"));
			btn_Delete.setEnabled(false);
			c2.gridx = 0;
			c2.gridy = 3;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Delete, c2);

			
			JToggleButton btn_Sort = create_sort_button(button_table_Panel, c2, 0, 4, 0, 0);
			btn_Sort.addActionListener(e -> apply_sort_or_nosort(btn_Sort, model2, table2));

			
			create_mass_check_button(button_table_Panel, c2, 0, 5, 0, 0).addActionListener(e -> apply_mass_check_or_uncheck("mass_check", model2, table2, data2, colCount2));
			create_mass_uncheck_button(button_table_Panel, c2, 0, 6, 0, 0).addActionListener(e -> apply_mass_check_or_uncheck("mass_uncheck", model2, table2, data2, colCount2));
			

			c2.insets = new Insets(0, 0, 0, 0); // No padding
			// Add Empty Label to make all buttons on top not middle
			c2.gridx = 0;
			c2.gridy = 7;
			c2.weightx = 0;
			c2.weighty = 1;
			button_table_Panel.add(new JLabel(), c2);
			
			// Add table2				
			create_table2();
			JScrollPane table_ScrollPane = new JScrollPane(table2);	
//			c2.insets = new Insets(1, 0, 0, 0);			// Activate to remove the ugly inside border
//			Border tempBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.makeTransparent(Color.BLACK, 0));
//			table_ScrollPane.setBorder(tempBorder);
			c2.gridx = 1;
			c2.gridy = 0;
			c2.weightx = 1;
			c2.weighty = 1;
			c2.gridheight = 8;
			button_table_Panel.add(table_ScrollPane, c2);
			// End of 4th Grid -----------------------------------------------------------------------
			// End of 4th Grid -----------------------------------------------------------------------	


			// scrollPane Quick Edit ----------------------------------------------------------------------
			// scrollPane Quick Edit ----------------------------------------------------------------------
			quick_edit = new Panel_QuickEdit_Non_EA(table2, data2);
			scrollpane_QuickEdit = new JScrollPane(quick_edit);
			//quick_edit.add(button_select_nonEA_rules);
			//quick_edit.add(button_remove_nonEA_rules);
			border = new TitledBorder("Quick Edit");
			border.setTitleJustification(TitledBorder.CENTER);
			scrollpane_QuickEdit.setBorder(border);
			scrollpane_QuickEdit.setVisible(false);
			
			
			// Add Listeners for table2 & buttons----------------------------------------------------------
			// Add Listeners for table2 & buttons----------------------------------------------------------
			
			
			// table2
			class Table_Interaction {
				void refresh() {
	            	int[] selectedRow = table2.getSelectedRows();
					if (selectedRow.length == 1) {		// Reload Constraint & Enable Edit	when: 1 row is selected and no cell is editing
						int currentRow = selectedRow[0];
						currentRow = table2.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
						static_identifiers_scrollpane.reload_this_constraint_static_identifiers((String) data2[currentRow][2]);	// 2 is the static_identifiers which have some attributes selected				
						static_identifiers_scrollpane_silviculture.reload_this_constraint_static_identifiers((String) data2[currentRow][3]);	// 3 is the method & choice
						btn_Edit.setEnabled(true);
					} else {		// Disable Edit
						btn_Edit.setEnabled(false);
					}
					
					if (selectedRow.length >= 1 && table2.isEnabled()) {		// Enable Delete  when: >=1 row is selected, table is enable (often after Edit button finished its task)
						btn_Delete.setEnabled(true);
					} else {		// Disable Delete
						btn_Delete.setEnabled(false);
					}
					
					if (selectedRow.length >= 1 && btn_Sort.getText().equals("OFF")) {	// Enable Spinner when: >=1 row is selected and Sorter is off
						spin_move_rows.setEnabled(true);
					} else {		// Disable Spinner
						spin_move_rows.setEnabled(false);
					}
				}
			}
			Table_Interaction table_interaction = new Table_Interaction();
	                
			table2.addMouseListener(new MouseAdapter() { // Add listener
				@Override
				public void mouseReleased(MouseEvent e) {
					table_interaction.refresh();
				}
			});
			
			table2.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	table_interaction.refresh();
		        }
		    });			

			
			// New single
			btn_NewSingle.addActionListener(e -> {
				if (table2.isEditing()) {
					table2.getCellEditor().stopCellEditing();
				}
				
				// Add 1 row
				rowCount2++;
				data2 = new Object[rowCount2][colCount2];
				for (int ii = 0; ii < rowCount2 - 1; ii++) {
					for (int jj = 0; jj < colCount2; jj++) {
						data2[ii][jj] = model2.getValueAt(ii, jj);
					}
				}
								
				data2[rowCount2 - 1][1] = String.join(" .....eligible to apply on..... ",
						static_identifiers_scrollpane_silviculture.get_static_description_from_GUI(),
						static_identifiers_scrollpane.get_static_description_from_GUI());
				data2[rowCount2 - 1][2] = static_identifiers_scrollpane.get_static_info_from_GUI();
				data2[rowCount2 - 1][3] = static_identifiers_scrollpane_silviculture.get_static_info_from_GUI();
				data2[rowCount2-1][4] = true;
				model2.updateTableModelPrism(rowCount2, colCount2, data2, columnNames2);
				update_id();
				model2.fireTableDataChanged();
				//quick_edit = new Panel_QuickEdit_Non_EA(table2, data2);		// 2 lines to update data for Quick Edit Panel
	 			scrollpane_QuickEdit.setViewportView(quick_edit);
				
				// Convert the new Row to model view and then select it 
				int newRow = table2.convertRowIndexToView(rowCount2 - 1);
				table2.setRowSelectionInterval(newRow, newRow);
				table2.scrollRectToVisible(new Rectangle(table2.getCellRect(newRow, 0, true)));
			});
										
			
			// Edit
			btn_Edit.addActionListener(e -> {
				if (table2.isEditing()) {
					table2.getCellEditor().stopCellEditing();
				}
				
				if (table2.isEnabled()) {			
					int selectedRow = table2.getSelectedRow();
					selectedRow = table2.convertRowIndexToModel(selectedRow);		// Convert row index because "Sort" causes problems	
	
					// Apply change	
					data2[selectedRow][2] = static_identifiers_scrollpane.get_static_info_from_GUI();
					data2[selectedRow][3] = static_identifiers_scrollpane_silviculture.get_static_info_from_GUI();
					model2.fireTableDataChanged();	
					
					// Convert the edited Row to model view and then select it 
					int editRow = table2.convertRowIndexToView(selectedRow);
					table2.setRowSelectionInterval(editRow, editRow);
					
					static_identifiers_scrollpane.highlight();
					static_identifiers_scrollpane_silviculture.highlight();
				} 
			});
			
			
			btn_Edit.addMouseListener(new MouseAdapter() { // Add listener
				public void mouseEntered(java.awt.event.MouseEvent e) {
					if (table2.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.highlight();
						static_identifiers_scrollpane_silviculture.highlight();
					}
				}

				public void mouseExited(java.awt.event.MouseEvent e) {
					if (table2.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.unhighlight();
						static_identifiers_scrollpane_silviculture.unhighlight();
					}
				}
			});
			
			
			// Spinner
		    spin_move_rows.addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
					int up_or_down = (int) spin_move_rows.getValue() - 1;										
					spin_move_rows.setValue((int) 1);	// Reset spinner value to 1
										
					if (up_or_down == 1) {	// move up
						// Cancel editing before moving conditions up or down
						if (table2.isEditing()) {
							table2.getCellEditor().cancelCellEditing();
						}	
						
						// Get selected rows
						int[] selectedRow = table2.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(0) > 0) {	// If there is at least 1 row selected & the first row is not selected
							for (int i = 0; i < rowCount2; i++) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount2; j++) {
										Object temp = data2[i - 1][j];
										data2[i - 1][j] = data2[i][j];
										data2[i][j] = temp;
									}
								}
							}							
							model2.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table2.addRowSelectionInterval(i - 1, i - 1);
							}
						}
					}
										
					if (up_or_down == -1) {	// move down						
						if (table2.isEditing()) {
							table2.getCellEditor().cancelCellEditing();	// cancel editing before moving conditions up or down
						}	
						
						// Get selected rows
						int[] selectedRow = table2.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(selectedRowList.size() - 1) < rowCount2 - 1) {	// If ...
							for (int i = rowCount2 - 1; i >= 0; i--) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount2; j++) {
										Object temp = data2[i + 1][j];
										data2[i + 1][j] = data2[i][j];
										data2[i][j] = temp;
									}
								}
							}						
							model2.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table2.addRowSelectionInterval(i + 1, i + 1);
							}	
						}						
					}
					
					// Scroll to the first row of the current selected rows
					table2.scrollRectToVisible(new Rectangle(table2.getCellRect(table2.convertRowIndexToView(table2.getSelectedRow()), 0, true)));	
		        }
		    });
		    
				
			// Delete
			btn_Delete.addActionListener(e -> {
				//Cancel editing before delete
				if (table2.isEditing()) {
					table2.getCellEditor().cancelCellEditing();
				}				
				
				String ExitOption[] = {"Delete", "Cancel"};
				int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), "Delete now?", "Confirm Delete",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[1]);
				if (response == 0) {
					// Get selected rows
					int[] selectedRow = table2.getSelectedRows();
					for (int i = 0; i < selectedRow.length; i++) {
						selectedRow[i] = table2.convertRowIndexToModel(selectedRow[i]);	///Convert row index because "Sort" causes problems
					}
					
					// Create a list of selected row indexes
					List<Integer> selected_Index = new ArrayList<Integer>();				
					for (int i: selectedRow) {
						selected_Index.add(i);
					}	
					
					// Get values to the new data2
					data2 = new Object[rowCount2 - selectedRow.length][colCount2];
					int newRow =0;
					for (int ii = 0; ii < rowCount2; ii++) {
						if (!selected_Index.contains(ii)) {			//If row not in the list then add to data2 row
							for (int jj = 0; jj < colCount2; jj++) {
								data2[newRow][jj] = model2.getValueAt(ii, jj);
							}
							newRow++;
						}
					}
					// Pass back the info to table model
					rowCount2 = rowCount2 - selectedRow.length;
					model2.updateTableModelPrism(rowCount2, colCount2, data2, columnNames2);
					model2.fireTableDataChanged();	
					//quick_edit = new Panel_QuickEdit_Non_EA(table2, data2);	// 2 lines to update data for Quick Edit Panel
		 			scrollpane_QuickEdit.setViewportView(quick_edit);
				}
				
			});

			// End of Listeners for table8 & buttons -----------------------------------------------------------------------
			// End of Listeners for table8 & buttons -----------------------------------------------------------------------			



			// ToolBar Panel ----------------------------------------------------------------------------
			// ToolBar Panel ----------------------------------------------------------------------------
			ToolBarWithBgImage helpToolBar = new ToolBarWithBgImage("Project Tools", JToolBar.HORIZONTAL, null);
			helpToolBar.setFloatable(false);	//to make a tool bar immovable
			helpToolBar.setRollover(true);	//to visually indicate tool bar buttons when the user passes over them with the cursor
			helpToolBar.setBorderPainted(false);
			
			// button Quick Edit
			JToggleButton btnQuickEdit = new JToggleButton();
			btnQuickEdit.setToolTipText("Show Quick Edit Tool");
			btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
			btnQuickEdit.addActionListener(e -> {		
				if (btnQuickEdit.getToolTipText().equals("Show Quick Edit Tool")) {
						btnQuickEdit.setToolTipText("Hide Quick Edit Tool");
						btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_hide.png"));
						scrollpane_QuickEdit.setVisible(true);
						// Get everything show up nicely
						GUI_Text_splitpane.setLeftComponent(panel_Non_EA_Management_GUI);
						Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				} else {
					btnQuickEdit.setToolTipText("Show Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
					scrollpane_QuickEdit.setVisible(false);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_Non_EA_Management_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				}				
			});
			
			// button Help
			JButton btnHelp = new JButton();
			btnHelp.setToolTipText("Help");
			btnHelp.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_help.png"));
			btnHelp.addActionListener(e -> {
				
			});
			
			// Add all buttons to flow_panel
			helpToolBar.add(Box.createGlue());	//Add glue for Right alignment
			helpToolBar.add(btnQuickEdit);
			helpToolBar.add(btnHelp);
			// End of ToolBar Panel -----------------------------------------------------------------------
			// End of ToolBar Panel -----------------------------------------------------------------------
			
			
			
			
			
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			JSplitPane split_pane = new JSplitPane();
			split_pane.setBorder(null);
			split_pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			split_pane.setDividerSize(3);
						
			JPanel upper_panel = new JPanel();
			upper_panel.setBorder(null);
			upper_panel.setLayout(new GridBagLayout());			
			
			JPanel lower_panel = new JPanel();
			lower_panel.setBorder(null);
			lower_panel.setLayout(new GridBagLayout());
			
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
		    c.weighty = 1;
		    	    
		    // Add helpToolBar	
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0;
		    c.weighty = 0;
			c.gridwidth = 2;
			c.gridheight = 1;
			upper_panel.add(helpToolBar, c);				
			
			// Add the 1st grid - static_identifiers_scrollpane to the main Grid 
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 0.4;
		    c.weighty = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			upper_panel.add(static_identifiers_scrollpane, c);
			
			// Add the 2nd grid -  to the main Grid	
			c.gridx = 1;
			c.gridy = 1;
			c.weightx = 0.5;
		    c.weighty = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			upper_panel.add(static_identifiers_scrollpane_silviculture, c);
			
			// Add the button_table_Panel & scrollpane_QuickEdit to a new Panel then add that panel to the main Grid
			JPanel button_table_qedit_panel = new JPanel();
			button_table_qedit_panel.setLayout(new BorderLayout());
			button_table_qedit_panel.add(button_table_Panel, BorderLayout.CENTER);
			button_table_qedit_panel.add(scrollpane_QuickEdit, BorderLayout.EAST);			
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 2; 
			c.gridheight = 1;
			c.weightx = 1;
		    c.weighty = 1;
		    lower_panel.add(button_table_qedit_panel, c);
			
			
			split_pane.setLeftComponent(upper_panel);
			split_pane.setRightComponent(lower_panel);
			super.add(split_pane, BorderLayout.CENTER);	
		}
		
		
	    // Update id column. id needs to be unique in order to use in flow constraints-----------------
	    public void update_id() {  		
			List<Integer> id_list = new ArrayList<Integer>();			
			
			for (int row = 0; row < rowCount2; row++) {
				if (data2[row][0] != null) {
					id_list.add((int) data2[row][0]);
				}
			}			
			
			for (int row = 0; row < rowCount2; row++) {
				if (data2[row][0] == null) {
					int new_id = (id_list.size() > 0) ? Collections.max(id_list) + 1 : 1;	//new id = (max id + 1) or = 1 if no row
					data2[row][0] = new_id;
					id_list.add(new_id);
				}
			}			
		}
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	class EA_Management_GUI extends JLayeredPane {
		List<List<JCheckBox>> checkboxStaticIdentifiers;
		ScrollPane_StaticIdentifiers static_identifiers_scrollpane;
		ScrollPane_SubTable_EA_Management conversion_and_rotation_scrollpane;
		
		JPanel button_table_Panel;	
		Panel_QuickEdit_EA quick_edit;
		JScrollPane scrollpane_QuickEdit;
		
		public EA_Management_GUI() {
			// 1st grid -----------------------------------------------------------------------
			// 1st grid -----------------------------------------------------------------------	
			String panel_name = "Strata Attributes";
			static_identifiers_scrollpane = new ScrollPane_StaticIdentifiers(read_database, 0, panel_name);
			checkboxStaticIdentifiers = static_identifiers_scrollpane.get_CheckboxStaticIdentifiers();	
			
			for (int i = 0; i < checkboxStaticIdentifiers.size(); i++) {
				for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
					checkboxStaticIdentifiers.get(i).get(j).setSelected(true);
				}
			}
			// End of 1st grid -----------------------------------------------------------------------
			// End of 1st grid -----------------------------------------------------------------------						
			
			
			
			
			// 2nd grid -----------------------------------------------------------------------
			// 2nd grid -----------------------------------------------------------------------	
			create_table4a();
			conversion_and_rotation_scrollpane = new ScrollPane_SubTable_EA_Management(table4a, data4a, model4a);
//			conversion_and_rotation_scrollpane.show_table();
			// End of 2nd grid -----------------------------------------------------------------------
			// End of 2nd grid -----------------------------------------------------------------------			


			// 4th Grid ------------------------------------------------------------------------------		// Buttons	
			// 4th Grid -----------------------------------------------------------------------------
			// Add all buttons to a Panel----------------------------------
			button_table_Panel = new JPanel(new GridBagLayout());
			TitledBorder border = new TitledBorder("Agregation Conditions (no row = disable all even-aged prescriptions)");
			border.setTitleJustification(TitledBorder.CENTER);
			button_table_Panel.setBorder(border);
			GridBagConstraints c2 = new GridBagConstraints();
			c2.fill = GridBagConstraints.BOTH;
			c2.insets = new Insets(0, 5, 10, 10); // padding top 0, left 5, bottom 10, right 10
			
			
			JButton btn_NewSingle = new JButton();
			btn_NewSingle.setFont(new Font(null, Font.BOLD, 14));
//			btn_NewSingle.setText("NEW SINGLE");
			btn_NewSingle.setToolTipText("New");
			btn_NewSingle.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_add.png"));
			c2.gridx = 0;
			c2.gridy = 0;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_NewSingle, c2);
			
			
			JSpinner spin_move_rows = new JSpinner (new SpinnerNumberModel(1, 0, 2, 1));
			spin_move_rows.setToolTipText("Move");
			JFormattedTextField SpinnerText = ((DefaultEditor) spin_move_rows.getEditor()).getTextField();
			SpinnerText.setHorizontalAlignment(JTextField.LEFT);
			SpinnerText.setEditable(false);
			SpinnerText.setFocusable(false);
//			DefaultFormatter formatter = (DefaultFormatter) SpinnerText.getFormatter();
//		    formatter.setCommitsOnValidEdit(true);
		    spin_move_rows.setEnabled(false);
		    c2.gridx = 0;
			c2.gridy = 1;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(spin_move_rows, c2);
			
			
			JButton btn_Edit = new JButton();
//			btn_Edit.setText("EDIT");
			btn_Edit.setToolTipText("Modify");
			btn_Edit.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_swap.png"));
			btn_Edit.setEnabled(false);
			c2.gridx = 0;
			c2.gridy = 2;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Edit, c2);
			
			
			JButton btn_Delete = new JButton();
			btn_Delete.setFont(new Font(null, Font.BOLD, 14));
//			btn_Delete.setText("DELETE");
			btn_Delete.setToolTipText("Delete");
			btn_Delete.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_erase.png"));
			btn_Delete.setEnabled(false);
			c2.gridx = 0;
			c2.gridy = 3;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Delete, c2);

			
			JToggleButton btn_Sort = create_sort_button(button_table_Panel, c2, 0, 4, 0, 0);
			btn_Sort.addActionListener(e -> apply_sort_or_nosort(btn_Sort, model4, table4));

			
			create_mass_check_button(button_table_Panel, c2, 0, 5, 0, 0).addActionListener(e ->	apply_mass_check_or_uncheck("mass_check", model4, table4, data4, colCount4));
			create_mass_uncheck_button(button_table_Panel, c2, 0, 6, 0, 0).addActionListener(e-> apply_mass_check_or_uncheck("mass_uncheck", model4, table4, data4, colCount4));


			c2.insets = new Insets(0, 0, 0, 0); // No padding
			// Add Empty Label to make all buttons on top not middle
			c2.gridx = 0;
			c2.gridy = 7;
			c2.weightx = 0;
			c2.weighty = 1;
			button_table_Panel.add(new JLabel(), c2);

			
			// Add table4
			create_table4();
			JScrollPane table_ScrollPane = new JScrollPane(table4);	
//			c2.insets = new Insets(1, 0, 0, 0);			// Activate to remove the ugly inside border
//			Border tempBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.makeTransparent(Color.BLACK, 0));
//			table_ScrollPane.setBorder(tempBorder);
			c2.gridx = 1;
			c2.gridy = 0;
			c2.weightx = 1;
			c2.weighty = 1;
			c2.gridheight = 8;
			button_table_Panel.add(table_ScrollPane, c2);
			// End of 4th Grid -----------------------------------------------------------------------
			// End of 4th Grid -----------------------------------------------------------------------	
			
			
			
			// Add Listeners for table4 & buttons----------------------------------------------------------
			// Add Listeners for table4 & buttons----------------------------------------------------------
			
			
			// table4
			class Table_Interaction {
				void refresh() {
					int[] selectedRow = table4.getSelectedRows();
					if (selectedRow.length == 1) {		// Reload Constraint & Enable Edit	when: 1 row is selected and no cell is editing
						int currentRow = selectedRow[0];
						currentRow = table4.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
						static_identifiers_scrollpane.reload_this_constraint_static_identifiers((String) data4[currentRow][2]);	// 2 is the static_identifiers which have some attributes selected				
						conversion_and_rotation_scrollpane.reload_this_table((String) data4[currentRow][3]);	// 3 is the conversion_and_rotation
						btn_Edit.setEnabled(true);
						quick_edit.enable_all_apply_buttons();
						conversion_and_rotation_scrollpane.show_table();
					} else {		// Disable Edit
						btn_Edit.setEnabled(false);
						quick_edit.disable_all_apply_buttons();
						conversion_and_rotation_scrollpane.hide_table();
					}
					
					if (selectedRow.length >= 1 && table4.isEnabled()) {		// Enable Delete  when: >=1 row is selected, table is enable (often after Edit button finished its task)
						btn_Delete.setEnabled(true);
					} else {		// Disable Delete
						btn_Delete.setEnabled(false);
					}
					
					if (selectedRow.length >= 1 && btn_Sort.getText().equals("OFF")) {	// Enable Spinner when: >=1 row is selected and Sorter is off
						spin_move_rows.setEnabled(true);
					} else {		// Disable Spinner
						spin_move_rows.setEnabled(false);
					}
				}
			}
			Table_Interaction table_interaction = new Table_Interaction();
	                
			table4.addMouseListener(new MouseAdapter() { // Add listener
				@Override
				public void mouseReleased(MouseEvent e) {
					table_interaction.refresh();
				}
			});
			
			table4.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	table_interaction.refresh();
		        }
		    });			

			
			table4a.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	if (table4.getSelectedRow() != -1) {	// only update the table4 if there is a row selected
		        		int currentRow = table4.getSelectedRow();		        	
						currentRow = table4.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
						conversion_and_rotation_scrollpane.update_table_data(data4a);	// Update so we have the latest data of table 4a to retrieve and write to table4 below
						data4[currentRow][3] = conversion_and_rotation_scrollpane.get_conversion_and_rotation_info_from_GUI();	
						model4.fireTableCellUpdated(currentRow, 3);
		        	}
		        }
		    });
			
			
			// New single
			btn_NewSingle.addActionListener(e -> {
				if (table4.isEditing()) {
					table4.getCellEditor().stopCellEditing();
				}
				
				// Add 1 row
				rowCount4++;
				data4 = new Object[rowCount4][colCount4];
				for (int ii = 0; ii < rowCount4 - 1; ii++) {
					for (int jj = 0; jj < colCount4; jj++) {
						data4[ii][jj] = model4.getValueAt(ii, jj);
					}	
				}
								
				data4[rowCount4 - 1][1] = static_identifiers_scrollpane.get_static_description_from_GUI();
				data4[rowCount4 - 1][2] = static_identifiers_scrollpane.get_static_info_from_GUI();
				data4[rowCount4 - 1][3] = conversion_and_rotation_scrollpane.get_conversion_and_rotation_info_from_GUI();
				data4[rowCount4 -1][4] = true;
				model4.updateTableModelPrism(rowCount4, colCount4, data4, columnNames4);
				update_id();
				model4.fireTableDataChanged();
				
				// Convert the new Row to model view and then select it 
				int newRow = table4.convertRowIndexToView(rowCount4 - 1);
				table4.setRowSelectionInterval(newRow, newRow);
				table4.scrollRectToVisible(new Rectangle(table4.getCellRect(newRow, 0, true)));
			});
										
			
			// Edit
			btn_Edit.addActionListener(e -> {
				if (table4.isEditing()) {
					table4.getCellEditor().stopCellEditing();
				}
				
				if (table4.isEnabled()) {			
					int selectedRow = table4.getSelectedRow();
					selectedRow = table4.convertRowIndexToModel(selectedRow);		// Convert row index because "Sort" causes problems	
	
					// Apply change	
					data4[selectedRow][2] = static_identifiers_scrollpane.get_static_info_from_GUI();
					model4.fireTableDataChanged();	
					
					// Convert the edited Row to model view and then select it 
					int editRow = table4.convertRowIndexToView(selectedRow);
					table4.setRowSelectionInterval(editRow, editRow);
					
					static_identifiers_scrollpane.highlight();
				} 
			});
			
			
			btn_Edit.addMouseListener(new MouseAdapter() { // Add listener
				public void mouseEntered(java.awt.event.MouseEvent e) {
					if (table4.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.highlight();
					}
				}

				public void mouseExited(java.awt.event.MouseEvent e) {
					if (table4.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.unhighlight();
					}
				}
			});
			
			
			// Spinner
		    spin_move_rows.addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
					int up_or_down = (int) spin_move_rows.getValue() - 1;										
					spin_move_rows.setValue((int) 1);	// Reset spinner value to 1
										
					if (up_or_down == 1) {	// move up
						// Cancel editing before moving conditions up or down
						if (table4.isEditing()) {
							table4.getCellEditor().cancelCellEditing();
						}	
						
						// Get selected rows
						int[] selectedRow = table4.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(0) > 0) {	// If there is at least 1 row selected & the first row is not selected
							for (int i = 0; i < rowCount4; i++) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount4; j++) {
										Object temp = data4[i - 1][j];
										data4[i - 1][j] = data4[i][j];
										data4[i][j] = temp;
									}
								}
							}							
							model4.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table4.addRowSelectionInterval(i - 1, i - 1);
							}
						}
					}
										
					if (up_or_down == -1) {	// move down						
						if (table4.isEditing()) {
							table4.getCellEditor().cancelCellEditing();	// cancel editing before moving conditions up or down
						}	
						
						// Get selected rows
						int[] selectedRow = table4.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(selectedRowList.size() - 1) < rowCount4 - 1) {	// If ...
							for (int i = rowCount4 - 1; i >= 0; i--) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount4; j++) {
										Object temp = data4[i + 1][j];
										data4[i + 1][j] = data4[i][j];
										data4[i][j] = temp;
									}
								}
							}						
							model4.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table4.addRowSelectionInterval(i + 1, i + 1);
							}	
						}						
					}
					
					// Scroll to the first row of the current selected rows
					table4.scrollRectToVisible(new Rectangle(table4.getCellRect(table4.convertRowIndexToView(table4.getSelectedRow()), 0, true)));	
		        }
		    });
		    
				
			// Delete
			btn_Delete.addActionListener(e -> {
				//Cancel editing before delete
				if (table4.isEditing()) {
					table4.getCellEditor().cancelCellEditing();
				}				
				
				String ExitOption[] = {"Delete", "Cancel"};
				int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), "Delete now?", "Confirm Delete",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[0]);
				if (response == 0) {
					// Get selected rows
					int[] selectedRow = table4.getSelectedRows();
					for (int i = 0; i < selectedRow.length; i++) {
						selectedRow[i] = table4.convertRowIndexToModel(selectedRow[i]);	///Convert row index because "Sort" causes problems
					}
					
					// Create a list of selected row indexes
					List<Integer> selected_Index = new ArrayList<Integer>();				
					for (int i: selectedRow) {
						selected_Index.add(i);
					}	
					
					// Get values to the new data4
					data4 = new Object[rowCount4 - selectedRow.length][colCount4];
					int newRow =0;
					for (int ii = 0; ii < rowCount4; ii++) {
						if (!selected_Index.contains(ii)) {			//If row not in the list then add to data4 row
							for (int jj = 0; jj < colCount4; jj++) {
								data4[newRow][jj] = model4.getValueAt(ii, jj);
							}
							newRow++;
						}
					}
					// Pass back the info to table model
					rowCount4 = rowCount4 - selectedRow.length;
					model4.updateTableModelPrism(rowCount4, colCount4, data4, columnNames4);
					model4.fireTableDataChanged();	
				}
				
			});

			// End of Listeners for table4 & buttons -----------------------------------------------------------------------
			// End of Listeners for table4 & buttons -----------------------------------------------------------------------
	        

			// scrollPane Quick Edit-----------------------------------------------------------------------
			// scrollPane Quick Edit-----------------------------------------------------------------------		
			Object[][] default_data = new Object[data4a.length][];
			for (int i = 0; i < default_data.length; i++) {
				default_data[i] = Arrays.copyOf(data4a[i], data4a[i].length);
			}
			quick_edit = new Panel_QuickEdit_EA(table4a, data4a, read_database.get_rotation_ranges(), default_data);
			quick_edit.disable_all_apply_buttons();
			JScrollPane scrollpane_QuickEdit = new JScrollPane(quick_edit);
			border = new TitledBorder("Quick Edit ");
			border.setTitleJustification(TitledBorder.CENTER);
			scrollpane_QuickEdit.setBorder(border);
			scrollpane_QuickEdit.setVisible(false);
 			
					
			
			// ToolBar Panel ----------------------------------------------------------------------------
			// ToolBar Panel ----------------------------------------------------------------------------
			ToolBarWithBgImage helpToolBar = new ToolBarWithBgImage("Project Tools", JToolBar.HORIZONTAL, null);
			helpToolBar.setFloatable(false);	//to make a tool bar immovable
			helpToolBar.setRollover(true);	//to visually indicate tool bar buttons when the user passes over them with the cursor
			helpToolBar.setBorderPainted(false);
			
			// button Quick Edit
			JToggleButton btnQuickEdit = new JToggleButton();
			btnQuickEdit.setToolTipText("Show Quick Edit Tool");
			btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
			btnQuickEdit.addActionListener(e -> {		
				if (btnQuickEdit.getToolTipText().equals("Show Quick Edit Tool")) {
						btnQuickEdit.setToolTipText("Hide Quick Edit Tool");
						btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_hide.png"));
						scrollpane_QuickEdit.setVisible(true);
						// Get everything show up nicely
						GUI_Text_splitpane.setLeftComponent(panel_EA_Management_GUI);
						Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				} else {
					btnQuickEdit.setToolTipText("Show Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
					scrollpane_QuickEdit.setVisible(false);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_EA_Management_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				}				
			});
			
			// button Help
			JButton btnHelp = new JButton();
			btnHelp.setToolTipText("Help");
			btnHelp.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_help.png"));
			btnHelp.addActionListener(e -> {
				
			});
			
			// Add all buttons to flow_panel
			helpToolBar.add(Box.createGlue());	//Add glue for Right alignment
			helpToolBar.add(btnQuickEdit);
			helpToolBar.add(btnHelp);
			// End of ToolBar Panel -----------------------------------------------------------------------
			// End of ToolBar Panel -----------------------------------------------------------------------
			
			
			
			
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			JSplitPane split_pane = new JSplitPane();
			split_pane.setBorder(null);
			split_pane.setPreferredSize(new Dimension(0, 0));		// important to make the weight work well
			split_pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			split_pane.setDividerSize(3);
			split_pane.setLeftComponent(static_identifiers_scrollpane);
			split_pane.setRightComponent(button_table_Panel);
			
			
			
			JPanel combine_panel = new JPanel();
			combine_panel.setPreferredSize(new Dimension(0, 0));	// important to make the weight work well
			GridBagConstraints c = new GridBagConstraints();
			// Add to combine_panel
			combine_panel.setLayout(new GridBagLayout());			
			// Add
			combine_panel.add(conversion_and_rotation_scrollpane, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
					0, 0, 1, 1, 1, 1, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
					0, 0, 0, 0));		// insets top, left, bottom, right
			// Add
			combine_panel.add(scrollpane_QuickEdit, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
					0, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
					0, 0, 0, 0));		// insets top, left, bottom, right
			
			
			
			// Add to main grid
			super.setLayout(new GridBagLayout());
			// Add
			super.add(helpToolBar, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					0, 0, 2, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 0, 0, 0));		// insets top, left, bottom, right	
			// Add
 			super.add(split_pane, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					0, 1, 1, 1, 0.4, 1, // gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 0, 0, 0));		// insets top, left, bottom, right
 			// Add		
 			super.add(combine_panel, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
 					1, 1, 1, 1, 0.5, 1, // gridx, gridy, gridwidth, gridheight, weightx, weighty
 					0, 0, 0, 0));		// insets top, left, bottom, right
		}
		
	    // Update id column. id needs to be unique in order to use in flow constraints-----------------
	    public void update_id() {  		
			List<Integer> id_list = new ArrayList<Integer>();			
			
			for (int row = 0; row < rowCount4; row++) {
				if (data4[row][0] != null) {
					id_list.add((int) data4[row][0]);
				}
			}			
			
			for (int row = 0; row < rowCount4; row++) {
				if (data4[row][0] == null) {
					int new_id = (id_list.size() > 0) ? Collections.max(id_list) + 1 : 1;	//new id = (max id + 1) or = 1 if no row
					data4[row][0] = new_id;
					id_list.add(new_id);
				}
			}			
		}
	}

	
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	class Non_SR_Disturbances_GUI extends JLayeredPane {
		List<List<JCheckBox>> checkboxStaticIdentifiers;
		ScrollPane_StaticIdentifiers static_identifiers_scrollpane;
		
		JPanel button_table_Panel;	
		Panel_QuickEdit_Non_SR quick_edit;
		JScrollPane scrollpane_QuickEdit;
		
		public Non_SR_Disturbances_GUI() {
			setLayout(new BorderLayout());	
			// 1st grid -----------------------------------------------------------------------
			// 1st grid -----------------------------------------------------------------------	
			String panel_name = "Strata Attributes";
			static_identifiers_scrollpane = new ScrollPane_StaticIdentifiers(read_database, 0, panel_name);
			checkboxStaticIdentifiers = static_identifiers_scrollpane.get_CheckboxStaticIdentifiers();	
			
			for (int i = 0; i < checkboxStaticIdentifiers.size(); i++) {
				for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
					checkboxStaticIdentifiers.get(i).get(j).setSelected(true);
				}
			}
			// End of 1st grid -----------------------------------------------------------------------
			// End of 1st grid -----------------------------------------------------------------------						
			
			
			
			
			// 2nd grid -----------------------------------------------------------------------
			// 2nd grid -----------------------------------------------------------------------	
			String message = 
					  "1. Percentage is the proportion of an existing stratum area assigned to a Non-SR disturbance from the 1st period\n\n"
					+ "2. Percentage for each type of disturbance is auto zero if not found any prescription in the database\n\n"
					+ "3. If percentage = null (blank cell), percentage value will be based on the next lower priority condition\n\n"
					+ "4. If percentage = null after processing all conditions, Prism would be free to assign any percentage value.\nWe often use the \"all null\" set up to control Non-SR disturbances through using basic constraints\n\n"
					+ "5. MS_R and BS_R are absent. We might want to define zero loss for SR disturbances in MS_E and BS_E areas.\n\n"
					+ "A side-note for Management Cost: For any particular cost type instance,\nif value = null after processing all conditions, Prism would assign the value of zero to that cost type instance";
			PrismTextAreaReadMe warning_textarea = new PrismTextAreaReadMe("icon_script.png", 1, 1 /*32, 32*/);
			warning_textarea.append(message);
			warning_textarea.setSelectionStart(0);	// scroll to top
			warning_textarea.setSelectionEnd(0);
			warning_textarea.setEditable(false);
			
			PrismTitleScrollPane infoScrollPane = new PrismTitleScrollPane("Notes for non-stand-replacing disturbances", "CENTER", warning_textarea);
			infoScrollPane.setPreferredSize(new Dimension(0, 250));
			// End of 2nd grid -----------------------------------------------------------------------
			// End of 2nd grid -----------------------------------------------------------------------			
			
			
			
			
			// 4th Grid ------------------------------------------------------------------------------		// Buttons	
			// 4th Grid -----------------------------------------------------------------------------
			// Add all buttons to a Panel----------------------------------
			button_table_Panel = new JPanel(new GridBagLayout());
			TitledBorder border = new TitledBorder("Priority Conditions (top row = top priority, no row = no disturbance)");
			border.setTitleJustification(TitledBorder.CENTER);
			button_table_Panel.setBorder(border);
			GridBagConstraints c2 = new GridBagConstraints();
			c2.fill = GridBagConstraints.BOTH;
			c2.insets = new Insets(0, 5, 10, 10); // padding top 0, left 5, bottom 10, right 10
			
			
			JButton btn_NewSingle = new JButton();
			btn_NewSingle.setFont(new Font(null, Font.BOLD, 14));
//			btn_NewSingle.setText("NEW SINGLE");
			btn_NewSingle.setToolTipText("New");
			btn_NewSingle.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_add.png"));
			c2.gridx = 0;
			c2.gridy = 0;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_NewSingle, c2);
			
			
			JButton btn_New_Multiple = new JButton();
			btn_New_Multiple.setFont(new Font(null, Font.BOLD, 14));
//			btn_New_Multiple.setText("NEW MULTIPLE");
			btn_New_Multiple.setToolTipText("New multiple");
			btn_New_Multiple.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_add3.png"));					
			c2.gridx = 0;
			c2.gridy = 1;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_New_Multiple, c2);
			
			
			JSpinner spin_move_rows = new JSpinner (new SpinnerNumberModel(1, 0, 2, 1));
			spin_move_rows.setToolTipText("Move");
			JFormattedTextField SpinnerText = ((DefaultEditor) spin_move_rows.getEditor()).getTextField();
			SpinnerText.setHorizontalAlignment(JTextField.LEFT);
			SpinnerText.setEditable(false);
			SpinnerText.setFocusable(false);
//			DefaultFormatter formatter = (DefaultFormatter) SpinnerText.getFormatter();
//		    formatter.setCommitsOnValidEdit(true);
		    spin_move_rows.setEnabled(false);
		    c2.gridx = 0;
			c2.gridy = 2;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(spin_move_rows, c2);
			
			
			JButton btn_Edit = new JButton();
//			btn_Edit.setText("EDIT");
			btn_Edit.setToolTipText("Modify");
			btn_Edit.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_swap.png"));
			btn_Edit.setEnabled(false);
			c2.gridx = 0;
			c2.gridy = 3;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Edit, c2);
			
			
			JButton btn_Delete = new JButton();
			btn_Delete.setFont(new Font(null, Font.BOLD, 14));
//			btn_Delete.setText("DELETE");
			btn_Delete.setToolTipText("Delete");
			btn_Delete.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_erase.png"));
			btn_Delete.setEnabled(false);
			c2.gridx = 0;
			c2.gridy = 4;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Delete, c2);

			
//			JToggleButton btn_Sort = create_sort_button(button_table_Panel, c2, 0, 5, 0, 0);
//			btn_Sort.addActionListener(e -> apply_sort_or_nosort(btn_Sort, model5, table5));
			
			
			create_mass_check_button(button_table_Panel, c2, 0, 6, 0, 0).addActionListener(e->	apply_mass_check_or_uncheck("mass_check", model5, table5, data5, colCount5));
			create_mass_uncheck_button(button_table_Panel,c2, 0, 7, 0, 0).addActionListener(e->	apply_mass_check_or_uncheck("mass_uncheck", model5, table5, data5, colCount5));
			
			
			c2.insets = new Insets(0, 0, 0, 0); // No padding
			// Add Empty Label to make all buttons on top not middle
			c2.gridx = 0;
			c2.gridy = 8;
			c2.weightx = 0;
			c2.weighty = 1;
			button_table_Panel.add(new JLabel(), c2);
			
			
			// Add table5				
			create_table5();
			JScrollPane table_ScrollPane = new JScrollPane(table5);	
//			c2.insets = new Insets(1, 0, 0, 0);			// Activate to remove the ugly inside border
//			Border tempBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.makeTransparent(Color.BLACK, 0));
//			table_ScrollPane.setBorder(tempBorder);
			c2.gridx = 1;
			c2.gridy = 0;
			c2.weightx = 1;
			c2.weighty = 1;
			c2.gridheight = 9;
			button_table_Panel.add(table_ScrollPane, c2);
			// End of 4th Grid -----------------------------------------------------------------------
			// End of 4th Grid -----------------------------------------------------------------------	
			
			
			
			// Add Listeners for table5 & buttons----------------------------------------------------------
			// Add Listeners for table5 & buttons----------------------------------------------------------
			
			
			// table5
			class Table_Interaction {
				void refresh() {
					int[] selectedRow = table5.getSelectedRows();
					if (selectedRow.length == 1) {		// Reload Constraint & Enable Edit	when: 1 row is selected and no cell is editing
						int currentRow = selectedRow[0];
						currentRow = table5.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
						static_identifiers_scrollpane.reload_this_constraint_static_identifiers((String) data5[currentRow][2]);	// 2 is the static_identifiers which have some attributes selected				
						btn_Edit.setEnabled(true);
					} else {		// Disable Edit
						btn_Edit.setEnabled(false);
					}
					
					if (selectedRow.length >= 1 && table5.isEnabled()) {		// Enable Delete  when: >=1 row is selected, table is enable (often after Edit button finished its task)
						btn_Delete.setEnabled(true);
					} else {		// Disable Delete
						btn_Delete.setEnabled(false);
					}
					
					if (selectedRow.length >= 1) {	// Enable Spinner when: >=1 row is selected
						spin_move_rows.setEnabled(true);
					} else {		// Disable Spinner
						spin_move_rows.setEnabled(false);
					}
				}
			}
			Table_Interaction table_interaction = new Table_Interaction();
			table5.addMouseListener(new MouseAdapter() { // Add listener
				@Override
				public void mouseReleased(MouseEvent e) {
					table_interaction.refresh();
				}
			});
			
			table5.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	table_interaction.refresh();
		        }
		    });

			
			// New single
			btn_NewSingle.addActionListener(e -> {
				if (table5.isEditing()) {
					table5.getCellEditor().stopCellEditing();
				}
				
				// Add 1 row
				rowCount5++;
				data5 = new Object[rowCount5][colCount5];
				for (int ii = 0; ii < rowCount5 - 1; ii++) {
					for (int jj = 0; jj < colCount5; jj++) {
						data5[ii][jj] = model5.getValueAt(ii, jj);
					}	
				}
								
				data5[rowCount5 - 1][1] = static_identifiers_scrollpane.get_static_description_from_GUI();
				data5[rowCount5 - 1][2] = static_identifiers_scrollpane.get_static_info_from_GUI();
				data5[rowCount5 - 1][5] = true;
				model5.updateTableModelPrism(rowCount5, colCount5, data5, columnNames5);
				update_id();
				model5.fireTableDataChanged();
				//quick_edit = new Panel_QuickEdit_Non_SR(table5, data5);		// 2 lines to update data for Quick Edit Panel
	 			//scrollpane_QuickEdit.setViewportView(quick_edit);
				
				// Convert the new Row to model view and then select it 
				int newRow = table5.convertRowIndexToView(rowCount5 - 1);
				table5.setRowSelectionInterval(newRow, newRow);
				table5.scrollRectToVisible(new Rectangle(table5.getCellRect(newRow, 0, true)));
			});
					
			
			// New Multiple
			btn_New_Multiple.addActionListener(e -> {
				if (table8.isEditing()) {
					table8.getCellEditor().stopCellEditing();
				}
				
				ScrollPane_ConstraintsSplitNonSR constraint_split_ScrollPanel = new ScrollPane_ConstraintsSplitNonSR(static_identifiers_scrollpane.get_static_layer_title_as_checkboxes());
				
				String ExitOption[] = {"Add Conditions","Cancel"};
				int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), constraint_split_ScrollPanel, "Create multiple conditions - checked items will be split",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ExitOption, ExitOption[0]);
				if (response == 0) {	// Add conditions
					// calculate static_permutation
					int total_static_permutation = 1;
					List<Integer> static_split_id = constraint_split_ScrollPanel.get_static_split_id();	// id of static layers to be split
					for (int i : static_split_id) {
						int total_checked_elements = 0;
						for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
							if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {	// If element of this static is checked		
								total_checked_elements ++;	// Increase number of constraints
							}
						}
						total_static_permutation = total_static_permutation * total_checked_elements;
					}										
					int total_constraints = total_static_permutation;
					
					// Ask to confirm adding if there are more than 1000 constraints
					int response2 = 0;	
					if (total_constraints > 10000) {
						String ExitOption2[] = {"Add","Cancel"};
						String warningText = "Prism is going to add " + total_constraints + " conditions. Continue?";
						response2 = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), warningText, "Confirm adding condition",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_warning.png"), ExitOption2, ExitOption2[1]);
						
					}
						
					if (response2 == 0) {	
						constraint_split_ScrollPanel.stop_editing();
						
						
						// for static identifiers------------------------------------------------------------------------------------------------------
						List<List<String>> static_iterable_lists = new ArrayList<List<String>>();
						for (int i = 0; i < checkboxStaticIdentifiers.size(); i++) {
							List<String> joined_string_list = new ArrayList<String>();
							
							if (static_split_id.contains(i)) {	// if this static layer must be split
								for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
									if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {	// If element of this static is checked		
										List<String> temp = new ArrayList<String>(); 
										temp.add(String.valueOf(i));
										temp.add(checkboxStaticIdentifiers.get(i).get(j).getText());	
										joined_string_list.add(String.join(" ", temp));
									}	
								}
								
								static_iterable_lists.add(joined_string_list);
							} else {		// if this static layer would not be split
								List<String> temp = new ArrayList<String>(); 
								temp.add(String.valueOf(i));
								for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
									if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {	// If element of this static is checked		
										temp.add(checkboxStaticIdentifiers.get(i).get(j).getText());	
									}	
								}
								joined_string_list.add(String.join(" ", temp));
								
								static_iterable_lists.add(joined_string_list);
							}	
						}		
						// Create lists
						List<String> static_info_list = new ArrayList<String>();
						// Get all permutation
						MixedRangeCombinationIterable<String> static_iterable = new MixedRangeCombinationIterable<String>(static_iterable_lists);
						for (List<String> element : static_iterable) {
							String joined_string = String.join(";", element);
							static_info_list.add(joined_string);
						}

						
						// for static description------------------------------------------------------------------------------------------------------
						List<List<String>> static_description_iterable_lists = new ArrayList<List<String>>();
						for (int i = 0; i < checkboxStaticIdentifiers.size(); i++) {
							List<String> joined_string_list = new ArrayList<String>();
							
							if (static_split_id.contains(i)) {	// if this static layer must be split
								for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
									if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {	// If element of this static is checked		
										List<String> temp = new ArrayList<String>(); 
										temp.add(static_identifiers_scrollpane.get_static_layer_title_as_checkboxes().get(i).getText());
										temp.add(checkboxStaticIdentifiers.get(i).get(j).getText());	
										joined_string_list.add(String.join(" ", temp));
									}	
								}
								
								static_description_iterable_lists.add(joined_string_list);
							} else {	// this else is almost the same as "get_static_description_from_GUI" --> disable this "else" if do not want the full description
								// Count the total of checked items in each layer 
								int total_check_items = 0;
								int total_items = 0;
								for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {		//Loop all elements in each layer
									if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {
										total_check_items++;
									}
									if (checkboxStaticIdentifiers.get(i).get(j).isVisible()) {
										total_items++;
									}	
								}
								// Add to description only when the total of checked items < the total items
								if (total_check_items < total_items) {
									String static_info = static_identifiers_scrollpane.get_static_layer_title_as_checkboxes().get(i).getText();
									
									for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {		//Loop all elements in each layer
										String checkboxName = checkboxStaticIdentifiers.get(i).get(j).getText();				
										// Add if it is (selected & visible)
										if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {
											static_info = String.join(" ", static_info, checkboxName);
										}
									}
									joined_string_list.add(static_info);
									static_description_iterable_lists.add(joined_string_list);
								}
							}
						}	
						// Create lists
						List<String> static_description_info_list = new ArrayList<String>();
						// Get all permutation
						MixedRangeCombinationIterable<String> description_iterable = new MixedRangeCombinationIterable<String>(static_description_iterable_lists);
						for (List<String> element : description_iterable) {
							String joined_string = String.join(" | ", element);
							static_description_info_list.add(joined_string);
						}
						
						
						// Add all constraints------------------------------------------------------------------------------------------------------
						if (total_constraints > 0) {
							rowCount5 = rowCount5 + total_constraints;
							data5 = new Object[rowCount5][colCount5];
							for (int i = 0; i < rowCount5 - total_constraints; i++) {
								for (int j = 0; j < colCount5; j++) {
									data5[i][j] = model5.getValueAt(i, j);
								}	
							}
							Object[][] temp_data = constraint_split_ScrollPanel.get_multiple_constraints_data();
							JCheckBox autoDescription = constraint_split_ScrollPanel.get_autoDescription();
							
							for (int i = rowCount5 - total_constraints; i < rowCount5; i++) {
								for (int j = 0; j < colCount5; j++) {
									if (autoDescription.isSelected()) {
										if (temp_data[0][1] == null) {
											data5[i][1] = /*"set constraint" + " " + (i - rowCount5 + total_constraints + 1) + " ..... " +*/ static_description_info_list.get(i - rowCount5 + total_constraints);
										} else {
											data5[i][1] = temp_data[0][1] /*+ " " + (i - rowCount5 + total_constraints + 1)*/ + " ..... " + static_description_info_list.get(i - rowCount5 + total_constraints);
										}
									} else {
										data5[i][1] = temp_data[0][1];
									}
									data5[i][0] = temp_data[0][0];
									data5[i][2] = static_info_list.get(i - rowCount5 + total_constraints);		// static splitter is active
									data5[i][3] = temp_data[0][3];
									data5[i][5] = true;
									
								}	
							}	
	
							model5.updateTableModelPrism(rowCount5, colCount5, data5, columnNames5);
							update_id();
							model5.fireTableDataChanged();
							quick_edit = new Panel_QuickEdit_Non_SR(table5, data5);		// 2 lines to update data for Quick Edit Panel
				 			scrollpane_QuickEdit.setViewportView(quick_edit);
							
							// Convert the new Row to model view and then select it 
							for (int i = rowCount5 - total_constraints; i < rowCount5; i++) {
								int newRow = table5.convertRowIndexToView(i);
								table5.addRowSelectionInterval(newRow, newRow);
							}	
							table5.scrollRectToVisible(new Rectangle(table5.getCellRect(table5.convertRowIndexToView(rowCount5 - total_constraints), 0, true)));
						}
					}
										
				}
			});			
			
			
			// Edit
			btn_Edit.addActionListener(e -> {
				if (table5.isEditing()) {
					table5.getCellEditor().stopCellEditing();
				}
				
				if (table5.isEnabled()) {			
					int selectedRow = table5.getSelectedRow();
					selectedRow = table5.convertRowIndexToModel(selectedRow);		// Convert row index because "Sort" causes problems	
	
					// Apply change	
					data5[selectedRow][2] = static_identifiers_scrollpane.get_static_info_from_GUI();
					model5.fireTableDataChanged();	
					
					// Convert the edited Row to model view and then select it 
					int editRow = table5.convertRowIndexToView(selectedRow);
					table5.setRowSelectionInterval(editRow, editRow);
					
					static_identifiers_scrollpane.highlight();
				} 
			});
			
			
			btn_Edit.addMouseListener(new MouseAdapter() { // Add listener
				public void mouseEntered(java.awt.event.MouseEvent e) {
					if (table5.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.highlight();
					}
				}

				public void mouseExited(java.awt.event.MouseEvent e) {
					if (table5.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.unhighlight();
					}
				}
			});
			
			
			// Spinner
		    spin_move_rows.addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
					int up_or_down = (int) spin_move_rows.getValue() - 1;										
					spin_move_rows.setValue((int) 1);	// Reset spinner value to 1
										
					if (up_or_down == 1) {	// move up
						// Cancel editing before moving conditions up or down
						if (table5.isEditing()) {
							table5.getCellEditor().cancelCellEditing();
						}	
						
						// Get selected rows
						int[] selectedRow = table5.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(0) > 0) {	// If there is at least 1 row selected & the first row is not selected
							for (int i = 0; i < rowCount5; i++) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount5; j++) {
										Object temp = data5[i - 1][j];
										data5[i - 1][j] = data5[i][j];
										data5[i][j] = temp;
									}
								}
							}							
							model5.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table5.addRowSelectionInterval(i - 1, i - 1);
							}
						}
					}
										
					if (up_or_down == -1) {	// move down						
						if (table5.isEditing()) {
							table5.getCellEditor().cancelCellEditing();	// cancel editing before moving conditions up or down
						}	
						
						// Get selected rows
						int[] selectedRow = table5.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(selectedRowList.size() - 1) < rowCount5 - 1) {	// If ...
							for (int i = rowCount5 - 1; i >= 0; i--) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount5; j++) {
										Object temp = data5[i + 1][j];
										data5[i + 1][j] = data5[i][j];
										data5[i][j] = temp;
									}
								}
							}						
							model5.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table5.addRowSelectionInterval(i + 1, i + 1);
							}	
						}						
					}
					
					// Scroll to the first row of the current selected rows
					table5.scrollRectToVisible(new Rectangle(table5.getCellRect(table5.convertRowIndexToView(table5.getSelectedRow()), 0, true)));	
		        }
		    });
		    
				
			// Delete
			btn_Delete.addActionListener(e -> {
				//Cancel editing before delete
				if (table5.isEditing()) {
					table5.getCellEditor().cancelCellEditing();
				}				
				
				String ExitOption[] = {"Delete", "Cancel"};
				int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), "Delete now?", "Confirm Delete",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[1]);
				if (response == 0) {
					// Get selected rows
					int[] selectedRow = table5.getSelectedRows();
					for (int i = 0; i < selectedRow.length; i++) {
						selectedRow[i] = table5.convertRowIndexToModel(selectedRow[i]);	///Convert row index because "Sort" causes problems
					}
					
					// Create a list of selected row indexes
					List<Integer> selected_Index = new ArrayList<Integer>();				
					for (int i: selectedRow) {
						selected_Index.add(i);
					}	
					
					// Get values to the new data5
					data5 = new Object[rowCount5 - selectedRow.length][colCount5];
					int newRow =0;
					for (int ii = 0; ii < rowCount5; ii++) {
						if (!selected_Index.contains(ii)) {			//If row not in the list then add to data5 row
							for (int jj = 0; jj < colCount5; jj++) {
								data5[newRow][jj] = model5.getValueAt(ii, jj);
							}
							newRow++;
						}
					}
					// Pass back the info to table model
					rowCount5 = rowCount5 - selectedRow.length;
					model5.updateTableModelPrism(rowCount5, colCount5, data5, columnNames5);
					model5.fireTableDataChanged();	
					quick_edit = new Panel_QuickEdit_Non_SR(table5, data5);	// 2 lines to update data for Quick Edit Panel
		 			scrollpane_QuickEdit.setViewportView(quick_edit);
				}
				
			});

			// End of Listeners for table8 & buttons -----------------------------------------------------------------------
			// End of Listeners for table8 & buttons -----------------------------------------------------------------------			
			
			
			
			
	        // scrollPane Quick Edit ----------------------------------------------------------------------	
			// scrollPane Quick Edit ----------------------------------------------------------------------	
			quick_edit = new Panel_QuickEdit_Non_SR(table5, data5);
 			scrollpane_QuickEdit = new JScrollPane(quick_edit);
 			border = new TitledBorder("Quick Edit");
 			border.setTitleJustification(TitledBorder.CENTER);
 			scrollpane_QuickEdit.setBorder(border);
 			scrollpane_QuickEdit.setVisible(false);	
 			
					
		
			
			// ToolBar Panel ----------------------------------------------------------------------------
			// ToolBar Panel ----------------------------------------------------------------------------
			ToolBarWithBgImage helpToolBar = new ToolBarWithBgImage("Project Tools", JToolBar.HORIZONTAL, null);
			helpToolBar.setFloatable(false);	//to make a tool bar immovable
			helpToolBar.setRollover(true);	//to visually indicate tool bar buttons when the user passes over them with the cursor
			helpToolBar.setBorderPainted(false);
			
			// button Quick Edit
			JToggleButton btnQuickEdit = new JToggleButton();
			btnQuickEdit.setToolTipText("Show Quick Edit Tool");
			btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
			btnQuickEdit.addActionListener(e -> {		
				if (btnQuickEdit.getToolTipText().equals("Show Quick Edit Tool")) {
						btnQuickEdit.setToolTipText("Hide Quick Edit Tool");
						btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_hide.png"));
						scrollpane_QuickEdit.setVisible(true);
						// Get everything show up nicely
						GUI_Text_splitpane.setLeftComponent(panel_Non_SR_Disturbances_GUI);
						Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				} else {
					btnQuickEdit.setToolTipText("Show Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
					scrollpane_QuickEdit.setVisible(false);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_Non_SR_Disturbances_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				}				
			});
			
			// button Help
			JButton btnHelp = new JButton();
			btnHelp.setToolTipText("Help");
			btnHelp.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_help.png"));
			btnHelp.addActionListener(e -> {
				
			});
			
			// Add all buttons to flow_panel
			helpToolBar.add(Box.createGlue());	//Add glue for Right alignment
			helpToolBar.add(btnQuickEdit);
			helpToolBar.add(btnHelp);
			// End of ToolBar Panel -----------------------------------------------------------------------
			// End of ToolBar Panel -----------------------------------------------------------------------
			
			
			
			
			
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			JSplitPane split_pane = new JSplitPane();
			split_pane.setBorder(null);
			split_pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			split_pane.setDividerSize(3);
						
			JPanel upper_panel = new JPanel();
			upper_panel.setBorder(null);
			upper_panel.setLayout(new GridBagLayout());			
			
			JPanel lower_panel = new JPanel();
			lower_panel.setBorder(null);
			lower_panel.setLayout(new GridBagLayout());
			
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
		    c.weighty = 1;
		    	    
		    // Add helpToolBar	
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0;
		    c.weighty = 0;
			c.gridwidth = 2;
			c.gridheight = 1;
			upper_panel.add(helpToolBar, c);				
			
			// Add the 1st grid - static_identifiers_scrollpane to the main Grid 
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 0.4;
		    c.weighty = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			upper_panel.add(static_identifiers_scrollpane, c);
			
			// Add the 2nd grid -  to the main Grid	
			c.gridx = 1;
			c.gridy = 1;
			c.weightx = 0.5;
		    c.weighty = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			upper_panel.add(infoScrollPane, c);
			
			// Add the button_table_Panel & scrollpane_QuickEdit to a new Panel then add that panel to the main Grid
			JPanel button_table_qedit_panel = new JPanel();
			button_table_qedit_panel.setLayout(new BorderLayout());
			button_table_qedit_panel.add(button_table_Panel, BorderLayout.CENTER);
			button_table_qedit_panel.add(scrollpane_QuickEdit, BorderLayout.EAST);			
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 2; 
			c.gridheight = 1;
			c.weightx = 1;
		    c.weighty = 1;
		    lower_panel.add(button_table_qedit_panel, c);
			
			
			split_pane.setLeftComponent(upper_panel);
			split_pane.setRightComponent(lower_panel);
			super.add(split_pane, BorderLayout.CENTER);	
		}
		
		
	    // Update id column. id needs to be unique in order to use in flow constraints-----------------
	    public void update_id() {  		
			List<Integer> id_list = new ArrayList<Integer>();			
			
			for (int row = 0; row < rowCount5; row++) {
				if (data5[row][0] != null) {
					id_list.add((int) data5[row][0]);
				}
			}			
			
			for (int row = 0; row < rowCount5; row++) {
				if (data5[row][0] == null) {
					int new_id = (id_list.size() > 0) ? Collections.max(id_list) + 1 : 1;	//new id = (max id + 1) or = 1 if no row
					data5[row][0] = new_id;
					id_list.add(new_id);
				}
			}			
		}
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	class SR_Disturbances_GUI extends JLayeredPane implements ActionListener {
		List<List<JCheckBox>> checkboxStaticIdentifiers;
		ScrollPane_StaticIdentifiers static_identifiers_scrollpane;
		ScrollPane_DynamicIdentifiers dynamic_identifiersScrollPanel;
		ScrollPane_SubTables_SR_Disturbances sr_disturbances_tables_ScrollPane;
		TableColumnsHandle table6a_handle, table6b_handle;
		Panel_QuickEdit_SR quick_edit;

		public SR_Disturbances_GUI() {
			setLayout(new BorderLayout());		
			
			
			// 1st grid ------------------------------------------------------------------------------		// Static identifiers	
			String panel_name = "Static Identifiers  -  use strata attributes to filter variables";
			static_identifiers_scrollpane = new ScrollPane_StaticIdentifiers(read_database, 2, panel_name);
			checkboxStaticIdentifiers = static_identifiers_scrollpane.get_CheckboxStaticIdentifiers();
			// End of 1st grid -----------------------------------------------------------------------

			
			// 2nd Grid ------------------------------------------------------------------------------		// Dynamic identifiers
			dynamic_identifiersScrollPanel = new ScrollPane_DynamicIdentifiers(read_database);
			// End of 2nd Grid -----------------------------------------------------------------------
				
					
			// 3rd grid ------------------------------------------------------------------------------		// Parameters
			create_table6a();
			create_table6b();
			create_table6c();
			create_table6d();
			table6a_handle = new TableColumnsHandle(table6a);
			table6b_handle = new TableColumnsHandle(table6b);
			table6a_handle.setColumnVisible(columnNames6a[1], false);	// hide layer5_regen 
			table6b_handle.setColumnVisible(columnNames6b[1], false);	// hide layer5_regen 
			sr_disturbances_tables_ScrollPane = new ScrollPane_SubTables_SR_Disturbances(table6a, data6a, table6b, data6b, table6c, data6c, table6d, data6d, total_replacing_disturbance);
			sr_disturbances_tables_ScrollPane.update_4_tables_data(data6a, data6b, data6c, data6d);
			// End of 3rd grid -----------------------------------------------------------------------
				    			
	
			// 4th Grid ------------------------------------------------------------------------------		// Buttons	
			// 4th Grid -----------------------------------------------------------------------------
			JPanel sr_disturbances_condition_panel = new JPanel(new GridBagLayout());
			TitledBorder border = new TitledBorder("Priority Conditons (top row = top priority, no row = no disturbance)");
			border.setTitleJustification(TitledBorder.CENTER);
			sr_disturbances_condition_panel.setBorder(border);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(0, 5, 10, 10); // padding top 0, left 5, bottom 10, right 10
			
			
			JButton btn_New = new JButton();
			btn_New.setFont(new Font(null, Font.BOLD, 14));
//			btn_New.setText("NEW SET");
			btn_New.setToolTipText("New");
			btn_New.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_add.png"));	
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0;
			c.weighty = 0;
			sr_disturbances_condition_panel.add(btn_New, c);		
			

			// Add Spinner to move priority up or down
			JSpinner spin_priority = new JSpinner (new SpinnerNumberModel(1, 0, 2, 1));
			spin_priority.setToolTipText("Move");
			JFormattedTextField SpinnerText = ((DefaultEditor) spin_priority.getEditor()).getTextField();
			SpinnerText.setHorizontalAlignment(JTextField.LEFT);
			SpinnerText.setEditable(false);
			SpinnerText.setFocusable(false);
//			DefaultFormatter formatter = (DefaultFormatter) SpinnerText.getFormatter();
//		    formatter.setCommitsOnValidEdit(true);
		    spin_priority.setEnabled(false);
		    c.gridx = 0;
			c.gridy = 1;
			c.weightx = 0;
			c.weighty = 0;
			sr_disturbances_condition_panel.add(spin_priority, c);
			
			
			JButton btn_Edit = new JButton();
//			btn_Edit.setText("EDIT");
			btn_Edit.setToolTipText("Modify");
			btn_Edit.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_swap.png"));
			btn_Edit.setEnabled(false);	
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 0;
			c.weighty = 0;
			sr_disturbances_condition_panel.add(btn_Edit, c);
		    

			JButton btn_Delete = new JButton();
			btn_Delete.setFont(new Font(null, Font.BOLD, 14));
//			btn_Delete.setText("DELETE");
			btn_Delete.setToolTipText("Delete");
			btn_Delete.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_erase.png"));
			btn_Delete.setEnabled(false);
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 0;
			c.weighty = 0;
			sr_disturbances_condition_panel.add(btn_Delete, c);

			create_mass_check_button(sr_disturbances_condition_panel, c, 0, 4, 0, 0).addActionListener(e -> apply_mass_check_or_uncheck("mass_check", model6, table6, data6, colCount6));
			create_mass_uncheck_button(sr_disturbances_condition_panel, c, 0, 5, 0, 0).addActionListener(e -> apply_mass_check_or_uncheck("mass_uncheck", model6, table6, data6, colCount6));
			
			
			// Add Empty Label to make all buttons on top not middle
			c.insets = new Insets(0, 0, 0, 0); // No padding
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 0;
			c.weighty = 1;
			sr_disturbances_condition_panel.add(new JLabel(), c);
			
			// Add table7		
			create_table6();
			JScrollPane table_ScrollPane = new JScrollPane(table6);
			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.gridheight = 7;
			sr_disturbances_condition_panel.add(table_ScrollPane, c);
						
			
			// Add Listeners for buttons----------------------------------------------------------
			// Add Listeners for buttons----------------------------------------------------------							
			// table6
			class Table_Interaction {
				void refresh() {
					// Cancel editing before moving conditions up or down
					if (table6a.isEditing()) {
						table6a.getCellEditor().cancelCellEditing();
					}		
					if (table6c.isEditing()) {
						table6c.getCellEditor().cancelCellEditing();
					}
										
					int[] selectedRow = table6.getSelectedRows();
					
					if (selectedRow.length == 1) {		// Show the set's identifiers
						int currentRow = selectedRow[0];
						currentRow = table6.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
						static_identifiers_scrollpane.reload_this_constraint_static_identifiers((String) data6[currentRow][6]);	// 6 is the static_identifiers which have some attributes selected				
						dynamic_identifiersScrollPanel.reload_this_constraint_dynamic_identifiers((String) data6[currentRow][7], (String) data6[currentRow][8]);	// 6 is the original_dynamic_identifiers column
						sr_disturbances_tables_ScrollPane.reload_this_condition((String) data6[currentRow][2], (String) data6[currentRow][3], (String) data6[currentRow][4], (String) data6[currentRow][5]);
						
						btn_Edit.setEnabled(true);
						quick_edit.enable_all_apply_buttons();
						sr_disturbances_tables_ScrollPane.show_4_tables();
					} else {		// Disable Edit
						btn_Edit.setEnabled(false);
						quick_edit.disable_all_apply_buttons();
						sr_disturbances_tables_ScrollPane.hide_4_tables();
					}
					
					if (selectedRow.length >= 1 && table6.isEnabled()) {	// Enable Delete  when: >=1 row is selected, table is enable (often after Edit button finished its task)
						btn_Delete.setEnabled(true);
					} else {		// Disable Delete & Spinner
						btn_Delete.setEnabled(false);
					}	
					
					if (selectedRow.length >= 1) {	// Enable Spinner when: >=1 row is selected
						spin_priority.setEnabled(true);
					} else {		// Disable Delete & Spinner
						spin_priority.setEnabled(false);
					}
				}
			}
			Table_Interaction table_interaction = new Table_Interaction();
			table6.addMouseListener(new MouseAdapter() { // Add listener
				@Override
				public void mouseReleased(MouseEvent e) {
					table_interaction.refresh();
				}
			});
			
			table6.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	table_interaction.refresh();
		        }
		    });
			
			table6a.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	int currentRow = table6.getSelectedRow();		        	
					currentRow = table6.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
					sr_disturbances_tables_ScrollPane.update_4_tables_data(data6a, data6b, data6c, data6d);	// Update so we have the latest data of table 6a & 6c to retrieve and write to table6 below
					data6[currentRow][2] = sr_disturbances_tables_ScrollPane.get_lr_mean_from_GUI();	
					model6.fireTableCellUpdated(currentRow, 2);
		        }
		    });
			
			table6b.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	int currentRow = table6.getSelectedRow();		        	
					currentRow = table6.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
					sr_disturbances_tables_ScrollPane.update_4_tables_data(data6a, data6b, data6c, data6d);	// Update so we have the latest data of table 6a & 6c to retrieve and write to table6 below
					data6[currentRow][3] = sr_disturbances_tables_ScrollPane.get_lr_std_from_GUI();	
					model6.fireTableCellUpdated(currentRow, 3);
		        }
		    });
			
			table6c.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	int currentRow = table6.getSelectedRow();		        	
					currentRow = table6.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
					sr_disturbances_tables_ScrollPane.update_4_tables_data(data6a, data6b, data6c, data6d);	// Update so we have the latest data of table 6a & 6c to retrieve and write to table6 below
					data6[currentRow][4] = sr_disturbances_tables_ScrollPane.get_cr_mean_from_GUI();		
					model6.fireTableCellUpdated(currentRow, 4);
		        }
		    });
			
			table6d.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	int currentRow = table6.getSelectedRow();		        	
					currentRow = table6.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
					sr_disturbances_tables_ScrollPane.update_4_tables_data(data6a, data6b, data6c, data6d);	// Update so we have the latest data of table 6a & 6c to retrieve and write to table6 below
					data6[currentRow][5] = sr_disturbances_tables_ScrollPane.get_cr_std_from_GUI();		
					model6.fireTableCellUpdated(currentRow, 5);
		        }
		    });
			
			
			// New Condition
			btn_New.addActionListener(e -> {	
				if (table6.isEditing()) {
					table6.getCellEditor().stopCellEditing();
				}
				
				// Add 1 row
				rowCount6++;
				data6 = new Object[rowCount6][colCount6];
				for (int ii = 0; ii < rowCount6 - 1; ii++) {
					for (int jj = 0; jj < colCount6; jj++) {
						data6[ii][jj] = model6.getValueAt(ii, jj);
					}	
				}
					
				data6[rowCount6 - 1][1] = String.join(" ..... ",
						dynamic_identifiersScrollPanel.get_dynamic_description_from_GUI(),
						static_identifiers_scrollpane.get_static_description_from_GUI());
				data6[rowCount6 - 1][2] = sr_disturbances_tables_ScrollPane.get_lr_mean_from_GUI();
				data6[rowCount6 - 1][3] = sr_disturbances_tables_ScrollPane.get_lr_std_from_GUI();
				data6[rowCount6 - 1][4] = sr_disturbances_tables_ScrollPane.get_cr_mean_from_GUI();
				data6[rowCount6 - 1][5] = sr_disturbances_tables_ScrollPane.get_cr_std_from_GUI();
				data6[rowCount6 - 1][6] = static_identifiers_scrollpane.get_static_info_from_GUI();
				data6[rowCount6 - 1][7] = dynamic_identifiersScrollPanel.get_dynamic_info_from_GUI();
				data6[rowCount6 - 1][8] = dynamic_identifiersScrollPanel.get_original_dynamic_info_from_GUI();
				data6[rowCount6 - 1][9] = true;
								
				model6.updateTableModelPrism(rowCount6, colCount6, data6, columnNames6);
				model6.fireTableDataChanged();		
				
				// Convert the new Row to model view and then select it 
				int newRow = table6.convertRowIndexToView(rowCount6 - 1);
				table6.setRowSelectionInterval(newRow, newRow);
				update_id();
				table6.scrollRectToVisible(new Rectangle(table6.getCellRect(newRow, 0, true)));	
			});

			
			// Edit
			btn_Edit.addActionListener(e -> {
				if (table6.isEditing()) {
					table6.getCellEditor().stopCellEditing();
				}
				
				if (table6.isEnabled()) {
					int selectedRow = table6.getSelectedRow();
					selectedRow = table6.convertRowIndexToModel(selectedRow);		// Convert row index because "Sort" causes problems	
					
					// Apply change
					data6[selectedRow][6] = static_identifiers_scrollpane.get_static_info_from_GUI();
					data6[selectedRow][7] = dynamic_identifiersScrollPanel.get_dynamic_info_from_GUI();
					data6[selectedRow][8] = dynamic_identifiersScrollPanel.get_original_dynamic_info_from_GUI();
					model6.fireTableDataChanged();	
					
					// Convert the edited Row to model view and then select it 
					int editRow = table6.convertRowIndexToView(selectedRow);
					table6.setRowSelectionInterval(editRow, editRow);
					
					static_identifiers_scrollpane.highlight();
					dynamic_identifiersScrollPanel.highlight();			
				} 
			});			
			
			
			btn_Edit.addMouseListener(new MouseAdapter() { // Add listener
				public void mouseEntered(java.awt.event.MouseEvent e) {
					if (table6.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.highlight();
						dynamic_identifiersScrollPanel.highlight();
					}
				}

				public void mouseExited(java.awt.event.MouseEvent e) {
					if (table6.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.unhighlight();
						dynamic_identifiersScrollPanel.unhighlight();
					}
				}
			});
			
			
			// Spinner
			spin_priority.addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
					int up_or_down = (int) spin_priority.getValue() - 1;										
					spin_priority.setValue((int) 1);	// Reset spinner value to 1
										
					if (up_or_down == 1) {	// move up
						// Cancel editing before moving conditions up or down
						if (table6.isEditing()) {
							table6.getCellEditor().cancelCellEditing();
						}	
						
						// Get selected rows
						int[] selectedRow = table6.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(0) > 0) {	// If there is at least 1 row selected & the first row is not selected
							for (int i = 0; i < rowCount6; i++) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount6; j++) {
										Object temp = data6[i - 1][j];
										data6[i - 1][j] = data6[i][j];
										data6[i][j] = temp;
									}
								}
							}							
							model6.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table6.addRowSelectionInterval(i - 1, i - 1);
							}
						}
					}
										
					if (up_or_down == -1) {	// move down						
						if (table6.isEditing()) {
							table6.getCellEditor().cancelCellEditing();	// cancel editing before moving conditions up or down
						}	
						
						// Get selected rows
						int[] selectedRow = table6.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(selectedRowList.size() - 1) < rowCount6 - 1) {	// If ...
							for (int i = rowCount6 - 1; i >= 0; i--) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount6; j++) {
										Object temp = data6[i + 1][j];
										data6[i + 1][j] = data6[i][j];
										data6[i][j] = temp;
									}
								}
							}						
							model6.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table6.addRowSelectionInterval(i + 1, i + 1);
							}	
						}						
					}
					
					// Scroll to the first row of the current selected rows
					table6.scrollRectToVisible(new Rectangle(table6.getCellRect(table6.convertRowIndexToView(table6.getSelectedRow()), 0, true)));	
		        }
		    });
		    
			
			// Delete
			btn_Delete.addActionListener(e -> {
				//Cancel editing before delete
				if (table6.isEditing()) {
					table6.getCellEditor().cancelCellEditing();
				}				
				
				String ExitOption[] = {"Delete", "Cancel"};
				int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), "Delete now?", "Confirm Delete",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[0]);
				if (response == 0) {
					// Get selected rows
					int[] selectedRow = table6.getSelectedRows();	
					for (int i = 0; i < selectedRow.length; i++) {
						selectedRow[i] = table6.convertRowIndexToModel(selectedRow[i]);	///Convert row index because "Sort" causes problems
					}
					
					// Create a list of selected row indexes
					List<Integer> selected_Index = new ArrayList<Integer>();				
					for (int i: selectedRow) {
						selected_Index.add(i);
					}	
					
					// Get values to the new data7
					data6 = new Object[rowCount6 - selectedRow.length][colCount6];
					int newRow =0;
					for (int ii = 0; ii < rowCount6; ii++) {
						if (!selected_Index.contains(ii)) {			//If row not in the list then add to data7 row
							for (int jj = 0; jj < colCount6; jj++) {
								data6[newRow][jj] = model6.getValueAt(ii, jj);
							}
							newRow++;
						}
					}
					// Pass back the info to table model
					rowCount6 = rowCount6 - selectedRow.length;
					model6.updateTableModelPrism(rowCount6, colCount6, data6, columnNames6);
					
					model6.fireTableDataChanged();	
				}
			});			
			// End of Listeners for table8 & buttons -----------------------------------------------------------------------
			// End of Listeners for table8 & buttons -----------------------------------------------------------------------		    
		    
			
			

			
			// scrollPane Quick Edit ----------------------------------------------------------------------	
			// scrollPane Quick Edit ----------------------------------------------------------------------	
			quick_edit = new Panel_QuickEdit_SR(table6a, data6a, table6b, data6b, table6c, data6c, table6d, data6d, sr_disturbances_tables_ScrollPane.getEditorTable());
			quick_edit.disable_all_apply_buttons();
			JScrollPane scrollpane_QuickEdit = new JScrollPane(quick_edit);			
			border = new TitledBorder("Quick Edit ");
 			border.setTitleJustification(TitledBorder.CENTER);
 			scrollpane_QuickEdit.setBorder(border);
 			scrollpane_QuickEdit.setVisible(false);
 
 						
			// ToolBar Panel ----------------------------------------------------------------------------
			// ToolBar Panel ----------------------------------------------------------------------------
			ToolBarWithBgImage helpToolBar = new ToolBarWithBgImage("Project Tools", JToolBar.HORIZONTAL, null);
			helpToolBar.setFloatable(false);	//to make a tool bar immovable
			helpToolBar.setRollover(true);	//to visually indicate tool bar buttons when the user passes over them with the cursor
			helpToolBar.setBorderPainted(false);
			
			// button Quick Edit
 			JToggleButton btnQuickEdit = new JToggleButton();
 			btnQuickEdit.setToolTipText("Show Quick Edit Tool");
 			btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
 			btnQuickEdit.addActionListener(e -> {
 				if (btnQuickEdit.getToolTipText().equals("Show Quick Edit Tool")) {
					btnQuickEdit.setToolTipText("Hide Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_hide.png"));
					scrollpane_QuickEdit.setVisible(true);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_SR_Disturbances_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				} else {
					btnQuickEdit.setToolTipText("Show Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
					scrollpane_QuickEdit.setVisible(false);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_SR_Disturbances_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				}
 			});				
			
			// button Help
			JButton btnHelp = new JButton();
			btnHelp.setToolTipText("Help");
			btnHelp.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_help.png"));
			btnHelp.addActionListener(e -> {

			});
			
			// Add all buttons to flow_panel
			helpToolBar.add(Box.createGlue());	//Add glue for Right alignment
			helpToolBar.add(btnQuickEdit);
			helpToolBar.add(btnHelp);
			// End of ToolBar Panel -----------------------------------------------------------------------
			// End of ToolBar Panel ----------------------------------------------------------------------- 
	
						
			
			// Add 3 tables into the same panel
			JPanel combine_panel = new JPanel(new GridBagLayout());
			c = new GridBagConstraints();
			// Add the sr_disturbances_tables_ScrollPane to the main Grid	
			combine_panel.add(sr_disturbances_tables_ScrollPane, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
					0, 0, 1, 1, 0.5, 1, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
					0, 0, 0, 0));		// insets top, left, bottom, right
	
			// Add scrollpane_QuickEdit	
			combine_panel.add(scrollpane_QuickEdit, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
					0, 1, 1, 1, 0.5, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
					0, 0, 0, 0));		// insets top, left, bottom, right
			JSplitPane split_pane_lower = new JSplitPane();
			split_pane_lower.setBorder(null);
			split_pane_lower.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			split_pane_lower.setDividerSize(3);
			sr_disturbances_condition_panel.setPreferredSize(new Dimension(535, 0));
			split_pane_lower.setLeftComponent(sr_disturbances_condition_panel);
			split_pane_lower.setRightComponent(combine_panel);
						
			
			    			    
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			JSplitPane split_pane = new JSplitPane();
			split_pane.setBorder(null);
			split_pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			split_pane.setDividerSize(3);
						
			JPanel upper_panel = new JPanel();
			upper_panel.setBorder(null);
			upper_panel.setLayout(new GridBagLayout());			
			
			JPanel lower_panel = new JPanel();
			lower_panel.setBorder(null);
			lower_panel.setLayout(new GridBagLayout());
			
			
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;

			// Add helpToolBar to the main Grid
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 2;
			c.gridheight = 1;
			c.weightx = 0;
		    c.weighty = 0;
		    upper_panel.add(helpToolBar, c);				
			
			// Add static_identifiers_scrollpane to the main Grid
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 0.4;
		    c.weighty = 1;
		    upper_panel.add(static_identifiers_scrollpane, c);				
		    		
			// Add dynamic_identifiersPanel to the main Grid
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 0.5;
			c.weighty = 1;
			upper_panel.add(dynamic_identifiersScrollPanel, c);	
						    				
			// Add the combine_panel to the main Grid	
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 2;
			c.gridheight = 1;
			c.weightx = 1;
		    c.weighty = 1;
		    lower_panel.add(split_pane_lower, c);	
			
			
			split_pane.setLeftComponent(upper_panel);
			split_pane.setRightComponent(lower_panel);
			super.add(split_pane, BorderLayout.CENTER);
			
			
			// when radioButton_Right[5] is selected, time period GUI will be updated
			radio_button[5].addActionListener(this);			
		}
		
		// Listener for this class----------------------------------------------------------------------
		public void actionPerformed(ActionEvent e) {	    	
	    	// Update GUI for time period 
	    	for (int j = 0; j < checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).size(); j++) {			//The last element is Time period			
				if (j < total_period) {
					checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).get(j).setVisible(true);		//Periods to be visible 			
				} else {
					checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).get(j).setVisible(false);		//Periods to be invisible
					checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).get(j).setSelected(false);		//Periods to be unselected
				}
			}  	
	    	
	      	// Update Dynamic Identifier Panel
	    	if (read_database.get_yield_tables_column_names() != null && dynamic_identifiersScrollPanel.get_allDynamicIdentifiers() == null) {
	    		dynamic_identifiersScrollPanel = new ScrollPane_DynamicIdentifiers(read_database);	// "Get identifiers from yield table columns"
	    	}	
		}	
		
	    // Update set_id column. set_id needs to be unique-----------------
	    public void update_id() {  		
			List<Integer> id_list = new ArrayList<Integer>();			
		
			for (int row = 0; row < rowCount6; row++) {
				if (data6[row][0] != null) {
					id_list.add(Integer.valueOf((String) data6[row][0].toString()/*.replace("Set ", "")*/));
				}
			}			
			
			for (int row = 0; row < rowCount6; row++) {
				if (data6[row][0] == null) {
					int new_id = (id_list.size() > 0) ? Collections.max(id_list) + 1 : 1;	//new id = (max id + 1) or = 1 if no row
					data6[row][0] = /*"Set " + */new_id;
					id_list.add(new_id);
				}
			}			
		}
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	class Management_Cost_GUI extends JLayeredPane implements ActionListener {
		List<List<JCheckBox>> checkboxStaticIdentifiers;
		ScrollPane_StaticIdentifiers static_identifiers_scrollpane;
		ScrollPane_DynamicIdentifiers dynamic_identifiersScrollPanel;
		ScrollPane_SubTables_ManagementCost cost_tables_ScrollPane;
		Panel_QuickEdit_ManagementCost quick_edit;

		public Management_Cost_GUI() {
			setLayout(new BorderLayout());		
			
			
			// 1st grid ------------------------------------------------------------------------------		// Static identifiers	
			String panel_name = "Static Identifiers  -  use strata attributes to filter variables";
			static_identifiers_scrollpane = new ScrollPane_StaticIdentifiers(read_database, 2, panel_name);
			checkboxStaticIdentifiers = static_identifiers_scrollpane.get_CheckboxStaticIdentifiers();
			// End of 1st grid -----------------------------------------------------------------------

			
			// 2nd Grid ------------------------------------------------------------------------------		// Dynamic identifiers
			dynamic_identifiersScrollPanel = new ScrollPane_DynamicIdentifiers(read_database);
			// End of 2nd Grid -----------------------------------------------------------------------
				
					
			// 3rd grid ------------------------------------------------------------------------------		// Parameters
			create_table7a();
			create_table7b();
//			model7a.match_DataType();		//a smart way to retrieve the original data type :))))))
//			model7b.match_DataType();		//a smart way to retrieve the original data type :))))))
			cost_tables_ScrollPane = new ScrollPane_SubTables_ManagementCost(table7a, data7a, columnNames7a, table7b, data7b, columnNames7b);
			cost_tables_ScrollPane.update_2_tables_data(data7a, data7b);
			// End of 3rd grid -----------------------------------------------------------------------
				    			
	
			// 4th Grid ------------------------------------------------------------------------------		// Buttons	
			// 4th Grid -----------------------------------------------------------------------------
			JPanel cost_condition_panel = new JPanel(new GridBagLayout());
			TitledBorder border = new TitledBorder("Priority Conditons (top row = top priority, no row = no cost)");
			border.setTitleJustification(TitledBorder.CENTER);
			cost_condition_panel.setBorder(border);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(0, 5, 10, 10); // padding top 0, left 5, bottom 10, right 10
			
			
			JButton btn_New = new JButton();
			btn_New.setFont(new Font(null, Font.BOLD, 14));
//			btn_New.setText("NEW SET");
			btn_New.setToolTipText("New");
			btn_New.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_add.png"));	
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0;
			c.weighty = 0;
			cost_condition_panel.add(btn_New, c);		
			

			// Add Spinner to move priority up or down
			JSpinner spin_priority = new JSpinner (new SpinnerNumberModel(1, 0, 2, 1));
			spin_priority.setToolTipText("Move");
			JFormattedTextField SpinnerText = ((DefaultEditor) spin_priority.getEditor()).getTextField();
			SpinnerText.setHorizontalAlignment(JTextField.LEFT);
			SpinnerText.setEditable(false);
			SpinnerText.setFocusable(false);
//			DefaultFormatter formatter = (DefaultFormatter) SpinnerText.getFormatter();
//		    formatter.setCommitsOnValidEdit(true);
		    spin_priority.setEnabled(false);
		    c.gridx = 0;
			c.gridy = 1;
			c.weightx = 0;
			c.weighty = 0;
			cost_condition_panel.add(spin_priority, c);
			
			
			JButton btn_Edit = new JButton();
//			btn_Edit.setText("EDIT");
			btn_Edit.setToolTipText("Modify");
			btn_Edit.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_swap.png"));
			btn_Edit.setEnabled(false);	
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 0;
			c.weighty = 0;
			cost_condition_panel.add(btn_Edit, c);
		    

			JButton btn_Delete = new JButton();
			btn_Delete.setFont(new Font(null, Font.BOLD, 14));
//			btn_Delete.setText("DELETE");
			btn_Delete.setToolTipText("Delete");
			btn_Delete.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_erase.png"));
			btn_Delete.setEnabled(false);
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 0;
			c.weighty = 0;
			cost_condition_panel.add(btn_Delete, c);

			create_mass_check_button(cost_condition_panel, c, 0, 4, 0, 0).addActionListener(e -> apply_mass_check_or_uncheck("mass_check", model7, table7, data7, colCount7));
			create_mass_uncheck_button(cost_condition_panel, c, 0, 5, 0, 0).addActionListener(e -> apply_mass_check_or_uncheck("mass_uncheck", model7, table7, data7, colCount7));

			
			// Add Empty Label to make all buttons on top not middle
			c.insets = new Insets(0, 0, 0, 0); // No padding			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 0;
			c.weighty = 1;
			cost_condition_panel.add(new JLabel(), c);
			
			// Add table7				
			create_table7();
			JScrollPane table_ScrollPane = new JScrollPane(table7);
			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.gridheight = 7;
			cost_condition_panel.add(table_ScrollPane, c);
						
			
			// Add Listeners for buttons----------------------------------------------------------
			// Add Listeners for buttons----------------------------------------------------------							
			// table7
			class Table_Interaction {
				void refresh() {
					// Cancel editing before moving conditions up or down
					if (table7a.isEditing()) {
						table7a.getCellEditor().cancelCellEditing();
					}		
					if (table7b.isEditing()) {
						table7b.getCellEditor().cancelCellEditing();
					}
										
					int[] selectedRow = table7.getSelectedRows();
					
					if (selectedRow.length == 1) {		// Show the set's identifiers
						int currentRow = selectedRow[0];
						currentRow = table7.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
						static_identifiers_scrollpane.reload_this_constraint_static_identifiers((String) data7[currentRow][4]);	// 4 is the static_identifiers which have some attributes selected				
						dynamic_identifiersScrollPanel.reload_this_constraint_dynamic_identifiers((String) data7[currentRow][5], (String) data7[currentRow][6]);	// 6 is the original_dynamic_identifiers column
						cost_tables_ScrollPane.reload_this_condition_action_cost_and_conversion_cost((String) data7[currentRow][2], (String) data7[currentRow][3]);
						cost_tables_ScrollPane.show_active_columns_after_reload();
						btn_Edit.setEnabled(true);
						quick_edit.enable_all_apply_buttons();
						cost_tables_ScrollPane.show_2_tables();
					} else {		// Disable Edit
						btn_Edit.setEnabled(false);
						quick_edit.disable_all_apply_buttons();
						cost_tables_ScrollPane.hide_2_tables();
					}
					
					if (selectedRow.length >= 1 && table7.isEnabled()) {	// Enable Delete  when: >=1 row is selected, table is enable (often after Edit button finished its task)
						btn_Delete.setEnabled(true);
					} else {		// Disable Delete & Spinner
						btn_Delete.setEnabled(false);
					}	
					
					if (selectedRow.length >= 1) {	// Enable Spinner when: >=1 row is selected
						spin_priority.setEnabled(true);
					} else {		// Disable Delete & Spinner
						spin_priority.setEnabled(false);
					}
				}
			}
			Table_Interaction table_interaction = new Table_Interaction();
			table7.addMouseListener(new MouseAdapter() { // Add listener
				@Override
				public void mouseReleased(MouseEvent e) {
					table_interaction.refresh();
				}
			});
			
			table7.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	table_interaction.refresh();
		        }
		    });
			
			table7a.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	int currentRow = table7.getSelectedRow();		        	
					currentRow = table7.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
					cost_tables_ScrollPane.update_2_tables_data(data7a, data7b);	// Update so we have the latest data of table 8a & 8b to retrieve and write to table7 below
					data7[currentRow][2] = cost_tables_ScrollPane.get_action_cost_info_from_GUI();		
					model7.fireTableCellUpdated(currentRow, 2);
		        }
		    });
			
			table7b.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	int currentRow = table7.getSelectedRow();		        	
					currentRow = table7.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
					cost_tables_ScrollPane.update_2_tables_data(data7a, data7b);	// Update so we have the latest data of table 8a & 8b to retrieve and write to table7 below
					data7[currentRow][3] = cost_tables_ScrollPane.get_conversion_cost_info_from_GUI();		
					model7.fireTableCellUpdated(currentRow, 3);
		        }
		    });
			
			
			// New Condition
			btn_New.addActionListener(e -> {	
				if (table7.isEditing()) {
					table7.getCellEditor().stopCellEditing();
				}
				
				// Add 1 row
				rowCount7++;
				data7 = new Object[rowCount7][colCount7];
				for (int ii = 0; ii < rowCount7 - 1; ii++) {
					for (int jj = 0; jj < colCount7; jj++) {
						data7[ii][jj] = model7.getValueAt(ii, jj);
					}	
				}
					
				data7[rowCount7 - 1][1] = String.join(" ..... ",
						dynamic_identifiersScrollPanel.get_dynamic_description_from_GUI(),
						static_identifiers_scrollpane.get_static_description_from_GUI());
				data7[rowCount7 - 1][2] = cost_tables_ScrollPane.get_action_cost_info_from_GUI();
				data7[rowCount7 - 1][3] = cost_tables_ScrollPane.get_conversion_cost_info_from_GUI();
				data7[rowCount7 - 1][4] = static_identifiers_scrollpane.get_static_info_from_GUI();
				data7[rowCount7 - 1][5] = dynamic_identifiersScrollPanel.get_dynamic_info_from_GUI();
				data7[rowCount7 - 1][6] = dynamic_identifiersScrollPanel.get_original_dynamic_info_from_GUI();
				data7[rowCount7 - 1][7] = true;
								
				model7.updateTableModelPrism(rowCount7, colCount7, data7, columnNames7);
				model7.fireTableDataChanged();		
				
				// Convert the new Row to model view and then select it 
				int newRow = table7.convertRowIndexToView(rowCount7 - 1);
				table7.setRowSelectionInterval(newRow, newRow);
				update_id();
				table7.scrollRectToVisible(new Rectangle(table7.getCellRect(newRow, 0, true)));	
			});

			
			// Edit
			btn_Edit.addActionListener(e -> {
				if (table7.isEditing()) {
					table7.getCellEditor().stopCellEditing();
				}
				
				if (table7.isEnabled()) {
					int selectedRow = table7.getSelectedRow();
					selectedRow = table7.convertRowIndexToModel(selectedRow);		// Convert row index because "Sort" causes problems	
					
					// Apply change
					data7[selectedRow][4] = static_identifiers_scrollpane.get_static_info_from_GUI();
					data7[selectedRow][5] = dynamic_identifiersScrollPanel.get_dynamic_info_from_GUI();
					data7[selectedRow][6] = dynamic_identifiersScrollPanel.get_original_dynamic_info_from_GUI();
					model7.fireTableDataChanged();	
					
					// Convert the edited Row to model view and then select it 
					int editRow = table7.convertRowIndexToView(selectedRow);
					table7.setRowSelectionInterval(editRow, editRow);
					
					static_identifiers_scrollpane.highlight();
					dynamic_identifiersScrollPanel.highlight();			
				} 
			});			
			
			
			btn_Edit.addMouseListener(new MouseAdapter() { // Add listener
				public void mouseEntered(java.awt.event.MouseEvent e) {
					if (table7.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.highlight();
						dynamic_identifiersScrollPanel.highlight();
					}
				}

				public void mouseExited(java.awt.event.MouseEvent e) {
					if (table7.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.unhighlight();
						dynamic_identifiersScrollPanel.unhighlight();
					}
				}
			});
			
			
			// Spinner
			spin_priority.addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
					int up_or_down = (int) spin_priority.getValue() - 1;										
					spin_priority.setValue((int) 1);	// Reset spinner value to 1
										
					if (up_or_down == 1) {	// move up
						// Cancel editing before moving conditions up or down
						if (table7.isEditing()) {
							table7.getCellEditor().cancelCellEditing();
						}	
						
						// Get selected rows
						int[] selectedRow = table7.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(0) > 0) {	// If there is at least 1 row selected & the first row is not selected
							for (int i = 0; i < rowCount7; i++) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount7; j++) {
										Object temp = data7[i - 1][j];
										data7[i - 1][j] = data7[i][j];
										data7[i][j] = temp;
									}
								}
							}							
							model7.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table7.addRowSelectionInterval(i - 1, i - 1);
							}
						}
					}
										
					if (up_or_down == -1) {	// move down						
						if (table7.isEditing()) {
							table7.getCellEditor().cancelCellEditing();	// cancel editing before moving conditions up or down
						}	
						
						// Get selected rows
						int[] selectedRow = table7.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(selectedRowList.size() - 1) < rowCount7 - 1) {	// If ...
							for (int i = rowCount7 - 1; i >= 0; i--) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount7; j++) {
										Object temp = data7[i + 1][j];
										data7[i + 1][j] = data7[i][j];
										data7[i][j] = temp;
									}
								}
							}						
							model7.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table7.addRowSelectionInterval(i + 1, i + 1);
							}	
						}						
					}
					
					// Scroll to the first row of the current selected rows
					table7.scrollRectToVisible(new Rectangle(table7.getCellRect(table7.convertRowIndexToView(table7.getSelectedRow()), 0, true)));	
		        }
		    });
		    
			
			// Delete
			btn_Delete.addActionListener(e -> {
				//Cancel editing before delete
				if (table7.isEditing()) {
					table7.getCellEditor().cancelCellEditing();
				}				
				
				String ExitOption[] = {"Delete", "Cancel"};
				int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), "Delete now?", "Confirm Delete",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[0]);
				if (response == 0) {
					// Get selected rows
					int[] selectedRow = table7.getSelectedRows();	
					for (int i = 0; i < selectedRow.length; i++) {
						selectedRow[i] = table7.convertRowIndexToModel(selectedRow[i]);	///Convert row index because "Sort" causes problems
					}
					
					// Create a list of selected row indexes
					List<Integer> selected_Index = new ArrayList<Integer>();				
					for (int i: selectedRow) {
						selected_Index.add(i);
					}	
					
					// Get values to the new data7
					data7 = new Object[rowCount7 - selectedRow.length][colCount7];
					int newRow =0;
					for (int ii = 0; ii < rowCount7; ii++) {
						if (!selected_Index.contains(ii)) {			//If row not in the list then add to data7 row
							for (int jj = 0; jj < colCount7; jj++) {
								data7[newRow][jj] = model7.getValueAt(ii, jj);
							}
							newRow++;
						}
					}
					// Pass back the info to table model
					rowCount7 = rowCount7 - selectedRow.length;
					model7.updateTableModelPrism(rowCount7, colCount7, data7, columnNames7);
					
					model7.fireTableDataChanged();	
				}
			});			
			// End of Listeners for table8 & buttons -----------------------------------------------------------------------
			// End of Listeners for table8 & buttons -----------------------------------------------------------------------		    
		    
			
			
			
			
			// scrollPane Quick Edit ----------------------------------------------------------------------	
			// scrollPane Quick Edit ----------------------------------------------------------------------	
			quick_edit = new Panel_QuickEdit_ManagementCost(read_database, table7a, data7a, columnNames7a, table7b, data7b, cost_tables_ScrollPane.getEditorTable());
			quick_edit.disable_all_apply_buttons();
			JScrollPane scrollpane_QuickEdit = new JScrollPane(quick_edit);			
			border = new TitledBorder("Quick Edit ");
 			border.setTitleJustification(TitledBorder.CENTER);
 			scrollpane_QuickEdit.setBorder(border);
 			scrollpane_QuickEdit.setVisible(false);
 
 						
			// ToolBar Panel ----------------------------------------------------------------------------
			// ToolBar Panel ----------------------------------------------------------------------------
			ToolBarWithBgImage helpToolBar = new ToolBarWithBgImage("Project Tools", JToolBar.HORIZONTAL, null);
			helpToolBar.setFloatable(false);	//to make a tool bar immovable
			helpToolBar.setRollover(true);	//to visually indicate tool bar buttons when the user passes over them with the cursor
			helpToolBar.setBorderPainted(false);
			
			// button Quick Edit
 			JToggleButton btnQuickEdit = new JToggleButton();
 			btnQuickEdit.setToolTipText("Show Quick Edit Tool");
 			btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
 			btnQuickEdit.addActionListener(e -> {
 				if (btnQuickEdit.getToolTipText().equals("Show Quick Edit Tool")) {
					btnQuickEdit.setToolTipText("Hide Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_hide.png"));
					scrollpane_QuickEdit.setVisible(true);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_Management_Cost_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				} else {
					btnQuickEdit.setToolTipText("Show Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
					scrollpane_QuickEdit.setVisible(false);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_Management_Cost_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				}
 			});				
			
			// button Help
			JButton btnHelp = new JButton();
			btnHelp.setToolTipText("Help");
			btnHelp.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_help.png"));
			btnHelp.addActionListener(e -> {

			});
			
			// Add all buttons to flow_panel
			helpToolBar.add(Box.createGlue());	//Add glue for Right alignment
			helpToolBar.add(btnQuickEdit);
			helpToolBar.add(btnHelp);
			// End of ToolBar Panel -----------------------------------------------------------------------
			// End of ToolBar Panel ----------------------------------------------------------------------- 
			
						
			
			// Add 3 tables into the same panel
			JPanel combine_panel = new JPanel(new GridBagLayout());
			c = new GridBagConstraints();
			// Add the cost_tables_ScrollPane to the main Grid	
			combine_panel.add(cost_tables_ScrollPane, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
					0, 0, 1, 1, 0.5, 1, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
					0, 0, 0, 0));		// insets top, left, bottom, right
			// Add scrollpane_QuickEdit	
			combine_panel.add(scrollpane_QuickEdit, PrismGridBagLayoutHandle.get_c(c, "BOTH", 
					0, 1, 1, 1, 0.5, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
					0, 0, 0, 0));		// insets top, left, bottom, right
			JSplitPane split_pane_lower = new JSplitPane();
			split_pane_lower.setBorder(null);
			split_pane_lower.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			split_pane_lower.setDividerSize(3);
			cost_condition_panel.setPreferredSize(new Dimension(535, 0));
			split_pane_lower.setLeftComponent(cost_condition_panel);	// Add the cost_condition_panel	
			split_pane_lower.setRightComponent(combine_panel);

						
			    			    
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			JSplitPane split_pane = new JSplitPane();
			split_pane.setBorder(null);
			split_pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			split_pane.setDividerSize(3);
						
			JPanel upper_panel = new JPanel();
			upper_panel.setBorder(null);
			upper_panel.setLayout(new GridBagLayout());			
			
			JPanel lower_panel = new JPanel();
			lower_panel.setBorder(null);
			lower_panel.setLayout(new GridBagLayout());
			
			
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;

			// Add helpToolBar to the main Grid
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 2;
			c.gridheight = 1;
			c.weightx = 0;
		    c.weighty = 0;
		    upper_panel.add(helpToolBar, c);				
			
			// Add static_identifiers_scrollpane to the main Grid
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 0.4;
		    c.weighty = 1;
		    upper_panel.add(static_identifiers_scrollpane, c);				
		    		
			// Add dynamic_identifiersPanel to the main Grid
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 0.5;
			c.weighty = 1;
			upper_panel.add(dynamic_identifiersScrollPanel, c);	
						    				
			// Add the combine_panel to the main Grid	
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 2;
			c.gridheight = 1;
			c.weightx = 1;
		    c.weighty = 1;
		    lower_panel.add(split_pane_lower, c);	
			
			
			split_pane.setLeftComponent(upper_panel);
			split_pane.setRightComponent(lower_panel);
			super.add(split_pane, BorderLayout.CENTER);
			
			
			// when radioButton_Right[6] is selected, time period GUI will be updated
			radio_button[6].addActionListener(this);			
		}
		
		// Listener for this class----------------------------------------------------------------------
		public void actionPerformed(ActionEvent e) {	    	
	    	//Update GUI for time period 
	    	for (int j = 0; j < checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).size(); j++) {			//The last element is Time period			
				if (j < total_period) {
					checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).get(j).setVisible(true);		//Periods to be visible 			
				} else {
					checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).get(j).setVisible(false);		//Periods to be invisible
					checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).get(j).setSelected(false);		//Periods to be unselected
				}
			}  	
	    	
	      	//Update Dynamic Identifier Panel
	    	if (read_database.get_yield_tables_column_names() != null && dynamic_identifiersScrollPanel.get_allDynamicIdentifiers() == null) {
	    		dynamic_identifiersScrollPanel = new ScrollPane_DynamicIdentifiers(read_database);	// "Get identifiers from yield table columns"
	    	}	    	
		}	
		
	    // Update set_id column. set_id needs to be unique-----------------
	    public void update_id() {  		
			List<Integer> id_list = new ArrayList<Integer>();			
		
			for (int row = 0; row < rowCount7; row++) {
				if (data7[row][0] != null) {
					id_list.add(Integer.valueOf((String) data7[row][0].toString()/*.replace("Set ", "")*/));
				}
			}			
			
			for (int row = 0; row < rowCount7; row++) {
				if (data7[row][0] == null) {
					int new_id = (id_list.size() > 0) ? Collections.max(id_list) + 1 : 1;	//new id = (max id + 1) or = 1 if no row
					data7[row][0] = /*"Set " + */new_id;
					id_list.add(new_id);
				}
			}			
		}
	}
	
	

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	class Basic_Constraints_GUI extends JLayeredPane implements ActionListener {
		List<List<JCheckBox>> checkboxStaticIdentifiers;
		List<List<JCheckBox>> checkboxDynamicIdentifiers;
		List<JCheckBox> allDynamicIdentifiers;
		List<JScrollPane> allDynamicIdentifiers_ScrollPane;
		List<JCheckBox> checkboxParameters;
		
		ScrollPane_Parameters parametersScrollPanel;
		ScrollPane_StaticIdentifiers static_identifiers_scrollpane;
		ScrollPane_DynamicIdentifiers dynamic_identifiersScrollPanel;
		JScrollPane table_scrollpane;	
		JPanel button_table_Panel;
		
		Panel_QuickEdit_BasicConstraints quick_edit;
		JScrollPane scrollpane_QuickEdit;
		
		public Basic_Constraints_GUI() {
			setLayout(new BorderLayout());
			
			
			// 1st grid ------------------------------------------------------------------------------		// Static identifiers	
			String panel_name = "Static Identifiers  -  use strata attributes to filter variables";
			static_identifiers_scrollpane = new ScrollPane_StaticIdentifiers(read_database, 2, panel_name);
			checkboxStaticIdentifiers = static_identifiers_scrollpane.get_CheckboxStaticIdentifiers();
			// End of 1st grid -----------------------------------------------------------------------

			
			// 2nd Grid ------------------------------------------------------------------------------		// Dynamic identifiers
			dynamic_identifiersScrollPanel = new ScrollPane_DynamicIdentifiers(read_database);
			checkboxDynamicIdentifiers = dynamic_identifiersScrollPanel.get_checkboxDynamicIdentifiers();
			allDynamicIdentifiers = dynamic_identifiersScrollPanel.get_allDynamicIdentifiers();
			allDynamicIdentifiers_ScrollPane = dynamic_identifiersScrollPanel.get_allDynamicIdentifiers_ScrollPane();
			// End of 2nd Grid -----------------------------------------------------------------------
				
					
			// 3rd grid ------------------------------------------------------------------------------		// Parameters
			parametersScrollPanel = new ScrollPane_Parameters(read_database);
			checkboxParameters = parametersScrollPanel.get_checkboxParameters();
			// End of 3rd grid -----------------------------------------------------------------------
			
	    	
	
			// 4th Grid ------------------------------------------------------------------------------		// Buttons	
			// 4th Grid -----------------------------------------------------------------------------
			// Add all buttons to a Panel----------------------------------
			button_table_Panel = new JPanel(new GridBagLayout());
			TitledBorder border3 = new TitledBorder("Basic Constraints");
			border3.setTitleJustification(TitledBorder.CENTER);
			button_table_Panel.setBorder(border3);
			GridBagConstraints c2 = new GridBagConstraints();
			c2.fill = GridBagConstraints.BOTH;
			c2.insets = new Insets(0, 5, 10, 10); // padding top 0, left 5, bottom 10, right 10
			
			
			JButton btn_NewSingle = new JButton();
			btn_NewSingle.setFont(new Font(null, Font.BOLD, 14));
//			btn_NewSingle.setText("NEW SINGLE");
			btn_NewSingle.setToolTipText("New");
			btn_NewSingle.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_add.png"));					
			c2.gridx = 0;
			c2.gridy = 0;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_NewSingle, c2);

			
			JButton btn_New_Multiple = new JButton();
			btn_New_Multiple.setFont(new Font(null, Font.BOLD, 14));
//			btn_New_Multiple.setText("NEW MULTIPLE");
			btn_New_Multiple.setToolTipText("New multiple");
			btn_New_Multiple.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_add3.png"));					
			c2.gridx = 0;
			c2.gridy = 1;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_New_Multiple, c2);
			
			
			JSpinner spin_move_rows = new JSpinner (new SpinnerNumberModel(1, 0, 2, 1));
			spin_move_rows.setToolTipText("Move");
			JFormattedTextField SpinnerText = ((DefaultEditor) spin_move_rows.getEditor()).getTextField();
			SpinnerText.setHorizontalAlignment(JTextField.LEFT);
			SpinnerText.setEditable(false);
			SpinnerText.setFocusable(false);
//			DefaultFormatter formatter = (DefaultFormatter) SpinnerText.getFormatter();
//		    formatter.setCommitsOnValidEdit(true);
		    spin_move_rows.setEnabled(false);
		    c2.gridx = 0;
			c2.gridy = 2;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(spin_move_rows, c2);
			
			
			JButton btn_Edit = new JButton();
//			btn_Edit.setText("EDIT");
			btn_Edit.setToolTipText("Modify");
			btn_Edit.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_swap.png"));
			btn_Edit.setEnabled(false);					
			c2.gridx = 0;
			c2.gridy = 3;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Edit, c2);
			
			
			JButton btn_Delete = new JButton();
			btn_Delete.setFont(new Font(null, Font.BOLD, 14));
//			btn_Delete.setText("DELETE");
			btn_Delete.setToolTipText("Delete");
			btn_Delete.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_erase.png"));
			btn_Delete.setEnabled(false);					
			c2.gridx = 0;
			c2.gridy = 4;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Delete, c2);


			JToggleButton btn_Sort = create_sort_button(button_table_Panel, c2, 0, 5, 0, 0);
			btn_Sort.addActionListener(e -> apply_sort_or_nosort(btn_Sort, model8, table8));

			
			JButton btn_Examine = new JButton();
			btn_Examine.setFont(new Font(null, Font.BOLD, 14));
			btn_Examine.setEnabled(false);
			btn_Examine.setToolTipText("Examine");
			btn_Examine.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_zoom.png"));
			btn_Examine.setVisible(false);
			c2.gridx = 0;
			c2.gridy = 6;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Examine, c2);
			
			
			c2.insets = new Insets(0, 0, 0, 0); // No padding
			// Add Empty Label to make all buttons on top not middle
			c2.gridx = 0;
			c2.gridy = 7;
			c2.weightx = 0;
			c2.weighty = 1;
			button_table_Panel.add(new JLabel(), c2);
			
			
			// Add table8				
			create_table8();
			table_scrollpane = new JScrollPane(table8);	
			c2.gridx = 1;
			c2.gridy = 0;
			c2.weightx = 1;
			c2.weighty = 1;
			c2.gridheight = 8;
			button_table_Panel.add(table_scrollpane, c2);
			// End of 4th Grid -----------------------------------------------------------------------
			// End of 4th Grid -----------------------------------------------------------------------	
			
			
			
			// Add Listeners for table8 & buttons----------------------------------------------------------
			// Add Listeners for table8 & buttons----------------------------------------------------------
			// table8
			class Table_Interaction {
				void refresh() {
					int[] selectedRow = table8.getSelectedRows();
					if (selectedRow.length == 1) {		// Reload Constraint & Enable Edit	when: 1 row is selected and no cell is editing
						int currentRow = selectedRow[0];
						currentRow = table8.convertRowIndexToModel(currentRow);		// Convert row index because "Sort" causes problems	
						static_identifiers_scrollpane.reload_this_constraint_static_identifiers((String) data8[currentRow][9]);	// 9 is the static_identifiers which have some attributes selected				
						dynamic_identifiersScrollPanel.reload_this_constraint_dynamic_identifiers((String) data8[currentRow][10], (String) data8[currentRow][11]);	// 11 is the original_dynamic_identifiers column
						parametersScrollPanel.reload_this_constraint_parameters((String) data8[currentRow][8]);	// 8 is the selected parameters of this constraint
						btn_Edit.setEnabled(true);
					} else {		// Disable Edit
						btn_Edit.setEnabled(false);
					}
					
					if (selectedRow.length >= 1 && table8.isEnabled()) {		// Enable Delete  when: >=1 row is selected, table is enable (often after Edit button finished its task)
						btn_Delete.setEnabled(true);
					} else {		// Disable Delete
						btn_Delete.setEnabled(false);
					}		
					
					if (selectedRow.length >= 1 && btn_Sort.getText().equals("OFF")) {	// Enable Spinner when: >=1 row is selected and Sorter is off
						spin_move_rows.setEnabled(true);
					} else {		// Disable Spinner
						spin_move_rows.setEnabled(false);
					}
				}
			}
			Table_Interaction table_interaction = new Table_Interaction();
			table8.addMouseListener(new MouseAdapter() { // Add listener
				@Override
				public void mouseReleased(MouseEvent e) {
					table_interaction.refresh();
				}
			});
			
			table8.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	table_interaction.refresh();
		        }
		    });
			

			// New single
			btn_NewSingle.addActionListener(e -> {	
				if (table8.isEditing()) {
					table8.getCellEditor().stopCellEditing();
				}
				
				// Add 1 row
				rowCount8++;
				data8 = new Object[rowCount8][colCount8];
				for (int i = 0; i < rowCount8 - 1; i++) {
					for (int j = 0; j < colCount8; j++) {
						data8[i][j] = model8.getValueAt(i, j);
					}	
				}
				
				
				data8[rowCount8 - 1][1] = String.join(" ..... ",
						parametersScrollPanel.get_parameters_description_from_GUI(),
						dynamic_identifiersScrollPanel.get_dynamic_description_from_GUI(),
						static_identifiers_scrollpane.get_static_description_from_GUI());
				data8[rowCount8 - 1][2] = "FREE";
				data8[rowCount8 - 1][3] = (double) 1;
				data8[rowCount8 - 1][8] = parametersScrollPanel.get_parameters_info_from_GUI();
				data8[rowCount8 - 1][9] = static_identifiers_scrollpane.get_static_info_from_GUI();
				data8[rowCount8 - 1][10] = dynamic_identifiersScrollPanel.get_dynamic_info_from_GUI();
				data8[rowCount8 - 1][11] = dynamic_identifiersScrollPanel.get_original_dynamic_info_from_GUI();
				
				model8.updateTableModelPrism(rowCount8, colCount8, data8, columnNames8);
				update_id();
				model8.fireTableDataChanged();
				quick_edit = new Panel_QuickEdit_BasicConstraints(table8, data8) {		// 2 lines to update data for Quick Edit Panel
					@Override
					public void check_IDLE_constraints_vs_flows() {
						is_IDLE_basic_constraints_used_in_flow_constraints();
					}
				};
	 			scrollpane_QuickEdit.setViewportView(quick_edit);
				
				// Convert the new Row to model view and then select it 
				int newRow = table8.convertRowIndexToView(rowCount8 - 1);
				table8.setRowSelectionInterval(newRow, newRow);
				table8.scrollRectToVisible(new Rectangle(table8.getCellRect(newRow, 0, true)));
			});
			
			
			// New Multiple
			btn_New_Multiple.addActionListener(e -> {
				if (table8.isEditing()) {
					table8.getCellEditor().stopCellEditing();
				}
				
				ScrollPane_ConstraintsSplitBasic constraint_split_ScrollPanel = new ScrollPane_ConstraintsSplitBasic(
						static_identifiers_scrollpane.get_static_layer_title_as_checkboxes(),
						parametersScrollPanel.get_checkboxParameters(),
						dynamic_identifiersScrollPanel.get_allDynamicIdentifiers());

				
				
				String ExitOption[] = {"Add Constraints","Cancel"};
				int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), constraint_split_ScrollPanel, "Create multiple constraints - checked items will be split",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, ExitOption, ExitOption[0]);
				if (response == 0) {	// Add Constraints
					
					
					
					// calculate parameter_permutation
					int total_parameter_permutation = 1;
					if (constraint_split_ScrollPanel.is_parameters_split()) { 	//if parameters would be split
						int total_checked_elements = 0;
						for (int i = 0; i < checkboxParameters.size(); i++) {
							if (checkboxParameters.get(i).isSelected() && checkboxParameters.get(i).isVisible()) {	// If this parameter is checked		
								total_checked_elements ++;	// Increase number of constraints
							}
						}
						if (total_checked_elements == 0) {	// This is the case when "NoParameter" or "CostParameter" or nothing is checked
							total_parameter_permutation = 1;
						} else {
							total_parameter_permutation = total_parameter_permutation * total_checked_elements;
						}
					}
					
					
					
					// calculate dynamic_permutation
					int total_dynamic_permutation = 1;
					List<Integer> dynamic_split_id = new ArrayList<Integer>();	// id of dynamic attribute to be split
					List<String> dynamic_split_name = constraint_split_ScrollPanel.get_dynamic_split_name();	// name of dynamic attribute to be split
					for (String name : dynamic_split_name) {
						// Find the index
						int i = 0;
						for (int cb_id = 0; cb_id < allDynamicIdentifiers.size(); cb_id++) {
							if (allDynamicIdentifiers.get(cb_id).getText().equals(name)) {
								i = cb_id;
								dynamic_split_id.add(i);
								break;
							}
						}
						int total_checked_elements = 0;
						for (int j = 0; j < checkboxDynamicIdentifiers.get(i).size(); j++) {
							if (checkboxDynamicIdentifiers.get(i).get(j).isSelected() && checkboxDynamicIdentifiers.get(i).get(j).isVisible()) {	// If element of this dynamic is checked		
								total_checked_elements ++;	// Increase number of constraints
							}
						}
						total_dynamic_permutation = total_dynamic_permutation * total_checked_elements;
					}
					
					// calculate static_permutation
					int total_static_permutation = 1;
					List<Integer> static_split_id = constraint_split_ScrollPanel.get_static_split_id();	// id of static layers to be split
					for (int i : static_split_id) {
						int total_checked_elements = 0;
						for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
							if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {	// If element of this static is checked		
								total_checked_elements ++;	// Increase number of constraints
							}
						}
						total_static_permutation = total_static_permutation * total_checked_elements;
					}										
					
					// calculate total number of constraints
					int total_constraints = total_parameter_permutation * total_dynamic_permutation * total_static_permutation;
					System.out.println(total_constraints);
					
					
					// Ask to confirm adding if there are more than 1000 constraints
					int response2 = 0;	
					if (total_constraints > 10000) {
						String ExitOption2[] = {"Add","Cancel"};
						String warningText = "Prism is going to add " + total_constraints + " constraints. It might take time. Continue to add?";
						response2 = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), warningText, "Confirm adding constraints",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_warning.png"), ExitOption2, ExitOption2[1]);
						
					}
						
					if (response2 == 0) {	
						constraint_split_ScrollPanel.stop_editing();
						
						
//						// Example: 
//						List<List<String>> lists = new ArrayList<List<String>>();
//						lists.add(Arrays.asList(new String[] { "lay1 a", "lay1 b", "lay1 c" }));
//						lists.add(Arrays.asList(new String[] { "lay2 c d e" }));
//						lists.add(Arrays.asList(new String[] { "lay3 f", "lay3 g" }));
						
						
						// for parameters------------------------------------------------------------------------------------------------------
						List<String> parameter_info_list = new ArrayList<String>();
						if (constraint_split_ScrollPanel.is_parameters_split()) { 	//if parameters would be split
							for (int i = 0; i < checkboxParameters.size(); i++) {
								if (checkboxParameters.get(i).isSelected() && checkboxParameters.get(i).isVisible()) {	// If this parameter is checked		
									parameter_info_list.add(String.valueOf(i));
								}
							}
						}
						
						
						// for dynamic identifiers------------------------------------------------------------------------------------------------------
						List<List<String>> dynamic_iterable_lists = new ArrayList<List<String>>();
						for (int i = 0; i < checkboxDynamicIdentifiers.size(); i++) {
							if (allDynamicIdentifiers_ScrollPane.get(i).isVisible()) {
								List<String> joined_string_list = new ArrayList<String>();
								
								if (dynamic_split_id.contains(i)) {	// if this dynamic layer must be split
									for (int j = 0; j < checkboxDynamicIdentifiers.get(i).size(); j++) {
										if (checkboxDynamicIdentifiers.get(i).get(j).isSelected() && checkboxDynamicIdentifiers.get(i).get(j).isVisible()) {	// If element of this static is checked		
											List<String> temp = new ArrayList<String>(); 
											temp.add(String.valueOf(i));
											temp.add(checkboxDynamicIdentifiers.get(i).get(j).getText());	
											joined_string_list.add(String.join(" ", temp));
										}	
									}
									
									dynamic_iterable_lists.add(joined_string_list);
								} else {		// if this dynamic layer would not be split
									List<String> temp = new ArrayList<String>(); 
									temp.add(String.valueOf(i));
									for (int j = 0; j < checkboxDynamicIdentifiers.get(i).size(); j++) {
										if (checkboxDynamicIdentifiers.get(i).get(j).isSelected() && checkboxDynamicIdentifiers.get(i).get(j).isVisible()) {	// If element of this static is checked		
											temp.add(checkboxDynamicIdentifiers.get(i).get(j).getText());	
										}	
									}
									joined_string_list.add(String.join(" ", temp));
									
									dynamic_iterable_lists.add(joined_string_list);
								}
							}
						}
						// Create lists
						List<String> dynamic_info_list = new ArrayList<String>();
						// Get all permutation
						MixedRangeCombinationIterable<String> dynamic_iterable = new MixedRangeCombinationIterable<String>(dynamic_iterable_lists);
						for (List<String> element : dynamic_iterable) {
							String joined_string = String.join(";", element);
							dynamic_info_list.add(joined_string);
						}
						
						
						// for static identifiers------------------------------------------------------------------------------------------------------
						List<List<String>> static_iterable_lists = new ArrayList<List<String>>();
						for (int i = 0; i < checkboxStaticIdentifiers.size(); i++) {
							List<String> joined_string_list = new ArrayList<String>();
							
							if (static_split_id.contains(i)) {	// if this static layer must be split
								for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
									if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {	// If element of this static is checked		
										List<String> temp = new ArrayList<String>(); 
										temp.add(String.valueOf(i));
										temp.add(checkboxStaticIdentifiers.get(i).get(j).getText());	
										joined_string_list.add(String.join(" ", temp));
									}	
								}
								
								static_iterable_lists.add(joined_string_list);
							} else {		// if this static layer would not be split
								List<String> temp = new ArrayList<String>(); 
								temp.add(String.valueOf(i));
								for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
									if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {	// If element of this static is checked		
										temp.add(checkboxStaticIdentifiers.get(i).get(j).getText());	
									}	
								}
								joined_string_list.add(String.join(" ", temp));
								
								static_iterable_lists.add(joined_string_list);
							}	
						}		
						// Create lists
						List<String> static_info_list = new ArrayList<String>();
						// Get all permutation
						MixedRangeCombinationIterable<String> static_iterable = new MixedRangeCombinationIterable<String>(static_iterable_lists);
						for (List<String> element : static_iterable) {
							String joined_string = String.join(";", element);
							static_info_list.add(joined_string);
						}
						
						
						
						
						// for parameter description------------------------------------------------------------------------------------------------------
						List<String> parameter_description_info_list = new ArrayList<String>();
						if (constraint_split_ScrollPanel.is_parameters_split()) { 	//if parameters would be split
							for (int i = 0; i < checkboxParameters.size(); i++) {
								if (checkboxParameters.get(i).isSelected() && checkboxParameters.get(i).isVisible()) {	// If this parameter is checked		
									parameter_description_info_list.add(checkboxParameters.get(i).getText());
								}
							}
							if (parameter_description_info_list.isEmpty()) parameter_description_info_list.add(parametersScrollPanel.get_parameters_description_from_GUI());
						} else { // --> disable this "else" if do not want the full description
							parameter_description_info_list.add(parametersScrollPanel.get_parameters_description_from_GUI());
						}
						
						
						// for dynamic description------------------------------------------------------------------------------------------------------
						List<List<String>> dynamic_description_iterable_lists = new ArrayList<List<String>>();
						for (int i = 0; i < checkboxDynamicIdentifiers.size(); i++) {
							List<String> joined_string_list = new ArrayList<String>();

							if (dynamic_split_id.contains(i)) {	// if this static layer must be split
								for (int j = 0; j < checkboxDynamicIdentifiers.get(i).size(); j++) {
									if (checkboxDynamicIdentifiers.get(i).get(j).isSelected() && checkboxDynamicIdentifiers.get(i).get(j).isVisible()) {	// If element of this dynamic is checked		
										List<String> temp = new ArrayList<String>(); 
										temp.add(allDynamicIdentifiers.get(i).getText());
										temp.add(checkboxDynamicIdentifiers.get(i).get(j).getText());	
										joined_string_list.add(String.join(" ", temp));
									}	
								}
								
								dynamic_description_iterable_lists.add(joined_string_list);
							} else {	// this else is almost the same as "get_dynamic_description_from_GUI" --> disable this "else" if do not want the full description
								// Count the total of checked items in each identifier 
								int total_check_items = 0;
								int total_items = 0;
								if (allDynamicIdentifiers_ScrollPane.get(i).isVisible() && checkboxDynamicIdentifiers.get(i).size() > 0) {	// get the active identifiers (when identifier ScrollPane is visible and List size >0)
									for (int j = 0; j < checkboxDynamicIdentifiers.get(i).size(); j++) { //Loop all checkBoxes in this active identifier
										if (checkboxDynamicIdentifiers.get(i).get(j).isSelected() && checkboxDynamicIdentifiers.get(i).get(j).isVisible()) {
											total_check_items++;
										}
										if (checkboxDynamicIdentifiers.get(i).get(j).isVisible()) {
											total_items++;
										}
									}
								}
								// Add to description only when the total of checked items < the total items
								if (total_check_items < total_items) {
									String dynamic_info = allDynamicIdentifiers.get(i).getText();
									
									for (int j = 0; j < checkboxDynamicIdentifiers.get(i).size(); j++) { 	// Loop all checkBoxes in this active identifier
										String checkboxName = checkboxDynamicIdentifiers.get(i).get(j).getText();									
										// Add if it is (selected & visible)
										if (checkboxDynamicIdentifiers.get(i).get(j).isSelected() && checkboxDynamicIdentifiers.get(i).get(j).isVisible()) {
											dynamic_info = String.join(" ", dynamic_info, checkboxName);
										}
									}
									joined_string_list.add(dynamic_info);
									dynamic_description_iterable_lists.add(joined_string_list);
								}
							}
						}	
						// Create lists
						List<String> dynamic_description_info_list = new ArrayList<String>();
						// Get all permutation
						MixedRangeCombinationIterable<String> dynamic_description_iterable = new MixedRangeCombinationIterable<String>(dynamic_description_iterable_lists);
						for (List<String> element : dynamic_description_iterable) {
							String joined_string = String.join(" | ", element);
							dynamic_description_info_list.add(joined_string);
						}
						
						
						// for static description------------------------------------------------------------------------------------------------------
						List<List<String>> static_description_iterable_lists = new ArrayList<List<String>>();
						for (int i = 0; i < checkboxStaticIdentifiers.size(); i++) {
							List<String> joined_string_list = new ArrayList<String>();
							
							if (static_split_id.contains(i)) {	// if this static layer must be split
								for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {
									if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {	// If element of this static is checked		
										List<String> temp = new ArrayList<String>(); 
										temp.add(static_identifiers_scrollpane.get_static_layer_title_as_checkboxes().get(i).getText());
										temp.add(checkboxStaticIdentifiers.get(i).get(j).getText());	
										joined_string_list.add(String.join(" ", temp));
									}	
								}
								
								static_description_iterable_lists.add(joined_string_list);
							} else {	// this else is almost the same as "get_static_description_from_GUI" --> disable this "else" if do not want the full description
								// Count the total of checked items in each layer 
								int total_check_items = 0;
								int total_items = 0;
								for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {		//Loop all elements in each layer
									if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {
										total_check_items++;
									}
									if (checkboxStaticIdentifiers.get(i).get(j).isVisible()) {
										total_items++;
									}	
								}
								// Add to description only when the total of checked items < the total items
								if (total_check_items < total_items) {
									String static_info = static_identifiers_scrollpane.get_static_layer_title_as_checkboxes().get(i).getText();
									
									for (int j = 0; j < checkboxStaticIdentifiers.get(i).size(); j++) {		//Loop all elements in each layer
										String checkboxName = checkboxStaticIdentifiers.get(i).get(j).getText();				
										// Add if it is (selected & visible)
										if (checkboxStaticIdentifiers.get(i).get(j).isSelected() && checkboxStaticIdentifiers.get(i).get(j).isVisible()) {
											static_info = String.join(" ", static_info, checkboxName);
										}
									}
									joined_string_list.add(static_info);
									static_description_iterable_lists.add(joined_string_list);
								}
							}
						}	
						// Create lists
						List<String> static_description_info_list = new ArrayList<String>();
						// Get all permutation
						MixedRangeCombinationIterable<String> description_iterable = new MixedRangeCombinationIterable<String>(static_description_iterable_lists);
						for (List<String> element : description_iterable) {
							String joined_string = String.join(" | ", element);
							static_description_info_list.add(joined_string);
						}
						
						
						
						
						// Combine everything together to create the final lists
						if (parameter_info_list.isEmpty()) parameter_info_list.add(parametersScrollPanel.get_parameters_info_from_GUI());
						if (dynamic_info_list.isEmpty()) dynamic_info_list.add(dynamic_identifiersScrollPanel.get_dynamic_info_from_GUI());
						if (static_info_list.isEmpty()) static_info_list.add(static_identifiers_scrollpane.get_static_info_from_GUI());
						if (parameter_description_info_list.isEmpty()) parameter_description_info_list.add(parametersScrollPanel.get_parameters_description_from_GUI());
						if (dynamic_description_info_list.isEmpty()) dynamic_description_info_list.add(dynamic_identifiersScrollPanel.get_dynamic_description_from_GUI());
						if (static_description_info_list.isEmpty()) static_description_info_list.add(static_identifiers_scrollpane.get_static_description_from_GUI());
						
						List<String> final_parameter_info_list = new ArrayList<String>();
						List<String> final_dynamic_info_list = new ArrayList<String>();
						List<String> final_static_info_list = new ArrayList<String>();
						List<String> final_description_info_list = new ArrayList<String>();
						for (int p = 0; p < parameter_info_list.size(); p++) {
							for (int i = 0; i < dynamic_info_list.size(); i++) {
								for (int j = 0; j < static_info_list.size(); j++) {
									final_parameter_info_list.add(parameter_info_list.get(p));
									final_dynamic_info_list.add(dynamic_info_list.get(i));
									final_static_info_list.add(static_info_list.get(j));
									
									List<String> temp_list = new ArrayList<String>();
									temp_list.add(parameter_description_info_list.get(p));
									temp_list.add(dynamic_description_info_list.get(i));
									temp_list.add(static_description_info_list.get(j));
									String joined_string = String.join(" ..... ", temp_list);
									final_description_info_list.add(joined_string);
								}
							}
						}
						
						
						
						
						// Test printing ------------------------------------------------------------------------------------------------------
						System.out.println("--------------------------------------------");
						for (String i: parameter_info_list) { 
							System.out.println(i);
						}
						System.out.println("--------------------------------------------");
						for (String i: dynamic_info_list) { 
							System.out.println(i);
						}
						System.out.println("--------------------------------------------");
						for (String i: static_info_list) { 
							System.out.println(i);
						}
						System.out.println("--------------------------------------------");
						
						
						
						
						// Add all constraints------------------------------------------------------------------------------------------------------
						if (total_constraints > 0) {
							rowCount8 = rowCount8 + total_constraints;
							data8 = new Object[rowCount8][colCount8];
							for (int i = 0; i < rowCount8 - total_constraints; i++) {
								for (int j = 0; j < colCount8; j++) {
									data8[i][j] = model8.getValueAt(i, j);
								}	
							}
							
							Object[][] temp_data = constraint_split_ScrollPanel.get_multiple_constraints_data();
							JCheckBox autoDescription = constraint_split_ScrollPanel.get_autoDescription();
							
							for (int i = rowCount8 - total_constraints; i < rowCount8; i++) {
								for (int j = 0; j < colCount8; j++) {
									if (autoDescription.isSelected()) {
										if (temp_data[0][1] == null) {
											data8[i][1] = /*"set constraint" + " " + (i - rowCount8 + total_constraints + 1) + " ..... " +*/ final_description_info_list.get(i - rowCount8 + total_constraints);
										} else {
											data8[i][1] = temp_data[0][1] /*+ " " + (i - rowCount8 + total_constraints + 1)*/ + " ..... " + final_description_info_list.get(i - rowCount8 + total_constraints);
										}
									} else {
										data8[i][1] = temp_data[0][1];
									}
									data8[i][0] = temp_data[0][0];
									data8[i][2] = (temp_data[0][2] == null) ? "FREE" : temp_data[0][2];
									data8[i][3] = temp_data[0][3];
									data8[i][4] = temp_data[0][4];
									data8[i][5] = temp_data[0][5];
									data8[i][6] = temp_data[0][6];
									data8[i][7] = temp_data[0][7];
									data8[i][8] = final_parameter_info_list.get(i - rowCount8 + total_constraints);		// parameter splitter is active
									data8[i][9] = final_static_info_list.get(i - rowCount8 + total_constraints);		// static splitter is active
									data8[i][10] = final_dynamic_info_list.get(i - rowCount8 + total_constraints);		// dynamic splitter is active
									data8[i][11] = dynamic_identifiersScrollPanel.get_original_dynamic_info_from_GUI();
								}	
							}	
	
							model8.updateTableModelPrism(rowCount8, colCount8, data8, columnNames8);
							update_id();
							model8.fireTableDataChanged();
							quick_edit = new Panel_QuickEdit_BasicConstraints(table8, data8) {		// 2 lines to update data for Quick Edit Panel
								@Override
								public void check_IDLE_constraints_vs_flows() {
									is_IDLE_basic_constraints_used_in_flow_constraints();
								}
							};
				 			scrollpane_QuickEdit.setViewportView(quick_edit);
							
							// Convert the new Row to model view and then select it 
							for (int i = rowCount8 - total_constraints; i < rowCount8; i++) {
								int newRow = table8.convertRowIndexToView(i);
								table8.addRowSelectionInterval(newRow, newRow);
							}	
							table8.scrollRectToVisible(new Rectangle(table8.getCellRect(table8.convertRowIndexToView(rowCount8 - total_constraints), 0, true)));
						}
					}
										
				}
			});			
			
			
			// Edit
			btn_Edit.addActionListener(e -> {
				if (table8.isEditing()) {
					table8.getCellEditor().stopCellEditing();
				}
				
				if (table8.isEnabled()) {				
					int selectedRow = table8.getSelectedRow();
					selectedRow = table8.convertRowIndexToModel(selectedRow);		// Convert row index because "Sort" causes problems		
					
					// Apply change
					data8[selectedRow][8] = parametersScrollPanel.get_parameters_info_from_GUI();
					data8[selectedRow][9] = static_identifiers_scrollpane.get_static_info_from_GUI();
					data8[selectedRow][10] = dynamic_identifiersScrollPanel.get_dynamic_info_from_GUI();	
					data8[selectedRow][11] = dynamic_identifiersScrollPanel.get_original_dynamic_info_from_GUI();
					model8.fireTableDataChanged();	
					
					// Convert the edited Row to model view and then select it 
					int editRow = table8.convertRowIndexToView(selectedRow);
					table8.setRowSelectionInterval(editRow, editRow);
					
					static_identifiers_scrollpane.highlight();
					dynamic_identifiersScrollPanel.highlight();
					parametersScrollPanel.highlight();
				}
			});
			
			
			btn_Edit.addMouseListener(new MouseAdapter() { // Add listener
				public void mouseEntered(java.awt.event.MouseEvent e) {
					if (table8.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.highlight();
						dynamic_identifiersScrollPanel.highlight();
						parametersScrollPanel.highlight();
					}
				}

				public void mouseExited(java.awt.event.MouseEvent e) {
					if (table8.getSelectedRows().length == 1) {
						static_identifiers_scrollpane.unhighlight();
						dynamic_identifiersScrollPanel.unhighlight();
						parametersScrollPanel.unhighlight();
					}
				}
			});
			
			
			// Spinner
		    spin_move_rows.addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
					int up_or_down = (int) spin_move_rows.getValue() - 1;										
					spin_move_rows.setValue((int) 1);	// Reset spinner value to 1
										
					if (up_or_down == 1) {	// move up
						// Cancel editing before moving conditions up or down
						if (table8.isEditing()) {
							table8.getCellEditor().cancelCellEditing();
						}	
						
						// Get selected rows
						int[] selectedRow = table8.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selected_ids = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selected_ids.size() >=1 && selected_ids.get(0) > 0) {	// If there is at least 1 row selected & the first row is not selected
							for (int i = 0; i < rowCount8; i++) {
								if (selected_ids.contains(i)) {		
									for (int j = 0; j < colCount8; j++) {
										Object temp = data8[i - 1][j];
										data8[i - 1][j] = data8[i][j];
										data8[i][j] = temp;
									}
								}
							}							
							model8.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table8.addRowSelectionInterval(i - 1, i - 1);
							}
						}
						
						// Scroll to the first row of the current selected rows (- 3 to see the 3 unselected rows above when moving up)
						table8.scrollRectToVisible(new Rectangle(table8.getCellRect(table8.convertRowIndexToView(table8.getSelectedRow()) - 3, 0, true)));	
					}
										
					if (up_or_down == -1) {	// move down						
						if (table8.isEditing()) {
							table8.getCellEditor().cancelCellEditing();	// cancel editing before moving conditions up or down
						}	
						
						// Get selected rows
						int[] selectedRow = table8.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(selectedRowList.size() - 1) < rowCount8 - 1) {	// If ...
							for (int i = rowCount8 - 1; i >= 0; i--) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount8; j++) {
										Object temp = data8[i + 1][j];
										data8[i + 1][j] = data8[i][j];
										data8[i][j] = temp;
									}
								}
							}						
							model8.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table8.addRowSelectionInterval(i + 1, i + 1);
							}	
						}
						
						// Scroll to the last row of the current selected rows (+ 3 to see the next 3 unselected rows below when moving down)
						table8.scrollRectToVisible(new Rectangle(table8.getCellRect(table8.convertRowIndexToView(table8.getSelectedRows()[table8.getSelectedRows().length - 1]) + 3, 0, true)));	
					}
		        }
		    });
		    
				
			// Delete
			btn_Delete.addActionListener(e -> {
				//Cancel editing before delete
				if (table8.isEditing()) {
					table8.getCellEditor().cancelCellEditing();
				}				
				
				// Get selected rows
				int[] selectedRow = table8.getSelectedRows();	
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = table8.convertRowIndexToModel(selectedRow[i]);	///Convert row index because "Sort" causes problems
				}
				
				// Create a list of selected row indexes
				List<Integer> selected_ids = new ArrayList<Integer>();	
				List<Integer> selected_basic_ids = new ArrayList<Integer>();	
				for (int i: selectedRow) {
					selected_ids.add(i);
					selected_basic_ids.add((Integer) data8[i][0]);
				}	
				
				Collections.sort(selected_basic_ids);	//sort to search binary

				// create a list for each flow constraint, each contains the ids used in that flow
				List<Integer>[] flow_list = new ArrayList[data9.length];
				String warning_message = "";
				for (int i = 0; i < data9.length; i++) {	// loop each flow
					flow_list[i] = new ArrayList<Integer>();
					String[] flow_arrangement = data9[i][2].toString().split(";");
					for (String each_sigma: flow_arrangement) {		// a sigma box might have several ids, separated by a space
						for (String id: each_sigma.split("\\s+")) {
							flow_list[i].add(Integer.valueOf(id));
						}
					}
					
			        // check if the flow contains IDLE basic constraints
					List<Integer> basic_ids_in_this_flow = new ArrayList<Integer>();
					for (int id: flow_list[i]) {
						int index = Collections.binarySearch(selected_basic_ids, id);
						if (index >= 0) basic_ids_in_this_flow.add(id);		// if id in this flow is not found as in active (hard/soft/free) basic constraints --> add
					}

					// test print
					if (!basic_ids_in_this_flow.isEmpty()) {
						warning_message = warning_message + (data9[i][0] + " " + data9[i][1] + " ----->");
						for (int id: basic_ids_in_this_flow) {
							warning_message = warning_message + (" " + id);
						}
						warning_message = warning_message + "\n";
					}
				}	
				
				// Popup for Delete
				if (warning_message.equals("")) {  // the case when basic ids are NOT in the flows
					String ExitOption[] = {"Delete", "Cancel"};
					int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), "Delete now?", "Confirm Delete",
							JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[0]);
					if (response == 0) {
						// Get values to the new data8
						data8 = new Object[rowCount8 - selectedRow.length][colCount8];
						int newRow =0;
						for (int ii = 0; ii < rowCount8; ii++) {
							if (!selected_ids.contains(ii)) {			//If row not in the list then add to data8 row
								for (int jj = 0; jj < colCount8; jj++) {
									data8[newRow][jj] = model8.getValueAt(ii, jj);
								}
								newRow++;
							}
						}
						// Pass back the info to table model
						rowCount8 = rowCount8 - selectedRow.length;
						model8.updateTableModelPrism(rowCount8, colCount8, data8, columnNames8);
						model8.fireTableDataChanged();	
						quick_edit = new Panel_QuickEdit_BasicConstraints(table8, data8) {		// 2 lines to update data for Quick Edit Panel
							@Override
							public void check_IDLE_constraints_vs_flows() {
								is_IDLE_basic_constraints_used_in_flow_constraints();
							}
						};
			 			scrollpane_QuickEdit.setViewportView(quick_edit);
					}
				} else {  // the case when some basic ids are in the flows
					warning_message = "Basic constraints can be deleted only when they are not used in any flow constraint.\n"
							+ "The below list shows: flow constraint -----> basic constraints in the flow which you want to delete.\n\n"
							+ warning_message;
					PrismTextAreaReadMe warning_textarea = new PrismTextAreaReadMe("icon_tree.png", 70, 70);
					warning_textarea.append(warning_message);
					warning_textarea.setSelectionStart(0);	// scroll to top
					warning_textarea.setSelectionEnd(0);
					warning_textarea.setEditable(false);
					PrismTitleScrollPane warning_scrollpane = new PrismTitleScrollPane("", "LEFT", warning_textarea);
					warning_scrollpane.get_nested_scrollpane().setPreferredSize(new Dimension(550, 300));
					
					String ExitOption[] = {"OK"};
					int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), warning_scrollpane, "Delete is denied",
							JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_warning.png"), ExitOption, ExitOption[0]);
					if (response == 0) {
						
					}
				}
			});

			// Examine
			btn_Examine.addActionListener(e -> {						
//					// Apply change			
//					int[] selectedRow = table8.getSelectedRows();	
//					///Convert row index because "Sort" causes problems
//					for (int i = 0; i < selectedRow.length; i++) {
//						selectedRow[i] = table8.convertRowIndexToModel(selectedRow[i]);
//					}
//					table8.clearSelection();	//To help trigger the row refresh: clear then add back the rows
//					for (int i: selectedRow) {
//						if (String.valueOf(data8[i][2]).equalsIgnoreCase("SOFT")) {
//							data8[i][2] = "FREE";
//						} else if (String.valueOf(data8[i][2]).equalsIgnoreCase("HARD")) {
//							data8[i][2] = "FREE";
//						}
//						table8.addRowSelectionInterval(table8.convertRowIndexToView(i),table8.convertRowIndexToView(i));
//					}
			});			

			// End of Listeners for table8 & buttons -----------------------------------------------------------------------
			// End of Listeners for table8 & buttons -----------------------------------------------------------------------		    
		    
	
			
			
			
	        // scrollPane Quick Edit ----------------------------------------------------------------------	
			// scrollPane Quick Edit ----------------------------------------------------------------------	
			quick_edit = new Panel_QuickEdit_BasicConstraints(table8, data8) {		// 2 lines to update data for Quick Edit Panel
				@Override
				public void check_IDLE_constraints_vs_flows() {
					is_IDLE_basic_constraints_used_in_flow_constraints();
				}
			};
 			scrollpane_QuickEdit = new JScrollPane(quick_edit);
 			TitledBorder border = new TitledBorder("Quick Edit ");
 			border.setTitleJustification(TitledBorder.CENTER);
 			scrollpane_QuickEdit.setBorder(border);
 			scrollpane_QuickEdit.setVisible(false);		
 			
		

			// ToolBar Panel ----------------------------------------------------------------------------
			// ToolBar Panel ----------------------------------------------------------------------------
			ToolBarWithBgImage helpToolBar = new ToolBarWithBgImage("Project Tools", JToolBar.HORIZONTAL, null);
			helpToolBar.setFloatable(false);	//to make a tool bar immovable
			helpToolBar.setRollover(true);	//to visually indicate tool bar buttons when the user passes over them with the cursor
			helpToolBar.setBorderPainted(false);
			
			// button Quick Edit
 			JToggleButton btnQuickEdit = new JToggleButton();
 			btnQuickEdit.setToolTipText("Show Quick Edit Tool");
 			btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
 			btnQuickEdit.addActionListener(e -> {		
 				if (btnQuickEdit.getToolTipText().equals("Show Quick Edit Tool")) {
					btnQuickEdit.setToolTipText("Hide Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_hide.png"));
					scrollpane_QuickEdit.setVisible(true);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_Basic_Constraints_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				} else {
					btnQuickEdit.setToolTipText("Show Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
					scrollpane_QuickEdit.setVisible(false);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_Basic_Constraints_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				}
 			});				
			
			// button Help
			JButton btnHelp = new JButton();
			btnHelp.setToolTipText("Help");
			btnHelp.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_help.png"));
			btnHelp.addActionListener(e -> {

			});
			
			// Add all buttons to flow_panel
			helpToolBar.add(Box.createGlue());	//Add glue for Right alignment
			helpToolBar.add(btnQuickEdit);
			helpToolBar.add(btnHelp);
			// End of ToolBar Panel -----------------------------------------------------------------------
			// End of ToolBar Panel ----------------------------------------------------------------------- 				
				
				
			
			    	
		    
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			JSplitPane split_pane = new JSplitPane();
			split_pane.setBorder(null);
			split_pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			split_pane.setDividerSize(3);
						
			JPanel upper_panel = new JPanel();
			upper_panel.setBorder(null);
			upper_panel.setLayout(new GridBagLayout());			
			
			JPanel lower_panel = new JPanel();
			lower_panel.setBorder(null);
			lower_panel.setLayout(new GridBagLayout());
			
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;

			// Add helpToolBar to the main Grid
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 3;
			c.gridheight = 1;
			c.weightx = 0;
		    c.weighty = 0;
		    upper_panel.add(helpToolBar, c);				
			
			// Add static_identifiers_scrollpane to the main Grid
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 2;
			c.gridheight = 1;
			c.weightx = 0.4;
		    c.weighty = 1;
		    upper_panel.add(static_identifiers_scrollpane, c);				
		    		
			// Add dynamic_identifiersPanel to the main Grid
			c.gridx = 2;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 0.5;
			c.weighty = 1;
			upper_panel.add(dynamic_identifiersScrollPanel, c);	
			    		
			// Add the parametersScrollPanel to the main Grid	
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 0;
		    c.weighty = 1;
		    lower_panel.add(parametersScrollPanel, c);						
		    	    		    
		    // Add the button_table_Panel & scrollpane_QuickEdit to a new Panel then add that panel to the main Grid
			JPanel button_table_qedit_panel = new JPanel();
			button_table_qedit_panel.setLayout(new BorderLayout());
			button_table_qedit_panel.add(button_table_Panel, BorderLayout.CENTER);
			button_table_qedit_panel.add(scrollpane_QuickEdit, BorderLayout.EAST);			
			c.gridx = 1;
			c.gridy = 2;
			c.gridwidth = 2; 
			c.gridheight = 1;
			c.weightx = 1;
		    c.weighty = 1;
		    lower_panel.add(button_table_qedit_panel, c);
			
			
			split_pane.setLeftComponent(upper_panel);
			split_pane.setRightComponent(lower_panel);
			super.add(split_pane, BorderLayout.CENTER);
			
			
			// when radioButton_Right[7] is selected, time period GUI will be updated
			radio_button[7].addActionListener(this);
		}
		
		
		// Listener for this class----------------------------------------------------------------------
	    public void actionPerformed(ActionEvent e) {
	    	// Update GUI for time period 
	    	for (int j = 0; j < checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).size(); j++) {			//The last element is Time period			
				if (j < total_period) {
					checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).get(j).setVisible(true);		//Periods to be visible 			
				} else {
					checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).get(j).setVisible(false);		//Periods to be invisible
					checkboxStaticIdentifiers.get(checkboxStaticIdentifiers.size() - 1).get(j).setSelected(false);		//Periods to be unselected
				}
			}  	
	    	
	       	// Update Parameter Panel
	    	if (read_database.get_yield_tables_column_names() != null && parametersScrollPanel.get_checkboxParameters() == null) {
	    		parametersScrollPanel = new ScrollPane_Parameters(read_database);	//"Get parameters from YT columns"
	    	}
	    	
	      	// Update Dynamic Identifier Panel
	    	if (read_database.get_yield_tables_column_names() != null && dynamic_identifiersScrollPanel.get_allDynamicIdentifiers() == null) {
	    		dynamic_identifiersScrollPanel = new ScrollPane_DynamicIdentifiers(read_database);	// "Get identifiers from yield table columns"
	    	}

	    	// Only set button_table_Panel visible when Parameter scroll Pane have checkboxes created
	    	if (parametersScrollPanel.get_checkboxParameters() == null) {
	    		button_table_Panel.setVisible(false);
	    	} else {
	    		button_table_Panel.setVisible(true);
	    	}
	    }
	    
	    // Update id column. id needs to be unique in order to use in flow constraints-----------------
	    public void update_id() {  		
			List<Integer> id_list = new ArrayList<Integer>();			
			
			for (int row = 0; row < rowCount8; row++) {
				if (data8[row][0] != null) {
					id_list.add((int) data8[row][0]);
				}
			}			
			
			for (int row = 0; row < rowCount8; row++) {
				if (data8[row][0] == null) {
					int new_id = (id_list.size() > 0) ? Collections.max(id_list) + 1 : 1;	//new id = (max id + 1) or = 1 if no row
					data8[row][0] = new_id;
					id_list.add(new_id);
				}
			}			
		}
	}

		

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	class Flow_Constraints_GUI extends JLayeredPane implements ActionListener {
		JTable basic_table;
		PrismTableModel model_basic;
		DefaultListModel id_list_model;
		JList id_list;
		JPanel button_table_Panel;
		JSpinner spin_sigma;
		
		Panel_QuickEdit_FlowConstraints quick_edit;
		JScrollPane scrollpane_QuickEdit;
		
		public Flow_Constraints_GUI() {
			setLayout(new BorderLayout());
			
			
			// 1st grid ------------------------------------------------------------------------------
			id_list_model= new DefaultListModel<>();
			id_list = new JList(id_list_model);		
			ScrollPane_ConstraintsFlow flow_scrollPane = new ScrollPane_ConstraintsFlow(id_list) {
				@Override
				public void update_spin_sigma() {
					if (spin_sigma != null && get_list_model() != null) spin_sigma.setValue(get_list_model().length);	// need the if here to avoid fail reloading GUI
				}
			};
			// End of 1st grid -----------------------------------------------------------------------

			
			// 2nd Grid ------------------------------------------------------------------------------		// Basic Constraint Table			
			// 2nd Grid ------------------------------------------------------------------------------		// Basic Constraint Table
			model_basic = new PrismTableModel(rowCount8, colCount8, data8, columnNames8) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			basic_table = new JTable(model_basic) {
				@Override		// These override is to make the width of the cell fit all contents of the cell
				public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
					// For the cell width								
					Component component = super.prepareRenderer(renderer, row, column);
					int rendererWidth = component.getPreferredSize().width;
					TableColumn tableColumn = getColumnModel().getColumn(column);
					int maxWidth = Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth());
					
					// For the column header width
					TableCellRenderer renderer2 = basic_table.getTableHeader().getDefaultRenderer();	
					Component component2 = renderer2.getTableCellRendererComponent(basic_table,
				            tableColumn.getHeaderValue(), false, false, -1, column);
					maxWidth = Math.max(maxWidth, component2.getPreferredSize().width);
					
					if (column != 1) {
						tableColumn.setPreferredWidth(maxWidth);
					} else {
						tableColumn.setMinWidth(140);
					}
									
					// Set icon for cells
					if (column == 2) {
						if (getValueAt(row, 2) == null || getValueAt(row, 2).toString().equals("IDLE")) {
							((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_gray.png"));
						} else if (getValueAt(row, 2).toString().equals("FREE")) {
							((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_blue.png"));
						} else if (getValueAt(row, 2).toString().equals("SOFT")) {
							((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_yellow.png"));
						} else if (getValueAt(row, 2).toString().equals("HARD")) {
							((DefaultTableCellRenderer) component).setIcon(IconHandle.get_scaledImageIcon(10, 10, "icon_circle_red.png"));
						}
					} else {
						((DefaultTableCellRenderer) component).setIcon(null);
					}
							
					return component;
				}
				
				@Override	// Implement table cell tool tips           
				public String getToolTipText(MouseEvent e) {
					java.awt.Point p = e.getPoint();
					int row = rowAtPoint(p);
					int col = columnAtPoint(p);
					String tip = (basic_table.getColumnName(col).equals("bc_description") && row >= 0 && getValueAt(row, col) != null) ? getValueAt(row, col).toString() : null;
					return tip;
				}	
			};			
			
			// Add listener to get the currently selected rows and put IDs into the id_list
			basic_table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	id_list_model = new DefaultListModel<>();					
					int[] selectedRow = basic_table.getSelectedRows();	
					for (int i = 0; i < selectedRow.length; i++) {
						selectedRow[i] = basic_table.convertRowIndexToModel(selectedRow[i]);	///Convert row index because "Sort" causes problems
						id_list_model.addElement(data8[selectedRow[i]][0]);				
					}	
					id_list.setModel(id_list_model);
		        }
		    });			
			basic_table.setAutoResizeMode(0);		// 0 = JTable.AUTO_RESIZE_OFF
			
			
	        // Set Double precision for cells
	        DefaultTableCellRenderer r2 = new DefaultTableCellRenderer() {
	            @Override
	            public Component getTableCellRendererComponent(JTable table, Object
				value, boolean isSelected, boolean hasFocus, int row, int column) {			
					setHorizontalAlignment(JLabel.RIGHT);			
					DecimalFormat formatter = new DecimalFormat("###,###.###");
					formatter.setMinimumFractionDigits(0);
					formatter.setMaximumFractionDigits(10);	// show value with max 10 digits after the dot if it is double value
					if (value instanceof Double) {
						value = formatter.format((Number) value);
					}
					return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	            }
	        };
	             
			
			for (int i = 0; i < basic_table.getColumnCount(); i++) {
				if (i < 3 || i > 8) {

				} else {
					basic_table.getColumnModel().getColumn(i).setCellRenderer(r2);
				}
			}
			
			// Hide columns
			TableColumnsHandle table_handle = new TableColumnsHandle(basic_table);
			table_handle.setColumnVisible("parameter_index", false);
			table_handle.setColumnVisible("static_identifiers", false);
			table_handle.setColumnVisible("dynamic_identifiers", false);
			table_handle.setColumnVisible("original_dynamic_identifiers", false);
			
			PrismTitleScrollPane basic_table_scrollPane = new PrismTitleScrollPane("Sources (Basic Constraints)", "CENTER", basic_table);
//			basic_table_scrollPane.get_nested_scrollpane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			basic_table_scrollPane.setPreferredSize(new Dimension(500, 0));
			// End of 2nd Grid -----------------------------------------------------------------------							
	    	
	
			// 4th Grid ------------------------------------------------------------------------------		// Buttons	
			// 4th Grid -----------------------------------------------------------------------------
			// Add all buttons to a Panel----------------------------------
			button_table_Panel = new JPanel(new GridBagLayout());
			TitledBorder border3 = new TitledBorder("Flow Constraints");
			border3.setTitleJustification(TitledBorder.CENTER);
			button_table_Panel.setBorder(border3);
			GridBagConstraints c2 = new GridBagConstraints();
			c2.fill = GridBagConstraints.BOTH;
			c2.insets = new Insets(0, 5, 10, 10); // padding top 0, left 5, bottom 10, right 10
			
			
			JButton btn_NewSingle = new JButton();
			btn_NewSingle.setFont(new Font(null, Font.BOLD, 14));
			btn_NewSingle.setToolTipText("New: require at least 2 unempty Sigma boxes");
			btn_NewSingle.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_add.png"));					
			c2.gridx = 0;
			c2.gridy = 0;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_NewSingle, c2);
			
			
			JSpinner spin_move_rows = new JSpinner (new SpinnerNumberModel(1, 0, 2, 1));
			spin_move_rows.setToolTipText("Move");
			JFormattedTextField SpinnerText = ((DefaultEditor) spin_move_rows.getEditor()).getTextField();
			SpinnerText.setHorizontalAlignment(JTextField.LEFT);
			SpinnerText.setEditable(false);
			SpinnerText.setFocusable(false);
//			DefaultFormatter formatter = (DefaultFormatter) SpinnerText.getFormatter();
//		    formatter.setCommitsOnValidEdit(true);
		    spin_move_rows.setEnabled(false);
		    c2.gridx = 0;
			c2.gridy = 1;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(spin_move_rows, c2);
			
			
			JButton btn_Edit = new JButton();
			btn_Edit.setToolTipText("Modify");
			btn_Edit.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_swap.png"));
			btn_Edit.setEnabled(false);					
			c2.gridx = 0;
			c2.gridy = 2;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Edit, c2);
			
			
			JButton btn_Delete = new JButton();
			btn_Delete.setFont(new Font(null, Font.BOLD, 14));
			btn_Delete.setToolTipText("Delete");
			btn_Delete.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_erase.png"));
			btn_Delete.setEnabled(false);				
			c2.gridx = 0;
			c2.gridy = 3;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Delete, c2);

			
			JToggleButton btn_Sort = create_sort_button(button_table_Panel, c2, 0, 4, 0, 0);
			btn_Sort.addActionListener(e -> apply_sort_or_nosort(btn_Sort, model9, table9));

			
			JToggleButton btn_Examine = new JToggleButton();
			btn_Examine.setSelected(false);
			btn_Examine.setEnabled(false);
			btn_Examine.setFocusPainted(false);
			btn_Examine.setFont(new Font(null, Font.BOLD, 12));
			btn_Examine.setToolTipText("Examine");
			btn_Examine.setIcon(IconHandle.get_scaledImageIcon(16, 16, "icon_zoom.png"));	
			c2.gridx = 0;
			c2.gridy = 5;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_Examine, c2);
			
			
			c2.insets = new Insets(0, 0, 0, 0); // No padding
			// Add Empty Label to make all buttons on top not middle
			c2.gridx = 0;
			c2.gridy = 6;
			c2.weightx = 0;
			c2.weighty = 1;
			button_table_Panel.add(new JLabel(), c2);
			
			
			// Add table9				
			create_table9();
			JScrollPane table_ScrollPane = new JScrollPane(table9);	
			c2.gridx = 1;
			c2.gridy = 0;
			c2.weightx = 1;
			c2.weighty = 1;
			c2.gridheight = 7;
			button_table_Panel.add(table_ScrollPane, c2);
			// End of 4th Grid -----------------------------------------------------------------------
			// End of 4th Grid -----------------------------------------------------------------------	
			
			
			
			// Add Listeners for table9 & buttons----------------------------------------------------------
			// Add Listeners for table9 & buttons----------------------------------------------------------
			// table9
			class Table_Interaction {
				void refresh() {
					int[] selectedRow = table9.getSelectedRows();
					if (selectedRow.length == 1) {		// Enable Edit	when: 1 row is selected and no cell is editing						
						flow_scrollPane.reload_flow_arrangement_for_one_flow(table9, data9, spin_sigma);
						btn_Edit.setEnabled(true);
						btn_Examine.setEnabled(true);
					} else {		// Disable Edit
						btn_Edit.setEnabled(false);
						btn_Examine.setEnabled(false);
						flow_scrollPane.create_flow_arrangement_UI(new DefaultListModel[0]);	// show nothing: no Sigma box
						spin_sigma.setValue(0);	// set the spin sigma to zero
					}
					
					if (selectedRow.length >= 1 && table9.isEnabled()) {		// Enable Delete  when: >=1 row is selected, table is enable (often after Edit button finished its task)
						btn_Delete.setEnabled(true);
					} else {		// Disable Delete
						btn_Delete.setEnabled(false);
					}		
					
					if (selectedRow.length >= 1 && btn_Sort.getText().equals("OFF")) {	// Enable Spinner when: >=1 row is selected and Sorter is off
						spin_move_rows.setEnabled(true);
					} else {		// Disable Spinner
						spin_move_rows.setEnabled(false);
					}
				}
			}
			Table_Interaction table_interaction = new Table_Interaction();
			table9.addMouseListener(new MouseAdapter() { // Add listener
				@Override
				public void mouseReleased(MouseEvent e) {
					table_interaction.refresh();
				}
			});
			
			table9.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		        public void valueChanged(ListSelectionEvent event) {
		        	table_interaction.refresh();
		        }
		    });
			

			// New single
			btn_NewSingle.addActionListener(e -> {	
				if (flow_scrollPane.get_flow_info_from_GUI().contains(";")) {	// Add constraint if there are at least 2 terms separated by ;
					if (!non_existing_ids_in_the_flow(flow_scrollPane.get_basic_ids_from_GUI()).isEmpty()) {  // the case when flow arrangement contain non-existing basic constraints
						String warning_message = "Flow cannot be added because Flow Arrangement contains non-existing basic constraints:\n"
								+ non_existing_ids_in_the_flow(flow_scrollPane.get_basic_ids_from_GUI());
						PrismTextAreaReadMe warning_textarea = new PrismTextAreaReadMe("icon_tree.png", 70, 70);
						warning_textarea.append(warning_message);
						warning_textarea.setSelectionStart(0);	// scroll to top
						warning_textarea.setSelectionEnd(0);
						warning_textarea.setEditable(false);
						PrismTitleScrollPane warning_scrollpane = new PrismTitleScrollPane("", "LEFT", warning_textarea);
						warning_scrollpane.get_nested_scrollpane().setPreferredSize(new Dimension(550, 200));
						
						String ExitOption[] = {"OK"};
						int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), warning_scrollpane, "Flow cannot be added",
								JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_warning.png"), ExitOption, ExitOption[0]);
					} else {	  // the case when flow arrangement does not contain any non-existing basic constraints --> add flow
						if (table9.isEditing()) {
							table9.getCellEditor().stopCellEditing();
						}
						
						// Add 1 row
						rowCount9++;
						data9 = new Object[rowCount9][colCount9];
						for (int ii = 0; ii < rowCount9 - 1; ii++) {
							for (int jj = 0; jj < colCount9; jj++) {
								data9[ii][jj] = model9.getValueAt(ii, jj);
							}	
						}
						
						data9[rowCount9 - 1][2] = flow_scrollPane.get_flow_info_from_GUI();	
						data9[rowCount9 - 1][3] = "FREE";
						model9.updateTableModelPrism(rowCount9, colCount9, data9, columnNames9);
						update_id();
						model9.fireTableDataChanged();
						quick_edit = new Panel_QuickEdit_FlowConstraints(table9, data9);		// 2 lines to update data for Quick Edit Panel
			 			scrollpane_QuickEdit.setViewportView(quick_edit);
						
						// Convert the new Row to model view and then select it 
						int newRow = table9.convertRowIndexToView(rowCount9 - 1);
						table9.setRowSelectionInterval(newRow, newRow);
						table9.scrollRectToVisible(new Rectangle(table9.getCellRect(newRow, 0, true)));
						
						flow_scrollPane.reload_flow_arrangement_for_one_flow(table9, data9, spin_sigma);
						
						
						if (idle_ids_in_the_flow(flow_scrollPane.get_basic_ids_from_GUI()).isEmpty()) {  // when flow arrangement does not contain IDLE constraints
							// do nothing
						} else {  // when flow arrangement contains some IDLE constraints 
							String warning_message = "Below list shows IDLE basic constraints which are turned into FREE:\n"
									+ idle_ids_in_the_flow(flow_scrollPane.get_basic_ids_from_GUI());
							PrismTextAreaReadMe warning_textarea = new PrismTextAreaReadMe("icon_tree.png", 70, 70);
							warning_textarea.append(warning_message);
							warning_textarea.setSelectionStart(0);	// scroll to top
							warning_textarea.setSelectionEnd(0);
							warning_textarea.setEditable(false);
							PrismTitleScrollPane warning_scrollpane = new PrismTitleScrollPane("", "LEFT", warning_textarea);
							warning_scrollpane.get_nested_scrollpane().setPreferredSize(new Dimension(550, 200));
							
							// turn IDLE basic constraints into FREE (both table 9 and basic_table)
							for (int i = 0; i < data8.length; i++) {
								if (idle_ids_in_the_flow(flow_scrollPane.get_basic_ids_from_GUI()).contains((int) data8[i][0])) {
									data8[i][2] = "FREE";
								}
							}
							model8.fireTableDataChanged();
							panel_Flow_Constraints_GUI.get_model_basic().updateTableModelPrism(rowCount8, colCount8, data8, columnNames8);
							panel_Flow_Constraints_GUI.get_model_basic().fireTableDataChanged();
							
							// show message that IDLE will be changed to FREE
							String ExitOption[] = {"OK"};
							int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), warning_scrollpane, "Flow is added by turning some IDLE basic constraints into FREE",
									JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_warning.png"), ExitOption, ExitOption[0]);
						}
					}
				}			
			});			
			
			
			// Edit
			btn_Edit.addActionListener(e -> {
				if (flow_scrollPane.get_flow_info_from_GUI().contains(";")) {	// Add constraint if there are at least 2 terms separated by ;
					if (!non_existing_ids_in_the_flow(flow_scrollPane.get_basic_ids_from_GUI()).isEmpty()) {  // the case when flow arrangement contain non-existing basic constraints
						String warning_message = "Flow cannot be modified because Flow Arrangement contains non-existing basic constraints:\n"
								+ non_existing_ids_in_the_flow(flow_scrollPane.get_basic_ids_from_GUI());
						PrismTextAreaReadMe warning_textarea = new PrismTextAreaReadMe("icon_tree.png", 70, 70);
						warning_textarea.append(warning_message);
						warning_textarea.setSelectionStart(0);	// scroll to top
						warning_textarea.setSelectionEnd(0);
						warning_textarea.setEditable(false);
						PrismTitleScrollPane warning_scrollpane = new PrismTitleScrollPane("", "LEFT", warning_textarea);
						warning_scrollpane.get_nested_scrollpane().setPreferredSize(new Dimension(550, 200));
						
						String ExitOption[] = {"OK"};
						int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), warning_scrollpane, "Flow cannot be modified",
								JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_warning.png"), ExitOption, ExitOption[0]);
					} else {	  // the case when flow arrangement does not contain any non-existing basic constraints --> add flow
						if (table9.isEditing()) {
							table9.getCellEditor().stopCellEditing();
						}
						
						if (table9.isEnabled()) {						
							int selectedRow = table9.getSelectedRow();
							selectedRow = table9.convertRowIndexToModel(selectedRow);		// Convert row index because "Sort" causes problems										
							
							if (flow_scrollPane.get_flow_info_from_GUI().contains(";")) {	// Edit is accepted if there are at least 2 terms separated by ;
								data9[selectedRow][2] = flow_scrollPane.get_flow_info_from_GUI();					
								model9.fireTableDataChanged();	
								
								// Convert the edited Row to model view and then select it 
								int editRow = table9.convertRowIndexToView(selectedRow);
								table9.setRowSelectionInterval(editRow, editRow);
								
								flow_scrollPane.reload_flow_arrangement_for_one_flow(table9, data9, spin_sigma);
							}									
						}
						
						
						if (idle_ids_in_the_flow(flow_scrollPane.get_basic_ids_from_GUI()).isEmpty()) {  // when flow arrangement does not contain IDLE constraints
							// do nothing
						} else {  // when flow arrangement contains some IDLE constraints 
							String warning_message = "Below list shows IDLE basic constraints which are turned into FREE:\n"
									+ idle_ids_in_the_flow(flow_scrollPane.get_basic_ids_from_GUI());
							PrismTextAreaReadMe warning_textarea = new PrismTextAreaReadMe("icon_tree.png", 70, 70);
							warning_textarea.append(warning_message);
							warning_textarea.setSelectionStart(0);	// scroll to top
							warning_textarea.setSelectionEnd(0);
							warning_textarea.setEditable(false);
							PrismTitleScrollPane warning_scrollpane = new PrismTitleScrollPane("", "LEFT", warning_textarea);
							warning_scrollpane.get_nested_scrollpane().setPreferredSize(new Dimension(550, 200));
							
							// turn IDLE basic constraints into FREE (both table 9 and basic_table)
							for (int i = 0; i < data8.length; i++) {
								if (idle_ids_in_the_flow(flow_scrollPane.get_basic_ids_from_GUI()).contains((int) data8[i][0])) {
									data8[i][2] = "FREE";
								}
							}
							model8.fireTableDataChanged();
							panel_Flow_Constraints_GUI.get_model_basic().updateTableModelPrism(rowCount8, colCount8, data8, columnNames8);
							panel_Flow_Constraints_GUI.get_model_basic().fireTableDataChanged();
							
							// show message that IDLE will be changed to FREE
							String ExitOption[] = {"OK"};
							int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), warning_scrollpane, "Flow is modified by turning some IDLE basic constraints into FREE",
									JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_warning.png"), ExitOption, ExitOption[0]);
						}
					}
				}	
			});
			
			
			btn_Edit.addMouseListener(new MouseAdapter() { // Add listener
				public void mouseEntered(java.awt.event.MouseEvent e) {
					if (table9.getSelectedRows().length == 1) {
						flow_scrollPane.highlight();
					}
				}

				public void mouseExited(java.awt.event.MouseEvent e) {
					if (table9.getSelectedRows().length == 1) {
						flow_scrollPane.unhighlight();
					}
				}
			});
			
			
			// Spinner
		    spin_move_rows.addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
					int up_or_down = (int) spin_move_rows.getValue() - 1;										
					spin_move_rows.setValue((int) 1);	// Reset spinner value to 1
										
					if (up_or_down == 1) {	// move up
						// Cancel editing before moving conditions up or down
						if (table9.isEditing()) {
							table9.getCellEditor().cancelCellEditing();
						}	
						
						// Get selected rows
						int[] selectedRow = table9.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(0) > 0) {	// If there is at least 1 row selected & the first row is not selected
							for (int i = 0; i < rowCount9; i++) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount9; j++) {
										Object temp = data9[i - 1][j];
										data9[i - 1][j] = data9[i][j];
										data9[i][j] = temp;
									}
								}
							}							
							model9.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table9.addRowSelectionInterval(i - 1, i - 1);
							}
						}
					}
										
					if (up_or_down == -1) {	// move down						
						if (table9.isEditing()) {
							table9.getCellEditor().cancelCellEditing();	// cancel editing before moving conditions up or down
						}	
						
						// Get selected rows
						int[] selectedRow = table9.getSelectedRows();		// No need to convert row index because we never allow Sort when moving rows
						List<Integer> selectedRowList = new ArrayList<Integer>() {{ for (int i : selectedRow) add(i);}};	// Convert array to list
						
						if (selectedRowList.size() >=1 && selectedRowList.get(selectedRowList.size() - 1) < rowCount9 - 1) {	// If ...
							for (int i = rowCount9 - 1; i >= 0; i--) {
								if (selectedRowList.contains(i)) {		
									for (int j = 0; j < colCount9; j++) {
										Object temp = data9[i + 1][j];
										data9[i + 1][j] = data9[i][j];
										data9[i][j] = temp;
									}
								}
							}						
							model9.fireTableDataChanged();	// Update the changes and select the currently selected conditions
							for (int i: selectedRow) {
								table9.addRowSelectionInterval(i + 1, i + 1);
							}	
						}						
					}
					
					// Scroll to the first row of the current selected rows
					table9.scrollRectToVisible(new Rectangle(table9.getCellRect(table9.convertRowIndexToView(table9.getSelectedRow()), 0, true)));	
					flow_scrollPane.reload_flow_arrangement_for_one_flow(table9, data9, spin_sigma);
		        }
		    });
		    
				
			// Delete
			btn_Delete.addActionListener(e -> {	
				//Cancel editing before delete
				if (table9.isEditing()) {
					table9.getCellEditor().cancelCellEditing();
				}				
				
				String ExitOption[] = {"Delete", "Cancel"};
				int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), "Delete now?", "Confirm Delete",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[0]);
				if (response == 0) {
					// Get selected rows
					int[] selectedRow = table9.getSelectedRows();	
					for (int i = 0; i < selectedRow.length; i++) {
						selectedRow[i] = table9.convertRowIndexToModel(selectedRow[i]);	///Convert row index because "Sort" causes problems
					}
					
					// Create a list of selected row indexes
					List<Integer> selected_Index = new ArrayList<Integer>();				
					for (int i: selectedRow) {
						selected_Index.add(i);
					}	
					
					// Get values to the new data9
					data9 = new Object[rowCount9 - selectedRow.length][colCount9];
					int newRow =0;
					for (int ii = 0; ii < rowCount9; ii++) {
						if (!selected_Index.contains(ii)) {			//If row not in the list then add to data9 row
							for (int jj = 0; jj < colCount9; jj++) {
								data9[newRow][jj] = model9.getValueAt(ii, jj);
							}
							newRow++;
						}
					}
					// Pass back the info to table model
					rowCount9 = rowCount9 - selectedRow.length;
					model9.updateTableModelPrism(rowCount9, colCount9, data9, columnNames9);
					model9.fireTableDataChanged();	
					quick_edit = new Panel_QuickEdit_FlowConstraints(table9, data9);		// 2 lines to update data for Quick Edit Panel
		 			scrollpane_QuickEdit.setViewportView(quick_edit);
				}
			});

			// Examine
			btn_Examine.addActionListener(e -> {
				if (btn_Examine.isSelected()) {
					btn_NewSingle.setEnabled(false); 
					spin_move_rows.setEnabled(false);
					btn_Edit.setEnabled(false);
					btn_Delete.setEnabled(false);
					btn_Sort.setEnabled(false);
					quick_edit.disable_all_apply_buttons();
					
					if (table9.isEditing()) {
						table9.getCellEditor().stopCellEditing();
					}
					int selectedRow = table9.getSelectedRow();
					selectedRow = table9.convertRowIndexToModel(selectedRow);	///Convert row index because "Sort" causes problems
					String flow_type = data9[selectedRow][3].toString();
					PrismTextAreaReadMe examine_textarea = new PrismTextAreaReadMe("icon_tree.png", 70, 70);
					PrismTitleScrollPane examine_scrollpane = new PrismTitleScrollPane("", "LEFT", examine_textarea);
					examine_scrollpane.setPreferredSize(new Dimension(0, 0));
					examine_scrollpane.setBorder(null);
					table_ScrollPane.setViewportView(examine_scrollpane);
					
					if (flow_type.equals("FREE")) {
						examine_textarea.append("This flow has no impact because it is FREE. Only HARD flows have impact.");
					} else {	// HARD
						// Show all the constraints in this flow set
						double lb_percentage = (data9[selectedRow][4] != null) ? Double.parseDouble(data9[selectedRow][4].toString()) : -9999;
						double ub_percentage = (data9[selectedRow][5] != null) ? Double.parseDouble(data9[selectedRow][5].toString()) : -9999;
						if (lb_percentage > 0 || ub_percentage >= 0) {
							examine_textarea.append("Note: number in the bracket represents bc_id\n");
							examine_textarea.append("The selected flow includes below constraints");
							if (lb_percentage >= 0 && ub_percentage >= 0 && lb_percentage > ub_percentage) examine_textarea.append(" (the model is likely INFEASIBLE because LB percentage > UB percentage)");
							examine_textarea.append(":\n");
							
							String flow_info = flow_scrollPane.get_flow_info_from_GUI();	
							String[] sigma_array = flow_info.split(";");
							for (int s = 0; s < sigma_array.length - 1; s++) {
								List<String> ids_in_sigma_before = Arrays.asList(sigma_array[s].split(" "));
								List<String> ids_in_sigma_after = Arrays.asList(sigma_array[s + 1].split(" "));
								
								if (lb_percentage > 0) {
									if (lb_percentage != 100) examine_textarea.append(NumberHandle.get_string_with_15_digits(lb_percentage / 100) + " * ");
									if (ids_in_sigma_before.size() > 1 && lb_percentage != 100) examine_textarea.append("(");
									for (int id = 0; id < ids_in_sigma_before.size(); id++) {
										examine_textarea.append("[" + ids_in_sigma_before.get(id) + "]");
										if (id < ids_in_sigma_before.size() - 1 ) examine_textarea.append("+");
									}
									if (ids_in_sigma_before.size() > 1 && lb_percentage != 100) examine_textarea.append(")");
									examine_textarea.append(" <= ");
									for (int id = 0; id < ids_in_sigma_after.size(); id++) {
										examine_textarea.append("[" + ids_in_sigma_after.get(id) + "]");
										if (id < ids_in_sigma_after.size() - 1 ) examine_textarea.append("+");
									}
								}
								
								if (lb_percentage > 0 && ub_percentage >= 0) examine_textarea.append("        and        ");
								
								if (ub_percentage > 0) {
									for (int id = 0; id < ids_in_sigma_after.size(); id++) {
										examine_textarea.append("[" + ids_in_sigma_after.get(id) + "]");
										if (id < ids_in_sigma_after.size() - 1 ) examine_textarea.append("+");
									}
									examine_textarea.append(" <= ");
									if (ub_percentage != 100) examine_textarea.append(NumberHandle.get_string_with_15_digits(ub_percentage / 100) + " * ");
									if (ids_in_sigma_before.size() > 1 && lb_percentage != 100) examine_textarea.append("(");
									for (int id = 0; id < ids_in_sigma_before.size(); id++) {
										examine_textarea.append("[" + ids_in_sigma_before.get(id) + "]");
										if (id < ids_in_sigma_before.size() - 1 ) examine_textarea.append("+");
									}
									if (ids_in_sigma_before.size() > 1 && lb_percentage != 100) examine_textarea.append(")");
								} else if (ub_percentage == 0) {
									for (int id = 0; id < ids_in_sigma_after.size(); id++) {
										examine_textarea.append("[" + ids_in_sigma_after.get(id) + "]");
										if (id < ids_in_sigma_after.size() - 1 ) examine_textarea.append("+");
									}
									examine_textarea.append(" <= 0");
								}
								examine_textarea.append("\n");	
							}
						} else {
							examine_textarea.append("This flow has no impact. You might want to change LB percentage and/or UB percentage");
						}
					}
					
					// Write the bc_description of all the unique bc_id in the flow
					examine_textarea.append("\nDescriptions of basic constraints in this flow:\n");
					Set<String> bc_id_set = new LinkedHashSet();
					String flow_info = flow_scrollPane.get_flow_info_from_GUI();	
					String[] sigma_array = flow_info.split(";");
					for (int s = 0; s < sigma_array.length; s++) {
						List<String> ids_in_this_sigma = Arrays.asList(sigma_array[s].split(" "));
						bc_id_set.addAll(ids_in_this_sigma);
					}
					for (int i = 0; i < data8.length; i++) {
						if (bc_id_set.contains(String.valueOf(data8[i][0]))) {
							examine_textarea.append("[" + data8[i][0] + "]     " + data8[i][1] + "\n");
						}
					}
					
					examine_textarea.setSelectionStart(0);	// scroll to top
					examine_textarea.setSelectionEnd(0);
					examine_textarea.setEditable(false);
				} else {
					table_ScrollPane.setViewportView(table9);
					btn_NewSingle.setEnabled(true); 
					spin_move_rows.setEnabled(true);
					btn_Edit.setEnabled(true);
					btn_Delete.setEnabled(true);
					btn_Sort.setEnabled(true);
					quick_edit.enable_all_apply_buttons();
				}
			});			
			// End of Listeners for table9 & buttons -----------------------------------------------------------------------
			// End of Listeners for table9 & buttons -----------------------------------------------------------------------		    
		    
	
			
			
			
			
			
			
	        // scrollPane Quick Edit ----------------------------------------------------------------------	
			// scrollPane Quick Edit ----------------------------------------------------------------------	
			quick_edit = new Panel_QuickEdit_FlowConstraints(table9, data9);
 			scrollpane_QuickEdit = new JScrollPane(quick_edit);
 			TitledBorder border = new TitledBorder("Quick Edit");
 			border.setTitleJustification(TitledBorder.CENTER);
 			scrollpane_QuickEdit.setBorder(border);
 			scrollpane_QuickEdit.setVisible(false);		
 			
		

 			
			// ToolBar Panel ----------------------------------------------------------------------------
			// ToolBar Panel ----------------------------------------------------------------------------
			ToolBarWithBgImage helpToolBar = new ToolBarWithBgImage("Project Tools", JToolBar.HORIZONTAL, null);
			helpToolBar.setFloatable(false);	//to make a tool bar immovable
			helpToolBar.setRollover(true);	//to visually indicate tool bar buttons when the user passes over them with the cursor
			helpToolBar.setBorderPainted(false);
			
			// button Quick Edit
 			JToggleButton btnQuickEdit = new JToggleButton();
 			btnQuickEdit.setToolTipText("Show Quick Edit Tool");
 			btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
 			btnQuickEdit.addActionListener(e -> {		
 				if (btnQuickEdit.getToolTipText().equals("Show Quick Edit Tool")) {
					btnQuickEdit.setToolTipText("Hide Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_hide.png"));
					scrollpane_QuickEdit.setVisible(true);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_Flow_Constraints_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				} else {
					btnQuickEdit.setToolTipText("Show Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
					scrollpane_QuickEdit.setVisible(false);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_Flow_Constraints_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				}
 			});				
			
			// button Help
			JButton btnHelp = new JButton();
			btnHelp.setToolTipText("Help");
			btnHelp.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_help.png"));
			btnHelp.addActionListener(e -> {

			});
						
			// spinner
			spin_sigma = new JSpinner(new SpinnerNumberModel(5, 0, 1000, 1));
			spin_sigma.setToolTipText("Total number of Sigma");
			JFormattedTextField sigma_spinner_text = ((DefaultEditor) spin_sigma.getEditor()).getTextField();
			sigma_spinner_text.setHorizontalAlignment(JTextField.LEFT);		
			DefaultFormatter formatter = (DefaultFormatter) sigma_spinner_text.getFormatter();
		    formatter.setCommitsOnValidEdit(true);
		    spin_sigma.addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
		        	int total_sigma = (int) spin_sigma.getValue();
		        	DefaultListModel[] list_model = new DefaultListModel[total_sigma];	
		        	
		        	for (int i = 0; i < total_sigma; i++) {	// Make empty Sigma boxes first
						list_model[i] = new DefaultListModel<>();				
					}
		        	
		        	for (int i = 0; i < flow_scrollPane.get_list_model().length; i++) {	// Then assign the recent non-empty Sigma boxes to the empty ones if possible
		        		if (i < total_sigma) {
		        			list_model[i] = flow_scrollPane.get_list_model()[i];
		        		}
		        	}		        					
					flow_scrollPane.create_flow_arrangement_UI(list_model);		        	
		        }
		    });	
						
						
			// Add all buttons to flow_panel
		    helpToolBar.add(spin_sigma);
			helpToolBar.add(Box.createGlue());	//Add glue for Right alignment
			helpToolBar.add(btnQuickEdit);
			helpToolBar.add(btnHelp);
			// End of ToolBar Panel -----------------------------------------------------------------------
			// End of ToolBar Panel ----------------------------------------------------------------------- 			
			
			

			
				

			
		    
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			JSplitPane split_pane = new JSplitPane();
			split_pane.setBorder(null);
			split_pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			split_pane.setDividerSize(3);
			split_pane.setDividerLocation(280);
						
			JPanel upper_panel = new JPanel();
			upper_panel.setBorder(null);
			upper_panel.setLayout(new GridBagLayout());			
			
			JPanel lower_panel = new JPanel();
			lower_panel.setBorder(null);
			lower_panel.setLayout(new GridBagLayout());
			
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;

			// Add helpToolBar to the main Grid
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 2;
			c.gridheight = 1;
			c.weightx = 0;
		    c.weighty = 0;
		    upper_panel.add(helpToolBar, c);
			
			// Add flow_scrollPane to the main Grid
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 0.7;
		    c.weighty = 1;
		    upper_panel.add(flow_scrollPane, c);				
		    		
			// Add basic_table_scrollPane to the main Grid
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 0.3;
		    c.weighty = 1;
		    upper_panel.add(basic_table_scrollPane, c);				    							
		    	    		  
			// Add the button_table_Panel & scrollpane_QuickEdit to a new Panel then add that panel to the main Grid
			JPanel button_table_qedit_panel = new JPanel();
			button_table_qedit_panel.setLayout(new BorderLayout());
			button_table_qedit_panel.add(button_table_Panel, BorderLayout.CENTER);
			button_table_qedit_panel.add(scrollpane_QuickEdit, BorderLayout.EAST);			
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 2; 
			c.gridheight = 1;
			c.weightx = 1;
		    c.weighty = 1;
		    lower_panel.add(button_table_qedit_panel, c);
			
			
			split_pane.setLeftComponent(upper_panel);
			split_pane.setRightComponent(lower_panel);
			super.add(split_pane, BorderLayout.CENTER);
			
			
			// when radioButton_Right[8] is selected, Sources (basic constraints) will be updated
			radio_button[8].addActionListener(this);
		}
		
		// Listener for this class----------------------------------------------------------------------
	    public void actionPerformed(ActionEvent e) {
			model_basic.updateTableModelPrism(rowCount8, colCount8, data8, columnNames8);	// Update table8 to the Sources in Advanced constraints GUI
	    }
	    
	    public PrismTableModel get_model_basic() {  		
			return model_basic;
		}
	    
	    // Update id column. id needs to be unique in order to use in flow constraints-----------------
	    public void update_id() {  		
			List<Integer> id_list = new ArrayList<Integer>();			
			
			for (int row = 0; row < rowCount9; row++) {
				if (data9[row][0] != null) {
					id_list.add((int) data9[row][0]);
				}
			}			
			
			for (int row = 0; row < rowCount9; row++) {
				if (data9[row][0] == null) {
					int new_id = (id_list.size() > 0) ? Collections.max(id_list) + 1 : 1;	//new id = (max id + 1) or = 1 if no row
					data9[row][0] = new_id;
					id_list.add(new_id);
				}
			}			
		}
	}

	
	
	//--------------------------------------------------------------------------------------------------------------------------------
	public boolean is_IDLE_basic_constraints_used_in_flow_constraints() {
		String warning_message = "";
		List<Integer> idle_to_free_list = new ArrayList<Integer>();
		
		// create a list of active basic constraints
		List<Integer> active_basic_constraints_list = new ArrayList<Integer>();				
		for (int i = 0; i < data8.length; i++) {
			if (!data8[i][2].equals("IDLE")) {
				active_basic_constraints_list.add((Integer) data8[i][0]);
			}
		}	
		Collections.sort(active_basic_constraints_list);	// sort to search binary

		// create a list for each flow constraint, each contains the ids used in that flow
		List<Integer>[] flow_list = new ArrayList[data9.length];
		for (int i = 0; i < data9.length; i++) {	// loop each flow
			flow_list[i] = new ArrayList<Integer>();
			String[] flow_arrangement = data9[i][2].toString().split(";");
			for (String each_sigma: flow_arrangement) {		// a sigma box might have several ids, separated by a space
				for (String id: each_sigma.split("\\s+")) {
					flow_list[i].add(Integer.valueOf(id));
				}
			}
			
	        // check if the flow contains IDLE basic constraints
			List<Integer> ids_in_this_flow_but_not_active_basic_constraints = new ArrayList<Integer>();
			for (int id: flow_list[i]) {
				int index = Collections.binarySearch(active_basic_constraints_list, id);
				if (index < 0) {	// if id in this flow is not found as in active (hard/soft/free) basic constraints --> add
					ids_in_this_flow_but_not_active_basic_constraints.add(id);
					if (!idle_to_free_list.contains(id)) idle_to_free_list.add(id);
				}
			}

			// test print
			if (!ids_in_this_flow_but_not_active_basic_constraints.isEmpty()) {
				warning_message = warning_message + (data9[i][0] + " " + data9[i][1] + " -----> ");
				for (int id: ids_in_this_flow_but_not_active_basic_constraints) {
					warning_message = warning_message + (id + " ");
				}
				warning_message = warning_message + "\n";
			}
		}	
		
		// if there are IDLE constraints used in the flows
		if (!idle_to_free_list.isEmpty()) {
			// turn IDLE into FREE
			for (int i = 0; i < data8.length; i++) {
				if (data8[i][2].equals("IDLE") && idle_to_free_list.contains((Integer) data8[i][0])) {
					data8[i][2] = "FREE";
				}
			}	
			
			// show popup
			warning_message = "PRISM makes some basic constraints FREE since they are used in flow constraints:\n"
					+ idle_to_free_list + "\n\n"
					+ "Below list shows:\n"
					+ "flow constraint -----> basic constraints you want to be IDLE but PRISM turns into FREE:\n"
					+ warning_message;
			PrismTextAreaReadMe warning_textarea = new PrismTextAreaReadMe("icon_tree.png", 70, 70);
			warning_textarea.append(warning_message);
			warning_textarea.setSelectionStart(0);	// scroll to top
			warning_textarea.setSelectionEnd(0);
			warning_textarea.setEditable(false);
			PrismTitleScrollPane warning_scrollpane = new PrismTitleScrollPane("", "LEFT", warning_textarea);
			warning_scrollpane.get_nested_scrollpane().setPreferredSize(new Dimension(550, 300));
			
			String ExitOption[] = {"OK"};
			int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), warning_scrollpane, "Constraints turn into FREE",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_warning.png"), ExitOption, ExitOption[0]);
			
			return true;
		} else {
			return false;
		}
	}

	public List<Integer> idle_ids_in_the_flow(List<Integer> basic_ids_in_the_flow) {
		// create a list of IDLE basic constraints
		List<Integer> idle_constraints_ids_list = new ArrayList<Integer>();
		for (int i = 0; i < data8.length; i++) {
			if (data8[i][2].equals("IDLE")) {
				idle_constraints_ids_list.add((Integer) data8[i][0]);
			}
		}
		basic_ids_in_the_flow.retainAll(idle_constraints_ids_list);
		return basic_ids_in_the_flow;
	}
	
	public List<Integer> non_existing_ids_in_the_flow(List<Integer> basic_ids_in_the_flow) {
		// create a list of IDLE basic constraints
		List<Integer> all_constraints_ids_list = new ArrayList<Integer>();
		for (int i = 0; i < data8.length; i++) {
			all_constraints_ids_list.add((Integer) data8[i][0]);
		}
		List<Integer> not_present = new ArrayList<Integer>(basic_ids_in_the_flow);
		not_present.removeAll(all_constraints_ids_list);
		return not_present;
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------
	class Area_Merging_GUI extends JLayeredPane {
		JButton btn_GetResult;
		
		public Area_Merging_GUI() {
			setLayout(new BorderLayout());
			
			
			
			
			// 1st grid -----------------------------------------------------------------------
			// 1st grid -----------------------------------------------------------------------	
			JPanel button_table_Panel = new JPanel(new GridBagLayout());
			TitledBorder border = new TitledBorder("Area Merging Requirements");
			border.setTitleJustification(TitledBorder.CENTER);
			button_table_Panel.setBorder(border);
			GridBagConstraints c2 = new GridBagConstraints();
			c2.fill = GridBagConstraints.BOTH;
			c2.insets = new Insets(0, 5, 10, 10); // padding top 0, left 5, bottom 10, right 10


			JToggleButton btn_Sort = create_sort_button(button_table_Panel, c2, 0, 0, 0, 0);
			btn_Sort.addActionListener(e -> apply_sort_or_nosort(btn_Sort, model10, table10));


			create_mass_check_button(button_table_Panel, c2, 0, 1, 0, 0).addActionListener(e -> apply_mass_check_or_uncheck("mass_check", model10, table10, data10, colCount10));
			create_mass_uncheck_button(button_table_Panel, c2, 0, 2, 0, 0).addActionListener(e -> apply_mass_check_or_uncheck("mass_uncheck", model10, table10, data10, colCount10));
			
			
			// compact view
			JButton btn_compact = new JButton();
			btn_compact.setVerticalTextPosition(SwingConstants.BOTTOM);
			btn_compact.setHorizontalTextPosition(SwingConstants.CENTER);
			btn_compact.setToolTipText("switch to compact view");
			btn_compact.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_script_gray.png"));
			btn_compact.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_script_gray.png"));
			btn_compact.setContentAreaFilled(false);
			btn_compact.addActionListener(e -> {
				if (table10.isEditing()) {
					table10.getCellEditor().cancelCellEditing();
				}
				switch (btn_compact.getToolTipText()) {
				case "switch to compact view":
					if (data10 != null) {		
						RowFilter<Object, Object> compact_filter = new RowFilter<Object, Object>() {
							public boolean include(Entry entry) {
								Boolean implementation = (boolean) entry.getValue(8);
								return implementation == true;
							}
						};
						TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>((PrismTableModel) table10.getModel());
						sorter.setRowFilter(compact_filter);
						table10.setRowSorter(sorter);
					}
					btn_compact.setToolTipText("switch to full view");
					btn_compact.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_script.png"));
					btn_compact.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_script.png"));
					break;
				case "switch to full view":
					table10.setRowSorter(null);
					btn_compact.setToolTipText("switch to compact view");
					btn_compact.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_script_gray.png"));
					btn_compact.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_script_gray.png"));
					break;
				}
			});
			c2.gridx = 0;
			c2.gridy = 3;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_compact, c2);
					
			
			// GetResult
			btn_GetResult = new JButton() {
				public Point getToolTipLocation(MouseEvent event) {
					return new Point(getWidth() - 10, 8);
				}
			};
			btn_GetResult.setFont(new Font(null, Font.BOLD, 14));
//			btn_GetResult.setText("Get Result");
			btn_GetResult.setToolTipText("Generate merging result & Save");
			btn_GetResult.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_calculator.png"));
			btn_GetResult.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_calculator.png"));
			btn_GetResult.setContentAreaFilled(false);
			c2.gridx = 0;
			c2.gridy = 4;
			c2.weightx = 0;
			c2.weighty = 0;
			button_table_Panel.add(btn_GetResult, c2);
			
			
			c2.insets = new Insets(0, 0, 0, 0); // No padding
			// Add Empty Label to make all buttons on top not middle
			c2.gridx = 0;
			c2.gridy = 5;
			c2.weightx = 0;
			c2.weighty = 1;
			button_table_Panel.add(new JLabel(), c2);
			
			
			// Add table10				
			create_table10();
			JScrollPane table_scrollpane = new JScrollPane(table10);	
			c2.gridx = 1;
			c2.gridy = 0;
			c2.weightx = 1;
			c2.weighty = 1;
			c2.gridheight = 6;
			button_table_Panel.add(table_scrollpane, c2);
			// End of 1st grid -----------------------------------------------------------------------
			// End of 1st grid -----------------------------------------------------------------------						
			
			
			
			
			// 2nd grid -----------------------------------------------------------------------
			// 2nd grid -----------------------------------------------------------------------	
			String message = 
					"For each attribute selected as implementation, its unique values will be ranked based on its merging method\n"
					+ "state_id is the combination of all the ranks from all the selected attributes, separated by underscore\n"
					+ "state_id should be defined by using attributes which represent the forest condition at the very beginning of a planning period (i.e. do not select rmcuft or action_type for implementation) \n"
					+ "state_id will be generated and saved after clicking the calculator button\n"
					+ "After generating state_id, the entire bottom area could be right clicked for more functionality\n"
					+ "For rolling horizon, forest areas would be qualified for merging if they:\n"
					+ "          1. have the same state_id\n"
					+ "          2. are at the same planning period\n"
					+ "          3. have the same forest status (Existing or Regenerated)\n"
					+ "          4. have the same 6 layers (existing strata) or have the same 5 layers (regenerated strata)\n"
					+ "          5. are neither Mixes Severity strata areas nor Bark Beetle strata areas (exclusion is for methods, not for the exact events)\n"
					+ "If none of the attribute is selected for implementation, Prism would apply No-Merging\n"
					+ "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
			PrismTextAreaReadMe merging_textarea = new PrismTextAreaReadMe("icon_script.png", 1, 1 /*32, 32*/);
			merging_textarea.append(message);
			merging_textarea.setSelectionStart(0);	// scroll to top
			merging_textarea.setSelectionEnd(0);
			merging_textarea.setEditable(false);
			
			PrismTitleScrollPane merging_result_scrollpane = new PrismTitleScrollPane("General information", "CENTER", merging_textarea);
			merging_result_scrollpane.setPreferredSize(new Dimension(0, 250));
			merging_result_scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			merging_result_scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			AreaMerging merging_result = new AreaMerging(merging_result_scrollpane);
			// End of 2nd grid -----------------------------------------------------------------------
			// End of 2nd grid -----------------------------------------------------------------------			
			
			
			
			
	        // scrollPane Quick Edit ----------------------------------------------------------------------	
			// scrollPane Quick Edit ----------------------------------------------------------------------	
			Panel_QuickEdit_AreaMerging quick_edit = new Panel_QuickEdit_AreaMerging(table10, data10);
			JScrollPane scrollpane_QuickEdit = new JScrollPane(quick_edit);
 			border = new TitledBorder("Quick Edit");
 			border.setTitleJustification(TitledBorder.CENTER);
 			scrollpane_QuickEdit.setBorder(border);
 			scrollpane_QuickEdit.setVisible(false);	
 			
 			
		
			
			// ToolBar Panel ----------------------------------------------------------------------------
			// ToolBar Panel ----------------------------------------------------------------------------
			ToolBarWithBgImage helpToolBar = new ToolBarWithBgImage("Project Tools", JToolBar.HORIZONTAL, null);
			helpToolBar.setFloatable(false);	//to make a tool bar immovable
			helpToolBar.setRollover(true);	//to visually indicate tool bar buttons when the user passes over them with the cursor
			helpToolBar.setBorderPainted(false);
			
			// button Quick Edit
			JToggleButton btnQuickEdit = new JToggleButton();
			btnQuickEdit.setToolTipText("Show Quick Edit Tool");
			btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
			btnQuickEdit.addActionListener(e -> {		
				if (btnQuickEdit.getToolTipText().equals("Show Quick Edit Tool")) {
						btnQuickEdit.setToolTipText("Hide Quick Edit Tool");
						btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_hide.png"));
						scrollpane_QuickEdit.setVisible(true);
						// Get everything show up nicely
						GUI_Text_splitpane.setLeftComponent(panel_Area_Merging_GUI);
						Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				} else {
					btnQuickEdit.setToolTipText("Show Quick Edit Tool");
					btnQuickEdit.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_show.png"));
					scrollpane_QuickEdit.setVisible(false);
					// Get everything show up nicely
					GUI_Text_splitpane.setLeftComponent(panel_Area_Merging_GUI);
					Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
				}				
			});
			
			// button Help
			JButton btnHelp = new JButton();
			btnHelp.setToolTipText("Help");
			btnHelp.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_help.png"));
			btnHelp.addActionListener(e -> {
				
			});
			
			// Add all buttons to flow_panel
			helpToolBar.add(Box.createGlue());	//Add glue for Right alignment
			helpToolBar.add(btnQuickEdit);
			helpToolBar.add(btnHelp);
			// End of ToolBar Panel -----------------------------------------------------------------------
			// End of ToolBar Panel -----------------------------------------------------------------------


			
			
			// Add Listeners for table10 & buttons----------------------------------------------------------
			// Add Listeners for table10 & buttons----------------------------------------------------------
			btn_GetResult.addActionListener(e -> {
				merging_result.generate_merging_result(data10, read_database);
				data11 = merging_result.get_data11();					// to pass back to save the state_id output
				columnNames11 = merging_result.get_columnNames11();		// to pass back to save the state_id output
			});
			// End of Listeners for table8 & buttons -----------------------------------------------------------------------
			// End of Listeners for table8 & buttons -----------------------------------------------------------------------

			
			
			
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			// Add all Grids to the Main Grid-----------------------------------------------------------------------
			JSplitPane split_pane = new JSplitPane();
			split_pane.setBorder(null);
			split_pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			split_pane.setDividerSize(3);
						
			JPanel upper_panel = new JPanel();
			upper_panel.setBorder(null);
			upper_panel.setLayout(new GridBagLayout());			
			
			JPanel lower_panel = new JPanel();
			lower_panel.setBorder(null);
			lower_panel.setLayout(new GridBagLayout());
			
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
		    c.weighty = 1;
		    	    
		    // Add helpToolBar	
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0;
		    c.weighty = 0;
			c.gridwidth = 2;
			c.gridheight = 1;
			upper_panel.add(helpToolBar, c);				
			
			// Add the 1st grid - Add meging_scrollpane & scrollpane_QuickEdit to a new Panel then add that panel to the main Grid
			JPanel table_qedit_panel = new JPanel();
			table_qedit_panel.setLayout(new BorderLayout());
			table_qedit_panel.add(button_table_Panel, BorderLayout.CENTER);
			table_qedit_panel.add(scrollpane_QuickEdit, BorderLayout.EAST);
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
		    c.weighty = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			upper_panel.add(table_qedit_panel, c);
			
			// Add the 2nd grid -  to the main Grid				
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1; 
			c.gridheight = 1;
			c.weightx = 1;
		    c.weighty = 1;
		    lower_panel.add(merging_result_scrollpane, c);
			
			
			split_pane.setLeftComponent(upper_panel);
			split_pane.setRightComponent(lower_panel);
			super.add(split_pane, BorderLayout.CENTER);	
		}
		
		
	    // Update id column. id needs to be unique in order to use in flow constraints-----------------
	    public void update_id() {  		
			List<Integer> id_list = new ArrayList<Integer>();			
			
			for (int row = 0; row < rowCount5; row++) {
				if (data5[row][0] != null) {
					id_list.add((int) data5[row][0]);
				}
			}			
			
			for (int row = 0; row < rowCount5; row++) {
				if (data5[row][0] == null) {
					int new_id = (id_list.size() > 0) ? Collections.max(id_list) + 1 : 1;	//new id = (max id + 1) or = 1 if no row
					data5[row][0] = new_id;
					id_list.add(new_id);
				}
			}			
		}
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------------------------------
	// Get values to pass to other classes
	// Add all input Files to a list
	public void save_inputs_for_this_run() {
		File input_01_file = new File(currentRunFolder.getAbsolutePath() + "/input_01_general_inputs.txt");
		File input_02_file = new File(currentRunFolder.getAbsolutePath() + "/input_02_model_strata.txt");
		File input_03_file = new File(currentRunFolder.getAbsolutePath() + "/input_03_prescription_assignment.txt");
		File input_04_file = new File(currentRunFolder.getAbsolutePath() + "/input_04_ea_management.txt");
		File input_05_file = new File(currentRunFolder.getAbsolutePath() + "/input_05_non_sr_disturbances.txt");
		File input_06_file = new File(currentRunFolder.getAbsolutePath() + "/input_06_sr_disturbances.txt");
		File input_07_file = new File(currentRunFolder.getAbsolutePath() + "/input_07_management_cost.txt");
		File input_08_file = new File(currentRunFolder.getAbsolutePath() + "/input_08_basic_constraints.txt");
		File input_09_file = new File(currentRunFolder.getAbsolutePath() + "/input_09_flow_constraints.txt");
		File input_10_file = new File(currentRunFolder.getAbsolutePath() + "/input_10_area_merging.txt");
		File input_11_file = new File(currentRunFolder.getAbsolutePath() + "/input_11_state_id.txt");
		create_file_input(input_01_file, data1, columnNames1);
		create_file_input(input_02_file, data3, columnNames3);	
		create_file_input(input_03_file, data2, columnNames2);	// 3 and 2 are currently switched
		create_file_input(input_04_file, data4, columnNames4);
		create_file_input(input_05_file, data5, columnNames5);
		create_file_input(input_06_file, data6, columnNames6);
		create_file_input(input_07_file, data7, columnNames7);
		create_file_input(input_08_file, data8, columnNames8);
		create_file_input(input_09_file, data9, columnNames9);
		create_file_input(input_10_file, data10, columnNames10);
		panel_Area_Merging_GUI.btn_GetResult.doClick();		// This would help get the data11 and columnNames11 for writing the output of state_id
		create_file_input(input_11_file, data11, columnNames11);
		create_file_database();
		create_readmeFile();
	}
	
	
	public void save_inputs_and_delete_outputs_for_this_run() {
		save_inputs_for_this_run();
		// Delete all output files, problem file, and solution file, but keep the fly_constraints file
		File[] contents = currentRunFolder.listFiles();
		if (contents != null) {
			for (File f : contents) {
				if ((f.getName().contains("output") || f.getName().contains("problem") || f.getName().contains("solution")) && !f.getName().contains("fly_constraints")) {
					f.delete();
				}
			}
		}
	}

	
	private void create_file_input(File input_file, Object[][] input_data, String[] input_columnNames) {
		// Delete the old file before writing new contents
		if (input_file.exists()) {
			input_file.delete();
		}
		
		if (input_data != null && input_data.length > 0) {
			try (BufferedWriter fileOut = new BufferedWriter(new FileWriter(input_file))) {
				for (int j = 0; j < input_columnNames.length; j++) {
					fileOut.write(input_columnNames[j] + "\t");
				}
				
				for (int i = 0; i < input_data.length; i++) {
					fileOut.newLine();
					for (int j = 0; j < input_data[i].length; j++) {
						fileOut.write(input_data[i][j] + "\t");
					}		
				}
				fileOut.close();
			} catch (IOException e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			} 
		}
	}
	
	
	private void create_file_database() {	
		// Note for this file, we just copy overwritten
		File databaseFile = new File(currentRunFolder.getAbsolutePath() + "/" + "database.db");	
		try {
			if (file_database != null) Files.copy(file_database.toPath(), databaseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			Prism2Main.get_databases_linkedlist().update(databaseFile, read_database);	// Allow saving the databse.db into remember list after stop editing and save					
		} catch (IOException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}	
	
	
	private void create_readmeFile() {	
        File readmeFile = new File(currentRunFolder.getAbsolutePath() + "/" + "readme.txt");
		if (readmeFile.exists()) {
			readmeFile.delete();		// Delete the old file before writing new contents
		}
			
		FileWriter pw;
		try {
			// Clear first line
			if (readme.getText().startsWith("Model is last edited by")) {
				int end = readme.getLineEndOffset(0); 
				readme.replaceRange("", 0, end);
			}
			// Write new last time edited
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   -   HH:mm:ss");
			readme.getDocument().insertString(0, "Model is last edited by:     " + Prism2Main.get_prism_version()  + "     on     " + dateFormat.format(new Date()) + "\n", null);
			pw = new FileWriter(currentRunFolder.getAbsolutePath() + "/" + "readme.txt");
			readme.write(pw);
			pw.close();
		} catch (BadLocationException | IOException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
}
