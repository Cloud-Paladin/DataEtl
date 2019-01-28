package com.dataexp.common.zookeeper.example.part2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class TestWatch implements Watcher, Runnable {
    private static final String hostPort = "localhost:2181";
    private static final String chroot = "/datadistclean";
    //    private static String hostPort = "localhost:2181/datadistclean";
    //    private static String hostPort = "localhost:2181/WatcherTest";
    private static String zooDataPath = "/MyConfig";
    private CountDownLatch startLock = new CountDownLatch(1);
    private Stat stat = new Stat();
    byte zoo_data[] = null;
    private AtomicInteger count = new AtomicInteger(0);
    List<String> parentList = new ArrayList<>();
    ZooKeeper zk;

    public TestWatch() {
        try {
            zk = new ZooKeeper(hostPort, 10000, this);
            startLock.await();
            if (zk != null) {
                try {
                    /**
                     * 如果chroot地址不存在，则初始化该节点
                     */
                    if (null == zk.exists(chroot, false)) {
                        zk.create(chroot, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                        zk.close();
                    }
                    //重新以chroot为根路径连接到Zookeeper
                    startLock = new CountDownLatch(1);
                    zk = new ZooKeeper(hostPort + chroot, 2000, this);
                    startLock.await();
                    //Create the znode if it doesn't exist, with the following code:
                    if (zk.exists(zooDataPath, null) == null) {
                        zk.create(zooDataPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                    zk.getChildren(zooDataPath, true);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        if (event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.None) {
            startLock.countDown();
        } else if (event.getState() == Event.KeeperState.SyncConnected) {
            try {
                List<String> current = zk.getChildren(zooDataPath, this);
                List<String> increased = getChanged(parentList, current);
                List<String> deleted = getChanged(current, parentList);
                parentList = current;
                for (String parent : increased) {
                    System.out.println("increased parent:" + parent);
                    zk.getChildren(zooDataPath + "/" + parent, new ParentWacher());
                }
                for (String parent: deleted) {
                    System.out.println("deleted parent:" + parent);
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class ParentWacher implements Watcher {
        private List<String> childList = new ArrayList<>();
        @Override
        public void process(WatchedEvent event) {
            System.out.printf("Event Received at parent watcher: %s\n", event.toString());
            try {
                if(null != zk.exists(event.getPath(),null)){
                    List<String> current = zk.getChildren(event.getPath(), this);
                    List<String> increased = getChanged(childList, current);
                    List<String> deleted = getChanged(current, childList);
                    childList = current;
                    for (String child : increased) {
                        System.out.println("increased children:" + child);
                    }
                    for (String child: deleted) {
                        System.out.println("deleted children:" + child);
                    }
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args)
            throws InterruptedException, KeeperException {
        TestWatch testWatcher = new TestWatch();
        new Thread(testWatcher).start();
        Thread.sleep(3000);
    }
}
