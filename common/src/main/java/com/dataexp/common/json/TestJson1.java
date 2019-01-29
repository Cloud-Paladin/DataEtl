package com.dataexp.common.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class TestJson1 {

    //json字符串-简单对象型
    private static final String  JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";

    //json字符串-数组类型
    private static final String  JSON_ARRAY_STR = "[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]";

    //复杂格式json字符串
    private static final String  COMPLEX_JSON_STR = "{\"teacherName\":\"crystall\",\"teacherAge\":27,\"course\":{\"courseName\":\"english\",\"code\":1270},\"students\":[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]}";

     public static void main(String[] args) throws UnsupportedEncodingException {

        JSONObject jsonObject = JSONObject.parseObject(JSON_OBJ_STR);
        Student student = JSONObject.parseObject(JSON_OBJ_STR, Student.class) ;
        System.out.println(student);

        List<Student> studentList1 = JSONArray.parseArray(JSON_ARRAY_STR, Student.class);
        System.out.println("studentList1:  " + studentList1);

        byte[] ba = "我是中国人".getBytes("UTF-8");
        String result = new String(ba,"UTF-8");
         System.out.println(result);

         String a="hello";
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
        return studentName +":" + studentAge;
    }
}
