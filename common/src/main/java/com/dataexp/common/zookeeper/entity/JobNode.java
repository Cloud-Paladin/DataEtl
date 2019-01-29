package com.dataexp.common.zookeeper.entity;

import java.util.Objects;

/**
 * @author: Bing.Li
 * @create: 2019-01-29
 */
public class JobNode {

    private String jobId;
    private String Content;

    public JobNode() {
    }

    public JobNode(String jobId, String content) {
        this.jobId = jobId;
        Content = content;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobNode jobNode = (JobNode) o;
        return jobId.equals(jobNode.jobId) && Content.equals(jobNode.Content);
    }

    @Override
    public String toString() {
        return "JobNode{" +
                "jobId='" + jobId + '\'' +
                ", Content='" + Content + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, Content);
    }
}
