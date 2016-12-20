package com.datathon.pricing.consumer.util;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

import com.datathon.pricing.consumer.model.PriceEvent;

public  class WindowUtil {
	
	static WindowedStream createSlidingWindow(KeyedStream value,int counter,int slide) {
		return value.countWindow(counter, slide);
	}

}
