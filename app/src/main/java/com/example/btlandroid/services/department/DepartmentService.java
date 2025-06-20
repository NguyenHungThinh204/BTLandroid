package com.example.btlandroid.services.department;

import com.example.btlandroid.models.Department;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
    public interface DepartmentCallback {
        void onSuccess(Department department);

        void onFailure(Exception e);
    }

    public interface DepartmentsCallback {
        void onSuccess(List<Department> departments);

        void onFailure(Exception e);
    }

    public void getDepartment(String id, DepartmentCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (StringUtils.isBlank(id)) {
            callback.onSuccess(null);
            return;
        }

        db.collection("departments")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Department department = documentSnapshot.toObject(Department.class);
                        callback.onSuccess(department);
                    } else {
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getDepartments(DepartmentsCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("departments")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Department> departmentList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Department department = doc.toObject(Department.class);
                        departmentList.add(department);
                    }
                    callback.onSuccess(departmentList);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
