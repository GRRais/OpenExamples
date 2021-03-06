package ru.rayanis.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.rayanis.firestore.databinding.ActivityMainBinding

class  MainActivity : AppCompatActivity() {
    private val personCollectionRef = Firebase.firestore.collection("persons")
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        b.btnUploadData.setOnClickListener {
            val person = getOldPerson()
            savePerson(person)
        }

        b.btnRetrieveData.setOnClickListener {
            retrievePerson()
        }

        b.btnUpdatePerson.setOnClickListener {
            val oldPerson = getOldPerson()
            val newPersonMap = getNewPersonMap()
            updatePerson(oldPerson, newPersonMap)
        }

        b.btnDeletePerson.setOnClickListener {
            val person = getOldPerson()
            deletePerson(person)
        }

        b.btnBatchWrite.setOnClickListener {
            changeName("7Ws4U4dFk7W1BaGBIgM4", "Elon", "Musk")
        }

        b.btnTransaction.setOnClickListener {
            birthday("7Ws4U4dFk7W1BaGBIgM4")
        }
    }

    private fun getOldPerson(): Person {
        val firstName = b.etFirstName.text.toString()
        val lastName = b.etLastName.text.toString()
        val age = b.etAge.text.toString().toInt()
        return Person(firstName, lastName, age)
    }

    private fun getNewPersonMap(): Map<String, Any> {
        val firstName = b.etNewFirstName.text.toString()
        val lastName = b.etNewLastName.text.toString()
        val age = b.etNewAge.text.toString()
        val map = mutableMapOf<String, Any>()
        if (firstName.isNotEmpty()) {
            map["firstName"] = firstName
        }
        if (lastName.isNotEmpty()) {
            map["lastName"] = lastName
        }
        if (age.isNotEmpty()) {
            map["age"] = age.toInt()
        }
        return map
    }

    private fun deletePerson(person: Person) = CoroutineScope(Dispatchers.IO)
            .launch {
                val personQuery = personCollectionRef
                        .whereEqualTo("firstName", person.firstName)
                        .whereEqualTo("lastName", person.lastName)
                        .whereEqualTo("age", person.age)
                        .get()
                        .await()
                if (personQuery.documents.isNotEmpty()) {
                    for (document in personQuery) {
                        try {
                            personCollectionRef.document(document.id).delete().await()
//                            personCollectionRef.document(document.id).update(mapOf(
//                                "firstName" to FieldValue.delete()))
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@MainActivity, e.message,
                                        Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "No person matched the query",
                                Toast.LENGTH_LONG).show()
                    }
                }
            }

    private fun updatePerson(person: Person, newPersonMap: Map<String, Any>) = CoroutineScope(Dispatchers.IO)
        .launch {
            val personQuery = personCollectionRef
                .whereEqualTo("firstName", person.firstName)
                .whereEqualTo("lastName", person.lastName)
                .whereEqualTo("age", person.age)
                .get()
                .await()
            if (personQuery.documents.isNotEmpty()) {
                for (document in personQuery) {
                    try {
                        personCollectionRef.document(document.id).set(
                            newPersonMap,
                            SetOptions.merge()
                        ).await()
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, e.message,
                                Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "No person matched the query",
                        Toast.LENGTH_LONG).show()
                }
            }
        }

    private  fun  birthday(personId: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            Firebase.firestore.runTransaction { transaction ->
                val personRef = personCollectionRef.document(personId)
                val person = transaction.get(personRef)
                val newAge = person["age"] as Long + 1
                transaction.update(personRef, "age", newAge)
                null
            }.await()
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@MainActivity, e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun changeName(
        personId: String,
        newFirstName: String,
        newLastName: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        try {
            Firebase.firestore.runBatch { batch ->
                val personRef = personCollectionRef.document(personId)
                batch.update(personRef, "firstName", newFirstName)
                batch.update(personRef, "lastName", newLastName)
            }.await()
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message,
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun subscribeToRealtimeUpdates() {
        personCollectionRef.addSnapshotListener { querySnapshot, firebaseFireStoreException ->
            firebaseFireStoreException?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val sb = StringBuilder()
                for (document in it) {
                    val person = document.toObject<Person>()
                    sb.append("$person\n")
                }
                b.tvPersons.text = sb.toString()
            }
        }
    }

    private fun retrievePerson() = CoroutineScope(Dispatchers.IO).launch {
        val fromAge = b.etFrom.text.toString().toInt()
        val toAge = b.etTo.text.toString().toInt()
        try {
            val querySnapshot = personCollectionRef
                .whereGreaterThan("age", fromAge)
                .whereLessThan("age", toAge)
                .orderBy("age")
                .get()
                .await()
            val sb = StringBuilder()
            for (document in querySnapshot.documents) {
                val person = document.toObject<Person>()
                sb.append("$person\n")
            }
            withContext(Dispatchers.Main) {
                b.tvPersons.text = sb.toString()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun savePerson(person: Person)  = CoroutineScope(Dispatchers.IO).launch {
        try {
            personCollectionRef.add(person).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Successfully saved data.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}