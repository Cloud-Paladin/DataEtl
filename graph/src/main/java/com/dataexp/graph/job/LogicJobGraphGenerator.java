package com.dataexp.graph.job;

import com.dataexp.graph.logic.LogicGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将LoigcGraph 转化为JobGraph
 */
public class LogicJobGraphGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(LogicJobGraphGenerator.class);

    public static JobGraph createJobGraph(LogicGraph logicGraph) {
        if (!logicGraph.cleanGraph()) {
            return null;
        }
        return null;
    }
}
