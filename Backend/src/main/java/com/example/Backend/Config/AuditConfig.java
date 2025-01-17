//package com.example.Backend.Config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.util.Optional;
//
//@Configuration
//@EnableJpaAuditing(auditorAwareRef = "getCurrentAuditor")
//public class AuditConfig {
//
//    @Bean
//    public AuditorAware<String> getCurrentAuditor() {
//        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
//    }
//}
