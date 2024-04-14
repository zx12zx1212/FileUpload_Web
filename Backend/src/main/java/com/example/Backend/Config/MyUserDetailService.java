//package com.example.Backend.Config;
//
//import com.example.Backend.Entity.EmployeeAccount;
//import com.example.Backend.Repository.EmployeeAccountRepositry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class MyUserDetailService implements UserDetailsService {
//
//    @Autowired
//    private EmployeeAccountRepositry employeeAccountRepositry;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<EmployeeAccount> appUser = employeeAccountRepositry.findById(username);
//        if (!appUser.isPresent()) {
//            throw new UsernameNotFoundException("Can't find user: " + username);
//        }
//        String password = new BCryptPasswordEncoder().encode(appUser.get().getPassword());
//        return new User(appUser.get().getAccount(), password, List.of());
//    }
//}
