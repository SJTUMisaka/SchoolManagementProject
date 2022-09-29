package com.project.myschool.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.myschool.pojo.Clazz;
import com.project.myschool.service.ClazzService;
import com.project.myschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Clazz Controller")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @ApiOperation("Delete clazz(s) by Id")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
            @ApiParam("Id of clazz to be deleted") @RequestBody List<Integer> ids
    ){
        clazzService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("Add or update clazz")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("Clazz information") @RequestBody Clazz clazz
    ){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("Get clazzs")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(
            @ApiParam("Page number") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("Page size") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("Grade name") String gradeName,
            @ApiParam("Clazz name") String name
    ){
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        IPage<Clazz> pageRs = clazzService.getClazzsByOpr(page, gradeName, name);

        return Result.ok(pageRs);
    }

    @ApiOperation("Get all clazzs")
    @GetMapping("/getClazzs")
    public Result getClazzs() {
        return Result.ok(clazzService.list());
    }
}
