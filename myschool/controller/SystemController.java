package com.project.myschool.controller;

import com.project.myschool.pojo.Admin;
import com.project.myschool.pojo.LoginForm;
import com.project.myschool.pojo.Student;
import com.project.myschool.pojo.Teacher;
import com.project.myschool.service.AdminService;
import com.project.myschool.service.StudentService;
import com.project.myschool.service.TeacherService;
import com.project.myschool.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "System Controller")
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("Update password")
    @PostMapping("/updatePwd/{oldPassword}/{newPassword}")
    public Result updatePwd(
            @ApiParam("Old password") @PathVariable String oldPassword,
            @ApiParam("New password") @PathVariable String newPassword,
            @ApiParam("Token") @RequestHeader("token") String token
    ) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        switch (userType){
            case 1:
                Admin admin = adminService.getById(userId);
                if (!StringUtils.equals(admin.getPassword(), MD5.encrypt(oldPassword))){
                    return Result.fail().message("Wrong old password!");
                }
                admin.setPassword(MD5.encrypt(newPassword));
                adminService.updateById(admin);
                return Result.ok();
            case 2:
                Student student = studentService.getById(userId);
                if (!StringUtils.equals(student.getPassword(), MD5.encrypt(oldPassword))){
                    return Result.fail().message("Wrong old password!");
                }
                student.setPassword(MD5.encrypt(newPassword));
                studentService.updateById(student);
                return Result.ok();
            case 3:
                Teacher teacher = teacherService.getById(userId);
                if (!StringUtils.equals(teacher.getPassword(), MD5.encrypt(oldPassword))){
                    return Result.fail().message("Wrong old password!");
                }
                teacher.setPassword(MD5.encrypt(newPassword));
                teacherService.updateById(teacher);
                return Result.ok();
        }

        return Result.fail().message("Can't get user type.");
    }

    @ApiOperation("Get user information by token")
    @GetMapping("/getInfo")
    public Result getInfoByToken(
            @ApiParam("Token") @RequestHeader("token") String token
    ) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String, Object> map = new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getById(userId);
                map.put("userType", 1);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getById(userId);
                map.put("userType", 2);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getById(userId);
                map.put("userType", 3);
                map.put("user", teacher);
                break;
        }
        return Result.ok(map);
    }

    @ApiOperation("Log in (return token)")
    @PostMapping("/login")
    public Result login(
            @ApiParam("Log in information") @RequestBody LoginForm loginForm,
            @ApiParam("Http Servlet Request") HttpServletRequest request
    ) {
        // check verify code
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String)session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode){
            return Result.fail().message("Invalid Verify Code. Please Refresh.");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)){
            return Result.fail().message("Wrong Verify Code!");
        }
        //remove verify code from session
        session.removeAttribute("verifiCode");

        Map<String, Object> map = new LinkedHashMap<>();
        // switch user type
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
                    }else {
                        throw new RuntimeException("Wrong Username Or Password!");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
                    }else {
                        throw new RuntimeException("Wrong Username Or Password!");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher) {
                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
                    }else {
                        throw new RuntimeException("Wrong Username Or Password!");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("Wrong User Type!");
    }

    @ApiOperation("Get verify code image")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(
            @ApiParam("Http Servlet Request") HttpServletRequest request,
            @ApiParam("Http Servlet Response") HttpServletResponse response
    ){
        // get Image
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        // get verify code
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        // put code text into session
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);
        // response image
        try {
            ImageIO.write(verifiCodeImage, "jpeg", response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation("Upload image")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("MultipartFile") @RequestPart("multipartFile") MultipartFile multipartFile
    ){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String fileName = uuid + originalFilename.substring(i);
        String portraitPath = "C:\\Users\\bhcao\\IdeaProjects\\mySchool\\target\\classes\\public\\upload\\" + fileName;
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = "upload/" + fileName;
        return Result.ok(path);
    }

}
