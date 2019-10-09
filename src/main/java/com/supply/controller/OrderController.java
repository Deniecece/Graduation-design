package com.supply.controller;


import com.supply.core.MallPage;
import com.supply.core.MallResponse;
import com.supply.core.MallStatus;
import com.supply.domain.DoOrder;
import com.supply.service.IOrderService;
import com.supply.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  订单部分前端控制器
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-16
 */
@RestController
public class OrderController {

    @Autowired
    IOrderService iOrderService;
    @Autowired
    IUserService iUserService;

    /**
     * 详情
     */
    @GetMapping("/order/{id}")
    public MallResponse getDetails(@PathVariable Integer id) {
        return iOrderService.getDetails(id);
    }

    /**
     * 列表
     */
    @PostMapping("/order/list")
    public MallResponse getList(@RequestParam(name = "openId", required = false) String openId,
                               @RequestBody(required = false)MallPage MallPage) {
        Object user = iUserService.getByOpenId(openId);
        if(user == null) {
            return new MallResponse(MallStatus.NO_DATA.getCode(), "用户不存在");
        }
        return iOrderService.selectByOpenId(openId, MallPage);
    }
    /**
     * 添加
     */
    @PostMapping("/order")
    public MallResponse addOrder(@RequestBody @Valid DoOrder doOrder){
        return iOrderService.addOrder(doOrder);
    }

    /**
     * 修改状态
     */
    @PutMapping("/order")
    public MallResponse updateOrder(@RequestBody @Valid DoOrder doOrder) {
        return iOrderService.updateOrder(doOrder);
    }
    /**
     * 删除
     */
    @DeleteMapping("/order/{id}")
    public MallResponse delOrder(@PathVariable Integer id){
        return iOrderService.delOrder(id);
    }
}

