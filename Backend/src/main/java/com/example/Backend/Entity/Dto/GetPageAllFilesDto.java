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
public class GetPageAllFilesDto {
    private Integer pageNowIndex;
    private Integer pageNowSize;
    private Integer pageTotalSize;
    private List<GetAllFilesDto> getAllFilesDtos;
}
