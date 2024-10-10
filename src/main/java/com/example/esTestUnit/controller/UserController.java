package com.example.esTestUnit.controller;

import com.example.esTestUnit.entities.User;
import com.example.esTestUnit.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices userService;

    @PostMapping("/add")
    public ResponseEntity<User> create(@RequestBody User user){
        User createdUser = userService.create(user);
        return ResponseEntity.ok().body(createdUser);
    }

    @GetMapping("/readAll")
    public ResponseEntity<List<User>> readAll(){
        List<User> allUsers = userService.readAll();
        return  ResponseEntity.ok().body(allUsers);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<User> readById(@PathVariable Long id){
        Optional<User> userOptional = userService.readById(id);
        if (userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(userOptional.get());
        }
    }

    @PutMapping("/change/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOptional = userService.update(id, user);
        if (userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(userOptional.get());
        }
    }

    @PutMapping("/setActive/{id}")
    public ResponseEntity<User> updateWorking(@PathVariable Long id, @RequestParam(name = "active") boolean isActive) {
        Optional<User> userOptional = userService.updateWorking(id, isActive);
        if (userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(userOptional.get());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        boolean wasThere = userService.delete(id);
        if(wasThere){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
