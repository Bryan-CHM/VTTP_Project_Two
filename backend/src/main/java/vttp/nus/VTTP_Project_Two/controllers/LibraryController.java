package vttp.nus.VTTP_Project_Two.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.nus.VTTP_Project_Two.models.Book;
import vttp.nus.VTTP_Project_Two.models.User;
import vttp.nus.VTTP_Project_Two.repositories.LibraryRepository;
import vttp.nus.VTTP_Project_Two.services.LibraryService;

@RestController
@RequestMapping(path = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class LibraryController {

    @Autowired
    private LibraryService libSvc;

    @Autowired
    private LibraryRepository libRepo;

    @PostMapping(path = "/list/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAdminListOfBooks(@RequestBody String payload){

        InputStream is = new ByteArrayInputStream (payload.getBytes()); 	// Create inputstream
        JsonReader reader = Json.createReader(is);							// Create reader for inputstream
        JsonObject data = reader.readObject();								// Convert string to JSON object

        String query = data.getString("query");
        List<Book> books = libSvc.getListOfBooks(query);

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for(Book b : books){
            arrBuilder.add(b.toJson());
        }
        return ResponseEntity.ok(arrBuilder.build().toString());
        }

    @PostMapping(path = "/list/admin/{index}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAdminNextListOfBooks(@RequestBody String payload, @PathVariable Integer index){

        InputStream is = new ByteArrayInputStream (payload.getBytes()); 	// Create inputstream
        JsonReader reader = Json.createReader(is);							// Create reader for inputstream
        JsonObject data = reader.readObject();								// Convert string to JSON object

        String query = data.getString("query");
        List<Book> books = libSvc.getNextListOfBooks(query, index);

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for(Book b : books){
            arrBuilder.add(b.toJson());
        }
        return ResponseEntity.ok(arrBuilder.build().toString());
        }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addListOfBooks(@RequestBody String payload){

        InputStream is = new ByteArrayInputStream (payload.getBytes()); 	// Create inputstream
        JsonReader reader = Json.createReader(is);							// Create reader for inputstream
        JsonArray data = reader.readArray();								// Convert string to JSON arra

        List<Book> books = new LinkedList<>();
        for(JsonValue v : data){
            JsonObject obj =  v.asJsonObject();
            Book book = Book.createFromRequest(obj);
            books.add(book);
        }
        JsonObjectBuilder respObjBuilder = Json.createObjectBuilder();
        if(libRepo.addBooksToLibrary(books) == books.size()){
            respObjBuilder.add("status", true);
            return ResponseEntity.ok(respObjBuilder.build().toString());
        }else{
            respObjBuilder.add("status", false);
            return ResponseEntity.ok(respObjBuilder.build().toString());
        }
    }

    @PostMapping(path = "/list/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserListOfBooks(@RequestBody String payload){

        InputStream is = new ByteArrayInputStream (payload.getBytes()); 	// Create inputstream
        JsonReader reader = Json.createReader(is);							// Create reader for inputstream
        JsonObject data = reader.readObject();								// Convert string to JSON object

        String query = "%" + data.getString("query") + "%";
        List<Book> books = libRepo.getUserListOfBooks(query);

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for(Book b : books){
            arrBuilder.add(b.toJsonForUser());
        }
        return ResponseEntity.ok(arrBuilder.build().toString());
        }

        @PostMapping(path = "/list/user/{index}", consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getUserNextListOfBooks(@RequestBody String payload, @PathVariable Integer index){
    
            InputStream is = new ByteArrayInputStream (payload.getBytes()); 	// Create inputstream
            JsonReader reader = Json.createReader(is);							// Create reader for inputstream
            JsonObject data = reader.readObject();								// Convert string to JSON object
    
            String query = "%" + data.getString("query") + "%";
            List<Book> books = libRepo.getUserNextListOfBooks(query, index);
    
            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
            for(Book b : books){
                arrBuilder.add(b.toJson());
            }
            return ResponseEntity.ok(arrBuilder.build().toString());
            }

        @PostMapping(path = "/list/user/borrowed", consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> getUserListOfBorrowedBooks(@RequestBody String payload){
    
            InputStream is = new ByteArrayInputStream (payload.getBytes()); 	// Create inputstream
            JsonReader reader = Json.createReader(is);							// Create reader for inputstream
            JsonObject data = reader.readObject();								// Convert string to JSON object
    
            List<Book> books = libRepo.getUserBorrowedBooks(data.getString("email"));
    
            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
            for(Book b : books){
                arrBuilder.add(b.toJsonForUser());
            }
            return ResponseEntity.ok(arrBuilder.build().toString());
            }

        @PostMapping(path = "/add/user", consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> addBorrowedBooks(@RequestBody String payload){
    
            InputStream is = new ByteArrayInputStream (payload.getBytes()); 	// Create inputstream
            JsonReader reader = Json.createReader(is);							// Create reader for inputstream
            JsonObject data = reader.readObject();								// Convert string to JSON array
            
            String email = data.getString("email");

            List<Book> books = new LinkedList<>();
            for(JsonValue v : data.getJsonArray("books")){
                JsonObject obj =  v.asJsonObject();
                Book book = Book.createFromRequest(obj);
                books.add(book);
            }

            JsonObjectBuilder respObjBuilder = Json.createObjectBuilder();
            if(libRepo.addUserBorrowedBooks(email, books) == books.size()){
                respObjBuilder.add("status", true);
                return ResponseEntity.ok(respObjBuilder.build().toString());
            }else{
                respObjBuilder.add("status", false);
                return ResponseEntity.ok(respObjBuilder.build().toString());
            }
        }

        @PostMapping(path = "/return", consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> returnBorrowedBooks(@RequestBody String payload){
    
            InputStream is = new ByteArrayInputStream (payload.getBytes()); 	// Create inputstream
            JsonReader reader = Json.createReader(is);							// Create reader for inputstream
            JsonObject data = reader.readObject();								// Convert string to JSON array
            
            String email = data.getString("email");
            JsonObject bookJson = data.getJsonObject("book");
            Book book = Book.createFromRequest(bookJson);

            libRepo.returnUserBorrowedBook(email, book);

            List<Book> books = libRepo.getUserBorrowedBooks(email);

            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
            for(Book b : books){
                arrBuilder.add(b.toJsonForUser());
            }
            String forTheCommit = "";
            return ResponseEntity.ok(arrBuilder.build().toString());
        }
    
}
