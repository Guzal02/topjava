package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController {

    @GetMapping("meals/{id}")
    public String get(@PathVariable int id, Model model) {
        model.addAttribute("meal", super.get(id));
        return "meals";
    }

    @RequestMapping(value = "meals/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable int id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("meals")
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/mealForm")
    public String getMealForm(@ModelAttribute("meal") Meal meal) {
        meal.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        meal.setDescription("");
        meal.setCalories(1000);
        return "mealForm";
    }

    @GetMapping("edit/{id}")
    public String edit(@ModelAttribute("meal") Meal meal, @PathVariable int id) {
        meal.setDateTime(super.get(id).getDateTime());
        meal.setDescription(super.get(id).getDescription());
        meal.setCalories(super.get(id).getCalories());
        return "mealForm";
    }

    @PostMapping("meals")
    public String save(HttpServletRequest request) throws UnsupportedEncodingException {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        String paramId = Objects.requireNonNull(request.getParameter("id"));

        if (StringUtils.hasLength(paramId)) {

            super.update(meal, Integer.parseInt(paramId));
        } else {
            super.create(meal);
        }
        return "redirect:/meals";
    }

    @GetMapping("meals/filter")
    public String getAllFiltered(Model model, HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
