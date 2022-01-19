package org.lordy.concurrent.activity;

public class DynamicOrderDeadlock {

    public static void transferMoney(Account from, Account to, double amount){
        synchronized (from){
            synchronized (to){
                if(from.getBalance() < amount){
                    throw new RuntimeException();
                }else {
                    from.setBalance(from.getBalance() - amount);
                    to.setBalance(to.getBalance() + amount);
                }
            }
        }
    }



    static class Account{
        private double balance;

        public Account(double balance) {
            this.balance = balance;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }
}
