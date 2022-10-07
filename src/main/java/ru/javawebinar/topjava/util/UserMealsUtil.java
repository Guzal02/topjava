package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        Map<LocalDate, List<UserMeal>> userMealHashMap = new HashMap<>();

        for (UserMeal meal : meals) {
            List<UserMeal> userMealsByDate = userMealHashMap.get(meal.getDateTime().toLocalDate());
            if (userMealsByDate == null) {
                List<UserMeal> mealsPerDay = new ArrayList<>();
                mealsPerDay.add(meal);
                userMealHashMap.put(meal.getDateTime().toLocalDate(), mealsPerDay);
            } else {
                userMealsByDate.add(meal);
            }
        }

        for (List<UserMeal> value : userMealHashMap.values()) {
            boolean excess = false;
            int sum = value.stream().mapToInt(UserMeal::getCalories).sum();
            if (sum > caloriesPerDay) {
                excess = true;
            }
            boolean finalExcess = excess;
            value.forEach(userMeal -> {
                if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                    userMealWithExcesses.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), finalExcess));
                }
            });
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> DateByCalories = meals.stream()
                .collect(Collectors.groupingBy(e -> e.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(
                        userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(e -> {
                    boolean excess = DateByCalories.get(e.getDateTime().toLocalDate()) > caloriesPerDay;
                    return new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(), excess);
                })
                .collect(Collectors.toList());
    }
}
