package com.project.myschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.myschool.mapper.ClazzMapper;
import com.project.myschool.pojo.Clazz;
import com.project.myschool.service.ClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public IPage<Clazz> getClazzsByOpr(Page<Clazz> page, String gradeName, String name) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.eq("grade_name", gradeName);
        }
        queryWrapper.orderByDesc("id");
        return baseMapper.selectPage(page, queryWrapper);
    }
}
