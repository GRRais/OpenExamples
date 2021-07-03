package ru.rayanis.stroyka.accounthelper

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import ru.rayanis.stroyka.MainActivity
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.dialoghelper.GoogleAccConst

class AccountHelper(act: MainActivity) {
    private val activity = act
    private lateinit var signInClient: GoogleSignInClient

    //функция регистрации по email
    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            activity.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    sendEmailVerification(task.result?.user!!)
                    activity.uiUpdate(task.result?.user)
                } else {
                    Toast.makeText(activity, activity.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id)).build()
        return GoogleSignIn.getClient(activity, gso)
    }

    fun signInWithGoogle(){
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        activity.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    //функция регистрации по email
    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            activity.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    activity.uiUpdate(task.result?.user)
                } else {
                    Toast.makeText(activity, activity.resources.getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, activity.resources.getString(R.string.send_verification_done), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, activity.resources.getString(R.string.send_verification_error), Toast.LENGTH_LONG).show()
            }
        }
    }
}