package com.rnd.scheduler.controller;

import com.rnd.scheduler.model.Calendar;
import com.rnd.scheduler.repository.CalendarRepository;
import com.rnd.scheduler.security.JwtUtil;
import com.rnd.scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/cal")
public class UserCalendarController {
    //Service classes would've been ideal, but while learning how to use REST Api we forgot about it
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CalendarRepository calendarRepository;
    @Autowired
    private JwtUtil jwtUtil;

    //This handles a Post request on localhost:8080/user, where the jwt token and body are used to append a new date
    @PostMapping("/add")
    public String registerDate(@RequestHeader("Authorization") String bearerToken, @RequestBody Calendar calendar)
    {
        //parse out the token
        bearerToken = bearerToken.replace("Bearer ", "");
        Calendar newCalendar = new Calendar(
                null,
                calendar.getTitle(),
                calendar.getDescription(),
                calendar.getDate(),
                false,
                userRepository.findByUsername(jwtUtil.getUsernameFromToken(bearerToken))
        );
        calendarRepository.save(newCalendar);
        return "Date has been registered!";
    }
    @GetMapping("/get")
    public List<Calendar> fetchDate(@RequestHeader("Authorization") String bearerToken)
    {
        bearerToken = bearerToken.replace("Bearer ", "");
        return calendarRepository.findCalendarByUser(userRepository.findByUsername(jwtUtil.getUsernameFromToken(bearerToken)));
    }
    @PutMapping("/put/{id}")
    public String updateCalendar(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id) {
        bearerToken = bearerToken.replace("Bearer ", "");
        Calendar updateCalendar=calendarRepository.findById(id).orElse(null);
        if(updateCalendar!=null)
        {
            System.out.println("hi");
            if(updateCalendar.getUser().getUsername().equals(jwtUtil.getUsernameFromToken(bearerToken)))
            {
                System.out.println("ho");
                updateCalendar.setFinished(true);
                calendarRepository.save(updateCalendar);
                return "Calendar has been updated!";
            }
        }
        return "Could not update calendar!";
    }
}
