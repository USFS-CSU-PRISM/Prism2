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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import prism_convenience.IconHandle;
import prism_convenience.PrismTableModel;
import prism_root.Prism2Main;

public class Panel_QuickEdit_EA extends JPanel {
	private JTable table;
	private Object[][] data;
	private DefaultTableCellRenderer render;
	
	private JButton btn_default;
	private JButton btn_compact;
	private JLabel view_label;
	private JButton btn_apply_e_min;
	private JButton btn_apply_e_max;
	private JButton btn_apply_r_min;
	private JButton btn_apply_r_max;
	private JButton btnApplyImplement;
	
	private JTextField searchField;
	
	public Panel_QuickEdit_EA(JTable table, Object[][] data, ArrayList<String>[] rotation_ranges, Object[][] default_data) {
		this.table = table;
		this.data = data;
		this.render = (DefaultTableCellRenderer) table.getColumnModel().getColumn(0).getCellRenderer();
		
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// Set weights to 1 to ensure equal spacing across columns
		c.weightx = 1.0;
		c.weighty = 0;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(2, 2, 2, 2); 

		
		// Some set up for combobox---------------------------------------------------------------------------------------------
		List<Integer> existing_min_age_list = new ArrayList<Integer>();
		List<Integer> existing_max_age_list = new ArrayList<Integer>();
		List<Integer> regeneration_min_age_list = new ArrayList<Integer>();
		List<Integer> regeneration_max_age_list = new ArrayList<Integer>();
		
		for (String s : rotation_ranges[1]) if (Integer.valueOf(s) != -9999) existing_min_age_list.add(Integer.valueOf(s));
		for (String s : rotation_ranges[2]) if (Integer.valueOf(s) != -9999) existing_max_age_list.add(Integer.valueOf(s));
		for (String s : rotation_ranges[3]) if (Integer.valueOf(s) != -9999) regeneration_min_age_list.add(Integer.valueOf(s));
		for (String s : rotation_ranges[4]) if (Integer.valueOf(s) != -9999) regeneration_max_age_list.add(Integer.valueOf(s));
		
		// this is the case when we remove -9999 but that is the only option we have across all cover types
		if (existing_min_age_list.isEmpty()) existing_min_age_list.add((int) -9999);
		if (existing_max_age_list.isEmpty()) existing_max_age_list.add((int) -9999);
		if (regeneration_min_age_list.isEmpty()) regeneration_min_age_list.add((int) -9999);
		if (regeneration_max_age_list.isEmpty()) regeneration_max_age_list.add((int) -9999);
		
		int min_age_for_combo_e = Collections.min(existing_min_age_list);
		int max_age_for_combo_e = Collections.max(existing_max_age_list);
		int min_age_for_combo_r = Collections.min(regeneration_min_age_list);
		int max_age_for_combo_r = Collections.max(regeneration_max_age_list);
		
		
		class Combo_e_minage extends JComboBox {
			{
				setPreferredSize(new Dimension(75, getPreferredSize().height));
			}

			public Combo_e_minage() {
				for (int i = min_age_for_combo_e; i <= max_age_for_combo_e; i++) {
					addItem(i);
				}
				setSelectedItem(min_age_for_combo_e);
			}
		}
		
		class Combo_e_maxage extends JComboBox {
			{
				setPreferredSize(new Dimension(75, getPreferredSize().height));
			}
			
			public Combo_e_maxage() {
				for (int i = min_age_for_combo_e; i <= max_age_for_combo_e; i++) {
					addItem(i);
				}
				setSelectedItem(max_age_for_combo_e);
			}
		}
		
		class Combo_r_minage extends JComboBox {
			{
				setPreferredSize(new Dimension(75, getPreferredSize().height));
			}
			
			public Combo_r_minage() {
				for (int i = min_age_for_combo_r; i <= max_age_for_combo_r; i++) {
					addItem(i);
				}
				setSelectedItem(min_age_for_combo_r);
			}
		}
		
		class Combo_r_maxage extends JComboBox {
			{
				setPreferredSize(new Dimension(75, getPreferredSize().height));
			}
			
			public Combo_r_maxage() {
				for (int i = min_age_for_combo_r; i <= max_age_for_combo_r; i++) {
					addItem(i);
				}
				setSelectedItem(max_age_for_combo_r);
			}
		}
		
		
		// Add Label-------------------------------------------------------------------------------------------------
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(new JLabel("reset"), c);
		
		// Add button default
		btn_default = new JButton();
		btn_default.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn_default.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_default.setToolTipText("set all cells to default values");
		btn_default.setIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_main.png"));
		btn_default.setRolloverIcon(IconHandle.get_scaledImageIcon(35, 35, "icon_main.png"));
		btn_default.setContentAreaFilled(false);
		btn_default.addActionListener(e -> {
			String ExitOption[] = {"Reset","Cancel"};
			int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(),"Reset now?", "Reset all cells to default",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[0]);
			if (response == 0) {	
				String previous_view = view_label.getText();
				view_label.setText("      full view      ");
				reset_view_without_changing_label();
				//---------------------------------------the 3 lines above is needed to reset to full view 
				for (int i = 0; i < default_data.length; i++) {
					data[i] = Arrays.copyOf(default_data[i], default_data[i].length);
				}
				
				// Fix: only select rows that are currently visible in the view
				table.clearSelection();
				for (int i = 0; i < data.length; i++) {
					int vIdx = table.convertRowIndexToView(i);
					if (vIdx != -1) {
						table.addRowSelectionInterval(vIdx, vIdx);
					}
				}
				table.clearSelection();
				//---------------------------------------the 2 lines below retrive the previous view. Those 5 lines are not good at all, keep it temporarily to avoid loading incorrectly table4a 
				view_label.setText(previous_view);
				reset_view_without_changing_label();
			}
			if (response == 1) {
			}
		});
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 2;
		add(btn_default, c);
		
		
		
		
		// Add Label-------------------------------------------------------------------------------------------------
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		view_label  = new JLabel("switch view");
		add(view_label, c);
		
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
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 2;
		add(btn_compact, c);
				

