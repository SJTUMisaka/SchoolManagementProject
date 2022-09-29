package com.project.myschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.myschool.mapper.GradeMapper;
import com.project.myschool.pojo.Grade;
import com.project.myschool.service.GradeService;
import freemarker.template.utility.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.like("name", gradeName);
        }

        queryWrapper.orderByDesc("id");

        return baseMapper.selectPage(page, queryWrapper);
    }
}
