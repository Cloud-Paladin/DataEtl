package com.dataexp.graph.logic.component;

import com.dataexp.common.metadata.InnerMsg;
import com.dataexp.graph.logic.BaseLogicNode;
import com.dataexp.graph.logic.component.config.WashTemplate;

import java.util.ArrayList;
import java.util.List;

public class WashNode extends BaseLogicNode {
    public WashNode(int id, int x, int y) {
        super(id, x, y);
    }

    public WashNode(int id, String name, int x, int y) {
        super(id, name, x, y);
    }

    @Override
    public String genNodeConfig() {
        return null;
    }

    @Override
    public void initNodeConfig(String config) {

    }

    private WashTemplate template = new WashTemplate();

    @Override
    public int defaultInputPortNumber() {
        return 1;
    }

    @Override
    public int defaultOutputPorNumber() {
        return 1;
    }

    @Override
    public int maxInputPortNumber() {
        return 1;
    }

    @Override
    public int maxOutputPortNumber() {
        //标准输出一个,格式异常、处理异常，校验异常各一个
        return 4;
    }

    @Override
    public String getDefaultName() {
        return "清洗";
    }

    @Override
    public List<String> getExceptions() {
        return null;
    }

    @Override
    public List<String> getWarnings() {
        return null;
    }

    @Override
    /**
     * 清洗节点的第一个输出端口是默认的数据出口，不可删除
     */
    public List<Integer> getForcedPortId() {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(getNormalOutputPort());
        return result;
    }

    /**
     * 获取清洗节点正常数据出口端口号
     * @return
     */
    public int getNormalOutputPort() {
        return getOutputPortMap().keySet().iterator().next();
    }



    public WashTemplate getTemplate() {
        return template;
    }

    public void setTemplate(WashTemplate template) {
        this.template = template;
    }

    /**
     * 过滤处理函数
     * @param input
     * @return
     */
    public InnerMsg processMsg(InnerMsg input) {
        input.setCurrentNodeId(getId());
        input.setMsgContent(washLine(input.getMsgContent()));
        return input;
    }

    /**
     * 处理单行数据
     * @param data
     * @return
     */
    public String washLine(String data) {
        //TODO: 清洗数据
        return data;
    }
}
