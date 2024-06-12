package com.lld.truecaller.models.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class SocialMedialnfo {
    private HashMap<SocialProfileType, String> socialMediaInfo = new HashMap<>();
}
