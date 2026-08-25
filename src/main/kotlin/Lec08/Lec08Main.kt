package Lec08

/*
 * 코틀린에서 함수를 다루는 방법
 *
 * 1. 함수 선언 문법
 * 2. default parameter
 * 3. named argument (parameter)
 * 4. 같은 타입의 여러 파라미터 받기 (가변인자)
 */

/*
 * 1. 함수 선언 문법
 *
 * public : 코틀린에서 생략 가능
 * fun : 코틀린에서 함수를 의미하는 키워드
 * max : 함수 이름
 * 매개변수명 : 매개변수: 타입
 * 함수의 반환 타입(Unit인 경우 생략 가능)
 * 함수 본문(중괄호 내부) : 함수 본문이 하나의 결과값이라면 중괄호를 벗기고 = 을 사용할 수 있다.
 *
 * 아래 함수의 경우 반환 타입이 반드시 Int이고, 함수에 중괄호 대신 = 을 사용했다.
 * 코틀린에서 함수의 반환 타입이 단일 타입이고, = 으로 함수 본문을 작성 시 스마트캐스트가 적용되어 함수의 반환 타입을 생략할 수도 있다.
 * 블록으로 함수 본문을 사용하는 경우에는 반환 타입이 Unit이 아니라면 반드시 명시적으로 작성해줘야 한다.
 *
 * 함수는 클래스 안에 있을 수도, 파일 최상단에 있을 수도 있다.
 * 또한, 한 파일 안에 여러 함수들이 있을 수도 있다.
 */
fun max(a: Int, b: Int): Int {
    return if (a > b) {
        a
    } else {
        b
    }
}

fun max2(a: Int, b: Int) = if (a > b) a else b

/*
 * 2. default parameter
 *
 * 자바에서는 함수에서 특정 파라미터가 대부분의 경우 고정된 값을 사용한다 할 때 메서드 Overloading을 통해 함수 사용을 편리하게 만들 수 있다.
 * 하지만 특정 파라미터가 많아질 수록 그만큼 만들어야하는 메서드 수가 늘어나는 단점이 있다.
 *
 * 코틀린은 이런 경우에 default parameter를 사용하여 파라미터 선언 시 해당 파라미터에 기본값을 지정할 수 있다.
 *
 * default parameter : 밖에서 파라미터를 넣어주지 않으면 기본값을 사용
 */
fun repeat(
    str: String,
    num: Int = 3,
    useNewLine: Boolean =  true
) {
    for (i in 1..num) {
        if (useNewLine) {
            println(str)
        } else {
            print(str)
        }
    }
}

/*
 * 3. named argument
 *
 * 위의 repeat 함수에서 num은 3 그대로 쓰고싶은데, useNewLine은 false를 쓰고 싶을 때?
 *      repeat("Hello World", 3, false)
 * 처럼 인자를 직접 넣는 방법이 있다.
 *
 * 근데 기껏 default parameter를 사용했는데, 굳이 다시 인자를 넣고 싶지 않다!
 * 이럴 때 사용하는 것이 named argument이다.
 * 어떤 파라미터에 어떤 값을 넣을 지 직접 명시하는 것이다.
 * 이 때 값이 들어가지 않은 파라미터는 default 값을 가지게 된다.
 *
 * named argument의 장점은 builder를 직접 만들지 않고도 builder의 장점을 가지게 된다.
 */
fun callRepeat() {
    repeat("Hello World", useNewLine = false)
}

fun printNameAndGender(name: String, gender: String) {
    println(name)
    println(gender)
}

fun callPrintNameAndGender() {
    // 위와 같이 동일한 타입이 연달아 선언된 파라미터를 가지는 함수의 경우 사용자가 실수로 파라미터 순서를 바꿔서 사용하더라도
    // 컴파일 수준에서는 해당 오류를 알아챌 수가 없다.
    printNameAndGender("MALE", "LJW")

    // 이 때, builder를 사용하면 각 파라미터의 이름이 명시되면서 값을 작성할 수 있게 되는데,
    // named argument를 사용함으로써 동일한 장점을 얻게 된다.
    printNameAndGender(
        name = "LJW",
        gender = "MALE"
    )
}

/*
 * 코틀린에서 자바 함수를 가져다 사용할 때에는 named argument를 사용할 수 없다.
 * 왜냐하면 코틀린에서 자바 코드를 쓸 때
 * JVM 상에서 Java가 바이트 코드로 변할 때 파라미터 이름을 보존하고있지 않기 때문이다.
 * native kotlin 함수에서만 사용 가능
 */

/*
 * 4. 같은 타입의 여러 파라미터 받기 (가변인자)
 *
 * 자바에서는 가변인자 타입 파라미터 선언 시 타입... 을 사용했지만
 * 코틀린에서는 vararg라는 키워드를 사용한다. (var argument)
 *
 */
fun printAll(vararg strings: String) {
    for (str in strings) {
        println(str)
    }
}

fun callPrintAll() {
    // 코틀린에서 가변인자 함수 호출 방법
    // #1 : 배열 사용
    val strs = arrayOf("A", "B", "C")
    printAll(strs) // 자바에서는 가변인자에 배열을 바로 사용했지만,
    printAll(*strs) // 코틀린에서는 가변인자에 배열을 사용할 때 앞에 *를 붙여야 한다. * : spread 연산자

    // #2 : 콤마 사용
    printAll("A", "B", "C")
}