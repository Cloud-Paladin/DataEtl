package com.dataexp.common.zookeeper.example.part2;

import java.io.IOException;
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
    private static String hostPort = "localhost:2181";
    //    private static String hostPort = "localhost:2181/WatcherTest";
    private static String zooDataPath = "/MyConfig";
    private CountDownLatch startLock = new CountDownLatch(1);
    private Stat stat = new Stat();
    byte zoo_data[] = null;
    private AtomicInteger count = new AtomicInteger(0);
    ZooKeeper zk;

    public TestWatch() {
        try {
            zk = new ZooKeeper(hostPort, 2000, this);
            startLock.await();
            if (zk != null) {
                try {
                    //Create the znode if it doesn't exist, with the following code:
                    if (zk.exists(zooDataPath, null) == null) {
                        zk.create(zooDataPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }

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

    public void printData() throws InterruptedException, KeeperException {
        List<String> list = zk.getChildren(zooDataPath, true, null);
        for (String child : list) {
            System.out.println("child:" + child);
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.printf("Event Received: %s\n", event.toString());
        System.out.println(count.incrementAndGet());
        //We will process only events of type NodeDataChanged
        if (event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.None) {
            startLock.countDown();
            return;
        }
        try {
            printData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
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
        testWatcher.zk.getChildren(zooDataPath,testWatcher);
        testWatcher.zk.create(zooDataPath + "/child1", "first".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        testWatcher.zk.create(zooDataPath + "/child11", "first".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        testWatcher.zk.create(zooDataPath + "/child12", "first".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

//        for (int i = 0; i < 100; i++) {
//            testWatcher.zk.create(zooDataPath + "/child"+i, "first".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        }
        Thread.sleep(3000);
    }
}