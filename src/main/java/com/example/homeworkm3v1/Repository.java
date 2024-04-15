package com.example.homeworkm3v1;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface Repository {

List <Contact> findAll();

    Optional<Contact> findById(Integer id);

    Contact save(Contact contact);

    Contact update(Contact contact);

    void deleteById(Integer id);
}
