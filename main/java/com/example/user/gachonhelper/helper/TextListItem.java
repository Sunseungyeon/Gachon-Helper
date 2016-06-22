package com.example.user.gachonhelper.helper;

/**
 * Created by wangki on 2016-06-20.
 */
public class TextListItem {
    private String title;
    private String name;
    private String created_at;
    private String see;


    public void setTitle(String title){
        this.title = title;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }
    public void setSee(String see){
        this.see = see;
    }
    public String getTitle(){
        return this.title;
    }
    public String getName(){
        return this.name;
    }
    public String getCreated_at(){
        return this.created_at;
    }
    public String getSee(){
        return this.see;
    }
}
