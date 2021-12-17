package bankomat.com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankautomatTest {

    Bankautomat bankomat;
    Bank bank;
    CardReader cardReader;

    @BeforeEach
    void setUp() {
        bank = mock(Bank.class);
        cardReader = mock(CardReader.class);
        bankomat = new Bankautomat(cardReader, bank);
    }

    @Test
    void should_checkCardID_when_cardIsReadByReader(){
        String expected = "Welcome John Doe";
        when(cardReader.getID()).thenReturn("1234567890123456");
        when(bank.checkCardID("1234567890123456")).thenReturn(true);
        when(bank.getUser("1234567890123456")).thenReturn(new User("John Doe", 2362));

        String actual = bankomat.cardInserted();

        assertEquals(expected, actual);
    }

    @Test
    void should_checkCardID_when_anotherCardIsReadByReader(){
        String expected = "Welcome Jane Doe";
        when(cardReader.getID()).thenReturn("1234567890654321");
        when(bank.checkCardID("1234567890654321")).thenReturn(true);
        when(bank.getUser("1234567890654321")).thenReturn(new User("Jane Doe", 2363));

        String actual = bankomat.cardInserted();

        assertEquals(expected, actual);
    }

    @Test
    void should_checkPinWithBank_when_pinCodeIsEnteredCorrectly(){
        String expected = "You are now logged in";
        String cardNumber = "1234567890123456";
        int pin = 1234;
        when(cardReader.getID()).thenReturn(cardNumber);
        when(cardReader.enterPIN()).thenReturn(pin);
        when(bank.pinApproved(cardNumber, pin)).thenReturn(true);

        String actual = bankomat.pinEntered();

        assertEquals(expected, actual);
    }

    @Test
    void should_checkPinWithBank_when_pinCodeIsEnteredIncorrectly(){
        String expected = "Wrong PIN, you have 2 attempts remaining";
        String cardNumber = "1234567890123456";
        int pin = 1234;
        when(cardReader.getID()).thenReturn(cardNumber);
        when(cardReader.enterPIN()).thenReturn(1235);
        when(bank.pinApproved(cardNumber, pin)).thenReturn(false);
        when(bank.pinAttemptsRemaining(cardNumber)).thenReturn(2);

        String actual = bankomat.pinEntered();

        assertEquals(expected, actual);
    }

    @Test
    void should_checkPinWithBank_when_pinCodeIsEnteredIncorrectlyAgain(){
        String expected = "Wrong PIN, you have 1 attempts remaining";
        String cardNumber = "1234567890123456";
        int pin = 1234;
        when(cardReader.getID()).thenReturn(cardNumber);
        when(cardReader.enterPIN()).thenReturn(1235);
        when(bank.pinApproved(cardNumber, pin)).thenReturn(false);
        when(bank.pinAttemptsRemaining(cardNumber)).thenReturn(1);

        String actual = bankomat.pinEntered();

        assertEquals(expected, actual);
    }

    @Test
    void should_checkPinWithBank_when_pinCodeIsEnteredIncorrectlyOnLastAttempt(){
        String expected = "Wrong PIN, your card has now been blocked";
        String cardNumber = "1234567890123456";
        int pin = 1235;
        when(cardReader.getID()).thenReturn(cardNumber);
        when(cardReader.enterPIN()).thenReturn(1235);
        when(bank.pinApproved(cardNumber, pin)).thenReturn(false);
        when(bank.pinAttemptsRemaining(cardNumber)).thenReturn(0);

        String actual = bankomat.pinEntered();

        assertEquals(expected, actual);
    }

    @Test
    void should_checkCardID_when_cardIsReadByReaderAndCardIsBlocked(){
        String expected = "Card is blocked";
        when(cardReader.getID()).thenReturn("1234567890123456");
        when(bank.checkCardID("1234567890123456")).thenReturn(false);

        String actual = bankomat.cardInserted();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 500, 5000, 10000, 15000})
    void should_getAmountInBank_when_callingGetAmountFromBank(int amount){
        String expected = "You currently have " + amount + "in the account connected with this card";
        String cardNumber = "1234567890123456";
        when(cardReader.getID()).thenReturn(cardNumber);
        when(bank.getAmount(cardNumber)).thenReturn(amount);

        String actual = bankomat.checkAmount();

        assertEquals(expected, actual);
    }

    @Test
    void should_depositMoneyToAccount_when_depositMoney(){
        when(cardReader.getID()).thenReturn("1234567890123456");
        when(cardReader.getAmount()).thenReturn(500);

        bankomat.depositMoney();

        verify(bank).depositMoneyCardNumber(anyString(), anyInt());
    }

    @Test
    void should_depositMoneyToAccount_when_depositMoneyNegativeAmount(){
        when(cardReader.getID()).thenReturn("1234567890123456");
        when(cardReader.getAmount()).thenReturn(-500);

        bankomat.depositMoney();

        verifyNoInteractions(bank);
    }

    @Test
    void should_withdrawMoneyFromAccount_when_withdrawMoney(){
        String expected = "Success";
        String cardNumber = "1234567890123456";
        when(cardReader.getID()).thenReturn(cardNumber);
        when(cardReader.getAmount()).thenReturn(2500);
        when(bank.getAmount(cardNumber)).thenReturn(5000);

        String actual = bankomat.withdrawMoney();

        verify(bank).getAmount(cardNumber);
        verify(bank).withdrawMoneyCardNumber(anyString(), anyInt());
        assertEquals(expected, actual);
    }

    @Test
    void should_notWithdrawMoneyFromAccount_when_notEnoughMoneyInAccount(){
        String expected = "There was not enough money in the bank account";
        String cardNumber = "1234567890123456";
        when(cardReader.getID()).thenReturn(cardNumber);
        when(cardReader.getAmount()).thenReturn(5500);
        when(bank.getAmount(cardNumber)).thenReturn(5000);

        String actual = bankomat.withdrawMoney();

        verify(bank).getAmount(cardNumber);
        verifyNoMoreInteractions(bank);
        assertEquals(expected, actual);
    }

    @Test
    void should_dispenseCard_when_logout(){
        bankomat.logout();

        verify(cardReader).dispenseCard();
    }

    @ParameterizedTest
    @ValueSource (strings = {"Testbanken", "Testbanken2", "Testbanken3"})
    void should_printMessage_when_printBankMessage(String bankName){
        String expected = "Welcome to " + bankName;

        MockedStatic<Bank> bankMockedStatic = mockStatic(Bank.class);
        bankMockedStatic.when(() -> Bank.getBankName()).thenReturn(bankName);

        String actual = bankomat.printBankMessage();

        assertEquals(expected, actual);
        bankMockedStatic.close();
    }
}