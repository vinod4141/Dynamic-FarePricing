package com.datathon.pricing.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import com.datathon.pricing.consumer.FlinkConsumer.AirlinePriceTSExtractor;
import com.datathon.pricing.consumer.model.AlertDetail;
import com.datathon.pricing.consumer.util.EventUtil;

public class FlinkTestConsumer {
	
	
		private static final int MAX_EVENT_DELAY = 60; //out of event max 60 second
		
		public static void main(String[] args) throws Exception {
			// create execution environment
			try {
				
			
			StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
			Map  map  = new HashMap();
			map.put("bootstrap.servers", "10.100.12.165:9092");
			map.put("zookeeper.connect", "10.100.12.165:2181");
			//map.put("bootstrap.servers", "localhost:9092");
			//map.put("zookeeper.connect", "localhost:2181");
			
			map.put("group.id", "gr211r283");
			map.put("auto.offset.reset", "earliest");
			
			map.put("topic", "reactive");
			//map.put("topic", "price4");
			
			String json = null;
			// parse user parameters
			ParameterTool parameterTool = ParameterTool.fromMap(map);
			FlinkKafkaConsumer09 fkConsumer   = new FlinkKafkaConsumer09<>(parameterTool.getRequired("topic"), new SimpleStringSchema(), parameterTool.getProperties());
			fkConsumer.assignTimestampsAndWatermarks(new AirlinePriceTSExtractor());
			
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
								 event = EventUtil.getAlertEvent(value);
							} catch (Exception e) {
								System.out.println("input error--" + value);

							}
						}
						 //System.out.println("input--" + event);
						return event;
					}

				});
				priceEventstream.print();
			env.execute();
			} catch (Exception e){
				e.printStackTrace();
			}
			
		}
		
		
}

