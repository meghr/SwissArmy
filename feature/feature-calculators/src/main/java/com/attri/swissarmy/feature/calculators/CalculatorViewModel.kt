package com.attri.swissarmy.feature.calculators

import androidx.lifecycle.ViewModel
import com.attri.swissarmy.feature.calculators.domain.SipResult
import com.attri.swissarmy.feature.calculators.domain.TaxCalculatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val repository: TaxCalculatorRepository
) : ViewModel() {

    private val _sipState = MutableStateFlow(CalculatorUiState())
    val sipState: StateFlow<CalculatorUiState> = _sipState.asStateFlow()

    fun setCalculatorType(type: CalculatorType) {
        _sipState.update { it.copy(activeType = type) }
        calculate()
    }

    fun updateInputs(amount: String, rate: String, years: String) {
        _sipState.update { it.copy(amount = amount, rate = rate, years = years) }
        calculate()
    }

    private fun calculate() {
        val amount = _sipState.value.amount.toDoubleOrNull() ?: 0.0
        val rate = _sipState.value.rate.toDoubleOrNull() ?: 0.0
        val years = _sipState.value.years.toIntOrNull() ?: 0
        
        if (amount > 0 && rate > 0 && years > 0) {
            when(_sipState.value.activeType) {
                CalculatorType.SIP -> {
                    val result = repository.calculateSip(amount, rate, years)
                    _sipState.update { it.copy(sipResult = result, interestResult = null) }
                }
                CalculatorType.SIMPLE_INTEREST -> {
                    val interest = repository.calculateSimpleInterest(amount, rate, years)
                    _sipState.update { it.copy(interestResult = interest, sipResult = null) }
                }
                CalculatorType.COMPOUND_INTEREST -> {
                    val interest = repository.calculateCompoundInterest(amount, rate, years)
                    _sipState.update { it.copy(interestResult = interest, sipResult = null) }
                }
            }
        } else {
             _sipState.update { it.copy(sipResult = null, interestResult = null) }
        }
    }
}

enum class CalculatorType {
    SIP, SIMPLE_INTEREST, COMPOUND_INTEREST
}

data class CalculatorUiState(
    val activeType: CalculatorType = CalculatorType.SIP,
    val amount: String = "5000",
    val rate: String = "12",
    val years: String = "5",
    val sipResult: SipResult? = null,
    val interestResult: Double? = null
)
