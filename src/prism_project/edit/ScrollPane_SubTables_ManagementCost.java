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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import prism_convenience.TableColumnsHandle;

public class ScrollPane_SubTables_ManagementCost extends JScrollPane {		
	private JTable table7a;
	private Object[][] data7a;
	private String[] columnNames7a;
	
	private JTable table7b;
	private Object[][] data7b;
	private String[] columnNames7b;
	
	private JScrollPane action_base_adjust_scrollpane;
	private JScrollPane conversion_base_adjust_scrollpane;
	
	private List<String> active_columns_list;
	private TableColumnsHandle column_handle;

	// New components for filtering table 7b
	private JComboBox<String> filterDropdown;
	private JTable editorTable;
	private List<Integer> mappingIndices = new ArrayList<>();
		
	public ScrollPane_SubTables_ManagementCost(JTable table7a, Object[][] data7a, String[] columnNames7a, JTable table7b, Object[][] data7b, String[] columnNames7b) {	
		this.table7a = table7a;
		this.data7a = data7a;
		this.columnNames7a = columnNames7a;
		
		this.table7b = table7b;
		this.data7b = data7b;
		this.columnNames7b = columnNames7b;
		
		this.column_handle = new TableColumnsHandle(table7a);	
		
		// Setup Filtered Editor for Table 7b
		filterDropdown = new JComboBox<>();
		filterDropdown.addActionListener(e -> refreshEditor());
		
		editorTable = new JTable();
		editorTable.setFillsViewportHeight(true);
		// Force save when clicking outside the table or hitting enter
		editorTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		editorTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		editorTable.setCellSelectionEnabled(true);
		editorTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		action_base_adjust_scrollpane = new JScrollPane(/*this.table7a*/);
		TitledBorder border = new TitledBorder("Activity cost per unit of column header");
		border.setTitleJustification(TitledBorder.CENTER);
		action_base_adjust_scrollpane.setBorder(border);
		action_base_adjust_scrollpane.setPreferredSize(new Dimension(0, 0));
		
		conversion_base_adjust_scrollpane = new JScrollPane();
		border = new TitledBorder("Conversion cost per area unit of conversion");
		border.setTitleJustification(TitledBorder.CENTER);
		conversion_base_adjust_scrollpane.setBorder(border);
		conversion_base_adjust_scrollpane.setPreferredSize(new Dimension(0, 0));
				
		JPanel combine_panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
	    c.weighty = 1;
	    combine_panel.add(action_base_adjust_scrollpane, c);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.4;
	    c.weighty = 1;
	    combine_panel.add(conversion_base_adjust_scrollpane, c);

		setViewportView(combine_panel);
		setBorder(null);
	}	
	
	/**
	 * Scans data7b to find unique items for the dropdown and refreshes view.
	 */
	public void setupDropdown7b() {
		// 1. Collect the unique items currently in data7b
		Set<String> newItems = new LinkedHashSet<>();
		for (int i = 0; i < data7b.length; i++) {
			if (data7b[i][0] != null) newItems.add(data7b[i][0].toString());
		}
		
		// 2. Check if the items in the dropdown are already the same
		// This prevents the "revert to first item" bug during cell edits
		boolean needsUpdate = false;
		if (filterDropdown.getItemCount() != newItems.size()) {
			needsUpdate = true;
		} else {
			for (int i = 0; i < filterDropdown.getItemCount(); i++) {
				if (!newItems.contains(filterDropdown.getItemAt(i))) {
					needsUpdate = true;
					break;
				}
			}
		}

		// 3. Only rebuild if necessary
		if (needsUpdate) {
			Object currentSelection = filterDropdown.getSelectedItem();
			filterDropdown.removeAllItems();
			for (String item : newItems) {
				filterDropdown.addItem(item);
			}
			
			// Restore previous selection if possible
			if (currentSelection != null) {
				filterDropdown.setSelectedItem(currentSelection);
			} else if (filterDropdown.getItemCount() > 0) {
				filterDropdown.setSelectedIndex(0);
			}
		}
		
		// Always refresh the editor view to show the latest master data values
		refreshEditor();
	}

	private void refreshEditor() {
	    if (editorTable.isEditing()) editorTable.getCellEditor().stopCellEditing();
	    if (filterDropdown.getSelectedItem() == null) {
	        editorTable.setModel(new DefaultTableModel());
	        return;
	    }

	    String selectedItem = filterDropdown.getSelectedItem().toString();
	    mappingIndices.clear();
	    Vector<Vector<Object>> filteredRows = new Vector<>();
	    Vector<String> headers = new Vector<>();
	    for (String name : columnNames7b) headers.add(name);

	    for (int i = 0; i < data7b.length; i++) {
	        if (data7b[i][0] != null && data7b[i][0].toString().equals(selectedItem)) {
	            mappingIndices.add(i);
	            Vector<Object> rowData = new Vector<>();
	            for (Object obj : data7b[i]) rowData.add(obj);
	            filteredRows.add(rowData);
	        }
	    }

	    DefaultTableModel model = new DefaultTableModel(filteredRows, headers) {
	        @Override
	        public boolean isCellEditable(int row, int col) { return col >= 2; }

	        @Override
	        public void setValueAt(Object value, int row, int col) {
	            int masterIndex = mappingIndices.get(row);
	            // Allow empty string to reset the value to null
	            if (value == null || value.toString().trim().isEmpty()) {
	                data7b[masterIndex][col] = null;
	                super.setValueAt(null, row, col);
	                return;
	            }

	            try {
	                double val = Double.parseDouble(value.toString());
	                if (val < 0) throw new NumberFormatException();

	                data7b[masterIndex][col] = val;
	                super.setValueAt(val, row, col);

	                if (table7b.getSelectionModel() != null) {
	                    table7b.getSelectionModel().setSelectionInterval(masterIndex, masterIndex);
	                }
	            } catch (Exception e) {
	                JOptionPane.showMessageDialog(null, "Invalid input. Only positive double values are allowed.");
	                super.setValueAt(data7b[masterIndex][col], row, col);
	            }
	        }
	    };
	    editorTable.setModel(model);
	}
			
