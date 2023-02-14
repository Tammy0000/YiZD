package com.isandy.yizd.Service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
/*
  解决定时任务调度：Unexpected error occurred in scheduled task问题
  2023年2月6日10:06:36
 */
public class TaskGetBean implements ApplicationContextAware {

    private static ApplicationContext contextAware;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        contextAware = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return contextAware;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

}
