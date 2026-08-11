package Lec03

/*
 * 코틀린에서 Type을 다루는 방법
 *
 * 1. 기본 타입
 * 2. 타입 캐스팅
 * 3. 코틀린의 3가지 특이한 타입
 * 4. String Interpolation, String indexing
 */

/*
 * Byte Short Int Long Float Double
 * 코틀린에서는 "선언된 기본값"을 보고 타입을 추론한다.
 */
val n1 = 3 // Int
val n2 = 3L // Long
val n3 = 3.0f // Float
val n4 = 3.0 // Double

/*
 * 자바에서의 primitive type간의 변환은 암시적으로 이루어질 수 있다.
 * int number1 = 4;
 * long number2 = number1;
 *
 * System.out.println( number1 + number2 );
 * int 타입의 값이 long 타입으로 암시적으로 변환되었다.
 * 자바에서는 더 큰 타입으로는 암시적으로 변환이 가능하다.
 *
 *
 * 코틀린에서 primitive type간의 변환은 "명시적"으로 이루어져야 한다.
 */
val number1 = 4
val number2: Long = number1 // Initializer type mismatch: expected 'Long', actual 'Int'.

val number11 = 4
val number22: Long = number1.toLong() // 코틀린에서는 primitive type에 to변환타입() 메서드를 사용할 수 있다

val number111 = 3
val number222 = 5
val result1: Double = number111 / number222.toDouble()

/*
 * 변수가 nullable이라면 적절한 처리가 필요하다
 */
val number1111: Int? = 3
val number2222: Long = number1111.toLong() // number1111이 nullable이기 때문에 메서드 호출 막힘
val number2222: Long = number1111?.toLong() ?: 0L // Safe call과 Elvis 연산자를 사용하여 적절하게 처리

/*
 * 기본 타입이 아닌 일반 타입은 어떨까?
 */
fun printAgeIfPerson( obj: Any ) {

    if(obj is Person) { // 자바의 instanceof -> 코틀린에선 is 키워드
        val person = obj as Person // 자바의 강제 형변환 (Person) -> 코틀린에서는 as 연산자를 활용
        println(person.age)
    }

}

/*
 * 그리고 코틀린에선 is 키워드로 객체 타입 검증 시 as 키워드를 통한 형변환이 필수가 아니다
 */
fun printAgeIfPerson0( obj: Any ) {

    if(obj is Person) {
        println(obj.age) // obj가 Person 객체임을 검증했기 때문에 별도의 형변환 없이 바로 메서드 호출 가능 : 스마트캐스트
    }

    if(obj !is Person) { } // 코틀린에선 is키워드의 반대가 되는 의미인 !is가 사용 가능하다. (obj가 Person 객체가 아니라면)

}

/*
 * obj가 nullable이라면?
 */
fun printAgeIfPerson1( obj: Any? ) {

    val person = obj as Person;
    println(person.age) // NPE

    val person2/*:Person?*/ = obj as? Person; // as? : obj가 null이라면 Safe call처럼 식 전체가 null이 된다
    println(person2?.age) // person2가 nullable이기 때문에 메서드 호출 시 Safe call을 해야 한다

}

/*
 * value is Type -> value가 Type이면 true, 아니면 false
 * value !is Type -> value가 Type이면 false, 아니면 true
 * value as Type -> value가 Type이면 타입 캐스팅, 아니면 ClassCastException
 * value as? Type -> value가 Type이면 타입 캐스팅, value가 null이면 null, value가 Type이 아니면 null : 안전한 타입 형변환
 */

/*
 * 코틀린의 특이한 타입 3가지
 * - Any
 *  1. Java의 Object 역할 (모든 객체의 최상위 타입)
 *  2. 모든 Primitive Type의 최상위 타입도 Any이다
 *  3. Any 자체로는 null을 포함할 수 없다. null을 포함하고 싶다면 Any?로 표현
 *  4. Any에 equals / hashCode / toString 존재 (자바의 Object 클래스에 존재하는 기본 메서드들)
 * - Unit
 *  1. Unit은 Java의 void와 동일한 역할
 *  2. void와 다르게 Unit은 그 자체로 타입 인자로 사용 가능하다
 *  3. 함수형 프로그래밍에서 Unit은 단 하나의 인스턴스만을 갖는 타입을 의미한다.
 *     즉, 코틀린의 Unit은 실제 존재하는 타입이라는 것을 표현
 * - Nothing
 *  1. Nothing은 함수가 정상적으로 끝나지 않았다는 사실을 표현하는 역할
 *  2. 무조건 예외를 반환하는 함수 / 무한 루프 함수 등
 *      fun fail(message: String): Nothing {
 *          throw Exception(message)
 *      }
 */

/*
 * String interpolation / String indexing
 * 코틀린에서 문자열 타입을 어떻게 사용하는지
 *
 * 자바 예시 코드를 먼저 보자.
 * Person person = new Person("홍길동", 100)
 * String log = String.format("사람 이름은 %s이고, 나이는 %s세 입니다", person.getName(), person.getAge());
 *
 * StringBuilder b = new StringBuilder();
 * b.append("사람의 이름은");
 * b.append(person.getName());
 * b.append("이고, 나이는");
 * b.append(person.getAge());
 * b.append("세 입니다");
 *
 * 다음은 코틀린으로 위 문장을 표현해보자
 */
val person = Person("홍길동", 100)
val log = "사람의 이름은 ${person.name}이고, 나이는 ${person.age}세 입니다" // ${} 안에 값을 넣어 문자열 조합 가능

val name = "홍길동"
val age = 100
val log2 = "사람의 이름은 $name 이고, 나이는 $age 세 입니다" // 중괄호 생략 가능, 그러나 변수와 문자열을 붙여쓸 수는 없음

/*
 * ${} 사용 시 중괄호를 그대로 사용하는 것이
 * 1) 가독성
 * 2) 일괄 변환
 * 3) 정규식 활용
 * 측면에 더 도움이 된다
 */

/*
 * 코틀린에서는 여러 줄에 걸친 문자열을 작성할 때 큰따옴표 3개를 사용할 수 있다
 */
val str = """
    ABC
    DEF
    ${name}
    ${age}
""".trimIndent() // trimIndent() : 코드 가독성을 위해 각 줄의 맨 앞에 들어가는 탭 문자(Indentation)를 제거해줌

/*
 * 문자열에서 특정 문자를 가져오는 것도 자바와 차이가 있다.
 *
 * 자바 코드 예시
 * String str = "ABCDE";
 * char ch = str.charAt(1);
 *
 * 코틀린 코드
 */
val str2 = "ABCDE"
val ch2 = str2[1] // 파이썬같음