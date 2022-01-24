package com.imooc.jvm.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JVMTest6 {
    public static final Logger LOGGER = LoggerFactory.getLogger(JVMTest6.class);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000000; i++) {
            Person person = new Person("小明", 18);
            LOGGER.info("user = {},age = {}", person.getName(), person.getAge());
        }
        Thread.sleep(10000000L);
    }
}

class Person {
    private String name;

    private int age;

    public Person(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}