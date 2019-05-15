package com.github.art;

import com.google.common.hash.Hashing;
import org.junit.Test;

import java.util.*;

/**
 * Created by fei.chen on 2018/7/5.
 */
public class ConsistentHash_test {
    @Test
    public void go_treeMap() {
        TreeMap<Integer, String> treemap = new TreeMap<>();
        SortedMap<Integer, String> treemapincl;
        treemap.put(2, "two");
        treemap.put(1, "one");
        treemap.put(13, "three");
        treemap.put(6, "six");
        treemap.put(5, "five");
        System.out.println("treemap: " + treemap);
        treemapincl = treemap.tailMap(3);
        System.out.println("Tail map values: " + treemapincl);

        treemapincl = treemap.headMap(3);
        System.out.println("Head map values: " + treemapincl);

        treemapincl = treemap.tailMap(10);
        System.out.println("Tail map values: " + treemapincl);

        treemapincl = treemap.tailMap(20);
        System.out.println("Tail map values: " + treemapincl);

        System.out.println("First key is: " + treemap.firstKey());


    }

    @Test
    public void go_ConsistentHash() {
        ConsistentHash<Node> consistentHash = new ConsistentHash<Node>(2);
        consistentHash.add(new Node("tms-1", "192.168.1.12"));
        consistentHash.add(new Node("tms-2", "192.168.1.123"));
        consistentHash.add(new Node("tms-3", "192.168.1.1234"));
        consistentHash.display();
        System.out.println(consistentHash.get(Objects.hashCode(null)));
        System.out.println(consistentHash.get("2"));
        System.out.println(consistentHash.get("423423423"));

    }

    public static class ConsistentHash<T> {
        private HashFunction hashFunction = new HashFunction();
        private final int numberOfReplicas;//虚拟节点
        private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

        public ConsistentHash(int numberOfReplicas) {
            this.numberOfReplicas = numberOfReplicas;
        }

        public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) {
            this.hashFunction = hashFunction;
            this.numberOfReplicas = numberOfReplicas;

            for (T node : nodes) {
                add(node);
            }
        }

        public void add(T node) {
            for (int i = 0; i < numberOfReplicas; i++) {
                int hash = hashFunction.hash(node.toString() + i);
                circle.put(hash, node);
            }
        }

        public void remove(T node) {
            for (int i = 0; i < numberOfReplicas; i++) {
                circle.remove(hashFunction.hash(node.toString() + i));
            }
        }

        public T get(Object key) {
            if (circle.isEmpty()) {
                return null;
            }
            int hash = hashFunction.hash(key.toString());
            System.out.print("key:" + key + "," + hash + ">>>");
            if (!circle.containsKey(hash)) {
                SortedMap<Integer, T> tailMap = circle.tailMap(hash);
                hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
                System.out.println(hash);
            }
            return circle.get(hash);
        }

        public void display() {
            circle.entrySet().stream().forEach(x -> System.out.println(x.getKey() + ":::" + x.getValue()));
        }

    }

    public static class HashFunction {
        public Integer hash(String input) {
            String key = Hashing.md5().hashBytes(input.getBytes()).toString();
            return key.hashCode();
        }
    }

    public static class Node {
        private String name;
        private String ip;

        public Node(String name, String ip) {
            this.name = name;
            this.ip = ip;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (name != null ? !name.equals(node.name) : node.name != null) return false;
            return ip != null ? ip.equals(node.ip) : node.ip == null;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    ", ip='" + ip + '\'' +
                    '}';
        }
    }
}
