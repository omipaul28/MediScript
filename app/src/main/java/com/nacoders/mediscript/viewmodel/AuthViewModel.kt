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

    val doctorProfile: StateFlow<DoctorEntity?> = doctorDao.getDoctor()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun registerDoctor(name: String, email: String, phone: String, hospital: String, pass: String, navController: NavController) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Please fill required fields"
            return
        }

        _isLoading.value = true
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                val userId = result.user?.uid
                val doctor = DoctorEntity(userId!!, name, email, phone, hospital)

                // Save to Firestore
                db.collection("doctors").document(userId).set(doctor)
                    .addOnSuccessListener {
                        viewModelScope.launch {
                            doctorDao.insertDoctor(doctor)
                            _isLoading.value = false
                            navController.navigate(NavRoutes.DASHBOARD) {
                                popUpTo(NavRoutes.REGISTER) { inclusive = true }
                            }
                        }
                    }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _errorMessage.value = it.localizedMessage
            }
    }

    fun loginDoctor(email: String, pass: String, navController: NavController) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid != null) {
                    fetchAndSyncDoctor(uid, navController)
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
                        navController.navigate(NavRoutes.DASHBOARD) {
                            popUpTo(NavRoutes.LOGIN) { inclusive = true }
                        }
                    }
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _errorMessage.value = "Failed to fetch profile"
            }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            auth.signOut()
            doctorDao.clearDoctor()
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.DASHBOARD) { inclusive = true }
            }
        }
    }
}