	public String get_action_cost_info_from_GUI() {			
		StringBuilder sb = new StringBuilder();		
		for (int row = 0; row < data7a.length; row++) {
			for (int col = 1; col < data7a[row].length; col++) {
				if (data7a[row][col] != null) {
					sb.append(data7a[row][0]).append(" ").append(columnNames7a[col]).append(" ").append(data7a[row][col].toString()).append(";");
				}	
			}
		}					
		return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
	}
	
	public String get_conversion_cost_info_from_GUI() {			
		if (editorTable.isEditing()) editorTable.getCellEditor().stopCellEditing();
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < data7b.length; row++) {
			for (int col = 2; col < data7b[row].length; col++) {
				if (data7b[row][col] != null) {
					sb.append(data7b[row][0]).append(" ").append(data7b[row][1]).append(" ").append(columnNames7b[col]).append(" ").append(data7b[row][col].toString()).append(";");
				}	
			}
		}			
		return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
	}
	
	public void reload_this_condition_action_cost_and_conversion_cost(String action_cost_info, String conversion_cost_info) {	
		active_columns_list = new ArrayList<String>();
		
		// Reset data7a to null		
		for (int row = 0; row < data7a.length; row++) {
			for (int col = 1; col < data7a[row].length; col++) {
				data7a[row][col] = null;
			}
		}
		
		// Reload table7a
		if(action_cost_info.length() > 0) {
			String[] info_8a = action_cost_info.split(";");					
			for (int i = 0; i < info_8a.length; i++) {			
				String[] sub_info = info_8a[i].split(" ");
				String action = sub_info[0];
				String attribute = sub_info[1];
				active_columns_list.add(attribute);
				double cost = Double.valueOf(sub_info[2]);
				
				for (int row = 0; row < data7a.length; row++) {
					if (data7a[row][0].toString().equals(action)) {
						for (int col = 1; col < data7a[row].length; col++) {
							if (columnNames7a[col].equals(attribute)) {
								data7a[row][col] = cost;
							}
						}
					}
				}	
			}		
		}
		
		// Reset data7b to null		
		for (int row = 0; row < data7b.length; row++) {
			for (int col = 2; col < data7b[row].length; col++) {
				data7b[row][col] = null;
			}
		}
		
		// Reload table7b
		if(conversion_cost_info.length() > 0) {
			String[] info_8b = conversion_cost_info.split(";");					
			for (int i = 0; i < info_8b.length; i++) {			
				String[] sub_info = info_8b[i].split(" ");
				String covertype_before = sub_info[0];
				String covertype_after = sub_info[1];
				String attribute = sub_info[2];
				double cost = Double.valueOf(sub_info[3]);
				
				for (int row = 0; row < data7b.length; row++) {
					if ((data7b[row][0].toString() + data7b[row][1].toString()).equals(covertype_before + covertype_after)) {
						for (int col = 2; col < data7b[row].length; col++) {
							if (columnNames7b[col].equals(attribute)) {
								data7b[row][col] = cost;
							}
						}
					}
				}	
			}
		}
		setupDropdown7b(); // Rebuild dropdown and editor view after reload
	}

	public JScrollPane get_action_base_adjust_scrollpane() { return action_base_adjust_scrollpane; }
	public JScrollPane get_conversion_base_adjust_scrollpane() { return conversion_base_adjust_scrollpane; }
	
	public void show_active_columns_after_reload() {			
		for (int i = 2; i < columnNames7a.length; i++) {
			column_handle.setColumnVisible(columnNames7a[i], true);
			column_handle.setColumnVisible(columnNames7a[i], false);
		}
		for (String column_name: active_columns_list) {
			column_handle.setColumnVisible(column_name, true);
		}
	}
	
	public void show_2_tables() {			
		action_base_adjust_scrollpane.setViewportView(table7a);
		
		// Create a container panel to hold Dropdown + Editor Table
		JPanel container7b = new JPanel(new BorderLayout());
		container7b.add(filterDropdown, BorderLayout.NORTH);
		container7b.add(new JScrollPane(editorTable), BorderLayout.CENTER);
		
		conversion_base_adjust_scrollpane.setViewportView(container7b);
		setupDropdown7b();
	}
	
	public void hide_2_tables() {			
		action_base_adjust_scrollpane.setViewportView(null);
		conversion_base_adjust_scrollpane.setViewportView(null);
	}
	
	public void update_2_tables_data(Object[][] data7a, Object[][] data7b) {			
		this.data7a = data7a;
		this.data7b = data7b;
		setupDropdown7b();
	}
	
	public JTable getEditorTable() {
		return editorTable;
	}
}