package com.example.Backend.Service;

import com.example.Backend.Entity.Dto.FilesDto;
import com.example.Backend.Entity.Dto.ReuploadFileDto;
import com.example.Backend.Entity.Dto.UploadFilesDto;
import com.example.Backend.Entity.Files;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FilesService {
    Files createFiles(FilesDto filesDto);

    Files searchFiles(String fileNumber);

    File createFolder(String folderName);

    String getFileName(String fileNumber);

    void saveFile(String filePath, MultipartFile file) throws IOException;

    void deleteFile(String filePath);

    void uploadFile(UploadFilesDto uploadFilesDto) throws IOException;

    void downloadFile(String fileName, String filePath, HttpServletResponse response) throws IOException;

    void modifyFile(ReuploadFileDto reuploadFileDto);

    void deleteFiles(String fileId);

    void testTransactional();
}
