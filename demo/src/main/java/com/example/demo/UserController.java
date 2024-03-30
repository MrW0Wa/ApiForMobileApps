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

    @GetMapping("/{email}")
    public User getUser(@PathVariable("email") int email){
        for (User user : users){
            if (user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }
    @GetMapping("/all")
    public ArrayList<User> getAllUsers(){
        return users;
    }
    @DeleteMapping("/{email}")
    public String deleteUser(@PathVariable("email") String email){

        for (User i : users){
            if(i.getEmail().equals(email)){
                users.removeIf(user -> (user.getEmail().equals(email)));
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                try {
                    mapper.writeValue(new File(fileName), users);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                return "user " + email + " have been deleted";

            }
        }
        return "failed";
    }

    @DeleteMapping("/all")
    public String deleteAllUsers(){

        users.clear();
        return "success";
    }

    @PostMapping("/add")
    public String addUser(@RequestBody User user){


        for(User i : users){

            if(i.getPhoneNumber().equals(user.getPhoneNumber())){
                return "the user with the same phone number already exists";
            }
            if(i.getEmail().equals(user.getEmail())){
                return "user with the same Email already exists";
            }
        }

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

    @PostMapping("/{email}")
    public String changePassword(@PathVariable("email")int email, @RequestBody String newpassword){
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).getEmail().equals(email)){
                users.get(i).setPassword(newpassword);
                return "success";
            }
        }
        return "failed";
    }
}
