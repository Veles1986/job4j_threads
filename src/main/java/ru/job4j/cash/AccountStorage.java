package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        if (!accounts.containsKey(account.id())) {
            accounts.put(account.id(), account);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean update(Account account) {
        if (accounts.containsKey(account.id())) {
            accounts.put(account.id(), account);
            return true;
        } else {
            return false;
        }
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        if (accounts.containsKey(id)) {
            return Optional.of(accounts.get(id));
        } else {
            return Optional.empty();
        }
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        if (accounts.containsKey(fromId) && accounts.containsKey(toId)) {
            Account accountFrom = accounts.get(fromId);
            Account accountTo = accounts.get(toId);
            if (accountFrom.amount() >= amount) {
                accounts.replace(accountFrom.id(),
                        new Account(accountFrom.id(), accountFrom.amount() - amount));
                accounts.replace(accountTo.id(),
                        new Account(accountTo.id(), accountTo.amount() + amount));
                return true;
            }
        }
        return false;
    }
}

