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
    //Here we call the define the repositories and wire them so that we can communicate with the server
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
    //Get request handler that returns users Calendar data according to their token
    @GetMapping("/get")
    public List<Calendar> fetchDate(@RequestHeader("Authorization") String bearerToken)
    {
        bearerToken = bearerToken.replace("Bearer ", "");
        return calendarRepository.findCalendarByUser(userRepository.findByUsername(jwtUtil.getUsernameFromToken(bearerToken)));
    }
    //Delete request handler for Tasks within the Calendar table
    @DeleteMapping("/delete/{id}")
    public String deleteCalendar(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id) {
        bearerToken = bearerToken.replace("Bearer ", "");
        Calendar calendar = calendarRepository.findById(id).orElse(null);

        if (calendar != null && calendar.getUser().getUsername().equals(jwtUtil.getUsernameFromToken(bearerToken))) {
            calendarRepository.delete(calendar);
            return "Calendar deleted!";
        }

        return "Could not delete calendar!";
    }
    //Put request handler for changing calendar information
    @PutMapping("/edit/{id}")
    public String editCalendar(
        @RequestHeader("Authorization") String bearerToken,
        @PathVariable Long id,
        @RequestBody Calendar updated
        
    ) {
        bearerToken = bearerToken.replace("Bearer ", "");
        Calendar calendar = calendarRepository.findById(id).orElse(null);

        if (calendar != null && calendar.getUser().getUsername().equals(jwtUtil.getUsernameFromToken(bearerToken))) {
            calendar.setTitle(updated.getTitle());
            calendar.setDescription(updated.getDescription());
            calendar.setDate(updated.getDate());
            calendar.setIsFinished(updated.getIsFinished()); 
            calendarRepository.save(calendar);
            return "Calendar updated!";
        }

        return "Could not edit calendar!";
    }
}
