package com.supply.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.supply.core.MallPage;
import com.supply.core.MallResponse;
import com.supply.domain.DoUser;
import com.supply.entity.User;
import com.google.common.collect.Maps;
import com.supply.service.IUserService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  用户部分前端控制器
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-16
 */
@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static Map<String, WxMaService> maServices = Maps.newHashMap();

    @Autowired
    IUserService  iUserService;

    /**
     * 列表
     */
    @PostMapping("/user/list")
    public MallResponse getList(@RequestBody MallPage MallPage) {
        return iUserService.getList(MallPage);
    }

    /**
     * 获取openId
     */
    @GetMapping("/user/openId")
    public String getOpenId(@RequestParam("code") String code) {
        if (StringUtils.isBlank(code)) {
            return "empty jscode";
        }
        final WxMaService wxService = maServices.get("wx432ef23a221d8815");
        String openId = null;
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            openId = session.getOpenid();
        } catch (WxErrorException e) {
            logger.error(e.getMessage(), e);
        }
        return openId;
    }

    /**
     * 新增
     */
    @PostMapping("/user")
    public MallResponse addUser(@RequestBody @Valid DoUser doUser) {
        return iUserService.addUser(doUser);
    }

}
