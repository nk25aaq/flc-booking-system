package flc;

import flc.model.Day;
import flc.model.ExerciseType;
import flc.model.Lesson;
import flc.model.Member;
import flc.model.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests key method addMember() on Lesson, specifically the capacity constraint.
 */
class LessonTest {

    private Lesson lesson;

    @BeforeEach
    void setUp() {
        lesson = new Lesson("L001", ExerciseType.YOGA, Day.SATURDAY, TimeSlot.MORNING, 1, 5);
    }

    @Test
    void addMember_capacityNotExceeded_returnsTrueAndIncrementsCount() {
        Member member = new Member("M001", "Alice Johnson");
        boolean result = lesson.addMember(member);
        assertTrue(result, "addMember should return true when capacity is not full");
        assertEquals(1, lesson.getBookedCount());
    }

    @Test
    void addMember_atFullCapacity_returnsFalseAndCountUnchanged() {
        // Fill all 4 slots
        lesson.addMember(new Member("M001", "Alice Johnson"));
        lesson.addMember(new Member("M002", "Bob Smith"));
        lesson.addMember(new Member("M003", "Carol White"));
        lesson.addMember(new Member("M004", "David Brown"));

        assertFalse(lesson.hasSpace(), "Lesson should be full after 4 bookings");

        // Fifth member must be rejected
        boolean result = lesson.addMember(new Member("M005", "Emma Davis"));
        assertFalse(result, "addMember should return false when lesson is at full capacity");
        assertEquals(4, lesson.getBookedCount(), "Booked count must remain 4 after rejected add");
    }
}
