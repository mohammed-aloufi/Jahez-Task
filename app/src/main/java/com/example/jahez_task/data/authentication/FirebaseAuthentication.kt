package com.example.jahez_task.data.authentication

import com.example.jahez_task.domain.models.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthentication @Inject constructor(
    private val auth: FirebaseAuth
) : AuthProvider {

    override val isLoggedIn: Boolean
        get() = auth.currentUser?.uid != null

    override suspend fun login(email: String, password: String): AuthState {
        val result: AuthState = AuthState()
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    result.isSuccessful = it.isSuccessful
                }.addOnFailureListener {
                    result.message = it.message.toString()
                }.await()
        } catch (e: FirebaseAuthInvalidUserException) {
            result.message = e.message.toString()
        } catch (e: Exception) {
            result.message = e.message.toString()
        }

        return result
    }

    override suspend fun register(name: String, email: String, password: String): AuthState {
        val result: AuthState = AuthState()
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    result.isSuccessful = it.isSuccessful
                    val updateRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    auth.currentUser?.updateProfile(updateRequest)
                        ?.addOnCompleteListener {
                            auth.signOut()
                        }
                }.addOnFailureListener {
                    result.message = it.message.toString()
                }.await()
        } catch (e: FirebaseAuthInvalidUserException) {
            result.message = e.message.toString()
        } catch (e: Exception) {
            result.message = e.message.toString()
        }

        return result
    }
}