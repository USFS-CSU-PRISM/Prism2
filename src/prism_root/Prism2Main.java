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
package prism_root;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.FontUIResource;

import prism_convenience.ColorUtil;
import prism_convenience.ComponentResizer;
import prism_convenience.FilesHandle;
import prism_convenience.IconHandle;
import prism_convenience.MenuScroller;
import prism_convenience.PrismMenu;
import prism_convenience.Processing;
import prism_convenience.RequestFocusListener;
import prism_convenience.StringHandle;
import prism_convenience.WindowAppearanceHandle;
import prism_database.Panel_DatabaseManagement;
import prism_project.Panel_Project;
import prism_project.data_process.LinkedList_Databases;

@SuppressWarnings("serial")
public class Prism2Main extends JFrame {
	// Define variables------------------------------------------------------------------------
	private static PrismMenuBar 		prism_Menubar;
	private PrismMenu 					menuFile, menuUtility, menuWindow, menuHelp,
										menuOpenProject;
	private JMenuItem 					newProject, exitSoftware, 			// For MenuFile
										existingProject, 					// For menuOpenProject
										DatabaseManagement, 				// For MenuUtility
										contents, update, contact, about; 	// For MenuMenuHelp
	
	private JMenuItem					setLogo, setTooltips; 	// For menuWindow
	private MenuItem_SetFont 			setFont;				// For menuWindow
	private MenuItem_SetLookAndFeel 	setLookAndFeel;			// For menuWindow
	private MenuItem_SetTransparency 	setTransparency;		// For menuWindow
	private MenuItem_CaptureGUI 		captureGUI;				// For menuWindow
		
	private static String 				prism_version = "PRISM ALPHA 2.1.04";
	private String 						currentProject;
	private static PrismDesktopPane 	prism_DesktopPane;
	private static PrismContentPane 	prism_ContentPane;
	private static Prism2Main 			main;
	private static ComponentResizer 	cr;
	
	private static LinkedList_Databases databases_linkedlist = new LinkedList_Databases();
	
	
	//--------------------------------------------------------------------------------------------------------------------------------
	public static void main(String[] args) {
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		if (gd.isWindowTranslucencySupported(WindowTranslucency.TRANSLUCENT)) {
//			setDefaultLookAndFeelDecorated(true);												// 1: activate this 1 with 2  --> then we can disable 2 lines in the middle
			main = new Prism2Main();
		 	main.setUndecorated(true);		// to help make translucent windows
			main.setOpacity(1f);
//			main.setBackground(new Color(0, 0, 0, 0.0f)); // alpha <1 = transparent;			// 2: activate this 2 with 1  --> then we can disable 2 lines in the middle
			
		 	
			// Need border so cr can work
			Border tempBorder = BorderFactory.createMatteBorder(3, 2, 3, 2, ColorUtil.makeTransparent(Color.BLACK, 255));
//			TitledBorder title = BorderFactory.createTitledBorder(tempBorder, "PRISM Demo Version 1.10");
			main.getRootPane().setBorder(tempBorder);
			
			cr = new ComponentResizer();	// Need resize since if "setDefaultLookAndFeelDecorated(true);" then the top corners cannot be resized (java famous bug?)
			cr.registerComponent(main);
			
			
//			PApplet.main("Processing_Prism");	// Start processing animation
//			try {
//				Runtime.getRuntime().exec("C://Users//Dung Nguyen//Desktop//Planets//application.windows64//Planets.exe", null, new File("C://Users//Dung Nguyen//Desktop//Planets//application.windows64//"));
//			} catch (IOException e) {
//			}
		} 		
	}

	//--------------------------------------------------------------------------------------------------------------------------------
	public Prism2Main() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				Thread exitThread = new Thread() { // Make a thread
					public void run() {
						try {
//							FilesHandle.ExportResource("/test.jar");
							Processing.playAnimation1();
						} catch (Exception e) {
							System.err.println(e.getClass().getName() + ": " + e.getMessage());
						}
					}
				};
				exitThread.start();
				
