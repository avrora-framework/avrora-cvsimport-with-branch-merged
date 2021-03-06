/* Generated By:JavaCC: Do not edit this line. ProbeParserConstants.java */
package avrora.avrora.test.probes;

public interface ProbeParserConstants
{

    int EOF = 0;
    int SINGLE_LINE_COMMENT = 7;
    int INTEGER_LITERAL = 9;
    int DECIMAL_LITERAL = 10;
    int HEX_LITERAL = 11;
    int BIN_LITERAL = 12;
    int OCTAL_LITERAL = 13;
    int PROBE = 14;
    int EVENT = 15;
    int WATCH = 16;
    int MAIN = 17;
    int INSERT = 18;
    int REMOVE = 19;
    int ADVANCE = 20;
    int RESULT = 21;
    int LBRACKET = 22;
    int RBRACKET = 23;
    int SEMI = 24;
    int PIPE = 25;
    int IDENTIFIER = 26;
    int LETTER = 27;
    int DIGIT = 28;

    int DEFAULT = 0;
    int IN_SINGLE_LINE_COMMENT = 1;

    String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"",
            "\"\\f\"", "\"#\"", "<SINGLE_LINE_COMMENT>", "<token of kind 8>",
            "<INTEGER_LITERAL>", "<DECIMAL_LITERAL>", "<HEX_LITERAL>",
            "<BIN_LITERAL>", "<OCTAL_LITERAL>", "\"probe\"", "\"event\"",
            "\"watch\"", "\"main\"", "\"insert\"", "\"remove\"", "\"advance\"",
            "\"result\"", "\"{\"", "\"}\"", "\";\"", "\"|\"", "<IDENTIFIER>",
            "<LETTER>", "<DIGIT>", };

}
