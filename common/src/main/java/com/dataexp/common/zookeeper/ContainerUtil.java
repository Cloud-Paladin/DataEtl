package com.dataexp.common.zookeeper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Bing.Li
 * @create: 2019-01-29
 */
public class ContainerUtil {

    /**
     * 比较两个输入的Map，找到后一个对前一个的差异
     * @param container1 第一个容器
     * @param container2 第二个容器
     * @param <T> 条目类型
     * @return 新增差异条目的map
     */
    public static <T> Map<String, T> getDiffer(Map<String, T> container1, Map<String, T> container2) {
        Map<String, T> result = new HashMap<>();
        for (String key : container2.keySet()) {
            if (!container1.containsKey(key)) {
                result.put(key, container2.get(key));
            }
        }
        return result;
    }
}
