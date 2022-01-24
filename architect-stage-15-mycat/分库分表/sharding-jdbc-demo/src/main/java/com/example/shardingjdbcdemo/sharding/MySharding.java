package com.example.shardingjdbcdemo.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class MySharding implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Long id = shardingValue.getValue();

        long mode =id % availableTargetNames.size();
        String[] strings = availableTargetNames.toArray(new String[0]);
        mode = Math.abs(mode);

        System.out.println(strings[0]+"---------"+strings[1]);
        System.out.println("mode="+mode);
        return strings[(int)mode];
    }
}
