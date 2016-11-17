package com.example.bombtest;
import cn.bmob.v3.BmobObject;
/**
 * Created by 鸿达 on 2016/11/17.
 */
public class BombTestObject extends BmobObject {
    private String name;
    private String address;

//    public BombTestObject(){
//        this.setTableName("Person");
//    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
