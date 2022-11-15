package vttp.nus.VTTP_Project_Two.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp.nus.VTTP_Project_Two.models.EmailDetails;
import vttp.nus.VTTP_Project_Two.models.User;
import vttp.nus.VTTP_Project_Two.repositories.UserRepository;
import vttp.nus.VTTP_Project_Two.services.EmailService;

@RestController
@RequestMapping(path = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailSvc;

    @PostMapping(path = "/verify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> userLogin(@RequestBody String payload){

        InputStream is = new ByteArrayInputStream (payload.getBytes()); 	// Create inputstream
        JsonReader reader = Json.createReader(is);							// Create reader for inputstream
        JsonObject data = reader.readObject();								// Convert string to JSON object

        User user = new User();
        user.setEmail(data.getString("email"));
        user.setPassword(data.getString("password"));

        JsonObjectBuilder respObjBuilder = Json.createObjectBuilder();
        if (userRepo.verifyUser(user)){
            respObjBuilder.add("status", true);
            respObjBuilder.add("admin", userRepo.getUserRole(user));
            return ResponseEntity.ok(respObjBuilder.build().toString());
        }else{
            respObjBuilder.add("status", false);
            return ResponseEntity.ok(respObjBuilder.build().toString());
        }
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAccount(@RequestBody String payload){

        InputStream is = new ByteArrayInputStream (payload.getBytes()); 	// Create inputstream
        JsonReader reader = Json.createReader(is);							// Create reader for inputstream
        JsonObject data = reader.readObject();								// Convert string to JSON object

        User user = new User();
        user.setEmail(data.getString("email"));
        user.setPassword(data.getString("password"));

        JsonObjectBuilder respObjBuilder = Json.createObjectBuilder();
        if(userRepo.checkIfUserExists(user)== 0){
            if(userRepo.createUser(user)){

                EmailDetails emailDetail = new EmailDetails();
                emailDetail.setRecipient(user.getEmail());
                emailDetail.setSubject("Welcome to Elib");
                emailDetail.setMsgBody(
                "Hi,\n\n" + 
                "Welcome to Elib. We hope you enjoy your time with us.\n\n" +
                "Happy Reading!\n" +
                "-Elib"
                );
                
                System.out.println(">>>>>"+ emailSvc.sendSimpleMail(emailDetail));

                respObjBuilder.add("status", "User successfully created");
                return ResponseEntity.ok(respObjBuilder.build().toString());
            }else{
                respObjBuilder.add("status", "Error in creating user");
                return ResponseEntity.ok(respObjBuilder.build().toString());
            }
        }else{
            respObjBuilder.add("status", "User already exist");
            return ResponseEntity.ok(respObjBuilder.build().toString());
        }
    }
    
}
