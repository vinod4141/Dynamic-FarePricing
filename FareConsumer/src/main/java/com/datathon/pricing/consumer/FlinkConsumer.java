package com.datathon.pricing.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.JoinedStreams;
import org.apache.flink.streaming.api.datastream.JoinedStreams.WithWindow;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.apache.flink.util.Collector;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import com.datathon.pricing.consumer.model.AlertDetail;
import com.datathon.pricing.consumer.model.PriceEvent;
import com.datathon.pricing.consumer.util.EventUtil;
import com.datathon.pricing.consumer.util.KeyUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class FlinkConsumer {
	private static final int MAX_EVENT_DELAY = 60; //out of event max 60 second
	private static ArrayList<String> OND = new ArrayList();
	public static void main(String[] args) throws Exception {
		// create execution environment
		try {
			
			
			OND.add("PRGPVG");
			OND.add("DUSBOM");
			OND.add("HKGFLL");
			
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		Map  map  = new HashMap();
		//map.put("bootstrap.servers", "10.100.12.165:9092");
		//map.put("zookeeper.connect", "10.100.12.165:2181");
		map.put("bootstrap.servers", "localhost:9092");
		map.put("zookeeper.connect", "localhost:2181");
		
		map.put("group.id", "gars211r271");
		map.put("auto.offset.reset", "earliest");
		
		map.put("topic", "datathon");
		//map.put("topic", "price4");
		
		String json = null;
		// parse user parameters
		ParameterTool parameterTool = ParameterTool.fromMap(map);
		FlinkKafkaConsumer09 fkConsumer   = new FlinkKafkaConsumer09<>(parameterTool.getRequired("topic"), new SimpleStringSchema(), parameterTool.getProperties());
		//fkConsumer.assignTimestampsAndWatermarks(new AirlinePriceTSExtractor());
		
		//Reading from kafka
		DataStream<String> messageStream = env.addSource(fkConsumer);

		//Converting to price event and returing only when value is there 
		DataStream<PriceEvent> priceEventstream = messageStream.flatMap(new FlatMapFunction<String,PriceEvent >() {
			private static final long serialVersionUID = -6867736771747690202L;
			PriceEvent event = null;

			@Override
			public void flatMap(String value, Collector<PriceEvent> out) throws Exception {
				if (!"".equalsIgnoreCase(value)) {
						 event = EventUtil.getPriceEvent(value);
						 out.collect(event);
				}
				// System.out.println("input--" + value);
				
				//return event;
			}

		});
		
		
		//Filtering based on specific interested OND
		DataStream<PriceEvent> ONDFilteredEventstream = priceEventstream.filter(new FilterFunction<PriceEvent>() {
			private static final long serialVersionUID = -6867736771747690202L;

			@Override
			public boolean filter(PriceEvent value) throws Exception {
				if(value!=null && OND.contains(value.getOd())){
					return true;
		}else{
			return false;
		}
			}
			
		});		
		
		//Only EK EVENTS
		DataStream<PriceEvent> ekFilteredEventstream = priceEventstream.filter(new FilterFunction<PriceEvent>() {
			private static final long serialVersionUID = -6867736771747690202L;
	
			@Override
			public boolean filter(PriceEvent value) throws Exception {
				if (value!=null && "EK".equalsIgnoreCase(value.getCarrier())) {
					//System.out.println("ekFilteredEventstream::EK element ->"+value);
					return true;
				} else {
					return false;
				}
			}
	
		});
			
		
		//Other than EK events
		DataStream<PriceEvent> otherFilteredEventstream = priceEventstream.filter(new FilterFunction<PriceEvent>() {
			private static final long serialVersionUID = -6867736771747690202L;

			@Override
			public boolean filter(PriceEvent value) throws Exception {
				if (value!=null && !"EK".equalsIgnoreCase(value.getCarrier())) {
					return true;
				} else {
					return false;
				}
			}

		});


		//Created different streams based on OND
		KeyedStream<PriceEvent, Integer>  keyByONDStream =    otherFilteredEventstream.keyBy(new KeySelector<PriceEvent, Integer>() {
	     public Integer getKey(PriceEvent priceEvent) { return priceEvent.hashCode(); }
	   });

		
		//Generate price events if there is a price change
		DataStream<PriceEvent> dsPriceChangeStream = keyByONDStream.countWindow(2, 1)
				.reduce(new ReduceFunction<PriceEvent>() {
					@Override
					public PriceEvent reduce(PriceEvent current, PriceEvent previous) throws Exception {
						PriceEvent evtCurr = (PriceEvent) current;
						PriceEvent evtPrev = (PriceEvent) previous;
/*						System.out.println("KEY->" + evtCurr.hashCode() + " evtCurr->"
								+ evtCurr.getInboundDepartureDate()+ evtCurr.getInboundDepartureTime() + "  ---evtPrev->" + evtPrev.getInboundDepartureDate()+ evtPrev.getInboundDepartureTime());
*/						evtCurr.setPriceChange(
								(new Double(evtCurr.getPriceINC()) - new Double(evtPrev.getPriceINC())));
						//DO this in eventPrice object?????
						evtCurr.setPreviousFare(Double.valueOf(evtPrev.getPriceINC()));

						return evtCurr;
					}
				}).filter(new FilterFunction<PriceEvent>() {

			@Override
			public boolean filter(PriceEvent value) throws Exception {
				if (value.getPriceChange() !=0) {
					return true;
				} else {
					return false;
				}
			}

		});
		
		//dsPriceChangeStream.print();
		
		KeySelector keyEK = new KeySelector<PriceEvent, Integer>() {
			public Integer getKey(PriceEvent evt) {
				return new KeyUtil().getPriceEventWOCarrKey(evt);
			}
		};
			

		
		WithWindow jStream	= ekFilteredEventstream.join(dsPriceChangeStream).where(keyEK).equalTo(keyEK).window(TumblingProcessingTimeWindows.of(Time.seconds(10)));
		DataStream<Tuple2<PriceEvent, PriceEvent>> nonEKAndEKStream =	jStream.apply(new JoinFunction<PriceEvent, PriceEvent, Tuple2<PriceEvent, PriceEvent>>() {
	        @Override
	        public Tuple2<PriceEvent, PriceEvent> join(PriceEvent first, PriceEvent second) throws Exception {
	            return new Tuple2<PriceEvent, PriceEvent>(first, second);
	            }
	    });
		
		nonEKAndEKStream.print();
		DataStream<String> alertStream =  nonEKAndEKStream.flatMap(new FlatMapFunction<Tuple2<PriceEvent, PriceEvent>, String>(){
			
			@Override
			public void flatMap(Tuple2<PriceEvent, PriceEvent> value, Collector<String> out) throws Exception {
				PriceEvent pe =value.f1;
				Double difference= pe.getPriceChange() - new Double(value.f0.getPriceINC());
				//System.out.println("difference between NON EK and EK  is  --->"+ difference);
					if(difference>0  || difference< 0){
						AlertDetail alert = new AlertDetail();
						alert.setAlertId(pe.getId() );
						alert.setCarrier(pe.getCarrier());
						alert.setCompartment(pe.getCompartment());
						alert.setCurrency(pe.getCurrency());
						alert.setOrigin(pe.getOrigin());
						alert.setDestination(pe.getDestination());
						alert.setDifferencetoEK(difference);
						//System.out.println("observe time is  --->"+ pe.getObservationTime());
                        alert.setProposedFare(Float.valueOf(pe.getPriceINC())*1.10f);
                        alert.setAvailableFare(Float.valueOf(pe.getPriceINC()));
                        alert.setDepartureDate(pe.getOutboundDepartureDate());
                        alert.setDepartureTime(pe.getOutBoundDepartureTime());
                        alert.setPreviousAvailableFare(0);

    					ObjectMapper mapper = new ObjectMapper();
    					String jsonInString = null;
    					try {
    						jsonInString = mapper.writeValueAsString(alert);
    					} catch (JsonProcessingException e) {
    						e.printStackTrace();
    					}
    					
    					//return jsonInString;                        
                        
						out.collect(jsonInString); 
					}
				 	
				 }
			
		});
		
		alertStream.addSink(new FlinkKafkaProducer09<String>(
		        "10.100.12.165:9092",      // Kafka broker host:port
		        "reactive",       // Topic to write to
		        new SimpleStringSchema())  // Serializer (provided as util)
		);		
		//alertStream.print();
		env.execute();
	
		} catch (Exception e){
			e.printStackTrace();
		}
	
			
		}
		
	/**
	 * Assigns timestamps to  records.
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



