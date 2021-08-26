package com.sanjay.basicconvertorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

data class Convertor(
    val label: String,
    val func: (Double) -> Double,
)

const val CENTIMETERS = "Centimeters"
const val METERS = "Meters"

fun centimetersToMeters(centimeters: Double): Double = if (centimeters == 0.0) 0.0 else (centimeters / 100.0)
fun metersToCentimeters(meters: Double): Double = meters * 100.0

class MainActivity : AppCompatActivity() {
    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private lateinit var label1: TextView
    private lateinit var label2: TextView
    private lateinit var switchBtn: Button

    private var convertor1 = Convertor(CENTIMETERS, ::centimetersToMeters)
    private var convertor2 = Convertor(METERS, ::metersToCentimeters)

    private var ignoreSync = false

    private fun swapConvertors() {
        val tmp = convertor1
        convertor1 = convertor2
        convertor2 = tmp
    }

    private fun syncLabelState() {
        label1.text = convertor1.label
        label2.text = convertor2.label
        editText1.hint = convertor1.label
        editText2.hint = convertor2.label
    }

    private fun syncConvertor1State() {
        val input = editText1.text.toString().toDoubleOrNull()
        val output = if (input == null) "" else convertor1.func(input).toString()
        ignoreSync = true
        editText2.setText(output)
    }

    private fun syncConvertor2State() {
        val input = editText2.text.toString().toDoubleOrNull()
        val output = if (input == null) "" else convertor2.func(input).toString()
        ignoreSync = true
        editText1.setText(output)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText1 = findViewById(R.id.editText1)
        editText2 = findViewById(R.id.editText2)
        label1 = findViewById(R.id.label1)
        label2 = findViewById(R.id.label2)
        switchBtn = findViewById(R.id.switchBtn)

        syncLabelState()

        switchBtn.setOnClickListener {
            swapConvertors()
            syncLabelState()
            syncConvertor1State()
        }

        editText1.addTextChangedListener {
            if (ignoreSync) {
                ignoreSync = false
                return@addTextChangedListener
            }
            syncConvertor1State()
        }

        editText2.addTextChangedListener {
            if (ignoreSync) {
                ignoreSync = false
                return@addTextChangedListener
            }
            syncConvertor2State()
        }

    }
}