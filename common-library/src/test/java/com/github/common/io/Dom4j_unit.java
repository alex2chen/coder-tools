package com.github.common.io;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Set;


/**
 * @author alex.chen
 * @Description DOM4J
 * * DOM4J 是一个非常非常优秀的Java XML API，具有性能优异、功能强大和极端易用使用的特点
 * * 特别值得一提的是连 Sun 的 JAXM 也在用 DOM4J
 * @date 2016/1/20 20:56
 */
public class Dom4j_unit {

    @Test
    public void readDom() {
        readerDom("student.xml");
    }

    @Test
    public void readerValidator() {
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(Dom4j_unit.class.getResource("/validator-config.xml"));
            document.getRootElement().elements().forEach(x -> {
                Element annotationEle = (Element) x;
                annotationEle.elements().forEach(y -> {
                    Element fieldEle = (Element) y;
                    System.out.println(fieldEle.getName() + ":" + fieldEle.getTextTrim());
                    if (fieldEle.getName().equals("chkFeilds"))
                        System.out.println(getSonElements(fieldEle, "type").size());
                    if (fieldEle.getName().equals("chkScopes"))
                        System.out.println(getSonElements(fieldEle, "scope").size());
                });
            });
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成dom
     */
    @Test
    public void genDom() {
        try {
            Element root = DocumentHelper.createElement("students");
            Element studentElement = null;
            Document document = DocumentHelper.createDocument(root);
            for (int i = 0; i < 3; i++) {
                studentElement = root.addElement("student");
                studentElement.addAttribute("id", "1" + i);
                studentElement.addElement("name").addAttribute("checked", "" + (i % 2 == 0)).setText("name" + i);
                studentElement.addElement("age").setText("3" + i);
            }
            //输出到控制台
            XMLWriter xmlWriter = new XMLWriter();
            xmlWriter.write(document);
            // 输出到文件
            OutputFormat format = new OutputFormat("  ", true);// 设置缩进为2个空格，并且另起一行为true
            XMLWriter xmlWriter2 = new XMLWriter(new FileOutputStream("student.xml"), format);
            xmlWriter2.write(document);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Set getSonElements(Element element, String son) {
        Set result = Sets.newConcurrentHashSet();
        if (element != null || !Strings.isNullOrEmpty(son)) {
            Iterator iterator = element.elementIterator(son);
            Element sonEle = null;
            while (iterator.hasNext()) {
                sonEle = ((Element) iterator.next());
                if (sonEle.getName().equals(son)) {
                    result.add(sonEle.getTextTrim());
                }
            }
        }
        return result;
    }

    private static void readerDom(String path) {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new File(path));
//            Iterator iterator=document.getRootElement().elements("/students/student/*").iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }
            document.getRootElement().elements().forEach(x -> {
                Element studentElement = (Element) x;
                System.out.print("id:" + studentElement.attributeValue("id"));
                Element nameElement = studentElement.element("name");
                System.out.print(",name:" + nameElement.getText());
                System.out.print(",checked:" + nameElement.attributeValue("checked"));
                System.out.print(",age:" + studentElement.element("age").getText());
                System.out.println();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
