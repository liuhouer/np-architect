package com.test;

public class DistributedClusterTest {

    public String distributedSession = "global-1001";

    public void UserSystem() {
        String userSession = "user-2001";

        System.out.println(distributedSession);
//        System.out.println(orderSession);
    }

    public void OrderSystem() {
        String orderSession = "order-3001";

        System.out.println(distributedSession);
//        System.out.println(userSession);
    }
}
