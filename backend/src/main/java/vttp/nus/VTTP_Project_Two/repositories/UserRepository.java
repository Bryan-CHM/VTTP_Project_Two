package vttp.nus.VTTP_Project_Two.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.nus.VTTP_Project_Two.models.User;
import static vttp.nus.VTTP_Project_Two.repositories.Queries.*;

@Repository
public class UserRepository{

    @Autowired
    private JdbcTemplate template;

    public Boolean verifyUser(User user){
        final SqlRowSet result = template.queryForRowSet(
            SQL_VERIFY_USER, user.getEmail(), user.getPassword());

        if(result.next()){
            return true;
        }
        return false;   
    }

    public Boolean getUserRole(User user){
        final SqlRowSet result = template.queryForRowSet(
            SQL_GET_USER_ROLE, user.getEmail());

        if(result.next()){
            return result.getBoolean("admin");
        }
        return false;   
    }

    public Integer checkIfUserExists(User user){
        final SqlRowSet result = template.queryForRowSet(
            SQL_CHECK_USER_EXISTS, user.getEmail());
    
        if(result.next()){
                return result.getInt("count");
        }
        return -1;   
    }

    public Boolean createUser(User user) throws DataIntegrityViolationException{
        int count = template.update(SQL_CREATE_USER, user.getEmail(), user.getPassword());
        return 1 == count; 
    }
    
}
