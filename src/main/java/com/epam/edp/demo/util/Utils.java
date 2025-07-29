package com.epam.edp.demo.util;

import com.epam.edp.demo.model.entity.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {

    /**
     * Returns the currently authenticated User.
     *
     * @return the current User or null if not authenticated
     */
    public static AuthUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthUser user) {
            return user;
        }

        return null;
    }


    public static boolean isEmpty(String string) {
        return string == null || string.isBlank();
    }

    public static String formatTimeSlot(LocalTime startTime, LocalTime endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

        String formattedStart = startTime.format(formatter).toLowerCase().replace("am", "a.m.").replace("pm", "p.m.");
        String formattedEnd = endTime.format(formatter).toLowerCase().replace("am", "a.m.").replace("pm", "p.m.");

        return formattedStart + " - " + formattedEnd;
    }

}
