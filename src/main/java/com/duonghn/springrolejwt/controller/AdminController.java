package com.duonghn.springrolejwt.controller;

import com.duonghn.springrolejwt.model.Role;
import com.duonghn.springrolejwt.model.User;
import com.duonghn.springrolejwt.model.UserDto;
import com.duonghn.springrolejwt.service.RoleService;
import com.duonghn.springrolejwt.service.UserService;
import com.duonghn.springrolejwt.dao.UserDao;
import com.duonghn.springrolejwt.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping({"/user"})
public class AdminController {
    @Autowired
    private UserDao userRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;
    
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping(produces = "application/json")
    public @ResponseBody Iterable<User> getAllUsers(){
        return userService.findAll();
    }

    @PostMapping(path="/add")
    public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
		return ResponseEntity.ok(userService.save(user));
	}

    @GetMapping(path="/{id}")
    public @ResponseBody User getUserById(@PathVariable("id") Long id) {
      // This returns a JSON or XML with the book
        return userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping(path="/name/{name}")
    public @ResponseBody User getUserByName(@PathVariable("name") String name) {
      // This returns a JSON or XML with the book
        return userService.findOne(name);
    }

    @DeleteMapping(path="/{id}")
    public @ResponseBody String deleteUserById(@PathVariable("id") Long id) {
      // This returns a JSON or XML with the book
        return userService.deleteById(id);
    }

    @PutMapping(path="/edit/{id}")
    public User updateUser(@ModelAttribute UserDto userdto, @PathVariable("id") Long id) {
      Role role = roleService.findByName(userdto.getRole());
      String username = userdto.getUsername();
      String password = userdto.getPassword();
      
      return userService.findById(id)
      .map(user -> {
        if (!"".equals(username)){
          System.out.println(username);
          user.setUsername(userdto.getUsername());
        }
        if (!"".equals(password)){
          System.out.println(password);
          user.setPassword(bcryptEncoder.encode(userdto.getPassword()));
        }
        user.setRoles(role);
        return userRepository.save(user);
      }).orElseGet(() -> {
        return userService.save(userdto);
      });
    }


} 