package com.happy.boot.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success = true;

    /**
     * 返回处理消息
     */
    private String message = "操作成功";

    /**
     * 返回数据对象
     */
    private T data;

    public Result(){}

    public static Result<Object> success(Object object){
        Result<Object> objectResult = new Result<>();
        objectResult.setSuccess(true);
        objectResult.setData(object);
        return objectResult;
    }

    public static Result<Object> success(String message){
        Result<Object> objectResult = new Result<>();
        objectResult.setSuccess(true);
        objectResult.setMessage(message);
        return objectResult;
    }

    public static Result<Object> success(){
        Result<Object> objectResult = new Result<>();
        objectResult.setSuccess(true);
        objectResult.setMessage("操作成功");
        return objectResult;
    }

    public static Result<Object> fail(String message){
        Result<Object> objectResult = new Result<>();
        objectResult.setSuccess(false);
        objectResult.setMessage(message);
        return objectResult;
    }

    public static Result<Object> fail(){
        Result<Object> objectResult = new Result<>();
        objectResult.setSuccess(false);
        objectResult.setMessage("操作失败");
        return objectResult;
    }

}
