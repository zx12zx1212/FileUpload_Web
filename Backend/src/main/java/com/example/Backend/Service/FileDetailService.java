package com.example.Backend.Service;


import com.example.Backend.Entity.Dto.*;
import com.example.Backend.Entity.FileDetail;

import java.util.List;

public interface FileDetailService {
    FileDetail createFileDetail(FileDetailDto fileDetailDto);

    List<GetAllFilesDto> getAllFiles(String empId);

    GetPageAllFilesDto getPageAllFiles(String empId, int page, int size);

    List<GetFilePermissionDto> getAllPermission(String fileNumber);

    void modifyFilePermissions(ModifyFileDetailDto dto);

    void deleteFileDetail(String fileId);
}
