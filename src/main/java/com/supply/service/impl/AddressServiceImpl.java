package com.supply.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.supply.core.MallResponse;
import com.supply.core.MallStatus;
import com.supply.domain.DoAddress;
import com.supply.entity.Address;
import com.supply.mapper.AddressMapper;
import com.supply.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.service.IUserService;
import com.supply.util.DateUtils;
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
    public MallResponse getDetails(Integer id) {
        Address address = baseMapper.selectById(id);
        if(address == null) {
            return new MallResponse(MallStatus.NO_DATA);
        }
        JSONObject jsonObject = (JSONObject) JSON.toJSON(address);
        jsonObject.remove("createTime");
        jsonObject.put("createTime", DateUtils.formatDate(address.getCreateTime().longValue()));
        return new MallResponse(jsonObject);
    }

    @Override
    public MallResponse selectByOpenId(String openId) {
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
                jsonObject.put("createTime", DateUtils.formatDate(address.getCreateTime().longValue()));
                list.add(jsonObject);
            });
        }
        return new MallResponse(list);
    }

    @Override
    public MallResponse addAddress(DoAddress doAddress) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", doAddress.getOpenId());
        queryWrapper.eq("mobile", doAddress.getMobile());
        queryWrapper.eq("name", doAddress.getName());
        queryWrapper.eq("address", doAddress.getAddress());
        Address address = baseMapper.selectOne(queryWrapper);
        if(address != null) {
            return new MallResponse(MallStatus.DATA_EXIST);
        }
        address = new Address();
        BeanUtils.copyProperties(doAddress, address);
        address.setCreateTime(DateUtils.getCurrentTime());
        int result = baseMapper.insert(address);
        if(result == 1) {
            return new MallResponse(MallStatus.SUCCESS);
        }
        return new MallResponse(MallStatus.FAILURE);
    }

    @Override
    public MallResponse updateAddress(DoAddress doAddress) {
        Address address = baseMapper.selectById(doAddress.getId());
        if(address == null) {
            return new MallResponse(MallStatus.NO_DATA);
        }
        BeanUtils.copyProperties(doAddress, address);
        int result = baseMapper.updateById(address);
        if(result == 1) {
            return new MallResponse(MallStatus.SUCCESS);
        }
        return new MallResponse(MallStatus.FAILURE);
    }

    @Override
    public MallResponse delAddress(String id) {
        Address address = baseMapper.selectById(id);
        if(address == null) {
            return new MallResponse(MallStatus.NO_DATA);
        }
        int result = baseMapper.deleteById(id);
        if(result == 1) {
            return new MallResponse(MallStatus.SUCCESS);
        }
        return new MallResponse(MallStatus.FAILURE);
    }

    @Override
    public String getUserName(Integer id) {
        return baseMapper.selectById(id).getName();
    }
}
