package com.project.myschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.myschool.mapper.StudentMapper;
import com.project.myschool.pojo.Admin;
import com.project.myschool.pojo.LoginForm;
import com.project.myschool.pojo.Student;
import com.project.myschool.service.StudentService;
import com.project.myschool.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<Student> getStudentsByOpr(Page<Student> page, Student student) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        String clazzName = student.getClazzName();
        String name = student.getName();
        if (!StringUtils.isEmpty(clazzName)){
            queryWrapper.eq("clazz_name", clazzName);
        }
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        return baseMapper.selectPage(page, queryWrapper);
    }
}
