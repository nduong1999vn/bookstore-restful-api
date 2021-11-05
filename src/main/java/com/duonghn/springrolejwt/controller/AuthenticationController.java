package com.duonghn.springrolejwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.duonghn.springrolejwt.config.TokenProvider;
import com.duonghn.springrolejwt.model.*;
import com.duonghn.springrolejwt.service.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public User saveUser(@RequestBody UserDto user){
        return userService.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/adminping", method = RequestMethod.GET)
    public String adminPing(){
        return "Only Admins Can Read This";
    }

    @PreAuthorize("hasRole('USER')" + "|| hasRole('ADMIN')")
    @RequestMapping(value="/userping", method = RequestMethod.GET)
    public String userPing(){
        return "Any User Can Read This";
    }

    @RequestMapping(value="/pingmyself", method = RequestMethod.GET)
    @ResponseBody
    public User currentUser(Authentication authentication) {
       String name = authentication.getName();
       return userService.findOne(name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path="/backup/{name}")
    public void backupSystem(@PathVariable("name") String name) throws IOException {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd-mm-yyyy hh-mm-ss");
        df.setTimeZone(TimeZone.getDefault());
        String strDate = df.format(date).replace(" ", "_");
        System.out.println(strDate);
        String cmd = "7z a ../backup/" + name + "_" + strDate + ".zip *";
        try {
            System.out.println(cmd);
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
}
