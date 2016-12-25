package com.datathon.pricing.consumer.model;

import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

public class PriceEvent {
	
	   private String sr_no;
	   private String id=""; 
	   private String observationDate=""; 
	   private String observationTime="" ;
	   private String pos="" ;
	   private String origin="";
	   private String destination="" ;
	   private String carrier="" ;
	   private String outboundDepartureDate="" ;
	   private String outboundArrivalDate="" ;
	   private String inboundDepartureDate="" ;
	   private String inboundArrivalDate="" ;
	   private String outboundFareBasis="" ;
	   private String inboundFareBasis="" ;
	   private String outboundBookingClass="" ;
	   private String inboundBookingClass="" ;
	   private String priceEXC="" ;
	   private String priceINC="" ;
	   private String tax=""  ;
	   private String currency="" ;
	   private String source="" ;
	   private String priceOutbound="";
	   private String priceInbound="" ;
	   private String isTaxIncCutIn="" ;
	   private String compartment="" ;
	   private String loadDate="" ;
	   private String outBoundDepartureTime="" ;
	   private String inboundDepartureTime="" ;
	   private String OutBoundArrivalTime="" ;
	   private String InboundArrivalTime="" ;
	   private String od="";
	   private Double priceChange=0.0;
	   
	public String getSr_no() {
		return sr_no;
	}
	public void setSr_no(String sr_no) {
		this.sr_no = sr_no;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObservationDate() {
		return observationDate;
	}
	public void setObservationDate(String observationDate) {
		this.observationDate = observationDate;
	}
	public String getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getOutboundDepartureDate() {
		return outboundDepartureDate;
	}
	public void setOutboundDepartureDate(String outboundDepartureDate) {
		this.outboundDepartureDate = outboundDepartureDate;
	}
	public String getOutboundArrivalDate() {
		return outboundArrivalDate;
	}
	public void setOutboundArrivalDate(String outboundArrivalDate) {
		this.outboundArrivalDate = outboundArrivalDate;
	}
	public String getInboundDepartureDate() {
		return inboundDepartureDate;
	}
	public void setInboundDepartureDate(String inboundDepartureDate) {
		this.inboundDepartureDate = inboundDepartureDate;
	}
	public String getInboundArrivalDate() {
		return inboundArrivalDate;
	}
	public void setInboundArrivalDate(String inboundArrivalDate) {
		this.inboundArrivalDate = inboundArrivalDate;
	}
	public String getOutboundFareBasis() {
		return outboundFareBasis;
	}
	public void setOutboundFareBasis(String outboundFareBasis) {
		this.outboundFareBasis = outboundFareBasis;
	}
	public String getInboundFareBasis() {
		return inboundFareBasis;
	}
	public void setInboundFareBasis(String inboundFareBasis) {
		this.inboundFareBasis = inboundFareBasis;
	}
	public String getOutboundBookingClass() {
		return outboundBookingClass;
	}
	public void setOutboundBookingClass(String outboundBookingClass) {
		this.outboundBookingClass = outboundBookingClass;
	}
	public String getInboundBookingClass() {
		return inboundBookingClass;
	}
	public void setInboundBookingClass(String inboundBookingClass) {
		this.inboundBookingClass = inboundBookingClass;
	}
	public String getPriceEXC() {
		return priceEXC;
	}
	public void setPriceEXC(String priceEXC) {
		this.priceEXC = priceEXC;
	}
	public String getPriceINC() {
		return priceINC;
	}
	public void setPriceINC(String priceINC) {
		this.priceINC = priceINC;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPriceOutbound() {
		return priceOutbound;
	}
	public void setPriceOutbound(String priceOutbound) {
		this.priceOutbound = priceOutbound;
	}
	public String getPriceInbound() {
		return priceInbound;
	}
	public void setPriceInbound(String priceInbound) {
		this.priceInbound = priceInbound;
	}
	public String getIsTaxIncCutIn() {
		return isTaxIncCutIn;
	}
	public void setIsTaxIncCutIn(String isTaxIncCutIn) {
		this.isTaxIncCutIn = isTaxIncCutIn;
	}
	public String getCompartment() {
		return compartment;
	}
	public void setCompartment(String compartment) {
		this.compartment = compartment;
	}
	public String getLoadDate() {
		return loadDate;
	}
	public void setLoadDate(String loadDate) {
		this.loadDate = loadDate;
	}
	public String getOutBoundDepartureTime() {
		return outBoundDepartureTime;
	}
	public void setOutBoundDepartureTime(String outBoundDepartureTime) {
		this.outBoundDepartureTime = outBoundDepartureTime;
	}
	public String getInboundDepartureTime() {
		return inboundDepartureTime;
	}
	public void setInboundDepartureTime(String inboundDepartureTime) {
		this.inboundDepartureTime = inboundDepartureTime;
	}
	public String getOutBoundArrivalTime() {
		return OutBoundArrivalTime;
	}
	public void setOutBoundArrivalTime(String outBoundArrivalTime) {
		OutBoundArrivalTime = outBoundArrivalTime;
	}
	public String getInboundArrivalTime() {
		return InboundArrivalTime;
	}
	public void setInboundArrivalTime(String inboundArrivalTime) {
		InboundArrivalTime = inboundArrivalTime;
	}
	public String getOd() {
		return od;
	}
	public void setOd(String od) {
		this.od = od;
	}
	   
	public boolean equals(Object obj) {
		return (this == obj);
	}
	
	public Double getPriceChange() {
		return priceChange;
	}
	public void setPriceChange(Double priceChange) {
		this.priceChange = priceChange;
	}

	
	public int hashCode() {
	    int hashCode = 0;

		 try {
			
				byte[] result = null;
				StringBuffer buf = null;
				 String strKey = carrier+origin+destination+outboundDepartureDate+outBoundDepartureTime+compartment;
				MessageDigest md = MessageDigest.getInstance("MD5");
				// allocate room for the hash
				result = new byte[md.getDigestLength()];
				// calculate hash
				md.reset();
				md.update(strKey.getBytes());
				result = md.digest();
			
        	    hashCode=  ByteBuffer.wrap(result).getInt();
	
		 } catch (NoSuchAlgorithmException cnse) {
		     try {
				throw new DigestException("couldn't make digest of partial content");
			} catch (DigestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		return hashCode;
	}
	/**
	 * Gives the logical stream based on the PriceEvent key.
	 * @param event
	 * @return
	 */
	
	KeyedStream<PriceEvent,Integer> getPriceEventKey(DataStream<PriceEvent> event) {
		KeyedStream<PriceEvent,Integer> priceKey = event
				  .keyBy(new KeySelector<PriceEvent, Integer>() {
				     public Integer getKey(PriceEvent priceEvent) { return priceEvent.hashCode(); }
				   });
		
		return priceKey;
	}
	/**
	 * Returns the sliding window.
	 * @param value
	 * @return
	 */
	
	WindowedStream<PriceEvent,Integer,GlobalWindow> createSlidingWindow(KeyedStream<PriceEvent,Integer> value) {
		return value.countWindow(2, 1);
	}
	
	public String toString() {
		return carrier + ":" + origin + ":" + destination + ":" + outboundDepartureDate + ":" + outBoundDepartureTime
				+ ":" + compartment + ":" + priceINC;
	}
	
public static void main(String[] args) {
	DataStream<PriceEvent> streamFiletered; //Filetered data stream.
	//streamFiletered.keyBy(key) keysteam.window.reduce
}

	   
	
	   
	
}
