package com.datathon.pricing.consumer.model;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.KeyedStream;

public class DPPriceKeyReduceFunction implements ReduceFunction {
	
		@Override
		public Object reduce(Object current, Object previous) throws Exception {
			// TODO Auto-generated method stub
			PriceEvent evtCurr = (PriceEvent) current;
			PriceEvent evtPrev = (PriceEvent) previous;
			
			if(Math.abs(new Integer(evtCurr.getPriceEXC())- new Integer(evtPrev.getPriceEXC()))
					/new Integer(evtCurr.getPriceEXC())>0.20) {
				//TODO
			}
			return evtCurr;
		
		}
	

}
