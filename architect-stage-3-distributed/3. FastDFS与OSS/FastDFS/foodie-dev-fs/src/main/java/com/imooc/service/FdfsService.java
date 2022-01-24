package com.imooc.service;

import org.springframework.web.multipart.MultipartFile;

public interface FdfsService {
    public String upload(MultipartFile file, String fileExtName) throws Exception;

    public String uploadOSS(MultipartFile file, String userId, String fileExtName) throws Exception;
}
