package com.example.onlinemall.controller;

import com.example.onlinemall.common.Result;
import com.example.onlinemall.entity.UserBehavior;
import com.example.onlinemall.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理员后台 - 用户行为数据控制器
 */
@RestController
@RequestMapping("/admin/behaviors")
public class AdminBehaviorController {

    @Autowired
    private UserBehaviorService userBehaviorService;

    /**
     * 获取用户行为记录，支持分页
     * @param limit 返回记录的数量，默认为500
     * @return 用户行为列表
     */
    @GetMapping
    public Result<List<UserBehavior>> getAllBehaviors(
            @RequestParam(defaultValue = "500") Integer limit) {
        List<UserBehavior> behaviors = userBehaviorService.getAllBehaviors(limit);
        return Result.success(behaviors, "获取用户行为记录成功");
    }
}