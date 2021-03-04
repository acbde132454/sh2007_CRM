package com.bjpowernode.crm.base.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

public class FileUploadUtil {

    public static String fileUpload(MultipartFile img, HttpServletRequest request) throws IOException {
        //上传的目录
        ServletContext servletContext = request.getSession().getServletContext();
        String realPath = servletContext.getRealPath("/upload");

        File file = new File(realPath);
        if(!file.exists()){
            file.mkdirs();
        }

        //解决不同用户上传的文件名可能相同   ...jpg png   121212231321...jpg
        String filename = img.getOriginalFilename();
        filename = System.currentTimeMillis() + filename;
        String path = realPath + File.separatorChar + filename;
        //realPath + "/" + 文件名
        img.transferTo(new File(path));
        // /项目名/upload/dfdfdf.jpg
        String savePath = request.getContextPath() + File.separator + "upload" + File.separator + filename;
       return savePath;
    }
}
