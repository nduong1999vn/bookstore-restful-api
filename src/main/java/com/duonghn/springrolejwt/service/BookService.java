package com.duonghn.springrolejwt.service;

import java.util.List;
import java.util.Optional;

import com.duonghn.springrolejwt.model.Book;

public interface BookService {
    List<Book> findByTitle(String title);
    List<Book> findAll();
    Optional<Book> findById(long id);
}