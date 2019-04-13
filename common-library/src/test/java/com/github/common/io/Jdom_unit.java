package com.github.common.io;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author alex.chen
 * @Description dom4j是一个简单的开源库，用于处理XML、 XPath和XSLT
 * * 优点：20-80原则，极大减少了代码量。
 * * 使用场合：要实现的功能简单，如解析、创建等，但在底层，JDOM还是使用SAX（最常用）、DOM、Xanan文档。
 * @date 2016/7/25
 */
public class Jdom_unit {
    @Test
    public void genXml() throws IOException {
        Document document = new Document();
        //根元素
        Element root = new Element("root");
        document.addContent(root);
        //给节点添加注释
        Comment comment = new Comment("this is my comments");
        document.addContent(comment);

        //添加子元素
        Element nextNode = new Element("hello");
        nextNode.setAttribute("baidu", "www.baidu.com");
        root.addContent(nextNode);
        Element nextNode2 = new Element("world");
        Attribute attribute = new Attribute("testAttr", "attr Value");
        nextNode2.setAttribute(attribute);// set方法会返回元素本身（方法链method chain style）
        root.addContent(nextNode2);
        //格式化
        Format format = Format.getPrettyFormat();
        format.setIndent("    ");// 把缩进设为四个空格（默认为两个空格）
        XMLOutputter outputter = new XMLOutputter(format);
//        outputter.output(document,new FileOutputStream("jdom.xml"));
        outputter.output(document, System.out);
    }

    @Test
    public void readXml() throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new File("jdom.xml"));
        Element root = document.getRootElement();
        System.out.println("root:" + root.getName());

        Element hello = root.getChild("hello");
        System.out.println("hello:" + hello.getName());

        List<Attribute> list = hello.getAttributes();
        for (Attribute attribute : list) {
            System.out.println(String.format("%s:%s", attribute.getName(), attribute.getValue()));
        }
    }
}
