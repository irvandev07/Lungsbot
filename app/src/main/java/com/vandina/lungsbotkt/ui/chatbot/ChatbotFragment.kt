package com.vandina.lungsbotkt.ui.chatbot

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.vandina.lungsbotkt.R
import com.vandina.lungsbotkt.data.Jawaban
import com.vandina.lungsbotkt.data.Message
import com.vandina.lungsbotkt.ui.MessagingAdapter
import com.vandina.lungsbotkt.ui.SelectionAdapter
import com.vandina.lungsbotkt.utils.BotResponse
import com.vandina.lungsbotkt.utils.Constants.RECEIVE_ID
import com.vandina.lungsbotkt.utils.Constants.SEND_ID
import com.vandina.lungsbotkt.utils.Time
import kotlinx.android.synthetic.main.fragment_chatbot.*
import kotlinx.android.synthetic.main.fragment_chatbot.view.*
import kotlinx.coroutines.*
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class ChatbotFragment : Fragment() {
    private val TAG = "ChatbotFragment"

    //You can ignore this messageList if you're coming from the tutorial,
    // it was used only for my personal debugging
    var messagesList = mutableListOf<Message>()
    var tanyaJawab = mutableListOf<String>()
    var jawabanList = ArrayList<Jawaban>()
    var hasilJawab = mutableListOf<String>()
    var hasilDiagnosa = mutableListOf<String>()

    //lateinit var sharedPreferances: SharedPreferances
    private lateinit var adapter: MessagingAdapter
    private lateinit var rv_message: RecyclerView
    private lateinit var rv_selection : RecyclerView
    private lateinit var btn_send: ImageButton
    private lateinit var et_message: EditText
    private lateinit var loading_itemView : LottieAnimationView

    lateinit var handler: Handler


    private lateinit var selectionAdapter: SelectionAdapter
    private lateinit var ref: DatabaseReference
    private var firebaseUser: FirebaseUser?= null

    private var firebaseUserId : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_chatbot, container, false)

            rv_message = root.findViewById(R.id.rv_messages) as RecyclerView
            rv_selection = root.findViewById(R.id.rv_selection) as RecyclerView
            btn_send = root.findViewById(R.id.btn_send) as ImageButton
            et_message = root.findViewById(R.id.et_message) as EditText
            loading_itemView = root.findViewById(R.id.loading_itemView) as LottieAnimationView

            rv_selection.visibility = View.GONE
            root.layoutInput.visibility = View.GONE

            recyclerView()

            clickEvents()
            fristBot()

        return root
    }

    private fun clickEvents() {

        //Send a message
        btn_send.setOnClickListener {
            sendMessage()
        }

        //Scroll back to correct position when user clicks on text view
        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    rv_messages.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun fristBot() {
        //val namaDepan = sharedPreferances.getSessionString(SharedPreferances.key_value)

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                delay(2000)
                loading_itemView.visibility = View.VISIBLE
            }
        }
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                delay(4000)
                customBotMessage("\uD83D\uDC4B Hallo!").apply {
                    loading_itemView.visibility = View.GONE
                }
            }
        }
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                delay(5000)
                customBotMessage("Apakah merasakan sesak nafas?").apply {
                    ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                    ref.orderByKey().startAt("1").endAt("2").addValueEventListener(object :
                        ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            jawabanList.clear()

                            for (h in p0.children) {
                                val jawaban = h.getValue(Jawaban::class.java)
                                if (jawaban != null) {
                                    jawabanList.add(jawaban)
                                }
                            }
                            PilihanRecView()
                        }
                    })

                    handler = Handler()
                    handler.postDelayed({
                        rv_selection.visibility = View.VISIBLE
                    }, 3000)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //In case there are messages, scroll to bottom when re-opening app
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter ()
        rv_message.adapter = adapter
        rv_message.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun sendMessage() {
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //Adds it to our local list
            messagesList.add(Message(message, SEND_ID, timeStamp))
            et_message.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rv_messages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {

                val response = BotResponse.basicResponses(message)


                messagesList.add(Message(response, RECEIVE_ID, timeStamp))


                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))


                rv_messages.scrollToPosition(adapter.itemCount - 1)

            }
        }
    }

    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                //messagesList.add(Message(message, RECEIVE_ID,timeStamp))
                //allMessage.add(message)
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun PilihanRecView() {

        selectionAdapter = SelectionAdapter(requireContext(), jawabanList)
        rv_selection.adapter = selectionAdapter
        rv_selection.layoutManager = FlexboxLayoutManager(requireContext())


        selectionAdapter.onItemClick = { jawaban ->
            // do something with your item
            val timeStamp = Time.timeStamp()

            hasilJawab.add(jawaban.jawaban)
            //allMessage.add(jawaban.jawaban)


            adapter.insertMessage(Message(jawaban.jawaban, SEND_ID, timeStamp))
            //rv_selection.visibility = View.INVISIBLE

            rv_messages.scrollToPosition(adapter.itemCount - 1)

            //botResponse(jawaban.jawaban)

            botAi(jawaban.jawaban)
        }
    }

    private fun botAi(jawaban: String) {
        if (jawaban.equals("Ya")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Kapan anda Merasakan Sesak Nafas?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("3").endAt("5").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Tidak")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Anda tidak asma").apply {
                        loading_itemView.visibility = View.INVISIBLE
                    }
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(3000)
                    customBotMessage("Terimakasih").apply {
                        loading_itemView.visibility = View.INVISIBLE
                    }
                }
            }
        }else if (jawaban.equals("Saat Istirahat")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Bagaimana posisi anda?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("20").endAt("22").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Saat Berjalan")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Bagaimana posisi anda?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("20").endAt("22").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Saat Berbicara")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Bagaimana posisi anda?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("20").endAt("22").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Bisa berbaring")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Bagaimana cara berbicara anda?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("6").endAt("8").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Lebih suka duduk")) {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Bagaimana cara berbicara anda?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("6").endAt("8").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Duduk bertopang lengan")) {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Bagaimana cara berbicara anda?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("6").endAt("8").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Kata Demi Kata")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Bagaimana kondisi anda saat ini?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("9").endAt("10").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Satu Kalimat")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Bagaimana kondisi anda saat ini?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("9").endAt("10").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Beberapa Kata")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Bagaimana kondisi anda saat ini?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("9").endAt("10").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Gelisah")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Berapakah Frekuensi Nafas anda?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("11").endAt("13").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("Mungkin Gelisah")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Berapakah Frekuensi Nafas anda?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("11").endAt("13").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("<20/menit")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Berapakah banyak denyut nadi anda permenit?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("14").endAt("16").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("20-30/menit")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Berapakah banyak denyut nadi anda permenit?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("14").endAt("16").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals(">30/menit")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Berapakah banyak denyut nadi anda permenit?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("14").endAt("16").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("<100")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Apakah Nafas anda terdengar suara melengking?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("17").endAt("19").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals("100-200")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Apakah Nafas anda terdengar suara melengking?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("17").endAt("19").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else if (jawaban.equals(">120")){
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    rv_selection.visibility = View.GONE
                    delay(1000)
                    loading_itemView.visibility = View.VISIBLE
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    delay(2000)
                    customBotMessage("Apakah Nafas anda terdengar suara melengking?").apply {
                        loading_itemView.visibility = View.INVISIBLE
                        ref = FirebaseDatabase.getInstance().getReference("Jawaban")
                        ref.orderByKey().startAt("17").endAt("19").addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                jawabanList.clear()

                                for (h in p0.children) {
                                    val jawaban = h.getValue(Jawaban::class.java)
                                    if (jawaban != null) {
                                        jawabanList.add(jawaban)
                                    }
                                }
                                PilihanRecView()
                            }
                        })
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                delay(2000)
                                rv_selection.visibility = View.VISIBLE
                            }
                        }
                    }
                }

            }
        }else {
        }
    }



    private fun saveDiagnosa(){
        firebaseUser = FirebaseAuth.getInstance().currentUser
        ref = FirebaseDatabase.getInstance().getReference("User")
        firebaseUserId = firebaseUser!!.uid

        val timeStamp = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val time = sdf.format(Date(timeStamp.time))

        val hasilJawab = hasilJawab.joinToString()
        val hasilDiagnosa = hasilDiagnosa.joinToString()
        val tanyaJawab = tanyaJawab.joinToString()
        val tglJam = time
        val rid = System.currentTimeMillis()
        //val riwayat = FirebaseDatabase.getInstance().reference.child(firebaseUserId).child("Riwayat")
        ref.child(firebaseUserId).child("riwayat").child(rid.toString()).child("rid").setValue(rid)
        ref.child(firebaseUserId).child("riwayat").child(rid.toString()).child("hasilDiagnosa").setValue(
            hasilDiagnosa
        )
        ref.child(firebaseUserId).child("riwayat").child(rid.toString()).child("hasilJawab").setValue(
            hasilJawab
        )
        ref.child(firebaseUserId).child("riwayat").child(rid.toString()).child("tanyaJawab").setValue(
            tanyaJawab
        )
        ref.child(firebaseUserId).child("riwayat").child(rid.toString()).child("waktu").setValue(
            tglJam
        )
    }

}




