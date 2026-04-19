package flc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lesson {
    private static final int MAX_CAPACITY = 4;

    private final String lessonId;
    private final ExerciseType exerciseType;
    private final Day day;
    private final TimeSlot timeSlot;
    private final int weekendNumber;
    private final int month;
    private final List<Member> bookedMembers;

    public Lesson(String lessonId, ExerciseType exerciseType, Day day,
                  TimeSlot timeSlot, int weekendNumber, int month) {
        this.lessonId = lessonId;
        this.exerciseType = exerciseType;
        this.day = day;
        this.timeSlot = timeSlot;
        this.weekendNumber = weekendNumber;
        this.month = month;
        this.bookedMembers = new ArrayList<>();
    }

    public boolean hasSpace() {
        return bookedMembers.size() < MAX_CAPACITY;
    }

    public boolean addMember(Member member) {
        if (!hasSpace()) return false;
        bookedMembers.add(member);
        return true;
    }

    public boolean removeMember(Member member) {
        return bookedMembers.remove(member);
    }

    public boolean isMemberBooked(Member member) {
        return bookedMembers.contains(member);
    }

    public int getBookedCount() {
        return bookedMembers.size();
    }

    public int getRemainingSpaces() {
        return MAX_CAPACITY - bookedMembers.size();
    }

    public List<Member> getBookedMembers() {
        return Collections.unmodifiableList(bookedMembers);
    }

    public String getLessonId()         { return lessonId; }
    public ExerciseType getExerciseType() { return exerciseType; }
    public Day getDay()                 { return day; }
    public TimeSlot getTimeSlot()       { return timeSlot; }
    public int getWeekendNumber()       { return weekendNumber; }
    public int getMonth()               { return month; }

    @Override
    public String toString() {
        return String.format("%-6s | Wknd %-2d | %-9s | %-9s | %-10s | £%-5.2f | Spaces: %d/%d",
                lessonId, weekendNumber,
                day.getDisplayName(), timeSlot.getDisplayName(),
                exerciseType.getDisplayName(), exerciseType.getPrice(),
                bookedMembers.size(), MAX_CAPACITY);
    }
}
