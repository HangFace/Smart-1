package com.example.Smart.Repository;

import com.example.Smart.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<User, Integer> {
}
