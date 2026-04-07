package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private final Comparator<TimeOfDay> timeComparator = new Comparator<>() {
        @Override
        public int compare(TimeOfDay o1, TimeOfDay o2) {
            if (o1.getHours() == o2.getHours()) {
                return o1.getMinutes() - o2.getMinutes();
            } else return o1.getHours() - o2.getHours();
        }
    };

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay timeTraining = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.computeIfAbsent(day, k -> new TreeMap<>(timeComparator));
        List<TrainingSession> sessions = dayMap.computeIfAbsent(timeTraining, k -> new ArrayList<>());
        sessions.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> sessions = new ArrayList<>();

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap == null) return sessions;

        for (TimeOfDay time : dayMap.navigableKeySet()) {
            sessions.addAll(dayMap.get(time));
        }
        return sessions;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap != null) {
            List<TrainingSession> sessions = dayMap.get(timeOfDay);
            if (sessions != null) {
                return sessions;
            }
        }
        return new ArrayList<>();
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, Integer> counters = new HashMap<>();

        for (DayOfWeek day : timetable.keySet()) {
            TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);

            for (List<TrainingSession> sessionsList : dayMap.values()) {

                for (TrainingSession session : sessionsList) {
                    Coach coach = session.getCoach();
                    if (counters.containsKey(coach)) {
                        counters.put(coach, counters.get(coach) + 1);
                    } else {
                        counters.put(coach, 1);
                    }
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> coach : counters.entrySet()) {
            result.add(new CounterOfTrainings(coach.getKey(), coach.getValue()));
        }

        Comparator<CounterOfTrainings> counterOfTrainingsComparator = new Comparator<>() {
            @Override
            public int compare(CounterOfTrainings o1, CounterOfTrainings o2) {
                return o2.getCountOfTrainings() - o1.getCountOfTrainings();
            }
        };

        result.sort(counterOfTrainingsComparator);
        return result;
    }
}
