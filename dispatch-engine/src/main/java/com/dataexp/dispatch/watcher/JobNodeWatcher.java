package com.dataexp.dispatch.watcher;

import com.alibaba.fastjson.JSONObject;
import com.dataexp.common.zookeeper.ContainerUtil;
import com.dataexp.dispatch.DispatchAgent;
import com.dataexp.common.zookeeper.entity.JobNode;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jobnode监视器，检视jobnode临时节点加入和删除事件
 * @author: Bing.Li
 * @create: 2019-01-29
 */
public class JobNodeWatcher implements Watcher {
     private static final Logger LOG = LoggerFactory.getLogger(JobNodeWatcher.class);

    @Override
    public synchronized void process(WatchedEvent event) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Enter JobNodeWatcher :" + event.toString());
        }
        if (event.getType() == Event.EventType.NodeChildrenChanged) {
            try {
                ZooKeeper zk = DispatchAgent.getZk();
                List<String> list = zk.getChildren(event.getPath(), this);
                Map<String, JobNode> currrent = new HashMap<>();
                for (String jobNodeId : list) {
                    String nodeContent = new String(zk.getData(event.getPath() + "/" + jobNodeId,null,null),"UTF-8");
                    if (!"".equals(nodeContent)) {
                        JobNode node = JSONObject.parseObject(nodeContent, JobNode.class);
                        currrent.put(jobNodeId, node);
                    }
                }
                Map<String, JobNode> origin = DispatchAgent.getJobNodeMap();
                Map<String, JobNode> increased = ContainerUtil.getDiffer(origin, currrent);
                Map<String, JobNode> decreased = ContainerUtil.getDiffer(currrent, origin);
                for (String jobNodeId : increased.keySet()) {
                    System.out.println("add  node :"+jobNodeId);
                    DispatchAgent.addJobNode(jobNodeId, increased.get(jobNodeId));
                }
                for (String jobNodeId : decreased.keySet()) {
                    System.out.println("remove node :"+jobNodeId);
                    DispatchAgent.removeJobNode(jobNodeId);
                }
                System.out.println("current nodesize"+DispatchAgent.getJobNodeMap().size());
            } catch (KeeperException e) {
                LOG.error("error occcured:", e);;
            } catch (InterruptedException e) {
                LOG.error("error occcured:", e);;
            } catch (UnsupportedEncodingException e) {
                LOG.error("error occcured:", e);;
            }
        }
    }
}
