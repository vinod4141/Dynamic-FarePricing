package com.datathon.pricing.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.FilterFunction;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import com.datathon.pricing.consumer.model.PriceEvent;
import com.datathon.pricing.consumer.util.EventUtil;


public class FlinkConsumer {
	private static final int MAX_EVENT_DELAY = 60; //out of event max 60 second
	private static ArrayList OND = new ArrayList();
	
	
	public static void main(String[] args) throws Exception {
		// create execution environment
		try {
			OND.add("CPHHKT");
			OND.add("DUSBUM");
			OND.add("HKGFLL");
			
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		Map  map  = new HashMap();
		map.put("bootstrap.servers", "10.100.12.165:9092");
		map.put("zookeeper.connect", "10.100.12.165:2181");
		map.put("group.id", "gr100");
		map.put("auto.offset.reset", "earliest");
		
		map.put("topic", "datathon");
		
		String json = null;
		// parse user parameters
		ParameterTool parameterTool = ParameterTool.fromMap(map);
		FlinkKafkaConsumer09 fkConsumer   = new FlinkKafkaConsumer09<>(parameterTool.getRequired("topic"), new SimpleStringSchema(), parameterTool.getProperties());
		fkConsumer.assignTimestampsAndWatermarks(new AirlinePriceTSExtractor());
		DataStream<String> messageStream = env.addSource(fkConsumer);

		// print() will write the contents of the stream to the TaskManager's standard out stream
		// the rebelance call is causing a repartitioning of the data so that all machines
		// see the messages (for example in cases when "num kafka partitions" < "num flink operators"
		
		DataStream<PriceEvent> priceEventstream = messageStream.map(new MapFunction<String, PriceEvent>() {
			private static final long serialVersionUID = -6867736771747690202L;

			@Override
			public PriceEvent map(String value) throws Exception {
				
				PriceEvent event = EventUtil.getPriceEvent(value);
				return event;
			}
			
		});
		
		DataStream<PriceEvent> ONDFilteredEventstream = priceEventstream.filter(new FilterFunction<PriceEvent>() {
			private static final long serialVersionUID = -6867736771747690202L;

			@Override
			public boolean filter(PriceEvent value) throws Exception {
				if(OND.contains(value.getOd())){
					return true;
		}else{
			return false;
		}
			}
			
		});

		ONDFilteredEventstream.print();
		env.execute();
	
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	
	/**
	 * Assigns timestamps to TaxiRide records.
	 * Watermarks are a fixed time interval behind the max timestamp and are periodically emitted.
	 */
	public static class AirlinePriceTSExtractor extends BoundedOutOfOrdernessTimestampExtractor<String> {

		public AirlinePriceTSExtractor() {
			super(Time.seconds(MAX_EVENT_DELAY));
		}

		@Override
		public long extractTimestamp(String record) {
			return System.currentTimeMillis() ;
		}
	}


}
