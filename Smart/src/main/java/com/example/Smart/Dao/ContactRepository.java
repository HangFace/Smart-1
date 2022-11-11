package com.example.Smart.Dao;

import com.example.Smart.Entity.Contact;
import com.example.Smart.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Query("from Contact as c where c.user.id =:userId")
     Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pageable);

     List<Contact> findByNameOrPhone(String name, String phone);

}
