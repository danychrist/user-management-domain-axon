package com.springbank.user.core.configuration;


import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import lombok.var;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoFactory;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoSettingsFactory;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration //tells Spring framework that this is a configuration class
public class AxonConfig {

    /*The value of the below variables/fields will be added in the application.properties file
      in the user.cmt.api and user.query.api project (under main-> resources)
     */
    @Value("${spring.data.mongodb.host:127.0.0.1}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port:27017}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database:user}")
    private String mongoDatabase;
    /*NOTE: The @Value notation tells the spring where to get the values for the variables:
    - spring.data.mongodb.host
    - spring.data.mongodb.port
    - spring.data.mongodb.database
     */

    /*@Bean means that the method produces a Bean to be managed by the spring container*/

    @Bean
    public MongoClient mongo(){
       var mongoFactory = new MongoFactory();
       var mongoSettingFactory = new MongoSettingsFactory();
       mongoSettingFactory.setMongoAddresses(Collections.singletonList(new ServerAddress(mongoHost,mongoPort)));
       mongoFactory.setMongoClientSettings(mongoSettingFactory.createMongoClientSettings());
       return mongoFactory.createMongo();
    }



    @Bean
    public MongoTemplate axonMongoTemplate(){
        return DefaultMongoTemplate.builder()
                .mongoDatabase(mongo(), mongoDatabase)
                .build();
    }
    /*TokenStore. Describes a component capable of storing and retrieving event tracking tokens. An EventProcessor
      that is tracking an event stream can use the store to keep track of its position in the event stream.*/
    @Bean
    public TokenStore tokenStore(Serializer serializer){
        return MongoTokenStore.builder()
                .mongoTemplate(axonMongoTemplate())
                .serializer(serializer)
                .build();
    }
    /*Provides a mechanism to append as well as retrieve events from an underlying storage like a database.
      An event storage engine can also be used to store and fetch aggregate snapshot events.*/
    @Bean
    public EventStorageEngine eventStorageEngine(MongoClient client) {
        return MongoEventStorageEngine.builder()
                .mongoTemplate(DefaultMongoTemplate.builder().mongoDatabase(client).build()).build();
    }
    /*EmbeddedEventStore is an implementation of Event Bus
      The EventBus is the mechanism that dispatches events to the subscribed event handlers.*/
    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine eventStorageEngine, AxonConfiguration configuration){
        return EmbeddedEventStore.builder()
                .storageEngine(eventStorageEngine)
                .messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
                .build();
    }
    /*Now let's tell the user.cmd and user.query to make use of the AxonConfig class
     by adding the @Import({AxonConfig.class}) in the UserCommandApplication and UserQueryApplication.
     Also and the the user.core as a dependency in each pom.xml*/

}

