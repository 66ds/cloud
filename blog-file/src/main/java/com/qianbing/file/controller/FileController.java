package com.qianbing.file.controller;

import com.qianbing.common.Result.R;
import com.qianbing.file.component.OosComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/pri/oos")
public class FileController {

    @Autowired
    private OosComponent oosComponent;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping(value = "/upload")
    public R uploadTeacherImg(@RequestParam("file") MultipartFile file) {
        try {
            return R.ok().setData(oosComponent.upload(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
