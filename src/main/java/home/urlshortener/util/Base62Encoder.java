package home.urlshortener.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class Base62Encoder {
    private static final List<Character> indexToCharTable;
    static {
        indexToCharTable = new ArrayList<>();
        for (int i = 0; i < 26; ++i) {
            char lowerCaseChar = 'a';
            lowerCaseChar += i;
            indexToCharTable.add(lowerCaseChar);
        }
        for (int i = 26; i < 52; ++i) {
            char upperCaseChar = 'A';
            upperCaseChar += (i-26);
            indexToCharTable.add(upperCaseChar);
        }
        for (int i = 52; i < 62; ++i) {
            char numericChar = '0';
            numericChar += (i - 52);
            indexToCharTable.add(numericChar);
        }
    }

    public static String encode(Long valueToEncode) {
        log.info("Encoding {}.", valueToEncode);
        List<Integer> digits = new LinkedList<>();
        while(valueToEncode > 0) {
            int remainder = (int) (valueToEncode % 62);
            ((LinkedList<Integer>) digits).addFirst(remainder);
            valueToEncode /= 62;
        }
        StringBuilder encodedValue = new StringBuilder();
        for (int digit: digits) {
            encodedValue.append(indexToCharTable.get(digit));
        }
        return encodedValue.toString();
    }
}
