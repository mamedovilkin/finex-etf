package io.github.mamedovilkin.core.repository

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.github.mamedovilkin.database.database.AssetDatabase
import io.github.mamedovilkin.database.entity.Asset
import io.github.mamedovilkin.database.util.Converter
import io.github.mamedovilkin.database.util.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CoreRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val assetDatabase: AssetDatabase,
) {

    suspend fun signInWithGoogle(idToken: String): FirebaseUser? {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            authResult.user
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    fun signOut() = firebaseAuth.signOut()

    fun backupAsset(uid: String, assets: List<Asset>) {
        firebaseFirestore
            .collection("users")
            .document(uid)
            .collection("assets")
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot) {
                    firebaseFirestore
                        .collection("users")
                        .document(uid)
                        .collection("assets")
                        .document(document.id)
                        .delete()
                }
            }
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    assets.forEach { asset ->
                        firebaseFirestore
                            .collection("users")
                            .document(uid)
                            .collection("assets")
                            .document(asset.id)
                            .set(asset)
                    }
                }
            }
    }

    fun getBackup(uid: String) {
        firebaseFirestore
            .collection("users")
            .document(uid)
            .collection("assets")
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot) {
                    CoroutineScope(Dispatchers.IO).launch {
                        assetDatabase.getDao().insert(Asset((document.data["id"] ?: "").toString(), (document.data["ticker"] ?: "").toString(), (document.data["icon"] ?: "").toString(), (document.data["name"] ?: "").toString(), (document.data["originalName"] ?: "").toString(), (document.data["isActive"] ?: true) as Boolean, (document.data["navPrice"] ?: 0.0) as Double, (document.data["currencyNav"] ?: "").toString(), (document.data["quantity"] ?: 0L) as Long, (document.data["datetime"] ?: 0L) as Long, (document.data["price"] ?: 0.0) as Double,  ((document.data["type"] ?: Converter.fromType(Type.PURCHASE)).toString())))
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("CoreRepository", e.message.toString())
            }
    }
}
