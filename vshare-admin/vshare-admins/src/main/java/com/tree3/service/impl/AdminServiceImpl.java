package com.tree3.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.tree3.dao.AdminMapper;
import com.tree3.pojo.entity.Admin;
import com.tree3.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author rupert
 * @since 2024-03-08 09:27:41
 */
@Slf4j
@Transactional
@Service("adminService")
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminMapper adminMapper;


    @Override
    public List<Admin> queryAll() {
        return adminMapper.queryAll();
    }

    @Override
    public List<Admin> queryAll(Admin admin) {
        return adminMapper.queryAll(admin);
    }

    @Override
    public Admin queryAdmin(Admin admin) {
        return adminMapper.queryAdmin(admin);
    }


    @Override
    public Admin queryById(Integer id) {
        return adminMapper.queryById(id);
    }

    @Override
    public Admin update(Admin admin) {
        this.adminMapper.update(admin);
        return queryById(admin.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return adminMapper.deleteById(id) > 0;
    }

    @Override
    public Admin insert(Admin admin) {
        this.adminMapper.insert(admin);
        return admin;
    }

    @Override
    public Admin login(Admin admin) {
        //Step 1: 查询数据库
        Admin adminDB = adminMapper.findByUsername(admin.getUsername());
        if (ObjectUtils.isEmpty(adminDB)) {
            throw new RuntimeException("用户名错误");
        }

        String psw = DigestUtils.md5DigestAsHex(admin.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!StringUtils.equals(psw, adminDB.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return adminDB;
    }
}
