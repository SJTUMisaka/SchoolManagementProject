package com.project.myschool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.myschool.pojo.LoginForm;
import com.project.myschool.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);

    IPage<Teacher> getTeachersByOpr(Page<Teacher> page, Teacher teacher);
}
