package vttp.nus.VTTP_Project_Two.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

public class Book {
    private Optional<String> title;
    private Optional<String> authors;
    private Optional<String> description;
    private Optional<String> thumbnail;
    private Optional<String> publisher;
    private Optional<String> publishedDate;
    private Optional<Integer> pageCount;
    private Optional<Integer> quantity;
    private Optional<String> duration;

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Optional<String> authors) {
        this.authors = authors;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public void setDescription(Optional<String> description) {
        this.description = description;
    }

    public Optional<String> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Optional<String> thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Optional<String> getPublisher() {
        return publisher;
    }

    public void setPublisher(Optional<String> publisher) {
        this.publisher = publisher;
    }

    public Optional<String> getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Optional<String> publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Optional<Integer> getPageCount() {
        return pageCount;
    }

    public void setPageCount(Optional<Integer> pageCount) {
        this.pageCount = pageCount;
    }

    public Optional<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(Optional<Integer> quantity) {
        this.quantity = quantity;
    }

    public Optional<String> getDuration() {
        return duration;
    }

    public void setDuration(Optional<String> duration) {
        this.duration = duration;
    }

    public static Book createFromAPI(JsonObject o){
        Book book = new Book();
        String authors = "";
        if(o.containsKey("title")){
            book.setTitle(Optional.of(o.getString("title")));
        }else{
            book.setTitle(Optional.empty());
        }
        if(o.containsKey("description")){
            book.setDescription(Optional.of(o.getString("description")));
        }else{
            book.setDescription(Optional.empty());
        }
        if(o.containsKey("publisher")){
            book.setPublisher(Optional.of(o.getString("publisher")));
        }else{
            book.setPublisher(Optional.empty());
        }
        if(o.containsKey("publishedDate")){
            book.setPublishedDate(Optional.of(o.getString("publishedDate")));
        }else{
            book.setPublishedDate(Optional.empty());
        }
        if(o.containsKey("pageCount")){
            book.setPageCount(Optional.of(o.getInt("pageCount")));
        }else{
            book.setPageCount(Optional.empty());
        }
        if(o.containsKey("imageLinks")){
            JsonObject imageLinkObj = o.getJsonObject("imageLinks");
            if(imageLinkObj.containsKey("thumbnail")){
                book.setThumbnail(Optional.of(imageLinkObj.getString("thumbnail")/*.concat("&fife=w400-h600")*/));
            }else{
                book.setThumbnail(Optional.empty());
            }
        }else{
            book.setThumbnail(Optional.empty());
        }
        if(o.containsKey("authors")){
            JsonArray bookAuthorArray = o.getJsonArray("authors");
            for(JsonValue author : bookAuthorArray){
                if(bookAuthorArray.size() > 1){
                    authors += author.toString()+", ";
                }else{
                    authors += author.toString();
                }
            }
            if(bookAuthorArray.size() > 1){
                authors = authors.substring(0, authors.length()-2);
            }
            book.setAuthors(Optional.of(authors));
        }else{
            book.setAuthors(Optional.empty());
        }
        return book;
    }

    public static Book createFromRequest(JsonObject o){
        Book book = new Book();
        List<String> authors = new LinkedList<>();
        if(o.containsKey("title")){
            book.setTitle(Optional.of(o.getString("title")));
        }else{
            book.setTitle(Optional.empty());
        }
        if(o.containsKey("description")){
            book.setDescription(Optional.of(o.getString("description")));
        }else{
            book.setDescription(Optional.empty());
        }
        if(o.containsKey("publisher")){
            book.setPublisher(Optional.of(o.getString("publisher")));
        }else{
            book.setPublisher(Optional.empty());
        }
        if(o.containsKey("publishedDate")){
            book.setPublishedDate(Optional.of(o.getString("publishedDate")));
        }else{
            book.setPublishedDate(Optional.empty());
        }
        if(o.containsKey("pageCount")){
            book.setPageCount(Optional.of(o.getInt("pageCount")));
        }else{
            book.setPageCount(Optional.empty());
        }
        if(o.containsKey("quantity")){
            book.setQuantity(Optional.of(o.getInt("quantity")));
        }else{
            book.setQuantity(Optional.empty());
        }
        if(o.containsKey("duration")){
            book.setDuration(Optional.of(o.getString("duration")));
        }else{
            book.setDuration(Optional.empty());
        }
        if(o.containsKey("thumbnail")){
            book.setThumbnail(Optional.of(o.getString("thumbnail")));         
        }else{
            book.setThumbnail(Optional.empty());
        }
        if(o.containsKey("authors")){
            book.setAuthors(Optional.of(o.getString(("authors"))));
        }else{
            book.setAuthors(Optional.empty());
        }
        return book;
    }

    public JsonObject toJson(){
    JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        if(title.isPresent()){
            objBuilder.add("title", title.get());
        }
        if(description.isPresent()){    
            objBuilder.add("description",description.get());
        }
        if(publisher.isPresent()){
            objBuilder.add("publisher", publisher.get());
        }
        if(publishedDate.isPresent()){
            objBuilder.add("publishedDate", publishedDate.get());
        }
        if(pageCount.isPresent()){
            objBuilder.add("pageCount", pageCount.get());
        }
        if(thumbnail.isPresent()){
            objBuilder.add("thumbnail", thumbnail.get());
        }
        if(authors.isPresent()){
            objBuilder.add("authors", authors.get()
            .toString()
            .replace("\"", "")
            .replace("[", "")
            .replace("]", ""));
        }
        return objBuilder.build();
    }

    public JsonObject toJsonForUser(){
    JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        if(title.isPresent()){
            objBuilder.add("title", title.get());
        }
        if(description.isPresent()){    
            objBuilder.add("description",description.get());
        }
        if(publisher.isPresent()){
            objBuilder.add("publisher", publisher.get());
        }
        if(publishedDate.isPresent()){
            objBuilder.add("publishedDate", publishedDate.get());
        }
        if(pageCount.isPresent()){
            objBuilder.add("pageCount", pageCount.get());
        }
        if(thumbnail.isPresent()){
            objBuilder.add("thumbnail", thumbnail.get());
        }
        if(quantity.isPresent()){
            objBuilder.add("quantity", quantity.get());
        }
        if(duration.isPresent()){
            objBuilder.add("duration", duration.get());
        }
        if(authors.isPresent()){
            objBuilder.add("authors", authors.get()
            .toString()
            .replace("\"", "")
            .replace("[", "")
            .replace("]", ""));
        }
        return objBuilder.build();
    }
}
