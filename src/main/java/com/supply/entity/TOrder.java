package com.supply.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-20
 */
public class TOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联用户表唯一标识
     */
    private String openId;

    /**
     * 关联商品名称
     */
    private Integer supplyNo;

    /**
     * 订单状态，默认：未付款，确认：已付款
     */
    private String status;

    /**
     * 商品总价
     */
    private BigDecimal price;

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

    @Override
    public String toString() {
        return "TOrder{" +
        "id=" + id +
        ", openId=" + openId +
        ", supplyNo=" + supplyNo +
        ", status=" + status +
        ", price=" + price +
        ", createTime=" + createTime +
        "}";
    }
}