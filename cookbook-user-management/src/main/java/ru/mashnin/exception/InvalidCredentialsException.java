package ru.mashnin.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException() {
        super("Неверный логин или пароль");
    }
}
