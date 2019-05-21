package com.supply.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-16
 */
public class DoOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 关联用户表唯一标识
     */
    private String openId;

    /**
     * 关联商品名称
     */
    @NotNull(message = "关联商品不能为空")
    private Integer supplyNo;

    private String supplyName;
    /**
     * 订单状态，默认：未付款，确认：已付款
     */
    private String status;

    private Integer type;

    /**
     * 商品总价
     */
    private BigDecimal price;

    private Integer userId;
    private Integer addressId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    /**
     * 创建时间
     */
    private Integer createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getSupplyNo() {
        return supplyNo;
    }

    public void setSupplyNo(Integer supplyNo) {
        this.supplyNo = supplyNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Order{" +
        "id=" + id +
        ", openId=" + openId +
        ", supplyNo=" + supplyNo +
        ", status=" + status +
        ", price=" + price +
        ", createTime=" + createTime +
        "}";
    }
}
