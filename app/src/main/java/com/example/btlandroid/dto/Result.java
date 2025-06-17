package com.example.btlandroid.dto;

public class Result<T> {
    public T data;
    public String message;
    public boolean loading;
    public boolean isSuccess = true;

    public Result(T data, String message, boolean loading, boolean isSuccess) {
        this.data = data;
        this.message = message;
        this.loading = loading;
        this.isSuccess = isSuccess;
    }

    public Result(T data, String message, boolean loading) {
        this.data = data;
        this.message = message;
        this.loading = loading;
    }

    public Result(T data) {
        this.data = data;
    }

    public Result(T data, String msg) {
        this.data = data;
        this.message = msg;
    }

    public static <T> Result<T> loading() {
        return new Result<>(null, null, true);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, false, true);
    }

    public static <T> Result<T> success(T data, String msg) {
        return new Result<>(data, msg, false, true);
    }

    public static <T> Result<T> error(String error) {
        return new Result<>(null, error, false, false);
    }
}
