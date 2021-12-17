package bankomat.com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardReaderTest {


    CardReader cr;

    @BeforeEach
    void setUp() {
        cr = new CardReader();
    }

    @Test
    void should_returnNull_when_getUser(){
        assertNull(cr.getID());
    }

    @Test
    void should_returnMinusOne_when_pinAttempsRemaining(){
        assertEquals(-1, cr.enterPIN());
    }

    @Test
    void should_returnMinusOne_when_getAmount(){
        assertEquals(-1, cr.getAmount());
    }
}