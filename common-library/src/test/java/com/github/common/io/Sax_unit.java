package com.github.common.io;

import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alex.chen
 * @Description SAX，全称Simple API for XML
 * * 既是一种接口，也是一种软件包。它是一种XML解析的替代方法，
 * * 这种处理的优点非常类似于流媒体的优点。分析能够立即开始，而不是等待所有的数据被处理，
 * * 而且，由于应用程序只是在读取数据时检查数据，因此不需要将数据存储在内存中。这对于大型文档来说是个巨大的优点。
 * * 应用程序甚至不必解析整个文档；它可以在某个条件得到满足时停止解析，它逐行扫描文档，一边扫描一边解析。
 * * 一般来说，SAX 还比它的替代者 DOM 快许多，
 * * <p>
 * * 优点：不用事先调入整个文档，占用资源少；SAX解析器代码比DOM解析器代码小，适于Applet，下载。
 * * 缺点：不是持久的；事件过后，若没保存数据，那么数据就丢了；无状态性；从事件中只能得到文本，但不知该文本属于哪个元素；
 * * 只能顺序读取文档，使用的是事件处理机制，不能随机读取
 * * <p>
 * * 使用场合：Applet;只需XML文档的少量内容，很少回头访问；机器内存少；
 * @date 2016/1/20 19:51
 */
public class Sax_unit {

    @Test
    public void readXml() {
        try {
            String path = "Teachers.xml";
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DocumentReader reader = new DocumentReader();
            saxParser.parse(new File(path), reader);
            reader.teachers.forEach(System.out::println);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static class DocumentReader extends DefaultHandler {
        private List<Teacher> teachers;
        private Teacher temp;
        private String tag;

        @Override
        public void startDocument() throws SAXException {
            teachers = new ArrayList<Teacher>();
            System.out.println("解析开始");
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            System.out.println("解析完成");
        }


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("teacher")) {
                temp = new Teacher();
                temp.setId(attributes.getValue("id"));
            }
            tag = qName;
            System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            if (tag != null) {
                switch (tag) {
                    case "name":
                        temp.setName(new String(ch, start, length));
                        break;
                    case "age":
                        temp.setAge(new String(ch, start, length));
                        break;
                    case "sex":
                        temp.setSex(new String(ch, start, length));
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (qName.equals("teacher")) {
                teachers.add(temp);
            }
            tag = null;
        }
    }

    public static class Teacher {
        private String id;
        private String age;
        private String sex;
        private String name;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "Teacher{" +
                    "age='" + age + '\'' +
                    ", id='" + id + '\'' +
                    ", sex='" + sex + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
