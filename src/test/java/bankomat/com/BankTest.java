package bankomat.com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Test
    void should_returnFalse_when_checkCardID(){
        assertFalse(bank.checkCardID(null));
    }

    @Test
    void should_returnNull_when_getUser(){
        assertNull(bank.getUser(null));
    }

    @Test
    void should_returnFalse_when_pinApproved(){
        assertFalse(bank.pinApproved(null, -1));
    }

    @Test
    void should_returnMinusOne_when_pinAttempsRemaining(){
        assertEquals(-1, bank.pinAttemptsRemaining(null));
    }

    @Test
    void should_returnNull_when_getBankName(){
        assertNull(bank.getBankName());
    }

    @Test
    void should_returnMinusOne_when_getAmount(){
        assertEquals(-1, bank.getAmount(null));
    }

}