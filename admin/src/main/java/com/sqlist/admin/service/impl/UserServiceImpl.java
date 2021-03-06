package com.sqlist.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sqlist.admin.domain.Product;
import com.sqlist.admin.domain.User;
import com.sqlist.admin.mapper.UserMapper;
import com.sqlist.admin.redis.RedisUtil;
import com.sqlist.admin.redis.keyprefix.UserKeyPrefix;
import com.sqlist.admin.service.FileService;
import com.sqlist.admin.service.ProductService;
import com.sqlist.admin.service.UserService;
import com.sqlist.admin.service.task.TaskService;
import com.sqlist.admin.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SqList
 * @date 2019/5/11 19:58
 * @description
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProductService productService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FileService fileService;

    @Override
    public Map<String, Object> list(PageVO pageVO) {
        HashMap<String, Object> map = new HashMap<>();

        if (pageVO.getLimit() != -1) {
            PageHelper.startPage(pageVO.getPage(), pageVO.getLimit());
        }
        List<User> userList = userMapper.selectAll();

        if (pageVO.getLimit() != -1) {
            map.put("total", ((Page)userList).getTotal());
        }
        map.put("list", userList);

        return map;
    }

    @Override
    public User get(Integer uid) {
        User user = new User();
        user.setUid(uid);
        return userMapper.selectByPrimaryKey(user);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void delete(Integer uid) {
        User user = new User();
        user.setUid(uid);
        userMapper.deleteByPrimaryKey(user);

        productService.deleteMultiple(uid);
        taskService.deleteMultiple(uid);
        fileService.deleteMultiple(uid);
    }

    @Override
    public Integer count() {
        return userMapper.selectCount(new User());
    }

    @Override
    public Integer countOnline() {
        return redisUtil.keys(UserKeyPrefix.TOKEN);
    }
}
