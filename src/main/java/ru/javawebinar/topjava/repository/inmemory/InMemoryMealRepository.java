package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.rmi.CORBA.Util;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Map<Integer, Map<Integer, Meal>> usersMealsRepository = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, InMemoryUserRepository.USER_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> integerMealMap = usersMealsRepository.putIfAbsent(userId, new ConcurrentHashMap<>());
            return integerMealMap.putIfAbsent(meal.getId(), meal);
        }
        return usersMealsRepository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> integerMealMap = usersMealsRepository.get(userId);
        if (integerMealMap != null) {
            return integerMealMap.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> integerMealMap = usersMealsRepository.get(userId);
        if (integerMealMap != null) {
            return integerMealMap.get(id);
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true);
    }

    public List<Meal> filteredMeal(LocalDateTime starDateTime, LocalDateTime endDateTime, int userId) {
        return filterByPredicate(userId, meal ->
                DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), starDateTime.toLocalTime(), endDateTime.toLocalTime()));
    }

    public List<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = usersMealsRepository.get(userId);
        return meals == null ? Collections.emptyList() :
                meals.values().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDate).reversed())
                        .collect(Collectors.toList());
    }

}

