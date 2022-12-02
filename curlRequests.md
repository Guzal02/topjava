// Тесты  на методы класса - MealRestController
// информация о всей еде залогиненного пользователя 
curl http://localhost:8080/topjava/rest/meals
// получить информацию о конкретной еде
curl http://localhost:8080/topjava/rest/meals/100003
// Чтобы добавить новую еду
curl -X POST -H "Content-Type: application/json" -d '{"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300}' http://localhost:8080/topjava/rest/meals
// Обновить еду
curl -d '{"dateTime":"2020-01-30T10:00", "description":"Завтрак обн", "calories":10}' -H 'Content-Type: application/json' -X PUT http://localhost:8080/topjava/rest/meals/100003
//Удалить еду
curl -X DELETE http://localhost:8080/topjava/rest/meals/100003
// Отфильтровать список еды по дате и времени
curl http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&endDate=2020-01-31&startTime=00:00&endTime=23:00
