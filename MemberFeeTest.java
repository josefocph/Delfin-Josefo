import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberFeeTest {
    private static final LocalDate TODAY = LocalDate.of(2026, 11, 18);

    private Member member(int ageOnToday, MembershipType type) {
        // date of birth such that the member is exactly `ageOnToday` on TODAY
        LocalDate dob = TODAY.minusYears(ageOnToday);
        return new Member(1, "Test", dob, type,
                SwimmerLevel.JUNIOR, SwimmerCategory.RECREATIONAL);
    }

    @Test
    void activeYouthUnder18_pays1000() {
        assertEquals(1000, member(17, MembershipType.ACTIVE).calculateFee(TODAY));
    }

    @Test
    void activeYouthAt0_pays1000() {
        assertEquals(1000, member(0, MembershipType.ACTIVE).calculateFee(TODAY));
    }

    @Test
    void activeSeniorAt18_pays1600() {           // boundary
        assertEquals(1600, member(18, MembershipType.ACTIVE).calculateFee(TODAY));
    }

    @Test
    void activeSeniorAt59_pays1600() {
        assertEquals(1600, member(59, MembershipType.ACTIVE).calculateFee(TODAY));
    }

    @Test
    void activeSeniorAt60_noDiscount() {          // boundary: "over 60" = >60
        assertEquals(1600, member(60, MembershipType.ACTIVE).calculateFee(TODAY));
    }

    @Test
    void activeSeniorAt61_gets25PercentDiscount() {
        assertEquals(1200, member(61, MembershipType.ACTIVE).calculateFee(TODAY));
    }

    @Test
    void passiveAnyAge_pays500() {
        assertEquals(500, member(30, MembershipType.PASSIVE).calculateFee(TODAY));
        assertEquals(500, member(70, MembershipType.PASSIVE).calculateFee(TODAY));
    }

    @Test
    void constructor_rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Member(
                1, "  ", TODAY.minusYears(20),
                MembershipType.ACTIVE, SwimmerLevel.SENIOR, SwimmerCategory.RECREATIONAL));
    }

    @Test
    void constructor_rejectsFutureDob() {
        assertThrows(IllegalArgumentException.class, () -> new Member(
                1, "Future", TODAY.plusDays(1),
                MembershipType.ACTIVE, SwimmerLevel.SENIOR, SwimmerCategory.RECREATIONAL));
    }
}