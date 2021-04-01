package com.springbank.user.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data //Bundle the feature of @ToString, @EqualAndHashCode, @Getter and @Setter
@AllArgsConstructor //Generate a constructor with a parameter for each field in your class
@NoArgsConstructor //Generate default constructor with "NO" argument
@Builder
@Document(collection = "users") //Important for mongoDB
/*@Document annotation is used at the class level, Which identifies a domain object (in this case User) that
is going to be persisted to MongoDB database*/
public class User {
    @Id
    private String id;
    @NotEmpty(message = "firstName is mandatory")
    private String firstName;
    @NotEmpty(message = "lastName is mandatory")
    private String lastName;
    @Email(message = "please provide a valid email address")
    private String emailAddress;
    @NotNull(message = "please provide account credentials")
    @Valid
    private Account account;
}
