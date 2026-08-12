package Lec04

data class KotlinMoney(
    val amount: Long
) {

    /* 코틀린에서 연산자를 직접 구현 */
    operator fun plus(other: KotlinMoney): KotlinMoney {
        return KotlinMoney(this.amount + other.amount)
    }

    override fun toString(): String {
        return "KotlinMoney(amount=$amount)"
    }

}