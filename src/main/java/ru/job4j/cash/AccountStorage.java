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
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        Account accountFrom = accounts.get(fromId);
        Account accountTo = accounts.get(toId);
        if (accountFrom != null && accountTo != null) {
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

