package com.dataexp.common.zookeeper.example.part2;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.UUID;

/**
 * @author: Bing.Li
 * @create: 2019-01-28 19:11
 */
public class TestUpdate implements Watcher {
    private static String hostPort = "localhost:2181/datadistclean";
    private static String zooDataPath = "/MyConfig";
    ZooKeeper zk;

    // updates the znode path /MyConfig every 5 seconds with a new UUID string.
    public TestUpdate() throws IOException {
        try {
            zk = new ZooKeeper(hostPort, 10000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws InterruptedException, KeeperException {
        String uuid = UUID.randomUUID().toString();
        byte zoo_data[] = uuid.getBytes();
        zk.setData(zooDataPath, zoo_data, -1);
        for (int i = 1; i <= 5; i++) {
            zk.create(zooDataPath + "/parent" + i, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            for (int j = 1; j <= 5; j++) {
                zk.create(zooDataPath + "/parent" + i + "/son" + j, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.printf("\nEvent Received: %s", event.toString());
    }

    public static void main(String[] args) throws
            IOException, InterruptedException, KeeperException {
        TestUpdate dataUpdater = new TestUpdate();
        dataUpdater.run();
    }
}
