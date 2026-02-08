/*******************************************************************************
 * Copyright (C) 2016-2018 PRISM Development Team
 ******************************************************************************/
package prism_project.edit;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class ScrollPane_SubTables_SR_Disturbances extends JScrollPane {		
	private JTable table6a, table6b, table6c, table6d;
	private Object[][] data6a, data6b, data6c, data6d;
	private int total_replacing_disturbances;
	private JScrollPane loss_rate_mean_scrollpane, loss_rate_std_scrollpane, conversion_rate_mean_scrollpane, conversion_rate_std_scrollpane;
	// New components for the filtered view
	private JTable editorTable;
	private List<Integer> mappingIndices = new ArrayList<>();
		
	public ScrollPane_SubTables_SR_Disturbances(JTable table6a, Object[][] data6a, JTable table6b, Object[][] data6b, JTable table6c, Object[][] data6c, JTable table6d, Object[][] data6d, int total_replacing_disturbances) {	
		this.table6a = table6a;
		this.table6b = table6b;
		this.table6c = table6c;
		this.table6d = table6d;
		this.data6a = data6a;
		this.data6b = data6b;
		this.data6c = data6c;
		this.data6d = data6d;
		this.total_replacing_disturbances = total_replacing_disturbances;
		
		// Setup editor table
		editorTable = new JTable();
		editorTable.setFillsViewportHeight(true);
		// Force save when clicking outside the table or hitting enter
		editorTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		editorTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		editorTable.setCellSelectionEnabled(true);
		editorTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	
		loss_rate_mean_scrollpane = new JScrollPane();
		TitledBorder border = new TitledBorder("Loss rate mean (%)");
		border.setTitleJustification(TitledBorder.CENTER);
		loss_rate_mean_scrollpane.setBorder(border);
		loss_rate_mean_scrollpane.setPreferredSize(new Dimension(0, 0));
		
		loss_rate_std_scrollpane = new JScrollPane();
		border = new TitledBorder("Loss rate standard deviation");
		border.setTitleJustification(TitledBorder.CENTER);
		loss_rate_std_scrollpane.setBorder(border);
		loss_rate_std_scrollpane.setPreferredSize(new Dimension(0, 0));
		
		conversion_rate_mean_scrollpane = new JScrollPane(editorTable);
		border = new TitledBorder("Conversion rate mean (%): sum of a column must be 100 if Loss rate mean > 0");
		border.setTitleJustification(TitledBorder.CENTER);
		conversion_rate_mean_scrollpane.setBorder(border);
		conversion_rate_mean_scrollpane.setPreferredSize(new Dimension(0, 0));
			
		conversion_rate_std_scrollpane = new JScrollPane();
		border = new TitledBorder("Conversion rate standard deviation");
		border.setTitleJustification(TitledBorder.CENTER);
		conversion_rate_std_scrollpane.setBorder(border);
		conversion_rate_std_scrollpane.setPreferredSize(new Dimension(0, 0));
		
		// Synchronize scroll and selection
		loss_rate_mean_scrollpane.getVerticalScrollBar().setModel(loss_rate_std_scrollpane.getVerticalScrollBar().getModel());	
		loss_rate_mean_scrollpane.getHorizontalScrollBar().setModel(loss_rate_std_scrollpane.getHorizontalScrollBar().getModel());	
		table6b.setSelectionModel(table6a.getSelectionModel());	 
		table6b.setColumnModel(table6a.getColumnModel());	 
		
		// Listener: Click in table6a updates the filtered editorTable
		table6a.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					updateVisuals();
				}
			}
		});
		
		JPanel combine_panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1;
	    combine_panel.add(loss_rate_mean_scrollpane, c);
		c.gridx = 1; c.gridy = 0; c.weightx = 1.4; c.weighty = 1; c.gridheight = 2;
	    combine_panel.add(conversion_rate_mean_scrollpane, c);
		c.gridx = 0; c.gridy = 1; c.weightx = 1; c.weighty = 1; c.gridheight = 1;
	    combine_panel.add(loss_rate_std_scrollpane, c);
		
		setViewportView(combine_panel);
		setBorder(null);
	}

	private void updateVisuals() {
		int row = table6a.getSelectedRow();
		if (row != -1) {
			String layer5 = table6a.getValueAt(row, 0).toString();
			refreshEditor(layer5);
		} else {
			editorTable.setModel(new DefaultTableModel());
		}
	}

	private void refreshEditor(String layer5) {
		if (editorTable.isEditing()) editorTable.getCellEditor().stopCellEditing();
		
		mappingIndices.clear();
		Vector<Vector<Object>> filteredRows = new Vector<>();
		Vector<String> headers = new Vector<>();
		for (int i = 0; i < table6c.getColumnCount(); i++) headers.add(table6c.getColumnName(i));

		for (int i = 0; i < data6c.length; i++) {
			if (data6c[i][0] != null && data6c[i][0].toString().equals(layer5)) {
				mappingIndices.add(i);
				Vector<Object> rowData = new Vector<>();
				for (Object obj : data6c[i]) rowData.add(obj);
				filteredRows.add(rowData);
			}
		}

		DefaultTableModel model = new DefaultTableModel(filteredRows, headers) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return col >= 2; // Only conversion rate columns are editable
			}
		};

		model.addTableModelListener(new TableModelListener() {
		    private boolean isReverting = false; // Flag to prevent infinite loops

		    @Override
		    public void tableChanged(TableModelEvent e) {
		        if (isReverting || e.getType() != TableModelEvent.UPDATE) return;

		        int r = e.getFirstRow();
		        int col = e.getColumn();
		        if (r < 0 || r >= mappingIndices.size()) return;

		        Object newValue = editorTable.getValueAt(r, col);
		        int masterRow = mappingIndices.get(r);

		        try {
		            double val = Double.parseDouble(newValue.toString());
		            if (val < 0 || val > 100) {
		                throw new NumberFormatException("Out of range");
		            }

		            // Success: update master data
		            data6c[masterRow][col] = val;

		            if (table6c.getSelectionModel() != null) {
		                table6c.getSelectionModel().setSelectionInterval(masterRow, masterRow);
		            }
		        } catch (NumberFormatException ex) {
		            // REVERT: Put the old value back from master data
		            isReverting = true; 
		            javax.swing.JOptionPane.showMessageDialog(null, "Invalid. Only double values from 0 to 100 (%) are allowed.");
		            
		            // This resets the text in the cell to what it was before
		            editorTable.setValueAt(data6c[masterRow][col], r, col); 
		            isReverting = false;
		        }
		    }
		});
		editorTable.setModel(model);
	}

	public String get_lr_mean_from_GUI() {	
		StringBuilder lr_mean = new StringBuilder();
		for (int row = 0; row < data6a.length; row++) {
			lr_mean.append(data6a[row][0]).append(" ").append(data6a[row][1]);
			for (int col = 2; col < data6a[row].length; col++) {
				lr_mean.append(" ").append(data6a[row][col]);
			}
			lr_mean.append(";");
		}			
		return lr_mean.length() > 0 ? lr_mean.substring(0, lr_mean.length() - 1) : "";
	}

	public String get_lr_std_from_GUI() {	
		StringBuilder lr_std = new StringBuilder();
		for (int row = 0; row < data6b.length; row++) {
			lr_std.append(data6b[row][0]).append(" ").append(data6b[row][1]);
			for (int col = 2; col < data6b[row].length; col++) {
				lr_std.append(" ").append(data6b[row][col]);
			}
			lr_std.append(";");
		}			
		return lr_std.length() > 0 ? lr_std.substring(0, lr_std.length() - 1) : "";
	}

	public String get_cr_mean_from_GUI() {	
		if (editorTable.isEditing()) editorTable.getCellEditor().stopCellEditing();
		StringBuilder cr_mean = new StringBuilder();
		for (int row = 0; row < data6c.length; row++) {
			cr_mean.append(data6c[row][0]).append(" ").append(data6c[row][1]);
			for (int col = 2; col < data6c[row].length; col++) {
				Object val = data6c[row][col];
				cr_mean.append(" ").append(val == null ? "0.0" : val.toString());
			}
			cr_mean.append(";");
		}			
		return cr_mean.length() > 0 ? cr_mean.substring(0, cr_mean.length() - 1) : "";
	}

	public String get_cr_std_from_GUI() {	
		StringBuilder cr_std = new StringBuilder();
		for (int row = 0; row < data6d.length; row++) {
			cr_std.append(data6d[row][0]).append(" ").append(data6d[row][1]);
			for (int col = 2; col < data6d[row].length; col++) {
				cr_std.append(" ").append(data6d[row][col]);
			}
			cr_std.append(";");
		}			
		return cr_std.length() > 0 ? cr_std.substring(0, cr_std.length() - 1) : "";
	}

	public void reload_this_condition(String lr_mean, String lr_std, String cr_mean, String cr_std) {	
		parseStringToData(lr_mean, data6a);
		parseStringToData(lr_std, data6b);
		parseStringToData(cr_mean, data6c);
		parseStringToData(cr_std, data6d);
		
		updateVisuals(); 
	}

	private void parseStringToData(String input, Object[][] target) {
		if(input != null && input.length() > 0) {
			String[] info = input.split(";");					
			for (int row = 0; row < info.length && row < target.length; row++) {			
				String[] sub_info = info[row].split(" ");
				for (int col = 2; col < 2 + total_replacing_disturbances && col < sub_info.length; col++) {
					target[row][col] = Double.valueOf(sub_info[col]);
				}
			}
		}
	}

	public JScrollPane get_loss_rate_mean_scrollpane() { return loss_rate_mean_scrollpane; }
	public JScrollPane get_conversion_rate_mean_scrollpane() { return conversion_rate_mean_scrollpane; }
	
	public void show_4_tables() {			
		loss_rate_mean_scrollpane.setViewportView(table6a);
		loss_rate_std_scrollpane.setViewportView(table6b);
		conversion_rate_mean_scrollpane.setViewportView(editorTable);
		conversion_rate_std_scrollpane.setViewportView(table6d);
		updateVisuals();
	}
	
	public void hide_4_tables() {			
		loss_rate_mean_scrollpane.setViewportView(null);
		loss_rate_std_scrollpane.setViewportView(null);
		conversion_rate_mean_scrollpane.setViewportView(null);
		conversion_rate_std_scrollpane.setViewportView(null);
	}
	
	public void update_4_tables_data(Object[][] data6a, Object[][] data6b, Object[][] data6c, Object[][] data6d) {			
		this.data6a = data6a;
		this.data6b = data6b;
		this.data6c = data6c;
		this.data6d = data6d;
		updateVisuals(); 
	}

	public JTable getEditorTable() {
		return editorTable;
	}
}