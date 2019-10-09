package com.supply.core;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Deniecece
 */
public class MallResponse implements Serializable {


    private static final long serialVersionUID = -7318467237446066728L;
    private int code;
    private String msg;
    /**
     * @JsonInclude(Include.NON_NULL) 这个注解放在类头上就可以解决。 实体类与json互转的时候 属性值为null的不参与序列化
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;


    public MallResponse() {
        this(MallStatus.SUCCESS, null);
    }

    public MallResponse(Object data) {
        this(MallStatus.SUCCESS, data);
    }

    public MallResponse(MallWebStatus MallWebStatus) {
        this(MallWebStatus, null);
    }

    public MallResponse(MallWebStatus MallWebStatus, Object data) {
        this.code = MallWebStatus.getCode();
        this.msg = MallWebStatus.getMsg();
        this.data = data;
    }

    public MallResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
