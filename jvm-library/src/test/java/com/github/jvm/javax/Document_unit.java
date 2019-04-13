package com.github.jvm.javax;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author alex.chen
 * @Description Document
 * * 优点：由于整棵树在内存中，因此可以对xml文档进行随机访问；可以对xml文档进行修改操作，API使用起来比较简单，
 * * 它还可以在任何时候在树中上下导航，而不是像 SAX 那样是一次性的处理，
 * * 缺点：整个文档都需要载入内存，对于大文档成本高，浪费时间和空间
 * * <p>
 * * 使用场合：一旦解析了文档还需多次访问这些数据；硬件资源充足（内存、CPU）
 * @date 2016/1/20
 * @See Sax_unit
 */
public class Document_unit {
    @Test
    public void readDocument() {
        String path = "Teachers.xml";
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(path));
            NodeList teacher = document.getElementsByTagName("teacher");
            for (int i = 0; i < teacher.getLength(); i++) {
                Node node = teacher.item(i);
                String _id = node.getAttributes().getNamedItem("id").getNodeValue();
                System.out.print("ID:" + _id);
                NodeList teacherInfo = node.getChildNodes();
                for (int j = 0; j < teacherInfo.getLength(); j++) {
                    Node node1 = teacherInfo.item(j);
                    String nodeName = node1.getNodeName();
                    if (nodeName.equals("name")) {
                        System.out.print(",name:" + node1.getFirstChild().getNodeValue());
                    }
                    if (nodeName.equals("sex")) {
                        System.out.print(",sex" + node1.getFirstChild().getNodeValue());
                    }
                    if (nodeName.equals("age")) {
                        System.out.print(",age" + node1.getFirstChild().getNodeValue());
                    }
                }
                System.out.println("");
            }
        } catch (ParserConfigurationException psex) {
            psex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
