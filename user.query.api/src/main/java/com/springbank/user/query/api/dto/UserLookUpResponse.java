package com.springbank.user.query.api.dto;

import com.springbank.user.core.dto.BaseResponse;
import com.springbank.user.core.models.User;


import java.util.ArrayList;
import java.util.List;

//Note: Lombok does not work well with inheritance here we will do everything the all way
public class UserLookUpResponse extends BaseResponse {
    private List<User> users; //Return the list of users data that will be queried from the db

    public UserLookUpResponse(String message){
        super(message);
    }
    public UserLookUpResponse(String message,User user){
        super(message);
        this.users = new ArrayList<>();
        this.users.add(user);
    }

    public UserLookUpResponse(User user){
        super(null);
        this.users = new ArrayList<>();
        this.users.add(user);
    }

    public UserLookUpResponse(ArrayList<User> users) {
        super(null);
        this.users = users;
    }

    //GetUsers and SetMethod added because the @Data annotation was removed
    public List<User> getUsers(){
        return this.users;
    }
    public void setUsers(List<User> Users){
        this.users = users;
    }
}
