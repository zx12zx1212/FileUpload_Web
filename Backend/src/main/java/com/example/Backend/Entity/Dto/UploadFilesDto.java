package com.example.Backend.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadFilesDto {
    private MultipartFile[] files;
    private List<String> sharer;
    private String creator;
    private Boolean modifyFile;
    private Boolean modifyPermission;
    private Boolean deletePermission;
}
