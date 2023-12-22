package com.assesments.madassesmenttwo

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.madassesmenttwo.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    private lateinit var fetchButton: Button
    private lateinit var petSpinner: Spinner
    private lateinit var petImageView: ImageView

    private val catApiinterface: CatApiInter = Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatApiInter::class.java)

    private val dogApiinterface: DogApiInterface = Retrofit.Builder()
        .baseUrl("https://random.dog/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DogApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        petSpinner = findViewById(R.id.cmb_pet)
        fetchButton = findViewById(R.id.fetchButton)
        petImageView = findViewById(R.id.img_pet)

        val petTypes = arrayOf("Cat", "Dog")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, petTypes)
        petSpinner.adapter = adapter

        petSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val selectedPetType = petTypes[position]
                if (selectedPetType == "Cat") {
                    fetchCatImage()
                } else if (selectedPetType == "Dog") {
                    fetchDogImage()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        fetchButton.setOnClickListener {
            val selectedPetType = petTypes[petSpinner.selectedItemPosition]
            if (selectedPetType == "Cat") {
                fetchCatImage()
            } else if (selectedPetType == "Dog") {
                fetchDogImage()
            }
        }
    }

    private fun fetchCatImage() {
        catApiinterface.getCatImage().enqueue(object : Callback<List<CatImage>> {
            override fun onResponse(call: Call<List<CatImage>>, response: Response<List<CatImage>>) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    val imageUrl = response.body()?.get(0)?.url
                    runOnUiThread {
                        if (imageUrl != null) {
                            loadImage(imageUrl)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<CatImage>>, t: Throwable) {
            }
        })
    }

    private fun fetchDogImage() {
        dogApiinterface.getDogImage().enqueue(object : Callback<DogImage> {
            override fun onResponse(call: Call<DogImage>, response: Response<DogImage>) {
                if (response.isSuccessful && response.body() != null) {
                    val imageUrl = response.body()?.url
                    runOnUiThread {
                        imageUrl?.let { loadImage(it) }
                    }
                }
            }

            override fun onFailure(call: Call<DogImage>, t: Throwable) {
            }
        })
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .into(petImageView)
    }
}
