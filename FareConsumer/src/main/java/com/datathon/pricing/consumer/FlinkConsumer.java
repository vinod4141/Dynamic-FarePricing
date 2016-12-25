package com.datathon.pricing.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.FilterFunction;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

import com.datathon.pricing.consumer.model.PriceEvent;
import com.datathon.pricing.consumer.util.EventUtil;
import com.datathon.pricing.consumer.util.KeyUtil;


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
		map.put("bootstrap.servers", "10.100.12.165:9092");
		map.put("zookeeper.connect", "10.100.12.165:2181");
		//map.put("bootstrap.servers", "localhost:9092");
		//map.put("zookeeper.connect", "localhost:2181");
		
		map.put("group.id", "gr115");
		map.put("auto.offset.reset", "earliest");
		
		map.put("topic", "datathon1");
		//map.put("topic", "price4");
		
		String json = null;
		// parse user parameters
		ParameterTool parameterTool = ParameterTool.fromMap(map);
		FlinkKafkaConsumer09 fkConsumer   = new FlinkKafkaConsumer09<>(parameterTool.getRequired("topic"), new SimpleStringSchema(), parameterTool.getProperties());
		fkConsumer.assignTimestampsAndWatermarks(new AirlinePriceTSExtractor());
		DataStream<String> messageStream = env.addSource(fkConsumer);

			DataStream<PriceEvent> priceEventstream = messageStream.map(new MapFunction<String, PriceEvent>() {
				private static final long serialVersionUID = -6867736771747690202L;
				PriceEvent event = null;

				@Override
				public PriceEvent map(String value) {
					if (!"".equalsIgnoreCase(value)) {
						try {
							PriceEvent event = EventUtil.getPriceEvent(value);
						} catch (Exception e) {
							System.out.println("input--" + value);

						}
					}
					// System.out.println("input--" + value);
					return event;
				}

			});
		
		
		
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
		
			DataStream<PriceEvent> ekFilteredEventstream = priceEventstream.filter(new FilterFunction<PriceEvent>() {
				private static final long serialVersionUID = -6867736771747690202L;

				@Override
				public boolean filter(PriceEvent value) throws Exception {
					if (value!=null && "EK".equalsIgnoreCase(value.getCarrier())) {
						return true;
					} else {
						return false;
					}
				}

			});
			
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
			//ONDFilteredEventstream.print();
		//
		KeyedStream<PriceEvent, Integer>  keyByONDStream =    otherFilteredEventstream.keyBy(new KeySelector<PriceEvent, Integer>() {
		     public Integer getKey(PriceEvent priceEvent) { return priceEvent.hashCode(); }
		   });
		
		
			DataStream<PriceEvent> dsPriceEvent = keyByONDStream.countWindow(2, 1)
					.reduce(new ReduceFunction<PriceEvent>() {
						@Override
						public PriceEvent reduce(PriceEvent current, PriceEvent previous) throws Exception {
							PriceEvent evtCurr = (PriceEvent) current;
							PriceEvent evtPrev = (PriceEvent) previous;
							System.out.println("KEY->" + evtCurr.hashCode() + " evtCurr->"
									+ evtCurr.getInboundDepartureDate()+ evtCurr.getInboundDepartureTime() + "  ---evtPrev->" + evtPrev.getInboundDepartureDate()+ evtPrev.getInboundDepartureTime());
							// if(Math.abs(new Integer(evtCurr.getPriceEXC())-
							// new Integer(evtPrev.getPriceEXC()))
							// /new Integer(evtCurr.getPriceEXC())>0.20) {

							evtCurr.setPriceChange(
									(new Double(evtCurr.getPriceINC()) - new Double(evtPrev.getPriceINC())));
							// }
							return evtCurr;
						}
					});

			KeySelector keyEK = new KeySelector<PriceEvent, Integer>() {
				public Integer getKey(PriceEvent evt) {
					return new KeyUtil().getPriceEventWOCarrKey(evt);
				}
			};
			DataStream<PriceEvent> pricechangeAlert = ekFilteredEventstream.connect(dsPriceEvent).keyBy(keyEK, keyEK)
					.map(new PriceChangeActor());// map(new PriceChangeActor());

		///dsPriceEvent.print();
	//	DataStream<PriceEvent, Integer> countwindow1  = keyByONDStream.countWindow(2).sum(new count(PriceEvent pe));
	//	countwindow1.print();
		  
//	DataStream<PriceEvent>  gpevenst  = ONDFilteredEventstream.transform(operatorName, outTypeInfo, operator)
		
		// print() will write the contents of the stream to the TaskManager's standard out stream
		// the rebelance call is causing a repartitioning of the data so that all machines
		// see the messages (for example in cases when "num kafka partitions" < "num flink operators"
		/*messageStream.rebalance().map(new MapFunction<String, String>() {
			private static final long serialVersionUID = -6867736771747690202L;

			@Override
			public String map(String value) throws Exception {
				
				PriceEvent event = EventUtil.getPriceEvent(value);
				return EventUtil.getEventJSON(event);
				//return "Kafka and Flink says: " + value;
				
			}
			
		}).print();*/
		
		//ONDFilteredEventstream.print();
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
	
	public static class PriceChangeActor  implements CoMapFunction<PriceEvent, PriceEvent, PriceEvent> {
        PriceEvent emiratesElement= null;
        PriceEvent otherElement= null;
        Integer emiratesKey = null;
        Integer otherKey=null;

        @Override
		public  PriceEvent map1(PriceEvent value1) throws Exception {
			// TODO Auto-generated method stub
        	if(value1!=null)
			emiratesKey = new KeyUtil().getPriceEventWOCarrKey(value1);
			System.out.println("EK element map1->"+emiratesElement);
			if(emiratesElement!=null && value1!=null) {
				System.out.println("EK element map1->"+emiratesElement);
				emiratesElement = value1;
			}
			return emiratesElement;
			
		}

		@Override
		public PriceEvent map2(PriceEvent value2) throws Exception {
			// TODO Auto-generated method stub
			PriceEvent evt=new PriceEvent();
			if(value2!=null && emiratesElement!=null) {
			otherKey = new KeyUtil().getPriceEventWOCarrKey(value2);
			
			int pricechange = Math.abs(value2.getPriceChange().intValue());
			System.out.println("EK element map2->" + emiratesElement);
			System.out.println("other element map2->" + value2);
			System.out.println("EK and other price comparisionvalues :other-emirate-difference ->" + pricechange + "-"
					+ emiratesElement.getPriceINC() + "-"
					+ Math.abs(pricechange - new Integer(emiratesElement.getPriceINC())));
			if (emiratesKey == otherKey && emiratesElement != null && value2 !=null && pricechange > 0
					&& Math.abs(pricechange - new Integer(emiratesElement.getPriceINC())) > 0) {
				// Need to generate alert
				System.out.println("EK and other price comparision Alert->" + "price change hapened");
				evt = new PriceEvent();
			}
			if (emiratesKey != otherKey) {
				System.out.println("keys are different in comparision->" + "price change hapened");
				// TODO write the code to compare emirates and other airline.
				System.out.println("EK and other price comparision->" + emiratesElement.toString());
				System.out.println("EK element map2->" + value2.toString());
			}
			
		}
			return evt;
		}
		
	}


}
