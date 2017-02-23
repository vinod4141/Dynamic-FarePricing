package com.datathon.farepricing.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/consume")
public class FareConsumerWebSocket {
	
	private static final Logger LOG = Logger.getLogger(FareConsumerWebSocket.class.getName());
	static Queue<Session> queue = new ConcurrentLinkedQueue<>();
	public static void send(){
		
		String msg = "Hello Vinod ";
		int i = 0;
		try {
		while(true){
		   
			for (Session session : queue){
				
					session.getBasicRemote().sendText(msg + i);
					//session.getBasicRemote().sendPing(arg0);
			}
			Thread.sleep(2000);
			i++;
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnOpen
	public void onOpen(Session session){
		LOG.info("Connectione Opned");
		queue.add(session);
		send();
	}
	
	@OnClose
	public void onClose(Session session){
		LOG.info("Connection Closes");
		queue.remove(session);
	}
	
	@OnMessage
	public String myOnMessage(String txt){
		
		LOG.info("Inside OnMessage with " +  txt);
		String str = "Inside OnMessage method";
		return str;
	}
	
}
	


