package com.example.managebackend.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@SuppressWarnings("unused")
public class ResponseVO<T> {

    private final Integer status;

    private final String message;

    private final T data;

    private ResponseVO(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }



    /**
     * 构建返回对象
     *
     * @param status  状态码
     * @param message 消息
     * @param data    数据
     */
    public static <T> ResponseVO<T> build(Integer status, String message, T data) {
        return new ResponseVO<>(status, message, data);
    }

    /**
     * 构建成功返回对象
     */
    public static <T> ResponseVO<T> success() {
        return new ResponseVO<>(200,"Success",null);
    }






    /**
     * 构建失败返回对象
     */
    public static <T> ResponseVO<T> error() {
        return new ResponseVO<>(400,"Error" ,null);
    }

    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>(200,"Success", data);
    }




}
