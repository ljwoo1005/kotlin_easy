package Lec04;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JavaMoney implements Comparable<JavaMoney> {

    private final long amount;

    public JavaMoney(long amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(@NotNull JavaMoney o) {
        return Long.compare(amount, o.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JavaMoney javaMoney = (JavaMoney) o;
        return amount == javaMoney.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

    /* 자바에서 연산자를 직접 정의 */
    public JavaMoney plus(JavaMoney other) {
        return new JavaMoney(this.amount + other.amount);
    }

    @Override
    public String toString() {
        return "JavaMoney{" +
                "amount=" + amount +
                '}';
    }
}
