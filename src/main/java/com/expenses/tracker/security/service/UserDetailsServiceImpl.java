package com.expenses.tracker.security.service;

import com.expenses.tracker.model.UserModel;
import com.expenses.tracker.repository.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {

        Optional<UserModel> userDetail = repository.findByPhoneNumber(phoneNumber);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("PhoneNumber not found " + phoneNumber));
    }
}
