package com.example.Backend.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllFilesDto {
    private Integer items;
    private String fileId;
    private String fileName;
    private LocalDate createDate;
    private String sharerId;
    private String sharer;
    private String sharerEmail;
    private Boolean modifyFile;
    private Boolean modifyPermission;
    private Boolean deletePermission;
}
