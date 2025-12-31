package com.attri.swissarmy.feature.calculators.domain

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

@Singleton
class TaxCalculatorRepository @Inject constructor() {
    
    // Monthly SIP Formula: P × ({[1 + i]^n - 1} / i) × (1 + i)
    // P = Monthly amount
    // i = Periodic interest rate (rate / 12 / 100)
    // n = Total months
    fun calculateSip(monthlyInvestment: Double, rate: Double, years: Int): SipResult {
        val i = rate / 12 / 100
        val n = years * 12
        
        // Investment Amount
        val invested = monthlyInvestment * n
        
        if (rate == 0.0) {
             return SipResult(invested, 0.0, invested)
        }

        // Maturity Value
        val maturityValue = monthlyInvestment * (((1 + i).pow(n) - 1) / i) * (1 + i)
        val wealthGained = maturityValue - invested
        
        return SipResult(invested, wealthGained, maturityValue)
    }

    // Simple Interest
    fun calculateSimpleInterest(principal: Double, rate: Double, years: Int): Double {
        return (principal * rate * years) / 100
    }

    // Compound Interest: A = P(1 + r/n)^(nt)
    // Assuming annual compounding for simplicity (n=1)
    fun calculateCompoundInterest(principal: Double, rate: Double, years: Int): Double {
        val amount = principal * (1 + rate / 100).pow(years)
        return amount - principal
    }
}

data class SipResult(
    val investedAmount: Double,
    val wealthGained: Double,
    val totalValue: Double
)
