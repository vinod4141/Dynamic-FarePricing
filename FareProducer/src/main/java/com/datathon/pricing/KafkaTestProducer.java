package com.datathon.pricing;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.joda.time.*;
import org.joda.time.format.*;

import com.emirates.datathon.pricing.common.PriceEvent;

public class KafkaTestProducer {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		KafkaTestProducer prod = new KafkaTestProducer();
		prod.pushEvents("datathon3");
	}

	
	private void pushEvents( String kafkaTopic) throws InterruptedException{
		
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(getkafkaConfig());
		
		
		PriceEvent event ;
	
		
		for (int i = 1; i<10 ; i++)
		{
			event =  getPricingEvent( "AB", i );
			
			
			producer.send(new ProducerRecord<String, String>(kafkaTopic, "a", event.toString()));
			/*for (String line : (Iterable<String>) lines::iterator)
		    {
				
				 producer.send(new ProducerRecord<String, String>(kafkaTopic, "a", line));
				 System.out.println("--"+line);
				 System.out.println("--"+i);
				if (i%100==0){
					Thread.sleep(5000);
				}
				 i++;
				 
				 
		    }*/
			
			System.out.println(event.toString());
		}
		
		for (int i = 1; i<5 ; i++)
		{
			event =  getPricingEvent( "EK", i );
			
			
			producer.send(new ProducerRecord<String, String>(kafkaTopic, "a", event.toString()));
			/*for (String line : (Iterable<String>) lines::iterator)
		    {
				
				 producer.send(new ProducerRecord<String, String>(kafkaTopic, "a", line));
				 System.out.println("--"+line);
				 System.out.println("--"+i);
				if (i%100==0){
					Thread.sleep(5000);
				}
				 i++;
				 
				 
		    }*/
			
			System.out.println(event.toString());
		}
		
		producer.close();
	 
	}
	
	private PriceEvent getPricingEvent(String carrier, int i) {
		
		PriceEvent event = new PriceEvent();
		
		Random rand = new Random();
		int max = 12000;
		int min =  3000;
		
		int value = rand.nextInt((max - min) + 1) + min;
		
		//System.out.println("length of the array->"+values.length);
		event.setId(String.valueOf(i));
		event.setObservationDate( new Date().toString());
		event.setObservationTime(new Date().toString());
		event.setPos("DUS");
		event.setOrigin("DUS");
		event.setDestination("BOM");
		event.setCarrier(carrier);
		//event.setOutboundDepartureDate(dateFormat(addDay(new Date(), i)));
		event.setOutboundDepartureDate(dateFormatJODA( i));
		event.setOutboundArrivalDate(dateFormatJODA( i+1));
		//event.setOutBoundArrivalTime(values[9]);
		event.setInboundDepartureDate("28-01-2017");
		event.setInboundArrivalDate("29-01-2017");
		//event.setInboundArrivalTime(values[11]);
		event.setOutboundBookingClass("E");
		event.setInboundBookingClass("E");
		//event.setPriceEXC(values[16]);
		event.setPriceINC(String.valueOf(value));
		//event.setTax(values[18]);
		event.setCurrency("AED");
		event.setSource("Portal");
		//event.setPriceInbound(values[22]);
		//event.setPriceOutbound(values[21]);
		//event.setIsTaxIncCutIn(values[23]);
		//event.setCompartment(values[24]);
		//event.setLoadDate(values[25]);
		event.setOutBoundDepartureTime("10:15");
		event.setInboundDepartureTime("11:15");
		event.setOutBoundArrivalTime("06:00");
		event.setInboundArrivalTime("07:00");
		event.setOd("DUSBOM");
		
		return event;
	}


	private static Properties getkafkaConfig(){
		Properties props = new Properties();
		props.put("bootstrap.servers", "10.100.12.165:9092");
		props.put("metadata.broker.list", "10.100.12.165:9092");
/*		props.put("bootstrap.servers", "dolinux752.hq.emirates.com:9092,dolinux753.hq.emirates.com:9092,dolinux768.hq.emirates.com:9092");
		props.put("metadata.broker.list", "dolinux752.hq.emirates.com:9092,dolinux753.hq.emirates.com:9092,dolinux768.hq.emirates.com:9092");
*/		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("request.required.acks", "1");
		return props;
		
		
	}
	
	public static Date addDay(Date date, int i){
		   //TODO you may want to check for a null date and handle it.
		   Calendar cal = Calendar.getInstance();
		   cal.setTime (date);
		   cal.add (Calendar.DATE, i);
		   return cal.getTime();
		}
	
	public static String dateFormat(Date date){
		
		DateFormat fmt = new SimpleDateFormat("DD/MM/YYYY");
		
		String fmtDate = fmt.format(date);
		
		return fmtDate;
	}
	
	public static String dateFormatJODA( int i){
		
		String inputDate = new DateTime().toString();
		String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
		String outputPattern = "dd/MM/yyyy";
		
		DateTime dt = DateTime.parse(inputDate, DateTimeFormat.forPattern(inputPattern));
		DateTime dtTemp = dt.plusDays(i);
		
		return dtTemp.toString(outputPattern);
		
		
		
		 
		/* DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
		// yyyy-MM-dd HH:mm:ss
	     //DateTime time = formatter.parseLocalTime(date.toString());
		 
	     String dateTimeStr = new DateTime().toString();
	     System.out.println(dateTimeStr);
		DateTime dtime = formatter.parseDateTime("2017-01-03T11:33:52.117+04:00");
	     System.out.println(dtime);
	     
	     dtime.plusDays(i);
	     System.out.println(formatter.print(dtime));
	     return dtime.toString();*/
	}

}
