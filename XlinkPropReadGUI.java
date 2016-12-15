package org.saki.XlinkPropRead;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
	private static final String ATTR_VAL_TO_CHECK = "Xhttps://server:56801/XIAxisAdapter/MessageServlet";
    JTextField txtDomain;
    JTextField txtUser;
    JTextField txtPassword;
    JTextField txtAttribute;
    JLabel lblFilePath;    
    JPasswordField passwordField;
    JTabbedPane tabbedPane;
    List<XlinkData> AdapterPropertiesList = new ArrayList<XlinkData>();
    static JFrame frame ;
    static JFrame resultFrame ;
    JPanel card1;
    JPanel card2;
    JPanel card3;
    static Container globalPane;
    JCheckBox senderAdapter;
    JCheckBox receiverAdapter;
    JButton processButton;
    File file;
    
    String domain;
    String user;
    String password;
    String attribute;
    Boolean senderSelected;
    Boolean receiverSelected;
    
    Boolean resultPane = false;
    
    
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
    	        password = passwordField.getText();
    	        attribute = txtAttribute.getText();
    	        

    	        
    	      }
    	    };
    	
    	
        tabbedPane = new JTabbedPane();

        
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setSelectedFile(new File("C:\\Temp\\xlinklist_small.txt"));
        
        openButton = new JButton("Upload list of servers...");
        openButton.setPreferredSize(new Dimension(400, 40));
        openButton.addActionListener(this);        
        card1 = new JPanel(new GridLayout(0,1,45,65));
        card1.add(openButton);
        
        lblFilePath = new JLabel("");
        card1.add(lblFilePath);
        
        processButton = new JButton("Process");
        processButton.addActionListener(this);              
        card1.add(processButton);
        
        

        card2 = new JPanel(new GridLayout(0,1,2,2));
        
        card2.add(new Label("Domain"));
        txtDomain = new JTextField("DOMAIN", 30); 
        card2.add(txtDomain);
        txtDomain.addKeyListener(keyListener);
        
        card2.add(new Label("User"));
        txtUser = new JTextField("user", 30);
        card2.add(txtUser);
        txtUser.addKeyListener(keyListener);

        card2.add(new Label("Password"));
        passwordField = new JPasswordField("", 11);
        passwordField.setEchoChar('*');
        card2.add(passwordField);
        passwordField.addKeyListener(keyListener);

        

        card2.add(new Label("Attribute"));
        txtAttribute = new JTextField("XI.XFileTargetURL", 30);
        card2.add(txtAttribute);
        txtAttribute.addKeyListener(keyListener);
        
        card2.add(new Label("Adapter Type"));
        
        senderAdapter = new JCheckBox("Sender");
        senderAdapter.setMnemonic(KeyEvent.VK_C);
        senderAdapter.setSelected(true);
        card2.add(senderAdapter);
        senderSelected = true;
        
        senderAdapter.addActionListener(this);
        
        
        receiverAdapter = new JCheckBox("Receiver");
        receiverAdapter.setMnemonic(KeyEvent.VK_C);
        receiverAdapter.setSelected(false);
        card2.add(receiverAdapter);
        receiverSelected = false;
        
        receiverAdapter.addActionListener(this);

        
        
        tabbedPane.addTab(TEXTPANEL, card2);
        tabbedPane.addTab(BUTTONPANEL, card1);
        globalPane.add(tabbedPane, BorderLayout.CENTER);
        
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Xlink Remote Properties Read");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        XlinkPropReadGUI xlinkProcessing = new XlinkPropReadGUI();
        globalPane = frame.getContentPane();
        xlinkProcessing.addComponentToPane(globalPane);

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
		

        if (e.getSource() == senderAdapter) {

        	
        	print("SENDER" + e.getActionCommand() );
        	AbstractButton abstractButton = (AbstractButton) e.getSource();    
        	senderSelected = abstractButton.getModel().isSelected();
                     	
        	
        
        }
		
        else        if (e.getSource() == receiverAdapter) {
        	
        	print("RECEIVER"+ e.getActionCommand());;
        	AbstractButton abstractButton = (AbstractButton) e.getSource();    
       	 	receiverSelected = abstractButton.getModel().isSelected();
         	
        }

        
        
        //Handle open button action.
        else        if (e.getSource() == openButton) {
        	
            int returnVal = fc.showOpenDialog(XlinkPropReadGUI.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                lblFilePath.setText(file.getAbsolutePath());
                //Display the window.
                frame.pack();
                frame.setVisible(true);
                
                
            }} else if ( e.getSource() == processButton ){
            	
            	

            	try {

					InputStream is1 = new FileInputStream(file);


					BufferedReader br1 = null;
					br1 = new BufferedReader(new InputStreamReader(is1));
					String line1 = "";

					while ((line1 = br1.readLine()) != null) {
						String server = line1.trim();

						retrieveRemoteXlinkValues( server);
					}					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
                	
            	



        		
		resultPane = true;
		frame.getContentPane().remove(frame);
		
		globalPane.remove(tabbedPane);
		SwingUtilities.updateComponentTreeUI(frame);
		
		
		
		String col[] = {"Server" , "Adapter" , "Attribute"};
		DefaultTableModel tableModel = new DefaultTableModel(col, 0);
		
		final JTable table = new JTable(tableModel);
		for (int i = 0; i < AdapterPropertiesList.size(); i++){
			String localServer = AdapterPropertiesList.get(i).getServer();
			String localAdapter = AdapterPropertiesList.get(i).getAdapter();
			String localAttribute = AdapterPropertiesList.get(i).getAttribute1();
			
			Object data[] = {localServer,localAdapter,localAttribute};
			tableModel.addRow(  data);
			
		}
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        
        
        table.getColumnModel().getColumn(0).setMinWidth(10);
        table.getColumnModel().getColumn(1).setMinWidth(150);
        table.getColumnModel().getColumn(2).setMinWidth(290);
        
        
        int height = 25 * ( AdapterPropertiesList.size() + 1 );
        frame.setPreferredSize(new Dimension(700,height));
        frame.pack();
        
        
		JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem item = new JMenuItem("Export Data");
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	ExporttoExcel(table,new File("C:\\Temp\\xlink_properties.csv"));
            	
            	
            	
            }
        });
        menu.add(item);
        frame.setJMenuBar(menuBar);


		
		JScrollPane scrollPane = new JScrollPane(table);		
		frame.add(scrollPane, BorderLayout.CENTER);
	    frame.setSize(400, 150);
	    frame.pack();
	    frame.setVisible(true);	
	    
            }	    
        } 	
                    
	
	
	 private void print(String string) {
//		 System.out.println(string);
		
	}

	private void retrieveRemoteXlinkValues( String server)
			throws MalformedURLException {
		List<String> ActiveAdapterList = new ArrayList<String>();
		
		for (int i = 0;i<2;i++){
		
		String XLPATH = "//f$//sap//xlink//XL"+i+"//Configuration/";
		String url = "smb://" + server + XLPATH;
		String activeurl = "smb://" + server + "//f$//sap//xlink//XL"+i+"//Data/";

		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
				domain, user, password);
		

		try {

			SmbFile dirAdapters = new SmbFile(url, auth);
			SmbFile dirActiveAdapters = new SmbFile(activeurl, auth);			
			
			retriveActiveAdapters(ActiveAdapterList, activeurl,
					dirActiveAdapters);

			for (SmbFile f : dirAdapters.listFiles()) {
				if (senderSelected && isSender(f)
				|| receiverSelected && 	isReceiver(f)
				) {

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
								
				XlinkData adapterProperty = new XlinkData();												
				adapterProperty.setServer(server);
				adapterProperty.setAdapter(fileName);
				adapterProperty.setAttribute1(line.replace(attribute+"=",""));

				AdapterPropertiesList.add(adapterProperty);
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

			
		}
		
		}
		
		
	}

	private boolean isSender(SmbFile f) throws SmbException {
		return f.isFile()
				&& (f.getName().contains("SEND") || f.getName()
						.contains("SND"))
				&& (!f.getName().contains("TEMPLATE"))
				&& (!f.getName().contains("HOUSEKEEPING"));
	}

	private boolean isReceiver(SmbFile f) throws SmbException {
		return f.isFile()
				&& (f.getName().contains("RCV") || f.getName()
						.contains("RECEIVER"))
				&& (!f.getName().contains("RECV"))
				&& (!f.getName().contains("RCVR"));
	}
	

		private void retriveActiveAdapters(List<String> ActiveAdapterList,
				String activeurl, SmbFile dirActiveAdapters) throws SmbException {
			
		try{	
			for (SmbFile fileactiveAdapter : dirActiveAdapters.listFiles()) {
				String activeAdapterName = fileactiveAdapter.toString();
				if (senderSelected && isSender(fileactiveAdapter)
						|| receiverSelected && 	isReceiver(fileactiveAdapter)
						)
				
				{
					ActiveAdapterList.add(activeAdapterName.trim().replace(
							activeurl, "").toUpperCase().replace(".DAT", "").replace("_ID", ""));
				}

			}
		} catch (Exception e){
		
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

		public void ExporttoExcel(JTable table, File file){
		    
			TableModel model = table.getModel();
	    	JFileChooser chooser = new JFileChooser();
	        chooser.setCurrentDirectory(new File("C://Temp/"));
	        int retrival = chooser.showSaveDialog(null);		
	        if (retrival == JFileChooser.APPROVE_OPTION) {
	        	
                try {
					FileWriter fw = new FileWriter(chooser.getSelectedFile()+".txt");
	                for(int i = 0; i < model.getColumnCount(); i++){
			            fw.write(model.getColumnName(i) + "\t");
			        }		              
		            fw.write("\n");
		            
			        for(int i=0; i< model.getRowCount(); i++) {
			            for(int j=0; j < model.getColumnCount(); j++) {
			                fw.write(model.getValueAt(i,j).toString()+",");
			            }
			            fw.write("\n");
			        }
	                
			        fw.close();					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	
	        
	        }
			
			
		     
		                
		                
		                
		        }
		
		
}
		    
		    	

		       



		    
