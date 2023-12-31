package com.example.demo.model.result;

public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(1, "成功"),


    /* 参数错误 */
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 2001-2999*/
    USER_NOTLOGGED_IN(2001, "用户未登录"),
    USER_LOGIN_ERROR(2002, "账号不存在或密码错误"),
    USER_HAS_EXISTED(2003, "用户已存在"),
    USER_NOT_EXIST(2004, "用户不存在"),
    SYSTEM_ERROR(10000, "系统异常，请稍后重试"),

    /* 业务错误 */
    ORDER_STATUS_ERROR(3001, "订单状态异常"),
    NO_ORDER(3002, "订单不存在"),

    NO_ROUTE(3003, "没有路径信息");

    private Integer code;
    private String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }
    public String getMessage() {
        return this.message;
    }
}
