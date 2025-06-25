package cia.arkrypto.auth.test;


import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class LexerTest {
    private static final String[] reserveWords = { "abstract", "boolean", "break", "byte", "case", "catch", "char",
            "class", "continue", "default", "do", "double", "else", "extends", "final", "finally", "float", "for", "if",
            "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private",
            "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw",
            "throws", "transient", "try", "void", "volatile", "while", "strictfp", "enum", "goto", "const", "assert" }; // 50 个
    private BufferedReader fd;
    private int state;
    private char ch;
    private String info; // 结果串
    private String temp; // 临时存储
    int lineNum;
    boolean finished;

    public LexerTest() {
        info = "";
        temp = "";
        lineNum = 1;
        finished = false;
        getChar();
        analyze();
//        write(info); // 写文件
        System.out.println(info); // 就不写了，打印看一下就行
    }

    private void analyze() {

        if (finished && temp.equals(""))
            return; // 已经读取到最后一个字符，且没有待处理字符
        if (ch == '\n')
            lineNum++;

        switch (state) {
            case 0 -> {
                temp = "";
                if (ch == ' ' || ch == '\r' || ch == '\t' || ch == '\n') {
                    toNextCharAndChangeState(0);
                } else if (ch == '/') {
                    toNextCharAndStoreTempAndChangeState(1);
                } else if (isDigital(ch)) {
                    toNextCharAndStoreTempAndChangeState(5);
                } else if (isOperator1(ch)) {
                    toNextCharAndStoreTempAndChangeState(8);
                } else if (ch == '!') {
                    toNextCharAndStoreTempAndChangeState(9);
                } else if (isOperator2(ch)) {
                    writeInfo((ch + ""), "运算符");
                    getChar();
                } else if (isBoundary(ch)) {
                    writeInfo((ch + ""), "界符");
                    getChar();
                } else if (ch == '"') {
                    toNextCharAndStoreTempAndChangeState(10);
                } else if (isLetter(ch)) {
                    toNextCharAndStoreTempAndChangeState(11);
                } else if (ch == '\'') {
                    toNextCharAndStoreTempAndChangeState(14);
                } else if (ch == '-' || ch == '+') {
                    toNextCharAndStoreTempAndChangeState(16);
                } else if (ch == '|') {
                    toNextCharAndStoreTempAndChangeState(17);
                } else if (ch == '&') {
                    toNextCharAndStoreTempAndChangeState(18);
                } else if (ch == (char) -1) {
                    // 程序应该结束
                    return;
                } else { // 非法字符
                    error(1);
                    return;
                }
            }
            case 1 -> {
                if (ch == '/') {
                    toNextCharAndChangeState(2);
                } else if (ch == '*') {
                    toNextCharAndChangeState(3);
                } else {
                    state = 8;
                }
            }
            case 2 -> { // 处理注释
                if (ch == '\n') {
                    state = 0;
                    getChar();
                } else {
                    getChar();
                }
            }
            case 3 -> { // 处理注释
                if (ch == '*') {
                    toNextCharAndChangeState(4);
                } else {
                    getChar();
                }
            }
            case 4 -> { // 处理注释
                if (ch == '/') {
                    toNextCharAndChangeState(0);
                } else {
                    toNextCharAndChangeState(3);
                }
            }
            case 5 -> {
                if (isDigital(ch)) {
                    temp += ch;
                    getChar();
                } else {
                    state = 6;
                }
            }
            case 6 -> {
                if (ch == '.') {
                    toNextCharAndStoreTempAndChangeState(7);
                } else {
                    writeInfo(temp, "常数");
                }
            }
            case 7 -> {
                if (isDigital(ch)) {
                    toNextCharAndStoreTempAndChangeState(13);
                } else {
                    error(4);
                    return;
                }
            }
            case 8 -> {
                if (ch == '=') {
                    temp += ch;
                    writeInfo(temp, "运算符");
                    getChar();
                } else {
                    writeInfo(temp, "运算符");
                }
            }
            case 9 -> {
                if (ch == '=') {
                    temp += ch;
                    writeInfo(temp, "运算符");
                    getChar();
                } else {
                    error(2);
                    return;
                }
            }
            case 10 -> {
                if (ch == '"') {
                    temp += ch;
                    writeInfo(temp, "常量");
                    getChar();
                } else if (ch == '\\') {
                    for (int i = 0; i < 2 ; i++){
                        temp += ch;
                        getChar();
                    }
                    state = 10;
                } else {
                    toNextCharAndStoreTempAndChangeState(10);
                }
            } case 11 -> {
                if (isDigital(ch) || isLetter(ch) || ch == '_') {
                    toNextCharAndStoreTempAndChangeState(11);
                } else {
                    state = 12;
                }
            }
            case 12 -> {
                if (isReserve(temp)) {
                    writeInfo(temp, "保留字");
                    getChar();
                } else {
                    writeInfo(temp, "标识符");
                    getChar();
                }
            }
            case 13 -> {
                if (isDigital(ch)) {
                    toNextCharAndStoreTempAndChangeState(13);
                } else {
                    writeInfo(temp, "常数");
                }
            }
            case 14 -> {
                if (ch == '\'') {
                    temp += ch;
                    if (isLegalChar(temp)) {
                        writeInfo(temp, "常量");
                    } else {
                        error(9);
                        return;
                    }
                    getChar();
                } else if (ch == '\\') {
                    for (int i = 0; i < 2; i++){
                        temp += ch;
                        getChar();
                    }
                    state = 14;
                } else {
                    toNextCharAndStoreTempAndChangeState(14);
                }
            } case 16 -> {
                if (isDigital(ch)) {
                    toNextCharAndStoreTempAndChangeState(5);
                } else {
                    state = 8;
                }
            }
            case 17 -> {
                if (ch == '|') {
                    temp += ch;
                    writeInfo(temp, "运算符");
                    getChar();
                } else {
                    writeInfo(temp, "运算符");
                }
            }
            case 18 -> {
                if (ch == '&') {
                    temp += ch;
                    writeInfo(temp, "运算符");
                    getChar();
                } else {
                    writeInfo(temp, "运算符");
                }
            }
            default -> {
                error(3);
                return;
            }
        }

        analyze();
    }

    private boolean isLegalChar(String temp) {
        char[] ch = temp.toCharArray();
        int length = ch.length;
        boolean isLegalChar = false;

        /*
         * Char a = '';// error char b = ' ';// length = 3; char c = '\n';//length = 4;
         * b n r t " ' \ char d = '\122'; // length <= 6;
         */
        if (length == 2) { // ''
            isLegalChar = false;
        } else if (length == 3) {
            isLegalChar = true;
        } else if (length == 4) {
            if ((ch[1] == '\\') && (ch[2] == 'b' || ch[2] == 'n' || ch[2] == 'r' || ch[2] == 't' || ch[2] == '\"'
                    || ch[2] == '\'' || ch[2] == '\\' || isDigital(ch[2]))) {
                isLegalChar = true;
            }
        } else if (length <= 6) {
            if (ch[1] == '\\') {
                for (int i = 2; i < (length - 1); i++) {
                    if (!isDigital(ch[i])) {
                        isLegalChar = false;
                        break;
                    }
                    isLegalChar = true;
                }
            } else {
                System.out.println('*');
                isLegalChar = false;
            }
        } else {
            isLegalChar = false;
        }

        return isLegalChar;
    }

    private void toNextCharAndChangeState(int state) {
        this.state = state;
        getChar();
    }

    private void toNextCharAndStoreTempAndChangeState(int state) {
        temp += ch;
        this.state = state;
        getChar();
    }

    private boolean isReserve(String temp2) {
        for (int i = 0; i < 50; i++) {
            if (temp.equals(reserveWords[i])) {
                return true;
            }
        }
        return false;
    }

    private void writeInfo(String value, String type) {
        info += lineNum + ": < " + type + " , " + value + " >;\r\n";
        state = 0;
    }

    private boolean isLetter(char ch) {
        return (ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122);
    }

    private boolean isBoundary(char ch) {
        return ch == ',' || ch == ';' || ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' || ch == '}';
    }

    private boolean isOperator1(char ch) { // / * = &lt; &gt;
        return ch == '/' || ch == '*' || ch == '=' || ch == '<' || ch == '>';
    }

    private boolean isOperator2(char ch) { // ? . :
        return ch == '?' || ch == '.' || ch == ':';
    }

    private boolean isDigital(char ch) {
        return ch >= 48 && ch <= 57;
    }

    private void error(int i) {
        info = "词法分析出错\r\n错误定位：" + i;
    }

    private void getChar() {
        try {
            if (fd == null) {
                ClassPathResource resource = new ClassPathResource("test.txt");
                fd = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            }
            int end = fd.read();
            if (end == -1) { // 当从一个文件中读取数据时，在数据最后会返回一个int型-1来表示结束
                fd.close();
                finished = true;
                return;
            }
            ch = (char) end;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void write(String info) {
        try {
            FileWriter fw = new FileWriter("result.txt");
            fw.write(info);
            fw.flush(); // 刷新数据，将数据写入文件中
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new LexerTest();
    }
}