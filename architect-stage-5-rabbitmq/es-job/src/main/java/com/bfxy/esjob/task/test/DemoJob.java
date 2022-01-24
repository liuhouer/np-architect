package com.bfxy.esjob.task.test;

import org.springframework.stereotype.Component;

import com.bfxy.rabbit.task.annotation.ElasticJobConfig;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

@Component
@ElasticJobConfig(
			name = "com.bfxy.esjob.task.test.DemoJob",
			cron = "0/10 * * * * ?",
			description = "样例定时任务",
			overwrite = true,
			shardingTotalCount = 2
		)
public class DemoJob implements SimpleJob {

	@Override
	public void execute(ShardingContext shardingContext) {
		System.err.println("执行Demo job.");
	}

}
