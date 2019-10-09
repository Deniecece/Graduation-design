package com.supply.core;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 简单分页模型
 * </p>
 *
 * @author Deniecece
 * @since 2018-06-09
 */
public class MallPage<T> extends Page<T> {

    private static final long serialVersionUID = 8545996863226528798L;

    private Map<Object, Object> condition = new HashMap<>();

    private String[] ascs;

    private String[] descs;

    public Map<Object, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<Object, Object> condition) {
        this.condition = condition;
    }
    public void addCondition(String key, Object value) {
        getCondition().put(key, value);
    }

    public String[] getAscs() {
        return ascs;
    }

    public void setAscs(String[] ascs) {
        this.ascs = ascs;
        if (ascs != null && ascs.length > 0) super.setAsc(ascs);
    }

    public String[] getDescs() {
        return descs;
    }

    public void setDescs(String[] descs) {
        this.descs = descs;
        if (descs != null && descs.length > 0) super.setDesc(descs);
    }
}
