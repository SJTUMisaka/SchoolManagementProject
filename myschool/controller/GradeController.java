package com.project.myschool.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.myschool.pojo.Grade;
import com.project.myschool.service.GradeService;
import com.project.myschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Grade Controller")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("Delete grade info")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("JSON of id of grades to be deleted") @RequestBody List<Integer> ids
    ){
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("Add or update grade info")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("JSON of Grade") @RequestBody Grade grade
    ){
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("Get grade pages")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
        @ApiParam("Page Number") @PathVariable("pageNo") Integer pageNo,
        @ApiParam("Page Size") @PathVariable("pageSize") Integer pageSize,
        @ApiParam("Grade Name") String gradeName
    ) {
        Page<Grade> page = new Page<>(pageNo, pageSize);
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page, gradeName);

        return Result.ok(pageRs);
    }

    @ApiOperation("Get grades")
    @GetMapping("/getGrades")
    public Result getGrades() {
        return Result.ok(gradeService.list());
    }

}
