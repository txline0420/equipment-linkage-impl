package com.txl.linkage.core;


import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class ResultBean<T> implements Serializable{

	private static final long serialVersionUID = -204438634738394135L;
	private int code;
    private String message;
    private T data;

    public ResultBean() {

    }

    public static <T> ResultBean<T> error(int code, String message) {
        ResultBean<T> resultBean = new ResultBean<>();
        resultBean.setCode(code);
        resultBean.setMessage(message);
        return resultBean;
    }
    
    public static <T> ResultBean<T> error(String message){
    	ResultBean<T> resultBean = new ResultBean<>();
        resultBean.setCode(300);
        resultBean.setMessage(message);
        return resultBean;
    }

    public static <T> ResultBean<T> success() {
    	ResultBean<T> resultBean = new ResultBean<>();
        resultBean.setCode(200);
        resultBean.setMessage("success");
        return resultBean;
    }

    public static <T> ResultBean<T> success(String message) {
    	ResultBean<T> resultBean = new ResultBean<>();
        resultBean.setCode(200);
        resultBean.setMessage(message);
        return resultBean;
    }
    
    public static<T> ResultBean<T> success(T datas) {
        ResultBean<T> resultBean = new ResultBean<>();
        resultBean.setCode(200);
        resultBean.setMessage("success");
        resultBean.setData(datas);
        return resultBean;
    }
    
    public static<T> ResultBean<T> success(String message,T Data){
        ResultBean<T> resultBean = new ResultBean<>();
        resultBean.setCode(200);
        resultBean.setMessage(message);
        resultBean.setData(Data);
        return resultBean;
    }
    
    public static<T> ResultBean<T> success(int code,String message,T Data){
        ResultBean<T> resultBean = new ResultBean<>();
        resultBean.setCode(code);
        resultBean.setMessage(message);
        resultBean.setData(Data);
        return resultBean;
    }

    public String toJSON(){
        return JSON.toJSONString(this);
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    
   /* public static<T> Map<T, T> success(Integer code) {
    	 Map<String, Object> result = new HashMap<>();
    	 if ( code == 200 ) {
    		  result.put("Result", true);
    		  result.put("Data", "成功接受了1条记录");
    		  Map<String, Object> r2 = new HashMap<>();
    		  r2.put("Code", 200);
    		  r2.put("Message", "成功");
    		  result.put("Error", r2);
		 }else {
			 result.put("Result", false);
			 result.put("Data", "");
			 Map<String, Object> r2 = new HashMap<>();
			 r2.put("Code", 300);
			 r2.put("Message", "失败");
			 result.put("Error", r2);
		}
        return (Map<T, T>) result;
    }*/
}