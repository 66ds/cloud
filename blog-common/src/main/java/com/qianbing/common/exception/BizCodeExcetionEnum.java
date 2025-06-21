package com.qianbing.common.exception;

public enum BizCodeExcetionEnum {
    CODE_ERROR(10006,"验证码错误"),

    CODE_NOT_NULL(10005,"验证码不能为空"),

    SMS_SEND_MORE_EXCEPTION(10003,"操作频繁,请稍后再试"),

    USER_NAME_EXIST_EXCEPTION(10004,"用户名已存在"),

    SMS_SEND_EXCEPTION(10007,"发送验证码异常请联系管理员"),

    LOGIN_TIME_ERROR(10008,"请先登录"),

    FAILED(500, "操作失败"),

    PRAM_NOT_MATCH(400, "参数不正确"),

    VALIDATE_FAILED(400, "参数检验失败"),

    UNAUTHORIZED(401, "未登录或token过期，请登录！"),

    FORBIDDEN(403, "没有相关权限"),

    NOT_FOUND(404, "没有找到相关数据"),

    ERROR(500, "服务器内部错误");

    private int code;

    private String msg;

    BizCodeExcetionEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }
}
