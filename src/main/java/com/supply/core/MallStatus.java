package com.supply.core;

/**
 * MallBaseStatus class description
 *
 * @author Deniecece
 * @date 2019-03-21
 */
public enum MallStatus implements MallWebStatus {
    /**
     *  成功
     */
    SUCCESS(0 , "操作成功！"),
    /**
     * 失败
     */
    FAILURE(1 , "操作失败！"),
    /**
     *  数据不存在
     */
    NO_DATA(4, "数据不存在"),
    /**
     *  数据已存在
     */
    DATA_EXIST(3, "数据已存在"),

    SALED(4, "该商品已出售"),
    /**
     *  验证码无效
     */
    INVALID_VERIFY_CODE(10003, "验证码无效"),
    /**
     * 认证授权失败
     */
    AUTHORIZATION_FAILED(10004, "认证授权失败"),
    /**
     * 每个设备绑定的营销号有数量限制，最多支持2个
     */
    MARKETING_ACCOUNT_LIMIT(10005, "每个设备绑定的营销号有数量限制，最多支持2个"),
    /**
     * 手机号已存在
     */
    MOBILE_EXIST(10006, "手机号已存在"),
    /**
     * 邮箱已存在
     */
    EMAIL_EXIST(10007, "邮箱已存在"),
    /**
     * 邮箱格式不正确
     */
    INVALID_EMAIL(10008, "邮箱格式不正确"),
    /**
     * 手机号格式不正确
     */
    INVALID_MOBILE(10009, "手机号格式不正确"),
    /**
     * 未知异常,请联系管理员!
     */
    UNKNOW_EXCEPTION(500 , "未知异常,请联系管理员!"),
    /**
     * 请求参数错误
     */
    BAD_REQUEST(400 , "请求参数错误"),
    /**
     * 用户名或密码错误
     */
    ACCOUNT_BAD_CREDENTIALS(401 , "用户名或密码错误!"),
    /**
     * 该帐号已被禁用，请联系系统管理员!
     */
    ACCOUNT_LOCK(402 , "该帐号已被禁用，请联系系统管理员!"),
    ;

    private int code;
    private String msg;

    MallStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
