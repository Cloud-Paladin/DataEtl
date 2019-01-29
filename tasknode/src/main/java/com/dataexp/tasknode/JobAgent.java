package com.dataexp.tasknode;

import com.alibaba.fastjson.JSON;
import com.dataexp.common.zookeeper.Constant;
import com.dataexp.common.zookeeper.entity.JobNode;
import io.opencensus.stats.Aggregation;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

/**
 * 任务节点服务器Zookeeper通信类
 *
 * @author: Bing.Li
 * @create: 2019-01-29
 */
public class JobAgent {

    private static final Logger LOG = LoggerFactory.getLogger(JobAgent.class);
    private static ZooKeeper zk;
    private static CountDownLatch lock;
    private static final String jobNodeId = "node1"+Math.random();

    /**
     * 初始化连接以及从Zookeeper获取初始化的成员
     */
    public static void initData() {
        try {
            lock = new CountDownLatch(1);
            zk = new ZooKeeper(Constant.HOSTPORT + Constant.ROOT, 2000, new Watcher() {
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
                //向服务器注册自己
                registerSelf();
            }
        } catch (InterruptedException e) {
            LOG.error("error occured:", e);
        } catch (IOException e) {
            LOG.error("error occured:", e);
        } catch (KeeperException e) {
            LOG.error("error occured:", e);
        }
    }

    /**
     *  向服务器注册自己
     */
    public static void registerSelf() throws UnsupportedEncodingException, KeeperException, InterruptedException {
        JobNode node1 = new JobNode(jobNodeId, "节点内容");
        String content = JSON.toJSONString(node1);
        if (null == zk.exists(Constant.JOB_NODE + "/" + jobNodeId, null)) {
            zk.create(Constant.JOB_NODE + "/" + jobNodeId, content.getBytes("UTF-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }
    }

    public static ZooKeeper getZk() {
        return zk;
    }

    public static CountDownLatch getLock() {
        return lock;
    }

    public static void main(String[] args) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        initData();
        Thread.sleep(50000);
    }

}
