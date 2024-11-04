package org.example.service.utils;

import org.example.model.entity.User;

import java.util.ArrayList;
import java.util.List;

import static org.example.enums.Gender.MALE;

public class SearchUtils {

    public static List<String> defineSearchUserData(User user, User searchUser) {

        List<String> filters = new ArrayList<>();
        switch (user.getSexualOrientation()) {
            case HETERO: {
                filters.add("'sexualOrientation' != 'GAY'");
                filters.add("gender" + (user.getGender().equals(MALE) ? " = 'FEMALE'" : " = 'MALE'"));
                break;
            }
            case GAY: {
                filters.add("'sexualOrientation' != 'HETERO'");
                filters.add("gender" + (user.getGender().equals(MALE) ? " = 'MALE'" : " = 'FEMALE'"));
                break;
            }
            case BI: {
                filters.add(user.getGender().equals(MALE) ? "('sexualOrientation' = 'BI')\n" +
                        "   OR (gender = 'MALE' AND 'sexualOrientation' = 'GAY')\n" +
                        "   OR (gender = 'FEMALE' AND 'sexualOrientation' = 'HETERO')"
                        : "(sexualOrientation = 'BI')\n" +
                        "   OR (gender = 'MALE' AND 'sexualOrientation' = 'HETERO')\n" +
                        "   OR (gender = 'FEMALE' AND 'sexualOrientation' = 'GAY')");
                break;
            }

        }
        return filters;
    }

}
