package com.example.btlandroid.services.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.btlandroid.dto.Result;
import com.example.btlandroid.dto.UserDetail;
import com.example.btlandroid.models.Department;
import com.example.btlandroid.models.User;
import com.example.btlandroid.services.department.DepartmentService;
import com.example.btlandroid.utils.SharedPrefUtil;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserService {
    public LiveData<Result<User>> register(String email, String password) {
        MutableLiveData<Result<User>> liveData = new MutableLiveData<>();
        liveData.setValue(Result.loading());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công
                        FirebaseUser user = mAuth.getCurrentUser();
                        liveData.setValue(Result.success(new User(), "Đăng ký thành công!"));
                    } else {
                        Exception e = task.getException();
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            // Email đã tồn tại
                            liveData.setValue(Result.error("Email đã tồn tại!"));
                        } else {
                            liveData.setValue(Result.error("Đăng ký thất bại!"));
                        }
                    }
                });

        return liveData;
    }

    public LiveData<Result<User>> login(String email, String password) {
        MutableLiveData<Result<User>> liveData = new MutableLiveData<>();
        liveData.setValue(Result.loading());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user == null) {
                            liveData.setValue(Result.error("Đăng nhập thất bại!"));
                            return;
                        }
                        liveData.setValue(Result.success(new User(user.getUid())));
                    } else {
                        Exception e = task.getException();
                        String msg;
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            msg = "Tài khoản không tồn tại!";
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            msg = "Email hoặc mật khẩu không đúng!";
                        } else if (e instanceof FirebaseNetworkException) {
                            msg = "Vui lòng kiểm tra kết nối mạng của bạn!";
                        } else {
                            msg = "Đăng nhập thất bại: " + e;
                        }
                        liveData.setValue(Result.error(msg));
                    }
                });

        return liveData;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        SharedPrefUtil.clear();
    }

    public LiveData<Result<UserDetail>> getUserDetail(String id) {
        MutableLiveData<Result<UserDetail>> liveData = new MutableLiveData<>();

        if (id == null) {
            liveData.setValue(Result.error("Đã có lỗi xảy ra. Vui lòng đăng nhập lại!"));
            return liveData;
        }

        liveData.setValue(Result.loading());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            UserDetail userDetail = new UserDetail(user);
                            String departmentId = user.getDepartmentId();
                            DepartmentService departmentService = new DepartmentService();
                            departmentService.getDepartment(departmentId, new DepartmentService.DepartmentCallback() {
                                @Override
                                public void onSuccess(Department department) {
                                    userDetail.setDepartment(department);
                                    liveData.setValue(Result.success(userDetail));
                                }

                                @Override
                                public void onFailure(Exception e) {
//                                    liveData.setValue(Result.error("Không thể tải phòng ban: " + e.getMessage()));
                                }
                            });
                        } else {
                            liveData.setValue(Result.error("Không thể ánh xạ dữ liệu người dùng."));
                        }
                        liveData.setValue(Result.success(new UserDetail(user)));
                    } else {
                        liveData.setValue(Result.error("Đã có lỗi xảy ra!"));
                    }
                })
                .addOnFailureListener(e -> {
                    liveData.setValue(Result.error("Lỗi tải dữ liệu: " + e.getMessage()));
                });

        return liveData;
    }

    public LiveData<Result<UserDetail>> updateProfile(UserDetail userDetail) {
        MutableLiveData<Result<UserDetail>> liveData = new MutableLiveData<>();

        if (userDetail == null || userDetail.getId() == null) {
            liveData.setValue(Result.error("Thông tin người dùng không hợp lệ."));
            return liveData;
        }

        liveData.setValue(Result.loading());
        User user = new User(userDetail);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(unused -> {
                    liveData.setValue(Result.success(userDetail, "Cập nhật thành công!"));
                })
                .addOnFailureListener(e -> {
                    liveData.setValue(Result.error("Cập nhật thất bại: " + e.getMessage()));
                });

        return liveData;
    }
}