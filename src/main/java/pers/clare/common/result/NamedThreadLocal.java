package pers.clare.common.result;


import org.springframework.util.Assert;

public class NamedThreadLocal<T> extends ThreadLocal<T> {
    private final String name;

    public NamedThreadLocal(String name) {
        Assert.hasLength(name, "Name must not be empty");
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
