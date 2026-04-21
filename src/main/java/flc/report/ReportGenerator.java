package flc.report;

import flc.manager.BookingManager;
import flc.model.Booking;
import flc.model.BookingStatus;
import flc.model.ExerciseType;
import flc.model.Lesson;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {

    public String monthlyLessonReport(int month, BookingManager manager) {
        List<Lesson> monthLessons = manager.getTimetable().getLessonsForMonth(month);
        if (monthLessons.isEmpty()) {
            return "No lessons found for month " + month + ".";
        }

        List<Booking> allBookings = manager.getAllBookings();
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append(String.format(" Monthly Lesson Report  -  Month %d%n", month));
        sb.append("=================================================================\n");
        sb.append(String.format("%-6s %-5s %-9s %-9s %-11s %-8s %-10s%n",
                "ID", "Wknd", "Day", "Slot", "Exercise", "Attended", "Avg Rating"));
        sb.append("-----------------------------------------------------------------\n");

        for (Lesson lesson : monthLessons) {
            List<Booking> attended = allBookings.stream()
                    .filter(b -> b.getLesson().equals(lesson)
                            && b.getStatus() == BookingStatus.ATTENDED)
                    .collect(Collectors.toList());

            int count = attended.size();
            String avgRating = count == 0 ? "N/A"
                    : String.format("%.2f", attended.stream()
                            .mapToInt(b -> b.getReview().getRating())
                            .average()
                            .orElse(0.0));

            sb.append(String.format("%-6s %-5d %-9s %-9s %-11s %-8d %-10s%n",
                    lesson.getLessonId(),
                    lesson.getWeekendNumber(),
                    lesson.getDay().getDisplayName(),
                    lesson.getTimeSlot().getDisplayName(),
                    lesson.getExerciseType().getDisplayName(),
                    count,
                    avgRating));
        }
        sb.append("=================================================================\n");
        return sb.toString();
    }

    public String championExerciseReport(int month, BookingManager manager) {
        List<Lesson> monthLessons = manager.getTimetable().getLessonsForMonth(month);
        if (monthLessons.isEmpty()) {
            return "No lessons found for month " + month + ".";
        }

        Map<ExerciseType, Double> incomeByType = new EnumMap<>(ExerciseType.class);
        for (ExerciseType type : ExerciseType.values()) {
            incomeByType.put(type, 0.0);
        }

        for (Booking b : manager.getAllBookings()) {
            if (b.getStatus() == BookingStatus.ATTENDED
                    && b.getLesson().getMonth() == month) {
                ExerciseType type = b.getLesson().getExerciseType();
                incomeByType.merge(type, type.getPrice(), Double::sum);
            }
        }

        ExerciseType champion = incomeByType.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append(String.format(" Monthly Champion Exercise Report  -  Month %d%n", month));
        sb.append("=================================================================\n");
        sb.append(String.format("%-12s %-14s %-10s%n", "Exercise", "Income (£)", ""));
        sb.append("-----------------------------------------------------------------\n");

        for (ExerciseType type : ExerciseType.values()) {
            double income = incomeByType.getOrDefault(type, 0.0);
            String tag = type.equals(champion) && income > 0 ? "<-- CHAMPION" : "";
            sb.append(String.format("%-12s £%-13.2f %s%n",
                    type.getDisplayName(), income, tag));
        }
        sb.append("=================================================================\n");
        return sb.toString();
    }
}
