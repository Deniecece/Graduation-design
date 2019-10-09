package com.supply.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supply.entity.TOrder;
import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-16
 */
public interface OrderMapper extends BaseMapper<TOrder> {

    @Insert("INSERT INTO `t_order` (open_id, supply_no, `status`, price, create_time) VALUES (#{openId}, #{supplyNo}, #{status}, #{price}, #{createTime})")
    int inserta(TOrder order);
}
