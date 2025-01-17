package io.github.mamedovilkin.core.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.github.mamedovilkin.core.model.Asset
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CoreRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
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

    private fun clearOldBackup(uid: String) {
        firebaseDatabase
            .getReferenceFromUrl("https://finex-etf-default-rtdb.europe-west1.firebasedatabase.app")
            .child("users")
            .child(uid)
            .child("assets").removeValue()
    }

    fun backupAsset(uid: String, assets: List<io.github.mamedovilkin.database.entity.Asset>) {
        clearOldBackup(uid)

        assets.forEach {
            val asset = Asset(
                it.id,
                it.ticker,
                it.icon,
                it.name,
                it.originalName,
                it.isActive,
                it.navPrice,
                it.currencyNav,
                it.quantity,
                it.datetime,
                it.price,
                it.type,
            )

            firebaseDatabase
                .getReferenceFromUrl("https://finex-etf-default-rtdb.europe-west1.firebasedatabase.app")
                .child("users")
                .child(uid)
                .child("assets")
                .child(asset.id.toString())
                .setValue(asset)
        }
    }

    fun getBackup(uid: String): LiveData<List<Asset>> {
        val assetsLiveData = MutableLiveData<List<Asset>>()

        firebaseDatabase
            .getReferenceFromUrl("https://finex-etf-default-rtdb.europe-west1.firebasedatabase.app")
            .child("users")
            .child(uid)
            .child("assets")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userList = mutableListOf<Asset>()

                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(Asset::class.java)
                        user?.let { userList.add(it) }
                    }

                    assetsLiveData.value = userList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("CoreRepository", error.message)
                }
            })

        return assetsLiveData
    }
}
