package com.datathon.pricing;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
import java.io.IOException;
//import java.io.OutputStream;
//import java.io.RandomAccessFile;
//import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;



//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.BytesWritable;
//import org.apache.hadoop.io.SequenceFile;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


import java.util.stream.Stream;

import kafka.javaapi.producer.Producer;
import kafka.message.Message;
import kafka.producer.ProducerConfig;
import kafka.producer.KeyedMessage;

public class KafkaCustomerProducer{
	
	public static void main(String[] args) throws IOException, InterruptedException {
		KafkaCustomerProducer  kcp  = new KafkaCustomerProducer();
		kcp.readFile(args[0], args[1]);
		//kcp.SequenceFileMessageProducer(args[0], args[1]);
		
	}

	
	private void readFile(String filepath, String kafkaTopic) throws InterruptedException{
		//try (Stream<String> lines = Files.lines (FileSystems.getDefault().getPath( filepath), StandardCharsets.UTF_8))
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(getkafkaConfig());
		
		//KeyedMessage<String,String> data = new KeyedMessage<String, String>(kafkaTopic,"a",);		
		
		
		
		try (Stream<String> lines = Files.lines (FileSystems.getDefault().getPath( filepath)))
		{
			int i = 1;
			for (String line : (Iterable<String>) lines::iterator)
		    {
				
				 producer.send(new ProducerRecord<String, String>(kafkaTopic, "a", line));
				 System.out.println("--"+line);
				 System.out.println("--"+i);
				if (i%100==0){
					Thread.sleep(5000);
				}
				 i++;
				 
				 
		    }
			producer.close();
		}
	 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 
	}
	
	private Properties getkafkaConfig(){
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("metadata.broker.list", "localhost:9092");
/*		props.put("bootstrap.servers", "dolinux752.hq.emirates.com:9092,dolinux753.hq.emirates.com:9092,dolinux768.hq.emirates.com:9092");
		props.put("metadata.broker.list", "dolinux752.hq.emirates.com:9092,dolinux753.hq.emirates.com:9092,dolinux768.hq.emirates.com:9092");
*/		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("request.required.acks", "1");
		return props;
		
		
	}
	
	private Properties getkafkaByteConfig(){
		Properties props = new Properties();
/*		props.put("bootstrap.servers", "localhost:9092");
		props.put("metadata.broker.list", "localhost:9092");
*/
		props.put("partitioner.class", "test.SimplePartitioner");
		props.put("key.serializer", "org.apache.kafka.common.serialization.bytearrayserializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.bytearrayserializer");
		props.put("request.required.acks", "1");
		return props;
		
		
	}
	
	
/*	private void SequenceFileMessageProducer(String fileName, String kafkaTopic){
		
		File xmlFile = new File( fileName);
		
		
		OutputStream outputStream = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			 outputStream = new FileOutputStream (fileName);
			 stream.writeTo(outputStream);
			 outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] b = "There".getBytes();
		
		 Message m = new Message(stream.toByteArray());

		 Producer<byte[], byte[]> producer = new Producer<byte[], byte[]>(new ProducerConfig(getkafkaByteConfig()));
         KeyedMessage<byte[], byte[]> data = new KeyedMessage<byte[], byte[]>("abc", b, b);
         producer.close();
		 Producer kafkaProducer = new Producer<String, Message>(new ProducerConfig(getkafkaByteConfig()));
		 kafkaProducer.send(new KeyedMessage<String, byte[]>("pax10", "my-partition-key", b));
		 kafkaProducer.close();
	}
*/	
	
	
	
	
	
	
}
	
	
	
	
