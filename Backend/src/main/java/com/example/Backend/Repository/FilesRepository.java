package com.example.Backend.Repository;

import com.example.Backend.Entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<Files,String> {
}
