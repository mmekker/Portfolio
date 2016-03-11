package bstree;
import javax.swing.*;
import javax.swing.SwingUtilities.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Random;

/** This class is the Swing App used for displaying a binary search tree
 * 
 * @author Mike Mekker
 *
 */
public class bstSwingApp extends JFrame implements ActionListener
{
	
	//TODO: Export code and package it correctly
	//TODO: Make bst.java a stand alone collections class
	
	/**XDIM - X Dimension of window */
    public static int XDIM = 1000;
    /**YDIM - Y Dimension of window */
    public static int YDIM = 500;
    /**Instance of Swing App*/
    static bstSwingApp bts;
    /**Instance of bst*/
	private bst<Integer> tree;

    boolean clearFlag;
    boolean reflectFlag;
    
    //Buttons
    JButton clearButton;
    JButton addButton;
    JButton reflectButton;
    JButton removeButton;
    JButton saveButton;
    //Text fields
    JTextField removeTextField;
    //Panels
    JPanel buttonPanel;
    TreePanel treePanel;
 
    /**
     * bstSwingApp() Constructor for Swing App
     */
    public bstSwingApp() 
    {
		clearFlag = true;
	    reflectFlag = false;
		tree = new bst<Integer>();
		
		//Look for file
    	//open it if its there
    	//make new one if there is not
		String fs = System.getProperty("file.separator");
		String fileName = System.getProperty("user.home") + fs + "bst.bstrc";
    	try 
    	{
		    File file = new File(fileName);
		    if (file.createNewFile())
		    {
		    	System.out.println("File is created!");
		    }
		    else
		    {
		    	System.out.println("File already exists.");
		    	if(file.length() != 0)
		    	{
			    	try
			    	{
			    		tree = (bst)(SerializationTool.deserialize(fileName));
			    		tree.setComp();
			    	}
			    	catch (ClassNotFoundException e)
			    	{
			    		e.printStackTrace();
			    	}
		    	}
		    }
  	      
      	} 
    	catch (IOException e)
    	{
    		e.printStackTrace();
      	}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(40,40);
		setLayout(new BorderLayout() );
		
		//Create things
		addButton =     new JButton("  Add  ");
		reflectButton = new JButton("Reflect");
		clearButton =   new JButton(" Clear ");
		removeButton = new JButton("Remove");
		removeTextField = new JTextField(5);
		saveButton = new JButton(" Save ");
	
		//Create the panels
	    buttonPanel = new JPanel();
		treePanel = new TreePanel();
	
		buttonPanel.setLayout(new FlowLayout());
	
		//Add things to panels
		buttonPanel.add(addButton);
		buttonPanel.add(reflectButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(removeTextField);
		buttonPanel.add(saveButton);
	
		//Add panels to frame 
		add(treePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		//Repaint tree for if its bringing in pre-made tree
		if(tree.data != null)
		{
			clearFlag = false;
		    reflectFlag = false;
			treePanel.repaint();
		}
		
		//Add Button ActionListener
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			   clearFlag = false;
			   reflectFlag = false;
			   tree.add((int)(Math.random()*100), tree.getComp());
			   //System.out.println("\n\n\n\n\n");
			   //tree.printBinaryTree(tree, 0);
			   treePanel.repaint();
			}
		    });
		//Clear Button ActionListener
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    clearFlag = true;
			    reflectFlag = false;
			    tree = new bst<Integer>();
			    //System.out.println("\n\n\n\n\n");
			    //tree.printBinaryTree(tree, 0);
			    treePanel.repaint();
			}
		    });
		//Remove Button ActionListener
		removeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(tree.size() == 1 && removeTextField.getText().contentEquals(tree.data.toString()))
				{
					clearFlag = true;
				    reflectFlag = false;
				    tree = new bst<Integer>();
					removeTextField.setText("");
				}
				else if(!removeTextField.getText().contentEquals(""))
				{
					clearFlag = false;
				    reflectFlag = false;
					int i = Integer.parseInt(removeTextField.getText());
					tree.remove(i);
					removeTextField.setText("");
					//System.out.println("\n\n\n\n\n");
					//tree.printBinaryTree(tree, 0);
				}
				else
				{
					clearFlag = false;
				    reflectFlag = false;
				}
				treePanel.repaint();
			}
		});
		//Reflect Button ActionListener
		reflectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    clearFlag = false;
			    reflectFlag = true;
				treePanel.repaint();
			}
		    });
		//Save Button ActionListener
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    clearFlag = false;
			    reflectFlag = false;
			    try 
			    {
					SerializationTool.serialize(tree, fileName);
				} 
			    catch (IOException e1) 
			    {
					e1.printStackTrace();
				}
			}
		    });
	
	
		// pack the widgets and make frame visible
		setSize(XDIM,YDIM);
		pack();
		setResizable(false);
		setVisible(true);   // make frame visible
    } // end constructor


    //Only here because its necessary to have it
    //never used
    public void actionPerformed(ActionEvent e) {}
    
    /**
     * createAndShowGUI() Instantiates instance of swing app
     */
    public static void createAndShowGUI()
    {
		try
		{
		    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (Exception e) {}
		bts = new bstSwingApp();
    }
    
    //Starts when bstSwingApp.java is run
    //Starts everything
    /**
     * main(String [] args) Starts everything
     * @param args
     */
    public static void main(String [] args)
    {
    	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    createAndShowGUI();
		}
	    });
    } //end main
    
    /**
     * TreePanel class - panel that displays the bst
     * @author Mike Mekker
     *
     */
    class TreePanel extends JPanel {
    	/**
    	 * TreePanel constructor
    	 */
		public TreePanel() 
		{
		    setBorder(BorderFactory.createLineBorder(Color.blue));
		}
		public Dimension getPreferredSize() 
		{
		    return new Dimension(XDIM,YDIM);
		}
		
		/**
		 * paintComponent(Graphics g) - repaints nodes
		 * called when repaint() is called
		 */
		protected void paintComponent(Graphics g) 
		{
		    super.paintComponent(g);
		    if (clearFlag) return;
		    if(tree.data == null)return;
		    if(reflectFlag) //if reflect button is pressed
		    {
		    	tree.reflectCoords();
		    	tree.redoCoords();
		    	paintReflect(tree, g);
		    	tree.reflectCoords();
		    	tree.redoCoords();
		    }
		    
		    tree.redoCoords();
		    paintNode(tree, g);
		    
		}
		
		/**
		 * recursively paints all  nodes and lines between the nodes
		 * @param t - BST
		 * @param g - Graphics
		 */
		public void paintNode(bst t, Graphics g)
		{
			g.setColor(Color.blue);
			g.fillRect(t.x-2, t.y-14, 20, 20);
			g.setColor(Color.white);
			g.drawString(t.data.toString(),t.x,t.y);
			g.setColor(Color.black);
			if(t.x > XDIM){XDIM += 100;bts.setSize(XDIM,YDIM);}
			if(t.y > YDIM-100){YDIM += 100;bts.setSize(XDIM,YDIM);}
			if(t.leftTree != null)
			{
				g.drawLine(t.x,t.y,t.leftTree.x,t.leftTree.y);
				paintNode(t.leftTree, g);
			}
			if(t.rightTree != null)
			{
				g.drawLine(t.x,t.y,t.rightTree.x,t.rightTree.y);
				paintNode(t.rightTree, g);
			}
		}
		/**
		 * Similar to paintNode() except with different coordinates and with a 500 offset
		 * @param t - BST
		 * @param g - Graphics
		 */
		public void paintReflect(bst t, Graphics g)
		{
			int newX = t.x + 500;
			g.setColor(Color.blue);
			g.fillRect(newX-2, t.y-14, 20, 20);
			g.setColor(Color.white);
			g.drawString(t.data.toString(),newX,t.y);
			g.setColor(Color.black);
			if(t.leftTree != null)
			{
				g.drawLine(newX,t.y,t.leftTree.x+500,t.leftTree.y);
				paintReflect(t.leftTree, g);
			}
			if(t.rightTree != null)
			{
				g.drawLine(newX,t.y,t.rightTree.x+500,t.rightTree.y);
				paintReflect(t.rightTree, g);
			}
		}
		
    }
    
}