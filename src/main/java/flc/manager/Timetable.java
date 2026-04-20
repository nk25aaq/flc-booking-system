package flc.manager;

import flc.model.Day;
import flc.model.ExerciseType;
import flc.model.Lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Timetable {
    private final List<Lesson> lessons;

    public Timetable() {
        this.lessons = new ArrayList<>();
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public List<Lesson> getAllLessons() {
        return new ArrayList<>(lessons);
    }

    public List<Lesson> getLessonsByDay(Day day) {
        return lessons.stream()
                .filter(l -> l.getDay() == day)
                .collect(Collectors.toList());
    }

    public List<Lesson> getLessonsByExerciseType(ExerciseType type) {
        return lessons.stream()
                .filter(l -> l.getExerciseType() == type)
                .collect(Collectors.toList());
    }

    public Lesson getLessonById(String id) {
        return lessons.stream()
                .filter(l -> l.getLessonId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public List<Lesson> getLessonsForMonth(int month) {
        return lessons.stream()
                .filter(l -> l.getMonth() == month)
                .collect(Collectors.toList());
    }

    public String formatLessonList(List<Lesson> list) {
        if (list.isEmpty()) return "  (no lessons found)";
        StringBuilder sb = new StringBuilder();
        for (Lesson l : list) {
            sb.append("  ").append(l).append("\n");
        }
        return sb.toString().stripTrailing();
    }
}
