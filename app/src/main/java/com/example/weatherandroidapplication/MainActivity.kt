package com.example.weatherandroidapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.example.weatherandroidapplication.network.WeatherApi
//import com.example.weatherandroidapplication.network.WeatherApi
import com.example.weatherandroidapplication.ui.theme.WeatherAndroidApplicationTheme
import com.example.weatherandroidapplication.viewmodel.WeatherViewModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import kotlin.reflect.typeOf


class MainActivity : ComponentActivity() {

    val weatherViewModel by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
//            LaunchedEffect(Unit) {
//                val retrofitData = WeatherApi.retrofitService.getWeatherdata("Chennai", country = "IN", key = "9fcb60c4b3984edb99886da0d26b8ee7")
//                println("Temperature is:")
//                println(retrofitData.data[0].temp)
//
//            }


            WeatherAndroidApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    weatherViewModel.city = "Delhi"
                    weatherViewModel.country = "IN"
                    weatherViewModel.key = "9fcb60c4b3984edb99886da0d26b8ee7"
                    weatherViewModel.getWeatherData()
                    println("Weather for chosen city is")
                    if(weatherViewModel.weatherResponse.count!=0){
                        println(weatherViewModel.weatherResponse.data[0].temp)
                    }

                    else{
                        println("No data returned")
                    }

                    MainContent()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContent(){
    Scaffold(
        topBar = {TopAppBar(
            title = {Text(
                "Weather App",
                color = Color.White)},
            backgroundColor = Color(0xff0f9d58)
        ) },
        content = { InitialCitiesDisplay() }
    )
}

@Composable
fun CardWithBorder(city:String,temperature:String,comment:String) {
    Card(
        elevation = 10.dp,
        border = BorderStroke(1.dp, Color.Blue),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Row() {
            Column() {
                Text(text = city, modifier = Modifier.padding(10.dp))
                Text(text = comment, modifier = Modifier.padding(10.dp))
                Text(text = temperature, modifier = Modifier.padding(10.dp))
            }
            Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.End) {
                Text(text = "Icon", modifier = Modifier.padding(10.dp))

                Text(text = "Last updated 10 min ago", modifier = Modifier.padding(10.dp))
            }
        }


    }
}

@Composable
fun InitialCitiesDisplay() {

    Column() {
        CardWithBorder("Mumbai","25"+"\u2103","Fog")
        CardWithBorder("Delhi","30"+"\u2103","Clear Sunny")
        CardWithBorder("Kolkata","24"+"\u2103","Rain")
        CardWithBorder("Chennai","24"+"\u2103","Rain")
    }



//    Column() {
//        Box(
//            modifier = Modifier.border(BorderStroke(2.dp, Color.Red))
//        )
//
//        Row(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxHeight()
//                .fillMaxWidth()
//                .wrapContentHeight()
//                .background(color = Color.Magenta)
//                .border(border = BorderStroke(width = 1.dp, Color.LightGray))
//        ) {
//            Column() {
//                Text("Mumbai")
//                Text("Fog")
//                Text("25 C")
//
//            }
//
//            Column(modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.End) {
//                Text("Icon")
//                Text("Last updated 10 min ago")
//
//            }
//
//        }
//
//        Row(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth()
//                .wrapContentHeight()
//                .background(color = Color.Red)
//        ) {
//            Column() {
//                Text("Delhi")
//                Text("Clear Sunny")
//                Text("30 C")
//
//            }
//        }
//
//        Row(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth()
//                .wrapContentHeight()
//                .background(color = Color.Gray)
//        ) {
//            Column() {
//                Text("Bengaluru")
//                Text("Rain")
//                Text("24 C")
//
//            }
//        }
//
//
//    }
}



    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        WeatherAndroidApplicationTheme {
            InitialCitiesDisplay()
        }
    }
