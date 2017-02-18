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
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.tree.DefaultMutableTreeNode;

import com.fgarmo.utilities.Constants;
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
		textPane.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");

		
		
		final Font currFont = textPane.getFont();
		textPane.setFont(new Font("monospaced", currFont.getStyle(), currFont.getSize()));
	    
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
        
        textPane.getDocument().addDocumentListener(new DocumentListener() 
        {
            public void changedUpdate(DocumentEvent event) {}

            public void insertUpdate(DocumentEvent event)
            {
                checkForHighlights();
            }

            public void removeUpdate(DocumentEvent event)
            {
                checkForHighlights();
            }
        });
        
        //check syntax first time
        checkForHighlights();
			
        // add our custom text line number
        TextLineNumber tln = new TextLineNumber(textPane, 4, Constants.COLOR_TEXT_LINE_NUMBER_HIGHTLIGH);
        setRowHeaderView(tln);
        
        //create our custom line highlighter
        new LineHighlighter(textPane, Constants.COLOR_LINE_HIGHTLIGH);
        
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
	
//	public void highLight(){
//		Highlighter highlighter = textPane.getHighlighter();
//        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
//        List<String> keyWords = Arrays.asList("<-", "GOTO", "IF");
//        String content = getContent();
//        
//        for(String key : keyWords){
//	        int p0 = content.indexOf(key);
//	        int p1 = p0 + key.length();
//	        
//	        try {
//
//				
//				while ( p0 >= 0 ) {
//				    int len = key.length();
//				    highlighter.addHighlight(p0, p1, DefaultHighlighter.DefaultPainter);
//				    p0 = content.indexOf(key, p0+len);
//				}
//				
//			} catch (BadLocationException e) {
//				e.printStackTrace();
//			}
//        }
//	}
	
//	public Pattern matcher = Pattern.compile("GOTO|IF");
	
	
	//Source 1: http://stackoverflow.com/questions/21790781/problems-highlighting-keywords-in-jtextpane
	// Source 2: http://andreinc.net/2013/07/15/how-to-customize-the-font-inside-a-jtextpane-component-java-swing-highlight-java-keywords-inside-a-jtextpane/
	private void checkForHighlights(){
        Runnable checker = new Runnable() {
            public void run(){
                Matcher stringMatcher;
                try{
                    StyleContext style = StyleContext.getDefaultStyleContext();
                    Map<String, Color> patterns = Constants.syntaxColors();
                    
                    clearTextColors();
                    
                    for(String reg: patterns.keySet()){
                    	Pattern matcher = Pattern.compile(reg);
                    	Color color = patterns.get(reg);
                    	stringMatcher = matcher.matcher(textPane.getDocument().getText(0, textPane.getDocument().getLength()));
	                    
//	                    textPane.getStyledDocument().setCharacterAttributes(textPane.getDocument().getLength(), textPane.getDocument().getLength(), style.getEmptySet(), true);
	                   
	                    while (stringMatcher.find()){
	                    	boolean bold = reg.equals(Constants.GOTO_KEYWORDS_REGEX);
	                    	updateTextColor(stringMatcher.start(), stringMatcher.end() - stringMatcher.start(), color, bold);
	                    }
                    }
                }
                catch (BadLocationException e){
                    e.printStackTrace();
                }
            }
        };
        SwingUtilities.invokeLater(checker);
    }
	




	public void clearTextColors() {
		updateTextColor(0, textPane.getText().length(), Color.BLACK, false);
	}


	public void updateTextColor(int offset, int length, Color c, boolean bold) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
	
		if(bold){
			MutableAttributeSet asNew = new SimpleAttributeSet(aset.copyAttributes());
			StyleConstants.setBold(asNew, true);
			textPane.getStyledDocument().setCharacterAttributes(offset, length, asNew, true);
		}
		else{		
			textPane.getStyledDocument().setCharacterAttributes(offset, length, aset, true);
		}
	}
 

}