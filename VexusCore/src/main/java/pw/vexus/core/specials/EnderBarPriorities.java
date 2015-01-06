package pw.vexus.core.specials;

public enum EnderBarPriorities {
    REGION_INFO(1),
    ANNOUNCER_BAR(5),
    TELEPORT(10),
    PVP(15);
    private final int priority;

    EnderBarPriorities(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
