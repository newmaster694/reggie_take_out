package com.newmaster.reggie.controller;

import com.newmaster.reggie.common.CustomException;
import com.newmaster.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传/下载处理
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        // file是一个临时文件,需要转存到指定位置,否则本次请求完成后临时文件会删除
        log.info(file.toString());

        String originalFilename = file.getOriginalFilename();
        String suffixName = null;
        if (originalFilename != null) {
            suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        if (!(file.getOriginalFilename().endsWith(".jpg") || file.getOriginalFilename().endsWith(".png"))) {
            throw new CustomException("导入文件格式错误，请导入jpg/png格式的文件！");
        }
        String uuid = UUID.randomUUID().toString();
        String FileName = uuid + suffixName;
        file.transferTo(new File(FileName));
        return R.success(FileName);
    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        // 输入流,通过输入流读取文件内容
        String filePath = "/www/web_application/reggie/image/";
        FileInputStream fileInputStream = new FileInputStream(new File(filePath + name));

        // 输出流,通过输出流将文件写回浏览器,在浏览器里展示图片
        ServletOutputStream outputStream = response.getOutputStream();

        response.setContentType("image/jpeg");

        int len;
        byte[] bytes = new byte[1024];
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }

        outputStream.close();
        fileInputStream.close();
    }

}
