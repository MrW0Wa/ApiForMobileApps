package com.example.demo;

import com.example.demo.data.User;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final String fileName = "users.json";
    private ArrayList<User> users = new ArrayList<User>();

   public UserController(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory f = new JsonFactory();
            List<User> listUsers = null;
            JsonParser jp = f.createJsonParser(new File(fileName));
            TypeReference<List<User>> tRef = new TypeReference<List<User>>() {
            };
            listUsers = mapper.readValue(jp,tRef);
            users.addAll(listUsers);
        }
        catch (IOException e){
            e.printStackTrace();
        }
   }

    @GetMapping("/{userid}")
    public User getUser(@PathVariable("userid") int userid){
        for (User user : users){
            if (user.getId() == userid){
                return user;
            }
        }
        return null;
    }
    @GetMapping("/all")
    public ArrayList<User> getAllUsers(){
        return users;
    }
    @DeleteMapping("/{userid}")
    public String geleteUser(@PathVariable("userid") int userid){
        users.removeIf(user -> (user.getId() == userid));
        return "user " + userid + " have been deleted";
    }

    @PostMapping("/add")
    public String addUser(@RequestBody User user){
        users.add(user);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(new File(fileName), users);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return "user " + user.getName() + " have been added";
    }

    @PostMapping("/{userid}")
    public String changePassword(@PathVariable("userid")int userid, @RequestBody String newpassword){
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).getId() == userid){
                users.get(i).setPassword(newpassword);
                return "succes";
            }
        }
        return "failed";
    }
}
