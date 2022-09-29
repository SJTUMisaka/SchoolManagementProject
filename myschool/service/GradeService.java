package com.project.myschool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.myschool.pojo.Grade;

public interface GradeService extends IService<Grade> {
    IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName);
}
