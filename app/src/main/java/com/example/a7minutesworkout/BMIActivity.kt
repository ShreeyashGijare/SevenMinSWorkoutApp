package com.example.a7minutesworkout

import android.icu.math.BigDecimal
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigInteger
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNIT_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNIT_VIEW = "US_UNIT_VIEW"
    }

    private var binding: ActivityBmiBinding? = null
    private var currentVisibleView: String = METRIC_UNIT_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricsUnitView()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if(checkedId == R.id.rbMetricUnit){
                makeVisibleMetricsUnitView()
            }else {
                makeVisibleUsUnitView()
            }
        }

        binding?.btnCalculate?.setOnClickListener {

            calculateUnit()
        }
    }

    private fun makeVisibleMetricsUnitView(){
        currentVisibleView = METRIC_UNIT_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE //Metric Unit Weight
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE //Metric Unit Height
        binding?.tilUSUnitWeight?.visibility = View.GONE        //US Unit Weight
        binding?.tilUSUnitHeightFeet?.visibility = View.GONE    //US Unit Height Feet
        binding?.tilUSUnitHeightInch?.visibility = View.GONE    //US Unit Height Inch

        binding?.etMetricUnitHeight?.text!!.clear() //height value is cleared if US view is selected
        binding?.etMetricUnitWeight?.text!!.clear() //weight value is cleared if US view is selected

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsUnitView() {
        currentVisibleView = US_UNIT_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE //Metric Unit Weight
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE //Metric Unit Height
        binding?.tilUSUnitWeight?.visibility = View.VISIBLE        //US Unit Weight
        binding?.tilUSUnitHeightFeet?.visibility = View.VISIBLE    //US Unit Height Feet
        binding?.tilUSUnitHeightInch?.visibility = View.VISIBLE    //US Unit Height Inch

        binding?.etUSUnitWeight?.text!!.clear()
        binding?.etUSUnitHeightFeet?.text!!.clear()
        binding?.etUSUnitHeightInch?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription: String

        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very Severly Underweight"
            bmiDescription = "Oops! You ned to take care of yourself! Eat More!."
        }else if(bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severly Underweight"
            bmiDescription = "Oops! You ned to take care of yourself! Eat More!."
        }else if(bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You ned to take care of yourself! Eat More!."
        }else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in good shape!."
        }else if(bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You ned to take care of yourself! workout More!."
        }else if(bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese Class | (Moderately Obese)"
            bmiDescription = "Oops! You ned to take care of yourself! workout More!."
        }else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese Class || (Severely Obese)"
            bmiDescription = "Oops! You ned to take care of yourself! workout More!."
        } else {
            bmiLabel = "Obese Class ||| (Very Severely Obese)"
            bmiDescription = "OMG! You are in very dangerous condition! Act Now!."
        }

        val bmiValue = java.math.BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription

    }

    private fun calculateUnit() {
        if(currentVisibleView == METRIC_UNIT_VIEW){
            if(validateMetricUnit()){
                val heightValue: Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue*heightValue)

                displayBMIResult(bmi)
            }else{
                Toast.makeText(this@BMIActivity, "Please enter valid values", Toast.LENGTH_LONG).show()
            }
        }else {
            if (validateUSUnit()) {
                val usUnitHeightFeetValue: String = binding?.etUSUnitHeightFeet?.text.toString()
                val usUnitHeightInchValue: String = binding?.etUSUnitHeightInch?.text.toString()
                val usUnitWeightValue: Float = binding?.etUSUnitWeight?.text.toString().toFloat()

                val heightValue = usUnitHeightInchValue.toFloat() + usUnitHeightFeetValue.toFloat() * 12

                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                displayBMIResult(bmi)
            } else {
                Toast.makeText(this@BMIActivity, "Please enter valid values", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateMetricUnit():Boolean{
        var isValid = true
        if(binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun validateUSUnit():Boolean{
        var isValid = true
        when {
            binding?.etUSUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUSUnitHeightFeet?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUSUnitHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }

}