				for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {	// Set Look & Feel
					if (info.getName().equals("Nimbus")) {
						try {
							UIManager.setLookAndFeel(info.getClassName());
							File memory_file = new File(FilesHandle.get_temporaryFolder() + "/prism_memory.txt");	// Store the last time MAx Memory is saved by users: just an integer number
							String font_name = "Century Schoolbook";
							int font_size = 12;
							try {		
								List<String> list = Files.readAllLines(Paths.get(memory_file.getAbsolutePath()), StandardCharsets.UTF_8);			
								font_name = list.get(2).split("\t")[0];		// space delimited between Font Name and Font Size
								font_size = Integer.parseInt(list.get(2).split("\t")[1]);	// space delimited between Font Name and Font Size
							} catch (Exception e) {
								System.out.println("File prism_memory.txt does not exists");
							} finally {
								UIManager.getLookAndFeelDefaults().put("info", new Color(255, 250, 205));		// Change the ugly yellow color of ToolTip --> lemon chiffon
								UIManager.getLookAndFeelDefaults().put("defaultFont", new Font(font_name, Font.PLAIN, font_size));	// Since the update to eclipse Oxygen and update to java9, 
																																	// this line is required to make it not fail when click File --> Open after changing Look and Feel in Eclise IDE
								WindowAppearanceHandle.setUIFont(new FontUIResource(font_name, Font.PLAIN, font_size));				// Change Font for the current LAF
							}
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
							System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
						}
						SwingUtilities.updateComponentTreeUI(main);
					}
				}		

				
				setIconImage(new ImageIcon(getClass().getResource("/icon_main.png")).getImage());
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				addWindowListener(new WindowAdapter() {@Override public void windowClosing(WindowEvent e){exitPRISM();}});
				prism_DesktopPane = new PrismDesktopPane();
				prism_Menubar = new PrismMenuBar();
				
								
				// Define components: Menubar, Menus, MenuItems----------------------------------
				newProject = new JMenuItem("New");
				menuOpenProject = new PrismMenu("Open");	MenuScroller.setScrollerFor(menuOpenProject, 10, 50, 0, 0);		// 1st number --> in the range, 2nd number --> milliseconds, 3rd number --> on top, 4th number --> at bottom
				exitSoftware = new JMenuItem("Exit");
				DatabaseManagement = new JMenuItem("Database Management");
				DatabaseManagement.setIcon(IconHandle.get_scaledImageIcon(15, 15, "icon_database.png"));
				setLogo = new JMenuItem("Hide Logo");
				setTooltips = new JMenuItem("Hide Tooltips");
				setFont = new MenuItem_SetFont(main);
				setTransparency = new MenuItem_SetTransparency(main);
				setLookAndFeel = new MenuItem_SetLookAndFeel(main, cr);
				captureGUI = new MenuItem_CaptureGUI();
				contents = new JMenuItem("Contents");
				update = new JMenuItem("Check for updates");
				contact = new JMenuItem("Contact us");
				about = new JMenuItem("About");

				menuFile = new PrismMenu("File");
				menuUtility = new PrismMenu("Utility");
				menuWindow = new PrismMenu("Window");
				menuHelp = new PrismMenu("Help");

				
				// Add components: Menubar, Menus, MenuItems----------------------------------
				menuFile.add(newProject);
				menuFile.add(menuOpenProject);
				menuFile.add(exitSoftware);
				menuUtility.add(DatabaseManagement);
				menuWindow.add(captureGUI);
				menuWindow.add(setLogo);
				menuWindow.add(setTooltips);
				menuWindow.add(setTransparency);
				menuWindow.add(setFont);
				menuWindow.add(setLookAndFeel);
				
				menuHelp.add(contents);
				menuHelp.add(update);
				menuHelp.add(contact);
				menuHelp.add(about);

				prism_Menubar.add(menuFile);
				prism_Menubar.add(menuUtility);
				prism_Menubar.add(menuWindow);
				prism_Menubar.add(menuHelp);
				prism_Menubar.addFrameFeatures();
				setJMenuBar(prism_Menubar);	
				
				prism_ContentPane = new PrismContentPane();
		        prism_ContentPane.setLayout(new BorderLayout());	
		        prism_ContentPane.add(prism_DesktopPane);
		        setContentPane(prism_ContentPane);
		        
