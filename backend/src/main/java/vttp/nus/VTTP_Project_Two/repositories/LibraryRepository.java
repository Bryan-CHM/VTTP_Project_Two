package vttp.nus.VTTP_Project_Two.repositories;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.nus.VTTP_Project_Two.models.Book;
import static vttp.nus.VTTP_Project_Two.repositories.Queries.*;

@Repository
public class LibraryRepository {

    @Autowired
    private JdbcTemplate template;

    public Integer addBooksToLibrary(List<Book> books){
        Integer updated = 0;
        for(Book b : books){
            final SqlRowSet result = template.queryForRowSet(
                SQL_CHECK_IF_BOOK_EXISTS, b.getTitle().orElse(null));
        
            if(result.next()){
                updated += template.update(SQL_UPDATE_QUANTITY, 
                result.getInt("quantity")+b.getQuantity().get(), 
                result.getInt("book_id"));
            }else{
                updated += template.update(
                SQL_ADD_BOOKS_TO_LIBRARY, 
                b.getTitle().orElse(null),
                b.getAuthors().orElse(null), 
                b.getDescription().orElse(null), 
                b.getThumbnail().orElse(null), 
                b.getPublisher().orElse(null), 
                b.getPublishedDate().orElse(null), 
                b.getPageCount().orElse(null), 
                b.getQuantity().orElse(null));
                
            }   
        }
        return updated;
    }
    
    public List<Book> getUserListOfBooks(String query){
        List<Book> books = new LinkedList<>();

        final SqlRowSet result = template.queryForRowSet(SQL_GET_USER_BOOKS_BY_QUERY, query, query);
        while(result.next()){

            Book b = new Book();

            if(result.getString("title") != null){
                b.setTitle(Optional.of(result.getString("title")));
            }else{
                b.setTitle(Optional.empty());
            }
            if(result.getString("authors") != null){
                b.setAuthors(Optional.of(result.getString("authors")));
            }else{
                b.setAuthors(Optional.empty());
            }
            if(result.getString("description") != null){
                b.setDescription(Optional.of(result.getString("description")));
            }else{
                b.setDescription(Optional.empty());
            }
            if(result.getString("thumbnail") != null){
                b.setThumbnail(Optional.of(result.getString("thumbnail")));
            }else{
                b.setThumbnail(Optional.empty());
            }
            if(result.getString("publisher") != null){
                b.setPublisher(Optional.of(result.getString("publisher")));
            }else{
                b.setPublisher(Optional.empty());
            }
            if(result.getString("published_date") != null){
                b.setPublishedDate(Optional.of(result.getString("published_date")));
            }else{
                b.setPublishedDate(Optional.empty());
            }
            if(String.valueOf(result.getInt("page_count")) != null){
                b.setPageCount(Optional.of(result.getInt("page_count")));
            }else{
                b.setPageCount(Optional.empty());
            }
            if(String.valueOf(result.getInt("quantity")) != null){
                b.setQuantity(Optional.of(result.getInt("quantity")));
            }else{
                b.setQuantity(Optional.empty());
            }
            b.setDuration(Optional.empty());
            books.add(b);
        }
        return books;
    }

    public List<Book> getUserNextListOfBooks(String query, Integer index){
        List<Book> books = new LinkedList<>();

        final SqlRowSet result = template.queryForRowSet(SQL_GET_USER_BOOKS_BY_QUERY_WITH_INDEX, query, query, index);
        while(result.next()){

            Book b = new Book();

            if(result.getString("title") != null){
                b.setTitle(Optional.of(result.getString("title")));
            }else{
                b.setTitle(Optional.empty());
            }
            if(result.getString("authors") != null){
                b.setAuthors(Optional.of(result.getString("authors")));
            }else{
                b.setAuthors(Optional.empty());
            }
            if(result.getString("description") != null){
                b.setDescription(Optional.of(result.getString("description")));
            }else{
                b.setDescription(Optional.empty());
            }
            if(result.getString("thumbnail") != null){
                b.setThumbnail(Optional.of(result.getString("thumbnail")));
            }else{
                b.setThumbnail(Optional.empty());
            }
            if(result.getString("publisher") != null){
                b.setPublisher(Optional.of(result.getString("publisher")));
            }else{
                b.setPublisher(Optional.empty());
            }
            if(result.getString("published_date") != null){
                b.setPublishedDate(Optional.of(result.getString("published_date")));
            }else{
                b.setPublishedDate(Optional.empty());
            }
            if(String.valueOf(result.getInt("page_count")) != null){
                b.setPageCount(Optional.of(result.getInt("page_count")));
            }else{
                b.setPageCount(Optional.empty());
            }
            if(String.valueOf(result.getInt("quantity")) != null){
                b.setQuantity(Optional.of(result.getInt("quantity")));
            }else{
                b.setQuantity(Optional.empty());
            }
            b.setDuration(Optional.empty());
            books.add(b);
        }
        return books;
    }

