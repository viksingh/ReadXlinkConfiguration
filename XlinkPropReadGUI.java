package org.saki.XlinkPropRead;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;



public class XlinkPropReadGUI  extends JPanel
implements ActionListener  {
    final static String BUTTONPANEL = "Select File for Processing";
    final static String TEXTPANEL = "User Settings";
    final static int extraWindowWidth = 100;
    JFileChooser fc;
    static private final String newline = "\n";    
    JButton openButton;
	private static final String PROPERTIES = ".properties";
	private static final String XL0_ACTIVE_ADAPTER_PATH = "//f$//sap//xlink//XL0//Data/";
	private static final String XL0_PATH = "//f$//sap//xlink//XL0//Configuration/";
	private static final String ATTR_VAL_TO_CHECK = "Xhttps://ebswebdisp.nestle.com:56801/XIAxisAdapter/MessageServlet";
    JTextField txtDomain;
    JTextField txtUser;
    JTextField txtPassword;
    JTextField txtAttribute;

    String domain;
    String user;
    String password;
    String attribute;
    
    @SuppressWarnings("serial")
	public void addComponentToPane(Container pane) {
    	
    	KeyListener keyListener = new KeyListener() {
    	      public void keyPressed(KeyEvent keyEvent) {
    	        printIt("Pressed", keyEvent);
    	      }

    	      public void keyReleased(KeyEvent keyEvent) {
    	        printIt("Released", keyEvent);
    	      }

    	      public void keyTyped(KeyEvent keyEvent) {
    	        printIt("Typed", keyEvent);
    	      }

    	      private void printIt(String title, KeyEvent keyEvent) {
    	        int keyCode = keyEvent.getKeyCode();
    	        String keyText = KeyEvent.getKeyText(keyCode);
    	        
    	        domain = txtDomain.getText(); 
    	        user = txtUser.getText();
    	        password = txtPassword.getText();
    	        attribute = txtAttribute.getText();
    	        
    	      }
    	    };
    	
    	
        JTabbedPane tabbedPane = new JTabbedPane();

        
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
        openButton = new JButton("Upload list of servers...");
        openButton.addActionListener(this);        
        
        //Create the "cards".
        JPanel card1 = new JPanel() {
            //Make the panel wider than it really needs, so
            //the window's wide enough for the tabs to stay
            //in one row.
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        card1.add(openButton);

        JPanel card2 = new JPanel(new GridLayout(0,1,2,2));
        
        card2.add(new Label("DOMAIN"));
        txtDomain = new JTextField("NESTLE", 30); 
        card2.add(txtDomain);
        txtDomain.addKeyListener(keyListener);
        
        card2.add(new Label("User"));
        txtUser = new JTextField("adm2singh4", 30);
        card2.add(txtUser);
        txtUser.addKeyListener(keyListener);

        card2.add(new Label("Password"));
        txtPassword = new JTextField("password", 30);
        card2.add(txtPassword);
        txtPassword.addKeyListener(keyListener);


        card2.add(new Label("Attribute"));
        txtAttribute = new JTextField("XI.XFileTargetURL", 30);
        card2.add(txtAttribute);
        txtAttribute.addKeyListener(keyListener);

        tabbedPane.addTab(BUTTONPANEL, card1);
        tabbedPane.addTab(TEXTPANEL, card2);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Xlink Remote Properties Read");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        XlinkPropReadGUI demo = new XlinkPropReadGUI();
        demo.addComponentToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

	@Override
	public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
        	
            int returnVal = fc.showOpenDialog(XlinkPropReadGUI.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                
                //This is where a real application would open the file.
//                log.append("Opening: " + file.getName() + "." + newline);
                try {
                	System.out.println("File is : " + file);
					InputStream is1 = new FileInputStream(file);


					BufferedReader br1 = null;
					br1 = new BufferedReader(new InputStreamReader(is1));
					String line1 = "";

					while ((line1 = br1.readLine()) != null) {

						System.out.println("***Checking server: " + line1.trim());

						List<String> ActiveAdapterList = new ArrayList<String>();
						String url = "smb://" + line1.trim() + XL0_PATH;
						String activeurl = "smb://" + line1.trim()
								+ XL0_ACTIVE_ADAPTER_PATH;

						NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
								domain, user, password);
						SmbFile dirAdapters = new SmbFile(url, auth);
						SmbFile dirActiveAdapters = new SmbFile(activeurl, auth);

						try {

							retriveActiveAdapters(ActiveAdapterList, activeurl,
									dirActiveAdapters);

							for (SmbFile f : dirAdapters.listFiles()) {
								if (f.isFile()
										&& (f.getName().contains("SEND") || f.getName()
												.contains("SND"))
										&& (!f.getName().contains("TEMPLATE"))
										&& (!f.getName().contains("HOUSEKEEPING"))) {

									InputStream is = f.getInputStream();

									BufferedReader reader = null;
									reader = new BufferedReader(new InputStreamReader(is));
									String line = reader.readLine();
									while (line != null) {

										if (line.contains(attribute) && !line.contains(ATTR_VAL_TO_CHECK)) {

											String fileName = f.getName().replace(
													PROPERTIES, "");

											if (StringExistsInActiveAdapterList(fileName,
													ActiveAdapterList)) {
												System.out.println(fileName);
												System.out.println(line);
												
											}
											break;
										}
										try {
											line = reader.readLine();
										} catch (Exception e1) {
										}
									}

								}
							}
						} catch (Exception e1) {
							System.out.println("Error connecting server: " + line1.trim());
						}

					}					
					
					
					
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
            } else {
//                log.append("Open command cancelled by user." + newline);
            }
//            log.setCaretPosition(log.getDocument().getLength());
        } 		
		
		
	}


		private static void retriveActiveAdapters(List<String> ActiveAdapterList,
				String activeurl, SmbFile dirActiveAdapters) throws SmbException {
			
		try{	
			for (SmbFile fileactiveAdapter : dirActiveAdapters.listFiles()) {
				String activeAdapterName = fileactiveAdapter.toString();
				if (fileactiveAdapter.isFile()
						&& (activeAdapterName.contains("SEND") || activeAdapterName
								.contains("SND"))) {
					ActiveAdapterList.add(activeAdapterName.trim().replace(
							activeurl, "").toUpperCase().replace(".DAT", "").replace("_ID", ""));
				}

			}
		} catch (Exception e){
			System.out.println("Error retrieving active adapters: " + activeurl);		
		}
		
		}

		static Boolean StringExistsInActiveAdapterList(String inputString,
				List<String> activeAdapterList2) {

			for (String str : activeAdapterList2) {
				if (str.trim().equals(inputString))
					return true;
			}
			return false;

		}

}
