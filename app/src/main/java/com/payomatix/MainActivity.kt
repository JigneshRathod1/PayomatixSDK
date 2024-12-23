package com.payomatix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.payomatix.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            paymentMethod.setOriginalPrice("200")
            paymentMethod.setDiscountedPrice("160")
            paymentMethod.setSavedPrice("40")
        }
    }
}