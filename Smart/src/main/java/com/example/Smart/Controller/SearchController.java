package com.example.Smart.Controller;

import com.example.Smart.Dao.ContactRepository;
import com.example.Smart.Dao.UserRepository;
import com.example.Smart.Entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/search/{name}/{phone}")
    public ResponseEntity<?> search(@PathVariable("name") String name,@PathVariable("phone") String phone) {
        List<Contact> contactList = this.contactRepository.findByNameOrPhone(name, phone);
        return ResponseEntity.ok(contactList);
    }
}
