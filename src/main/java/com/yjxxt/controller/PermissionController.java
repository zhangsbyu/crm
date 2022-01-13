package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@RequestMapping("permission")
@Controller
public class PermissionController extends BaseController {
@Resource
    private PermissionService permissionService;
}
