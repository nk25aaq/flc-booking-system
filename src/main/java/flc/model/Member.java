package flc.model;

public class Member {
    private final String memberId;
    private final String name;

    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        return memberId.equals(((Member) o).memberId);
    }

    @Override
    public int hashCode() {
        return memberId.hashCode();
    }

    @Override
    public String toString() {
        return memberId + " - " + name;
    }
}
