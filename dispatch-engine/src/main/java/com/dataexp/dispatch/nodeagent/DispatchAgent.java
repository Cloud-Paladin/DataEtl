package com.dataexp.dispatch.nodeagent;

import com.dataexp.common.zookeeper.Constant;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author: Bing.Li
 * @create: 2019-01-28
 */
public class DispatchAgent implements Watcher, Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(DispatchAgent.class);
    private static ZooKeeper zk;
    private CountDownLatch lock = new CountDownLatch(1);
    List<String> parentList = new ArrayList<>();


    /**
     * 系统启动时初始化框架Zookeeper的必须路径
     * @return true:初始化成功 false:初始化失败
     */
    public static boolean initPath() {
        try {
            ZooKeeper zk = new ZooKeeper(Constant.HOSTPORT, 10000, null);
            CountDownLatch Lock = new CountDownLatch(1);
            Lock.await();
            if (zk != null) {
                /**
                 * 如果chroot地址不存在，则初始化该节点
                 */
                if (null == zk.exists(Constant.ROOT, false)) {
                    zk.create(Constant.ROOT, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    zk.close();
                }
                //重新以chroot为根路径连接到Zookeeper
                Lock = new CountDownLatch(1);
                zk = new ZooKeeper(Constant.HOSTPORT + Constant.ROOT, 2000, null);
                Lock.await();
                //Create the znode if it doesn't exist, with the following code:
                if (zk.exists(Constant.WORKBEATS, null) == null) {
                    zk.create(Constant.WORKBEATS, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                if (zk.exists(Constant.WORKBEATS + Constant.JOB_NODE, null) == null) {
                    zk.create(Constant.WORKBEATS + Constant.JOB_NODE, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                if (zk.exists(Constant.WORKBEATS + Constant.INTERFACE_NODE, null) == null) {
                    zk.create(Constant.WORKBEATS + Constant.INTERFACE_NODE, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                if (zk.exists(Constant.JOB, null) == null) {
                    zk.create(Constant.JOB, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                if (zk.exists(Constant.INTERFACE, null) == null) {
                    zk.create(Constant.INTERFACE, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }
            zk.close();
            return true;
        } catch (IOException e) {
            LOG.error(e.getStackTrace().toString());
        } catch (InterruptedException e) {
            LOG.error(e.getStackTrace().toString());
        } catch (KeeperException e) {
            LOG.error(e.getStackTrace().toString());
        }finally {
            try {
                zk.close();
            } catch (InterruptedException e) {
                LOG.error(e.getStackTrace().toString());
            }
        }
        return false;
    }

    public DispatchAgent() {
        try {
            zk = new ZooKeeper(Constant.HOSTPORT, 10000, this);
        } catch (IOException e) {
            LOG.error(e.getStackTrace().toString());
        }
    }

    public List<String> getChanged(List<String> orgin, List<String> current) {
        List<String> changedList = new ArrayList<>();
        for (String c : current) {
            if (!orgin.contains(c)) {
                changedList.add(c);
            }
        }
        return changedList;
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.printf("Event Received at grandfather watcher: %s\n", event.toString());
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected && event.getType() == Watcher.Event.EventType.None) {
            lock.countDown();
        } else if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
            try {
                List<String> current = zk.getChildren(Constant.JOB, this);
                List<String> increased = getChanged(parentList, current);
                List<String> deleted = getChanged(current, parentList);
                parentList = current;
                for (String parent : increased) {
                    System.out.println("increased parent:" + parent);
                    zk.getChildren(Constant.JOB + "/" + parent, new ParentWacher());
                }
                for (String parent : deleted) {
                    System.out.println("deleted parent:" + parent);
                }
            } catch (KeeperException e) {
                LOG.error(e.getStackTrace().toString());
            } catch (InterruptedException e) {
                LOG.error(e.getStackTrace().toString());
            }
        }
    }

    public class ParentWacher implements Watcher {
        private List<String> childList = new ArrayList<>();

        @Override
        public void process(WatchedEvent event) {
            System.out.printf("Event Received at parent watcher: %s\n", event.toString());
            try {
                if (null != zk.exists(event.getPath(), null)) {
                    List<String> current = zk.getChildren(event.getPath(), this);
                    List<String> increased = getChanged(childList, current);
                    List<String> deleted = getChanged(current, childList);
                    childList = current;
                    for (String child : increased) {
                        System.out.println("increased children:" + child);
                    }
                    for (String child : deleted) {
                        System.out.println("deleted children:" + child);
                    }
                }
            } catch (KeeperException e) {
                LOG.error(e.getStackTrace().toString());
            } catch (InterruptedException e) {
                LOG.error(e.getStackTrace().toString());
            }
        }
    }

    public class ChildWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {

        }
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (true) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            LOG.error(e.getStackTrace().toString());
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args)
            throws InterruptedException, KeeperException {
        DispatchAgent dispatchAgent = new DispatchAgent();
        new Thread(dispatchAgent).start();
        Thread.sleep(3000);
    }

}
