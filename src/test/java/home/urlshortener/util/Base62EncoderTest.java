package home.urlshortener.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Base62EncoderTest {
    @ParameterizedTest
    @CsvSource({"1024,qG", "1,b", "75,bn", "7530213,FK7d"})
    public void shouldEncode(Long valueToEncode, String expectedValue) {
        assertEquals(expectedValue, Base62Encoder.encode(valueToEncode));
    }

}