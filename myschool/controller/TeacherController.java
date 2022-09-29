package com.project.myschool.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.myschool.pojo.Teacher;
import com.project.myschool.service.TeacherService;
import com.project.myschool.util.MD5;
import com.project.myschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Teacher Controller")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("Delete teacher(s) by id")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @ApiParam("Id of teacher to be deleted") @RequestBody List<Integer> ids
    ){
        teacherService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("Add or update teacher information")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @ApiParam("Teacher information") @RequestBody Teacher teacher
    ){
        Integer id = teacher.getId();
        if (null == id || 0 == id) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @ApiOperation("Get teachers")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("Page number") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("Page size") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("Teacher name and/or clazz name") Teacher teacher
    ) {
        Page<Teacher> page = new Page<>(pageNo, pageSize);
        IPage<Teacher> pageRs = teacherService.getTeachersByOpr(page, teacher);

        return Result.ok(pageRs);
    }
}
