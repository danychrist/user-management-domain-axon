package com.springbank.user.query.api.handlers;

import com.springbank.user.query.api.dto.UserLookUpResponse;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import com.springbank.user.query.api.queries.SearchUserQuery;
import com.springbank.user.query.api.repositories.UserRepository;
import lombok.var;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserQueryHandlerImpl implements UserQueryHandler{

    private final UserRepository userRepository;

    @Autowired //For dependency injection
    public UserQueryHandlerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @QueryHandler
    @Override
    public UserLookUpResponse getUserById(FindUserByIdQuery query) {
        var user = userRepository.findById(query.getId());
        return user.isPresent() ? new UserLookUpResponse(user.get()) : null;
    }

    @QueryHandler
    @Override
    public UserLookUpResponse searchUsers(SearchUserQuery query) {
        //Extend UserRepository to add regular expression to query data from mongoDB
        var users = new ArrayList<>(userRepository.findByFilterRegex(query.getFilter()));
        return new UserLookUpResponse(users);
    }

    @QueryHandler
    @Override
    public UserLookUpResponse getAllUsers(FindAllUsersQuery query) {
        var users = new ArrayList<>(userRepository.findAll());
        return new UserLookUpResponse(users);
    }
}
