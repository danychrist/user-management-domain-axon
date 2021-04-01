package com.springbank.user.query.api.handlers;

import com.springbank.user.query.api.dto.UserLookUpResponse;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import com.springbank.user.query.api.queries.SearchUserQuery;

public interface UserQueryHandler {
    UserLookUpResponse getUserById(FindUserByIdQuery query);
    UserLookUpResponse searchUsers(SearchUserQuery query);
    UserLookUpResponse getAllUsers(FindAllUsersQuery query);

}
