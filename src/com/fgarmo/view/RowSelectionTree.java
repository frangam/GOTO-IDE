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

//http://ateraimemo.com/Swing/TreeRowSelection.html
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;


/**
 * @see //http://ateraimemo.com/Swing/TreeRowSelection.html
 *
 */
public class RowSelectionTree extends JTree {
	private static final Color SELC = new Color(100, 150, 255);
    //private Handler handler;

    @Override protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(SELC);
        for (int i: getSelectionRows()) {
            Rectangle r = getRowBounds(i);
            g2.fillRect(0, r.y, getWidth(), r.height);
        }
        super.paintComponent(g);
        if (hasFocus()) {
            TreePath path = getLeadSelectionPath();
            if (Objects.nonNull(path)) {
                Rectangle r = getRowBounds(getRowForPath(path));
                g2.setPaint(SELC.darker());
                g2.drawRect(0, r.y, getWidth() - 1, r.height - 1);
            }
        }
        g2.dispose();
    }
    @Override public void updateUI() {
        //removeFocusListener(handler);
        super.updateUI();
        setUI(new BasicTreeUI() {
            @Override public Rectangle getPathBounds(JTree tree, TreePath path) {
                if (Objects.nonNull(tree) && Objects.nonNull(treeState)) {
                    return getPathBounds(path, tree.getInsets(), new Rectangle());
                }
                return null;
            }
            private Rectangle getPathBounds(TreePath path, Insets insets, Rectangle bounds) {
                Rectangle rect = treeState.getBounds(path, bounds);
                if (Objects.nonNull(rect)) {
                    rect.width = tree.getWidth();
                    rect.y += insets.top;
                }
                return rect;
            }
        });
        UIManager.put("Tree.repaintWholeRow", Boolean.TRUE);
        //handler = new Handler();
        //addFocusListener(handler);
        setCellRenderer(new Handler());
        setOpaque(false);
    }
    private static class Handler extends DefaultTreeCellRenderer { //implements FocusListener {
        @Override public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            l.setBackground(selected ? SELC : tree.getBackground());
            
            	ImageIcon tDoc = new ImageIcon(MainView.class.getResource("/com/fgarmo/resources/images16/1487298560_file-code.png"));
	          ImageIcon tOpen = new ImageIcon(MainView.class.getResource("/com/fgarmo/resources/images16/1487298560_file-code.png"));
	          ImageIcon tClosed = new ImageIcon(MainView.class.getResource("/com/fgarmo/resources/images16/1487298560_file-code.png"));
	          setClosedIcon(tClosed);
	          setOpenIcon(tOpen);
	          setLeafIcon(tDoc);
            
            l.setOpaque(true);
            return l;
        }
//         @Override public void focusGained(FocusEvent e) {
//             e.getComponent().repaint();
//         }
//         @Override public void focusLost(FocusEvent e) {
//             e.getComponent().repaint();
//             //TEST:
//             //if (Objects.nonNull(tree.getLeadSelectionPath())) {
//             //    Rectangle r = tree.getRowBounds(tree.getRowForPath(tree.getLeadSelectionPath()));
//             //    r.width += r.x;
//             //    r.x = 0;
//             //    tree.repaint(r);
//             //}
//         }
    }
}
