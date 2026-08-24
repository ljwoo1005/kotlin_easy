package Lec07;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Lec07Main {

    /*
     * 주어진 문자열을 정수로 변경하는 예제
     */
    private int parseIntOrThrow(@NotNull String str) {
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException(String.format("주어진 %s는 숫자가 아닙니다", str));
        }
    }

    /*
     * 주어진 문자열을 정수로 변경하는 예제
     * 실패하면 null을 반환!
     */
    private Integer parseIntOrNull(@NotNull String str) {
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException e) {
            return null;
        }
    }

    public static void main(String[] args) throws IOException{

        JavaFilePrinter printer = new JavaFilePrinter();
        printer.readFile();

    }

}

class JavaFilePrinter {

    /*
     * 파일을 읽는 예제
     */
    public void readFile() throws IOException {
        File currentFile = new File(".");
        File file = new File(currentFile.getAbsolutePath() + "/a.txt");

        BufferedReader reader = new BufferedReader(new FileReader(file));

        System.out.println(reader.readLine());
        reader.close();
    }

    public void readFile2(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            System.out.println(reader.readLine());
        }
    }

}