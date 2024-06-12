package com.lld.truecaller.models;

import com.lld.truecaller.models.common.Contact;
import com.lld.truecaller.models.common.PersonalInfo;
import com.lld.truecaller.models.common.SocialMedialnfo;
import com.lld.truecaller.models.common.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Business {
    private String businessName;
    private String businessDescription;
    private Tag tag;
    private BusinessSize businessSize;
    private Map<Days, OperatingHours> openHours;
    private Contact contact;
    private PersonalInfo personalInfo;
    private SocialMedialnfo socialMedialnfo;

    public Business(String businessName, Tag tag) {
        this.businessName = businessName;
        this.tag = tag;
    }
}
