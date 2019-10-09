package com.supply.controller;


import com.supply.core.MallPage;
import com.supply.core.MallResponse;
import com.supply.domain.DoSupply;
import com.supply.service.ISupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  商品部分前端控制器
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-16
 */
@RestController
public class SupplyController {

    @Autowired
    ISupplyService iSupplyService;

    /**
     * 详情
     */
    @GetMapping("/supply/{id}")
    public MallResponse getDetails(@PathVariable Integer id) {
        return iSupplyService.getDetails(id);
    }

    /**
     * 列表-分页
     */
    @PostMapping("/supply/list")
    public MallResponse getList(@RequestBody MallPage MallPage) {
        return iSupplyService.selectList(MallPage);
    }
    /**
     * 添加
     */
    @PostMapping("/supply")
    public MallResponse addSupply(@RequestBody @Valid DoSupply doSupply){
        return iSupplyService.addSupply(doSupply);
    }

    /**
     * 修改
     */
    @PutMapping("/supply")
    public MallResponse updateSupply(@RequestBody @Valid DoSupply reqForm){
        return iSupplyService.updateSupply(reqForm);
    }

    /**
     * 删除
     */
    @DeleteMapping("/supply/{id}")
    public MallResponse delSupply(@PathVariable String id){
        return iSupplyService.delSupply(id);
    }

    /**
     * 最新发布
     */
    @GetMapping("/supply/new")
    public MallResponse getNewSupply() {
        return iSupplyService.getNewSupply();
    }
}

