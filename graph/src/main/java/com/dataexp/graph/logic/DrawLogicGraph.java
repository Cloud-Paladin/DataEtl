package com.dataexp.graph.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * 逻辑图绘制工具
 *
 * @author: Bing.Li
 * @create: 2019-02-02
 */
public class DrawLogicGraph {

    public static void drawSelf(LogicGraph lg) {
        String newLine = "\n";
        StringBuilder dotText = new StringBuilder();
        dotText.append(String.format("digraph G{" + newLine));
        //设置字体支持中文
        dotText.append("edge [fontname=\"Microsoft Yahei\"];\n");
        dotText.append("node [fontname=\"Microsoft Yahei\"];\n");
        for (BaseLogicNode node : lg.getNodeMap().values()) {
            dotText.append(node.getName());
//            if(!node.color.equals("black"))dotText.append(String.format(" [style=filled, fillcolor=%s]",node.color));
            dotText.append(";" + newLine);
        }
        dotText.append(newLine);
        for (BaseLogicNode node : lg.getNodeMap().values()) {
            for (OutputPort outputPort : node.getOutputPortMap().values()) {
                for (InputPort ip : outputPort.getLinkedPortMap().values()) {
                    BaseLogicNode target = ip.getParentNode();
                    dotText.append(String.format("%s->%s[label=%d]", node.getName(), target.getName(), outputPort.getId()));
                    //                if(!edge.color.equals("black"))dotText.append(String.format("[style=bold, color=%s]",edge.color));
                    dotText.append(";" + newLine);
                }
            }
        }
        dotText.append("}" + newLine);

        //把生成好的脚本写到指定的缓存路径下
        String tmpPath = "example/";
        String graphFilePath = tmpPath + "graph.gv";
        try {
            File tmpf = new File(tmpPath);
            if (!tmpf.exists()) {
                tmpf.mkdirs();
            }
            FileWriter fw = new FileWriter(graphFilePath);
            BufferedWriter bufw = new BufferedWriter(fw);
            bufw.write(dotText.toString());
            bufw.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to open file");
        }

        Runtime rt = Runtime.getRuntime();
        try {
            String[] args = {"dot", "-Tpng", graphFilePath, "-o", tmpPath + "img.png"};
            Process process = rt.exec(args);
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate image.");
        }
    }
}
