package com.rnd.scheduler.controller;

import com.rnd.scheduler.model.Calendar;
import com.rnd.scheduler.repository.CalendarRepository;
import com.rnd.scheduler.security.JwtUtil;
import com.rnd.scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cal")
public class UserCalendarController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CalendarRepository calendarRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/user")
    public String registerDate(@RequestHeader("Authorization") String bearerToken, @RequestBody Calendar calendar)
    {
        bearerToken = bearerToken.replace("Bearer ", "");
        Calendar newCalendar = new Calendar(
                null,
                calendar.getTitleAppointment(),
                calendar.getDate(),
                userRepository.findByUsername(jwtUtil.getUsernameFromToken(bearerToken))
        );
        calendarRepository.save(newCalendar);
        return "Date has been registered!";
    }
    @PostMapping("/group")
    public String registerDateGroup(@RequestHeader("Authorization") String bearerToken, @RequestBody Calendar calendar)
    {
        bearerToken = bearerToken.replace("Bearer ", "");
        Long groupId = userRepository.findGroupId(jwtUtil.getUsernameFromToken(bearerToken));
        userRepository.findUsersByGroupId(groupId).forEach(user -> {
            Calendar newCalendar = new Calendar(
                    null,
                    calendar.getTitleAppointment(),
                    calendar.getDate(),
                    user
            );
            calendarRepository.save(newCalendar);
        });
        return "Date has been registered!";
    }
    @PostMapping("/group/join")
    public String joinGroup(@RequestHeader("Authorization") String BearerToken, @RequestParam Long groupId)
    {
        userRepository.updateUser(jwtUtil.getUsernameFromToken(BearerToken), groupId);
        return "you have joined the group " + groupId;
    }
    @GetMapping("/user")
    public List<Calendar> fetchDate(@RequestHeader("Authorization") String bearerToken)
    {
        bearerToken = bearerToken.replace("Bearer ", "");
        return calendarRepository.findCalendarByUser(userRepository.findByUsername(jwtUtil.getUsernameFromToken(bearerToken)));
    }
    @GetMapping("/group")
    public List<Calendar> fetchDateGroup(@RequestHeader("Authorization") String bearerToken)
    {
        List<Calendar> calendars = new ArrayList<Calendar>();
        bearerToken = bearerToken.replace("Bearer ", "");
        Long groupId = userRepository.findGroupId(jwtUtil.getUsernameFromToken(bearerToken));
        userRepository.findUsersByGroupId(groupId).forEach(user -> {
            calendars.addAll(calendarRepository.findCalendarByUser(user));
        });
        return calendars;
    }
}
