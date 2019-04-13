package com.github.tensorflow;

import com.sun.corba.se.impl.orbutil.graph.Graph;

/**
 * @author alex.chen
 * @Description:
 * @date 2018/8/13
 */
public class Graph_unit {
    @Test
    public void defineGraph() {
        /**
         * 定义一个 graph 类，并在这张图上定义了 foo 与 bar 的两个变量
         */
        try (Graph g = new Graph()) {
            try (Tensor<Integer> t = Tensor.create(30, Integer.class)) {
                g.opBuilder("Const", "foo").setAttr("dtype", t.dataType()).setAttr("value", t).build();
            }
            try (Tensor<Integer> t = Tensor.create(20, Integer.class)) {
                g.opBuilder("Const", "bar").setAttr("dtype", t.dataType()).setAttr("value", t).build();
            }

            try (Session s = new Session(g);
                 Tensor output1 = s.runner().fetch("foo").run().get(0);
                 Tensor output2 = s.runner().fetch("bar").run().get(0)) {

                System.out.println(output1.intValue());
                System.out.println(output2.intValue());
            }
        }
    }
    @Test
    public void hello() throws Exception {
        try (Graph g = new Graph()) {
            final String value = "Hello from " + TensorFlow.version();
            // Construct the computation graph with a single operation, a constant
            // named "MyConst" with a value "value".
            try (Tensor t = Tensor.create(value.getBytes("UTF-8"))) {
                // The Java API doesn't yet include convenience functions for adding operations.
                g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
            }
            // Execute the "MyConst" operation in a Session.
            try (Session s = new Session(g);
                 Tensor output = s.runner().fetch("MyConst").run().get(0)) {
                System.out.println(new String(output.bytesValue(), "UTF-8"));
            }
        }
    }
}
