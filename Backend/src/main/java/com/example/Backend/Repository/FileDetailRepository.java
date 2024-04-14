package com.example.Backend.Repository;

import com.example.Backend.Entity.FileDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDetailRepository extends JpaRepository<FileDetail, String> {
    List<FileDetail> findBySharer(String sharer);

    List<FileDetail> findByFileNumber(String fileNumber);

    Page<FileDetail> findBySharer(String sharer, Pageable pageable);
}
