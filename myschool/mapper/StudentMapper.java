package com.project.myschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.myschool.pojo.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMapper extends BaseMapper<Student> {
}
