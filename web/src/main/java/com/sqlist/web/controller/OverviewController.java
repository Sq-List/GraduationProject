package com.sqlist.web.controller;

import com.sqlist.web.domain.Task;
import com.sqlist.web.domain.User;
import com.sqlist.web.result.Result;
import com.sqlist.web.service.DeviceService;
import com.sqlist.web.service.FileService;
import com.sqlist.web.service.ProductService;
import com.sqlist.web.service.TaskSendSumService;
import com.sqlist.web.service.task.TaskService;
import com.sqlist.web.vo.BetweenTimeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

/**
 * @author SqList
 * @date 2019/5/13 1:43
 * @description
 **/
@Slf4j
@RestController
@RequestMapping("/api/overview")
public class OverviewController {

    @Autowired
    private ProductService productService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TaskSendSumService taskSendSumService;

    @RequestMapping(value = "/nums", method = RequestMethod.GET)
    public Result nums(User user) {
        HashMap<String, Integer> numsMap = new HashMap<>(6);
        numsMap.put("productNum", productService.count(user));
        numsMap.put("deviceNum", deviceService.count(user));
        numsMap.put("taskNum", taskService.count(user));
        numsMap.put("fileNum", fileService.count(user));

        return Result.success(numsMap);
    }

    @RequestMapping(value = "/1hour", method = RequestMethod.GET)
    public Result get1hourInfo(User user, BetweenTimeVO betweenTimeVO) {
        log.info("betweenTime: {}", betweenTimeVO);
        Task task = new Task();
        task.setUid(user.getUid());
        return Result.success(taskSendSumService.getInfoBetweenTime(task, betweenTimeVO.getStartTime(), betweenTimeVO.getEndTime()));
    }

    @RequestMapping(value = "/7day", method = RequestMethod.GET)
    public Result get7DayInfo(User user) {
        Task task = new Task();
        task.setUid(user.getUid());
        return Result.success(taskSendSumService.get7DayInfo(task));
    }

    @RequestMapping(value = "/total", method = RequestMethod.GET)
    public Result getTotalInfo(User user) {
        Task task = new Task();
        task.setUid(user.getUid());
        return Result.success(taskSendSumService.getTotalInfo(task));
    }
}
