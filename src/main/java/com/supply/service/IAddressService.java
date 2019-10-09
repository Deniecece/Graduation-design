package com.supply.service;

import com.supply.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;
import com.supply.domain.DoAddress;
import com.supply.core.MallResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-16
 */
public interface IAddressService extends IService<Address> {

    MallResponse getDetails(Integer id);

    MallResponse selectByOpenId(String openId);

    MallResponse addAddress(DoAddress doAddress);

    MallResponse updateAddress(DoAddress doAddress);

    MallResponse delAddress(String id);

    String getUserName(Integer id);
}
