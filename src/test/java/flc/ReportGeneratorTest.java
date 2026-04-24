package flc;

import flc.manager.BookingManager;
import flc.manager.Timetable;
import flc.model.Booking;
import flc.model.Day;
import flc.model.ExerciseType;
import flc.model.Lesson;
import flc.model.Member;
import flc.model.TimeSlot;
import flc.report.ReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests monthlyLessonReport() on ReportGenerator, verifying that only
 * ATTENDED bookings are counted (not BOOKED bookings).
 */
class ReportGeneratorTest {

    private BookingManager manager;
    private ReportGenerator reportGenerator;
    private Lesson lesson;
    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        member1 = new Member("M001", "Alice Johnson");
        member2 = new Member("M002", "Bob Smith");
        Timetable timetable = new Timetable();
        lesson = new Lesson("L001", ExerciseType.YOGA, Day.SATURDAY, TimeSlot.MORNING, 1, 5);
        timetable.addLesson(lesson);
        manager = new BookingManager(List.of(member1, member2), timetable);
        reportGenerator = new ReportGenerator();
    }

    @Test
    void monthlyLessonReport_countsOnlyAttendedBookings() {
        // Member1 books AND attends
        Booking b1 = manager.bookLesson(member1, lesson);
        manager.attendLesson(b1.getBookingId(), "Great class!", 5);

        // Member2 books but does NOT attend
        manager.bookLesson(member2, lesson);

        String report = reportGenerator.monthlyLessonReport(5, manager);

        // The attended count for L001 must be 1, not 2
        assertTrue(report.contains("1"), "Report must show 1 attended member, not 2");
        // Sanity: report covers the lesson
        assertTrue(report.contains("L001"), "Report should contain lesson L001");
    }

    @Test
    void monthlyLessonReport_noAttended_showsNAForRating() {
        // Book but do not attend
        manager.bookLesson(member1, lesson);

        String report = reportGenerator.monthlyLessonReport(5, manager);
        assertTrue(report.contains("N/A"), "Report should show N/A average rating when no attended bookings");
    }
}
