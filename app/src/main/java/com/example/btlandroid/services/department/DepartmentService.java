package com.example.btlandroid.services.department;

import com.example.btlandroid.models.Department;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DepartmentService {
    public interface DepartmentCallback {
        void onSuccess(List<Department> departments);

        void onFailure(Exception e);
    }

    public void getDepartments(List<String> ids, DepartmentCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Department> departments = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);

        if (ids == null || ids.isEmpty()) {
            callback.onSuccess(departments);
            return;
        }

        db.collection("departments")
                .whereIn("id", ids)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Department department = document.toObject(Department.class);
                        departments.add(department);
                        counter.incrementAndGet();
                        if (counter.get() == ids.size()) {
                            callback.onSuccess(departments);
                        }
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
}
