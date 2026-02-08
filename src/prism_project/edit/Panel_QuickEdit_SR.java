/*******************************************************************************
 * Copyright (C) 2016-2018 PRISM Development Team
 * * PRISM is free software: you can redistribute it and/or modify
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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import prism_convenience.IconHandle;
import prism_convenience.PrismGridBagLayoutHandle;
import prism_convenience.PrismTableModel;

public class Panel_QuickEdit_SR extends JPanel {
	private JTable table6a, table6b, table6c;
	private Object[][] data6a, data6b, data6c;
	private DefaultTableCellRenderer render6a, render6b, render6c;
	
	private JButton btn_compact;
	private JLabel view_label;
	private JButton btnApplyLrmean, btnApplyLrstd;
	private JButton btnApplyPercentage;
	
	// Search field for category filtering
	private JTextField searchField;
	
	public Panel_QuickEdit_SR(JTable table6a, Object[][] data6a, JTable table6b, Object[][] data6b, JTable table6c, Object[][] data6c, JTable table6d, Object[][] data6d) {
		this.table6a = table6a;
		this.data6a = data6a;
		this.render6a = (DefaultTableCellRenderer) table6a.getColumnModel().getColumn(0).getCellRenderer();
		this.table6b = table6b;
		this.data6b = data6b;
		this.render6b = (DefaultTableCellRenderer) table6b.getColumnModel().getColumn(0).getCellRenderer();
		this.table6c = table6c;
		this.data6c = data6c;
		this.render6c = (DefaultTableCellRenderer) table6c.getColumnModel().getColumn(0).getCellRenderer();
		
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		
		// -------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------
		JPanel qd1 = new JPanel();
		qd1.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		

		// Add Label-------------------------------------------------------------------------------------------------
		qd1.add(new JLabel("Lr mean (%)"), PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				0, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right

		
		// Add formatedTextfield
		JFormattedTextField lr_mean_textfield = new JFormattedTextField();
		lr_mean_textfield.setColumns(8);
		lr_mean_textfield.setToolTipText("0-100 percent");
		lr_mean_textfield.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				Runnable format = new Runnable() {
					@Override
					public void run() {
						String text = lr_mean_textfield.getText();
//						if (!text.matches("\\d*(\\.\\d{0,2})?")) {		//	used regex: \\d*(\\.\\d{0,2})? because two decimal places is enough
						if (!text.matches("\\d*(\\.\\d{0,})?")) {		//	no restriction on number of digits after the dot
							lr_mean_textfield.setText(text.substring(0, text.length() - 1));
						} else {
							if (!text.isEmpty() && !text.equals(".") && (Double.valueOf(text) < (double) 0 || Double.valueOf(text) > (double) 100)) {		// If the added String make value <0 or >100 then delete that String
								lr_mean_textfield.setText(text.substring(0, text.length() - 1));
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
		qd1.add(lr_mean_textfield, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				0, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
				
		// Add button apply
		btnApplyLrmean = new JButton();
		btnApplyLrmean.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnApplyLrmean.setHorizontalTextPosition(SwingConstants.CENTER);
		btnApplyLrmean.setToolTipText("make changes for all highlighted cells, except cells in the first column");
		btnApplyLrmean.setIcon(IconHandle.get_scaledImageIcon(20, 20, "icon_split.png"));
		btnApplyLrmean.setRolloverIcon(IconHandle.get_scaledImageIcon(30, 30, "icon_split.png"));
		btnApplyLrmean.setContentAreaFilled(false);
		btnApplyLrmean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Get selected rows
				int[] selectedRow = table6a.getSelectedRows();
				int[] selectedCol = table6a.getSelectedColumns();
				
				if (selectedRow.length == 0) return; // Guard against empty selection
							
				// Convert row index because "Sort" causes problems
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = table6a.convertRowIndexToModel(selectedRow[i]);
				}
				// Convert col index because "Sort" causes problems
				for (int j = 0; j < selectedCol.length; j++) {
					selectedCol[j] = table6a.convertColumnIndexToModel(selectedCol[j]);
				}
				
				for (int i : selectedRow) {
					for (int j : selectedCol) {
						if (!lr_mean_textfield.getText().equals(".") && j >= 2) {	// Only apply the changes to selected cells in columns >= 2 (fall the percentage columns)
							if (!lr_mean_textfield.getText().isEmpty()) data6a[i][j] = Double.valueOf(lr_mean_textfield.getText());	// Only apply the changes to selected cells in column 2 "weight", do not allow null
						}
					}
				}
				
				// just need to add 1 currently selected row (no need to add all because it would trigger a lot of "fireTableDataChanged" in "setValueAt" because of the ListSelectionListener of table6a)
				// also need re-validate and repaint so all the new data would show up after the change is triggered by the "addRowSelectionInterval"
				table6a.removeRowSelectionInterval(table6a.convertRowIndexToView(selectedRow[0]), table6a.convertRowIndexToView(selectedRow[0]));	// only trigger the data change once by remove then add 1 time
				table6a.addRowSelectionInterval(table6a.convertRowIndexToView(selectedRow[0]), table6a.convertRowIndexToView(selectedRow[0]));
				table6a.revalidate();
				table6a.repaint();
			}
		});		
		qd1.add(btnApplyLrmean, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				0, 0, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
		
		
		
		// Add Label-------------------------------------------------------------------------------------------------
		qd1.add(new JLabel("Lr std"), PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right

		
		// Add lr_std_textfield
		JFormattedTextField lr_std_textfield = new JFormattedTextField();
		lr_std_textfield.setColumns(8);
//		lr_std_textfield.setToolTipText("0-100 percent");
		lr_std_textfield.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				Runnable format = new Runnable() {
					@Override
					public void run() {
						String text = lr_std_textfield.getText();
//						if (!text.matches("\\d*(\\.\\d{0,2})?")) {		//	used regex: \\d*(\\.\\d{0,2})? because two decimal places is enough
						if (!text.matches("\\d*(\\.\\d{0,})?")) {		//	no restriction on number of digits after the dot
							lr_std_textfield.setText(text.substring(0, text.length() - 1));
						} else {
//							if (!text.isEmpty() && !text.equals(".") && (Double.valueOf(text) < (double) 0 || Double.valueOf(text) > (double) 100)) {		// If the added String make value <0 or >100 then delete that String
//								lr_std_textfield.setText(text.substring(0, text.length() - 1));
//							}
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
		qd1.add(lr_std_textfield, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
				
		// Add button apply
		btnApplyLrstd = new JButton();
		btnApplyLrstd.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnApplyLrstd.setHorizontalTextPosition(SwingConstants.CENTER);
		btnApplyLrstd.setToolTipText("make changes for all highlighted cells, except cells in the first column");
		btnApplyLrstd.setIcon(IconHandle.get_scaledImageIcon(20, 20, "icon_split.png"));
		btnApplyLrstd.setRolloverIcon(IconHandle.get_scaledImageIcon(30, 30, "icon_split.png"));
		btnApplyLrstd.setContentAreaFilled(false);
		btnApplyLrstd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Get selected rows
				int[] selectedRow = table6b.getSelectedRows();
				int[] selectedCol = table6b.getSelectedColumns();
				
				if (selectedRow.length == 0) return;
							
				// Convert row index because "Sort" causes problems
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = table6b.convertRowIndexToModel(selectedRow[i]);
				}
				// Convert col index because "Sort" causes problems
				for (int j = 0; j < selectedCol.length; j++) {
					selectedCol[j] = table6b.convertColumnIndexToModel(selectedCol[j]);
				}
				
				for (int i : selectedRow) {
					for (int j : selectedCol) {
						if (!lr_std_textfield.getText().equals(".") && j >= 2) {	// Only apply the changes to selected cells in columns >= 2 (fall the percentage columns)
							if (!lr_std_textfield.getText().isEmpty()) data6b[i][j] = Double.valueOf(lr_std_textfield.getText());	// Only apply the changes to selected cells in column 2 "weight", do not allow null
						}
					}
				}
				
				// just need to add 1 currently selected row (no need to add all because it would trigger a lot of "fireTableDataChanged" in "setValueAt" because of the ListSelectionListener of table6b)
				// also need re-validate and repaint so all the new data would show up after the change is triggered by the "addRowSelectionInterval"
				table6b.removeRowSelectionInterval(table6b.convertRowIndexToView(selectedRow[0]), table6b.convertRowIndexToView(selectedRow[0]));	// only trigger the data change once by remove then add 1 time
				table6b.addRowSelectionInterval(table6b.convertRowIndexToView(selectedRow[0]), table6b.convertRowIndexToView(selectedRow[0]));
				table6b.revalidate();
				table6b.repaint();
			}
		});		
		qd1.add(btnApplyLrstd, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 0, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		// -------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------
		
		
		
		
		
		
		
		// -------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------
		JPanel qd2 = new JPanel();
		qd2.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		

		// Add Label-------------------------------------------------------------------------------------------------
		qd2.add(new JLabel("Cr mean (%)"), PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				0, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right

		
		// Add formatedTextfield
		JFormattedTextField formatedTextfield_2 = new JFormattedTextField();
		formatedTextfield_2.setColumns(8);
		formatedTextfield_2.setToolTipText("0-100 percent");
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
							if (!text.isEmpty() && !text.equals(".") && (Double.valueOf(text) < (double) 0 || Double.valueOf(text) > (double) 100)) {		// If the added String make value <0 or >100 then delete that String
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
				0, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
				
		// Add button apply
		btnApplyPercentage = new JButton();
		btnApplyPercentage.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnApplyPercentage.setHorizontalTextPosition(SwingConstants.CENTER);
		btnApplyPercentage.setToolTipText("make changes for all highlighted cells, except cells in the first two columns");
		btnApplyPercentage.setIcon(IconHandle.get_scaledImageIcon(20, 20, "icon_split.png"));
		btnApplyPercentage.setRolloverIcon(IconHandle.get_scaledImageIcon(30, 30, "icon_split.png"));
		btnApplyPercentage.setContentAreaFilled(false);
		btnApplyPercentage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Get selected rows
				int[] selectedRow = table6c.getSelectedRows();
				int[] selectedCol = table6c.getSelectedColumns();
				
				if (selectedRow.length == 0) return;
							
				// Convert row index because "Sort" causes problems
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = table6c.convertRowIndexToModel(selectedRow[i]);
				}
				// Convert col index because "Sort" causes problems
				for (int j = 0; j < selectedCol.length; j++) {
					selectedCol[j] = table6c.convertColumnIndexToModel(selectedCol[j]);
				}
				
				for (int i : selectedRow) {
					for (int j : selectedCol) {
						if (!formatedTextfield_2.getText().equals(".") && j >= 2) {	// Only apply the changes to selected cells in columns >= 2 (all the percentage columns)
							if (!formatedTextfield_2.getText().isEmpty()) data6c[i][j] = Double.valueOf(formatedTextfield_2.getText());	// Only apply the changes to selected cells in column 2 "weight", do not allow null
						}
					}
				}
				
				// just need to add 1 currently selected row (no need to add all because it would trigger a lot of "fireTableDataChanged" in "setValueAt" because of the ListSelectionListener of table6a)
				// also need re-validate and repaint so all the new data would show up after the change is triggered by the "addRowSelectionInterval"
				table6c.removeRowSelectionInterval(table6c.convertRowIndexToView(selectedRow[0]), table6c.convertRowIndexToView(selectedRow[0]));	// only trigger the data change once by remove then add 1 time
				table6c.addRowSelectionInterval(table6c.convertRowIndexToView(selectedRow[0]), table6c.convertRowIndexToView(selectedRow[0]));
				table6c.revalidate();
				table6c.repaint();
				reset_view_without_changing_label();
			}
		});		
		qd2.add(btnApplyPercentage, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				0, 0, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
		
		// Add Search Category Field----------------------------------------------------------------------------------
		qd2.add(new JLabel("layer5 filter"), PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		
		searchField = new JTextField(8);
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e) { reset_view_without_changing_label(); }
			@Override public void removeUpdate(DocumentEvent e) { reset_view_without_changing_label(); }
			@Override public void changedUpdate(DocumentEvent e) { reset_view_without_changing_label(); }
		});
		qd2.add(searchField, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				1, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
				
		
		
		// Add Label-------------------------------------------------------------------------------------------------
		view_label  = new JLabel("switch view");
		qd2.add(view_label, PrismGridBagLayoutHandle.get_c(c, "CENTER", 
				2, 2, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
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
				2, 1, 1, 1, 0, 0, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		// -------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------
		
		
		
				
		// Add 2 panels to this big Panel
		add(qd1, PrismGridBagLayoutHandle.get_c(c, "HORIZONTAL", 
				0, 0, 1, 0, 1, 1, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
		add(qd2, PrismGridBagLayoutHandle.get_c(c, "HORIZONTAL", 
				1, 0, 1, 0, 1, 1, 	// gridx, gridy, gridwidth, gridheight, weightx, weighty
				0, 0, 0, 0));		// insets top, left, bottom, right
	}
	
	
	public void disable_all_apply_buttons() {
		btnApplyLrmean.setEnabled(false);
		btnApplyPercentage.setEnabled(false);
		btn_compact.setEnabled(false);
	}
	
	public void enable_all_apply_buttons() {
		btnApplyLrmean.setEnabled(true);
		btnApplyPercentage.setEnabled(true);
		btn_compact.setEnabled(true);
		reset_view_without_changing_label();
	}
	
	public void reset_view_without_changing_label() {
		if (table6a.isEditing()) table6a.getCellEditor().cancelCellEditing();
		if (table6b.isEditing()) table6b.getCellEditor().cancelCellEditing();
		if (table6c.isEditing()) table6c.getCellEditor().cancelCellEditing();
		
		// Create Search Filter (Case Insensitive)
		String searchText = searchField.getText().trim();
		RowFilter<Object, Object> textFilter = RowFilter.regexFilter("(?i)" + searchText, 0);

		switch (btn_compact.getToolTipText()) {
		case "switch to full view":
			// for table 6a and 6b: loss rate mean and std
			List<RowFilter<Object, Object>> filtersA = new ArrayList<RowFilter<Object, Object>>();
			filtersA.add(textFilter);
			filtersA.add(new RowFilter<Object, Object>() {
				public boolean include(Entry entry) {
					for (int col = 2; col < data6a[0].length; col++) {	// except the first 2 columns
						if ((double) entry.getValue(col) != 0) {
							return true;		// if 1 cell in this row has value different from zero then show the row
						}
					}
					return false;	// hide the row when all cells have the value of zero
				}
			});
			
			List<RowFilter<Object, Object>> filtersB = new ArrayList<RowFilter<Object, Object>>();
			filtersB.add(textFilter);
			filtersB.add(new RowFilter<Object, Object>() {
				public boolean include(Entry entry) {
					for (int col = 2; col < data6b[0].length; col++) {	// except the first 2 columns
						if ((double) entry.getValue(col) != 0) {
							return true;		// if 1 cell in this row has value different from zero then show the row
						}
					}
					return false;	// hide the row when all cells have the value of zero
				}
			});

			TableRowSorter<PrismTableModel> sorter_a = new TableRowSorter<PrismTableModel>((PrismTableModel) table6a.getModel());
			sorter_a.setRowFilter(RowFilter.andFilter(filtersA));
			table6a.setRowSorter(sorter_a);
			
			TableRowSorter<PrismTableModel> sorter_b = new TableRowSorter<PrismTableModel>((PrismTableModel) table6b.getModel());
			sorter_b.setRowFilter(RowFilter.andFilter(filtersB));
			table6b.setRowSorter(sorter_b);
			
			
			// for table 6c: conversion rate mean
			List<RowFilter<Object, Object>> filtersC = new ArrayList<RowFilter<Object, Object>>();
			filtersC.add(textFilter);
			filtersC.add(new RowFilter<Object, Object>() {
				public boolean include(Entry entry) {
					for (int col = 2; col < data6c[0].length; col++) {	// except the first 2 columns
						if ((double) entry.getValue(col) != 0) {
							return true;		// if 1 cell in this row has value different from zero then show the row
						}
					}
					return false;	// hide the row when all cells have the value of zero
				}
			});
			
			TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>((PrismTableModel) table6c.getModel());
			sorter.setRowFilter(RowFilter.andFilter(filtersC));
			table6c.setRowSorter(sorter);
			
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
				table6c.getColumnModel().getColumn(i).setCellRenderer(compact_r);
			}
			break;
		case "switch to compact view":
			// Apply text filter even in full view
			TableRowSorter<PrismTableModel> full_sorter_a = new TableRowSorter<>((PrismTableModel) table6a.getModel());
			full_sorter_a.setRowFilter(textFilter);
			table6a.setRowSorter(full_sorter_a);
			
			TableRowSorter<PrismTableModel> full_sorter_b = new TableRowSorter<>((PrismTableModel) table6b.getModel());
			full_sorter_b.setRowFilter(textFilter);
			table6b.setRowSorter(full_sorter_b);
			
			TableRowSorter<PrismTableModel> full_sorter_c = new TableRowSorter<>((PrismTableModel) table6c.getModel());
			full_sorter_c.setRowFilter(textFilter);
			table6c.setRowSorter(full_sorter_c);
			
			for (int i = 0; i < 2; i++) {	// first 2 columns only
				table6c.getColumnModel().getColumn(i).setCellRenderer(render6c);
			}
			break;
		}
	}
}