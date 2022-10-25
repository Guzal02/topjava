package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL1_ID = START_SEQ + 3;
    public static final int MEAL2_ID = START_SEQ + 4;
    public static final int MEAL3_ID = START_SEQ + 5;
    public static final int MEAL4_ID = START_SEQ + 6;
    public static final int MEAL5_ID = START_SEQ + 7;
    public static final int MEAL6_ID = START_SEQ + 8;

    public static final Meal userMeal1 = new Meal(MEAL1_ID, LocalDateTime.of(2022, Month.OCTOBER, 22, 12, 15, 32), "Обед", 250);
    public static final Meal userMeal2 = new Meal(MEAL2_ID, LocalDateTime.of(2022, Month.OCTOBER, 20, 15, 10, 45), "Картошка", 200);
    public static final Meal userMeal3 = new Meal(MEAL3_ID, LocalDateTime.of(2022, Month.OCTOBER, 2, 9, 13, 15), "Завтрак", 345);
    public static final Meal adminMeal1 = new Meal(MEAL4_ID, LocalDateTime.of(2022, Month.OCTOBER, 15, 17, 55, 36), "Перекус", 220);
    public static final Meal adminMeal2 = new Meal(MEAL5_ID, LocalDateTime.of(2022, Month.OCTOBER, 6, 20, 1, 28), "Мясо", 550);
    public static final Meal adminMeal3 = new Meal(MEAL6_ID, LocalDateTime.of(2022, Month.OCTOBER, 5, 18, 2, 19), "Суп", 150);

    public static Meal getNewMeal() {
        return new Meal(null, LocalDateTime.of(2022, Month.OCTOBER, 20, 20, 1, 39), "Новая еда", 90);
    }

    public static Meal getUpdatedMeal() {
        Meal updated = userMeal1;
        updated.setDateTime(LocalDateTime.of(2020, Month.MARCH, 10, 10, 10, 10));
        updated.setDescription("Обновленная еда");
        updated.setCalories(600);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
