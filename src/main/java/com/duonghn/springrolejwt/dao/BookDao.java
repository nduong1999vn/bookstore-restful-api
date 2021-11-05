package com.duonghn.springrolejwt.dao;

import java.util.List;
import java.util.Optional;

import com.duonghn.springrolejwt.model.Book;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDao extends CrudRepository<Book, Long> {
    @Query(value = "SELECT u.* FROM book u WHERE u.title LIKE %:title%", nativeQuery = true)
    List<Book> findBookByTitle(@Param("title") String title);
}
