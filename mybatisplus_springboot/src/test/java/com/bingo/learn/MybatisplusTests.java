package com.bingo.learn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bingo.learn.domain.User;
import com.bingo.learn.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MybatisplusTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectList() {
        List<User> userList = userMapper.selectList(null);
        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setName("孙尚香");
        user.setAge(22);
        user.setUserName("阿香");
        user.setEmail("axiang@163.com");
        user.setPassword("123123");
        int ret = userMapper.insert(user);//数据库受影响的行数
        System.out.println("ret: " + ret);
        System.out.println("id: " + user.getId());
    }

    @Test
    public void testUpdateById() {
        User user = new User();
        user.setId(1L); // 根据id来更新
        user.setAge(19); // 更新的字段
        user.setPassword("888888");
        int ret = userMapper.updateById(user);
        System.out.println("ret: " + ret);
    }

    @Test
    public void testUpdates() {
        User user = new User();
        user.setAge(30); // 更新的字段
        user.setPassword("889999");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", "zhangsan");
        userMapper.update(user, queryWrapper);
    }

    @Test
    public void testUpdaes1() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("user_name", "法外狂徒").set("age", 25).eq("name", "张三");
        int ret = userMapper.update(null, updateWrapper);
        System.out.println("ret: " + ret);
    }

    @Test
    public void testDeleteById() {
        int ret = userMapper.deleteById(6L);
        System.out.println("ret: " + ret);
    }

    @Test
    public void deleteByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 24);
        map.put("name", "孙七");
        // 将map中的元素设置为删除条件，多个之间为and关系
        int ret = userMapper.deleteByMap(map);
        System.out.println("ret: " + ret);
    }

    @Test
    public void delete() {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        User user = new User();
        user.setUserName("wangwu");
        user.setPassword("123456");
        queryWrapper.setEntity(user);
        int ret = userMapper.delete(queryWrapper);
        System.out.println("ret: " + ret);
    }

    @Test
    public void testDeleteBatchIds() {
        int ret = userMapper.deleteBatchIds(Arrays.asList(2L, 3L));
        System.out.println("ret: " + ret);
    }

    @Test
    public void testSelectById() {
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    @Test
    public void testSelectBatchIds() {
//根据id集合批量查询
        List<User> users = this.userMapper.selectBatchIds(Arrays.asList(2L, 3L, 10L));
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testSelectOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", "lisi");
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }
}
