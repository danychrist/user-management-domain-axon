package com.springbank.user.core.events;

import com.springbank.user.core.models.User;
import lombok.Builder;
import lombok.Data;

@Data
/*@Data automatically generate:
 - getters and setters
 - toString
 - equal and hashcode*/
@Builder
public class UserRegisteredEvent {
    private String id;
    private User user;
}
