package flc;

import flc.manager.Timetable;
import flc.model.Day;
import flc.model.ExerciseType;
import flc.model.Lesson;
import flc.model.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests getLessonsByExerciseType() on Timetable.
 */
class TimetableTest {

    private Timetable timetable;

    @BeforeEach
    void setUp() {
        timetable = new Timetable();
        timetable.addLesson(new Lesson("L001", ExerciseType.YOGA,     Day.SATURDAY, TimeSlot.MORNING,   1, 5));
        timetable.addLesson(new Lesson("L002", ExerciseType.ZUMBA,    Day.SATURDAY, TimeSlot.AFTERNOON, 1, 5));
        timetable.addLesson(new Lesson("L003", ExerciseType.YOGA,     Day.SUNDAY,   TimeSlot.EVENING,   1, 5));
        timetable.addLesson(new Lesson("L004", ExerciseType.AQUACISE, Day.SUNDAY,   TimeSlot.MORNING,   1, 5));
    }

    @Test
    void getLessonsByExerciseType_returnsOnlyMatchingLessons() {
        List<Lesson> yogaLessons = timetable.getLessonsByExerciseType(ExerciseType.YOGA);
        assertEquals(2, yogaLessons.size(), "Should find exactly 2 Yoga lessons");
        assertTrue(yogaLessons.stream().allMatch(l -> l.getExerciseType() == ExerciseType.YOGA),
                "All returned lessons must be of type YOGA");
    }

    @Test
    void getLessonsByExerciseType_noMatch_returnsEmptyList() {
        List<Lesson> boxFitLessons = timetable.getLessonsByExerciseType(ExerciseType.BOX_FIT);
        assertTrue(boxFitLessons.isEmpty(), "Should return empty list when no lessons match the type");
    }
}
