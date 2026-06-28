package com.timora.app.service.impl;

import com.timora.app.dto.security.CurrentUser;
import com.timora.app.dto.ui.UserSessionDTO;
import com.timora.app.model.Person;
import com.timora.app.service.PersonService;
import com.timora.app.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final PersonService personService;

    @Override
    public UserSessionDTO getCurrentSession(CurrentUser currentUser) {

        Person p = personService.findById(currentUser.getPersonId());

        if (p == null) {
            throw new RuntimeException("Person not found for session");
        }

        String fullName = p.getFirstName() + " " + p.getLastName();

        return new UserSessionDTO(
                fullName,
                currentUser.getRole()
        );
    }
}