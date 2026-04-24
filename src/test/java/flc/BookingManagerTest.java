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
 * Tests bookLesson() on BookingManager, focusing on the duplicate booking guard.
 */
class BookingManagerTest {

    private BookingManager manager;
    private Member member;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        member = new Member("M001", "Alice Johnson");
        Timetable timetable = new Timetable();
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
}
