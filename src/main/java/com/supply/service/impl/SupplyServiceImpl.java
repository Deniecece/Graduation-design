package com.supply.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.core.KkbPage;
import com.supply.core.KkbResponse;
import com.supply.core.KkbStatus;
import com.supply.domain.DoSupply;
import com.supply.entity.Supply;
import com.supply.mapper.SupplyMapper;
import com.supply.service.ISupplyService;
import com.supply.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
public class SupplyServiceImpl extends ServiceImpl<SupplyMapper, Supply> implements ISupplyService {

    @Override
    public KkbResponse getDetails(Integer id) {
        Supply supply = baseMapper.selectById(id);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(supply);
        jsonObject.remove("createTime");
        jsonObject.put("createTime", DateUtil.formatDate(supply.getCreateTime().longValue()));
        return new KkbResponse(jsonObject);
    }

    @Override
    public KkbResponse selectList(KkbPage kkbPage) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> condition = kkbPage.getCondition();
        QueryWrapper queryWrapper = new QueryWrapper();
        if(condition != null) {
            if(condition.containsKey("name") && !"".equals(condition.get("name"))) {
                queryWrapper.like("name", condition.get("name"));
            }
            if(condition.containsKey("type") && !"".equals(condition.get("type"))) {
                if(condition.get("type").toString().contains(",")) {
                    queryWrapper.in("type", condition.get("type").toString().split(","));
                } else {
                    queryWrapper.eq("type", condition.get("type"));
                }
            }
            if(condition.containsKey("status")) {
                queryWrapper.in("status", CollectionUtils.arrayToList(condition.get("status").toString().split(",")));
            }
            if(condition.containsKey("openId")) {
                queryWrapper.eq("open_id", condition.get("openId"));
            }
        }
        List<Supply> supplies = baseMapper.selectList(queryWrapper);
        supplies.forEach(supply -> {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(supply);
            jsonObject.remove("createTime");
            jsonObject.put("createTime", DateUtil.formatDate(supply.getCreateTime().longValue()));
            list.add(jsonObject);
        });
        int currentStart = ((int)kkbPage.getCurrent()-1) * (int)kkbPage.getSize();
        int currentEnd = (int)kkbPage.getCurrent() * (int)kkbPage.getSize();
        if(currentStart > list.size()) {
            kkbPage.setRecords(null);
        } else {
            kkbPage.setRecords(supplies.size() > (int)kkbPage.getSize() ?
                    (currentEnd < supplies.size() ? list.subList(currentStart, currentEnd) : list.subList(currentStart, list.size())) :
                    list);
        }
        kkbPage.setTotal(new Long(list.size()));
        return new KkbResponse(kkbPage);
    }

    @Override
    public KkbResponse addSupply(DoSupply doSupply) {
        Supply supply = new Supply();
        BeanUtils.copyProperties(doSupply, supply);
        supply.setOpenId("wx90oofasd");
        supply.setCreateTime(DateUtil.getCurrentTime());
        int result = baseMapper.insert(supply);
        if(result == 1) {
            return new KkbResponse();
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }

    @Override
    public KkbResponse updateSupply(DoSupply doSupply) {
        Supply supply = getById(doSupply.getId());
        if(supply == null) {
            return new KkbResponse(KkbStatus.NO_DATA);
        }
        BeanUtils.copyProperties(doSupply, supply);
        int result = baseMapper.updateById(supply);
        if(result == 1) {
            return new KkbResponse();
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }

    @Override
    public KkbResponse delSupply(String id) {
        Supply supply = getById(id);
        if(supply == null) {
            return new KkbResponse(KkbStatus.NO_DATA);
        }
        int result = baseMapper.deleteById(id);
        if(result == 1) {
            return new KkbResponse();
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }

    @Override
    public KkbResponse getNewSupply() {
        List<Map<String, Object>> data = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        List<Supply> list = baseMapper.selectList(queryWrapper);
        list = list.subList(0, 5);
        list.forEach(supply -> {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(supply);
            jsonObject.remove("createTime");
            jsonObject.put("createTime", DateUtil.formatDate(supply.getCreateTime().longValue()));
            data.add(jsonObject);
        });
        return new KkbResponse(data);
    }

    @Override
    @Transactional
    public boolean updateStatus(Integer id) {
        Supply supply = baseMapper.selectById(id);
        if(supply == null) {
            return false;
        }
        int result = baseMapper.updateById(supply);
        if(result == 1) {
            return true;
        }
        return false;
    }
}
