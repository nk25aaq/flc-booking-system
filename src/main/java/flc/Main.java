package flc;

import flc.data.DataSeeder;
import flc.manager.BookingManager;
import flc.manager.Timetable;
import flc.model.Member;
import flc.ui.CLI;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Member> members   = DataSeeder.createMembers();
        Timetable timetable    = DataSeeder.createTimetable();
        BookingManager manager = new BookingManager(members, timetable);
        DataSeeder.seedBookingsAndReviews(manager);
        new CLI(manager).run();
    }
}
