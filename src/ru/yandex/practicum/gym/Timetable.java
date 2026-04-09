package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay timeTraining = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);
        List<TrainingSession> sessions = dayMap.computeIfAbsent(timeTraining, k -> new ArrayList<>());
        sessions.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> sessions = new ArrayList<>();
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        for (TimeOfDay time : dayMap.navigableKeySet()) {
            sessions.addAll(dayMap.get(time));
        }
        return sessions;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        List<TrainingSession> sessions = dayMap.get(timeOfDay);
        if (sessions != null) {
            return sessions;
        }

        return new ArrayList<>();
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> counters = new HashMap<>();

        for (DayOfWeek day : timetable.keySet()) {
            TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);
            for (List<TrainingSession> sessionsList : dayMap.values()) {
                for (TrainingSession session : sessionsList) {
                    Coach coach = session.getCoach();
                    counters.compute(coach, (key, value) -> value == null ? 1 : value + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> coach : counters.entrySet()) {
            result.add(new CounterOfTrainings(coach.getKey(), coach.getValue()));
        }
        Collections.sort(result);
        return result;
    }
}
