package com.example.Smart.config;

import com.example.Smart.Dao.UserRepository;
import com.example.Smart.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private  UserRepository userRepository;

//    public UserDetailServiceImpl() {
//        super();
//    }

    //in this method we fetch the user from the database and authenticate in this method
    //we call the data from dao class
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUserName(username);
        if(user==null){
            throw new UsernameNotFoundException("Could Not Found The User..!");
        }
        CustomUserDetail  customUserDetail = new CustomUserDetail(user);

        return customUserDetail;
    }


}
