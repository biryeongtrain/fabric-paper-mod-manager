package kim.biryeong.manager.impl.registry;

public interface RegistrySyncExtension<T> {
    void manager$setServerEntry(T obj, boolean value);
    boolean manager$isServerEntry(T obj);

    Status manager$getStatus();
    void manager$setStatus(Status status);
    boolean manager$updateStatus(Status status);
    void manager$clearStatus();

    void manager$reorderEntries();

    enum Status {
        VANILLA(0),
        WITH_SERVER_ONLY(1),
        WITH_MODDED(2);
        ;
        private final int priority;

        Status(int priority) {
            this.priority = priority;
        }
    }
}
