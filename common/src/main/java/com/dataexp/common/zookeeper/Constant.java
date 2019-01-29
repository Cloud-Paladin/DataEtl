package com.dataexp.common.zookeeper;

/**
 * 系统各节点的Zookeeper根地址
 *
 * @author: Bing.Li
 * @create: 2019-01-28
 */
public class Constant {

    public static final String HOSTPORT= "localhost:2181";
    public static final String ROOT = "/data_dist_clean";
    public static final String JOB_NODE = "/job_node";
    public static final String JOB_TASK = "/job_task";
    public static final String JOB_STATUS = "/job_status";
    public static final String INTERFACE_NODE = "/interface_node";
    public static final String INTERFACE_TASK = "/interface_task";
    public static final String INTERFACE_STATUS = "/interface_status";

}
