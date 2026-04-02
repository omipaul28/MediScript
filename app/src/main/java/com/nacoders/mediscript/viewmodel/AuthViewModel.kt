package com.nacoders.mediscript.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.entity.DoctorEntity
import com.nacoders.mediscript.navigation.NavRoutes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val doctorDao = AppDatabase.getInstance(application).doctorDao()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    val doctorProfile: StateFlow<DoctorEntity?> = doctorDao.getDoctor()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun registerDoctor(
        name: String,
        email: String,
        phone: String,
        hospital: String,
        pass: String,
        navController: NavController
    ) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Please fill required fields"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null
        _successMessage.value = null

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                val user = result.user
                user?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        val userId = user.uid
                        val doctor = DoctorEntity(userId, name, email, phone, hospital)

                        // Save profile to Firestore and Local DB
                        db.collection("doctors").document(userId).set(doctor)
                            .addOnSuccessListener {
                                viewModelScope.launch {
                                    doctorDao.insertDoctor(doctor)
                                    // Sign out immediately; user must verify before login
                                    auth.signOut()
                                    _isLoading.value = false
                                    _successMessage.value = "Verification email sent to $email. Please verify to login."
                                    navController.navigate(NavRoutes.LOGIN) {
                                        popUpTo(NavRoutes.REGISTER) { inclusive = true }
                                    }
                                }
                            }
                            .addOnFailureListener {
                                _isLoading.value = false
                                _errorMessage.value = "Profile setup failed: ${it.localizedMessage}"
                            }
                    }
                    ?.addOnFailureListener {
                        _isLoading.value = false
                        _errorMessage.value = "Account created, but failed to send verification email."
                    }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _errorMessage.value = it.localizedMessage
            }
    }

    fun loginDoctor(email: String, pass: String, navController: NavController) {
        if (email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Please enter your credentials"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null
        _successMessage.value = null

        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                val user = result.user
                // Check if email is verified before proceeding
                if (user != null) {
                    if (user.isEmailVerified) {
                        fetchAndSyncDoctor(user.uid, navController)
                    } else {
                        // Reject access and sign out if not verified
                        auth.signOut()
                        _isLoading.value = false
                        _errorMessage.value = "Please verify your email address to continue."
                    }
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _errorMessage.value = it.localizedMessage
            }
    }

    private fun fetchAndSyncDoctor(uid: String, navController: NavController) {
        db.collection("doctors").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val doctor = DoctorEntity(
                        uid = uid,
                        name = doc.getString("name") ?: "",
                        email = doc.getString("email") ?: "",
                        phone = doc.getString("phone") ?: "",
                        hospital = doc.getString("hospital") ?: ""
                    )
                    viewModelScope.launch {
                        doctorDao.insertDoctor(doctor)
                        _isLoading.value = false
                        // Use consistent navigation routes
                        navController.navigate(NavRoutes.DASHBOARD) {
                            popUpTo(NavRoutes.LOGIN) { inclusive = true }
                        }
                    }
                } else {
                    _isLoading.value = false
                    _errorMessage.value = "Doctor profile not found in cloud."
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _errorMessage.value = "Synchronization failed."
            }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            auth.signOut()
            doctorDao.clearDoctor() // Ensure local data is purged
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.DASHBOARD) { inclusive = true }
            }
        }
    }
}