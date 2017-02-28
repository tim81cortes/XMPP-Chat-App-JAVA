package com.pdl.chatclient2;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jivesoftware.smack.XMPPException;



public class Form extends JPanel{
	private JTextArea[] fields;
	private String displayMessage;
	
	public Form(String[] labels, int[] widths, int[] heights)
	{
		super(new BorderLayout());
		GridLayout vertDivide = new GridLayout(2,0);
		JPanel outerPanel = new JPanel(new BorderLayout());
		JPanel northInner = new JPanel(new BorderLayout());
		JPanel eastWestInner = new JPanel(new BorderLayout());
		
		JPanel xChatPane = new JPanel(new GridLayout(1, 1));
		JPanel sendMsgPane = new JPanel(new GridLayout(1, 1));
		JPanel xChtPeoplePane = new JPanel(new GridLayout(1, 1));

		JPanel subPane[] = new JPanel[labels.length];
		
		add(outerPanel, BorderLayout.NORTH);
		outerPanel.add(northInner, BorderLayout.NORTH);
		northInner.add(sendMsgPane, BorderLayout.SOUTH);
		northInner.add(eastWestInner, BorderLayout.NORTH);
		eastWestInner.add(xChatPane, BorderLayout.CENTER);
		eastWestInner.add(xChtPeoplePane, BorderLayout.EAST);
		fields = new JTextArea[labels.length];
		System.out.println("labels length " + labels.length);
		for (int i = 0;  i < labels.length; i ++)
		{		    
			subPane[i] = new JPanel();

			fields[i] = new JTextArea();
			if (i < widths.length)
			{
				fields[i].setColumns(widths[i]);
			}
			if (i < heights.length)
			{
				fields[i].setRows(heights[i]);
			}
			JLabel jLabel = new JLabel(labels[i], JLabel.RIGHT);
			jLabel.setLabelFor(fields[i]);
		    subPane[i].add(fields[i]);
		}
		xChatPane.add(subPane[0]);
		sendMsgPane.add(subPane[1]);
		xChtPeoplePane.add(subPane[2]);
		
		
	}	
	 public String getText(int i) {
		    return (fields[i].getText());
		  }
	 public void setDisplayMsg(String dspMsg){
		 displayMessage = dspMsg;
		 
	 }
	 public void addDisplayMsg(){
		 if (fields[0]!=null && displayMessage != null)
		 {
			 System.out.println("Displaying text");
			 fields[0].append(displayMessage+"\n\n");
		 }
		 else
		 {
			 System.out.println("Nothing to display");
		 }
	 }
	
	public static void main(String[] args){
	String[] labels = {"XChats", "New XChat", "XChatters"};
	int[] widths = {50, 50, 25};
	int[] heights = {30, 5, 25};
	
	final Form chatWin = new Form(labels, widths, heights);
	JButton sendBut = new JButton("Send xChat");
	JButton recieveBut = new JButton("Recieve xChat");
	
	
	
	XMPPHandler xmpHandle = new XMPPHandler("192.168.1.91", 5222);
	//MyMessageListener messList = new MyMessageListener();
	try {
		xmpHandle.initLogin();


		
	} catch (XMPPException e1) {
		e1.printStackTrace();
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	
	sendBut.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
	        System.out.println(chatWin.getText(0) + " " + chatWin.getText(1) + ". " + chatWin.getText(2));
	    	try {
	    		xmpHandle.send(chatWin.getText(1));
	    		
	    	} catch (XMPPException e2) {
	    		e2.printStackTrace();
	    	} 
		}
	});	
	recieveBut.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			System.out.println("Recieve button activated");
			String tmp = xmpHandle.getLatestMsg();
			System.out.println(tmp);
			chatWin.setDisplayMsg(tmp);
			chatWin.addDisplayMsg();
			
			
		}
	});
	
	JFrame window = new JFrame("XChat");
	window.getContentPane().add(chatWin, BorderLayout.CENTER);
	JPanel buttonPanel = new JPanel();
	chatWin.add(buttonPanel, BorderLayout.SOUTH);
	buttonPanel.add(sendBut);
	buttonPanel.add(recieveBut);
	
	//window.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	window.pack();
	window.setVisible(true);
	
	window.addWindowListener(new WindowAdapter() {
		  public void windowClosing(WindowEvent e) {
		    int confirmed = JOptionPane.showConfirmDialog(null, 
		        "Are you sure you want to exit the program?", "Exit Program Message Box",
		        JOptionPane.YES_NO_OPTION);

		    if (confirmed == JOptionPane.YES_OPTION) {
		    		xmpHandle.destroy();
		    		window.dispose();
		      
		    }
		  }
		});
	
	}
}
