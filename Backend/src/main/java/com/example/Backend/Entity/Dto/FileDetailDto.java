package com.example.Backend.Entity.Dto;

import com.example.Backend.Entity.Files;
import com.example.Backend.Enum.FileStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDetailDto {
    private String id;
    private String creatorDep;
    private String creator;
    private String sharerDep;
    private String sharer;
    private FileStatusEnum status;
    private Boolean modifyFile;
    private Boolean modifyPermission;
    private Boolean deletePermission;
}
