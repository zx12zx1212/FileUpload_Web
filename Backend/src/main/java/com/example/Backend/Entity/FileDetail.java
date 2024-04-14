package com.example.Backend.Entity;

import com.example.Backend.Enum.FileStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "file_detail")
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "file_number")
    private String fileNumber;

    @Column(name = "creator_dep")
    private String creatorDep;

    @CreatedBy
    @Column(name = "creator")
    private String creator;

    @Column(name = "sharer_dep")
    private String sharerDep;

    @Column(name = "sharer")
    private String sharer;

    @Column(name = "status")
    private FileStatusEnum status;

    @Column(name = "modify_file")
    private Boolean modifyFile;

    @Column(name = "modify_permission")
    private Boolean modifyPermission;

    @Column(name = "delete_permission")
    private Boolean deletePermission;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private Files files;
}