		// Add Label-------------------------------------------------------------------------------------------------
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(new JLabel("layer5 filter"), c);

		// Add Search Field
		searchField = new JTextField(6);
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e) { reset_view_without_changing_label(); }
			@Override public void removeUpdate(DocumentEvent e) { reset_view_without_changing_label(); }
			@Override public void changedUpdate(DocumentEvent e) { reset_view_without_changing_label(); }
		});
		c.gridx = 2;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(searchField, c);
		c.fill = GridBagConstraints.CENTER;

		
		// Add Label-------------------------------------------------------------------------------------------------
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(new JLabel("EA_E_min_ra"), c);

		
		// Add comboBox
		JComboBox combo_e_min = new Combo_e_minage();
		c.gridx = 3;
		c.gridy = 1;
		add(combo_e_min, c);
		
		
		// Add button apply
		btn_apply_e_min = new JButton();
		btn_apply_e_min.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn_apply_e_min.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_apply_e_min.setToolTipText("make changes for all highlighted cells in one column");
		btn_apply_e_min.setIcon(IconHandle.get_scaledImageIcon(20, 20, "icon_split.png"));
		btn_apply_e_min.setRolloverIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_split.png"));
		btn_apply_e_min.setContentAreaFilled(false);
		btn_apply_e_min.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Get selected rows
				int[] selectedRow = table.getSelectedRows();
				/// Convert row index because "Sort" causes problems
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = table.convertRowIndexToModel(selectedRow[i]);
				}
				table.clearSelection(); // To help trigger the row refresh: clear then add back the rows
				for (int i : selectedRow) {
					String covertype = (String) data[i][0];
					int min_age_cut_existing = (rotation_ranges[0].indexOf(covertype) >= 0) ? Integer.valueOf(rotation_ranges[1].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    int max_age_cut_existing = (rotation_ranges[0].indexOf(covertype) >= 0) ? Integer.valueOf(rotation_ranges[2].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    
				    if ((int) combo_e_min.getSelectedItem() > max_age_cut_existing) {
				    	if ((int) data[i][2] != -9999) data[i][2] = max_age_cut_existing;
					} else if ((int) combo_e_min.getSelectedItem() < min_age_cut_existing) {
						if ((int) data[i][2] != -9999) data[i][2] = min_age_cut_existing;
					} else {
						if ((int) data[i][2] != -9999) data[i][2] = combo_e_min.getSelectedItem();
					}
				    
					int viewIdx = table.convertRowIndexToView(i);
					if (viewIdx != -1) {
						table.addRowSelectionInterval(viewIdx, viewIdx);
					}
				}
			}
		});
		c.gridx = 3;
		c.gridy = 0;
		add(btn_apply_e_min, c);

		
	
		// Add Label-------------------------------------------------------------------------------------------------
		c.gridx = 4;
		c.gridy = 2;
		add(new JLabel("EA_E_max_ra"), c);
		
		
		// Add comboBox
		JComboBox combo_e_max = new Combo_e_maxage();
		c.gridx = 4;
		c.gridy = 1;
		add(combo_e_max, c);
		

		// Add button apply
		btn_apply_e_max = new JButton();
		btn_apply_e_max.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn_apply_e_max.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_apply_e_max.setToolTipText("make changes for all highlighted cells in one column");
		btn_apply_e_max.setIcon(IconHandle.get_scaledImageIcon(20, 20, "icon_split.png"));
		btn_apply_e_max.setRolloverIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_split.png"));
		btn_apply_e_max.setContentAreaFilled(false);
		btn_apply_e_max.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Get selected rows
				int[] selectedRow = table.getSelectedRows();
				/// Convert row index because "Sort" causes problems
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = table.convertRowIndexToModel(selectedRow[i]);
				}
				table.clearSelection(); // To help trigger the row refresh: clear then add back the rows
				for (int i : selectedRow) {
					String covertype = (String) data[i][0];
					int min_age_cut_existing = (rotation_ranges[0].indexOf(covertype) >= 0) ? Integer.valueOf(rotation_ranges[1].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    int max_age_cut_existing = (rotation_ranges[0].indexOf(covertype) >= 0) ? Integer.valueOf(rotation_ranges[2].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    
				    if ((int) combo_e_max.getSelectedItem() > max_age_cut_existing) {
				    	if ((int) data[i][3] != -9999) data[i][3] = max_age_cut_existing;
					} else if ((int) combo_e_max.getSelectedItem() < min_age_cut_existing) {
						if ((int) data[i][3] != -9999) data[i][3] = min_age_cut_existing;
					} else {
						if ((int) data[i][3] != -9999) data[i][3] = combo_e_max.getSelectedItem();
					}
				    
					int viewIdx = table.convertRowIndexToView(i);
					if (viewIdx != -1) {
						table.addRowSelectionInterval(viewIdx, viewIdx);
					}
				}
			}
		});
		c.gridx = 4;
		c.gridy = 0;
		add(btn_apply_e_max, c);
		
	
		
		// Add Label-------------------------------------------------------------------------------------------------
		c.gridx = 5;
		c.gridy = 2;
		add(new JLabel("EA_R_min_ra"), c);

		
		// Add comboBox
		JComboBox combo_r_min = new Combo_r_minage();
		c.gridx = 5;
		c.gridy = 1;
		add(combo_r_min, c);
		
		
		// Add button apply
		btn_apply_r_min = new JButton();
		btn_apply_r_min.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn_apply_r_min.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_apply_r_min.setToolTipText("make changes for all highlighted cells in one column");
		btn_apply_r_min.setIcon(IconHandle.get_scaledImageIcon(20, 20, "icon_split.png"));
		btn_apply_r_min.setRolloverIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_split.png"));
		btn_apply_r_min.setContentAreaFilled(false);
		btn_apply_r_min.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Get selected rows
				int[] selectedRow = table.getSelectedRows();
				/// Convert row index because "Sort" causes problems
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = table.convertRowIndexToModel(selectedRow[i]);
				}
				table.clearSelection(); // To help trigger the row refresh: clear then add back the rows
				for (int i : selectedRow) {
					String covertype = (String) data[i][0];
					int min_age_cut_regeneration = (rotation_ranges[0].indexOf(covertype) >= 0) ? Integer.valueOf(rotation_ranges[3].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    int max_age_cut_regeneration = (rotation_ranges[0].indexOf(covertype) >= 0) ? Integer.valueOf(rotation_ranges[4].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    
				    if ((int) combo_r_min.getSelectedItem() > max_age_cut_regeneration) {
				    	if ((int) data[i][4] != -9999) data[i][4] = max_age_cut_regeneration;
					} else if ((int) combo_r_min.getSelectedItem() < min_age_cut_regeneration) {
						if ((int) data[i][4] != -9999) data[i][4] = min_age_cut_regeneration;
					} else {
						if ((int) data[i][4] != -9999) data[i][4] = combo_r_min.getSelectedItem();
					}
				    
					int viewIdx = table.convertRowIndexToView(i);
					if (viewIdx != -1) {
						table.addRowSelectionInterval(viewIdx, viewIdx);
					}
				}
			}
		});
		c.gridx = 5;
		c.gridy = 0;
		add(btn_apply_r_min, c);

		
	
		// Add Label-------------------------------------------------------------------------------------------------
		c.gridx = 6;
		c.gridy = 2;
		add(new JLabel("EA_R_max_ra"), c);
		
		
		// Add comboBox
		JComboBox combo_r_max = new Combo_r_maxage();
		c.gridx = 6;
		c.gridy = 1;
		add(combo_r_max, c);
		

		// Add button apply
		btn_apply_r_max = new JButton();
		btn_apply_r_max.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn_apply_r_max.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_apply_r_max.setToolTipText("make changes for all highlighted cells in one column");
		btn_apply_r_max.setIcon(IconHandle.get_scaledImageIcon(20, 20, "icon_split.png"));
		btn_apply_r_max.setRolloverIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_split.png"));
		btn_apply_r_max.setContentAreaFilled(false);
		btn_apply_r_max.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Get selected rows
				int[] selectedRow = table.getSelectedRows();
				/// Convert row index because "Sort" causes problems
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = table.convertRowIndexToModel(selectedRow[i]);
				}
				table.clearSelection(); // To help trigger the row refresh: clear then add back the rows
				for (int i : selectedRow) {
					String covertype = (String) data[i][0];
					int min_age_cut_regeneration = (rotation_ranges[0].indexOf(covertype) >= 0) ? Integer.valueOf(rotation_ranges[3].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    int max_age_cut_regeneration = (rotation_ranges[0].indexOf(covertype) >= 0) ? Integer.valueOf(rotation_ranges[4].get(rotation_ranges[0].indexOf(covertype))) : -9999;
				    
				    if ((int) combo_r_max.getSelectedItem() > max_age_cut_regeneration) {
						if ((int) data[i][5] != -9999) data[i][5] = max_age_cut_regeneration;
					} else if ((int) combo_r_max.getSelectedItem() < min_age_cut_regeneration) {
						if ((int) data[i][5] != -9999) data[i][5] = min_age_cut_regeneration;
					} else {
						if ((int) data[i][5] != -9999) data[i][5] = combo_r_max.getSelectedItem();
					}
				    
					int viewIdx = table.convertRowIndexToView(i);
					if (viewIdx != -1) {
						table.addRowSelectionInterval(viewIdx, viewIdx);
					}
				}
			}
		});
		c.gridx = 6;
		c.gridy = 0;
		add(btn_apply_r_max, c);
		
		
		
		
		
		
		// Add Label-------------------------------------------------------------------------------------------------
		c.gridx = 7;
		c.gridy = 2;
		add(new JLabel("implementation"), c);

	
		// Add checkBox
		JCheckBox implement_Check = new JCheckBox();
		implement_Check.setSelected(true);
		c.gridx = 7;
		c.gridy = 1;
		add(implement_Check, c);

		
		// Add button apply
		btnApplyImplement = new JButton();
		btnApplyImplement.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnApplyImplement.setHorizontalTextPosition(SwingConstants.CENTER);
		btnApplyImplement.setToolTipText("make changes for all highlighted cells in one column");
		btnApplyImplement.setIcon(IconHandle.get_scaledImageIcon(20, 20, "icon_split.png"));
		btnApplyImplement.setRolloverIcon(IconHandle.get_scaledImageIcon(25, 25, "icon_split.png"));
		btnApplyImplement.setContentAreaFilled(false);
		btnApplyImplement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Get selected rows
				int[] selectedRow = table.getSelectedRows();
				/// Convert row index because "Sort" causes problems
				for (int i = 0; i < selectedRow.length; i++) {
					selectedRow[i] = table.convertRowIndexToModel(selectedRow[i]);
				}
				table.clearSelection(); // To help trigger the row refresh: clear then add back the rows
				for (int i : selectedRow) {
					if (implement_Check.isSelected()) {
						data[i][6] = true;
					} else {
						data[i][6] = false;
					}	
					int viewIdx = table.convertRowIndexToView(i);
					if (viewIdx != -1) {
						table.addRowSelectionInterval(viewIdx, viewIdx);
					}
				}
				reset_view_without_changing_label();
			}
		});
		c.gridx = 7;
		c.gridy = 0;
		add(btnApplyImplement, c);
	}
	
	
	public void disable_all_apply_buttons() {
		btn_default.setEnabled(false);
		btn_compact.setEnabled(false);
		btn_apply_e_min.setEnabled(false);
		btn_apply_e_max.setEnabled(false);
		btn_apply_r_min.setEnabled(false);
		btn_apply_r_max.setEnabled(false);
		btnApplyImplement.setEnabled(false);
	}
	
	public void enable_all_apply_buttons() {
		btn_default.setEnabled(true);
		btn_compact.setEnabled(true);
		btn_apply_e_min.setEnabled(true);
		btn_apply_e_max.setEnabled(true);
		btn_apply_r_min.setEnabled(true);
		btn_apply_r_max.setEnabled(true);
		btnApplyImplement.setEnabled(true);
		reset_view_without_changing_label();
	}
	
	public void reset_view_without_changing_label() {
		if (table.isEditing()) {
			table.getCellEditor().cancelCellEditing();
		}
		
		List<RowFilter<Object, Object>> filters = new ArrayList<>();
		
		// Search filter logic
		String searchText = (searchField != null) ? searchField.getText() : "";
		filters.add(RowFilter.regexFilter("(?i)" + searchText, 0));
		
		switch (btn_compact.getToolTipText()) {
		case "switch to full view":
			if (data != null) {
				filters.add(new RowFilter<Object, Object>() {
					public boolean include(Entry entry) {
						Boolean implementation = (boolean) entry.getValue(6);
						return implementation == true;
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
					table.getColumnModel().getColumn(i).setCellRenderer(compact_r);
				}
			}
			break;
		case "switch to compact view":
			for (int i = 0; i < 2; i++) {	// first 2 columns only
				table.getColumnModel().getColumn(i).setCellRenderer(render);
			}
			break;
		}
		
		TableRowSorter<PrismTableModel> sorter = new TableRowSorter<PrismTableModel>((PrismTableModel) table.getModel());
		sorter.setRowFilter(RowFilter.andFilter(filters));
		table.setRowSorter(sorter);
	}
}