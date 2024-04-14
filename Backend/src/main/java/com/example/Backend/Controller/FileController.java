package com.example.Backend.Controller;

import com.example.Backend.Entity.Dto.GetFilePermissionDto;
import com.example.Backend.Entity.Dto.ModifyFileDetailDto;
import com.example.Backend.Entity.Dto.ReuploadFileDto;
import com.example.Backend.Entity.Dto.UploadFilesDto;
import com.example.Backend.Entity.Files;
import com.example.Backend.Service.FileDetailService;
import com.example.Backend.Service.FilesService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class FileController {
    @Autowired
    private FilesService filesService;
    @Autowired
    private FileDetailService fileDetailService;

    // 取得檔案名稱
    @GetMapping("/getFileName/{fileNumber}")
    ResponseEntity<String> getFileName(@PathVariable("fileNumber") String fileNumber) {
        return ResponseEntity.ok().body(filesService.getFileName(fileNumber));
    }

    // 取得資料表
    @GetMapping("/getFiles/{empId}")
    ResponseEntity<?> getFiles(@PathVariable("empId") String empId) {
        // 只取得分享人
        return ResponseEntity.ok().body(fileDetailService.getAllFiles(empId));
    }

    // 取得資料表
    @GetMapping("/getPageFiles/{empId}")
    ResponseEntity<?> getPageFiles(@PathVariable("empId") String empId, int page, int size) {
        // 只取得分享人
        return ResponseEntity.ok().body(fileDetailService.getPageAllFiles(empId, page, size));
    }

    // 上傳檔案
    @PostMapping("/uploadFiles")
    ResponseEntity<String> uploadFiles(UploadFilesDto uploadFilesDto) {
        try {
            filesService.uploadFile(uploadFilesDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("Success!");
    }

    // 下載檔案
    @GetMapping("/downloadFile/{fileNumber}")
    ResponseEntity<?> downloadFile(@PathVariable("fileNumber") String fileNumber, HttpServletResponse response) {
        Files file_path = filesService.searchFiles(fileNumber);
        try {
            filesService.downloadFile(file_path.getFileName(), file_path.getFilePath(), response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    // 重新上傳檔案
    @PutMapping("/modifyFile")
    ResponseEntity<?> ModifyFile(ReuploadFileDto reuploadFileDto) {
        filesService.modifyFile(reuploadFileDto);
        return ResponseEntity.ok().build();
    }

    // 取得目前檔案所有編輯權限 本人登入 非本人登入
    @PostMapping("/getFileAllFPermission/{fileNumber}")
    ResponseEntity<List<GetFilePermissionDto>> getFileAllFPermission(@PathVariable("fileNumber") String fileNumber) {
        return ResponseEntity.ok().body(fileDetailService.getAllPermission(fileNumber));
    }

    // 編輯權限
    @PutMapping("/modifyPermission")
    ResponseEntity<?> ModifyPermission(ModifyFileDetailDto modifyFileDetailDto) {
        fileDetailService.modifyFilePermissions(modifyFileDetailDto);
        return ResponseEntity.ok().build();
    }

    // 刪除檔案
    @DeleteMapping("/deleteFile/{fileNumber}")
    ResponseEntity<?> deleteFile(@PathVariable("fileNumber") String fileNumber) {
        // 檔案刪除
        Files file_path = filesService.searchFiles(fileNumber);
        filesService.deleteFile(file_path.getFilePath());
        // 資料表刪除
        fileDetailService.deleteFileDetail(fileNumber);
        filesService.deleteFiles(fileNumber);
        return ResponseEntity.ok().build();
    }
}
