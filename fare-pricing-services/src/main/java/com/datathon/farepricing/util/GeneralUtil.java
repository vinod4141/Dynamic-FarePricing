package com.datathon.farepricing.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import com.datathon.farepricing.model.AlertDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class GeneralUtil {
	
	public static AlertDetail getAlertEvent(String alertJson){
		AlertDetail event = new AlertDetail();
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			event = mapper.readValue(alertJson, AlertDetail.class);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	
		return event;
		
	}
	
	public static List<AlertDetail> getAlertFromKafka(){
		
		DataStream<AlertDetail> priceEventstream = null;
		List<AlertDetail> alerts = new ArrayList<>();
		try{
			StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
			Map  map  = new HashMap();
			map.put("bootstrap.servers", "10.100.12.165:9092");
			map.put("zookeeper.connect", "10.100.12.165:2181");
			map.put("group.id", "gr211r283");
			map.put("auto.offset.reset", "earliest");
			
			map.put("topic", "reactive");
			//map.put("topic", "price4");
			
			String json = null;
			// parse user parameters
			ParameterTool parameterTool = ParameterTool.fromMap(map);
			FlinkKafkaConsumer09 fkConsumer   = new FlinkKafkaConsumer09<>(parameterTool.getRequired("topic"), new SimpleStringSchema(), parameterTool.getProperties());
			//fkConsumer.assignTimestampsAndWatermarks(new AirlinePriceTSExtractor());
			
			//Reading from kafka
			DataStream<String> messageStream = env.addSource(fkConsumer);

				//Converting to price event
				priceEventstream = messageStream.map(new MapFunction<String, AlertDetail>() {
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
								System.out.println("input error--" + value);

							}
						}
						 //System.out.println("input--" + event);
						alerts.add(event);
						return event;
						
					}

				});
				priceEventstream.print();
				env.execute();
			}catch (Exception e){
				e.printStackTrace();
			}
			return alerts;
	}
	
	public static String getEventJSON( Object obj){
		String json = null;
		
		Gson gson = new Gson();
		json = gson.toJson(obj);
		
		System.out.println(json);
		return json;
	}

}
