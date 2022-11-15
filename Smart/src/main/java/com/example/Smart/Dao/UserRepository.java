package com.example.Smart.Dao;

import com.example.Smart.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.email = :email")
    User getUserByUserName(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("delete from User c WHERE c.id = ?1")
    void deleteById(int id);
}
