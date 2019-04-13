package com.github.common.io;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.junit.Test;

import javax.validation.constraints.Min;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author alex.chen
 * @Description
 * @date 2016/11/27
 */
public class XStream_unit {
    @Test
    public void toxml() {
        List<User> users = new ArrayList<User>();
        users.add(new User("123", "爱边程", "23"));
        users.add(new User("456", "刘大拿", "24"));
        BaseBean base = new BaseBean();
        base.setUserList(users);
        //XStream xStream = new XStream(new StaxDriver());//stax解析xml
        XStream xs = new XStream();
        xs.alias("root", BaseBean.class);
        xs.alias("user", User.class);
        xs.aliasField("list", BaseBean.class, "userList");
//        xs.toXML(base, System.out);
        String xml = xs.toXML(base);
        System.out.println(xml);
    }

    @Test
    public void toObj() {
        XStream xs1 = new XStream(new DomDriver());//dom解析xml
        xs1.alias("root", BaseBean.class);
        xs1.alias("user", User.class);
        xs1.aliasField("list", BaseBean.class, "userList");
        BaseBean base = (BaseBean) xs1.fromXML(new File("BaseBean.xml"));
        List<User> users = base.getUserList();
        System.out.println(users);
    }

    @Test
    public void toxmlValidator() {
        ValidatorXmlDefineRoot root = new ValidatorXmlDefineRoot();
        ValidatorXmlDefine validator1 = new ValidatorXmlDefine() {{
            setName("最小值校验");
            setChkType(javax.validation.constraints.Min.class);
            setChkFeilds(new String[]{"java.lang.Integer", "java.lang.Long"});
            setChkText("@Min(value = 0)");
            setChkScopes(new String[]{"Save", "Update"});

        }};
        ValidatorXmlDefine validator2 = new ValidatorXmlDefine() {{
            setName("正则校验");
            setChkType(javax.validation.constraints.Pattern.class);
            setChkFeilds(new String[]{"java.lang.String"});
            setChkText("@Pattern(regexp = \"[^\\\\#\\\\$\\\\*]\")");
            setChkScopes(new String[]{"Save", "Update"});

        }};
        root.getValidationConfig().add(validator1);
        root.getValidationConfig().add(validator2);
        XStream xs = new XStream();
        xs.alias("validationConfig", ValidatorXmlDefineRoot.class);
        xs.aliasField("list", BaseBean.class, "annotations");
        xs.alias("annotation", ValidatorXmlDefine.class);
        xs.toXML(root, System.out);


    }

    @Test
    public void toValidatorObj() {
        String xml = "<validationConfig>\n" +
                "  <annotations>\n" +
                "    <annotation>\n" +
                "      <name>最小值校验</name>\n" +
                "      <chkType>javax.validation.constraints.Min</chkType>\n" +
                "      <chkFeilds>\n" +
                "        <string>java.lang.Integer</string>\n" +
                "        <string>java.lang.Long</string>\n" +
                "      </chkFeilds>\n" +
                "      <chkText>@Min(value = 0)</chkText>\n" +
                "      <chkScopes>\n" +
                "        <string>Save</string>\n" +
                "        <string>Update</string>\n" +
                "      </chkScopes>\n" +
                "    </annotation>\n" +
                "  </annotations>\n" +
                "</validationConfig>";
        XStream xs = new XStream(new DomDriver());
        xs.alias("validationConfig", ValidatorXmlDefineRoot.class);
        xs.aliasField("list", ValidatorXmlDefineRoot.class, "annotations");
        xs.alias("annotation", ValidatorXmlDefine.class);
        ValidatorXmlDefineRoot validatorXmlDefineRoot = (ValidatorXmlDefineRoot) xs.fromXML(xml);

        System.out.println(validatorXmlDefineRoot.annotations);
    }


    static class User {
        private String id;
        private String name;
        private String age;

        public User() {
        }

        public User(String id, String name, String age) {
            this.id = id;
            this.name = name;
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

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    static class BaseBean {
        private List<User> userList;

        public BaseBean() {
            userList = new ArrayList<User>();
        }

        public List<User> getUserList() {
            return userList;
        }

        public void setUserList(List<User> userList) {
            this.userList = userList;
        }

        public void addUser(User user) {
            userList.add(user);
        }
    }

    static class ValidatorXmlDefineRoot {
        private List<ValidatorXmlDefine> annotations;

        public ValidatorXmlDefineRoot() {
            annotations = new ArrayList<>();
        }

        public List<ValidatorXmlDefine> getValidationConfig() {
            return annotations;
        }

        public void setValidationConfig(List<ValidatorXmlDefine> validationConfig) {
            this.annotations = validationConfig;
        }

        @Override
        public String toString() {
            return "ValidatorXmlDefineRoot{" +
                    "annotations=" + annotations +
                    '}';
        }
    }

    static class ValidatorXmlDefine {
        private String name;
        private Class<? extends Annotation> chkType;
        //        private String chkType;
        //        private Class<?>[] chkFeilds;
        private String[] chkFeilds;
        private String chkText;
        private String[] chkScopes;

        public ValidatorXmlDefine() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<? extends Annotation> getChkType() {
            return chkType;
        }

        public void setChkType(Class<? extends Annotation> chkType) {
            this.chkType = chkType;
        }

        public String[] getChkFeilds() {
            return chkFeilds;
        }

        public void setChkFeilds(String[] chkFeilds) {
            this.chkFeilds = chkFeilds;
        }

        public String getChkText() {
            return chkText;
        }

        public void setChkText(String chkText) {
            this.chkText = chkText;
        }

        public String[] getChkScopes() {
            return chkScopes;
        }

        public void setChkScopes(String[] chkScopes) {
            this.chkScopes = chkScopes;
        }

        @Override
        public String toString() {
            return "ValidatorXmlDefine{" +
                    "name='" + name + '\'' +
                    ", chkType=" + chkType +
                    ", chkFeilds=" + Arrays.toString(chkFeilds) +
                    ", chkText='" + chkText + '\'' +
                    ", chkScopes=" + Arrays.toString(chkScopes) +
                    '}';
        }
    }
}
