package com.duonghn.springrolejwt.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import com.duonghn.springrolejwt.dao.CommentDao;
import com.duonghn.springrolejwt.model.Book;
import com.duonghn.springrolejwt.model.Comment;
import com.duonghn.springrolejwt.model.CommentDto;
import com.duonghn.springrolejwt.model.User;
import com.duonghn.springrolejwt.service.BookService;
import com.duonghn.springrolejwt.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping({"/books"})
public class BookController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private BookService bookRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CommentDao commentDao;

    @GetMapping(produces = "application/json")
    public @ResponseBody Iterable<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @GetMapping(path="/{id}")
    public @ResponseBody Optional<Book> getBookById(@PathVariable("id") Long id) {
      // This returns a JSON or XML with the book
        return bookRepository.findById(id);
    }

    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    @PostMapping(path="/{id}/comment")
    public Comment postNewComment(@ModelAttribute CommentDto comment, @PathVariable("id") Long id, Authentication authentication) {
      Comment nComment = comment.getCmtFromDto();
  
      String name = authentication.getName();
      System.out.println(name);
      User user = userService.findOne(name);
      nComment.setUser(user);

      Optional<Book> oBook = bookRepository.findById(id);
      Book book = oBook.get();
      nComment.setBook(book);

      return commentDao.save(nComment);
    }

    @GetMapping(path="/comment")
    public @ResponseBody Iterable<Comment> getComment() {
      // This returns a JSON or XML with the book
        return commentDao.findAll();
    }

    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    @DeleteMapping(path="/comment/{id}/{name}")
    public void deleteComment(@PathVariable("id") Long id, @PathVariable("name") String name, HttpServletResponse response) {
      // This returns a JSON or XML with the book
      User user = currentUser();
      String usrname = user.getUsername();
      if (usrname.equals(name) || user.getRoles().getName().equals("ADMIN")) {
        commentDao.deleteById(id);
      } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    }

    private User currentUser() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();;
      String name = authentication.getName();
      return userService.findOne(name);
    }

    @GetMapping(path="comment/{id}")
    public @ResponseBody Optional<Comment> getCommentById(@PathVariable("id") Long id) {
      // This returns a JSON or XML with the book
        return commentDao.findById(id);
    }

    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    @GetMapping(path="/search")
    public @ResponseBody List<Book> getBookByTitle(@RequestParam String title) {
      // This returns a JSON or XML with the book
        System.out.println(title);
        return bookRepository.findByTitle(title);
    }
}