package com.example.Backend.Service.impl;

import com.example.Backend.Entity.Dto.FileDetailDto;
import com.example.Backend.Entity.Dto.FilesDto;
import com.example.Backend.Entity.Dto.ReuploadFileDto;
import com.example.Backend.Entity.Dto.UploadFilesDto;
import com.example.Backend.Entity.Files;
import com.example.Backend.Enum.FileStatusEnum;
import com.example.Backend.Repository.FilesRepository;
import com.example.Backend.Service.EmployeeService;
import com.example.Backend.Service.FileDetailService;
import com.example.Backend.Service.FilesService;
import com.example.Backend.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FilesServiceimpl implements FilesService {

    @Autowired
    private FilesRepository filesRepository;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private FileDetailService fileDetailService;
    @Autowired
    private UserService userService;

    @Override
    public Files createFiles(FilesDto filesDto) {
        Files entity = Files.builder()
                .id(filesDto.getId())
                .folder(filesDto.getFolder())
                .fileName(filesDto.getFileName())
                .filePath(filesDto.getFilePath()).build();
        return filesRepository.save(entity);
    }

    @Override
    public Files searchFiles(String fileNumber) {
        Optional<Files> files = filesRepository.findById(fileNumber);
        if (files.isPresent()) {
            Files result;
            result = files.get();
            return result;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File with ID " + fileNumber + " not found");
    }

    @Override
    public File createFolder(String folderName) {
        String targetFolderPath = Paths.get("uploadFiles", folderName).toString();
        File folder = new File(targetFolderPath);
        if (!folder.isDirectory()) {
            boolean created = folder.mkdir();
        }
        return folder;
    }

    @Override
    public String getFileName(String fileNumber) {
        Optional<Files> result = filesRepository.findById(fileNumber);
        if (result.isPresent()) {
            Files files;
            files = result.get();
            return files.getFileName();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File with ID " + fileNumber + " not found");
    }

    @Override
    public void saveFile(String filePath, MultipartFile file) throws IOException {
        file.transferTo(new File(filePath));
    }

    @Override
    public void deleteFile(String filePath) {
        File file = new File(filePath);
        boolean deleted = file.delete();
    }

    @Override
    public void uploadFile(UploadFilesDto uploadFilesDto) throws IOException {
        // 取得目前登入者
//        String user = userService.getUserName();
        String user = uploadFilesDto.getCreator();
        // 生成存放資料夾名稱
        String folderName = UUID.randomUUID().toString();
        File folder = createFolder(folderName);
        for (MultipartFile file : uploadFilesDto.getFiles()) {
            // 生成流水編號
            String filesId = UUID.randomUUID().toString();
            String targetFilePath = Paths.get(folder.getAbsolutePath(), file.getOriginalFilename()).toString();
            saveFile(targetFilePath, file);
            FilesDto filesDto = FilesDto.builder()
                    .id(filesId)
                    .folder(folderName)
                    .fileName(file.getOriginalFilename())
                    .filePath(folder.getPath()).build();
            createFiles(filesDto);
            // 自己本人要擁有所有權限
//            String createorDepId = employeeService.searchEmployeeDepId(user);
            String createorDepId = employeeService.searchEmployeeDepId(uploadFilesDto.getCreator());
            FileDetailDto fileDetailDto = FileDetailDto.builder()
                    .id(filesId)
                    .creatorDep(createorDepId)
                    .creator(uploadFilesDto.getCreator())
                    .sharerDep(createorDepId)
                    .sharer(user)
                    .status(FileStatusEnum.CREATE)
                    .modifyFile(true)
                    .modifyPermission(true)
                    .deletePermission(true).build();
            fileDetailService.createFileDetail(fileDetailDto);
            for (String sharer : uploadFilesDto.getSharer()) {
                // 存入 fileDetail 資料庫
                String sharerDepId = employeeService.searchEmployeeDepId(sharer);
                FileDetailDto dto = FileDetailDto.builder()
                        .id(filesId)
                        .creatorDep(createorDepId)
                        .creator(uploadFilesDto.getCreator())
                        .sharerDep(sharerDepId)
                        .sharer(sharer)
                        .status(FileStatusEnum.CREATE)
                        .modifyFile(uploadFilesDto.getModifyFile())
                        .modifyPermission(uploadFilesDto.getModifyPermission())
                        .deletePermission(uploadFilesDto.getDeletePermission()).build();
                fileDetailService.createFileDetail(dto);
            }
        }
    }

    @Override
    public void downloadFile(String fileName, String filePath, HttpServletResponse response) throws IOException {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        InputStream inputStream = new FileInputStream(Paths.get(filePath, fileName).toString());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

    @Override
    public void modifyFile(ReuploadFileDto reuploadFileDto) {
        Files files = searchFiles(reuploadFileDto.getFileNumber());
        for (MultipartFile file : reuploadFileDto.getFiles()) {
            String targetFilePath = Paths.get(files.getFilePath(), file.getOriginalFilename()).toAbsolutePath().toString();
            try {
                deleteFile(Paths.get(files.getFilePath(), files.getFileName()).toAbsolutePath().toString());
                saveFile(targetFilePath, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Files entity = Files.builder()
                    .id(files.getId())
                    .folder(files.getFolder())
                    .fileName(file.getOriginalFilename())
                    .filePath(files.getFilePath())
                    .createDate(files.getCreateDate()).build();
            filesRepository.save(entity);
        }
    }

    @Override
    public void deleteFiles(String fileId) {
        filesRepository.delete(filesRepository.findById(fileId).get());
    }

    @Override
    public void testTransactional() {
        Files user = Files.builder()
                .id("test")
                .folder("test")
                .fileName("test")
                .filePath("test")
                .build();
        filesRepository.save(user);
        throw new RuntimeException("我是異常");

    }

}
