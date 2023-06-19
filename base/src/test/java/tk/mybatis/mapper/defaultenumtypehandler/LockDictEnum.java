package tk.mybatis.mapper.defaultenumtypehandler;

/**
 * @author liuzh
 */
public enum LockDictEnum implements Dict {
    locked(1),
    unlocked(2);

    private final int value;

    LockDictEnum(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }

}
