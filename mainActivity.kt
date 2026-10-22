package com.example.travelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelApp()
        }
    }
}

@Composable
fun TravelApp() {
    val coroutineScope = rememberCoroutineScope()

    var flights by remember { mutableStateOf("Flight API:") }
    var hotels by remember { mutableStateOf("Hotel API:") }
    var carRentals by remember { mutableStateOf("Car Rental API:") }
    var result by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            coroutineScope.launch {
                supervisorScope {
                    val flightTask = async {
                        runCatching { getFlightApi() }
                    }

                    val hotelTask = async {
                        runCatching { getHotelApi() }
                    }

                    val carTask = async {
                        runCatching { getCarApi() }
                    }

                    flights = flightTask.await()
                        .getOrElse { "Flight API Failed" }

                    hotels = hotelTask.await()
                        .getOrElse { "Hotel API Failed" }

                    carRentals = carTask.await()
                        .getOrElse { "Car Rental API Failed" }

                    result = "$flights\n$hotels\n$carRentals"
                }
            }
        }) {
            Text("Fetch Travel Data")
        }

        Text(text = result)
    }
}

suspend fun getFlightApi(): String {
    return withContext(Dispatchers.IO) {
        "Flight API Working"
    }
}

suspend fun getHotelApi(): String {
    return withContext(Dispatchers.IO) {
        "Hotel API Working"
    }
}

suspend fun getCarApi(): String {
    return withContext(Dispatchers.IO) {
        "Car Rental API Working"
    }
}
