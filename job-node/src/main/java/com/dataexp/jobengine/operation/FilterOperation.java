package com.dataexp.jobengine.operation;

import com.dataexp.common.metadata.FieldType;
import com.dataexp.common.metadata.InnerMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 过滤操作
 *
 * @author: Bing.Li
 * @create: 2019-01-23 14:19
 */
public class FilterOperation extends BaseOperation {

     private static final Logger LOG = LoggerFactory.getLogger(FilterOperation.class);
    /**
     * 过滤组件的过滤表达式
     * key: 字段序号
     * value：该字段过滤条件
     */
    private Map<Integer, String> filterPattern;

    public FilterOperation() {
    }

    public FilterOperation(int nodeId, InputConfig inputConfig, List<OutputConfig> outputConfigList) {
        super(nodeId, inputConfig, outputConfigList);
    }

    public Map<Integer, String> getFilterPattern() {
        return filterPattern;
    }

    public void setFilterPattern(Map<Integer, String> filterPattern) {
        this.filterPattern = filterPattern;
    }

    @Override
    public void processMsg(InnerMsg input) {
        LOG.debug("get msg:{}"+input.getMsgContent());
        if (filter(input)) {
            List<BaseOperation> opList = getFirstPortOperationList();
            if (null != opList) {
                for (BaseOperation op : opList) {
                    op.processMsg(input);
                }
            }
        }
    }

    private boolean filter(InnerMsg msg) {

        return "".equals(msg.getMsgContent()) || null == msg.getMsgContent() ? false : msg.getMsgContent().length() > 8;
    }
}
