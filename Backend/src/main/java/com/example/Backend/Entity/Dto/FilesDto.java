package com.example.Backend.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilesDto {
    private String id;
    private String folder;
    private String fileName;
    private String filePath;
}
