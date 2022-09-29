package com.project.myschool.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.myschool.pojo.Admin;
import com.project.myschool.service.AdminService;
import com.project.myschool.util.MD5;
import com.project.myschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Admin Controller")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("Delete admin(s) ids")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @ApiParam("Id of admin to be deleted") @RequestBody List<Integer> ids
    ) {
        adminService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("Add or update admin")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @ApiParam("Admin information") @RequestBody Admin admin
    ) {
        if (null == admin.getId() || 0 == admin.getId()) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @ApiOperation("Get admins")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("Page number") @PathVariable Integer pageNo,
            @ApiParam("Page size") @PathVariable Integer pageSize,
            @ApiParam("Admin name") String adminName
    ) {
        Page<Admin> page = new Page<>(pageNo, pageSize);
        IPage<Admin> pageRs = adminService.getAdminsByOpr(page, adminName);

        return Result.ok(pageRs);
    }
}
