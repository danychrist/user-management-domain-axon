package com.springbank.user.query.api.repositories;

import com.springbank.user.core.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/*MongoRepository<T, String>Encapsulate all the logic/method that is required to access data source
 T = type for the domain model in this case User
 String = is for the document ID*/

public interface UserRepository extends MongoRepository<User, String> {
    //Custom mongoDB query
    @Query("{'$or' : [{'firstName': {$regex : ?0, $options: '1'}}, {'lastName': {$regex : ?0, $options: '1'}}, " +
            "{'emailAddress': {$regex : ?0, $options: '1'}}, {'emailAddress': {$regex : ?0, $options: '1'}}, " +
            "{'account.username': {$regex : ?0, $options: '1'}}]}")
    List<User> findByFilterRegex(String filter);
}
