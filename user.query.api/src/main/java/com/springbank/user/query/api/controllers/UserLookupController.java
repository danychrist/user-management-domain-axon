package com.springbank.user.query.api.controllers;

import com.springbank.user.query.api.dto.UserLookUpResponse;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import com.springbank.user.query.api.queries.SearchUserQuery;
import lombok.var;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/userLookup")
public class UserLookupController {
    private final QueryGateway queryGateway;

    public UserLookupController(QueryGateway queryGateway){
        this.queryGateway = queryGateway;

    }
    @GetMapping(path = "/")
    public ResponseEntity<UserLookUpResponse> getAllUsers(){
        try{
            var query = new FindAllUsersQuery(); //This what the queryGateway user to know which method to invoke
            var response= queryGateway.query(query, ResponseTypes.instanceOf(UserLookUpResponse.class)).join();
            /*The queryGateway will know exactly which Query method,to invoke from the UserQueryHandlerImpl.
            It is important to specify @QueryHandler when implementing a query method*/
            if(response == null || response.getUsers() == null){ //response is of type UserLooUpResponse
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            var safeErrorMessage = "Failed to complete all users request";
            System.out.println(e.toString());
            return new ResponseEntity<>(new UserLookUpResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "/byId/{id}")
    public ResponseEntity<UserLookUpResponse> getUserById(@PathVariable(value = "id") String id){
        try{
            var query = new FindUserByIdQuery(id);
            var response= queryGateway.query(query, ResponseTypes.instanceOf(UserLookUpResponse.class)).join();
            //The queryGateway will know exactly which Query method,to invoke from the UserQueryHandlerImpl
            if(response == null || response.getUsers() == null){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            var safeErrorMessage = "Failed to complete get user by id request";
            System.out.println(e.toString());
            return new ResponseEntity<>(new UserLookUpResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "/byFilter/{filter}")
    public ResponseEntity<UserLookUpResponse> searchUserByFilter(@PathVariable(value = "filter") String filter){
        try{
            var query = new SearchUserQuery(filter);
            var response= queryGateway.query(query, ResponseTypes.instanceOf(UserLookUpResponse.class)).join();

            if(response == null || response.getUsers() == null){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            var safeErrorMessage = "Failed to complete user search request";
            System.out.println(e.toString());
            return new ResponseEntity<>(new UserLookUpResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
