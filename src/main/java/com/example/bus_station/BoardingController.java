
package com.example.bus_station;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BoardingController {

    private final Queue<String> passengers = new LinkedList<>();

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("passengers", passengers);
        model.addAttribute("waitingCount", passengers.size());
        model.addAttribute(
            "nextPassenger",
            passengers.isEmpty() ? "None" : passengers.peek()
        );

        return "index";
    }

    @PostMapping("/add")
    public String addPassenger(
            @RequestParam String name,
            @RequestParam String destination,
            RedirectAttributes redirectAttributes) {

        name = name.trim();
        destination = destination.trim();

        if (name.isEmpty() || destination.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                "message",
                "Please enter both your name and destination."
            );
            return "redirect:/";
        }

        passengers.offer(name + " — " + destination);

        redirectAttributes.addFlashAttribute(
            "message",
            name + " joined the boarding queue successfully!"
        );

        return "redirect:/";
    }

    @PostMapping("/board")
    public String boardPassenger(
            RedirectAttributes redirectAttributes) {

        String passenger = passengers.poll();

        if (passenger == null) {
            redirectAttributes.addFlashAttribute(
                "message",
                "There are no passengers waiting."
            );
        } else {
            redirectAttributes.addFlashAttribute(
                "message",
                passenger + " is now boarding."
            );
        }

        return "redirect:/";
    }

    @PostMapping("/clear")
    public String clearQueue(
            RedirectAttributes redirectAttributes) {

        passengers.clear();

        redirectAttributes.addFlashAttribute(
            "message",
            "The boarding queue has been cleared."
        );

        return "redirect:/";
    }
}
