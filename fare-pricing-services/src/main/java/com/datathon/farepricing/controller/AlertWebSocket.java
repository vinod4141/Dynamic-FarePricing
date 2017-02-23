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

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import com.datathon.farepricing.model.AlertDetail;
import com.datathon.farepricing.util.GeneralUtil;




@ServerEndpoint("/FareAlerts")
public class AlertWebSocket {
	
	
	private static final Logger LOG = Logger.getLogger(AlertWebSocket.class.getName());
	static Queue<Session> queue = new ConcurrentLinkedQueue<>();
	public static void send(){
		
		try{
			
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		Map  map  = new HashMap();
		map.put("bootstrap.servers", "10.100.12.165:9092");
		map.put("zookeeper.connect", "10.100.12.165:2181");
		map.put("group.id", "gr211r285");
		map.put("auto.offset.reset", "earliest");
		
		map.put("topic", "reactive1");
		//map.put("topic", "price4");
		
		String json = null;
		// parse user parameters
		ParameterTool parameterTool = ParameterTool.fromMap(map);
		FlinkKafkaConsumer09 fkConsumer   = new FlinkKafkaConsumer09<>(parameterTool.getRequired("topic"), new SimpleStringSchema(), parameterTool.getProperties());
		//fkConsumer.assignTimestampsAndWatermarks(new AirlinePriceTSExtractor());
		
		//Reading from kafka
		DataStream<String> messageStream = env.addSource(fkConsumer);

			//Converting to price event
			DataStream<AlertDetail> priceEventstream = messageStream.map(new MapFunction<String, AlertDetail>() {
				private static final long serialVersionUID = -6867736771747690202L;
				//PriceEvent event = null;
				AlertDetail event = null;
				@Override
				public AlertDetail map(String value) {
					if (!"".equalsIgnoreCase(value)) {
						try {
							//System.out.println("input --" + value);
							 event = GeneralUtil.getAlertEvent(value);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("input error--" + value);

						}
					}
					for (Session session : queue){
						try {
							System.out.println("The Event is " +  GeneralUtil.getEventJSON(event));
							session.getBasicRemote().sendText(GeneralUtil.getEventJSON(event));
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return event;
				}

			});
			//priceEventstream.print();
			env.execute();
		}catch (Exception e){
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
		System.out.println(str);
		send();
		return str;
	}
	
			
}