    public Integer addUserBorrowedBooks(String email, List<Book> books){
        Integer updated = 0, quantity = 0;
        String bookId = "", userId = "";
        for(Book b : books){

            // Get bookId from library table
            SqlRowSet result = template.queryForRowSet(SQL_GET_BOOK_ID_AND_QUANTITY, b.getTitle().get());
            if(result.next()){
                bookId = result.getString("book_id");
                quantity = result.getInt("quantity");
            }
            
            // Get userId from user table
            result = template.queryForRowSet(SQL_GET_USER_ID, email);
            if(result.next()){
                userId = result.getString("user_id");
            }
            
            // Add new book into borrow db
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.now();
            if(b.getDuration().get().equals("24h")){
                time = time.plusDays(1);
            }else if(b.getDuration().get().equals("1w")){
                time = time.plusDays(7);
            }else if(b.getDuration().get().equals("1m")){
                time = time.plusMonths(1);
            }
            updated += template.update(
                SQL_ADD_USER_BORROWED_BOOKS, 
                userId, 
                bookId, 
                dtf.format(time)
            );
                
            // Update library quantity
            template.update(
                SQL_UPDATE_QUANTITY, quantity-1, bookId
            );
        }
        return updated;
    }

    public List<Book> getUserBorrowedBooks(String email){
        String userId = "";
        List<Book> books = new LinkedList<>();

        SqlRowSet result = template.queryForRowSet(SQL_GET_USER_ID, email);
            if(result.next()){
                userId = result.getString("user_id");
            }
        result = template.queryForRowSet(SQL_GET_USER_BORROWED_BOOKS, userId);
        while(result.next()){
            Book b = new Book();

            if(result.getString("title") != null){
                b.setTitle(Optional.of(result.getString("title")));
            }else{
                b.setTitle(Optional.empty());
            }
            if(result.getString("authors") != null){
                b.setAuthors(Optional.of(result.getString("authors")));
            }else{
                b.setAuthors(Optional.empty());
            }
            if(result.getString("description") != null){
                b.setDescription(Optional.of(result.getString("description")));
            }else{
                b.setDescription(Optional.empty());
            }
            if(result.getString("thumbnail") != null){
                b.setThumbnail(Optional.of(result.getString("thumbnail")));
            }else{
                b.setThumbnail(Optional.empty());
            }
            if(result.getString("publisher") != null){
                b.setPublisher(Optional.of(result.getString("publisher")));
            }else{
                b.setPublisher(Optional.empty());
            }
            if(result.getString("published_date") != null){
                b.setPublishedDate(Optional.of(result.getString("published_date")));
            }else{
                b.setPublishedDate(Optional.empty());
            }
            if(String.valueOf(result.getInt("page_count")) != null){
                b.setPageCount(Optional.of(result.getInt("page_count")));
            }else{
                b.setPageCount(Optional.empty());
            }
            if(String.valueOf(result.getInt("quantity")) != null){
                b.setQuantity(Optional.of(result.getInt("quantity")));
            }else{
                b.setQuantity(Optional.empty());
            }
            if(String.valueOf(result.getString("due")) != null){
                b.setDuration(Optional.of(result.getString("due")));
            }else{
                b.setDuration(Optional.empty());
            }
            books.add(b);
        }
        return books;
    }

    public Integer returnUserBorrowedBook(String email, Book book){
        Integer updated = 0, quantity = 0;
        String bookId = "", userId = "";

        // Get bookId from library table
        SqlRowSet result = template.queryForRowSet(SQL_GET_BOOK_ID_AND_QUANTITY, book.getTitle().get());
        if(result.next()){
            bookId = result.getString("book_id");
            quantity = result.getInt("quantity");
        }
        
        // Get userId from user table
        result = template.queryForRowSet(SQL_GET_USER_ID, email);
        if(result.next()){
            userId = result.getString("user_id");
        }

        updated += template.update(SQL_RETURN_USER_BORROWED_BOOK, userId, bookId);

        // Update library quantity
        template.update(
            SQL_UPDATE_QUANTITY, quantity+1, bookId
        );
        return updated;
    }
}

