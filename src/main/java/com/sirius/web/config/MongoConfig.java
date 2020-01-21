package com.sirius.web.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
  @Bean
  public MongoClient mongo() {

    MongoClientURI uri = new MongoClientURI(
        "mongodb+srv://nur:sirius123@recycle-i2lwq.mongodb.net/test?retryWrites=true&w=majority");

    MongoClient mongoClient = new MongoClient(uri);
    MongoDatabase database = mongoClient.getDatabase("test");
    return mongoClient;
  }


  MongoClientURI uri = new MongoClientURI(
      "mongodb+srv://nur:sirius123@recycle-i2lwq.mongodb.net/test?retryWrites=true&w=majority");

  MongoClient mongoClient = new MongoClient(uri);
  MongoDatabase database = mongoClient.getDatabase("test");

  @Bean
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(mongo(), "test");
  }
}
