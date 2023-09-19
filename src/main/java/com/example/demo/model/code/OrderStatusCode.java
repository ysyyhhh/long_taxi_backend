package com.example.demo.model.code;

public enum OrderStatusCode {

    /* 成功状态码 */
    WAIT_TO_ASSIGN(0),

    WAIT_TO_START(1),

    /*等待上车*/
    WAIT_TO_UP(2),

    WAIT_TO_END(3),

    /*等待下车*/
    WAIT_TO_DOWN(4),

    COMPLETED(5),

    /* 取消订单 */
    CANCEL(6);

    private Integer status;

    private OrderStatusCode(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public OrderStatusCode getNewStatus() {
        if(this.status < 5) {
            return OrderStatusCode.values()[this.status + 1];
        }
        return null;
    }
}
