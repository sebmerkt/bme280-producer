package com.sensors;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProducerTest {

    MockProducer<Integer, MeasurementData> mockProducer;
    Producer producer;

    MockSchemaRegistryClient schemaRegistryClient;

    // Contains data sent so System.out during the test.
    private ByteArrayOutputStream systemOutContent;
    // Contains data sent so System.err during the test.
    private ByteArrayOutputStream systemErrContent;
    private final PrintStream originalSystemOut = System.out;
    private final PrintStream originalSystemErr = System.err;

    @Before
    public void setUp() {
        schemaRegistryClient = new MockSchemaRegistryClient();


        producer = new Producer();
        final Properties props = producer.initProperties();


        final SpecificAvroSerde<MeasurementData> particleAvroSerde = new SpecificAvroSerde<>();
        final Map<String, String> serdeConfig = new HashMap<>();
        serdeConfig.put(SCHEMA_REGISTRY_URL_CONFIG, props.getProperty("schema.registry.url"));
        particleAvroSerde.configure(
                serdeConfig, false);


        mockProducer = new MockProducer<>(false,
                new IntegerSerializer(), particleAvroSerde.serializer());

        producer.producer = mockProducer;

    }

//    @Test
//    public void testStreamFiles() throws IOException {
//
//        producer.filePaths= new String[]{"hourly_ozone_2020.csv"};
//        producer.fileCount= 1;
//
//        producer.streamFiles(2);
//
//        mockProducer.completeNext();
//
//        final List<ProducerRecord<Integer, MeasurementData>> records = mockProducer.history();
//
//        Assert.assertEquals(2, records.size());
//
//
//
//        final List<ProducerRecord<Integer, MeasurementData>> expectedList = new java.util.ArrayList<>();
//        expectedList.add(new ProducerRecord<>(producer.TOPIC, 0, MeasurementData.newBuilder()
//                .setTemperatureCelcius(0.008)
//                .setTemperatureFahrenheit(30.497478)
//                .setPressure(997.89)
//                .build()));
//        expectedList.add(new ProducerRecord<>(producer.TOPIC, 0, MeasurementData.newBuilder()
//                .setTemperatureCelcius(0.007)
//                .setTemperatureFahrenheit(30.497478)
//                .setPressure(997.89)
//                .build()));
//
//        for (final ProducerRecord<String, MeasurementData> kv : records) {
//            System.out.println(kv.toString());
//            final MeasurementData pd = kv.value();
//            System.out.println(pd.get("temperature_celcius")+", "+pd.get("temperature_fahrenheit"));
//            System.out.println(pd.get("timestamp"));
//
//            Assert.assertEquals(0, kv.key());
//            Assert.assertEquals(Producer.TOPIC, kv.topic());
//        }
//
////        Needs the full initialization of each record
////        assertThat(records, equalTo(expectedList));
//
//    }

}