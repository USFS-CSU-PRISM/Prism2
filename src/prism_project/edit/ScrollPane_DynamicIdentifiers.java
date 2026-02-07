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

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import prism_convenience.ColorUtil;
import prism_convenience.IconHandle;
import prism_project.data_process.Read_Database;
import prism_root.Prism2Main;

public class ScrollPane_DynamicIdentifiers extends JScrollPane {	
	private JCheckBox checkboxNoIdentifier;
	private List<JCheckBox> allDynamicIdentifiers;
	private JScrollPane selectIdentifiersScrollPanel;
	private List<JScrollPane> allDynamicIdentifiers_ScrollPane;
	private List<List<JCheckBox>> checkboxDynamicIdentifiers;
	private JScrollPane defineScrollPane;		//for Definition of dynamic identifier
	
	private JPanel[] allDynamicIdentifiers_JPanel;
	private JPanel dynamic_identifiersPanel;
	private JPanel select_Panel;
	
	public ScrollPane_DynamicIdentifiers(Read_Database read_Database) {	
		
		String[][][] yield_tables_values = read_Database.get_yield_tables_values();
		String[] yield_tables_column_names = read_Database.get_yield_tables_column_names();	
		
		// Define the Panel contains everything --------------------------
		dynamic_identifiersPanel = new JPanel();
		dynamic_identifiersPanel.setLayout(new GridBagLayout());
		GridBagConstraints c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.BOTH;
		c3.weightx = 1;
	    c3.weighty = 1;
	    // Add elements to this Panel later at the end --------------------------
		
		
	

		//This is the Panel for select all available identifiers--------------------------
		select_Panel = new JPanel();	
		select_Panel.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.weightx = 1;
	    c2.weighty = 1;		
		//------------------------------------------------------------------------------
		
		
	    if (yield_tables_column_names != null && allDynamicIdentifiers == null) {				
			
			checkboxDynamicIdentifiers = new ArrayList<List<JCheckBox>>();	
			allDynamicIdentifiers = new ArrayList<JCheckBox>();
			
			for (int i = 0; i < yield_tables_column_names.length; i++) {
				String YTcolumnName = yield_tables_column_names[i];

				checkboxDynamicIdentifiers.add(new ArrayList<JCheckBox>());		//add empty List
				allDynamicIdentifiers.add(new JCheckBox(YTcolumnName));		//add checkbox
				allDynamicIdentifiers.get(i).setToolTipText(read_Database.get_ParameterToolTip(YTcolumnName) + " (Column index: " + i + ")");		//add toolTip
				
				// add checkboxParameter to the Panel
			    c2.gridx = 0;
			    c2.gridy = 1 + i;
				c2.weightx = 1;
			    c2.weighty = 1;
				select_Panel.add(allDynamicIdentifiers.get(i), c2);
			}
			
			
			//Add an extra checkBox for the option of not using any Column as dynamic identifier
			checkboxNoIdentifier = new JCheckBox();		//add checkBox		
			checkboxNoIdentifier.setText("NoIdentifier");	
			checkboxNoIdentifier.setRolloverIcon(UIManager.getIcon("CheckBox.icon"));
			checkboxNoIdentifier.setSelectedIcon(IconHandle.get_scaledImageIcon(15, 15, "icon_check.png"));
			checkboxNoIdentifier.setToolTipText("No column will be used as dynamic identifier");		//set toolTip
			
			// add the checkBox to the Panel
			c2.gridx = 0;
			c2.gridy = 0;
			c2.weightx = 1;
			c2.weighty = 1;
			select_Panel.add(checkboxNoIdentifier, c2);
			
			// Add listeners to de-select all other checkBoxes
			checkboxNoIdentifier.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					if (checkboxNoIdentifier.isSelected()) {
						for (int i = 0; i < yield_tables_column_names.length; i++) {
							allDynamicIdentifiers.get(i).setSelected(false);
							allDynamicIdentifiers_ScrollPane.get(i).setVisible(false);		//Set invisible all scrollPanes of dynamic identifiers
							selectIdentifiersScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Dynamic Identifiers  -  use yield attributes to filter variables", TitledBorder.CENTER, 0));
							
							//Do a resize to same size for JInteral Frame of the project to help repaint	
							revalidate();
							repaint();
							Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());	
						} 
					}
				}
			});								
			
	
			
			
					
			
		
			//Add all dynamic identifiers labels
			allDynamicIdentifiers_ScrollPane = new ArrayList<JScrollPane>();
			
			for (int i = 0; i < yield_tables_column_names.length; i++) {
				String YTcolumnName = allDynamicIdentifiers.get(i).getText();		
				allDynamicIdentifiers_ScrollPane.add(new JScrollPane());			//Add ScrollPane
				allDynamicIdentifiers_ScrollPane.get(i).setBorder(new TitledBorder(YTcolumnName));	//set Title
				allDynamicIdentifiers_ScrollPane.get(i).setPreferredSize(new Dimension(200, 0));
//				allDynamicIdentifiers_ScrollPane.get(i).setToolTipText(read_Identifiers.get_ParameterToolTip(YTcolumnName) + " (Column index: " + i + ")");		//add toolTip										
				allDynamicIdentifiers_ScrollPane.get(i).setVisible(false);		//Set invisible
				
				c3.gridx =1 + i;
				c3.gridy = 0;
				dynamic_identifiersPanel.add(allDynamicIdentifiers_ScrollPane.get(i), c3);
			}		
			allDynamicIdentifiers_JPanel = new JPanel[yield_tables_column_names.length];
						
			
			// add listeners to select all or de-select all
			for (int i = 0; i < allDynamicIdentifiers_ScrollPane.size(); i ++) {
				int curent_index = i;
				allDynamicIdentifiers_ScrollPane.get(curent_index).addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						check_or_uncheck_all(checkboxDynamicIdentifiers.get(curent_index));
					}
				});
			}
			
			
			// Add listeners to checkBoxes
			for (int i = 0; i < yield_tables_column_names.length; i++) {
				String currentCheckBoxName = yield_tables_column_names[i];
				int currentCheckBoxIndex = i;
				
				allDynamicIdentifiers.get(i).addActionListener(new ActionListener() {	
					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						// A popupPanel to define identifier if the checkBox is selected
						if (allDynamicIdentifiers.get(currentCheckBoxIndex).isSelected()) {
							
							//Remove all checkBoxes previously added into the Column list (or the dynamic identifier)
							checkboxDynamicIdentifiers.get(currentCheckBoxIndex).clear();
									
							//define popupPanel
							JPanel popupPanel = new JPanel();
							popupPanel.setLayout(new GridBagLayout());
							TitledBorder border_popup = new TitledBorder("DEFINE THIS IDENTIFIER");
							border_popup.setTitleJustification(TitledBorder.CENTER);
							popupPanel.setBorder(border_popup);
							popupPanel.setPreferredSize(new Dimension(800, 400));
							GridBagConstraints c_popup = new GridBagConstraints();
							c_popup.fill = GridBagConstraints.BOTH;
							c_popup.weightx = 1;
							c_popup.weighty = 1;
							
							// Just to make the OptionPanel resizable
							popupPanel.addHierarchyListener(new HierarchyListener() {
							    public void hierarchyChanged(HierarchyEvent e) {
							        Window window = SwingUtilities.getWindowAncestor(popupPanel);
							        if (window instanceof Dialog) {
							            Dialog dialog = (Dialog)window;
							            if (!dialog.isResizable()) {
							                dialog.setResizable(true);
							            }
							        }
							    }
							});												
							//---------------------------------------------------------------------------------------------------	
							List<String> unique_values_list = read_Database.get_col_unique_values_list(currentCheckBoxIndex);									
							JList<String> jList = new JList<>(new Vector<>(unique_values_list));	// Convert unique values to a Vector
							JScrollPane uniqueValueList_ScrollPanel = new JScrollPane(jList);		// ScrollPane contains the vector
							TitledBorder border_List = new TitledBorder("List of unique values");
							border_List.setTitleJustification(TitledBorder.CENTER);
							uniqueValueList_ScrollPanel.setBorder(border_List);
							uniqueValueList_ScrollPanel.setPreferredSize(new Dimension(80, 300));
							//---------------------------------------------------------------------------------------------------
							
							//JTextArea contains some info of this column
							JTextArea columnInfo_TArea = new JTextArea();
							columnInfo_TArea.setEditable(false);
							columnInfo_TArea.setLineWrap(true);
							columnInfo_TArea.setWrapStyleWord(true);
							columnInfo_TArea.append("PRISM found " + unique_values_list.size() + 
									" unique values for this identifier (across " + yield_tables_values.length + " prescriptions in your database)."  + "\n");
							
							if (unique_values_list.size() <= 300) {
								columnInfo_TArea.append("'DISCRETE IDENTIFIER' is recommended.");
							} else {
								columnInfo_TArea.append("'DISCRETE IDENTIFIER' is disable when exceeding 100 unique values. 'RANGE IDENTIFIER' is recommended.");
							}
							//---------------------------------------------------------------------------------------------------

							//defineScrollPane for Definition
							defineScrollPane = new JScrollPane();	
							defineScrollPane.setBorder(new TitledBorder(""));
							//---------------------------------------------------------------------------------------------------
						
							//2 radioButtons for DISCRETE or RANGE definition
							JRadioButton radioDISCRETE = new JRadioButton("DISCRETE IDENTIFIER"); 
							JRadioButton radioRANGE = new JRadioButton("RANGE IDENTIFIER"); 
							if (unique_values_list.size() > 300) radioDISCRETE.setEnabled(false);
							
							//Add 2 radio to the group
							ButtonGroup definitionGroup = new ButtonGroup();
							definitionGroup.add(radioDISCRETE);
							definitionGroup.add(radioRANGE);
							
							
							//Add listener for radioDISCRETE
							radioDISCRETE.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if (radioDISCRETE.isSelected()) {
										//Remove all checkBoxes previously added into the Column list (or the dynamic identifier)
										checkboxDynamicIdentifiers.get(currentCheckBoxIndex).clear();
										
										
										JPanel discretePanel = new JPanel();	
										discretePanel.setLayout(new GridBagLayout());
										GridBagConstraints c_dP = new GridBagConstraints();
										c_dP.fill = GridBagConstraints.HORIZONTAL;
										c_dP.weightx = 1;
										c_dP.weighty = 1;
										
										//Add 2 labels
										c_dP.gridx = 0;
										c_dP.gridy = 0;
										discretePanel.add(new JLabel("Unique Value"), c_dP);
										
										c_dP.gridx = 1;
										c_dP.gridy = 0;
										discretePanel.add(new JLabel("Define Name (Below are suggestions from PRISM-Library)"), c_dP);
										
										//Add all discrete values and textField for the toolTip
										for (int j = 0; j < unique_values_list.size(); j++) {
											String nameOfColumnAndUniqueValue = currentCheckBoxName + " " + unique_values_list.get(j);	//The name
																		
											checkboxDynamicIdentifiers.get(currentCheckBoxIndex).add(new JCheckBox(unique_values_list.get(j)));
											checkboxDynamicIdentifiers.get(currentCheckBoxIndex).get(j).setToolTipText(read_Database.get_ParameterToolTip(nameOfColumnAndUniqueValue));	//ToolTip of this Name from Prism Library;
											c_dP.gridx = 0;
											c_dP.gridy = 1 + j;
											discretePanel.add(checkboxDynamicIdentifiers.get(currentCheckBoxIndex).get(j), c_dP);
											
											c_dP.gridx = 1;
											c_dP.gridy = 1 + j;
											JTextField name_TF = new JTextField(20);
											name_TF.setText(read_Database.get_ParameterToolTip(nameOfColumnAndUniqueValue));	//ToolTip of this Name from Prism Library
											discretePanel.add(name_TF, c_dP);
											
											//Add listener for TextField to be toolTip
											int jj=j;
											name_TF.getDocument().addDocumentListener(new DocumentListener() {
												@Override  
												public void changedUpdate(DocumentEvent e) {
													checkboxDynamicIdentifiers.get(currentCheckBoxIndex).get(jj).setToolTipText(name_TF.getText());
												}
												public void removeUpdate(DocumentEvent e) {
													checkboxDynamicIdentifiers.get(currentCheckBoxIndex).get(jj).setToolTipText(name_TF.getText());
												}
												public void insertUpdate(DocumentEvent e) {
													checkboxDynamicIdentifiers.get(currentCheckBoxIndex).get(jj).setToolTipText(name_TF.getText());
												}
											});
										}						
										defineScrollPane.setViewportView(discretePanel);	
									}
								}
							});
							
							
							//Add listener for radioRANGE
							radioRANGE.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if (radioRANGE.isSelected()) {
										//Remove all checkBoxes previously added into the Column list (or the dynamic identifier)
										checkboxDynamicIdentifiers.get(currentCheckBoxIndex).clear();
										
										
										JPanel rangePanel = new JPanel();
										rangePanel.setLayout(new GridBagLayout());
										GridBagConstraints c_dP = new GridBagConstraints();
										c_dP.fill = GridBagConstraints.HORIZONTAL;
										c_dP.weightx = 0;
										c_dP.weighty = 0;
										
										//Add Label and Combo asking for number of ranges
										c_dP.gridx = 0;
										c_dP.gridy = 0;
										rangePanel.add(new JLabel("Number of ranges"), c_dP);
										
										c_dP.gridx = 1;
										c_dP.gridy = 0;
										JComboBox combo = new JComboBox();		
										for (int comboValue = 1; comboValue <= 100; comboValue++) {
											combo.addItem(comboValue);
										}
										combo.setSelectedItem(null);
										rangePanel.add(combo, c_dP);


										//Add Label and TextField asking for min value
										c_dP.gridx = 0;
										c_dP.gridy = 1;
										rangePanel.add(new JLabel("Min value"), c_dP);
										
										c_dP.gridx = 1;
										c_dP.gridy = 1;
										JTextField min_TF = new JTextField(10);															
										min_TF.setText(unique_values_list.get(0));
										rangePanel.add(min_TF, c_dP);
										
										
										//Add Label and TextField asking for max value
										c_dP.gridx = 0;
										c_dP.gridy = 2;
										rangePanel.add(new JLabel("Max value"), c_dP);
										
										c_dP.gridx = 1;
										c_dP.gridy = 2;
										JTextField max_TF = new JTextField(10);															
										max_TF.setText(unique_values_list.get(unique_values_list.size() - 1));
										rangePanel.add(max_TF, c_dP);
										
										
										//add empty label to prevent things move
										c_dP.gridx = 0;
										c_dP.gridy = 3;
										c_dP.weightx = 0;
										c_dP.weighty = 1;
										rangePanel.add(new JLabel(""), c_dP);	
										

										
										//Listener for the combo
									    combo.addActionListener(new AbstractAction() {
									        @Override
									        public void actionPerformed(ActionEvent e) {
									        	try {																        													        	
										        	int numberofRanges = (Integer) combo.getSelectedItem();	        	
										        	double minValue = Double.parseDouble(min_TF.getText());
										        	double maxValue = Double.parseDouble(max_TF.getText());
									        
										        	if (minValue <= maxValue) {
											        	//Remove all checkBoxes previously added into the Column list (or the dynamic identifier)
														checkboxDynamicIdentifiers.get(currentCheckBoxIndex).clear();
														rangePanel.removeAll();
																																			
											        	
											        	c_dP.weightx = 1;
														c_dP.weighty = 0;
											        	
											        	//Add Label and Spinner asking for number of ranges
														c_dP.gridx = 0;
														c_dP.gridy = 0;
														rangePanel.add(new JLabel("Number of ranges"), c_dP);
														
														c_dP.gridx = 1;
														c_dP.gridy = 0;
														rangePanel.add(combo, c_dP);

														//Add 4 labels
														c_dP.gridx = 0;
														c_dP.gridy = 1;
														rangePanel.add(new JLabel("Range"), c_dP);
														
														c_dP.gridx = 1;
														c_dP.gridy = 1;
														rangePanel.add(new JLabel("From"), c_dP);
														
														c_dP.gridx = 2;
														c_dP.gridy = 1;
														rangePanel.add(new JLabel("To"), c_dP);
														
														c_dP.gridx = 3;
														c_dP.gridy = 1;
														rangePanel.add(new JLabel("Define name of this range"), c_dP);	
											        
													
											        	//Add all ranges and textField for the toolTip
														for (int j = 0; j < numberofRanges; j++) {																									
															String valueFrom = String.format("%.2f", minValue + (maxValue - minValue) / numberofRanges * j);
															String valueTo = String.format("%.2f", minValue + (maxValue - minValue) / numberofRanges * (j + 1));
															
															checkboxDynamicIdentifiers.get(currentCheckBoxIndex).add(new JCheckBox("[" + valueFrom + "," + valueTo + ")"));
															c_dP.gridx = 0;
															c_dP.gridy = 2 + j;
															rangePanel.add(checkboxDynamicIdentifiers.get(currentCheckBoxIndex).get(j), c_dP);
													
															c_dP.gridx = 1;
															c_dP.gridy = 2 + j;
															JTextField from_TF = new JTextField(3);
															
															from_TF.setText(valueFrom);
															rangePanel.add(from_TF, c_dP);
															
															c_dP.gridx = 2;
															c_dP.gridy = 2 + j;
															JTextField to_TF = new JTextField(3);
															to_TF.setText(valueTo);
															rangePanel.add(to_TF, c_dP);
															
															c_dP.gridx = 3;
															c_dP.gridy = 2 + j;
															JTextField name_TF = new JTextField(20);
															name_TF.setText("");	//ToolTip text = ""
															rangePanel.add(name_TF, c_dP);
															
															//Add listener for TextField to be toolTip
															int jj=j;
															name_TF.getDocument().addDocumentListener(new DocumentListener() {
																@Override  
																public void changedUpdate(DocumentEvent e) {
																	checkboxDynamicIdentifiers.get(currentCheckBoxIndex).get(jj).setToolTipText(name_TF.getText());
																}
																public void removeUpdate(DocumentEvent e) {
																	checkboxDynamicIdentifiers.get(currentCheckBoxIndex).get(jj).setToolTipText(name_TF.getText());
																}
																public void insertUpdate(DocumentEvent e) {
																	checkboxDynamicIdentifiers.get(currentCheckBoxIndex).get(jj).setToolTipText(name_TF.getText());
																}
															});
														}
														
														
														c_dP.gridx = 4;
														c_dP.gridy = 2 + numberofRanges;
														c_dP.weightx = 1;
														c_dP.weighty = 1;
														rangePanel.add(new JLabel(""), c_dP);  //add empty label to make everything not move
											        	
														
											        	// Apply change to the GUI
														defineScrollPane.setViewportView(rangePanel);
										        	} else {
										        		JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(), "'Min value' must be less than or equal to 'Max value'");														        																							
										        	}
												} catch (NumberFormatException ex) {
													JOptionPane.showMessageDialog(Prism2Main.get_Prism_DesktopPane(),
															"'Min value' and 'Max value' must be numbers");
												}
											}
									    });

										defineScrollPane.setViewportView(rangePanel);
									}
								}
							});
							//---------------------------------------------------------------------------------------------------
					
							//Add all to the popupPanel												
							
							//Add columnInfo_TArea to popupPanel
							c_popup.gridx = 0;
							c_popup.gridy = 0;
							c_popup.gridwidth = 3;
							c_popup.gridheight = 1;
							c_popup.weightx = 1;
							c_popup.weighty = 0;
							popupPanel.add(columnInfo_TArea, c_popup);
																
							//Add uniqueValueList_ScrollPanel to popupPanel
							c_popup.gridx = 0;
							c_popup.gridy = 1;
							c_popup.gridwidth = 1;
							c_popup.gridheight = 2;
							c_popup.weightx = 1;
							c_popup.weighty = 1;
							popupPanel.add(uniqueValueList_ScrollPanel, c_popup);
							
							//Add 2 radios to popupPanel
							c_popup.gridx = 1;
							c_popup.gridy = 1;
							c_popup.gridwidth = 1;
							c_popup.gridheight = 1;
							c_popup.weightx = 1;
							c_popup.weighty = 0;
							popupPanel.add(radioDISCRETE, c_popup);
							
							c_popup.gridx = 2;
							c_popup.gridy = 1;
							c_popup.gridwidth = 1;
							c_popup.gridheight = 1;
							c_popup.weightx = 1;
							c_popup.weighty = 0;
							popupPanel.add(radioRANGE, c_popup);
							
							//Add defineScrollPane to popupPanel
							c_popup.gridx = 1;
							c_popup.gridy = 2;
							c_popup.gridwidth = 2;
							c_popup.gridheight = 1;
							c_popup.weightx = 1;
							c_popup.weighty = 1;
							popupPanel.add(defineScrollPane, c_popup);
							//---------------------------------------------------------------------------------------------------
							
							int response = JOptionPane.showConfirmDialog(Prism2Main.get_Prism_DesktopPane(), popupPanel,
									"Add   '" + currentCheckBoxName + "'   to the set of dynamic identifiers ?", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null);
							if (response == JOptionPane.NO_OPTION) {
								allDynamicIdentifiers.get(currentCheckBoxIndex).setSelected(false);
							} else if (response == JOptionPane.YES_OPTION && checkboxDynamicIdentifiers.get(currentCheckBoxIndex).size() > 0) {
								//Deselect the No Identifier checkBox
								checkboxNoIdentifier.setSelected(false);
								
								//Set the identifier ScrollPane visible
								allDynamicIdentifiers_ScrollPane.get(currentCheckBoxIndex).setVisible(true);
								
								
								//create a temporary Panel contains all checkboxes of that column 
								JPanel tempPanel = new JPanel();
								tempPanel.setLayout(new GridBagLayout());
								GridBagConstraints c_temp = new GridBagConstraints();
								c_temp.fill = GridBagConstraints.HORIZONTAL;
								c_temp.weightx = 1;
								c_temp.weighty = 1;
								
								for (int j = 0; j < checkboxDynamicIdentifiers.get(currentCheckBoxIndex).size(); j++) {
									c_temp.gridx = 1;
									c_temp.gridy = j;
									tempPanel.add(checkboxDynamicIdentifiers.get(currentCheckBoxIndex).get(j), c_temp);
								}

								// Set Scroll Pane view to the tempPanel
								allDynamicIdentifiers_JPanel[currentCheckBoxIndex] = tempPanel;
								allDynamicIdentifiers_ScrollPane.get(currentCheckBoxIndex).setViewportView(tempPanel);
								selectIdentifiersScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Dynamic Identifiers", TitledBorder.CENTER, 0));
								
								// Re draw the super scroll pane to make new identifiers show up (especially for cost adjustment set - add) 
								validate();
								repaint();
								
							} else if (response == JOptionPane.CLOSED_OPTION) {
								allDynamicIdentifiers.get(currentCheckBoxIndex).setSelected(false);
							} else {
								allDynamicIdentifiers.get(currentCheckBoxIndex).setSelected(false);
							}
						
						} else {	//if checkbox is not selected then remove the identifier ScrollPane
							allDynamicIdentifiers_ScrollPane.get(currentCheckBoxIndex).setVisible(false);
						}
					
						//Do a resize to same size for JInteral Frame of the project to help repaint the identifier ScrollPane added or removed	
						revalidate();
						repaint();
						Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
					}
				});
			}
						
			//Do a resize to same size for JInteral Frame of the project to help repaint the checkboxes added		
			revalidate();
			repaint();
			Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());	
		}
		

		

		// ScrollPane contains the identifiers that are able to be selected
		selectIdentifiersScrollPanel = new JScrollPane(select_Panel);
		selectIdentifiersScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Dynamic Identifiers  -  use yield attributes to filter variables", TitledBorder.CENTER, 0));
		selectIdentifiersScrollPanel.setPreferredSize(new Dimension(200, 0));
		
		// add listeners to select NoIdentifier only
		selectIdentifiersScrollPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkboxNoIdentifier.setSelected(true);
				for (int i = 0; i < yield_tables_column_names.length; i++) {
					allDynamicIdentifiers.get(i).setSelected(false);
					allDynamicIdentifiers_ScrollPane.get(i).setVisible(false);		// Set invisible all scrollPanes of dynamic identifiers
					selectIdentifiersScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Dynamic Identifiers  -  use yield attributes to filter variables", TitledBorder.CENTER, 0));
				} 
				// Do a resize to same size for JInteral Frame of the project to help repaint	
				revalidate();
				repaint();
				Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());
			}
		});	
		
		// Add the above ScrollPane
		c3.gridx = 0;
		c3.gridy = 0;
		dynamic_identifiersPanel.add(selectIdentifiersScrollPanel, c3);
		
		
		// Add dynamic_identifiersPanel to this Class which is a mother JSCrollPanel
		setViewportView(dynamic_identifiersPanel);		
		
		
		this.setBorder(null);
		this.setPreferredSize(new Dimension(0, 250));	
	}
	
	public List<List<JCheckBox>> get_checkboxDynamicIdentifiers() {
		return checkboxDynamicIdentifiers;
	}
	
	public List<JCheckBox> get_allDynamicIdentifiers() {
		return allDynamicIdentifiers;
	}
	
	public JCheckBox get_checkboxNoIdentifier() {
		return checkboxNoIdentifier;
	}
	
	public List<JScrollPane> get_allDynamicIdentifiers_ScrollPane() {
		return allDynamicIdentifiers_ScrollPane;
	}
	
	public void check_or_uncheck_all(List<JCheckBox> one_identifier_checkboxes) {
		boolean is_atleast_one_item_checked = false;
		for (JCheckBox item: one_identifier_checkboxes) {
			if (item.isSelected()) is_atleast_one_item_checked = true;
		}
		
		if (is_atleast_one_item_checked) {
			for (JCheckBox item: one_identifier_checkboxes) {
				item.setSelected(false);
			}
		} else {
			for (JCheckBox item: one_identifier_checkboxes) {
				item.setSelected(true);
			}
		}
	}
	
	
	public String get_dynamic_description_from_GUI() {		
		List<String> temp = new ArrayList<String>();
		
		for (int i = 0; i < allDynamicIdentifiers_ScrollPane.size(); i++) {		//Loop all dynamic identifier ScrollPanes
			// Count the total of checked items in each identifier 
			int total_check_items = 0;
			int total_items = 0;
			if (allDynamicIdentifiers_ScrollPane.get(i).isVisible() && checkboxDynamicIdentifiers.get(i).size() > 0) {	// get the active identifiers (when identifier ScrollPane is visible and List size >0)
				for (int j = 0; j < checkboxDynamicIdentifiers.get(i).size(); j++) { //Loop all checkBoxes in this active identifier
					// Add checkBox if it is selected & visible & enabled
					if (checkboxDynamicIdentifiers.get(i).get(j).isSelected() && checkboxDynamicIdentifiers.get(i).get(j).isVisible() && checkboxDynamicIdentifiers.get(i).get(j).isEnabled()) {
						total_check_items++;
					}
					if (checkboxDynamicIdentifiers.get(i).get(j).isVisible() && checkboxDynamicIdentifiers.get(i).get(j).isEnabled()) {
						total_items++;
					}
				}
			}
			
			// Add to description only when the total of checked items < the total items
			if (total_check_items < total_items) {
				String dynamic_info = allDynamicIdentifiers.get(i).getText();
				
				for (int j = 0; j < checkboxDynamicIdentifiers.get(i).size(); j++) { 	// Loop all checkBoxes in this active identifier
					String checkboxName = checkboxDynamicIdentifiers.get(i).get(j).getText();									
					// Add checkBox if it is selected & visible & enabled
					if (checkboxDynamicIdentifiers.get(i).get(j).isSelected() && checkboxDynamicIdentifiers.get(i).get(j).isVisible() && checkboxDynamicIdentifiers.get(i).get(j).isEnabled()) {	
						dynamic_info = String.join(" ", dynamic_info, checkboxName);	
					}
				}
				temp.add(dynamic_info);
			}
		}	
		
		if (temp.isEmpty()) {
			return "No dynamic restriction";
		} else {
			String joined_string = String.join(" | ", temp);
			return joined_string;
		}
	}
	
	
	public String get_dynamic_info_from_GUI() {			
		String dynamic_info = "";
		for (int i = 0; i < allDynamicIdentifiers_ScrollPane.size(); i++) {		//Loop all dynamic identifier ScrollPanes
			if (allDynamicIdentifiers_ScrollPane.get(i).isVisible() &&
					checkboxDynamicIdentifiers.get(i).size() > 0) {			//get the active identifiers (when identifier ScrollPane is visible and List size >0)
				dynamic_info = dynamic_info + i + " ";
				for (int j = 0; j < checkboxDynamicIdentifiers.get(i).size(); j++) { //Loop all checkBoxes in this active identifier
					String checkboxName = checkboxDynamicIdentifiers.get(i).get(j).getText();									
					// Add checkBox if it is selected & visible & enabled
					if (checkboxDynamicIdentifiers.get(i).get(j).isSelected() && checkboxDynamicIdentifiers.get(i).get(j).isVisible() && checkboxDynamicIdentifiers.get(i).get(j).isEnabled()) {
						dynamic_info = dynamic_info + checkboxName + " ";
					}
				}
				
				if (!dynamic_info.equals("")) {
					dynamic_info = dynamic_info.substring(0, dynamic_info.length() - 1) + ";";		// remove the last space, and add ;
				}
			}
		}	
		
		if (dynamic_info.equals("") || checkboxNoIdentifier.isSelected()) {
			dynamic_info = "NoIdentifier";			//= checkboxNoIdentifier.getText();
		} else {
			dynamic_info = dynamic_info.substring(0, dynamic_info.length() - 1);		// remove the last ;
		}
		
		return dynamic_info;
	}

	
	public String get_original_dynamic_info_from_GUI() {			
		String original_dynamic_info = "";
		for (int i = 0; i < allDynamicIdentifiers_ScrollPane.size(); i++) {		//Loop all dynamic identifier ScrollPanes
			if (allDynamicIdentifiers_ScrollPane.get(i).isVisible() &&
					checkboxDynamicIdentifiers.get(i).size() > 0) {			//get the active identifiers (when identifier ScrollPane is visible and List size >0)
				original_dynamic_info = original_dynamic_info + i + " ";
				for (int j = 0; j < checkboxDynamicIdentifiers.get(i).size(); j++) { //Loop all checkBoxes in this active identifier
					String checkboxName = checkboxDynamicIdentifiers.get(i).get(j).getText();	
//					String checkboxToolTip = checkboxDynamicIdentifiers.get(i).get(j).getToolTipText();		// Add ToolTip later
					// Add checkBox if it is selected & visible & enabled
					if (checkboxDynamicIdentifiers.get(i).get(j).isVisible() && checkboxDynamicIdentifiers.get(i).get(j).isEnabled()) {
						original_dynamic_info = original_dynamic_info + checkboxName + " ";
					}
				}
				
				if (!original_dynamic_info.equals("")) {
					original_dynamic_info = original_dynamic_info.substring(0, original_dynamic_info.length() - 1) + ";";		// remove the last space, and add ;
				}
			}
		}	
		
		if (original_dynamic_info.equals("") || checkboxNoIdentifier.isSelected()) {
			original_dynamic_info = "NoIdentifier";			//= checkboxNoIdentifier.getText();
		} else {
			original_dynamic_info = original_dynamic_info.substring(0, original_dynamic_info.length() - 1);		// remove the last ;
		}
		
		return original_dynamic_info;
	}
	
	
	public void reload_this_constraint_dynamic_identifiers(String dynamic_identifiers_info, String original_dynamic_identifiers_info) {	
		// Note: 
		// 1. dynamic_identifiers_info: contains all the selected dynamic identifiers
		// 2. original_dynamic_identifiers_info: contains all the original dynamic identifiers (regardless of being selected or not)
		
		checkboxNoIdentifier.setSelected(false);
		List<Integer> visible_scrollpane_id = new ArrayList<Integer>();	// instead of the below (invisible all then visible), use this list to setVisible later only when needed to avoid blinking when running NOSQL calculation on output_05
//		for (JScrollPane i: allDynamicIdentifiers_ScrollPane) {	// Hide all dynamic identifier ScrollPanes
//			i.setVisible(false);
//		}
		for (List<JCheckBox> i: checkboxDynamicIdentifiers) {	// Remove all checkBoxes
			i.clear();
		}
		
		
		if (original_dynamic_identifiers_info.equals("NoIdentifier")) {
			checkboxNoIdentifier.setSelected(true);
			selectIdentifiersScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Dynamic Identifiers  -  use yield attributes to filter variables", TitledBorder.CENTER, 0));
		} else {
			// Read the whole cell into array
			String[] info = dynamic_identifiers_info.split(";");
			String[] original_info = original_dynamic_identifiers_info.split(";");
			int total_dynamic_identifiers = original_info.length;		// Same for those above 2
			
			// Get all dynamic Identifiers to be in the list
			for (int i = 0; i < total_dynamic_identifiers; i++) {	
				String[] identifier_elements = info[i].split("\\s+");	// Space delimited
				String[] original_identifier_elements = original_info[i].split("\\s+");	// Space delimited
				int current_identifier_id = Integer.valueOf(original_identifier_elements[0]);		// Same for those above 2
				visible_scrollpane_id.add(current_identifier_id);
				
				
				// Create check box for each attribute of this identifier			
				for (int j = 1; j < original_identifier_elements.length; j++) {		//Ignore the first element which is the identifier id
					String this_original_identifier_attribute = original_identifier_elements[j].replaceAll("\\s+","");		//Add element name, if name has spaces then remove all the spaces
					checkboxDynamicIdentifiers.get(current_identifier_id).add(new JCheckBox(this_original_identifier_attribute));
				}	
							
				// Select the check box if it is selected in the column 10 - dynamic_identifiers			
				for (int j = 1; j < identifier_elements.length; j++) {		//Ignore the first element which is the identifier id
					String this_identifier_attribute = identifier_elements[j].replaceAll("\\s+","");		//Add element name, if name has spaces then remove all the spaces				
					for (JCheckBox k: checkboxDynamicIdentifiers.get(current_identifier_id)) {
						if (k.getText().equals(this_identifier_attribute)) {
							k.setSelected(true);
						}
					}
				}
								
				// Create a temporary panel to hold all check boxes
				JPanel tempPanel = new JPanel();
				tempPanel.setLayout(new GridBagLayout());
				GridBagConstraints c_temp = new GridBagConstraints();
				c_temp.fill = GridBagConstraints.HORIZONTAL;
				c_temp.weightx = 1;
				c_temp.weighty = 1;
				
				for (int j = 0; j < checkboxDynamicIdentifiers.get(current_identifier_id).size(); j++) {
					c_temp.gridx = 1;
					c_temp.gridy = j;
					tempPanel.add(checkboxDynamicIdentifiers.get(current_identifier_id).get(j), c_temp);
				}	
				
				allDynamicIdentifiers_JPanel[current_identifier_id] = tempPanel;
				allDynamicIdentifiers_ScrollPane.get(current_identifier_id).setViewportView(tempPanel);	// Set Scroll Pane view to the tempPanel
//				allDynamicIdentifiers_ScrollPane.get(current_identifier_id).setVisible(true);
				selectIdentifiersScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Dynamic Identifiers", TitledBorder.CENTER, 0));
			}	
		}
		
		
		// Hide all dynamic identifier ScrollPanes, except the ones that are currently captured by the row
		for (int i = 0; i < allDynamicIdentifiers_ScrollPane.size(); i++) {
			if (visible_scrollpane_id.contains(i)) {
				if (!allDynamicIdentifiers_ScrollPane.get(i).isVisible()) allDynamicIdentifiers_ScrollPane.get(i).setVisible(true);	// make visible only when needed
			} else {
				allDynamicIdentifiers_ScrollPane.get(i).setVisible(false);
			}
		}
		
		
		// Reload the selection in the 'Select Identifiers" scroll pane
		for (int i = 0; i < allDynamicIdentifiers.size(); i++) {
			allDynamicIdentifiers.get(i).setSelected(false);
			if (allDynamicIdentifiers_ScrollPane.get(i).isVisible()) {
				allDynamicIdentifiers.get(i).setSelected(true);
			}
			// Do a resize to same size for JInteral Frame of the project to help repaint	
			revalidate();
			repaint();
			Prism2Main.get_Prism_DesktopPane().getSelectedFrame().setSize(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getSize());	
		}	
	}
	
	public void highlight() {
		select_Panel.setBackground(ColorUtil.makeTransparent(new Color(240, 255, 255), 255));	
		for (JPanel i : allDynamicIdentifiers_JPanel) {
			if (i != null) i.setBackground(ColorUtil.makeTransparent(new Color(240, 255, 255), 255));
		}
		revalidate();
		repaint();
	}
	
	public void unhighlight() {			
		select_Panel.setBackground(null);
		for (JPanel i : allDynamicIdentifiers_JPanel) {
			if (i != null) i.setBackground(null);
		}
		revalidate();
		repaint();
	}
}
