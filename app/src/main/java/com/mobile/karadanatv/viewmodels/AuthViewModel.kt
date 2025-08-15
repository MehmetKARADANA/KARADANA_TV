package com.mobile.karadanatv.viewmodels


import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.karadanatv.data.USERS
import com.mobile.karadanatv.data.User
import com.mobile.karadanatv.utils.CheckSignedIn
import com.mobile.karadanatv.utils.isShortOrLong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AuthViewModel : BaseViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData.asStateFlow()

    private val _inProcess = MutableStateFlow(false)
    val inProcess: StateFlow<Boolean> = _inProcess.asStateFlow()

    private val _signIn = MutableStateFlow(false)
    val signIn: StateFlow<Boolean> = _signIn.asStateFlow()
    init {
        val currentUser = auth.currentUser
        _signIn.value = currentUser != null
        currentUser?.uid?.let { getUserData(it) }
    }

    private fun getUserData(uid: String) {
        viewModelScope.launch {
            _inProcess.value = true
            try {
                val snapshot = db.collection(USERS).document(uid).get().await()
                val user = snapshot.toObject(User::class.java)
                _userData.value = user
            } catch (e: Exception) {
                handleException(e, "Kullanıcı verisi alınamadı.")
            } finally {
                _inProcess.value = false
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        _inProcess.value = true
        if (name.isEmpty() or email.isEmpty() or password.isEmpty()) {
            _inProcess.value = false
            return
        }

        if (isShortOrLong(password)) {
            handleException(customMessage = "Şifreniz 6 karakterden kısa olamaz.")
            _inProcess.value = false
            return
        }

        db.collection(USERS).where(
            Filter.or(
                Filter.equalTo("name", name),
                Filter.equalTo("email", email)
            )
        ).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _signIn.value = true
                        Log.d("SignUp", "signUp: User Logged In")
                        createProfile(name, email)
                        _inProcess.value = false
                        handleException(customMessage = "Kayıt Olundu.")
                    } else {
                        handleException(it.exception, it.exception?.message.toString())
                        _inProcess.value = false
                    }
                }

            } else {
                handleException(customMessage = "Aynı bilgilerde kullanıcı bulunuyor.")
                _inProcess.value = false
            }
        }
    }


    fun login(email: String, password: String) {
        _inProcess.value = true
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Alanları Doldurun")
            _inProcess.value = false
            return
        }

        if (isShortOrLong(password)) {
            handleException(customMessage = "Şifreniz 6 karakterden kısa olamaz.")
            _inProcess.value = false
            return
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _signIn.value = true
                _inProcess.value = false
                auth.currentUser?.uid?.let {
                    getUserData(it)
                }
            } else {
                handleException(it.exception, "Bilgilerinizi kontrol edin.")
                _inProcess.value = false
            }
        }
    }


    private fun createProfile(name: String, email: String) {
        var uid = auth.currentUser?.uid
        val userData = User(
            uid = uid.toString(),
            name = name,
            email = email
        )

        uid?.let {
            _inProcess.value = true
            db.collection(USERS).document(uid).set(userData).addOnCompleteListener {
                _inProcess.value = false
                it.exception?.let {
                    handleException(it, it.message.toString())
                }
            }
            getUserData(uid)
        }
    }

    fun logout() {
        auth.signOut()
        _userData.value = null
        _signIn.value = false
        handleException(customMessage = "Çıkış Yapıldı!")
    }
}