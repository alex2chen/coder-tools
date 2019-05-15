package com.github.art.graph;

import org.junit.Test;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 图是一种和树相像的数据结构，通常有一个固定的形状，这是由物理或抽象的问题来决定的。
 * 路径是从一个顶点到另一个顶点经过的边的序列。
 * 有向图的边是有方向的，如果只能从 A 到 B，不能从 B 到 A。
 * 无向图的边是没有方向的，可以从 A 到 B，也可以从 B 到 A。
 * 图的搜索是指从一个指定的顶点到达哪些顶点。
 * 有两种常用的方法可以用来搜索图：深度优先搜索（DFS）和广度优先搜索（BFS）。
 * 深度优先搜索通过栈来实现，而广度优先搜索通过队列来实现。
 * <p>
 * Created by YT on 2018/3/28.
 */
public class MyGraph {

    @Test
    public void go_dfs() {
        MyGraphInner g = new MyGraphInner();
        g.addVertex('A');
        g.addVertex('B');
        g.addVertex('C');
        g.addVertex('D');
        g.addVertex('E');

        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(0, 3);
        g.addEdge(3, 4);
        g.display();
        System.out.println("深度优先搜索");
        g.dfs();

    }

    @Test
    public void go_bfs() {
        MyGraphInner g = new MyGraphInner();
        g.addVertex('A');
        g.addVertex('B');
        g.addVertex('C');
        g.addVertex('D');
        g.addVertex('E');
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 3);
        g.addEdge(0, 4);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 3);
        g.addEdge(2, 4);
        g.addEdge(3, 4);
        g.display();
        System.out.println("广度优先搜索");
        g.bfs();
    }

    public class MyGraphInner {
        //顶点数组
        private Vertex[] vertexList;
        //邻接矩阵
        private int[][] adjMat;
        //顶点的最大数目
        private int maxSize = 20;
        //当前顶点
        private int nVertex;

        private Stack stack;
        private Queue queue;

        public MyGraphInner() {
            vertexList = new Vertex[maxSize];
            adjMat = new int[maxSize][maxSize];
            for (int i = 0; i < maxSize; i++) {
                for (int j = 0; j < maxSize; j++) {
                    adjMat[i][j] = 0;
                }
            }
            nVertex = 0;
            stack = new Stack();
            queue = new ConcurrentLinkedQueue();
        }

        /**
         * 添加顶点
         */
        public void addVertex(char label) {
            vertexList[nVertex++] = new Vertex(label);
        }

        /**
         * 添加边
         */
        public void addEdge(int start, int end) {
            adjMat[start][end] = 1;
            adjMat[end][start] = 1;
        }

        public void dfs() {
            //首先访问0号顶点
            vertexList[0].wasVisited = true;
            //显示该顶点
            System.out.println(vertexList[0].label);
            //压入栈中
            stack.push(0);
            while (!stack.isEmpty()) {
                //获得一个未访问过的邻接点
                int v = getadjUnvisitedVertex((int) stack.peek());
                if (v == -1) {
                    //弹出一个顶点
                    Object remove = stack.pop();
                    System.out.println("弹出："+vertexList[(Integer)remove].label);
                } else {
                    vertexList[v].wasVisited = true;
                    System.out.println(vertexList[v].label);
                    stack.push(v);
                }
            }

            //搜索完以后，要将访问信息修改
            for (int i = 0; i < nVertex; i++) {
                vertexList[i].wasVisited = false;
            }

        }

        private int getadjUnvisitedVertex(int v) {
            for (int i = 0; i < nVertex; i++) {
                if (adjMat[v][i] == 1 && vertexList[i].wasVisited == false) {
                    return i;
                }
            }
            return -1;
        }

        public void bfs() {
            //首先访问0号顶点
            vertexList[0].wasVisited = true;
            queue.add(0);
            System.out.println(vertexList[0].label);
            while (!queue.isEmpty()) {
                //当前顶点
                int currentVertex = (int) queue.peek();
                //获得一个未访问过的邻接点
                int v = getadjUnvisitedVertex(currentVertex);
                if (v == -1) {
                    //弹出一个顶点
                    Object remove = queue.remove();
                    System.out.println("弹出："+vertexList[(Integer)remove].label);
                } else {
                    vertexList[v].wasVisited = true;
                    queue.add(v);
                    System.out.println(vertexList[v].label);
                }
            }

            //搜索完以后，要将访问信息修改
            for (int i = 0; i < nVertex; i++) {
                vertexList[i].wasVisited = false;
            }
        }

        public void display() {
            for (int i = 0; i < vertexList.length; i++) {
                Vertex vertex = vertexList[i];
                if (vertex == null) continue;
                System.out.print(vertex.label+">");
                for (int j=0;j<maxSize;j++){
                    if (adjMat[i][j]==1) System.out.print(vertexList[j].label+",");
                }
                System.out.println("");
            }
        }
    }

    //顶点类
    public class Vertex {
        public char label;
        public boolean wasVisited;

        public Vertex(char label) {
            this.label = label;
        }
    }
}