				// testing transparent, works by activating the below line, but font is in bad quality. 2 System.setProperty lines probably works on old java (before 9) for anti-alias, but not for java 9
//				setBackground(new Color(0, 0, 0, 0)); // alpha <1 = transparent
			 	prism_DesktopPane.setBackground(new Color(0, 0, 0, 0)); // alpha <1 = transparent. If not having this line, it is going to be an ugly blue background picture
				System.setProperty("awt.useSystemAAFontSettings", "on");
				System.setProperty("swing.aatext", "true");
			 	/*
			 	-Dawt.useSystemAAFontSettings=on -Dswing.aatext=true			// --> use for VMargument
			 	*/
				WindowAppearanceHandle.setOpaqueForAll(prism_Menubar, false);
				WindowAppearanceHandle.setOpaqueForAll(prism_ContentPane, false);
				WindowAppearanceHandle.setOpaqueForAll(prism_DesktopPane, false);
				
				pack();
				setLocationRelativeTo(null);
				setVisible(true);
				
				
				// Allow users to modify max heap size on PRISM start up------------------------------------------------------
				OptionPane_Startup.Set_Memory();
				
								
				// Add listener for "Window"-----------------------------------------------------------------
				menuWindow.addMenuListener(new MenuListener() {
					@Override
			        public void menuSelected(MenuEvent e) {						
						//Only allow to change look and feel if No Frame is opened, this is to prevent fail performance of the components after changing look and feel
						if (Prism2Main.get_Prism_DesktopPane().getAllFrames().length ==  0) {
							setLookAndFeel.setEnabled(true);
							setFont.setEnabled(true);
						} else {
							setLookAndFeel.setEnabled(false);
							setFont.setEnabled(false);
						}		
					}

					@Override
					public void menuDeselected(MenuEvent e) {
						
					}

					@Override
					public void menuCanceled(MenuEvent e) {
						
					}
				});
				
				
				// Add listeners "New"------------------------------------------------
//				newProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));	// CTRL on Windows, *** on MAC-OS
				newProject.setMnemonic(KeyEvent.VK_N);
				newProject.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						//JtextField to type name
						JTextField projectName_JTextField = new JTextField(35);
						projectName_JTextField.getDocument().addDocumentListener(new DocumentListener() {
							@Override  
							public void changedUpdate(DocumentEvent e) {
								currentProject = projectName_JTextField.getText().trim();		//Trim spaces at the begin and end of the string
							}
							public void removeUpdate(DocumentEvent e) {
								currentProject = projectName_JTextField.getText().trim();		//Trim spaces at the begin and end of the string
							}
							public void insertUpdate(DocumentEvent e) {
								currentProject = projectName_JTextField.getText().trim();		//Trim spaces at the begin and end of the string
							}
						});
						projectName_JTextField.setText("new_project");		//default name
						projectName_JTextField.addAncestorListener( new RequestFocusListener());	//Set focus (mouse cursor on the JTextField)
						
						
						boolean stop_naming = false;
						String titleText = "Project's name";
						while (stop_naming == false) {
					  		String ExitOption[] = {"OK","Cancel"};
							int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), projectName_JTextField, titleText,
									JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[0]);
							if (response == 0) {
								// Find all the existing projects in the "Projects" folder		
								File[] listOfFiles = FilesHandle.get_projectsFolder().listFiles(new FilenameFilter() {
									@Override
									public boolean accept(File dir, String name) {
										return name.startsWith("");
									}
								});

								List<String> existingName_list = new ArrayList<String>();
								if (listOfFiles != null) {
									for (int i = 0; i < listOfFiles.length; i++) {
										if (listOfFiles[i].isDirectory()) {
											String fileName;
											fileName = listOfFiles[i].getName();
											existingName_list.add(fileName);								
										}
									}		
								}
												
								//if Name is valid and existing projects do not contain this Name
								if (StringHandle.nameIsValid(currentProject) == true && !existingName_list.contains(currentProject) && !currentProject.equals("Database Management")) {
									if (new File(FilesHandle.get_projectsFolder().getAbsolutePath() + "/" + currentProject).mkdir()) {		//try if can create a folder with the existing project name
										create_project_internal_frame(currentProject);		//create new internal frame for this existing project
										stop_naming = true;
									}
								} else {
									titleText = "Name exists or contains special characters. Try a different name:";									
								}
							} else {
								stop_naming = true;
							}
					    }
					}
				});	

				
				// Add listeners "Open"------------------------------------------------
				menuOpenProject.setMnemonic(KeyEvent.VK_O);
				menuOpenProject.addMenuListener(new MenuListener() {
					@Override
			        public void menuSelected(MenuEvent e) {					
						menuOpenProject.removeAll();			// Remove all existing projects
										
						// Find all the existing projects in the "Projects" folder		
						File[] listOfFiles = FilesHandle.get_projectsFolder().listFiles(new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return name.startsWith("");
							}
						});

						if (listOfFiles != null) {
							for (int i = 0; i < listOfFiles.length; i++) {
								if (listOfFiles[i].isDirectory()) {
									String fileName;
									fileName = listOfFiles[i].getName();
									existingProject = new JMenuItem(fileName);
									menuOpenProject.add(existingProject);			// Add all existing projects
									existingProject.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent event) {
											currentProject = fileName;
											create_project_internal_frame(currentProject); // IF the project exists --> creates mean Open
										}
									});
									existingProject.setOpaque(menuOpenProject.isOpaque());
									existingProject.setBackground(menuOpenProject.getBackground());
									existingProject.setForeground(menuOpenProject.getForeground());
								}
							}
						}
					}
					
					@Override
			        public void menuDeselected(MenuEvent e) {
			        }

			        @Override
			        public void menuCanceled(MenuEvent e) {
			        }
				});	
				
				
				// Add listeners "setLogo"-----------------------------------------------------
				setLogo.setIcon(IconHandle.get_scaledImageIcon(15, 15, "icon_main.png"));
				setLogo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK, true));
				setLogo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						if (prism_DesktopPane.getBackgroundImage() != null) {
							prism_DesktopPane.setBackgroundImage(null);
							setLogo.setText("Show Logo");
						} else {
							prism_DesktopPane.process_image();
							setLogo.setText("Hide Logo");
						}	
						Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
				    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
					}
				});
				
				
				// Add listeners "setTooltips"-----------------------------------------------------
				setTooltips.setIcon(IconHandle.get_scaledImageIcon(15, 15, "icon_tip.png"));
				setTooltips.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK, true));
				setTooltips.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						if (ToolTipManager.sharedInstance().isEnabled()) {
							ToolTipManager.sharedInstance().setEnabled(false);
							setTooltips.setText("Show Tooltips");
						} else {
							ToolTipManager.sharedInstance().setEnabled(true);
							setTooltips.setText("Hide Tooltips");
						}						
					}
				});
				
				
				// Disable the functions that are not yet written
				contents.setEnabled(false);
				update.setEnabled(false);
				contact.setEnabled(false);
				contact.setIcon(IconHandle.get_scaledImageIcon(15, 15, "icon_contact.png"));
				
				
				// Add listeners "about"-----------------------------------------------------
				about.setIcon(IconHandle.get_scaledImageIcon(15, 15, "icon_info.png"));
