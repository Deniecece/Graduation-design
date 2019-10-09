package com.supply.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonObject;
import com.supply.core.MallPage;
import com.supply.core.MallResponse;
import com.supply.core.MallStatus;
import com.supply.domain.DoOrder;
import com.supply.domain.DoSupply;
import com.supply.entity.Supply;
import com.supply.entity.TOrder;
import com.supply.entity.User;
import com.supply.mapper.OrderMapper;
import com.supply.service.IAddressService;
import com.supply.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.service.ISupplyService;
import com.supply.service.IUserService;
import com.supply.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, TOrder> implements IOrderService {

    @Autowired
    ISupplyService iSupplyService;
    @Autowired
    IAddressService iAddressService;
    @Autowired
    IUserService iUserService;

    @Override
    public MallResponse getDetails(Integer id) {
        TOrder supply = baseMapper.selectById(id);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(supply);
        jsonObject.remove("createTime");
        jsonObject.put("createTime", DateUtils.formatDate(supply.getCreateTime().longValue()));
        return new MallResponse(jsonObject);
    }

    @Override
    public MallResponse selectByOpenId(String openId, MallPage MallPage) {
        List<Map<String, Object>> data = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        if(MallPage != null && MallPage.getCondition()!= null && MallPage.getCondition().containsKey("id") && StringUtils.isNotEmpty(MallPage.getCondition().get("id").toString())) {
            queryWrapper.eq("id", MallPage.getCondition().get("id"));
        }
        if(MallPage != null && MallPage.getCondition()!= null && MallPage.getCondition().containsKey("status") && StringUtils.isNotEmpty(MallPage.getCondition().get("status").toString()) && !"全部".equals(MallPage.getCondition().get("status").toString())) {
            queryWrapper.eq("status", MallPage.getCondition().get("status"));
        }
        if(MallPage != null && MallPage.getCondition()!= null && MallPage.getCondition().containsKey("openId") && StringUtils.isNotEmpty(MallPage.getCondition().get("openId").toString())) {
            queryWrapper.eq("open_id", MallPage.getCondition().get("openId"));
        }
        List<TOrder> list;
        if(openId == null && MallPage == null) {
            list = baseMapper.selectList(queryWrapper);
            list.forEach(order -> {
                JSONObject jsonObject = (JSONObject) JSON.toJSON(order);
                jsonObject.remove("create_time");
                jsonObject.put("userName", iAddressService.getUserName(order.getAddressId()));
                jsonObject.put("createTime", DateUtils.getTimestamp2DateStr(order.getCreateTime().toString()));
                data.add(jsonObject);
            });
            return new MallResponse(data);
        }
        list = baseMapper.selectPage(MallPage, queryWrapper).getRecords();
        list.forEach(order -> {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(order);
            jsonObject.remove("create_time");
            jsonObject.put("createTime", DateUtils.getTimestamp2DateStr(order.getCreateTime().toString()));
            data.add(jsonObject);
        });
        int currentStart = ((int)MallPage.getCurrent()-1) * (int)MallPage.getSize();
        int currentEnd = (int)MallPage.getCurrent() * (int)MallPage.getSize();
        if(currentStart > list.size()) {
            MallPage.setRecords(null);
        } else {
            MallPage.setRecords(data.size() > (int)MallPage.getSize() ?
                    (currentEnd < data.size() ? data.subList(currentStart, currentEnd) : data.subList(currentStart, data.size())) :
                    data);
        }
        MallPage.setTotal(new Long(data.size()));
        return new MallResponse(MallPage);
    }

    @Override
    public MallResponse updateOrder(DoOrder doOrder) {
        TOrder supply = getById(doOrder.getId());
        if(supply == null) {
            return new MallResponse(MallStatus.NO_DATA);
        }
        if(doOrder.getStatus()!=null && doOrder.getStatus().equals("已收货")) {
            DoSupply doSupply = new DoSupply();
            doSupply.setId(supply.getSupplyNo());
            doSupply.setStatus("已收货");
            iSupplyService.updateSupply(doSupply);
        }
        BeanUtils.copyProperties(doOrder, supply);
        int result = baseMapper.updateById(supply);
        if(result == 1) {
            return new MallResponse();
        }
        return new MallResponse(MallStatus.FAILURE);
    }

    @Override
    @Transactional
    public MallResponse addOrder(DoOrder doOrder) {
        //查询订单中商品是否存在
        Supply supply = iSupplyService.getById(doOrder.getSupplyNo());
        if(supply == null) {
            return new MallResponse(MallStatus.NO_DATA.getCode(), "商品不存在");
        }
        //查询订单中商品是否已出售 0未出售 1已出售
        if(supply.getStatus()!=null &&supply.getStatus().equals("已出售")) {
            return new MallResponse(MallStatus.SALED);
        }
        TOrder order = new TOrder();
        BeanUtils.copyProperties(doOrder, order);
        order.setCreateTime(DateUtils.getCurrentTime());
        if(order.getSupplyNo() != null) {
            order.setSupplyName(iSupplyService.getById(doOrder.getSupplyNo()).getName());
            order.setType(iSupplyService.getById(doOrder.getSupplyNo()).getType());
        }
        if(doOrder.getAddressId() != null) {
            order.setAddressName(iAddressService.getById(doOrder.getAddressId()).getName());
            order.setAddress(iAddressService.getById(doOrder.getAddressId()).getAddress());
        }
        if(StringUtils.isEmpty(doOrder.getOpenId())) {
            order.setOpenId("wx90oofasd");
        }
        order.setStatus("已付款");
        order.setPrice(iSupplyService.getById(doOrder.getSupplyNo()).getPrice());
        int result = baseMapper.insert(order);
        if(result == 1) {
            //TODO 购物车创建订单后商品显示出售
            boolean b = iSupplyService.updateStatus(doOrder.getSupplyNo());
            if(!b) {
                return new MallResponse(MallStatus.NO_DATA.getCode(), "商品不存在");

            }
            return new MallResponse();
        }
        return new MallResponse(MallStatus.FAILURE);
    }

    @Override
    public MallResponse delOrder(Integer id) {
        TOrder order = baseMapper.selectById(id);
        if(order == null) {
            return new MallResponse(MallStatus.NO_DATA);
        }
        int result = baseMapper.deleteById(id);
        if(result == 1) {
            return new MallResponse();
        }
        return new MallResponse(MallStatus.FAILURE);
    }
}
