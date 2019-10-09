package com.supply.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supply.core.MallPage;
import com.supply.core.MallResponse;
import com.supply.core.MallStatus;
import com.supply.domain.DoSupply;
import com.supply.entity.Supply;
import com.supply.mapper.SupplyMapper;
import com.supply.service.ISupplyService;
import com.supply.util.DateUtils;
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
    public MallResponse getDetails(Integer id) {
        Supply supply = baseMapper.selectById(id);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(supply);
        jsonObject.remove("createTime");
        jsonObject.put("createTime", DateUtils.formatDate(supply.getCreateTime().longValue()));
        return new MallResponse(jsonObject);
    }

    @Override
    public MallResponse selectList(MallPage MallPage) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> condition = MallPage.getCondition();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
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
            if(condition.containsKey("status") && !"全部".equals(condition.get("status").toString())) {
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
            jsonObject.put("createTime", DateUtils.formatDate(supply.getCreateTime().longValue()));
            list.add(jsonObject);
        });
        int currentStart = ((int)MallPage.getCurrent()-1) * (int)MallPage.getSize();
        int currentEnd = (int)MallPage.getCurrent() * (int)MallPage.getSize();
        if(currentStart > list.size()) {
            MallPage.setRecords(null);
        } else {
            MallPage.setRecords(supplies.size() > (int)MallPage.getSize() ?
                    (currentEnd < supplies.size() ? list.subList(currentStart, currentEnd) : list.subList(currentStart, list.size())) :
                    list);
        }
        MallPage.setTotal(new Long(list.size()));
        return new MallResponse(MallPage);
    }

    @Override
    public MallResponse addSupply(DoSupply doSupply) {
        Supply supply = new Supply();
        BeanUtils.copyProperties(doSupply, supply);
        supply.setOpenId("wx90oofasd");
        supply.setStatus("未出售");
        supply.setCreateTime(DateUtils.getCurrentTime());
        supply.setImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1558591351328&di=fb4b51e6bc5de20cea0c6d9f4ec37b02&imgtype=0&src=http%3A%2F%2Fimg3.doubanio.com%2Fview%2Fsubject%2Fl%2Fpublic%2Fs29544766.jpg");
        int result = baseMapper.insert(supply);
        if(result == 1) {
            return new MallResponse();
        }
        return new MallResponse(MallStatus.FAILURE);
    }

    @Override
    public MallResponse updateSupply(DoSupply doSupply) {
        Supply supply = getById(doSupply.getId());
        if(supply == null) {
            return new MallResponse(MallStatus.NO_DATA);
        }
        BeanUtils.copyProperties(doSupply, supply);
        int result = baseMapper.updateById(supply);
        if(result == 1) {
            return new MallResponse();
        }
        return new MallResponse(MallStatus.FAILURE);
    }

    @Override
    public MallResponse delSupply(String id) {
        Supply supply = getById(id);
        if(supply == null) {
            return new MallResponse(MallStatus.NO_DATA);
        }
        int result = baseMapper.deleteById(id);
        if(result == 1) {
            return new MallResponse();
        }
        return new MallResponse(MallStatus.FAILURE);
    }

    @Override
    public MallResponse getNewSupply() {
        List<Map<String, Object>> data = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        List<Supply> list = baseMapper.selectList(queryWrapper);
        list = list.subList(0, 5);
        list.forEach(supply -> {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(supply);
            jsonObject.remove("createTime");
            jsonObject.put("createTime", DateUtils.formatDate(supply.getCreateTime().longValue()));
            data.add(jsonObject);
        });
        return new MallResponse(data);
    }

    @Override
    @Transactional
    public boolean updateStatus(Integer id) {
        Supply supply = baseMapper.selectById(id);
        if(supply == null) {
            return false;
        }
        supply.setStatus("已出售");
        int result = baseMapper.updateById(supply);
        if(result == 1) {
            return true;
        }
        return false;
    }
}
