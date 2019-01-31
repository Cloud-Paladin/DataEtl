package com.dataexp.common.json.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class TestJson1 {

    //json字符串-简单对象型
    private static final String JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";

    //json字符串-数组类型
    private static final String JSON_ARRAY_STR = "[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]";

    //复杂格式json字符串
    private static final String COMPLEX_JSON_STR = "{\"teacherName\":\"crystall\",\"teacherAge\":27,\"course\":{\"courseName\":\"english\",\"code\":1270},\"students\":[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]}";

    public static void main(String[] args) throws UnsupportedEncodingException {

        JSONObject jsonObject = JSONObject.parseObject(JSON_OBJ_STR);
        Student student = JSONObject.parseObject(JSON_OBJ_STR, Student.class);
        System.out.println(student);

        List<Student> studentList1 = JSONArray.parseArray(JSON_ARRAY_STR, Student.class);
        System.out.println("studentList1:  " + studentList1);

        byte[] ba = "我是中国人".getBytes("UTF-8");
        String result = new String(ba, "UTF-8");
        System.out.println(result);

        String a = "hello";
        String b = a;
        System.out.println(b);
        a = "world";
        System.out.println(b);

    }

}

class Student {
    public String studentName;
    public String studentAge;

    public Student() {
    }

    @Override
    public String toString() {
        return studentName + ":" + studentAge;
    }
}


class SubDemo extends BaseDemo {

    public int a = 7;
    public static int b = 7;

    public SubDemo() {
        super();
    }

    public void accessOwner() {
        System.out.println(a);

    }

    public void accessBase() {
        System.out.println(super.a);

    }

    public static void main(String[] args) {
        SubDemo sc = new SubDemo();
        System.out.println(sc.a);
        sc.accessOwner();
        sc.accessBase();
        sc.gettt();
    }

    @Override
    public int reta() {
        return a;
    }

    @Override
    public void gettt() {
//        System.out.println(reta());
        System.out.println(a);
    }
}

abstract class BaseDemo {

    public int a = 5;
    public static int b = 5;

    public abstract int reta();

    public void gettt() {
//        System.out.println(reta());
        System.out.println(a);
    }
}


class ParentClass {
    public static String staticField = "父类静态变量";

    public final String finalField = "父类常量";

    public static final String staticFinalField = "父类静态常量";
}

class SubClass extends ParentClass {
    public static String staticField = "子类静态变量";

    public final String finalField = "子类常量";

    public static final String staticFinalField = "子类静态常量";

    public static void main(String[] args) {
        SubClass subClass = new SubClass();
        System.out.println(SubClass.staticField);
        System.out.println(subClass.finalField);
        System.out.println(SubClass.staticFinalField);
    }
}