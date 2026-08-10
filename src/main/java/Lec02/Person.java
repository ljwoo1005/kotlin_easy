package Lec02;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Person {

    private final String name;

    public Person( String name ) {
        this.name = name;
    }

    @Nullable
    public String getNameNullable() {
        return name;
    }

    @NotNull
    public String getNameNotNull() {
        return name;
    }

}
