package vttp.nus.VTTP_Project_Two.repositories;

public interface Queries {
    public static final String SQL_VERIFY_USER = "select * from user where email = ? and password = sha1(?);";
    public static final String SQL_CHECK_USER_EXISTS = "select count(*) as count from user where email = ?;";
    public static final String SQL_CREATE_USER = "insert into user(email, password) values(?, sha1(?));";
    public static final String SQL_GET_USER_ID = "select user_id from user where email = ?";
    public static final String SQL_GET_USER_ROLE = "select admin from user where email = ?;";

    public static final String SQL_CHECK_IF_BOOK_EXISTS = "select book_id, quantity from library where title = ?";
    public static final String SQL_ADD_BOOKS_TO_LIBRARY = "insert into library(title, authors, description, thumbnail, publisher, published_date, page_count, quantity) values(?,?,?,?,?,?,?,?)";
    public static final String SQL_UPDATE_QUANTITY = "update library set quantity = ? where book_id = ?";

    public static final String SQL_GET_BOOK_ID_AND_QUANTITY = "select book_id, quantity from library where title = ?";
    public static final String SQL_GET_USER_BOOKS_BY_QUERY = 
    "select * from library where quantity > 0 AND title like ? UNION select * from library where quantity > 0 AND authors like ? LIMIT 20";
    public static final String SQL_GET_USER_BOOKS_BY_QUERY_WITH_INDEX = 
    "select * from library where quantity > 0 AND title like ? UNION select * from library where quantity > 0 AND authors like ? LIMIT 20 OFFSET ?";

    
    public static final String SQL_GET_BORROW_QUANTITY = "select quantity from borrow where user_id = ? and book_id = ?";
    public static final String SQL_ADD_USER_BORROWED_BOOKS = "insert into borrow(user_id, book_id, due) values (?,?,?)";
    public static final String SQL_GET_USER_BORROWED_BOOKS = "select * from borrow JOIN library ON borrow.book_id = library.book_id where user_id = ? and returned = false";
    public static final String SQL_RETURN_USER_BORROWED_BOOK = "update borrow set returned = true where user_id = ? and book_id = ?";

    public static final String SQL_INSERT_UPLOADED_BOOK = "insert into library(title, authors, description, publisher, published_date, page_count, quantity, uploadedImage) values(?,?,?,?,?,?,?,?)";

}
