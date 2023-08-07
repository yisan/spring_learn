package com.bingo.learn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bingo.learn.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**Ø
 * Created by ing on 2022/1/6 18:02
 */
public interface UserService  {
    Boolean save(User user);
    Boolean update(User user);
    Boolean delete(Long id);
    User getById(Long id);
    List<User> getAll();
    IPage<User> getPage(int current,int pageSize);
}
