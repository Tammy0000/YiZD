package com.isandy.yizd.Service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import javax.annotation.Resource;

/**
 * 去除mongo中_class
 * 2023年2月19日21:02:44
 */
@Configuration
public class MongoConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Resource
    MongoTemplate mongoTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        MongoConverter converter = mongoTemplate.getConverter();
        if (converter.getTypeMapper().isTypeKey("_class")) {
            ((MappingMongoConverter) converter).setTypeMapper(new DefaultMongoTypeMapper(null));
        }
    }
}
