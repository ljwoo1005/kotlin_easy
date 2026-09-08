# [Lec04] 코틀린에서 연산자를 다루는 방법 (학습 가이드)

본 문서는 **Lec04 강의 노트(`Lec04Main.kt`, `KotlinMoney.kt`, `Lec04Main.java`, `JavaMoney.java`)** 및 제작된 **실습 문제(Quiz01 ~ Quiz03)**를 완벽하게 이해하고 체화할 수 있도록 설계된 심화 교육용 지식 자산 문서입니다.

---

## 📌 목차
1. [연산자를 바라보는 코틀린의 철학: 문법 설탕(Syntactic Sugar)과 객체지향의 융합](#1-연산자를-바라보는-코틀린의-철학-문법-설탕syntactic-sugar과-객체지향의-융합)
2. [단항 / 산술 / 대입 연산자: 자바와의 호환성과 차이점](#2-단항--산술--대입-연산자-자바와의-호환성과-차이점)
3. [동등성(Equality) vs 동일성(Identity): `==` vs `===`](#3-동등성equality-vs-동일성identity--vs-)
4. [비교 연산자(`>`, `<`, `>=`, `<=`)와 `Comparable` 인터페이스 연계](#4-비교-연산자---와-comparable-인터페이스-연계)
5. [논리 연산자와 단락 평가(Short-Circuit / Lazy Evaluation)](#5-논리-연산자와-단락-평가short-circuit--lazy-evaluation)
6. [코틀린 고유의 특이 연산자 (범위 `..`, 포함 `in`, 인덱싱 `[]`)](#6-코틀린-고유의-특이-연산자-범위--포함-in-인덱싱-)
7. [연산자 오버로딩 (Operator Overloading)의 원리와 `operator` 키워드](#7-연산자-오버로딩-operator-overloading의-원리와-operator-키워드)
8. [실습 퀴즈(Quiz01~03) 심층 분석 및 해설](#8-실습-퀴즈quiz0103-심층-분석-및-해설)
9. [핵심 요약 치트시트 (Cheat Sheet)](#9-핵심-요약-치트시트-cheat-sheet)

---

## 1. 연산자를 바라보는 코틀린의 철학: 문법 설탕(Syntactic Sugar)과 객체지향의 융합

자바(Java)에서는 기본 타입(Primitive Types: `int`, `long` 등)에 대해서만 연산자(`+`, `-`, `*`, `/` 등)를 사용할 수 있었고, 객체(Object)에 대해서는 연산자 사용이 엄격히 금지되었습니다. 이로 인해 도메인 객체 간의 연산(예: 금액 합산, 벡터 계산, 시간 가산 등)을 표현할 때 장황한 메서드 호출 체이닝(`money1.plus(money2).minus(discount)`)을 강제당했습니다.

반면 코틀린(Kotlin)은 **"개발자가 도메인 수식을 자연스럽게 코드로 옮길 수 있어야 한다"**는 철학을 채택했습니다.
코틀린의 모든 연산자는 사실상 **미리 정해진 이름의 멤버 함수(컨벤션)를 호출하는 문법적 설탕(Syntactic Sugar)**입니다.

```kotlin
// 사용자가 작성하는 코드 (연산자)
val total = money1 + money2

// 컴파일러가 실제로 변환하여 실행하는 코드 (컨벤션 함수)
val total = money1.plus(money2)
```

이러한 접근 방식을 통해 코틀린은 C++처럼 임의의 기호로 난해한 연산자를 무분별하게 만드는 부작용을 방지하면서도, 자바의 경직된 한계를 벗어나 비즈니스 가독성을 극대화합니다.

---

## 2. 단항 / 산술 / 대입 연산자: 자바와의 호환성과 차이점

기본적인 단항, 산술, 산술 대입 연산자는 Java와 100% 동일하게 동작합니다.

| 연산자 분류 | 기호 | 의미 | 대응하는 코틀린 컨벤션 메서드 |
| :--- | :--- | :--- | :--- |
| **단항 연산자** | `++`, `--` | 1 증가, 1 감소 (전위/후위) | `inc()`, `dec()` |
| **단항 부호** | `+`, `-`, `!` | 양수, 음수, 논리 부정 | `unaryPlus()`, `unaryMinus()`, `not()` |
| **산술 연산자** | `+`, `-`, `*`, `/`, `%` | 덧셈, 뺄셈, 곱셈, 나눗셈, 나머지 | `plus()`, `minus()`, `times()`, `div()`, `rem()` |
| **산술 대입** | `+=`, `-=`, `*=`, `/=`, `%=` | 연산 후 변수에 재할당 | `plusAssign()`, `minusAssign()` 등 |

```kotlin
var count = 10
count++     // 11
count += 5  // 16
val remainder = count % 3 // 1
```

---

## 3. 동등성(Equality) vs 동일성(Identity): `==` vs `===`

코틀린을 배울 때 자바 개발자가 가장 크게 헷갈리면서도 가장 감탄하는 부분이 바로 **동등성과 동일성 연산자의 재정의**입니다.

### (1) 개념 정의
- **동일성 (Identity)**: 두 변수가 메모리 상에서 **완전히 동일한 객체 인스턴스(동일 주소)**를 가리키고 있는가?
- **동등성 (Equality)**: 두 객체가 메모리 상의 주소는 다를지라도 **내부 상태(값)가 동등**한가?

### (2) Java vs Kotlin 연산자 비교

| 구분 | 의미 | Java 문법 | Kotlin 문법 |
| :--- | :--- | :--- | :--- |
| **동일성 (주소 비교)** | 같은 인스턴스인가? | `a == b` | `a === b` |
| **동등성 (값 비교)** | 내용(상태)이 같은가? | `a.equals(b)` | `a == b` |
| **부정 동일성** | 주소가 다른가? | `a != b` | `a !== b` |
| **부정 동등성** | 내용이 다른가? | `!a.equals(b)` | `a != b` |

### (3) 코틀린의 `==` 연산자가 내부적으로 동작하는 원리 (Safe Equals)
자바에서는 `a.equals(b)`를 호출할 때 `a`가 `null`이면 악명 높은 `NullPointerException`이 발생했습니다.
하지만 코틀린에서 `a == b`를 작성하면 컴파일러가 내부적으로 아래와 같이 안전한 널 처리를 포함하여 실행합니다:

```kotlin
// 코틀린의 'a == b' 내부 변환 메커니즘
a?.equals(b) ?: (b === null)
```
1. `a`가 `null`이 아니면 `a.equals(b)`를 호출합니다.
2. `a`가 `null`인 경우, `b` 역시 `null`(`b === null`)인지를 검사합니다. (둘 다 null이면 true, 하나만 null이면 false)
3. 따라서 개발자는 별도의 널 방어 코드 없이 안심하고 `a == b`를 사용할 수 있습니다.

---

## 4. 비교 연산자(`>`, `<`, `>=`, `<=`)와 `Comparable` 인터페이스 연계

### (1) Java의 한계
Java에서는 객체의 크기를 비교할 때 `Comparable` 인터페이스의 `compareTo()` 메서드를 호출하고, 그 결과가 양수/음수/0인지를 수동으로 확인해야 했습니다.

```java
// Java: 객체 크기 비교의 불편함
if (money1.compareTo(money2) > 0) {
    System.out.println("money1이 더 큽니다");
}
```

### (2) 코틀린의 우아함: 비교 연산자 자동 바인딩
코틀린에서는 객체가 `Comparable<T>` 인터페이스를 구현하고 있거나 `compareTo` 연산자 함수를 가지고 있다면, 일반 숫자처럼 부등호 기호를 바로 사용할 수 있습니다.

```kotlin
// Kotlin: 직관적인 비교 연산자
if (money1 > money2) {
    println("money1이 더 큽니다")
}
```

| 코틀린 코드 | 컴파일러 변환 형태 |
| :--- | :--- |
| `a > b` | `a.compareTo(b) > 0` |
| `a < b` | `a.compareTo(b) < 0` |
| `a >= b` | `a.compareTo(b) >= 0` |
| `a <= b` | `a.compareTo(b) <= 0` |

---

## 5. 논리 연산자와 단락 평가(Short-Circuit / Lazy Evaluation)

논리 연산자 `&&`(AND), `||`(OR), `!`(NOT)는 자바와 완전히 동일하게 동작합니다.
특히 **단락 평가(Lazy Evaluation / Short-circuit)**를 지원하므로 성능 최적화와 널 안전성 검사에 유용합니다.

- **`A || B`**: `A`가 `true`이면 `B`의 연산 자체를 건너뛰고 즉시 `true`를 반환합니다.
- **`A && B`**: `A`가 `false`이면 `B`의 연산 자체를 건너뛰고 즉시 `false`를 반환합니다.

```kotlin
// a가 null이 아닌 경우에만 뒷부분(a.length > 5)이 실행되므로 안전함
if (a != null && a.length > 5) {
    // ...
}
```

---

## 6. 코틀린 고유의 특이 연산자 (범위 `..`, 포함 `in`, 인덱싱 `[]`)

코틀린은 일상적인 코딩을 더욱 간결하게 만들어주는 특별한 연산자들을 기본 제공합니다.

### (1) 범위 연산자 (`..`)
- `a..b` 형태로 작성하며, `a` 이상 `b` 이하의 범위를 나타내는 `ClosedRange` 객체를 생성합니다.
- 내부적으로 `a.rangeTo(b)` 메서드로 변환됩니다.

### (2) 포함 연산자 (`in`, `!in`)
- 컬렉션(List, Set 등)이나 범위 객체 안에 특정 원소가 포함되어 있는지 검사합니다.
- 내부적으로 `collection.contains(element)` 메서드로 변환됩니다.

```kotlin
// Java 스타일
if (score >= 80 && score <= 100) { ... }

// Kotlin 스타일 (직관적인 범위 검사)
if (score in 80..100) { ... }
```

### (3) 인덱싱 연산자 (`[]`, `[]=`)
- 자바에서는 배열에만 `arr[0]`을 쓸 수 있었고, `List`나 `Map`은 `list.get(0)`, `map.put(key, val)`을 써야 했습니다.
- 코틀린에서는 모든 컬렉션 및 `operator fun get/set`이 구현된 객체에서 배열 인덱싱 구문 `[]`을 사용할 수 있습니다.

```kotlin
// List / Map 조회
val firstItem = list[0]       // list.get(0)
val user = userMap["userId"]  // userMap.get("userId")

// 문자열의 특정 인덱스 문자 조회
val firstChar = "Kotlin"[0]   // 'K'
```

---

## 7. 연산자 오버로딩 (Operator Overloading)의 원리와 `operator` 키워드

### (1) `operator` 키워드의 역할
코틀린에서 특정 함수가 연산자로 동작하도록 선언하려면 **반드시 함수 시그니처 앞에 `operator` 키워드를 명시**해야 합니다.
`operator` 키워드가 누락되면 일반 메서드로만 호출할 수 있고, 연산자 기호로는 호출할 수 없습니다.

```kotlin
data class KotlinMoney(val amount: Long) {
    // '+' 연산자 오버로딩 정의
    operator fun plus(other: KotlinMoney): KotlinMoney {
        return KotlinMoney(this.amount + other.amount)
    }
}

val m1 = KotlinMoney(1000L)
val m2 = KotlinMoney(2000L)

val m3 = m1 + m2 // operator fun plus 호출
```

### (2) 주요 연산자 오버로딩 컨벤션 테이블

| 연산자 | 구현할 메서드 명칭 | 요구사항 / 파라미터 |
| :--- | :--- | :--- |
| `+a` | `unaryPlus()` | 파라미터 없음 |
| `-a` | `unaryMinus()` | 파라미터 없음 |
| `!a` | `not()` | 파라미터 없음, 반환타입 Boolean |
| `a + b` | `plus(b)` | 1개 파라미터 |
| `a - b` | `minus(b)` | 1개 파라미터 |
| `a * b` | `times(b)` | 1개 파라미터 |
| `a / b` | `div(b)` | 1개 파라미터 |
| `a % b` | `rem(b)` | 1개 파라미터 |
| `a..b` | `rangeTo(b)` | 1개 파라미터 |
| `a in b` | `b.contains(a)` | `b`에 선언된 메서드, Boolean 반환 |
| `a[i]` | `get(i)` | 1개 이상의 인덱스 파라미터 |
| `a[i] = b` | `set(i, b)` | 인덱스 파라미터 + 대입할 값 |
| `a > b` | `compareTo(b)` | Int 반환 (< 0, == 0, > 0) |

---

## 8. 실습 퀴즈(Quiz01~03) 심층 분석 및 해설

### 📘 Quiz01: 동일성, 동등성, 비교 연산자 기초 (`Quiz01_Problem.kt` / `Quiz01_Solution.kt`)
- **출제 목적**: 코틀린의 `===`(동일성), `==`(동등성), `>`(비교 연산자)의 1:1 기본 문법 체화.
- **모범 답안 코드**:
  ```kotlin
  private fun checkIdentityAndEquality(a: Quiz01Score, b: Quiz01Score): Pair<Boolean, Boolean> {
      val isIdentical = (a === b) // 참조 주소 일치 여부
      val isEqual = (a == b)       // 값의 일치 여부 (내부 equals 호출)
      return Pair(isIdentical, isEqual)
  }

  private fun isHigherScore(a: Quiz01Score, b: Quiz01Score): Boolean {
      return a > b // Comparable 구현으로 인해 자동으로 a.compareTo(b) > 0 실행
  }
  ```
- **핵심 해설**:
  - `s1 = Quiz01Score(100)`, `s2 = s1` ➔ 주소가 같으므로 `===`와 `==` 모두 `true`.
  - `s1 = Quiz01Score(100)`, `s3 = Quiz01Score(100)` ➔ 주소는 다르지만 점수 값이 같으므로 `===`는 `false`, `==`는 `true`.
  - `Comparable` 구현체는 `a.compareTo(b)` 대신 `a > b`를 사용하는 것이 관용적인 코틀린 스타일입니다.

---

### 📘 Quiz02: Java to Idiomatic Kotlin 리팩토링 (`Quiz02_Problem.kt` / `Quiz02_Solution.kt`)
- **출제 목적**: Java 스타일의 장황한 비교/연산 메서드 호출을 코틀린의 간결한 연산자 및 단일 표현식 함수로 리팩토링.
- **리팩토링 대비표**:

| Java 스타일 (기존) | Idiomatic Kotlin 스타일 (리팩토링) | 핵심 개선 효과 |
| :--- | :--- | :--- |
| `return a.compareTo(b) > 0;` | `private fun isGreaterThan(a: Quiz02Money, b: Quiz02Money): Boolean = a > b` | 메서드 호출 노이즈 제거, 도메인 직관성 향상 |
| `return a.equals(b);` | `private fun isSameAmount(a: Quiz02Money, b: Quiz02Money): Boolean = a == b` | 널 안전한 동등성 연산자 활용 |
| `return a.plus(b);` | `private fun sumMoney(a: Quiz02Money, b: Quiz02Money): Quiz02Money = a + b` | `operator fun plus`를 통한 산술 연산자 적용 |

---

### 📘 Quiz03: 실전 이커머스 장바구니 결제 & 예산 검증 (`Quiz03_Problem.kt` / `Quiz03_Solution.kt`)
- **출제 목적**: 산술 연산자 오버로딩(`+`, `-`), 범위 연산자(`..`) 및 포함 연산자(`in`), 인덱싱 연산자(`[]`)를 비즈니스 시나리오에 융합.
- **모범 답안 코드**:
  ```kotlin
  // 1. 산술 연산자 오버로딩 활용
  private fun calculateFinalPrice(
      original: Quiz03Price,
      discount: Quiz03Price,
      deliveryFee: Quiz03Price
  ): Quiz03Price {
      return original - discount + deliveryFee
  }

  // 2. '..' 및 'in' 연산자 활용 단일 표현식
  private fun isInBudget(
      price: Quiz03Price,
      minBudget: Quiz03Price,
      maxBudget: Quiz03Price
  ): Boolean = price in minBudget..maxBudget

  // 3. 인덱싱 '[]' 연산자 활용
  private fun getItemPrice(cart: Quiz03Cart, index: Int): Quiz03Price = cart[index]
  ```
- **핵심 해설**:
  - `original - discount + deliveryFee`: 오버로딩된 `-`와 `+` 연산자가 순차 실행되어 마치 일반 수학 수식을 계산하듯 비즈니스 로직을 명확하게 표현합니다.
  - `price in minBudget..maxBudget`: `minBudget <= price && price <= maxBudget`의 복잡한 논리식을 간결한 단일 표현식으로 치환합니다.
  - `cart[index]`: `Quiz03Cart` 내부에 `operator fun get(index: Int)`를 선언함으로써 객체 내부 리스트에 접근하는 전형적인 자바식 getter 호출을 제거했습니다.

---

## 9. 핵심 요약 치트시트 (Cheat Sheet)

```markdown
1. 동일성과 동등성:
   👉 '===' : 참조 주소(Identity) 비교 (자바의 ==)
   👉 '=='  : 객체 내용(Equality) 비교 (자바의 equals(), 내부적으로 널 안전성 보장)

2. 객체 크기 비교:
   👉 Comparable 구현체는 'a.compareTo(b) > 0' 대신 직관적인 부등호 'a > b'를 쓴다.

3. 연산자 오버로딩 정의:
   👉 반드시 'operator' 키워드를 붙여야 하며, 사전에 약속된 메서드 이름(plus, minus, get 등)을 사용한다.

4. 특이 연산자 3대장:
   👉 'a..b'     : a부터 b까지의 범위 생성 (rangeTo)
   👉 'x in a..b': 범위 또는 컬렉션 포함 여부 판정 (contains)
   👉 'obj[i]'   : 인덱스 기반 조회 (get)
```
