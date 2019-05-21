package com.supply.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonObject;
import com.supply.core.KkbPage;
import com.supply.core.KkbResponse;
import com.supply.core.KkbStatus;
import com.supply.domain.DoOrder;
import com.supply.entity.Supply;
import com.supply.entity.TOrder;
import com.supply.entity.User;
import com.supply.mapper.OrderMapper;
import com.supply.service.IAddressService;
import com.supply.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.service.ISupplyService;
import com.supply.service.IUserService;
import com.supply.util.DateUtil;
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
    public KkbResponse getDetails(Integer id) {
        TOrder supply = baseMapper.selectById(id);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(supply);
        jsonObject.remove("createTime");
        jsonObject.put("createTime", DateUtil.formatDate(supply.getCreateTime().longValue()));
        return new KkbResponse(jsonObject);
    }

    @Override
    public KkbResponse selectByOpenId(String openId, KkbPage kkbPage) {
        List<Map<String, Object>> data = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        if(kkbPage != null && kkbPage.getCondition()!= null && kkbPage.getCondition().containsKey("id") && StringUtils.isNotEmpty(kkbPage.getCondition().get("id").toString())) {
            queryWrapper.eq("id", kkbPage.getCondition().get("id"));
        }
        if(kkbPage != null && kkbPage.getCondition()!= null && kkbPage.getCondition().containsKey("status") && StringUtils.isNotEmpty(kkbPage.getCondition().get("status").toString()) && !"全部".equals(kkbPage.getCondition().get("status").toString())) {
            queryWrapper.eq("status", kkbPage.getCondition().get("status"));
        }
        List<TOrder> list;
        if(openId == null && kkbPage == null) {
            list = baseMapper.selectList(queryWrapper);
            list.forEach(order -> {
                JSONObject jsonObject = (JSONObject) JSON.toJSON(order);
                jsonObject.remove("create_time");
                jsonObject.put("userName", iAddressService.getUserName(order.getAddressId()));
                jsonObject.put("createTime", DateUtil.formatDate(order.getCreateTime().longValue()));
                data.add(jsonObject);
            });
            return new KkbResponse(data);
        }
        list = baseMapper.selectPage(kkbPage, queryWrapper).getRecords();
        list.forEach(order -> {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(order);
            jsonObject.remove("create_time");
            jsonObject.put("createTime", DateUtil.formatDate(order.getCreateTime().longValue()));
            data.add(jsonObject);
        });
        int currentStart = ((int)kkbPage.getCurrent()-1) * (int)kkbPage.getSize();
        int currentEnd = (int)kkbPage.getCurrent() * (int)kkbPage.getSize();
        if(currentStart > list.size()) {
            kkbPage.setRecords(null);
        } else {
            kkbPage.setRecords(list.size() > (int)kkbPage.getSize() ?
                    (currentEnd < list.size() ? list.subList(currentStart, currentEnd) : list.subList(currentStart, list.size())) :
                    list);
        }
        kkbPage.setTotal(new Long(list.size()));
        return new KkbResponse(kkbPage);
    }

    @Override
    public KkbResponse updateOrder(DoOrder doOrder) {
        TOrder supply = getById(doOrder.getId());
        if(supply == null) {
            return new KkbResponse(KkbStatus.NO_DATA);
        }
        BeanUtils.copyProperties(doOrder, supply);
        int result = baseMapper.updateById(supply);
        if(result == 1) {
            return new KkbResponse();
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }

    @Override
    @Transactional
    public KkbResponse addOrder(DoOrder doOrder) {
        //查询订单中商品是否存在
        Supply supply = iSupplyService.getById(doOrder.getSupplyNo());
        if(supply == null) {
            return new KkbResponse(KkbStatus.NO_DATA.getCode(), "商品不存在");
        }
        //查询订单中商品是否已出售 0未出售 1已出售
        if(supply.getStatus()!=null &&supply.getStatus().equals("已出售")) {
            return new KkbResponse(KkbStatus.SALED);
        }
        TOrder order = new TOrder();
        BeanUtils.copyProperties(doOrder, order);
        order.setCreateTime(DateUtil.getCurrentTime());
        if(order.getSupplyNo() != null) {
            order.setSupplyName(iSupplyService.getById(doOrder.getSupplyNo()).getName());
        }
        if(doOrder.getAddressId() != null) {
            order.setAddressName(iAddressService.getById(doOrder.getAddressId()).getName());
            order.setAddress(iAddressService.getById(doOrder.getAddressId()).getAddress());
        }
        int result = baseMapper.inserta(order);
        if(result == 1) {
            //TODO 购物车创建订单后商品显示出售
            boolean b = iSupplyService.updateStatus(doOrder.getSupplyNo());
            if(!b) {
                return new KkbResponse(KkbStatus.NO_DATA.getCode(), "商品不存在");

            }
            return new KkbResponse();
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }

    @Override
    public KkbResponse delOrder(Integer id) {
        TOrder order = baseMapper.selectById(id);
        if(order == null) {
            return new KkbResponse(KkbStatus.NO_DATA);
        }
        int result = baseMapper.deleteById(id);
        if(result == 1) {
            return new KkbResponse();
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }
}
