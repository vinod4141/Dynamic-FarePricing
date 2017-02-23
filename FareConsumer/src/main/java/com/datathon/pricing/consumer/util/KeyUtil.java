package com.datathon.pricing.consumer.util;

import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.datathon.pricing.consumer.model.PriceEvent;

public class KeyUtil {
	
	public int getKey(String key) {
	    int hashCode = 0;

			 try {
				
					byte[] result = null;
					StringBuffer buf = null;
					// String strKey = null;// = carrier+origin+destination+outboundDepartureDate+outBoundDepartureTime+compartment;
					MessageDigest md = MessageDigest.getInstance("MD5");
					// allocate room for the hash
					result = new byte[md.getDigestLength()];
					// calculate hash
					md.reset();
					md.update(key.getBytes());
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
	
	public int getPriceEventWOCarrKey(PriceEvent value){
		
		//System.out.println(value.toString());
		
/*		System.out.println("getPriceEventWOCarrKey::Key value->"+value.getOrigin()+"-"+value.getDestination()+"-"+value.getOutboundDepartureDate()
		+"-"+value.getCompartment());*/
		return getKey(value.getOrigin()+value.getDestination()+value.getOutboundDepartureDate()
					  +value.getCompartment());
		
	}

}
