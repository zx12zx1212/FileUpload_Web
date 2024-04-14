package com.example.Backend.Entity.Dto;

import com.example.Backend.Enum.FileStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TBFileDetailDto {
    private Integer id;
    private String fileNumber;
    private String creatorDep;
    private String creator;
    private String sharerDep;
    private String sharer;
    private FileStatusEnum status;
    private Boolean modifyFile;
    private Boolean modifyPermission;
    private Boolean deletePermission;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
