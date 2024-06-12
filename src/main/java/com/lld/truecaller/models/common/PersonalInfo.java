package com.lld.truecaller.models.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalInfo {
    public PersonalInfo(String firstName) {
        this.firstName = firstName;
    }

    private String firstName;
    private String middleName;
    private String lastName;
    private String dob;
    private String initials;
    private Gender gender;
    private Address address;
    private String companyName;
    private String title;
}
