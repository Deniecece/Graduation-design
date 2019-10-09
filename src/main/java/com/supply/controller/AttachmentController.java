package com.supply.controller;


import com.supply.core.MallResponse;
import com.supply.domain.DoAttachment;
import com.supply.service.IAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 *  附件部分前端控制器
 * </p>
 *
 * @author Deniecece
 * @since 2019-04-16
 */
@RestController
public class AttachmentController {

    @Autowired
    IAttachmentService iAttachmentService;

    @PostMapping("/attachment")
    public MallResponse addAttachment(@RequestBody @Valid DoAttachment doAttachment) {
        return iAttachmentService.addAttachment(doAttachment);
    }
}

