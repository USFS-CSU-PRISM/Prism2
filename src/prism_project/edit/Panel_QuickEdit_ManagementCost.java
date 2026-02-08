/*******************************************************************************
 * Copyright (C) 2016-2018 PRISM Development Team
 * * PRISM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * * PRISM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * * You should have received a copy of the GNU General Public License
 * along with PRISM.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package prism_project.edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import prism_convenience.IconHandle;
import prism_convenience.PrismGridBagLayoutHandle;
import prism_convenience.PrismTableModel;
import prism_convenience.TableColumnsHandle;
import prism_project.data_process.Read_Database;
import prism_root.Prism2Main;

public class Panel_QuickEdit_ManagementCost extends JPanel {
	private JTable table7b, editorTable;
	private Object[][] data7b;
	private DefaultTableCellRenderer render7b;
	
	private JButton btn_compact;
	private JLabel view_label;
	private JButton btnApplyActivityCost;
	private JButton btnApplyConversionCost;
	private Prism_ShowHideColumnsButtons btnApplyShowHide;
	
	public Panel_QuickEdit_ManagementCost(Read_Database read_database, JTable table7a, Object[][] data7a, String[] columnNames7a, JTable table7b, Object[][] data7b, JTable editorTable) {
		this.table7b = table7b;
		this.data7b = data7b;
		this.render7b = (DefaultTableCellRenderer) table7b.getColumnModel().getColumn(0).getCellRenderer();
		this.editorTable = editorTable;
		
		
		setLayout(new GridBagLayout());
		
		
		
		
		
		JPanel qd1 = new JPanel();
		qd1.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
				
		
		// Add Button-------------------------------------------------------------------------------------------------
		btnApplyShowHide = new Prism_ShowHideColumnsButtons(read_database, table7a, data7a, columnNames7a);
		btnApplyShowHide.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnApplyShowHide.setHorizontalTextPosition(SwingConstants.CENTER);
		btnApplyShowHide.setToolTipText("show/hide yield tables columns");
		btnApplyShowHide.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_binoculars.png"));
		btnApplyShowHide.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_binoculars.png"));
		btnApplyShowHide.setContentAreaFilled(false);
		
		qd1.add(btnApplyShowHide, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				0, 1, 1, 1, 0, 1, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
		
		// Add Label-------------------------------------------------------------------------------------------------
		qd1.add(new JLabel("Activity cost"), PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
				
		
		// Add formatedTextfield
		JFormattedTextField formatedTextfield = new JFormattedTextField();
		formatedTextfield.setColumns(8);
		formatedTextfield.setToolTipText("greater than or equal to zero");
		formatedTextfield.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				Runnable format = new Runnable() {
					@Override
					public void run() {
						String text = formatedTextfield.getText();
//						if (!text.matches("\\d*(\\.\\d{0,2})?")) {		//	used regex: \\d*(\\.\\d{0,2})? because two decimal places is enough
						if (!text.matches("\\d*(\\.\\d{0,})?")) {		//	no restriction on number of digits after the dot
							formatedTextfield.setText(text.substring(0, text.length() - 1));
						} else {
							if (!text.isEmpty() && !text.equals(".") && Double.valueOf(text) < (double) 0) {		// If the added String make value <0 then delete that String
								formatedTextfield.setText(text.substring(0, text.length() - 1));
							}
						}	
					}
				};
				SwingUtilities.invokeLater(format);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {

			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		qd1.add(formatedTextfield, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
				
		
		// Add button apply
		btnApplyActivityCost = new JButton();
		btnApplyActivityCost.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnApplyActivityCost.setHorizontalTextPosition(SwingConstants.CENTER);
		btnApplyActivityCost.setToolTipText("make changes for all highlighted cells, except cells in the first column");
		btnApplyActivityCost.setIcon(IconHandle.get_scaledImageIcon(20, 20, "icon_split.png"));
		btnApplyActivityCost.setRolloverIcon(IconHandle.get_scaledImageIcon(30, 30, "icon_split.png"));
		btnApplyActivityCost.setContentAreaFilled(false);
		btnApplyActivityCost.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Get selected rows
				int[] selectedRow = table7a.getSelectedRows();
				int[] selectedCol = table7a.getSelectedColumns();
				
				if (selectedRow.length == 0) return;
							
				// Convert row index because "Sort" causes problems
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = table7a.convertRowIndexToModel(selectedRow[i]);
				}
				// Convert col index because "Sort" causes problems
				for (int j = 0; j < selectedCol.length; j++) {
					selectedCol[j] = table7a.convertColumnIndexToModel(selectedCol[j]);
				}
				
				for (int i : selectedRow) {
					for (int j : selectedCol) {
						if (!formatedTextfield.getText().equals(".") && j != 0) {	// Only apply the changes to selected cells in columns > 0 (from 'acres' column)
							data7a[i][j] = (formatedTextfield.getText().isEmpty())? null : Double.valueOf(formatedTextfield.getText());
						}
					}
				}
				
				// just need to add 1 currently selected row (no need to add all because it would trigger a lot of "fireTableDataChanged" in "setValueAt" because of the ListSelectionListener of table7a)
				// also need re-validate and repaint so all the new data would show up after the change is triggered by the "addRowSelectionInterval"
				table7a.removeRowSelectionInterval(table7a.convertRowIndexToView(selectedRow[0]), table7a.convertRowIndexToView(selectedRow[0]));	// only trigger the data change once by remove then add 1 time
				table7a.addRowSelectionInterval(table7a.convertRowIndexToView(selectedRow[0]), table7a.convertRowIndexToView(selectedRow[0]));
				table7a.revalidate();
				table7a.repaint();
			}
		});
		qd1.add(btnApplyActivityCost, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 0, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
		
		

		
		JPanel qd2 = new JPanel();
		qd2.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
			
		
		// Add Label-------------------------------------------------------------------------------------------------
		qd2.add(new JLabel("Conversion cost"), PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right

		
		// Add formatedTextfield
		JFormattedTextField formatedTextfield_2 = new JFormattedTextField();
		formatedTextfield_2.setColumns(8);
		formatedTextfield_2.setToolTipText("greater than or equal to zero");
		formatedTextfield_2.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				Runnable format = new Runnable() {
					@Override
					public void run() {
						String text = formatedTextfield_2.getText();
//						if (!text.matches("\\d*(\\.\\d{0,2})?")) {		//	used regex: \\d*(\\.\\d{0,2})? because two decimal places is enough
						if (!text.matches("\\d*(\\.\\d{0,})?")) {		//	no restriction on number of digits after the dot
							formatedTextfield_2.setText(text.substring(0, text.length() - 1));
						} else {
							if (!text.isEmpty() && !text.equals(".") && Double.valueOf(text) < (double) 0) {		// If the added String make value <0 then delete that String
								formatedTextfield_2.setText(text.substring(0, text.length() - 1));
							}
						}	
					}
				};
				SwingUtilities.invokeLater(format);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {

			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		qd2.add(formatedTextfield_2, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
				
		// Add button apply
		btnApplyConversionCost = new JButton();
		btnApplyConversionCost.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnApplyConversionCost.setHorizontalTextPosition(SwingConstants.CENTER);
		btnApplyConversionCost.setToolTipText("make changes for all highlighted cells, except cells in the first two columns");
		btnApplyConversionCost.setIcon(IconHandle.get_scaledImageIcon(20, 20, "icon_split.png"));
		btnApplyConversionCost.setRolloverIcon(IconHandle.get_scaledImageIcon(30, 30, "icon_split.png"));
		btnApplyConversionCost.setContentAreaFilled(false);
		btnApplyConversionCost.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Get selected rows
				int[] selectedRow = editorTable.getSelectedRows();
				int[] selectedCol = editorTable.getSelectedColumns();
				
				if (selectedRow.length == 0) return;
							
				// Convert row index because "Sort" causes problems
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = editorTable.convertRowIndexToModel(selectedRow[i]);
				}
				// Convert col index because "Sort" causes problems
				for (int j = 0; j < selectedCol.length; j++) {
					selectedCol[j] = editorTable.convertColumnIndexToModel(selectedCol[j]);
				}
				
				for (int i : selectedRow) {
					for (int j : selectedCol) {
						if (!formatedTextfield_2.getText().equals(".") && j >= 2) {	// Only apply the changes to selected cells in columns >= 2 (all the percentage columns)
							if (!formatedTextfield_2.getText().isEmpty()) editorTable.getModel().setValueAt(Double.valueOf(formatedTextfield_2.getText()), i, j);	// Only apply the changes to selected cells in column 2 "weight", do not allow null
						}
					}
				}
				
				// just need to add 1 currently selected row (no need to add all because it would trigger a lot of "fireTableDataChanged" in "setValueAt" because of the ListSelectionListener of table6a)
				// also need re-validate and repaint so all the new data would show up after the change is triggered by the "addRowSelectionInterval"
				editorTable.removeRowSelectionInterval(editorTable.convertRowIndexToView(selectedRow[0]), editorTable.convertRowIndexToView(selectedRow[0]));	// only trigger the data change once by remove then add 1 time
				editorTable.addRowSelectionInterval(editorTable.convertRowIndexToView(selectedRow[0]), editorTable.convertRowIndexToView(selectedRow[0]));
				editorTable.revalidate();
				editorTable.repaint();
				reset_view_without_changing_label();	// very important to fix the last cell of multiple selected cells is not registered the new value
			}
		});		
		qd2.add(btnApplyConversionCost, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 0, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
		

		// Add Label-------------------------------------------------------------------------------------------------
		view_label = new JLabel("switch view");
		qd2.add(view_label, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				3, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
		// Add button compact view
		btn_compact = new JButton();
		btn_compact.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn_compact.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_compact.setToolTipText("switch to compact view");
		btn_compact.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_script_gray.png"));
		btn_compact.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_script_gray.png"));
		btn_compact.setContentAreaFilled(false);
		btn_compact.addActionListener(e -> {
			switch (btn_compact.getToolTipText()) {
			case "switch to compact view":
				btn_compact.setToolTipText("switch to full view");
				btn_compact.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_script.png"));
				btn_compact.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_script.png"));
				break;
			case "switch to full view":
				btn_compact.setToolTipText("switch to compact view");
				btn_compact.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_script_gray.png"));
				btn_compact.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_script_gray.png"));
				break;
			}
			reset_view_without_changing_label();
		});
		qd2.add(btn_compact, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				3, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right		
		
		
		// Add 2 panels to this big Panel
		add(qd1, PrismGridBagLayoutHandle.get_c(c, "HORIZONTAL", 
				0, 0, 1, 0, 1, 1, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		add(qd2, PrismGridBagLayoutHandle.get_c(c, "HORIZONTAL", 
				1, 0, 1, 0, 1, 1, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
	}

	
	
	private class Prism_ShowHideColumnsButtons extends JButton {
		private JRadioButton[] radioButton;
		
		public Prism_ShowHideColumnsButtons(Read_Database read_database, JTable table7a,  Object[][] data7a, String[] columnNames7a) {
			// Must set this show/hide column method when all columns are still visible------------------------------------------------------
			TableColumnsHandle column_handle = new TableColumnsHandle(table7a);
			
						
			// Create a radio buttons-----------------------------------------------------------------------------
			radioButton = new JRadioButton[2];		
			radioButton[0] = new JRadioButton("Select default columns (acres and harvested volume in cubic feet per acre)");
			radioButton[1] = new JRadioButton("Select active columns (active column has at least one cell with unempty value)");
			
			
			// Create a radio group
			ButtonGroup radioGroup = new ButtonGroup();
			radioGroup.add(radioButton[0]);
			radioGroup.add(radioButton[1]);
				
			
			// Create a radio panel
			JPanel radio_panel = new JPanel();
			radio_panel.setLayout(new GridLayout(0, 1));
			radio_panel.setBorder(BorderFactory.createTitledBorder("Preset columns"));
			radio_panel.add(radioButton[0]);
			radio_panel.add(radioButton[1]);
						
						
			// Create a list of JCheckBox-------------------------------------------------------------------------
			List<JCheckBox> column_checkboxes = new ArrayList<JCheckBox>();		
			for (int i = 0; i < table7a.getColumnModel().getColumnCount(); i++) {
				if (i > 1) {	// ignore columns 0 and 1: action_list & acres
					column_checkboxes.add(new JCheckBox(table7a.getColumnName(i)));
					column_checkboxes.get(i - 2).setSelected(true);		// -2 because we ignore 2 columns
									
					String tip = read_database.get_ParameterToolTip(column_checkboxes.get(i - 2).getText()) + " (Column index: " + (int) (i - 2) + ")";
					column_checkboxes.get(i - 2).setToolTipText(tip);		
					
//					// Disable Parameter check box if unit is not per Acre
//					if (!tip.contains("per Acre")) {
//						column_checkboxes.get(i - 2).setEnabled(false);
//					}
					
					// Disable Parameter check box if the minimum unique value is not a double 
					try {
						Double.parseDouble(read_database.get_col_unique_values_list(i - 2).get(0));
					} catch (NumberFormatException e) {
						column_checkboxes.get(i - 2).setEnabled(false);
					}
				}
			}
			
			
			// Add listener for JCheckBoxes
			for (JCheckBox i: column_checkboxes) {
				i.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						radioGroup.clearSelection();
					}
				});
				
				i.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent changeEvent) {
						if (i.isSelected()) {
							column_handle.setColumnVisible(i.getText(), true);	// show column
						} else {
							column_handle.setColumnVisible(i.getText(), false);	// hide column
						}
					}
				});
			}
			

			// Add JCheckBoxes to check_panel
			JPanel check_panel = new JPanel();
			check_panel.setLayout(new GridLayout(0, 4));
			for (JCheckBox i: column_checkboxes) {
				check_panel.add(i);
			}
			
			
			// Add check_panel to a scroll panel
			JScrollPane scrollPane = new JScrollPane(check_panel);				
			scrollPane.setBorder(BorderFactory.createTitledBorder("Available columns (you should not use columns if the unit is not per acre)"));
			scrollPane.setPreferredSize(new Dimension(600, 350));
			
			
			// Add listeners for radio buttons			
			// Listener 1		
			radioButton[0].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {		//3 columns at start only
					column_handle.setColumnVisible("action_list", true);	// show column acres
					column_handle.setColumnVisible("acres", true);	// show column acres
					for (JCheckBox i: column_checkboxes) {
						i.setSelected(true);		// true then false to activate the ChangeListener
						i.setSelected(false);
						if (i.getText().equalsIgnoreCase("hca_allsx") || i.getText().equalsIgnoreCase("rmcuft")) {
							i.setSelected(true);							
						}
					}						
				}
			});
			radioButton[0].doClick();		// Start with default 3 columns
			
			// Listener 2	
			radioButton[1].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {																	
					List<Integer> active_col_id = new ArrayList<Integer>();		// List of active columns: at least 1 cell <> null			
					for (int i = 0; i < data7a.length; i++) {
						for (int j = 0; j < data7a[i].length; j++) {
							if (data7a[i][j] != null && !active_col_id.contains(j)) {
								active_col_id.add(j);
							}		
						}	
					}
					
					// For only acres column (No check boxes so we have to set visible/invisible manually)
					if (active_col_id.contains(1)) {	// if acres is active column
						column_handle.setColumnVisible(columnNames7a[1], true);	// show column
					} else {
						column_handle.setColumnVisible(columnNames7a[1], false);	// hide column
					}
						
					// For columns > 1 (Have check boxes to we only have to check/uncheck)
					for (int i = 0; i < columnNames7a.length; i++) {						
						if (i > 1) {	// ignore columns 0 and 1: action_list & acres	
							column_checkboxes.get(i - 2).setSelected(false);		// -2 because we ignore 2 columns
							if (active_col_id.contains(i)) {
								column_checkboxes.get(i - 2).setSelected(true);		// -2 because we ignore 2 columns
							}			
						}
					}
				}
			});
			
			
			// Add radioPanel & scrollPane to a panel				
			JPanel combined_panel = new JPanel(new BorderLayout());
			combined_panel.add(radio_panel, BorderLayout.NORTH);
			combined_panel.add(scrollPane, BorderLayout.CENTER);
			
			
			// Listener for this button class------------------------------------------------------------------------------------------------------
			if (column_checkboxes.size() > 2) {
				setEnabled(true);	// Enable only when the yield columns are loaded
			} else {
				setEnabled(false);
			}
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					radioButton[1].doClick();
					String ExitOption[] = { "Ok" };
					int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), combined_panel,
							"Select yield tables columns to show", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
							IconHandle.get_scaledImageIcon(50, 50, "icon_binoculars.png"), ExitOption, ExitOption[0]);

					if (response == 0) {

					}
				}
			});
		}
	}
	
	
	public void disable_all_apply_buttons() {
		btnApplyActivityCost.setEnabled(false);
		btnApplyConversionCost.setEnabled(false);
		btnApplyShowHide.setEnabled(false);
		btn_compact.setEnabled(false);
	}
	
	public void enable_all_apply_buttons() {
		btnApplyActivityCost.setEnabled(true);
		btnApplyConversionCost.setEnabled(true);
		btnApplyShowHide.setEnabled(true);
		btn_compact.setEnabled(true);
		reset_view_without_changing_label();
	}
	
	public void reset_view_without_changing_label() {
		if (table7b.isEditing()) {
			table7b.getCellEditor().cancelCellEditing();
		}
		
		// Prepare View Filters
		List<RowFilter<Object, Object>> filters = new ArrayList<>();
		
		switch (btn_compact.getToolTipText()) {
		case "switch to full view":
			filters.add(new RowFilter<Object, Object>() {
				public boolean include(Entry entry) {
					for (int col = 2; col < data7b[0].length; col++) {	// except the first 2 columns
						if (entry.getValue(col) != null) {
							return true;		// if 1 cell in this row is null then show the row
						}
					}
					return false;	// hide the row when all cells have the value of zero
				}
			});
			
			// Set Color and Alignment for Cells
	        DefaultTableCellRenderer compact_r = new DefaultTableCellRenderer() {
	            @Override
	            public Component getTableCellRendererComponent(JTable table, Object
				value, boolean isSelected, boolean hasFocus, int row, int column) {
					super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					setHorizontalAlignment(JLabel.LEFT);
					setBackground(new Color(160, 160, 160));	// Set cell background color	
					if (isSelected)	setBackground(table.getSelectionBackground());	// Set background color	for selected row
	                return this;
	            }
	        };
			for (int i = 0; i < 2; i++) {	// first 2 columns only
				table7b.getColumnModel().getColumn(i).setCellRenderer(compact_r);
			}
			break;
		case "switch to compact view":
			for (int i = 0; i < 2; i++) {	// first 2 columns only
				table7b.getColumnModel().getColumn(i).setCellRenderer(render7b);
			}
			break;
		}
		
		// Apply combined filters
		TableRowSorter<PrismTableModel> sorter = new TableRowSorter<>((PrismTableModel) table7b.getModel());
		if (!filters.isEmpty()) {
			sorter.setRowFilter(RowFilter.andFilter(filters));
		}
		table7b.setRowSorter(sorter);
	}
}