package com.dataexp.common.zookeeper;

/**
 * 系统各节点的Zookeeper根地址
 *
 * @author: Bing.Li
 * @create: 2019-01-28
 */
public class ZookeeperConstant {

    public static final String ROOT_PATH = "/datadistclean";
    public static final String WORKBEATS_PATH = "/workbeats";
    public static final String INTERFACE_PATH = "/interface";
    public static final String TASKNODE_PATH = "/tasknode";
    public static final String TOPOLOGY_PATH = "/topology";
    public static final String INTERFACENODE_PATH = "/interfacenode";
    public static final String WORKBEAT_TASKNODE_PATH = WORKBEATS_PATH + TASKNODE_PATH;
    public static final String WORKBEAT_INTERFACENODE_PATH = WORKBEATS_PATH + INTERFACE_PATH;
}
