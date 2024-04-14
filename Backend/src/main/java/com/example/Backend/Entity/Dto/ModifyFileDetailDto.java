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
public class ModifyFileDetailDto {
    private String fileNumber;
    private List<String> sharer;
    private Boolean modifyFile;
    private Boolean modifyPermission;
    private Boolean deletePermission;
}
