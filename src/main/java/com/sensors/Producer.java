package com.sensors;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.common.serialization.IntegerSerializer;

public class Producer {

    static Map<String, String> env = System.getenv();
    // Input topic
    static String TOPIC = env.get("TOPIC");
    static MeasurementData measurementData = new MeasurementData();
    static org.apache.kafka.clients.producer.Producer producer;
    static Bme280 bme280;
    static Properties props;


    public Producer(){
        props = initProperties();
        producer = new KafkaProducer<>(props);
    }

    public static void main(final String[] args) throws IOException {
        final Producer producer = new Producer();

        streamFiles();
    }

    public static void streamFiles() throws IOException {
        final Integer sensor_id = 0;

        while (true) {
            bme280.measure();

            measurementData.setTimestamp(bme280.getTimestamp());
            measurementData.setTemperatureCelsius(bme280.getTempCelcius());
            measurementData.setTemperatureFahrenheit(bme280.getTempFahrenheit());
            measurementData.setHumidity(bme280.getHumidity());
            measurementData.setPressure(bme280.getPressure());

            // Send the message to the Kafka input topic
            producer.send(new ProducerRecord<>(TOPIC, sensor_id, measurementData));
            try {
                // Control the frequency of messages to be sent
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (final InterruptedException e) {
                break;
            }
        }

    }

    // Initialize Kafka producer properties
    static Properties initProperties() {

        // Read out the Kafka specific environment variables
        final Map<String, String> env = System.getenv();
        final String BROKER1 = env.get("BROKER1");
        final String BROKER_PORT = env.get("BROKER_PORT");
        final String SCHEMAREGISTRYURL = "http://"+env.get("SCHEMAREGISTRYIP")+":8081";

        // Construct the properties
        final Properties props = new Properties();

        // Addresses of the Kafka Zookeeper and Brokers
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER1 + ":" + BROKER_PORT);
        // All messages require acknowledgement
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        // Do not retry
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        // Define key and value serializers
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put("schema.registry.url", SCHEMAREGISTRYURL);

        return props;
    }
}
