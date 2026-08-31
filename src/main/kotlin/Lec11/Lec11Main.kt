package Lec11

/*
 * 코틀린에서 접근 제어를 다루는 방법
 *
 * 1. 자바와 코틀린의 가시성 제어
 * 2. 코틀린 파일의 접근 제어
 * 3. 다양한 구성요소의 접근 제어
 * 4. 자바와 코틀린을 함께 사용할 경우 주의할 점
 */

/*
 * 1. 자바와 코틀린의 가시성 제어
 *
 * 자바의 접근 제어
 *  - public : 모든 곳에서 접근 가능
 *  - protected : 같은 패키지 또는 하위 클래스에서만 접근 가능
 *  - default : 같은 패키지 내에서만 접근 가능
 *  - private : 선언된 클래스 내에서만 접근 가능
 *
 * 코틀린의 접근 제어
 *  - public : 모든 곳에서 접근 가능
 *  - protected : "선언된 클래스" 또는 하위 클래스에서만 접근 가능 -> 같은 패키지가 선언된 클래스로 변경됨
 *  - internal : 같은 모듈에서만 접근 가능 -> default가 아예 사라짐
 *  - private : 선언된 클래스 내에서만 접근 가능
 *
 * 코틀린에서는 패키지를 namespace를 관리하기 위한 용도로만 사용!
 * 가시성 제어에는 사용되지 않는다.
 *
 * 모듈 : 한 번에 컴파일되는 코틀린 코드
 * IDEA Module, Maven Project, Gradle Source Set, Ant Task <kotlinc>의 호출로 컴파일 파일의 집합
 *
 * 자바의 기본 접근 지시어 : default
 * 코틀린의 기본 접근 지시어 : public
 *
 */

/*
 * 2. 코틀린 파일의 접근 제어
 *  - public : 기본값. 어디서든 접근할 수 있다.
 *  - protected : 파일(최상단)에는 "사용 불가능"
 *  - internal : 같은 모듈에서만 접근 가능
 *  - private : 같은 파일 내에서만 접근 가능
 *
 * 코틀린은 .kt 파일에 변수, 함수, 클래스 여러 개를 바로 만들 수 있다.
 */
public val a = 3
internal fun add(a: Int, b:Int) = a + b
private class cls { }
protected val b = 3 // err. 파일에서는 사용 불가능

/*
 * 3. 다양한 구성요소의 접근 제어
 *
 * 클래스 안의 멤버
 *  - public : 모든 곳에서 접근 가능
 *  - protected : 선언된 클래스 또는 하위 클래스에서만 접근 가능
 *  - internal : 같은 모듈에서만 접근 가능
 *  - private : 선언된 클래스 내에서만 접근 가능
 *
 * 생성자도 멤버와 마찬가지이다.
 * 단! 생성자에 접근 지시어를 사용하기 위해선 constructor를 반드시 작성해야 한다!
 */
class Cat private constructor() { }

/*
 * 자바에서 유틸성 코드를 만들 때
 * abstract class + private constructor를 사용해서 인스턴스화를 막을 수 있다.
 * 코틀린에서도 마찬가지로 작성할 수 있지만
 * 파일 최상단에 바로 유틸 함수를 작성하면 편하다!
 * StringUtils.kt 참고
 */

/*
 * 프로퍼티의 가시성 범위도 동일하다.
 * 단! 사용하는 방법은 여러가지이다.
 *
 * 1. primary constructor에서 변수 선언문에 접근 제어자를 같이 붙이는 경우
 */
class Car(
    internal val name: String, // name에 대한 getter를 internal로 만들고 싶다
    private var owner: String, // owner에 대한 getter, setter를 private으로 만들고 싶다
    _price: Int
) {

    /*
     * 2. custom getter나 custom setter 중 하나만 접근 제어자를 붙이는 경우
     */
    var price = _price
        private set(value) { // _price에 대한 setter를 private으로 만들고 싶다 -> getter는 public인 상태
            price = value
        }

}

/*
 * 4. 자바와 코틀린을 함께 사용할 경우 주의할 점
 *
 * internal은 바이트 코드 상 public이 된다.
 * 때문에 자바 코드에서는 코틀린 모듈의 internal 코드를 가져올 수 있다.
 *
 * 코틀린의 protected와 자바의 protected는 다르다.
 * 자바는 같은 패키지의 코틀린 protected 멤버에 접근할 수 있다.
 */