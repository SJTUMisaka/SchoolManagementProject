package com.project.myschool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.myschool.pojo.LoginForm;
import com.project.myschool.pojo.Student;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);

    IPage<Student> getStudentsByOpr(Page<Student> page, Student student);
}
