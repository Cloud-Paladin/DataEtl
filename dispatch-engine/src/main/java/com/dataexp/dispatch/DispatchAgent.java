package com.dataexp.dispatch;

import com.alibaba.fastjson.JSONObject;
import com.dataexp.common.zookeeper.Constant;
import com.dataexp.dispatch.watcher.JobNodeWatcher;
import com.dataexp.common.zookeeper.entity.JobNode;
import com.dataexp.common.zookeeper.entity.JobStatus;
import com.dataexp.common.zookeeper.entity.JobTask;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 调度服务器Zookeeper通信类
 * @author: Bing.Li
 * @create: 2019-01-28
 */
public class DispatchAgent {

    private static final Logger LOG = LoggerFactory.getLogger(DispatchAgent.class);
    private static ZooKeeper zk;
    private static CountDownLatch lock;

    /**
     * jobnodemap,key为jobnode的id，,value为jobnode的具体信息
     */
    private static Map<String, JobNode> jobNodeMap;
    /**
     * jobtaskmap,key为jobtask的id,value为jobtask
     */
    private static Map<String, JobTask> jobTaskMap;
    /**
     * jobstatusmap,key为jobtaskde的id，value为一个map，
     * map的key是jobnode的id，value为该jobnode对该jobtask的
     * 状态和运行数据统计的集合
     */
    private static Map<String, Map<String, JobStatus>> jobStatusMap;

    private static JobNodeWatcher jobNodeWatcher = new JobNodeWatcher();


    /**
     * 系统启动时初始化框架Zookeeper的必须路径
     *
     * @return true:初始化成功 false:初始化失败
     */
    public static boolean initPath() {
        try {
            lock = new CountDownLatch(1);
            zk = new ZooKeeper(Constant.HOSTPORT, 10000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.None) {
                        lock.countDown();
                    }
                }
            });
            lock.await();
            if (zk != null) {
                //如果chroot地址不存在，则初始化该节点
                if (null == zk.exists(Constant.ROOT, false)) {
                    zk.create(Constant.ROOT, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                zk.close();
                //重新以chroot为根路径连接到Zookeeper
                lock = new CountDownLatch(1);
                zk = new ZooKeeper(Constant.HOSTPORT + Constant.ROOT, 10000, new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        if (event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.None) {
                            lock.countDown();
                        }
                    }
                });
                lock.await();
                //初始化必须的节点
                if (zk.exists(Constant.JOB_NODE, null) == null) {
                    zk.create(Constant.JOB_NODE, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                if (zk.exists(Constant.JOB_TASK, null) == null) {
                    zk.create(Constant.JOB_TASK, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                if (zk.exists(Constant.JOB_STATUS, null) == null) {
                    zk.create(Constant.JOB_STATUS, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                if (zk.exists(Constant.INTERFACE_NODE, null) == null) {
                    zk.create(Constant.INTERFACE_NODE, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                if (zk.exists(Constant.INTERFACE_TASK, null) == null) {
                    zk.create(Constant.INTERFACE_TASK, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                if (zk.exists(Constant.INTERFACE_STATUS, null) == null) {
                    zk.create(Constant.INTERFACE_STATUS, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }
            return true;
        } catch (IOException e) {
            LOG.error("error occcured:", e);;
        } catch (InterruptedException e) {
            LOG.error("error occcured:", e);;
        } catch (KeeperException e) {
            LOG.error("error occcured:", e);;
        } finally {
            try {
                zk.close();
                zk = null;
            } catch (InterruptedException e) {
                LOG.error("error occcured:", e);;
            }
        }
        return false;
    }

    public static void initData() {
        try {
            jobNodeMap = new ConcurrentHashMap<String, JobNode>();
            jobTaskMap = new ConcurrentHashMap<String, JobTask>();
            jobStatusMap = new ConcurrentHashMap<String, Map<String, JobStatus>>();
            lock = new CountDownLatch(1);
            zk = new ZooKeeper(Constant.HOSTPORT + Constant.ROOT, 60000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.None) {
                        lock.countDown();
                    } else if (event.getState() == Event.KeeperState.Expired) {
                        LOG.error("session expired, now rebuilding");
                        try {
                            zk.close();
                        } catch (InterruptedException e) {
                            LOG.error("error occcured:", e);
                            zk = null;
                        }
                        /**
                         * 重新开始连接并初始化数据
                         */
                        initData();
                    }
                }
            });
            lock.await();
            if (null != zk) {
                initJobNode();
                //TODO:获取所有任务的节点状态和统计
                //TODO:从数据库获取有效任务列表（运行和停止的），和Zookeeper做一致性检查


                //TODO:获取所有接口节点
                //TODO:获取所有接口任务的节点状态和统计
            }
        } catch (IOException e) {
            LOG.error("error occcured:", e);;
        } catch (InterruptedException e) {
            LOG.error("error occcured:", e);;
        } catch (KeeperException e) {
            LOG.error("error occcured:", e);;
        }
    }

    /**
     * 初始化任务节点信息
     */
    public static void initJobNode() throws KeeperException, InterruptedException, UnsupportedEncodingException {
        //初始化所有的jobnode
        List<String> list = zk.getChildren(Constant.JOB_NODE, jobNodeWatcher);
        String nodeContent;
        for (String jobNodeId : list) {
            if (null != zk.exists(Constant.JOB_NODE + "/" + jobNodeId, null)) {
                nodeContent = new String(zk.getData(Constant.JOB_NODE + "/" + jobNodeId, null, null), "UTF-8");
                if (!"".equals(nodeContent)) {
                    JobNode node = JSONObject.parseObject(nodeContent, JobNode.class);
                    addJobNode(jobNodeId, node);
                }
            }
        }
        System.out.println("init jobnodmap zize:" + jobNodeMap.size());
    }


    public static CountDownLatch getLock() {
        return lock;
    }

    public static Map<String, JobNode> getJobNodeMap() {
        return jobNodeMap;
    }

    public static boolean addJobNode(String nodeId, JobNode node) {
        return null == jobNodeMap.put(nodeId, node) ? true : false;
    }

    public static boolean removeJobNode(String nodeId) {
        return null == jobNodeMap.remove(nodeId) ? false : true;
    }

    public static Map<String, JobTask> getJobTaskMap() {
        return jobTaskMap;
    }

    public static Map<String, Map<String, JobStatus>> getJobStatusMap() {
        return jobStatusMap;
    }

    public static ZooKeeper getZk() {
        return zk;
    }

    public static List<String> getChanged(List<String> orgin, List<String> current) {
        List<String> changedList = new ArrayList<>();
        for (String c : current) {
            if (!orgin.contains(c)) {
                changedList.add(c);
            }
        }
        return changedList;
    }

    public static void main(String[] args)
            throws InterruptedException, KeeperException, UnsupportedEncodingException {
        initPath();
        initData();
        while (true) {
            Thread.sleep(500000);
        }
    }
}
