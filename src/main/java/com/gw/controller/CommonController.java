package com.gw.controller;

import com.gw.common.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/17 9:43
 * @Version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")  //通过yml文件配置获取路径
    private String basePath;

    /**
     * 本质上是将图片从服务器的缓存中拿出来进行保存
     * @param multipartFile
     * @return
     */
    @PostMapping("/upload")
    public Status<String> upload(@RequestParam("file") MultipartFile multipartFile){

        /**
         * 获取原始的文件后缀
         */
        log.info(multipartFile.toString());
        String filename = multipartFile.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));

        /**
         * 给上传的文件取新的名字
         */
        String uuID = UUID.randomUUID().toString();
        filename = uuID + suffix;

        /**
         * 设置文件的存储路径、并进行存储
         */
        File dir = new File(basePath);

        if(!dir.exists()){
            dir.mkdirs();  //如果没有对应的文件夹则进行创建
        }


        try {
            multipartFile.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Status.success(filename);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, @RequestParam("name") String filename){

        response.setContentType("image/jpeg");

        try {
            //输入流，通过输入流读取文件
            FileInputStream inputStream = new FileInputStream(new File(basePath + filename));


            //输出流，通过输出流将文件写回给浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024];

            while((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
