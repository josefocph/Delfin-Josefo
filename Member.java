import java.time.LocalDate;
import java.time.Period;

public class Member {
    private final int memberNumber;      // unique, assigned by the club
    private String name;
    private LocalDate dateOfBirth;
    private MembershipType membershipType;
    private SwimmerLevel swimmerLevel;
    private SwimmerCategory swimmerCategory;

    // Fee constants — single source of truth
    private static final int YOUTH_FEE_DKK   = 1000;
    private static final int SENIOR_FEE_DKK  = 1600;
    private static final int PASSIVE_FEE_DKK = 500;
    private static final double SENIOR_DISCOUNT = 0.25; // 25% off senior rate
    private static final int SENIOR_DISCOUNT_AGE = 60;  // strictly over 60

    public Member(int memberNumber,
                  String name,
                  LocalDate dateOfBirth,
                  MembershipType membershipType,
                  SwimmerLevel swimmerLevel,
                  SwimmerCategory swimmerCategory) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth must be in the past");
        }
        if (membershipType == null || swimmerLevel == null || swimmerCategory == null) {
            throw new IllegalArgumentException("Membership classifications must not be null");
        }
        this.memberNumber = memberNumber;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.membershipType = membershipType;
        this.swimmerLevel = swimmerLevel;
        this.swimmerCategory = swimmerCategory;
    }
    // ---------- Getters ----------
    public int getMemberNumber() {
        return memberNumber;
    }
    public String getName() {
        return name;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public MembershipType getMembershipType() {
        return membershipType;
    }
    public SwimmerLevel getSwimmerLevel() {
        return swimmerLevel;
    }
    public SwimmerCategory getSwimmerCategory() {
        return swimmerCategory;
    }

    // ---------- Setters (for US-03 update) ----------
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name must not be blank");
        this.name = name;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Date of birth must be in the past");
        this.dateOfBirth = dateOfBirth;
    }
    public void setMembershipType(MembershipType t){
        this.membershipType = t;
    }
    public void setSwimmerLevel(SwimmerLevel l){
        this.swimmerLevel = l;
    }
    public void setSwimmerCategory(SwimmerCategory c){
        this.swimmerCategory = c;
    }
    // ---------- Information Expert: age & fee ----------

    /** Age in whole years on the given date. */
    public int getAge(LocalDate date) {
        return Period.between(dateOfBirth, date).getYears();
    }

    /**
     * Yearly membership fee in DKK on the given date.
     * Date is a parameter (not LocalDate.now()) so tests are stable.
     *
     * Rules:
     *  - Passive: 500 DKK.
     *  - Active youth (<18): 1,000 DKK.
     *  - Active senior (>=18): 1,600 DKK.
     *  - Active senior over 60 (strictly >60): 1,600 * 0.75 = 1,200 DKK.
     */
    public int calculateFee(LocalDate date) {
        if (membershipType == MembershipType.PASSIVE) {
            return PASSIVE_FEE_DKK;
        }
        int age = getAge(date);
        if (age < 18) {
            return YOUTH_FEE_DKK;
        }
        if (age > SENIOR_DISCOUNT_AGE) {
            return (int) Math.round(SENIOR_FEE_DKK * (1 - SENIOR_DISCOUNT));
        }
        return SENIOR_FEE_DKK;
    }
    /** Convenience: does this member count as a junior on the given date? */
    public boolean isJunior(LocalDate date) {
        return getAge(date) < 18;
    }

    /** Convenience: is this member a competitive swimmer? */
    public boolean isCompetitive() {
        return swimmerCategory == SwimmerCategory.COMPETITIVE;
    }
    @Override
    public String toString() {
        return String.format("Member[#%d, %s, born %s, %s/%s/%s]",
                memberNumber, name, dateOfBirth,
                membershipType, swimmerLevel, swimmerCategory);
    }
}
