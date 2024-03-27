package com.clever.controller;


import com.clever.annotation.AuthGroup;
import com.clever.bean.model.Result;
import com.clever.config.AppRunConfig;
import com.clever.exception.BaseException;
import com.clever.exception.ConstantException;
import com.clever.util.SpringUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件接口
 *
 * @Author xixi
 * @Date 2024-03-27 17:36:54
 */
@RestController
@Validated
@RequestMapping("/file")
@AuthGroup(value = "clever-shopping.file", name = "文件模块", description = "文件模块权限组")
public class FileController {

    @Resource
    private AppRunConfig appRunConfig;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file, HttpServletRequest request) {
        String realPath = request.getSession().getServletContext().getRealPath("/uploadFile/");
        System.out.println(realPath);
        Path path = Paths.get(appRunConfig.getUploadFileFolder());
        File folder = new File(path.toString());
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        try {
            file.transferTo(new File(folder, newName));

        } catch (IOException e) {
            throw new BaseException(ConstantException.FILE_UPLOAD_FAIL);
        }
        String filePath = "/file/resource/" + newName;
        return new Result<>(filePath, "上传成功");
    }

    @GetMapping("/resource/{path}")
    public void uploadFile(@NotBlank(message = "路径不能为空") @PathVariable("path") String path) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpServletResponse response = SpringUtil.getResponse();
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + path.substring(path.lastIndexOf("/") + 1));
            response.setHeader("Content-Type", "application/octet-stream");
            response.setContentType("application/octet-stream; charset=UTF-8");

            Path filePath = Paths.get(appRunConfig.getUploadFileFolder(), path);
            inputStream = Files.newInputStream(filePath);
            outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }

        } catch (Exception e) {
            throw new BaseException(ConstantException.FILE_VIEW_FAIL);

        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (Exception e) {
                throw new BaseException(ConstantException.FILE_VIEW_FAIL);
            }
        }
    }
}
