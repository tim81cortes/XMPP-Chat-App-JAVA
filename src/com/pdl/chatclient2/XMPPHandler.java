package com.pdl.chatclient2;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Message;



public class XMPPHandler {
	String server;
	int port;
	private static final int timeout = 500; // The packet reply times out after half a second
	private XMPPConnection xmpConnect;
	private ChatManager chatMngr;
	private ConnectionConfiguration cnnctnConfig;
	private String latestMsg;
	//private String server;
	private MyMessageListenerInside msgLstnr;
	// Add constructor
	public XMPPHandler(String server, int port) {
		this.server = server;
		this.port = port;
	}
	
	public void initLogin() throws XMPPException{
		
		cnnctnConfig = new ConnectionConfiguration("yeahsowhat.local", 5222);
		cnnctnConfig.setSASLAuthenticationEnabled(false);
		cnnctnConfig.setSecurityMode(SecurityMode.disabled);
		
		latestMsg = "nothing yet";
		
		System.out.println("Creating connection to server on port");
		
		SmackConfiguration.setPacketReplyTimeout(timeout);
		// New xmpp connection
		xmpConnect = new XMPPConnection(cnnctnConfig);
		
		//Attempt to establish a connection
		
		xmpConnect.connect();
		//check connection
		System.out.println("Connected: " + xmpConnect.isConnected());

		// Log in with username and password
		xmpConnect.login("sparkusr", "password");		
		chatMngr = xmpConnect.getChatManager();
		msgLstnr = new MyMessageListenerInside();
		//recieve();
		
	}
	public String getLatestMsg(){
		
		return latestMsg;
	}
public void setLatestMsg(String latest){
		latestMsg = latest;
	}
	
	public void send(String msgTxt) throws XMPPException{
		
		System.out.println("Just before send");
		Chat chat = chatMngr.createChat("sparkusr2@yeahsowhat.local", msgLstnr);
		chat.sendMessage(msgTxt);
	}
	public void recieve()
	{
		Chat recievedChat = chatMngr.createChat("sparkusr2@yeahsowhat.local", new MessageListener(){
				public void processMessage(Chat chat, Message message){
					if(message != null)
					{
						latestMsg = message.getBody();
					}
				}
		});
	}
	
	public void destroy() {
		if (xmpConnect!=null && xmpConnect.isConnected()) {
			System.out.println("Just before disconnection");
			// Disconnect from server
			xmpConnect.disconnect();
		}	
	}	
	class MyMessageListenerInside implements MessageListener {

		@Override
		public void processMessage(Chat chat, Message message) {
			String from = message.getFrom();
			String body = message.getBody();
			if (body != null){
				latestMsg = body;
			}
				
			System.out.println(String.format("Received message '%1$s' from %2$s", body, from));
		}
		
	}
}
