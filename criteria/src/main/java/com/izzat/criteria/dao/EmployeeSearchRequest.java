package com.izzat.criteria.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EmployeeSearchRequest {
    String firstName;
    String lastName;
    String email;
}
