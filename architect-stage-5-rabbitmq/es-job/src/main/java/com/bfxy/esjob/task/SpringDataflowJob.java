/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.bfxy.esjob.task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfxy.esjob.entity.Foo;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;


public class SpringDataflowJob implements DataflowJob<Foo> {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataflowJob.class);
    
    @Override
    public List<Foo> fetchData(final ShardingContext shardingContext) {
    	System.err.println("--------------@@@@@@@@@@ 抓取数据集合...--------------");
    	List<Foo> list = new ArrayList<Foo>();
    	list.add(new Foo("001", "张三"));
    	list.add(new Foo("002", "李四"));
    	return list;
    }
    
    @Override
    public void processData(final ShardingContext shardingContext, final List<Foo> data) {
    	System.err.println("--------------@@@@@@@@@ 处理数据集合...--------------");
    }
}
