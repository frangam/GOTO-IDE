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
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.tree.DefaultMutableTreeNode;

import com.fgarmo.utilities.LineHighlighter;

public class TextEditorTab extends JScrollPane {
	/******************************************
    * Constants
    ******************************************/
	public static final int TOTAL_STOP_TABS = 1000;
	
	/******************************************
    * Fields
    ******************************************/
	private JTextPane textPane;
	private DefaultMutableTreeNode node; //the node that opens this tb
	private File file; //the linked file with this text editor tab
	
	/******************************************
    * Getters & Setters
    ******************************************/
	public JTextPane getTextPane() {
		return textPane;
	}
	public void setTextPane(JTextPane textPane) {
		this.textPane = textPane;
	}
	public File getFile() {
		return file;
	}
	public DefaultMutableTreeNode getNode() {
		return node;
	}
	public void setNode(DefaultMutableTreeNode node) {
		this.node = node;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getContent(){
		return this.textPane.getText();
	}
	
	/******************************************
    * Constructors
    ******************************************/
	public TextEditorTab(File file, DefaultMutableTreeNode node) {
		this.node = node;
		this.file = file;
		textPane = new JTextPane();
		setViewportView(textPane);
		
		TabStop[] tabs = new TabStop[TOTAL_STOP_TABS];
		for(int i=0; i<TOTAL_STOP_TABS; i++)
			tabs[i] = new TabStop(20*(i+1), TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
		
	    
        TabSet tabset = new TabSet(tabs);
        
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
        StyleConstants.TabSet, tabset);
        
        textPane.setParagraphAttributes(aset, false);
        
        textPane.setText(getFileContent());
       
        // add our custom text line number
        TextLineNumber tln = new TextLineNumber(textPane, 4, new Color(0.5f, 0.2f, 1, 1f));
        setRowHeaderView(tln);
        
        //create our custom line highlighter
        new LineHighlighter(textPane, new Color(0.2f, 0.8f, 1, 0.15f));
        
        //init the cursor at the top
        textPane.setCaretPosition(0);
        
        active();
	}
	
	/******************************************
    * Public Methods
    ******************************************/	
	//from: http://stackoverflow.com/questions/3402735/what-is-simplest-way-to-read-a-file-into-string
	public String getFileContent(){
		String content = "";
		
		try {
			content = Files.lines(Paths.get(file.getAbsolutePath())).collect(Collectors.joining("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
	public void active(){
		textPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        textPane.getCaret().setVisible(true);
        textPane.requestFocus();
        
	}
}