package com.attri.swissarmy.feature.calculators

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.attri.swissarmy.core.ui.components.SwissButton
import com.attri.swissarmy.core.ui.components.SwissCard
import com.attri.swissarmy.feature.calculators.domain.SipResult
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CalculatorRoute(
    viewModel: CalculatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.sipState.collectAsState()
    
    CalculatorScreen(
        uiState = uiState,
        onInputsChange = viewModel::updateInputs,
        onTypeChange = viewModel::setCalculatorType
    )
}

@Composable
fun CalculatorScreen(
    uiState: CalculatorUiState,
    onInputsChange: (String, String, String) -> Unit,
    onTypeChange: (CalculatorType) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Calc Tools",
                style = MaterialTheme.typography.headlineMedium
            )

            // Type Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorType.entries.forEach { type ->
                    val isSelected = uiState.activeType == type
                    val label = when(type) {
                        CalculatorType.SIP -> "SIP"
                        CalculatorType.SIMPLE_INTEREST -> "Simple"
                        CalculatorType.COMPOUND_INTEREST -> "Compound"
                    }
                    
                    SwissButton(
                        text = label,
                        onClick = { onTypeChange(type) },
                        primary = isSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Input Section
            val amountLabel = if (uiState.activeType == CalculatorType.SIP) "Monthly Investment" else "Principal Amount"
            val maxAmount = if (uiState.activeType == CalculatorType.SIP) 100000f else 10000000f
            
            SwissCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("$amountLabel: ₹${uiState.amount}", style = MaterialTheme.typography.titleMedium)
                    Slider(
                        value = uiState.amount.toFloatOrNull() ?: 0f,
                        onValueChange = { onInputsChange(it.toInt().toString(), uiState.rate, uiState.years) },
                        valueRange = 500f..maxAmount
                    )
                    OutlinedTextField(
                        value = uiState.amount,
                        onValueChange = { onInputsChange(it, uiState.rate, uiState.years) },
                        label = { Text("Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text("Interest Rate: ${uiState.rate}%", style = MaterialTheme.typography.titleMedium)
                    Slider(
                        value = uiState.rate.toFloatOrNull() ?: 0f,
                        onValueChange = { onInputsChange(uiState.amount, String.format("%.1f", it), uiState.years) },
                        valueRange = 1f..30f
                    )
                     OutlinedTextField(
                        value = uiState.rate,
                        onValueChange = { onInputsChange(uiState.amount, it, uiState.years) },
                        label = { Text("Rate (%)") },
                         keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Time Period: ${uiState.years} Years", style = MaterialTheme.typography.titleMedium)
                    Slider(
                        value = uiState.years.toFloatOrNull() ?: 0f,
                        onValueChange = { onInputsChange(uiState.amount, uiState.rate, it.toInt().toString()) },
                        valueRange = 1f..40f
                    )
                     OutlinedTextField(
                        value = uiState.years,
                        onValueChange = { onInputsChange(uiState.amount, uiState.rate, it) },
                        label = { Text("Years") },
                         keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Results Section
            uiState.sipResult?.let { result ->
                SipResultView(result)
            }
            
            uiState.interestResult?.let { interest ->
                InterestResultView(interest, uiState.amount.toDoubleOrNull() ?: 0.0)
            }
        }
    }
}

@Composable
fun InterestResultView(interest: Double, principal: Double) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    currencyFormatter.maximumFractionDigits = 0
    val totalAmount = principal + interest

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SwissCard {
             Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Total Maturity Value", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = currencyFormatter.format(totalAmount),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
              Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Principal", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = currencyFormatter.format(principal),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Interest Earned", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = currencyFormatter.format(interest),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun SipResultView(result: SipResult) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    currencyFormatter.maximumFractionDigits = 0

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SwissCard {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Expected Maturity Amount", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = currencyFormatter.format(result.totalValue),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Invested", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = currencyFormatter.format(result.investedAmount),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Returns", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = currencyFormatter.format(result.wealthGained),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}
