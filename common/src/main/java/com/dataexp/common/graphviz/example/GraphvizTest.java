package com.dataexp.common.graphviz.example;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.RankDir;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.model.Factory.*;

/**
 * @author: Bing.Li
 * @create: 2019-02-02
 */
public class GraphvizTest {

    public static void draw() throws IOException {
        Graph g = graph("example1").directed()
                .graphAttr().with(RankDir.LEFT_TO_RIGHT)
                .with(
                        node("a").with(Color.RED).link(node("b")),
                        node("b").link(to(node("c")).with(Style.DASHED))
                );
        Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new File("example/ex1.png"));
    }




    public static void main(String[] args) throws IOException {
        draw();
    }
}
