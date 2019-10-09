package com.supply.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.core.MallPage;
import com.supply.core.MallResponse;
import com.supply.core.MallStatus;
import com.supply.domain.DoUser;
import com.supply.entity.User;
import com.supply.mapper.UserMapper;
import com.supply.service.IUserService;
import com.supply.util.DateUtils;
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
    public MallResponse getByOpenId(String openId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        User user = baseMapper.selectOne(queryWrapper);
        JSONObject jsonObject = new JSONObject();
        if(user != null) {
            jsonObject = (JSONObject) JSON.toJSON(user);
            jsonObject.remove("createTime");
            jsonObject.put("createTime", DateUtils.formatDate(user.getCreateTime().longValue()));
        }
        return new MallResponse(jsonObject);
    }

    @Override
    public MallResponse getList(MallPage MallPage) {
        List<Map<String, Object>> list = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        if(MallPage.getCondition().containsKey("name")) {
            queryWrapper.like("name", MallPage.getCondition().get("name"));
        }
        List<User> users = baseMapper.selectList(queryWrapper);
        users.forEach(user -> {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(user);
            jsonObject.remove("createTime");
            jsonObject.put("createTime", DateUtils.formatDate(user.getCreateTime().longValue()));
            list.add(jsonObject);
        });
        int currentStart = ((int)MallPage.getCurrent()-1) * (int)MallPage.getSize();
        int currentEnd = (int)MallPage.getCurrent() * (int)MallPage.getSize();
        if(currentStart > list.size()) {
            MallPage.setRecords(null);
        } else {
            MallPage.setRecords(users.size() > (int)MallPage.getSize() ?
                    (currentEnd < users.size() ? list.subList(currentStart, currentEnd) : list.subList(currentStart, list.size())) :
                    list);
        }
        MallPage.setTotal(new Long(list.size()));
        return new MallResponse(MallPage);
    }

    @Override
    public MallResponse addUser(DoUser doUser) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", doUser.getOpenId());
        User user = baseMapper.selectOne(queryWrapper);
        if(user != null) {
            return new MallResponse(MallStatus.DATA_EXIST);
        }
        user = new User();
        BeanUtils.copyProperties(doUser, user);
        user.setCreateTime(DateUtils.getCurrentTime());
        int result = baseMapper.insert(user);
        if(result == 1) {
            return new MallResponse(MallStatus.SUCCESS);
        }
        return new MallResponse(MallStatus.FAILURE);
    }
}
