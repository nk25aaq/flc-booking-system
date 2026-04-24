package flc;

import flc.manager.BookingException;
import flc.manager.BookingManager;
import flc.manager.Timetable;
import flc.model.Booking;
import flc.model.Day;
import flc.model.ExerciseType;
import flc.model.Lesson;
import flc.model.Member;
import flc.model.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests bookLesson() on BookingManager: duplicate guard, time-conflict guard,
 * and seat release on cancel.
 */
class BookingManagerTest {

    private BookingManager manager;
    private Member member;
    private Lesson lesson;
    private Timetable timetable;

    @BeforeEach
    void setUp() {
        member = new Member("M001", "Alice Johnson");
        timetable = new Timetable();
        lesson = new Lesson("L001", ExerciseType.YOGA, Day.SATURDAY, TimeSlot.MORNING, 1, 5);
        timetable.addLesson(lesson);
        manager = new BookingManager(List.of(member), timetable);
    }

    @Test
    void bookLesson_firstBooking_succeeds() {
        Booking booking = manager.bookLesson(member, lesson);
        assertNotNull(booking, "Booking should not be null on first successful booking");
        assertEquals(member, booking.getMember());
        assertEquals(lesson, booking.getLesson());
        assertEquals(1, lesson.getBookedCount());
    }

    @Test
    void bookLesson_duplicateBooking_throwsBookingException() {
        manager.bookLesson(member, lesson);
        BookingException ex = assertThrows(BookingException.class,
                () -> manager.bookLesson(member, lesson),
                "Booking the same lesson twice must throw BookingException");
        assertTrue(ex.getMessage().toLowerCase().contains("already"),
                "Exception message should mention an existing booking");
    }

    @Test
    void bookLesson_timeConflict_throwsBookingException() {
        // Two different lessons in the exact same weekend + day + time slot
        Lesson conflictLesson = new Lesson("L002", ExerciseType.ZUMBA, Day.SATURDAY, TimeSlot.MORNING, 1, 5);
        timetable.addLesson(conflictLesson);
        manager.bookLesson(member, lesson);
        assertThrows(BookingException.class,
                () -> manager.bookLesson(member, conflictLesson),
                "Booking a second lesson in the same slot must throw BookingException");
    }

    @Test
    void cancelBooking_releasesLessonSeat() {
        Booking booking = manager.bookLesson(member, lesson);
        assertEquals(1, lesson.getBookedCount());
        manager.cancelBooking(booking.getBookingId());
        assertEquals(0, lesson.getBookedCount(), "Cancelling should release the seat back to the lesson");
    }
}
