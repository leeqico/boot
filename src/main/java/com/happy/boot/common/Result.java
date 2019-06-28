package com.happy.boot.common;

import cn.hutool.http.HttpStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回实体
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success = true;

    private int code = HttpStatus.HTTP_OK;

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
        Result<Object> result = new Result<>();
        result.setSuccess(true);
        result.setCode(HttpStatus.HTTP_OK);
        result.setData(object);
        return result;
    }

    public static Result<Object> success(String message){
        Result<Object> result = new Result<>();
        result.setSuccess(true);
        result.setCode(HttpStatus.HTTP_OK);
        result.setMessage(message);
        return result;
    }

    public static Result<Object> success(){
        Result<Object> result = new Result<>();
        result.setSuccess(true);
        result.setCode(HttpStatus.HTTP_OK);
        result.setMessage("操作成功");
        return result;
    }

    public static Result<Object> fail(String message){
        Result<Object> result = new Result<>();
        result.setSuccess(false);
        result.setCode(HttpStatus.HTTP_INTERNAL_ERROR);
        result.setMessage(message);
        return result;
    }

    public static Result<Object> fail(){
        Result<Object> result = new Result<>();
        result.setSuccess(false);
        result.setCode(HttpStatus.HTTP_INTERNAL_ERROR);
        result.setMessage("操作失败");
        return result;
    }

}
