package com.supply.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.core.KkbPage;
import com.supply.core.KkbResponse;
import com.supply.core.KkbStatus;
import com.supply.domain.DoUser;
import com.supply.entity.User;
import com.supply.mapper.UserMapper;
import com.supply.service.IUserService;
import com.supply.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User getUserByOpenId(String openId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public KkbResponse getByOpenId(String openId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        User user = baseMapper.selectOne(queryWrapper);
        JSONObject jsonObject = new JSONObject();
        if(user != null) {
            jsonObject = (JSONObject) JSON.toJSON(user);
            jsonObject.remove("createTime");
            jsonObject.put("createTime", DateUtil.formatDate(user.getCreateTime().longValue()));
        }
        return new KkbResponse(jsonObject);
    }

    @Override
    public KkbResponse getList(KkbPage kkbPage) {
        List<Map<String, Object>> list = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        if(kkbPage.getCondition().containsKey("name")) {
            queryWrapper.like("name", kkbPage.getCondition().get("name"));
        }
        List<User> users = baseMapper.selectList(queryWrapper);
        users.forEach(user -> {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(user);
            jsonObject.remove("createTime");
            jsonObject.put("createTime", DateUtil.formatDate(user.getCreateTime().longValue()));
            list.add(jsonObject);
        });
        int currentStart = ((int)kkbPage.getCurrent()-1) * (int)kkbPage.getSize();
        int currentEnd = (int)kkbPage.getCurrent() * (int)kkbPage.getSize();
        if(currentStart > list.size()) {
            kkbPage.setRecords(null);
        } else {
            kkbPage.setRecords(users.size() > (int)kkbPage.getSize() ?
                    (currentEnd < users.size() ? list.subList(currentStart, currentEnd) : list.subList(currentStart, list.size())) :
                    list);
        }
        kkbPage.setTotal(new Long(list.size()));
        return new KkbResponse(kkbPage);
    }

    @Override
    public KkbResponse addUser(DoUser doUser) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", doUser.getOpenId());
        User user = baseMapper.selectOne(queryWrapper);
        if(user != null) {
            return new KkbResponse(KkbStatus.DATA_EXIST);
        }
        user = new User();
        BeanUtils.copyProperties(doUser, user);
        user.setCreateTime(DateUtil.getCurrentTime());
        int result = baseMapper.insert(user);
        if(result == 1) {
            return new KkbResponse(KkbStatus.SUCCESS);
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }
}
