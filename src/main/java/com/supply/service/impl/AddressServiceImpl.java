package com.supply.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.supply.core.KkbResponse;
import com.supply.core.KkbStatus;
import com.supply.domain.DoAddress;
import com.supply.entity.Address;
import com.supply.mapper.AddressMapper;
import com.supply.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.service.IUserService;
import com.supply.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.xml.ws.Action;
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
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IAddressService iAddressService;

    @Override
    public KkbResponse getDetails(Integer id) {
        Address address = baseMapper.selectById(id);
        if(address == null) {
            return new KkbResponse(KkbStatus.NO_DATA);
        }
        JSONObject jsonObject = (JSONObject) JSON.toJSON(address);
        jsonObject.remove("createTime");
        jsonObject.put("createTime", DateUtil.formatDate(address.getCreateTime().longValue()));
        return new KkbResponse(jsonObject);
    }

    @Override
    public KkbResponse selectByOpenId(String openId) {
        List<Map<String, Object>> list = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        List<Address> addresses = baseMapper.selectList(queryWrapper);
        if(!CollectionUtils.isEmpty(addresses)) {
            addresses.forEach(address ->
            {
                JSONObject jsonObject = (JSONObject) JSON.toJSON(address);
                jsonObject.remove("createTime");
                jsonObject.put("userName", iUserService.getUserByOpenId(openId).getName());
                jsonObject.put("createTime", DateUtil.formatDate(address.getCreateTime().longValue()));
                list.add(jsonObject);
            });
        }
        return new KkbResponse(list);
    }

    @Override
    public KkbResponse addAddress(DoAddress doAddress) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", doAddress.getOpenId());
        queryWrapper.eq("mobile", doAddress.getMobile());
        queryWrapper.eq("name", doAddress.getName());
        queryWrapper.eq("address", doAddress.getAddress());
        Address address = baseMapper.selectOne(queryWrapper);
        if(address != null) {
            return new KkbResponse(KkbStatus.DATA_EXIST);
        }
        address = new Address();
        BeanUtils.copyProperties(doAddress, address);
        address.setCreateTime(DateUtil.getCurrentTime());
        int result = baseMapper.insert(address);
        if(result == 1) {
            return new KkbResponse(KkbStatus.SUCCESS);
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }

    @Override
    public KkbResponse updateAddress(DoAddress doAddress) {
        Address address = baseMapper.selectById(doAddress.getId());
        if(address == null) {
            return new KkbResponse(KkbStatus.NO_DATA);
        }
        BeanUtils.copyProperties(doAddress, address);
        int result = baseMapper.updateById(address);
        if(result == 1) {
            return new KkbResponse(KkbStatus.SUCCESS);
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }

    @Override
    public KkbResponse delAddress(String id) {
        Address address = baseMapper.selectById(id);
        if(address == null) {
            return new KkbResponse(KkbStatus.NO_DATA);
        }
        int result = baseMapper.deleteById(id);
        if(result == 1) {
            return new KkbResponse(KkbStatus.SUCCESS);
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }

    @Override
    public String getUserName(Integer id) {
        return baseMapper.selectById(id).getName();
    }
}
