package bankomat.com;

public class Bank {

    public boolean checkCardID(String s) {
        return false;
    }

    public User getUser(String s) {
        return null;
    }

    public boolean pinApproved(String cardNumber, int pin) {
        return false;
    }

    public int pinAttemptsRemaining(String cardNumber) {
        return -1;
    }

    public static String getBankName(){
        return null;
    }

    public int getAmount(String cardNumber) {
        return -1;
    }

    public void depositMoneyCardNumber(String cardNumber, int amount) {
    }

    public void withdrawMoneyCardNumber(String cardNumber, int amount) {
    }
}
