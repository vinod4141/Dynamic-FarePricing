package com.datathon.pricing.consumer.util;

import java.io.IOException;

import com.datathon.pricing.consumer.model.AlertDetail;
import com.datathon.pricing.consumer.model.PriceEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class EventUtil {
	
	public static PriceEvent getPriceEvent(String csv){
		PriceEvent event = new PriceEvent();
		
		String[] values = csv.split(",");
		//System.out.println("length of the array->"+values.length);
		event.setId(values[1]);
		event.setObservationDate(values[2]);
		event.setObservationTime(values[3]);
		event.setPos(values[4]);
		event.setOrigin(values[5]);
		event.setDestination(values[6]);
		event.setCarrier(values[7]);
		event.setOutboundDepartureDate(values[8]);
		event.setOutboundArrivalDate(values[9]);
		//event.setOutBoundArrivalTime(values[9]);
		event.setInboundDepartureDate(values[10]);
		event.setInboundArrivalDate(values[11]);
		//event.setInboundArrivalTime(values[11]);
		event.setOutboundBookingClass(values[14]);
		event.setInboundBookingClass(values[15]);
		event.setPriceEXC(values[16]);
		event.setPriceINC(values[17]);
		event.setTax(values[18]);
		event.setCurrency(values[19]);
		event.setSource(values[20]);
		event.setPriceInbound(values[22]);
		event.setPriceOutbound(values[21]);
		event.setIsTaxIncCutIn(values[23]);
		event.setCompartment(values[24]);
		event.setLoadDate(values[25]);
		event.setOutBoundDepartureTime(values[26]);
		event.setInboundDepartureTime(values[27]);
		event.setOutBoundArrivalTime(values[28]);
		//event.setInboundArrivalTime(values[29]);
		event.setOd(values[5]+values[6]);
		return event;
		
	}
	
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
	
	public static String getEventJSON( Object obj){
		String json = null;
		
		Gson gson = new Gson();
		json = gson.toJson(obj);
		
		System.out.println(json);
		return json;
	}

}
