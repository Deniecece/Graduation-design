package com.supply.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.core.MallPage;
import com.supply.core.MallResponse;
import com.supply.domain.DoOrder;
import com.supply.entity.TOrder;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-16
 */
public interface IOrderService extends IService<TOrder> {

    MallResponse getDetails(Integer id);

    MallResponse selectByOpenId(String openId, MallPage MallPage);

    MallResponse updateOrder(DoOrder doOrder);

    MallResponse addOrder(DoOrder doOrder);

    MallResponse delOrder(Integer id);

}
