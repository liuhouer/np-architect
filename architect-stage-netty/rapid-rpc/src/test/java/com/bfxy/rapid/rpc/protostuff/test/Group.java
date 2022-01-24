package com.bfxy.rapid.rpc.protostuff.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Group {
	
    private String id;

    private String name;

    private User user;
    
}