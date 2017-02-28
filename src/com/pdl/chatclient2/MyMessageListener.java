package com.pdl.chatclient2;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class MyMessageListener implements MessageListener {
	private String from;
	private String body;
	
	@Override
	public void processMessage(Chat chat, Message message) {
		from = message.getFrom();
		body = message.getBody();
		System.out.println(String.format("Received message '%1$s' from %2$s", body, from));
	}
	
	public String getMsgSndr(){
		return from;
	}
	
	public String getMsgTxt(){
		return body;
	}
}
