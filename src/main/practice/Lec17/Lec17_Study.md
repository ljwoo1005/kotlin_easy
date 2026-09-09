# [Lec17] 코틀린에서 람다를 다루는 방법 (학습 가이드)

본 문서는 **Lec17 강의 노트(`Lec17Main.kt`, `Lec17Main.java`)** 및 제작된 **실습 문제(Quiz01 ~ Quiz03)**를 완벽하게 이해하고 실무에 적용할 수 있도록 설계된 심화 교육용 지식 자산 문서입니다.

---

## 📌 목차
1. [자바에서 람다를 다루기 위한 눈물겨운 노력과 한계](#1-자바에서-람다를-다루기-위한-눈물겨운-노력과-한계)
2. [코틀린의 혁신: 함수는 1급 시민(First-Class Citizen)이다](#2-코틀린의-혁신-함수는-1급-시민first-class-citizen이다)
3. [고차 함수(Higher-Order Function)와 코틀린 관용 문법](#3-고차-함수higher-order-function와-코틀린-관용-문법)
4. [클로저(Closure)의 비밀과 가변 변수(var) 포획(Capture)](#4-클로저closure의-비밀과-가변-변수var-포획capture)
5. [자원 관리의 진화: Closeable.use 패턴 파헤치기](#5-자원-관리의-진화-closeableuse-패턴-파헤치기)
6. [실습 퀴즈(Quiz01~03) 심층 분석 및 해설](#6-실습-퀴즈quiz0103-심층-분석-및-해설)
7. [핵심 요약 치트시트 (Cheat Sheet)](#7-핵심-요약-치트시트-cheat-sheet)

---

## 1. 자바에서 람다를 다루기 위한 눈물겨운 노력과 한계

### (1) 메서드 중복과 파라미터 폭발의 역사
과일가게 프로그램을 작성한다고 가정해 봅시다:
- "사장님, 사과만 골라주세요" -> `findApples(fruits)`
- "사장님, 바나나만 골라주세요" -> `findBananas(fruits)`
- "사장님, 사과랑 바나나 같이 보여주세요" -> `findFruitsByName(fruits, name)`
- "사과인데 1,200원 안 넘는 것만 주세요" -> ???
- "10,000원 이하 수박이랑 1,000원 이상 바나나 같이 보여주세요" -> ???

조건이 추가될 때마다 메서드를 새로 만들거나 파라미터를 무한정 늘리는 것은 불가능합니다.

### (2) 인터페이스와 익명 클래스의 도입 (Java의 1차 해법)
이를 해결하기 위해 Java 개발자들은 인터페이스와 익명 클래스를 도입했습니다:
```java
public interface FruitFilter {
    boolean isSelected(Fruit fruit);
}

// 호출 지점:
List<Fruit> results = filterFruits(fruits, new FruitFilter() {
    @Override
    public boolean isSelected(Fruit fruit) {
        return fruit.getName().equals("사과") && fruit.getPrice() <= 1200;
    }
});
```
메서드 폭발은 막았지만, 코드가 매우 장황하고 가독성이 떨어집니다.

### (3) Java 8 람다와 `Predicate`의 등장 (Java의 2차 해법)
Java 8에 이르러 단일 추상 메서드(SAM)를 갖는 인터페이스를 람다 표현식으로 치환할 수 있게 되었습니다:
```java
List<Fruit> results = filterFruits(fruits, fruit -> fruit.getName().equals("사과"));
```
또한 `Predicate<T>`, `Function<T, R>`, `Consumer<T>` 등 수많은 함수형 인터페이스를 표준 라이브러리에 내장했습니다.

### (4) Java가 넘지 못한 근본적인 2가지 벽

#### 1) 함수는 여전히 "2급 시민(Second-Class Citizen)"이다
- Java에서 람다는 진짜 함수가 아닙니다. 람다는 **익명 구현 객체**에 불과합니다.
- 함수 자체를 독립된 타입으로 변수에 할당하거나 파라미터로 넘길 수 없으며, 항상 어떤 인터페이스(`Predicate` 등)의 인스턴스라는 껍데기를 뒤집어써야만 합니다.

#### 2) `effectively final`의 족쇄: 외부 변수 수정 불가
Java 람다는 외부의 지역 변수를 참조할 때 **"상수(final)이거나 사실상 상수(effectively final)"**인 변수만 사용할 수 있습니다:
```java
int budget = 5000;
budget = 3000; // 가변 변수 수정

// 컴파일 에러 발생!
// "Variable used in lambda expression should be final or effectively final"
fruits.removeIf(fruit -> fruit.getPrice() > budget);
```
람다 내부에서 외부 변수를 누적하거나 수정하는 작업은 원천 차단되어 개발자들을 끊임없이 괴롭혔습니다.

---

## 2. 코틀린의 혁신: 함수는 1급 시민(First-Class Citizen)이다

코틀린은 태생부터 함수형 프로그래밍을 일급으로 지원하도록 설계되었습니다.

### (1) 1급 시민(First-Class Citizen)이란?
어떤 개체가 프로그래밍 언어에서 다음 3가지 조건을 충족할 때 "1급 시민"이라고 부릅니다:
1. **변수에 할당할 수 있다.**
2. **함수의 인자(Parameter)로 넘길 수 있다.**
3. **함수의 반환값(Return Value)으로 반환할 수 있다.**

Java에서는 오직 '객체(Object)'만이 1급 시민이었지만, **코틀린에서는 '함수(Function)'도 당당한 1급 시민**입니다.

### (2) 함수 타입 선언 문법: `(파라미터 타입...) -> 반환 타입`
코틀린은 인터페이스를 새로 선언하지 않고도 언어 차원에서 함수의 시그니처를 타입으로 표현합니다:
```kotlin
val noArg: () -> Unit                    // 인자 없음, 반환값 없음
val printNum: (Int) -> Unit              // Int 1개 인자, 반환값 없음
val sum: (Int, Int) -> Int               // Int 2개 인자, Int 반환
val predicate: (Fruit) -> Boolean        // Fruit 인자, Boolean 반환
val nullableReturn: (String) -> String?  // 널 반환 가능 함수
val nullableFunc: ((Int) -> Int)?        // 함수 자체가 null일 수 있음
```

### (3) 람다(익명 함수)를 선언하는 두 가지 방법
```kotlin
// 방법 1: fun 키워드를 사용한 익명 함수 (명시적 return 사용)
val isApple1: (Fruit) -> Boolean = fun(fruit: Fruit): Boolean {
    return fruit.name == "사과"
}

// 방법 2: 중괄호와 화살표를 사용하는 람다식 (추천!)
val isApple2: (Fruit) -> Boolean = { fruit: Fruit -> fruit.name == "사과" }
```

### (4) 변수에 할당된 함수를 호출하는 두 가지 방법
```kotlin
// 1. 일반 함수처럼 소괄호로 직접 호출
val r1 = isApple1(apple)

// 2. 함수 객체의 invoke() 메서드를 통해 명시적 호출
val r2 = isApple2.invoke(apple)
```
> 💡 **참고**: `isApple(apple)`을 실행하면 코틀린 컴파일러는 내부적으로 `isApple.invoke(apple)`을 호출합니다.

### (5) 단일 파라미터의 특권: `it`
람다식의 매개변수가 단 **하나**뿐이라면, 매개변수 선언부(`fruit ->`)를 완전히 생략하고 암시적 예약어인 **`it`**을 사용할 수 있습니다:
```kotlin
// 기본 형태
val isApple: (Fruit) -> Boolean = { fruit -> fruit.name == "사과" }

// it 키워드 활용 (매우 간결함!)
val isApple: (Fruit) -> Boolean = { it.name == "사과" }
```

### (6) 람다식의 반환 규칙
람다식 블록(`{ }`)은 여러 줄로 작성할 수 있으며, **별도의 `return` 키워드 없이 "마지막 줄의 평가 결과"가 곧 람다의 반환값**이 됩니다.
```kotlin
val calculate: (Int, Int) -> Int = { a, b ->
    println("계산 시작: a=$a, b=$b")
    a * 2 + b // 마지막 줄의 평가 결과가 반환됨!
}
```
> ⚠️ **주의**: 람다 내부에서 무심코 `return`을 쓰면 람다를 감싸고 있는 바깥 함수 전체가 리턴되어 버리는 **비지역 반환(Non-local return)**이 발생하거나 컴파일 오류가 납니다. 람다의 결과는 항상 마지막 줄에 위치시키세요!

---

## 3. 고차 함수(Higher-Order Function)와 코틀린 관용 문법

### (1) 고차 함수(Higher-Order Function)란?
- 하나 이상의 **함수를 파라미터로 받거나**, **함수를 결과로 반환하는 함수**를 의미합니다.
```kotlin
fun filterFruits(fruits: List<Fruit>, filter: (Fruit) -> Boolean): List<Fruit> {
    val results = mutableListOf<Fruit>()
    for (fruit in fruits) {
        if (filter(fruit)) { // 파라미터로 받은 함수를 직접 실행
            results.add(fruit)
        }
    }
    return results
}
```

### (2) 후행 람다(Trailing Lambda) 문법의 마법
코틀린 문법 중 가장 사랑받는 규칙입니다:
> **"함수의 마지막 파라미터가 함수 타입인 경우, 소괄호 바깥으로 람다 중괄호 블록을 분리할 수 있다!"**

```kotlin
// 1. 소괄호 안에 람다를 넣은 일반적인 모습
filterFruits(fruits, { it.name == "사과" })

// 2. 후행 람다 문법 적용: 소괄호 바깥으로 중괄호 탈출!
filterFruits(fruits) { it.name == "사과" }

// 3. 만약 파라미터가 함수 딱 하나뿐이라면 소괄호() 자체도 생략 가능!
run { println("Hello, Kotlin!") }
```
이 후행 람다 문법 덕분에 코틀린은 라이브러리 개발자가 언어 내장 제어문(if, while 등)처럼 자연스러운 **DSL(도메인 특화 언어)**을 만들 수 있게 해줍니다.

---

## 4. 클로저(Closure)의 비밀과 가변 변수(var) 포획(Capture)

### (1) 클로저(Closure)란 무엇인가?
> **클로저(Closure)**: 람다가 생성되는 시점에 **자신이 참조하고 있는 외부 스코프의 변수들을 모두 포획(Capture)하여 유지하는 데이터 구조**.

### (2) Java vs Kotlin: 가변 변수(`var`)의 자유
앞서 Java에서는 람다 외부의 `var` 변수를 수정할 수 없다고 배웠습니다.  
하지만 코틀린에서는 아무런 문제 없이 동작합니다:

```kotlin
fun pickFruitsUnderBudget(fruits: List<Fruit>, budget: Int): List<Fruit> {
    var currentSpent = 0 // 외부의 가변 지역 변수

    // 람다 내부에서 외부의 var 변수를 읽고 심지어 값을 변경(수정)함!
    val selected = filterFruits(fruits) { fruit ->
        if (currentSpent + fruit.price <= budget) {
            currentSpent += fruit.price // 🌟 var 변수 직접 수정! (Java에서는 불가능)
            true
        } else {
            false
        }
    }

    println("총 지출 금액: $currentSpent")
    return selected
}
```

### (3) 컴파일러는 어떻게 이것을 가능하게 했을까? (디컴파일 분석)
Java 가상 머신(JVM)의 제약은 동일한데, 코틀린은 도대체 어떻게 외부 가변 변수를 수정한 걸까요?  
비밀은 컴파일러가 만들어주는 **`Ref` 래퍼 객체(Wrapper Object)**에 있습니다.

코틀린 코드를 Java로 디컴파일해 보면 다음과 같은 코드가 숨겨져 있습니다:
```java
// 코틀린 컴파일러가 생성한 자바 바이트코드의 실체
final IntRef currentSpent = new IntRef(); // 힙 영역에 Int 래퍼 객체 생성!
currentSpent.element = 0;

List selected = filterFruits(fruits, new Function1() {
    @Override
    public Object invoke(Object obj) {
        Fruit fruit = (Fruit) obj;
        if (currentSpent.element + fruit.getPrice() <= budget) {
            currentSpent.element += fruit.getPrice(); // 래퍼 객체 내부의 필드를 수정!
            return true;
        }
        return false;
    }
});
```
- 컴파일러가 원시 타입 변수를 힙(Heap)에 존재하는 `IntRef` 객체로 자동 포장합니다.
- `currentSpent` 참조 자체는 `final`이지만, 래퍼 객체 내부의 `.element` 필드를 변경함으로써 JVM의 `final` 제약을 우회하면서도 완벽한 클로저를 제공하는 것입니다.

---

## 5. 자원 관리의 진화: Closeable.use 패턴 파헤치기

### (1) Java 7: try-with-resources
Java 7 이전에는 `finally` 블록에서 `null` 체크와 `close()`를 일일이 호출하다가 예외가 삼켜지는 끔찍한 코드를 작성했습니다. Java 7에서 `AutoCloseable`과 try-with-resources가 도입되었습니다:
```java
try (BufferedReader br = new BufferedReader(new FileReader(path))) {
    return br.readLine();
}
```

### (2) 코틀린의 접근법: 인라인 고차 확장 함수 `use`
코틀린은 언어 문법을 새로 만들지 않고, **확장 함수 + 람다(고차 함수)**의 조합으로 try-with-resources를 완벽히 대체했습니다:
```kotlin
fun readFile(path: String): String? {
    return BufferedReader(FileReader(path)).use { reader ->
        reader.readLine()
    }
}
```

### (3) `use` 함수의 실제 구현 구조
표준 라이브러리(`kotlin.io.Closeable.kt`)에 선언된 `use`의 원형을 살펴봅시다:
```kotlin
public inline fun <T : Closeable?, R> T.use(block: (T) -> R): R {
    var exception: Throwable? = null
    try {
        return block(this) // 수신 객체(T)를 파라미터로 넘겨 람다 실행
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        // 정상 완료되든 예외가 터지든 안전하게 자원 해제
        this.closeFinally(exception)
    }
}
```
- `Closeable` 구현체라면 무엇이든 점 표기법(`.use { }`)으로 즉시 안전한 스코프를 열 수 있습니다.
- `inline` 키워드가 붙어 있어 함수 호출 오버헤드나 람다 객체 생성 비용이 전혀 발생하지 않습니다.
- 람다 내부에서 던져진 예외와 `close()` 도중 발생한 예외까지 완벽히 추적하여 억제된 예외(`suppressedExceptions`)로 등록해 줍니다.

---

## 6. 실습 퀴즈(Quiz01~03) 심층 분석 및 해설

이번에 제작된 3세트 실습 문제는 위 개념들을 단계별로 체화하도록 정교하게 설계되었습니다.

### 📝 Quiz01 (Set 1: 기초 문법 체화 / Level: 쉬움)
- **학습 목표**: 함수 1급 시민, 함수 타입 `(T) -> R`, `it`, 소괄호 vs `invoke()` 호출, 기초 고차 함수.
- **핵심 코드 해설**:
```kotlin
// 1. 단일 매개변수 it을 사용한 람다 변수
private val isExpensiveFruit: (Quiz17Fruit) -> Boolean = {
    it.price >= 3_000
}

// 2. 함수 타입을 인자로 받아 실행하는 고차 함수
private fun calculate(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

// 3. 소괄호 직접 호출과 invoke() 호출의 1:1 비교
private fun executeTwice(message: String, action: (String) -> String): Pair<String, String> {
    val first = action(message)        // 일반 함수처럼 호출
    val second = action.invoke(message) // invoke() 명시적 호출
    return Pair(first, second)
}
```
- **체화 포인트**: 코틀린에서 함수는 일반 변수처럼 다룰 수 있으며, `action()`과 `action.invoke()`가 본질적으로 동일함을 이해합니다.

---

### 📝 Quiz02 (Set 2: Java to Idiomatic Kotlin / Level: 보통 - Case A)
- **학습 목표**: Java의 `Predicate`/SAM 인터페이스 제거, 제네릭 고차 함수 구현, 코틀린 클로저의 가변 변수 포획.
- **핵심 코드 해설**:
```kotlin
// 1. 제네릭 고차 함수 (임의의 타입 T 필터링)
private fun <T> filterList(items: List<T>, predicate: (T) -> Boolean): List<T> {
    val results = mutableListOf<T>()
    for (item in items) {
        if (predicate(item)) {
            results.add(item)
        }
    }
    return results
}

// 2. 외부 var 변수(totalSpent)를 포획하여 수정하는 클로저 구현
private fun filterAndAccumulatePrice(fruits: List<Quiz17Fruit>, budget: Int): Pair<List<Quiz17Fruit>, Int> {
    var totalSpent = 0 // 외부 가변 변수

    val selectedFruits = filterList(fruits) { fruit ->
        if (totalSpent + fruit.price <= budget) {
            totalSpent += fruit.price // 🌟 람다 내부에서 외부 var 수정!
            true
        } else {
            false
        }
    }

    return Pair(selectedFruits, totalSpent)
}
```
- **체화 포인트**: Java에서는 절대 컴파일되지 않는 외부 `var` 변경 코드가 코틀린에서는 클로저와 컴파일러의 래퍼 객체 덕분에 부드럽게 작동함을 직접 체험합니다.

---

### 📝 Quiz03 (Set 3: 실전 미니 시나리오 / Level: 보통~약간 응용)
- **학습 목표**: `Closeable.use` 패턴을 응용한 안전 세션 고차 함수 작성, 비즈니스 할인 정책 람다 주입.
- **핵심 코드 해설**:
```kotlin
// 1. 예외가 발생해도 반드시 close()를 호출하는 세션 래퍼 고차 함수
private fun <R> withOrderSession(sessionId: String, block: (Quiz17OrderSession) -> R): R {
    val session = Quiz17OrderSession(sessionId)
    return try {
        block(session) // 전달받은 람다 실행
    } finally {
        session.close() // 성공이든 예외든 100% 해제 보장
    }
}

// 2. 전략 패턴을 람다 함수 타입으로 단순화한 할인 계산
private fun calculateFinalAmount(
    items: List<Quiz17CartItem>,
    discountPolicy: (Quiz17CartItem) -> Int
): Int {
    var totalAmount = 0
    for (item in items) {
        val discount = maxOf(0, discountPolicy(item))
        val discountedPrice = maxOf(0, item.price - discount)
        totalAmount += discountedPrice
    }
    return totalAmount
}
```
- **체화 포인트**: 자바의 번거로운 인터페이스 전략 패턴이나 자원 누수 위험을, 코틀린 고차 함수를 통해 우아하고 안전하게 해결하는 실무 패턴을 체화합니다.

---

## 7. 핵심 요약 치트시트 (Cheat Sheet)

| 구분 | Java | Kotlin |
| :--- | :--- | :--- |
| **함수의 위상** | 2급 시민 (항상 객체/인터페이스로 감싸야 함) | **1급 시민** (그 자체로 값이며 변수/파라미터 전달 가능) |
| **함수 타입 표현** | `@FunctionalInterface` 인터페이스 필수 선언 | **`(ParamType) -> ReturnType`** 언어 내장 타입 |
| **단일 파라미터 참조** | `x -> x.getName()` | **`it.name`** |
| **람다 호출 문법** | `filter(list, x -> ...)` | **`filter(list) { ... }`** (후행 람다 문법) |
| **람다 반환값** | 명시적 `return` 필요 | **마지막 라인의 평가 결과가 반환값** (return 지양) |
| **외부 변수 참조** | `final` 또는 `effectively final`만 가능 | **`var` 가변 변수도 자유롭게 포획 및 수정 가능 (클로저)** |
| **자원 관리** | `try (Resource r = ...) { ... }` 문법 구문 | **`resource.use { r -> ... }`** (고차 확장 함수) |
| **함수 호출 방식** | `func.apply(x)` | **`func(x)`** 또는 **`func.invoke(x)`** |

---

> 💡 **학습 완료 후 추천 실습**:
> 1. `Quiz01_Problem.kt` ~ `Quiz03_Problem.kt` 파일을 열어 `TODO()` 부분을 스스로 지우고 직접 타이핑해 보세요.
> 2. `main()` 함수를 실행하여 테스트 통과 녹색 체크(`✅`)를 확인하세요.
> 3. 본 학습 가이드와 `Solution` 파일의 하단 주석을 대조하며 개념을 100% 본인의 것으로 만드세요!
