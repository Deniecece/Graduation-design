package com.supply.service;

import com.supply.core.MallPage;
import com.supply.core.MallResponse;
import com.supply.domain.DoUser;
import com.supply.entity.User;
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
public interface IUserService extends IService<User> {

    User getUserByOpenId(String openId);

    MallResponse getByOpenId(String openId);

    MallResponse getList(MallPage MallPage);

    MallResponse addUser(DoUser doUser);
}
