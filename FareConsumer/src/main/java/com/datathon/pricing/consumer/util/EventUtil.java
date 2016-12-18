package com.datathon.pricing.consumer.util;

import com.datathon.pricing.consumer.model.PriceEvent;
import com.google.gson.Gson;

public class EventUtil {
	
	public static PriceEvent getPriceEvent(String csv){
		PriceEvent event = new PriceEvent();
		
		String[] values = csv.split(",");
		
		event.setId(values[1]);
		event.setObservationDate(values[2]);
		event.setObservationTime(values[3]);
		event.setPos(values[4]);
		event.setOrigin(values[5]);
		event.setDestination(values[6]);
		event.setCarrier(values[7]);
		event.setOutboundDepartureDate(values[8]);
		event.setOutBoundArrivalTime(values[9]);
		event.setInboundDepartureDate(values[10]);
		event.setInboundArrivalTime(values[11]);
		event.setOutboundBookingClass(values[12]);
		event.setInboundBookingClass(values[13]);
		event.setPriceEXC(values[14]);
		event.setPriceINC(values[15]);
		event.setTax(values[16]);
		event.setCurrency(values[17]);
		event.setSource(values[18]);
		event.setPriceInbound(values[19]);
		event.setPriceOutbound(values[20]);
		event.setIsTaxIncCutIn(values[21]);
		event.setCompartment(values[22]);
		event.setLoadDate(values[23]);
		event.setOutBoundDepartureTime(values[24]);
		event.setInboundDepartureTime(values[25]);
		event.setOutBoundArrivalTime(values[26]);
		event.setInboundArrivalTime(values[27]);
		event.setOd(values[5]+values[6]);
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
