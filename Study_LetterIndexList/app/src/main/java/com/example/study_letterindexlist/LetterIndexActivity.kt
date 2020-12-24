package com.example.study_letterindexlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.study_letterindexlist.bean.UIContact
import com.example.study_letterindexlist.databinding.ActivityLetterIndexBinding

class LetterIndexActivity : AppCompatActivity() {
    lateinit var activityLetterIndexBinding: ActivityLetterIndexBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_index)

        initView();
        initData()
    }

    private fun initView() {
        activityLetterIndexBinding = ActivityLetterIndexBinding.inflate(layoutInflater)
    }

    private fun initData() {
        var contacts = UIContact.getContacts()
        var contactsAdapter = ContactsAdapter(this, contacts)

        activityLetterIndexBinding.listView.adapter = contactsAdapter

        var hashSet = HashSet<String>()
        for (uiContact in contacts) {
            hashSet.add(uiContact.category)
        }
        var arr = arrayOfNulls<String>(hashSet.size)
        var groups = hashSet.toArray(arr)
        activityLetterIndexBinding.livIndex.setLetters(arr)
    }
}