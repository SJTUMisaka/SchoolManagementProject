package com.project.myschool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.myschool.pojo.Admin;
import com.project.myschool.pojo.LoginForm;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    IPage<Admin> getAdminsByOpr(Page<Admin> page, String name);
}
