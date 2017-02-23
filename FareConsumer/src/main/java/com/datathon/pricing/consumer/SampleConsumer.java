/*
 * Copyright (c) 2016 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datathon.pricing.consumer;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import reactor.core.Cancellation;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.receiver.Receiver;
import reactor.kafka.receiver.ReceiverOffset;

/**
 * Sample consumer application using Reactive API for Kafka.
 * To run sample consumer
 * <ol>
 *   <li> Start Zookeeper and Kafka server
 *   <li> Create Kafka topic {@link #TOPIC}
 *   <li> Update {@link #BOOTSTRAP_SERVERS} and {@link #TOPIC} if required
 *   <li> Run {@link SampleConsumer} as Java application with all dependent jars in the CLASSPATH (eg. from IDE).
 *   <li> Shutdown Kafka server and Zookeeper when no longer required
 * </ol>
 */
public class SampleConsumer {

    private static final Logger log = LoggerFactory.getLogger(SampleConsumer.class.getName());

    private static final String BOOTSTRAP_SERVERS = "10.100.12.165:9092";
    private static final String TOPIC = "reactive";

    private final ReceiverOptions<Integer, String> receiverOptions;
    private final SimpleDateFormat dateFormat;

    public SampleConsumer(String bootstrapServers) {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "sample-consumer1");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group111");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
       // props.put(ConsumerConfig., arg1)
        receiverOptions = ReceiverOptions.create(props);
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
    }

    public Cancellation consumeMessages(String topic, CountDownLatch latch) {

        ReceiverOptions<Integer, String> options = receiverOptions.subscription(Collections.singleton(topic))
                .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));
        Flux<ReceiverRecord<Integer, String>> kafkaFlux = Receiver.create(options).receive();
        return kafkaFlux.subscribe(message -> {
                ReceiverOffset offset = message.offset();
                ConsumerRecord<Integer, String> record = message.record();
                System.out.println("The Message Received is " + message);
                System.out.printf("Received message: topic-partition=%s offset=%d timestamp=%s key=%s value=%s\n",
                        offset.topicPartition(),
                        offset.offset(),
                        dateFormat.format(new Date()),
                        record.key(),
                        record.value());
                //Flux<PriceEvent> fluxEvent = Flux.
                latch.countDown();
            });
    }

    public static void main(String[] args) throws Exception {
        int count = 20;
        CountDownLatch latch = new CountDownLatch(count);
        SampleConsumer consumer = new SampleConsumer(BOOTSTRAP_SERVERS);
        Cancellation cancellation = consumer.consumeMessages(TOPIC, latch);
        latch.await(10, TimeUnit.SECONDS);
        cancellation.dispose();
    }
}