# [Lec01] 코틀린에서 변수를 다루는 방법 (학습 가이드)

본 문서는 **Lec01 강의 노트(`Lec01Main.kt`)** 및 제작된 **실습 문제(Quiz01 ~ Quiz03)**를 완벽하게 이해하고 체화할 수 있도록 설계된 심화 교육용 지식 자산 문서입니다.

---

## 📌 목차
1. [코틀린 변수 선언의 철학](#1-코틀린-변수-선언의-철학)
2. [가변 변수(var) vs 불변 변수(val)](#2-가변-변수var-vs-불변-변수val)
3. [타입 추론(Type Inference) 원리와 적용](#3-타입-추론type-inference-원리와-적용)
4. [Primitive Type과 래퍼 클래스의 통합](#4-primitive-type과-래퍼-클래스의-통합)
5. [널 안전성(Null Safety)과 Nullable 타입](#5-널-안전성null-safety과-nullable-타입)
6. [객체 인스턴스화와 new 키워드의 부재](#6-객체-인스턴스화와-new-키워드의-부재)
7. [실습 퀴즈(Quiz01~03) 심층 분석 및 해설](#7-실습-퀴즈quiz0103-심층-분석-및-해설)
8. [핵심 요약 치트시트 (Cheat Sheet)](#8-핵심-요약-치트시트-cheat-sheet)

---

## 1. 코틀린 변수 선언의 철학

자바(Java)에서는 변수를 선언할 때 **타입을 먼저 명시**하고, 가변 여부는 옵션(`final`)으로 지정했습니다.
반면 코틀린(Kotlin)은 **"이 데이터가 변할 수 있는가(Mutability)?"**를 가장 중요한 관심사로 봅니다.

```kotlin
// 코틀린의 변수 선언 기본 구조
val a: Long = 10L // 불변
var b: Long = 10L // 가변
```

- **불변성(Immutability) 우선 원칙**: 함수형 프로그래밍의 사상을 계승하여 상태 변경(Side-effect)으로 인한 버그를 원천 차단합니다.
- **가독성 및 간결성**: 사람이 코드를 읽을 때 "이 값이 변하는가?"를 맨 앞의 키워드(`val`/`var`)만 보고 즉시 파악할 수 있습니다.

---

## 2. 가변 변수(var) vs 불변 변수(val)

### (1) 키워드 비교

| 키워드 | 어원 | 자바 대응 문법 | 특징 |
| :--- | :--- | :--- | :--- |
| `val` | **Val**ue (값) | `final long number = 10L;` | • 읽기 전용(Read-only) / 불변 참조<br>• 초기화 후 재할당(Re-assign) 불가 |
| `var` | **Var**iable (변수) | `long number = 10L;` | • 가변(Mutable) 변수<br>• 초기화 이후에도 다른 값으로 재할당 가능 |

### (2) `val`의 정확한 의미: 재할당 금지
> **주의**: `val`은 변수 식별자에 대한 **재할당(Re-assignment)이 불가능**하다는 뜻이지, 가리키는 객체의 내부 상태까지 불변이라는 뜻은 아닙니다.
```kotlin
val person = Person("홍길동")
// person = Person("이순신") // ❌ 컴파일 에러: val 변수는 재할당 불가!
person.name = "이순신"       // ⭕ 성공: Person 객체 내부의 가변 프로퍼티(var)는 변경 가능
```

### (3) 지연 초기화와 최초 1회 할당
`val` 변수라 할지라도 선언과 동시에 초기화하지 않고, 타입이 명시되어 있다면 조건문 등에 의해 **최초 1회에 한하여 값을 할당**할 수 있습니다.
```kotlin
val number: Long
if (someCondition) {
    number = 10L
} else {
    number = 20L
}
println(number) // ⭕ 정상 동작
```

> 💡 **Best Practice**:
> **"모든 변수는 기본적으로 `val`로 선언하고, 로직상 반드시 상태 변경이 필요한 경우에만 `var`로 바꾼다."**

---

## 3. 타입 추론(Type Inference) 원리와 적용

코틀린은 정적 타입 언어(Statically typed language)이지만, 컴파일러가 똑똑하게 우변의 대입식을 평가하여 타입을 자동으로 결정합니다.

```kotlin
var number1 = 10L  // 컴파일러가 Long 타입으로 추론
var number2 = 10   // 컴파일러가 Int 타입으로 추론
var text = "안녕"  // 컴파일러가 String 타입으로 추론
```

### 타입 명시가 강제되는 2가지 경우
1. **선언 시점에 초기값을 주지 않는 경우**:
   ```kotlin
   var number: Long // ⭕ 컴파일러가 우변을 볼 수 없으므로 타입 명시 필수
   number = 10L
   ```
2. **원하는 타입과 리터럴의 기본 추론 타입이 다른 경우**:
   ```kotlin
   var count: Short = 10 // 리터럴 10은 기본적으로 Int이므로, Short를 원하면 명시 필요
   ```

---

## 4. Primitive Type과 래퍼 클래스의 통합

### (1) 자바의 고민: 원시형 vs 참조형
- Java에서는 성능을 위한 기본형(`long`, `int`, `boolean`)과 객체 기능(컬렉션 제네릭 담기, 메서드 호출)을 위한 래퍼 클래스(`Long`, `Integer`, `Boolean`)가 분리되어 있었습니다.
- 이로 인해 개발자가 박싱(Boxing), 언박싱(Unboxing) 비용과 NullPointerException 위험을 수동으로 고려해야 했습니다.

### (2) 코틀린의 우아한 해결책
- 코틀린 코드 레벨에서는 **`Long`, `Int` 등 단 하나의 클래스 형태**로만 코딩합니다.
- **컴파일 타임 최적화**: 코틀린 컴파일러가 바이트코드를 생성할 때, 가능한 상황(연산, 지역 변수 등)에서는 자동으로 Java의 원시형(`long`, `int`)으로 치환하고, 컬렉션 제네릭 등 객체가 필요한 곳에서만 자동으로 래퍼형(`Long`, `Integer`)으로 변환합니다.
- **결과**: 개발자는 성능 최적화와 문법적 일관성을 동시에 누릴 수 있습니다.

---

## 5. 널 안전성(Null Safety)과 Nullable 타입

자바 진영의 최대 고통 중 하나인 `NullPointerException (NPE)`을 언어 차원에서 차단하기 위해, 코틀린은 **Non-null 타입과 Nullable 타입을 서로 다른 타입**으로 취급합니다.

```
      Any (모든 객체의 조상)
     /   \
   Long   String
    |       |
  Long?   String?  (null을 담을 수 있는 슈퍼타입)
    \       /
      null
```

### (1) 기본은 Non-null (null 절대 불가)
```kotlin
var number: Long = 10L
// number = null // ❌ 컴파일 에러! Null can not be a value of a non-null type Long
```

### (2) Nullable 변수 선언: 물음표(`?`)
타입 뒤에 `?`를 명시적으로 붙여야만 null을 담을 수 있습니다.
```kotlin
var number: Long? = 10L
number = null // ⭕ 정상 동작
```

### (3) 자주 하는 실수: `var a = null`
```kotlin
var a = null
// a = 10L // ❌ 컴파일 에러!
```
- 타입 명시 없이 `null`만 대입하면 컴파일러는 `a`의 타입을 `Nothing?`으로 추론합니다.
- `Nothing?` 타입에는 오직 `null`만 들어갈 수 있으므로 다른 값을 다시 넣을 수 없습니다.
- 따라서 null로 초기화할 때는 반드시 `var a: Long? = null`처럼 명시적 타입을 적어야 합니다.

---

## 6. 객체 인스턴스화와 new 키워드의 부재

코틀린에서는 클래스의 인스턴스를 생성할 때 **`new` 키워드를 사용하지 않습니다.**

```kotlin
// Java
Person person = new Person("홍길동");

// Kotlin
val person = Person("홍길동")
```

### 왜 new를 제거했을까?
1. **문법적 잡음(Noise) 제거**: 불필요한 키워드를 줄여 코드 가독성을 극대화합니다.
2. **생성자 호출의 함수화**: 생성자 호출도 일반 함수 호출과 동일한 문법 형태(`Identifier(...)`)를 갖추어 언어의 대칭성과 표현력을 높입니다.

---

## 7. 실습 퀴즈(Quiz01~03) 심층 분석 및 해설

### 📘 Quiz01: 기초 문법 체화 (`Quiz01_Problem.kt` / `Quiz01_Solution.kt`)
- **출제 핵심**: `val`과 `var`의 올바른 선택, 그리고 `Long?`을 통한 null 대입.
- **핵심 코드**:
  ```kotlin
  val step = 100L               // 고정 가산값이므로 val
  var currentCount = initialCount // 값이 변경되므로 var
  currentCount += step

  var nullableNumber: Long? = initialCount // null 대입을 위해 Long? 명시
  nullableNumber = null
  ```
- **채점 포인트**:
  - `check(result.first == 10L)`
  - `check(result.second == 110L)`
  - `check(result.third == null)`

---

### 📘 Quiz02: Java to Idiomatic Kotlin 리팩토링 (`Quiz02_Problem.kt` / `Quiz02_Solution.kt`)
- **출제 핵심**: 자바의 장황한 코드를 관용적 코틀린 스타일로 정제.
- **비교 분석**:

| Java 스타일 | Idiomatic Kotlin 스타일 | 변환 이유 |
| :--- | :--- | :--- |
| `final long productId = 1001L;` | `val productId = 1001L` | `final` -> `val`, 타입 추론으로 `long` 생략 |
| `long currentStock = 50L;` | `var currentStock = 50L` | 변경 가능하므로 `var` |
| `Long discountRate = null;` | `val discountRate: Long? = null` | 컴파일 타임 널 안전성을 위해 `Long?` 명시 |
| `new Product(...)` | `Quiz02Product(...)` | `new` 키워드 제거 |

---

### 📘 Quiz03: 실무 미니 시나리오 (`Quiz03_Problem.kt` / `Quiz03_Solution.kt`)
- **출제 핵심**: 데이터 모델링 시 프로퍼티 불변성 설계 및 선택적 필드 처리.
- **모델 설계**:
  ```kotlin
  data class Quiz03UserProfile(
      val userId: Long,     // 회원 ID는 절대 바뀌면 안 되므로 val
      var nickname: String, // 닉네임은 변경 가능하므로 var
      var bio: String?,     // 자기소개는 작성하지 않거나(null) 수정 가능하므로 var + Nullable
      var loginCount: Long  // 로그인 횟수는 누적 증가하므로 var
  )
  ```
- **비즈니스 흐름**:
  1. 회원 가입: `bio = null`, `loginCount = 1L`로 기본 초기화.
  2. 프로필 변경: 닉네임 변경 및 `bio`에 새 문자열 또는 `null`(자기소개 삭제) 반영.

---

## 8. 핵심 요약 치트시트 (Cheat Sheet)

```markdown
1. 변수 선언 우선순위:
   👉 무조건 'val'로 선언하고, 재할당이 불가피할 때만 'var'로 바꾼다.

2. 타입 지정 가이드:
   👉 초기값이 명확할 때는 컴파일러의 '타입 추론'에 맡긴다.
   👉 초기값 없이 선언만 하거나 null을 넣어야 할 때는 반드시 명시적 타입을 적는다.

3. 널 처리 황금률:
   👉 기본 타입은 모두 Non-null이다.
   👉 null이 필요한 경우에만 타입 뒤에 '?'를 붙인다 (예: String?, Long?).

4. 객체 생성:
   👉 'new'는 잊어라! 함수를 호출하듯 '클래스명(...)'으로 생성한다.
```
