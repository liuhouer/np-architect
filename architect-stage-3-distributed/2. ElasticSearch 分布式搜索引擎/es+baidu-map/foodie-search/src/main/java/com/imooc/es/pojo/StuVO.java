package com.imooc.es.pojo;

public class StuVO {

    private Long stuId;
    private String name;
    private String description;

    @Override
    public String toString() {
        return "StuVO{" +
                "stuId=" + stuId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Long getStuId() {
        return stuId;
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
