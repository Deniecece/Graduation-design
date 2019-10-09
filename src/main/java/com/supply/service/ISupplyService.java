package com.supply.service;

import com.supply.core.MallPage;
import com.supply.core.MallResponse;
import com.supply.domain.DoSupply;
import com.supply.entity.Supply;
import com.supply.entity.Supply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-16
 */
public interface ISupplyService extends IService<Supply> {

    MallResponse getDetails(Integer id);

    MallResponse selectList(MallPage MallPage);

    MallResponse addSupply(DoSupply doSupply);

    MallResponse updateSupply(DoSupply doSupply);

    MallResponse delSupply(String id);

    MallResponse getNewSupply();

    boolean updateStatus(Integer id);
}
