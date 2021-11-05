package com.duonghn.springrolejwt.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.duonghn.springrolejwt.dao.BookDao;
import com.duonghn.springrolejwt.model.Book;
import com.duonghn.springrolejwt.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "bookService")
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> book = bookDao.findBookByTitle(title);
        return book;
    }

    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        bookDao.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Optional<Book> findById(long id) {
        return bookDao.findById(id);
    }
}
