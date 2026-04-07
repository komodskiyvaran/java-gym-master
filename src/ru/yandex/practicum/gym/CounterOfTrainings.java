package ru.yandex.practicum.gym;

public class CounterOfTrainings {
    private Coach coach;
    private int countOfTrainings;

    public CounterOfTrainings(Coach coach, int countOfTrainings) {
        this.coach = coach;
        this.countOfTrainings = countOfTrainings;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCountOfTrainings() {
        return countOfTrainings;
    }
}