//				about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK, true));
				about.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						new OptionPane_About();					
					}
				});
				
				
				// Add listeners "ExitSoftware"-----------------------------------------------------
				exitSoftware.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK, true));
				exitSoftware.setMnemonic(KeyEvent.VK_E);
				exitSoftware.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						exitPRISM();
					}
				});
				
								
				// Add listeners "DatabaseManagement"------------------------------------------------
				DatabaseManagement.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_DOWN_MASK, true));
				DatabaseManagement.addActionListener(new ActionListener() { // anonymous inner class
					public void actionPerformed(ActionEvent event) {
						boolean is_Database_Management_opened = false;
						
						// display Database Management if it was already created
						for (JInternalFrame i : Prism2Main.get_Prism_DesktopPane().getAllFrames()) {		// Loop all displayed internalFrames
							if (i.getTitle().equals("Database Management")) {
								is_Database_Management_opened = true;
								if (!i.isSelected()) {	 // if it is not selected --> select it
									try {
										i.setSelected(true);
									} catch (PropertyVetoException e1) {
										e1.printStackTrace();
									}
								} else {	// if it is selected --> close
									i.dispose();
								}
							}
						} 
														
						// if not exist, create new internal frame
						if (!is_Database_Management_opened) {
							JInternalFrame DatabaseManagement_Frame = new JInternalFrame("Database Management", true /*resizable*/, true, /*closable*/true/*maximizable*/, true/*iconifiable*/);								
							prism_DesktopPane.add(DatabaseManagement_Frame, BorderLayout.CENTER); // attach internal frame
							DatabaseManagement_Frame.setSize((int) (getWidth()/1.08),(int) (getHeight()/1.19));
							DatabaseManagement_Frame.setLocation((int) ((getWidth() - DatabaseManagement_Frame.getWidth())/2),
																((int) ((getHeight() - DatabaseManagement_Frame.getHeight())/2.75)));	//Set the DatabaseManagement_Frame near the center of the Main frame
							if (Prism2Main.get_Prism_DesktopPane().getSelectedFrame() != null) {	//or Set the DatabaseManagement_Frame near the recently opened JInternalFrame
								DatabaseManagement_Frame.setLocation(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getX() + 25, Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getY() + 25);
							}
							
							// Note: visible first for the JIframe to be selected, pack at the end would be fail for JIframe to be selected (PrismMain.mainFrameReturn().getSelectedFrame = null)
							prism_ContentPane.stop_painting();
							DatabaseManagement_Frame.setVisible(true); // show internal frame					
							DatabaseManagement_Frame.add(new Panel_DatabaseManagement(), BorderLayout.CENTER); // add panel
							DatabaseManagement_Frame.addInternalFrameListener(new InternalFrameListener() {
								public void internalFrameActivated(InternalFrameEvent e) {
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}

								public void internalFrameClosed(InternalFrameEvent e) {
									prism_ContentPane.start_painting();
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}

								public void internalFrameClosing(InternalFrameEvent e) {
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}

								public void internalFrameDeactivated(InternalFrameEvent e) {
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}

								public void internalFrameDeiconified(InternalFrameEvent e) {
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}

								public void internalFrameIconified(InternalFrameEvent e) {
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}

								public void internalFrameOpened(InternalFrameEvent e) {
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}
							});
							
							// This is for the purpose of not repetitive revalidate and repaint in PrismContentPane --> protected void paintComponent
							DatabaseManagement_Frame.addComponentListener(new ComponentListener() {
								@Override
								public void componentResized(ComponentEvent e) {
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}

								@Override
								public void componentMoved(ComponentEvent e) {
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}

								@Override
								public void componentShown(ComponentEvent e) {
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}

								@Override
								public void componentHidden(ComponentEvent e) {
									Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
							    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
								}
							});
						}
					}
				});
							
			} // end public void run()
		}); // end EventQueue.invokeLater
	} // end public Prism_Main
			
	
	//--------------------------------------------------------------------------------------------------------------------------------
	public void create_project_internal_frame(String currentProject) {
		boolean is_currentProject_opened = false;
		
		// display project if it was already created
		for (JInternalFrame i : Prism2Main.get_Prism_DesktopPane().getAllFrames()) {		// Loop all displayed internalFrames
			if (i.getTitle().equals(currentProject)) {
				try {
					i.setSelected(true);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
				is_currentProject_opened = true;
			}
		} 
										
		// if not exist, create new internal frame
		if (!is_currentProject_opened) {
			// create internal frame
			JInternalFrame ProjectInternalFrame = new JInternalFrame(currentProject, true /*resizable*/, true, /*closable*/true/*maximizable*/, true/*iconifiable*/);	
			ProjectInternalFrame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
					
			prism_DesktopPane.add(ProjectInternalFrame, BorderLayout.CENTER); // attach internal frame
			ProjectInternalFrame.setSize((int) (getWidth()/1.08),(int) (getHeight()/1.19));		
			ProjectInternalFrame.setLocation((int) ((getWidth() - ProjectInternalFrame.getWidth())/2),
											((int) ((getHeight() - ProjectInternalFrame.getHeight())/2.75)));	//Set the ProjectInternalFrame near the center of the Main frame
			if (Prism2Main.get_Prism_DesktopPane().getSelectedFrame() != null) {	//or Set the ProjectInternalFrame near the recently opened JInternalFrame
				ProjectInternalFrame.setLocation(Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getX() + 25, Prism2Main.get_Prism_DesktopPane().getSelectedFrame().getY() + 25);
			}
				
			// Note: visible first for the JIframe to be selected, pack at the end would be fail for JIframe to be selected (PrismMain.mainFrameReturn().getSelectedFrame = null)
			prism_ContentPane.stop_painting();
			ProjectInternalFrame.setVisible(true); // show internal frame	
			ProjectInternalFrame.add(new Panel_Project(currentProject), BorderLayout.CENTER); // add panel
			ProjectInternalFrame.addInternalFrameListener(new InternalFrameListener() {
				public void internalFrameActivated(InternalFrameEvent e) {
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}

				public void internalFrameClosed(InternalFrameEvent e) {
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}

				public void internalFrameClosing(InternalFrameEvent e) {
					String ExitOption[] = { "Close", "Cancel" };
					int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(), "Close now?",
							"Close Project", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[0]);
					if (response == 0) {
						ProjectInternalFrame.dispose();
						prism_ContentPane.start_painting();
					}
					
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}

				public void internalFrameDeactivated(InternalFrameEvent e) {
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}

				public void internalFrameDeiconified(InternalFrameEvent e) {
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}

				public void internalFrameIconified(InternalFrameEvent e) {
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}

				public void internalFrameOpened(InternalFrameEvent e) {
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}
			});
			
			// This is for the purpose of not repetitive revalidate and repaint in PrismContentPane --> protected void paintComponent
			ProjectInternalFrame.addComponentListener(new ComponentListener() {
				@Override
				public void componentResized(ComponentEvent e) {
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}

				@Override
				public void componentMoved(ComponentEvent e) {
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}

				@Override
				public void componentShown(ComponentEvent e) {
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}

				@Override
				public void componentHidden(ComponentEvent e) {
					Prism2Main.get_main().revalidate();	// very important to make the background not show lagging from previous paint
			    	Prism2Main.get_main().repaint();		// very important to make the background not show lagging from previous paint
				}
			});
		}
	}
	 
	//--------------------------------------------------------------------------------------------------------------------------------
	public void exitPRISM() {
		String ExitOption[] = {"Exit","Cancel"};
		int response = JOptionPane.showOptionDialog(Prism2Main.get_Prism_DesktopPane(),"Exit now?", "Exit PRISM",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, IconHandle.get_scaledImageIcon(50, 50, "icon_question.png"), ExitOption, ExitOption[0]);
		if (response == 0) {		
//			main.setVisible(false);
//			Thread exitThread = new Thread() { // Make a thread
//				public void run() {
//					try {
//						PApplet.main("Processing_Prism"); // Start processing animation						
//						sleep(10000);
//					} catch (InterruptedException e) {
//					} finally {
//						System.exit(0);
//						this.interrupt();
//					}
//				}
//			};
//			exitThread.start();
			System.exit(0);
		}
		if (response == 1) {
		}
	}
	
	public void minimize() {
		main.setExtendedState(JFrame.ICONIFIED);
	}

	public void restore() {
		if (main.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
			main.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			main.setExtendedState(JFrame.NORMAL);
		}		
	}

	
	//--------------------------------------------------------------------------------------------------------------------------------
	@Override
	public Dimension getPreferredSize() {
		// Check multi-monitor screen resolution
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		
		if (width >= 1600 && height >= 900) {
			return new Dimension(1600, 900);
		} else {
			return new Dimension((int) (width * 0.85), (int) (height * 0.85));
		}
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------
	public static String get_prism_version() {
		return prism_version;
	}
	
	public static PrismMenuBar get_prism_Menubar() {
		return prism_Menubar;
	}
	
	public static PrismDesktopPane get_Prism_DesktopPane() {
		return prism_DesktopPane;
	}
	
	public static PrismContentPane get_prism_ContentPane() {
		return prism_ContentPane;
	} 
	
	public static Prism2Main get_main() {
		return main;
	}

	public static LinkedList_Databases get_databases_linkedlist() {
		return databases_linkedlist;
	}
	
}
