package com.springbank.user.cmd.api.aggregates;

import com.springbank.user.cmd.api.commands.RegisterUserCommand;
import com.springbank.user.cmd.api.commands.RemoveUserCommand;
import com.springbank.user.cmd.api.commands.UpdateUserCommand;
import com.springbank.user.cmd.api.security.PasswordEncoder;
import com.springbank.user.cmd.api.security.PasswordEncoderImpl;
import com.springbank.user.core.events.UserRegisteredEvent;
import com.springbank.user.core.events.UserRemovedEvent;
import com.springbank.user.core.events.UserUpdatedEvent;
import com.springbank.user.core.models.User;
import lombok.var;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

/*When  the event sourcing methods are raised, the events are also stored
 by Axon in the event store/write database*/
@Aggregate
public class UserAggregate {
    @AggregateIdentifier
    private String id;
    private User user;

    private final PasswordEncoder passwordEncoder;

    public UserAggregate(){
        passwordEncoder = new PasswordEncoderImpl();
    }
    @CommandHandler
    public UserAggregate(RegisterUserCommand command, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;

        var newUser = command.getUser();
        var newID = command.getId();
        newUser.setId(newID);
        var password = newUser.getAccount().getPassword(); //plain text password pass through https controller
        var hashedPassword = passwordEncoder.hashPassword(password);
        newUser.getAccount().setPassword(hashedPassword);

        /*Now that the command has been handled, an event has to be raised for the userEventHandler to take over
        in this case UserRegisteredEvent*/

        var event = UserRegisteredEvent.builder()
                .id(command.getId())
                .user(newUser)
                .build(); //build returns constructed user registered event

        //Now let's store the event in the event store and publish it to the event bus
        AggregateLifecycle.apply(event);
        /*The above statement raised the @EventSourcingHandler n(UserRegisteredEvent event) and publish the
        to the "event store". The same event is also published into the "event bus", for the @EventHandler
        (located in the user.query.api) to consume it.*/

    }
    @EventSourcingHandler
    public void on(UserRegisteredEvent event){
        //coming from the command at the very top (UserAggregate class variable)
        this.id = event.getId();
        this.user = event.getUser();
        //The the event is stored in the "event store" and the "event bus"

    }
    @CommandHandler
    public void handle(UpdateUserCommand command){
        var updatedUser = command.getUser();
        updatedUser.setId(command.getId());
        var password = updatedUser.getAccount().getPassword();
        var hashedPassword = this.passwordEncoder.hashPassword(password);
        updatedUser.getAccount().setPassword(hashedPassword);

        var event = UserUpdatedEvent.builder()
                .id(UUID.randomUUID().toString())
                .user(updatedUser)
                .build();

        AggregateLifecycle.apply(event); //publish to the event store and the event bus

    }
    @EventSourcingHandler
    public void on(UserUpdatedEvent event){
        this.user = event.getUser();
    }

    @CommandHandler
    public void handle(RemoveUserCommand command){
        var event = new UserRemovedEvent();
        event.setId(command.getId());
        AggregateLifecycle.apply(event);

    }

    @EventSourcingHandler
    public void on(UserRemovedEvent event){
        AggregateLifecycle.markDeleted();
    }

}
