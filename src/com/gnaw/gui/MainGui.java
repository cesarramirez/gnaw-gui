/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gnaw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gnaw.GnawApplication;
import com.gnaw.Profile;
import com.gnaw.discovery.event.BroadcastingEndEvent;
import com.gnaw.discovery.event.BroadcastingEndEventListener;
import com.gnaw.discovery.event.ClientFoundEvent;
import com.gnaw.discovery.event.ClientFoundEventListener;
import com.gnaw.interfaces.DataSourceInterface;
import com.gnaw.models.SharedFile;
import com.gnaw.request.Request;


/**
 * 
 * @author cesar
 */
public class MainGui extends JFrame implements DataSourceInterface, ClientFoundEventListener, BroadcastingEndEventListener {

	private Profile profile = new Profile();
	private GnawApplication application;
	private SharedFile sharedFiles = new SharedFile();

	private ProgressDialogReceiver recv;
	
	/**
	 * Creates new form Main
	 */
	public MainGui() {
		initComponents();
	}

	private void initComponents() {

		jTabbedPane1 = new JTabbedPane();
		jPanel1 = new JPanel();
		jLabel5 = new JLabel();
		jTextField3 = new JTextField();
		jButton1 = new JButton();
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				application.searchFile(jTextField3.getText(), listModelSearch);
			}
		});
		jScrollPane1 = new JScrollPane();
		jSeparator4 = new JSeparator();
		jLabel6 = new JLabel();
		jScrollPane2 = new JScrollPane();
		jPanel2 = new JPanel();
		jLabel1 = new JLabel();
		jSlider1 = new JSlider();
		jSlider1.setMaximum(5);
		jSlider1.setPaintLabels(true);
		jSlider1.setValue(0);
		jSlider1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				String text = "Error";
				switch (jSlider1.getValue()) {
				case 0:
					text = "10  Seconds";
					break;
				case 1:
					text = "30 Seconds";
					break;
				case 2:
					text = "1 Minute";
					break;
				case 3:
					text = "5 Minutes";
					break;
				case 4:
					text = "30 Minutes";
					break;
				case 5:
					text = "Always ON";
					break;
				}
				lbDiscoveryTime.setText(text);
			}
		});
		jSeparator1 = new JSeparator();
		jLabel2 = new JLabel();
		jTextField1 = new JTextField();
		jTextField1.setText(System.getProperty("user.home") + File.separator + "Gnaw");
		jButton2 = new JButton();
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = fc.showOpenDialog(MainGui.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					jTextField1.setText(file.getAbsolutePath());
					System.out.println("Opening: " + file.getName() + ".");
				} else {
					System.out.println("Open command cancelled by user.");
				}
			}
		});
		jToggleButton1 = new JToggleButton();
		jToggleButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jToggleButton1.isSelected()) {
					int seconds = 1;
					switch (jSlider1.getValue()) {
					case 0:
						seconds = 10;
						break;
					case 1:
						seconds = 30;
						break;
					case 2:
						seconds = 60;
						break;
					case 3:
						seconds = 300;
						break;
					case 4:
						seconds = 1800;
						break;
					case 5:
						seconds = -1;
						break;
					}
					application.saveSettings("broadcast_ttl", Integer.toString(seconds));
					application.startBroadcasting(MainGui.this, seconds);
				} else {
					application.stopBroadcasting();
				}
			}
		});

		jToggleButton2 = new JToggleButton();
		jToggleButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jToggleButton2.isSelected()) {
					sharedFiles = SharedFile.load(jTextField1.getText());
					jTextField1.setEnabled(false);
					jButton2.setEnabled(false);
					jToggleButton2.setText("Disable Sharing");
					application.saveSettings("shared_folder", jTextField1.getText());
					shareFiles();
				} else {
					unshareFiles();
					sharedFiles = null;
					jTextField1.setEnabled(true);
					jButton2.setEnabled(true);
					jToggleButton2.setText("Enable Sharing");
					
				}
			}
		});

		jSeparator2 = new JSeparator();
		jLabel3 = new JLabel();
		jLabel4 = new JLabel();
		jTextField2 = new JTextField();
		jTextField2.setText("UNKOWN");
		jSeparator3 = new JSeparator();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jLabel5.setText("Search");

		jButton1.setText("Go");

		jLabel6.setText("Nearby");

		listModelSearch = new DefaultListModel<String>();
		list_1 = new JList(listModelSearch);

		
		listModel = new DefaultListModel<String>();
		list = new JList(listModel);

		tglbtnNewToggleButton = new JToggleButton("Scan");
		tglbtnNewToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tglbtnNewToggleButton.isSelected()) {
					application.startListening(MainGui.this);
					listModel.clear();
				} else {
					application.stopListening();
				}
			}
		});

		btnNewButton = new JButton("Get Profile");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProfileDialog newDialog = new ProfileDialog(application.requestProfile((String) list.getSelectedValue()).getProfile());
				newDialog.setVisible(true);
			}
		});

		btnNewButton_1 = new JButton("View Folder");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SharedFile sharedFiles = application.requestSharedFiles((String) list.getSelectedValue()).getSharedFiles();
				SharedFilesDialog newDialog = new SharedFilesDialog(sharedFiles);
				newDialog.setVisible(true);
			}
		});

		btnNewButton_2 = new JButton("Send File");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dest = (String) list.getSelectedValue();
				SendFileDialog newDialog = new SendFileDialog();
				newDialog.setVisible(true);
				String result = newDialog.getResult();
				if (result != null && !"".equals(result)) {
					SharedFile file = new SharedFile(new File(result));
					application.sendOffer(dest, file);
				}
			}
		});
		
		tglbtnConnect = new JToggleButton("Connect");
		tglbtnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
				if (tglbtnConnect.isSelected()) {								
					if(!application.requestProfile((String) list.getSelectedValue()).getProfile().isNode()){
						JOptionPane.showMessageDialog(MainGui.this, "This user is not part of a network");
						tglbtnConnect.setSelected(false);
					}else{
						profile.setNode(true);
						String protocol = application.getProtocol();
						application.joinChordNetwork(protocol, (String) list.getSelectedValue(), GnawApplication.DEFAULT_MASTER_PORT);
						tglbtnConnect.setText("Disconnect");						

//						if(jToggleButton2.isSelected() && sharedFiles != null){
//							// insert files into Chord network
//							shareFiles();
//						}
						
					}
				} else {

					if(sharedFiles != null){
						// remove files from Chord network
						unshareFiles();
					}

					application.stopChordNetwork();
					tglbtnConnect.setText("Join");
					profile.setNode(false);
				}

			}
		});
		
		JSeparator separator = new JSeparator();
		
		btnDownload = new JButton("Download");

		GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
		jPanel1Layout.setHorizontalGroup(
			jPanel1Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup()
							.addComponent(jSeparator4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(tglbtnConnect, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnNewButton, 0, 0, Short.MAX_VALUE)
										.addComponent(btnNewButton_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(tglbtnNewToggleButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
										.addComponent(btnNewButton_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
								.addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
									.addComponent(jLabel5)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jTextField3, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jButton1))
								.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)))
						.addComponent(jLabel6)
						.addComponent(separator, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
						.addComponent(btnDownload, Alignment.TRAILING))
					.addContainerGap())
		);
		jPanel1Layout.setVerticalGroup(
			jPanel1Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel5)
						.addComponent(jButton1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
						.addComponent(jSeparator4, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDownload)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jLabel6)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
						.addGroup(jPanel1Layout.createSequentialGroup()
							.addComponent(btnNewButton_2)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tglbtnConnect)
							.addPreferredGap(ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
							.addComponent(tglbtnNewToggleButton))
						.addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		jScrollPane1.setViewportView(list_1);
		jScrollPane2.setViewportView(list);
		jPanel1.setLayout(jPanel1Layout);

		jTabbedPane1.addTab("Share", jPanel1);

		jLabel1.setText("Discovery");

		jLabel2.setText("Shared Folder");

		jButton2.setText("Explore");

		jToggleButton1.setText("Enable");

		jToggleButton2.setText("Enable Sharing");
		jToggleButton2.setToolTipText("");

		jLabel3.setText("Profile");

		jLabel4.setText("Name:");

		lbDiscoveryTime = new JLabel("10 Seconds");

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				profile.setName(jTextField2.getText());
				application.saveSettings("profile_name", profile.getName());
			}
		});
		
		JLabel lblNetwork = new JLabel("Network");
		
		final JToggleButton tglbtnCreateNetwork = new JToggleButton("Create network");
		tglbtnCreateNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (tglbtnCreateNetwork.isSelected()) {
					tglbtnCreateNetwork.setText("Disable network");
					application.createChordNetwork();
					profile.setNode(true);
				} else {
					tglbtnCreateNetwork.setText("Create network");
					profile.setNode(false);
				}
				
				
			}
		});

		GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
		jPanel2Layout.setHorizontalGroup(
			jPanel2Layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(jPanel2Layout.createSequentialGroup()
					.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
						.addComponent(jSeparator1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 471, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
							.addContainerGap()
							.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
								.addComponent(jSeparator3, GroupLayout.PREFERRED_SIZE, 459, Short.MAX_VALUE)
								.addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 459, Short.MAX_VALUE)
								.addComponent(jSlider1, GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
								.addGroup(jPanel2Layout.createSequentialGroup()
									.addGap(12)
									.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jTextField2, GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnSave))
								.addComponent(jLabel1)
								.addComponent(jLabel2)
								.addComponent(jLabel3)
								.addGroup(jPanel2Layout.createSequentialGroup()
									.addGap(12)
									.addComponent(lbDiscoveryTime)
									.addPreferredGap(ComponentPlacement.RELATED, 285, Short.MAX_VALUE)
									.addComponent(jToggleButton1))
								.addGroup(jPanel2Layout.createSequentialGroup()
									.addComponent(jTextField1, GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jButton2))))
						.addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
							.addContainerGap(330, Short.MAX_VALUE)
							.addComponent(jToggleButton2))
						.addGroup(jPanel2Layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNetwork))
						.addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
							.addContainerGap(304, Short.MAX_VALUE)
							.addComponent(tglbtnCreateNetwork)))
					.addContainerGap())
		);
		jPanel2Layout.setVerticalGroup(
			jPanel2Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jLabel1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jSlider1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lbDiscoveryTime)
						.addComponent(jToggleButton1))
					.addGap(12)
					.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jLabel2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jButton2))
					.addGap(12)
					.addComponent(jToggleButton2)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jLabel3)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel4)
						.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSave))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(jSeparator3, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNetwork)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tglbtnCreateNetwork)
					.addContainerGap(72, Short.MAX_VALUE))
		);
		jPanel2.setLayout(jPanel2Layout);

		jTabbedPane1.addTab("Settings", jPanel2);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE));
		getContentPane().setLayout(groupLayout);

		pack();

		this.application = new GnawApplication(this);

		String broadcastttl = this.application.retrieveSettings("broadcast_ttl");
		if (broadcastttl != null) {
			int value = -1;
			switch (Integer.parseInt(broadcastttl)) {
			case 10:
				value = 0;
				break;
			case 30:
				value = 1;
				break;
			case 60:
				value = 2;
				break;
			case 300:
				value = 3;
				break;
			case 1800:
				value = 4;
				break;
			case -1:
				value = 5;
				break;
			}

			this.jSlider1.setValue(value);
		}

		String shared = this.application.retrieveSettings("shared_folder");
		if (shared != null) {
			this.jTextField1.setText(shared);
		}

		String profile = this.application.retrieveSettings("profile_name");
		if (profile != null) {
			this.jTextField2.setText(profile);
		}
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainGui().setVisible(true);
			}
		});
	}

	private JButton jButton1;
	private JButton jButton2;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JSeparator jSeparator1;
	private JSeparator jSeparator2;
	private JSeparator jSeparator3;
	private JSeparator jSeparator4;
	private JSlider jSlider1;
	private JTabbedPane jTabbedPane1;
	private JTextField jTextField1;
	private JTextField jTextField2;
	private JTextField jTextField3;
	private JToggleButton jToggleButton1;
	private JToggleButton jToggleButton2;
	private JLabel lbDiscoveryTime;
	private JToggleButton tglbtnNewToggleButton;
	private JList list;
	private DefaultListModel listModel;
	private DefaultListModel listModelSearch;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JToggleButton tglbtnConnect;
	private JList list_1;
	private JButton btnDownload;

	@Override
	public Profile getProfile() {
		profile.setName(jTextField2.getText());
		return profile;
	}

	@Override
	public void ClientFoundEventOccurred(ClientFoundEvent evt) {
		listModel.addElement(evt.getSource());
	}

	@Override
	public SharedFile getSharedFiles() {
		return this.sharedFiles;
	}

	@Override
	public boolean postMessage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean postOffer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean postOfferResponse() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean postSearchRequest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean postSearchResult() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void BroadcastingEndEventOccurred(BroadcastingEndEvent evt) {
		jToggleButton1.setSelected(false);
	}

	@Override
	public boolean deliverMessage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deliverOffer(Request request) {
		try {
			AcceptRequestDialog newDialog = new AcceptRequestDialog(application, request);
			newDialog.setVisible(true);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deliverOfferResponse(Request request) {
		try {
			ProgressDialogSender newDialog = new ProgressDialogSender(request, this.application);
			newDialog.setVisible(true);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deliverSearchRequest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deliverPushRequest(Request request) {
		try {
			recv = new ProgressDialogReceiver(request, this.application);
			recv.setVisible(true);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void setProgress(int status) {
		recv.setProgress(status);
	}
	
	/**
	 * Share all files in shareFiles.
	 */
	private void shareFiles() {
		for (String filename : sharedFiles.getSharedFilenames()) {
			application.shareFile(filename);
		}
	}
	
	/**
	 * Unshare all files in sharedFiles.
	 */
	private void unshareFiles() {
		for (String filename : sharedFiles.getSharedFilenames()) {
			application.unshareFile(filename);
		}
	}
}
