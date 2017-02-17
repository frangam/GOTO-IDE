/*
 * Copyright (C) 2017 Francisco Manuel Garcia Moreno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.fgarmo.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.fgarmo.plgoto.GOTO;
import com.fgarmo.utilities.UnderlineHighlighter;
import com.fgarmo.utilities.WordSearcher;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainView extends JFrame {

	private JPanel contentPane;
	private JTextField tfInputValues;
	private JTree filesTree;
	private List<String> openedFiles = new ArrayList<String>();
	private JTextPane tfFileTextEditor;
	private JTextPane tfConsole;
	public static String word;
	public static Highlighter highlighter = new UnderlineHighlighter(null);
	private JTextField tfSearch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1024, 768);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		
		JSplitPane splitPane = new JSplitPane();
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		splitPane.setLeftComponent(panel_2);
		
		filesTree = new JTree();
		filesTree.setRootVisible(false);
		filesTree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("JTree") {
				{
				}
			}
		));
		
				GroupLayout gl_panel_2 = new GroupLayout(panel_2);
				gl_panel_2.setHorizontalGroup(
					gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addContainerGap()
							.addComponent(filesTree, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
							.addContainerGap())
				);
				gl_panel_2.setVerticalGroup(
					gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addContainerGap()
							.addComponent(filesTree, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
							.addContainerGap())
				);
				panel_2.setLayout(gl_panel_2);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(12)
					.addComponent(tabbedPane_1, GroupLayout.DEFAULT_SIZE, 996, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(12)
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 981, Short.MAX_VALUE)
					.addGap(21))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(tabbedPane_1, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
		);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabbedPane);
		
		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("New tab", null, scrollPane, null);
		
		tfFileTextEditor = new JTextPane();
		scrollPane.setViewportView(tfFileTextEditor);
		final WordSearcher searcher = new WordSearcher(tfFileTextEditor);
		
		tfFileTextEditor.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent evt) {
				searcher.search(word);
		    }
	
		    public void removeUpdate(DocumentEvent evt) {
		    	searcher.search(word);
		    }
	
		    public void changedUpdate(DocumentEvent evt) {
		    
		    }
	    });
		
		JScrollPane tabConsole = new JScrollPane();
		tabbedPane_1.addTab("Console", null, tabConsole, null);
		
		tfConsole = new JTextPane();
		tabConsole.setViewportView(tfConsole);
		
		JButton btnOpenFile = new JButton("");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openNewFile();
			}
		});
		btnOpenFile.setToolTipText("Open file");
		btnOpenFile.setIcon(new ImageIcon(MainView.class.getResource("/com/fgarmo/resources/images32/1487297603_folder-blue.png")));
		toolBar.add(btnOpenFile);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setToolTipText("New file");
		btnNewButton.setIcon(new ImageIcon(MainView.class.getResource("/com/fgarmo/resources/images32/1487297659_file-code.png")));
		toolBar.add(btnNewButton);
		
		JButton button = new JButton("");
		button.setToolTipText("Save");
		button.setIcon(new ImageIcon(MainView.class.getResource("/com/fgarmo/resources/images32/1487297525_floppy.png")));
		toolBar.add(button);
		
		JToolBar toolBar_1 = new JToolBar();
		toolBar_1.setFloatable(false);
		toolBar.add(toolBar_1);
		
		JButton btnRun = new JButton("");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run();
			}
		});
		toolBar_1.add(btnRun);
		btnRun.setIcon(new ImageIcon(MainView.class.getResource("/com/fgarmo/resources/images32/1487299231_Play Green Button.png")));
		
		JLabel label = new JLabel("   ");
		toolBar_1.add(label);
		
		JLabel lblInput = new JLabel("Input values:");
		toolBar_1.add(lblInput);
		
		tfInputValues = new JTextField();
		
		
		
		
		

		
		toolBar_1.add(tfInputValues);
		tfInputValues.setColumns(10);
		
		JLabel label_1 = new JLabel("   ");
		toolBar_1.add(label_1);
		
		JLabel lblSearch = new JLabel("Search:");
		toolBar_1.add(lblSearch);
		
		tfSearch = new JTextField();
		
		toolBar_1.add(tfSearch);
		tfSearch.setColumns(10);
		
		
		
		tfSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				word = tfSearch.getText().trim();
		        int offset = searcher.search(word);
		        if (offset != -1) {
		          try {
		            tfFileTextEditor.scrollRectToVisible(tfFileTextEditor
		                .modelToView(offset));
		          } catch (BadLocationException ex) {
		        	  
		          }
		        }
			}
		});
		contentPane.setLayout(gl_contentPane);
	}
	
	private void run(){
		GOTO myGOTO = new GOTO();
		try{
			myGOTO.run(openedFiles.toArray(new String[openedFiles.size()]));
		}
		catch(RuntimeException ex){
			showErrorInConsole(ex.getMessage());
		}
	}
	
	private void openNewFile(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int res = fileChooser.showOpenDialog(getParent());
		
		if (res == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String fileName = selectedFile.getName();
			String fileAbsPath = selectedFile.getAbsolutePath();
			openedFiles.add(fileAbsPath);
			showResultInConsole("File opened: " + fileAbsPath);
			
		    System.out.println("Selected file: " + fileAbsPath);
		    
		    //add node
		    DefaultTreeModel model = (DefaultTreeModel) filesTree.getModel();
		    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		    model.insertNodeInto(new DefaultMutableTreeNode(fileName), root, root.getChildCount());
		    model.reload();
		}
	}
	
	private void showErrorInConsole(String message){
		sM(tfConsole, message, Color.red);
	}
	
	private void showResultInConsole(String message){
		sM(tfConsole, message, Color.black);
	}
	
	
	
	private void clearJTree(JTree tree){
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload();
	}
	
	public void sM(JTextPane textPane, String s, Color c) {
        // bad: instiantiates a new AttributeSet object on each call
		SimpleAttributeSet aset = new SimpleAttributeSet();
		StyleConstants.setForeground(aset, c);
		
		int len = textPane.getText().length();
		textPane.setCaretPosition(len); // place caret at the end (with no selection)
		textPane.setCharacterAttributes(aset, false);
		
		if(len == 0)
			textPane.replaceSelection(s); // there is no selection, so inserts at caret
		else
			textPane.replaceSelection("\n"+s);
	}
}
