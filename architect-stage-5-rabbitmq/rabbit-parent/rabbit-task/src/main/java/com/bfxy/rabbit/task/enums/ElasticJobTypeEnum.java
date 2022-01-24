package com.bfxy.rabbit.task.enums;

public enum ElasticJobTypeEnum {

	SIMPLE("SimpleJob", "简单类型job"),
	DATAFLOW("DataflowJob", "流式类型job"),
	SCRIPT("ScriptJob", "脚本类型job");
	
	private String type;
	
	private String desc;
	
	private ElasticJobTypeEnum(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
