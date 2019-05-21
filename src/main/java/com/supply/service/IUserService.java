package com.supply.service;

import com.supply.core.KkbPage;
import com.supply.core.KkbResponse;
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

    KkbResponse getByOpenId(String openId);

    KkbResponse getList(KkbPage kkbPage);

    KkbResponse addUser(DoUser doUser);
}
