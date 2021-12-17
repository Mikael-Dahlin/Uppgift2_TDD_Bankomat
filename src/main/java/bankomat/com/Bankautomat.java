package bankomat.com;

public class Bankautomat {

    CardReader cardReader;
    Bank bank;
    User currentUser;
    String activeCard;

    Bankautomat (CardReader cardReader, Bank bank){
        this.cardReader = cardReader;
        this.bank = bank;
    }

    public String cardInserted() {
        if (activeCard == null)
            getCardID();

        if(bank.checkCardID(activeCard)){
            currentUser = bank.getUser(activeCard);
            return "Welcome " + currentUser.getName();
        }
        return "Card is blocked";
    }

    public String pinEntered() {
        if (activeCard == null)
            getCardID();

        int pin = cardReader.enterPIN();
        if (bank.pinApproved(activeCard, pin)){
            return "You are now logged in";
        } else {
            int attemptsRemaining = bank.pinAttemptsRemaining(activeCard);
            if(attemptsRemaining == 0)
                return "Wrong PIN, your card has now been blocked";

            return "Wrong PIN, you have " + attemptsRemaining + " attempts remaining";
        }
    }

    public String checkAmount() {
        if (activeCard == null)
            getCardID();

        int amount = bank.getAmount(activeCard);

        return "You currently have " + amount + "in the account connected with this card";
    }

    public void depositMoney() {
        if (activeCard == null)
            getCardID();

        int amount = cardReader.getAmount();

        if(amount > 0)
            bank.depositMoneyCardNumber(activeCard, amount);
    }

    public String withdrawMoney() {
        if (activeCard == null)
            getCardID();

        int amount = cardReader.getAmount();
        int amountInBank = bank.getAmount(activeCard);

        if(amountInBank > amount){
            bank.withdrawMoneyCardNumber(activeCard, amount);
            return "Success";
        }

        return "There was not enough money in the bank account";
    }

    public void logout(){
        cardReader.dispenseCard();
    }

    public String printBankMessage(){
        return "Welcome to " + Bank.getBankName();
    }

    private void getCardID(){
        activeCard = cardReader.getID();
    }
}
