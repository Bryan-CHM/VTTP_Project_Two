package vttp.nus.VTTP_Project_Two.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.nus.VTTP_Project_Two.models.Book;

@Service
public class LibraryService {


    private final String BOOK_API = "https://www.googleapis.com/books/v1/volumes";

    public List<Book> getListOfBooks(String query){

        String url = UriComponentsBuilder				
        .fromUriString(BOOK_API)
        .queryParam("q",query)
        .queryParam("maxResults",20)
        // .queryParam("startIndex",20)
        .toUriString();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.getForEntity(url, String.class); 
        InputStream is = new ByteArrayInputStream (resp.getBody().getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject data = reader.readObject();
        JsonArray itemsJsonArray = data.getJsonArray("items");

        List<Book> books = new LinkedList<>();
        
        for(JsonValue val : itemsJsonArray){
            JsonObject itemsJsonObject = val.asJsonObject();
            JsonObject volumeInfoJsonObject = itemsJsonObject.getJsonObject("volumeInfo");
            Book book = Book.createFromAPI(volumeInfoJsonObject);
            books.add(book);
        }
        return books;
    }

    public List<Book> getNextListOfBooks(String query, Integer index){

        String url = UriComponentsBuilder				
        .fromUriString(BOOK_API)
        .queryParam("q",query)
        .queryParam("maxResults",20)
        .queryParam("startIndex",index)
        .toUriString();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.getForEntity(url, String.class); 
        InputStream is = new ByteArrayInputStream (resp.getBody().getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject data = reader.readObject();
        JsonArray itemsJsonArray = data.getJsonArray("items");

        List<Book> books = new LinkedList<>();
        
        for(JsonValue val : itemsJsonArray){
            JsonObject itemsJsonObject = val.asJsonObject();
            JsonObject volumeInfoJsonObject = itemsJsonObject.getJsonObject("volumeInfo");
            Book book = Book.createFromAPI(volumeInfoJsonObject);
            books.add(book);
        }
        return books;
    } 
}
