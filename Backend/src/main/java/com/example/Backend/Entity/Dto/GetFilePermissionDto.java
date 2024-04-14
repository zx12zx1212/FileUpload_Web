package com.example.Backend.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFilePermissionDto {
    private String creator;
    private String creatorName;
    private String sharerDepId;
    private String sharerDepName;
    private String sharer;
    private String sharerName;
    private Boolean modifyFile;
    private Boolean modifyPermission;
    private Boolean deletePermission;
}
