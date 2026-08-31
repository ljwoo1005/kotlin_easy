package Lec11

fun isDirectoryPath(path: String): Boolean {
    return path.endsWith("/")
}

/*
 * 위의 코틀린 코드는 자바로 디컴파일 시 다음과 같이 변경된다.
 *
 * public final class StringUtilsKt {
 *      public static final boolean isDirectoryPath(@NotNull String path) {
 *          Intrinsics.checkNotNullParameter(path, "path");
 *          return StringsKt.endsWithDefault(path, "/", false, 2, (Object)null);
 *      }
 * }
 *
 * 파일 최상단에 함수를 작성하면 자바 코드로 변환 시 static 함수가 된다.
 * 위 코드는 실제 자바 파일에서 사용할 수 있다.
 */