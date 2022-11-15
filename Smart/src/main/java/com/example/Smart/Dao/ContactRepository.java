package com.example.Smart.Dao;

import com.example.Smart.Entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Query("from Contact as c where c.user.id =:userId")
    Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pageable);

    List<Contact> findByNameOrPhone(String name, String phone);

    @Transactional
    @Modifying
    @Query("delete from Contact c WHERE c.c_id = ?1")
    void deleteByContactId(int c_id);

}
