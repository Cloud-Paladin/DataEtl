package com.dataexp.graph.logic.component;

import com.dataexp.graph.logic.BaseLogicNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 逻辑组件生产工厂
 *
 * @author: Bing.Li`
 * @create: 2019-01-30
 */
public class ComponentFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentFactory.class);

    /**
     * 根据组件类型名称和构造参数生成组件类
     *
     * @param typeName 组件类型名称
     * @param id       组件id
     * @param name     组件名称
     * @param x
     * @param y
     * @return
     */
    public static BaseLogicNode createNode(String typeName, int id, String name, int x, int y) {
        ComponentType ct = ComponentType.fromComponentType(typeName);
        if (null == ct) {
            return null;
        }
        Class componentClass = ct.getComponentClass();
        Constructor<BaseLogicNode> constructor;
        BaseLogicNode node;
        try {
            if (null == name || "".equals(name)) {
                constructor = componentClass.getDeclaredConstructor(int.class, int.class, int.class);
                constructor.setAccessible(true);
                node = constructor.newInstance(id, x, y);
            } else {
                constructor = componentClass.getDeclaredConstructor(int.class, String.class, int.class, int.class);
                constructor.setAccessible(true);
                node = constructor.newInstance(id, name, x, y);
            }
            return node;
        } catch (Exception e) {
            LOG.error("error occured when createNode:", e);
        }
        return null;
    }


    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseLogicNode node = createNode("FileSink", 1, "", 10, 20);
        BaseLogicNode node1 = createNode("FileSource", 1, "", 10, 20);
        BaseLogicNode node2 = createNode("FilterNode", 1, "", 10, 20);
        BaseLogicNode node3 = createNode("FormatterNode", 1, "", 10, 20);
        BaseLogicNode node4 = createNode("SplitNode", 1, "", 10, 20);
        BaseLogicNode node5 = createNode("UnionNode", 1, "", 10, 20);
        BaseLogicNode node6 = createNode("WashNode", 1, "", 10, 20);
        System.out.println(node);
    }
}
