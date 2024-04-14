package com.example.Backend.Service.impl;

import com.example.Backend.Entity.Department;
import com.example.Backend.Entity.Dto.*;
import com.example.Backend.Entity.Employee;
import com.example.Backend.Entity.FileDetail;
import com.example.Backend.Enum.FileStatusEnum;
import com.example.Backend.Repository.DepartmentRepository;
import com.example.Backend.Repository.EmployeeRepository;
import com.example.Backend.Repository.FileDetailRepository;
import com.example.Backend.Repository.FilesRepository;
import com.example.Backend.Service.FileDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FileDetailServiceimpl implements FileDetailService {

    @Autowired
    private FileDetailRepository fileDetailRepository;

    @Autowired
    private FilesRepository filesRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FileDetail createFileDetail(FileDetailDto fileDetailDto) {
        FileDetail entity = FileDetail.builder()
                .fileNumber(fileDetailDto.getId())
                .creatorDep(fileDetailDto.getCreatorDep())
                .creator(fileDetailDto.getCreator())
                .sharerDep(fileDetailDto.getSharerDep())
                .sharer(fileDetailDto.getSharer())
                .status(fileDetailDto.getStatus())
                .modifyFile(fileDetailDto.getModifyFile())
                .modifyPermission(fileDetailDto.getModifyPermission())
                .deletePermission(fileDetailDto.getDeletePermission()).build();
        return fileDetailRepository.save(entity);
    }

    @Override
    public List<GetAllFilesDto> getAllFiles(String empId) {
        List<FileDetail> empList = fileDetailRepository.findBySharer(empId);
        List<GetAllFilesDto> result = new ArrayList<>();
        int i = 1;
        for (FileDetail emp : empList) {
            Optional<Employee> empDetail = employeeRepository.findById(emp.getSharer());
            GetAllFilesDto entity = GetAllFilesDto.builder()
                    .items(i)
                    .fileId(emp.getFileNumber())
//                    .fileName(emp.getFiles().getFileName())
                    .fileName(filesRepository.getById(emp.getFileNumber()).getFileName())
                    .createDate(emp.getCreateDate().toLocalDate())
                    .sharerId(emp.getSharer())
                    .sharer(empDetail.get().getName())
                    .sharerEmail(empDetail.get().getEmail())
                    .modifyFile(emp.getModifyFile())
                    .modifyPermission(emp.getModifyPermission())
                    .deletePermission(emp.getDeletePermission()).build();
            i++;
            result.add(entity);
        }
        return result;
    }

    @Override
    public GetPageAllFilesDto getPageAllFiles(String empId, int page, int size) {
        Page<FileDetail> result = fileDetailRepository.findBySharer(empId, PageRequest.of(page, size));
        List<GetAllFilesDto> getAllFilesDtoList = new ArrayList<>();
        int i = 1;
        for (FileDetail emp : result.getContent()) {
            Optional<Employee> empDetail = employeeRepository.findById(emp.getSharer());
            GetAllFilesDto entity = GetAllFilesDto.builder()
                    .items(i)
                    .fileId(emp.getFileNumber())
//                    .fileName(emp.getFiles().getFileName())
                    .fileName(filesRepository.getById(emp.getFileNumber()).getFileName())
                    .createDate(emp.getCreateDate().toLocalDate())
                    .sharerId(emp.getSharer())
                    .sharer(empDetail.get().getName())
                    .sharerEmail(empDetail.get().getEmail())
                    .modifyFile(emp.getModifyFile())
                    .modifyPermission(emp.getModifyPermission())
                    .deletePermission(emp.getDeletePermission()).build();
            i++;
            getAllFilesDtoList.add(entity);
        }
        return GetPageAllFilesDto.builder()
                .pageNowIndex(page)
                .pageNowSize(size)
                .pageTotalSize(result.getTotalPages())
                .getAllFilesDtos(getAllFilesDtoList).build();
    }

    @Override
    public List<GetFilePermissionDto> getAllPermission(String fileNumber) {
        List<FileDetail> result = fileDetailRepository.findByFileNumber(fileNumber);
        List<GetFilePermissionDto> dtoList = new ArrayList<>();
        for (FileDetail data : result) {
            Department depId = departmentRepository.findById(data.getSharerDep()).get();
            Employee sharerName = employeeRepository.findById(data.getSharer()).get();
            Employee creatorName = employeeRepository.findById(data.getCreator()).get();
            GetFilePermissionDto dto = GetFilePermissionDto.builder()
                    .creator(data.getCreator())
                    .creatorName(creatorName.getName())
                    .sharerDepId(depId.getId())
                    .sharerDepName(depId.getName())
                    .sharer(data.getSharer())
                    .sharerName(sharerName.getName())
                    .modifyFile(data.getModifyFile())
                    .modifyPermission(data.getModifyPermission())
                    .deletePermission(data.getDeletePermission()).build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public void modifyFilePermissions(ModifyFileDetailDto dto) {
        List<String> sharerList = dto.getSharer();
        List<FileDetail> fileDetail = fileDetailRepository.findByFileNumber(dto.getFileNumber()).stream().toList();
        for (FileDetail data : fileDetail) {
            if (dto.getSharer().contains(data.getSharer())) {
                // 修改資料
                TBFileDetailDto entity = TBFileDetailDto.builder()
                        .id(data.getId())
                        .fileNumber(dto.getFileNumber())
                        .creatorDep(data.getCreatorDep())
                        .creator(data.getCreator())
                        .sharerDep(data.getSharerDep())
                        .sharer(data.getSharer())
                        .status(FileStatusEnum.MODIFY)
                        .createDate(data.getCreateDate())
                        .build();
                if (data.getCreator().equals(data.getSharer())) {
                    entity.setModifyFile(data.getModifyFile());
                    entity.setModifyPermission(data.getModifyPermission());
                    entity.setDeletePermission(data.getDeletePermission());
                } else {
                    entity.setModifyFile(dto.getModifyFile());
                    entity.setModifyPermission(dto.getModifyPermission());
                    entity.setDeletePermission(dto.getDeletePermission());
                }
                fileDetailRepository.save(modelMapper.map(entity, FileDetail.class));
                sharerList.removeIf(value -> value.equals(data.getSharer()));
            } else {
                // 刪除資料
                fileDetailRepository.delete(data);
            }
        }
        // 新增資料
        if (!sharerList.isEmpty()) {
            for (String sharer : sharerList) {
                TBFileDetailDto entity = TBFileDetailDto.builder()
                        .fileNumber(dto.getFileNumber())
                        .creatorDep(fileDetail.getFirst().getCreatorDep())
                        .creator(fileDetail.getFirst().getCreator())
                        .sharerDep(employeeRepository.findById(sharer).get().getDepartmentId())
                        .sharer(sharer)
                        .status(FileStatusEnum.CREATE)
                        .modifyFile(dto.getModifyFile())
                        .modifyPermission(dto.getModifyPermission())
                        .deletePermission(dto.getDeletePermission())
                        .build();
                fileDetailRepository.save(modelMapper.map(entity, FileDetail.class));
            }
        }
    }

    @Override
    public void deleteFileDetail(String fileNumber) {
        List<FileDetail> result = fileDetailRepository.findByFileNumber(fileNumber);
        for (FileDetail r : result) {
            fileDetailRepository.delete(r);
        }
    }

}
