package com.example.btlandroid.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.btlandroid.dto.Result;
import com.example.btlandroid.models.User;
import com.example.btlandroid.services.user.UserService;

public class UserViewModel extends ViewModel {
    private final UserService userService = new UserService();
    private final MutableLiveData<Result<User>> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Result<User>> registerResult = new MutableLiveData<>();

    public LiveData<Result<User>> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        userService.login(email, password).observeForever(loginResult::setValue);
    }

    public LiveData<Result<User>> getRegisterResult() {
        return registerResult;
    }

    public void register(String email, String password) {
        userService.register(email, password).observeForever(registerResult::setValue);
    }
}
