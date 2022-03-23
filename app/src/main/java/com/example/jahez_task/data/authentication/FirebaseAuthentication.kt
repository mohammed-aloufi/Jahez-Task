package com.example.jahez_task.data.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthentication @Inject constructor(
    private val auth: FirebaseAuth
) : AuthProvider {

    override suspend fun login(email: String, password: String): Boolean {
        var isSuccess = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                isSuccess = true
            }.addOnFailureListener {
                throw it
            }.await()

        return isSuccess
    }

    override suspend fun register(name: String, email: String, password: String): Boolean {
        var isSuccess = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                isSuccess = it.isSuccessful
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                auth.currentUser?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener {
                        auth.signOut()
                    }
            }.addOnFailureListener {
                throw it
            }.await()

        return isSuccess
    }
}