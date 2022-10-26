package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL5_ID, ADMIN_ID);
        MealTestData.assertMatch(meal, adminMeal2);
    }

    @Test
    public void getAlienMeal() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL5_ID, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL6_ID, ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL6_ID, ADMIN_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL4_ID, NOT_FOUND));
    }

    @Test
    public void deletedAlienMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startDate = LocalDate.of(2022, Month.OCTOBER, 20);
        LocalDate endDate = LocalDate.of(2022, Month.OCTOBER, 22);

        List<Meal> meals = service.getBetweenInclusive(startDate, endDate, USER_ID);
        MealTestData.assertMatch(meals, userMeal1, userMeal2);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        MealTestData.assertMatch(all, userMeal1, userMeal2, userMeal3);
    }

    @Test
    public void update() {
        Meal updated = getUpdatedMeal();
        service.update(updated, USER_ID);
        MealTestData.assertMatch(service.get(MEAL1_ID, USER_ID), getUpdatedMeal());
    }

    @Test
    public void updateAlienMeal() {
        assertThrows(NotFoundException.class, () -> service.update(adminMeal1, USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNewMeal(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNewMeal();
        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        LocalDateTime dateTime = LocalDateTime.of(2022, Month.OCTOBER, 22, 12, 15, 32);
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(dateTime, "Морковь", 230), USER_ID));
    }
}