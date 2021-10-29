package ru.rayanis.stroyka.accounthelper

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import ru.rayanis.stroyka.MainActivity
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.constants.FirebaseAuthConstants
import ru.rayanis.stroyka.dialoghelper.GoogleAccConst

class AccountHelper(act: MainActivity) {
    private val activity = act
    private lateinit var signInClient: GoogleSignInClient

    //регистрация по email
    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            activity.mAuth.currentUser?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    activity.mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signUpWithEmailSuccessful(task.result.user!!)
                            } else {
                                signUpWithEmailException(task.exception!!, email, password)
                            }
                        }
                }
            }
        }
    }

    //проверяем
    private fun signUpWithEmailSuccessful(user: FirebaseUser) {
        sendEmailVerification(user)
        activity.uiUpdate(user)
    }

    //
    private fun signUpWithEmailException(e: Exception, email: String, password: String) {
        if (e is FirebaseAuthUserCollisionException) {
            //Log.d("MyLog", "Exception 2: ${e.errorCode}")
            if (e.errorCode == FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE) {
                linkEmailToG(email, password)
            }
        } else if (e is FirebaseAuthInvalidCredentialsException) {
            if (e.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                Toast.makeText(activity, FirebaseAuthConstants.ERROR_INVALID_EMAIL,
                    Toast.LENGTH_LONG).show()
            }
        }
        if (e is FirebaseAuthWeakPasswordException) {
            //Log.d("MyLog", "Exception: ${e.errorCode}")
            if (e.errorCode == FirebaseAuthConstants.ERROR_WEAK_PASSWORD) {
                Toast.makeText(
                    activity,
                    FirebaseAuthConstants.ERROR_WEAK_PASSWORD,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //функция регистрации по email
    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            activity.mAuth.currentUser?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    activity.mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                activity.uiUpdate(task.result?.user)
                            } else {
                                signInWithEmailException(task.exception!!, email, password)
                            }
                        }
                }
            }
        }
    }

    private fun signInWithEmailException(e: Exception, email: String, password: String) {
        //Log.d("MyLog", "Exception: ${e}")
        if (e is FirebaseAuthInvalidCredentialsException) {
            //Log.d("MyLog", "Exception 2: ${exception.errorCode}")
            if (e.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                Toast.makeText(
                    activity,
                    FirebaseAuthConstants.ERROR_INVALID_EMAIL,
                    Toast.LENGTH_LONG
                ).show()
            } else if (e.errorCode == FirebaseAuthConstants.ERROR_WRONG_PASSWORD) {
                Toast.makeText(
                    activity,
                    FirebaseAuthConstants.ERROR_WRONG_PASSWORD,
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (e is FirebaseAuthInvalidUserException) {
            if (e.errorCode == FirebaseAuthConstants.ERROR_USER_NOT_FOUND) {
                Toast.makeText(
                    activity,
                    FirebaseAuthConstants.ERROR_USER_NOT_FOUND,
                    Toast.LENGTH_LONG
                ).show()
            }
            Log.d("MyLog", "Exception 3: ${e.errorCode}")
        }
    }

    private fun linkEmailToG(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        if (activity.mAuth.currentUser != null) {
            activity.mAuth.currentUser?.linkWithCredential(credential)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            activity,
                            activity.resources.getString(R.string.link_done),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                activity,
                activity.resources.getString(R.string.enter_to_g),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id)).requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    fun signInWithGoogle() {
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        activity.googleSignInLauncher.launch(intent)
    }

    fun signOutG() {
        getSignInClient().signOut()
    }

    fun signInFirebaseWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        activity.mAuth.currentUser?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                activity.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Sign in done", Toast.LENGTH_LONG).show()
                        activity.uiUpdate(task.result?.user)
                    } else {
                        Log.d("MyLog", "Google Sign In Exception: ${task.exception}")
                    }
                }
            }
        }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    activity,
                    activity.resources.getString(R.string.send_verification_done),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    activity,
                    activity.resources.getString(R.string.send_verification_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //регистрация анонимного пользователя
    fun signInAnonymously(listener: Listener) {
        activity.mAuth.signInAnonymously().addOnCompleteListener {
            task ->
            if (task.isSuccessful) {
                listener.onComplete()
                Toast.makeText(activity, "Вы вошли как гость", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Не удалось войти как гость", Toast.LENGTH_SHORT).show()
            }
        }
    }

    interface Listener {
        fun onComplete()
    }
}