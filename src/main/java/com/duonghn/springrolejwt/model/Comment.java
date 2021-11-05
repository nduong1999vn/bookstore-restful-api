package com.duonghn.springrolejwt.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column
    private @NotNull String comment;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "COMMENT_USERS",
            joinColumns = {
            @JoinColumn(name = "COMMENT_ID")
            },
            inverseJoinColumns = {
            @JoinColumn(name = "USER_ID") })
    private User user;

    @ManyToOne(cascade = CascadeType.DETACH , fetch = FetchType.EAGER)
    @JoinTable(name = "COMMENT_BOOKS",
            joinColumns = {
            @JoinColumn(name = "COMMENT_ID")
            },
            inverseJoinColumns = {
            @JoinColumn(name = "BOOK_ID") })
    @JsonIgnore
    private Book book;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @PreRemove
    public void preRemove() {
        this.book.getComments().clear(); //of course this is just an example and probably you should have more complicated logic here
        this.book = null;
        this.user.getComments().clear(); //of course this is just an example and probably you should have more complicated logic here
        this.user = null;
    }
}
