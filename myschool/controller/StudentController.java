package com.project.myschool.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.myschool.pojo.Student;
import com.project.myschool.service.StudentService;
import com.project.myschool.util.MD5;
import com.project.myschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Student Controller")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("Delete student(s) by Id")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @ApiParam("Id of students to be deleted") @RequestBody List<Integer> ids
    ){
        studentService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("Add or update student information")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("Student Info") @RequestBody Student student
    ){
        Integer id = student.getId();
        if (null == id || 0 == id) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @ApiOperation("Get students")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("Page Number") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("Page Size") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("Student name and/or clazz name") Student student
    ) {
        Page<Student> page = new Page<>(pageNo, pageSize);
        IPage<Student> pageRs = studentService.getStudentsByOpr(page, student);

        return Result.ok(pageRs);
    }
}
