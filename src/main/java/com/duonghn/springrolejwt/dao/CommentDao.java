package com.duonghn.springrolejwt.dao;

import com.duonghn.springrolejwt.model.Comment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends CrudRepository<Comment, Long> {
    // @Query(value = "delete FROM comment where id = ?1", nativeQuery = true)
    // void deleteComment(@Param("id") long id);
}
