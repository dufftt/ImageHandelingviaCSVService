/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.csv_handeling.Kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

// import com.fasterxml.jackson.databind.JsonSerializer;
/*
 * cd opt/kafka/bin
 * Run sh kafka-topics.sh --bootstrap-server localhost:9090 --create --topic csv-handling-topic --partitions 1 --replication-factor 1
 * to create a topic named csv-handling-topic
 */
@Configuration
public class KafkaProducerConfig {

       @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String,KafkaMessage> producerFactory(){
        Map<String,Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);
        
        
        return new DefaultKafkaProducerFactory<String, KafkaMessage>(configProps);
    }
    @Bean
    public KafkaTemplate<String, KafkaMessage> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    /*
     * Written method to send message to kafka but instead
     * of creating autowired kafkaTemplate bean, we are using
     * kafkaTemplate() method to create a new instance of KafkaTemplate.
     * This is not the best practice but for simplicity, we are doing this.
     */
    public void sendMessage(String topic, KafkaMessage message){
        kafkaTemplate().send(topic,message);
    }
}