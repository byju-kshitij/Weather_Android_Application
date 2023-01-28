package com.example.weatherandroidapplication

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.WorkerThread
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.weatherandroidapplication.Repository.RepositoryClass
import com.example.weatherandroidapplication.models.WeatherClass
//import com.example.weatherandroidapplication.network.WeatherApi
import com.example.weatherandroidapplication.ui.theme.WeatherAndroidApplicationTheme
import com.example.weatherandroidapplication.viewmodel.WeatherRequest
import com.example.weatherandroidapplication.viewmodel.WeatherRequest.Error
import com.example.weatherandroidapplication.viewmodel.WeatherRequest.Ideal
import com.example.weatherandroidapplication.viewmodel.WeatherRequest.Loading
import com.example.weatherandroidapplication.viewmodel.WeatherRequest.Success
import com.example.weatherandroidapplication.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {

    val weatherViewModel by viewModels<WeatherViewModel>()

    val RepoObj = RepositoryClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //deleteAllWeatherData()

        setContent {
            LaunchedEffect(Unit) {
                /*val retrofitData = WeatherApi.retrofitService.getWeatherdata("Chennai", country = "IN", key = "9fcb60c4b3984edb99886da0d26b8ee7")
                println("Temperature is:")
                println(retrofitData.data[0].temp)*/

//                var appState: StateFlow<AppState> = MutableStateFlow(AppState.LOADING)
//                appState.value = AppState.LOADING
                // Perform some async operation
//                val result = performAsyncOperation()

                //appState.value = AppState.DONE(weatherViewModel.weatherResponse)

//                val cityList:List<String> = listOf("Mumbai")
//                val topCitiesWeatherMutableList = mutableListOf<WeatherClass>()
//                for (city in cityList){
//                    weatherViewModel.city = city
//                    weatherViewModel.country = "IN"
//                    weatherViewModel.key = "2facb83973524c8e927e726516722a3d"
//                    weatherViewModel.getWeatherData()
//
//                    println("Weather for chosen city $city is")
//                    if(weatherViewModel.weatherResponse.count!=0){
//                        println(weatherViewModel.weatherResponse.data[0].temp)
//                        var weatherRes = weatherViewModel.weatherResponse
//                        println("Object is")
//                        println(weatherRes)
//                        topCitiesWeatherMutableList.add(weatherRes)
//                    }
//
//                    else{
//                        println("No data found")
//                    }
//
//                    //delay(2000)
//
//                }
//
//                println(topCitiesWeatherMutableList)

//                callWeatherApi()
//                val state: WeatherRequest
//                when (state) {
//                    is Error -> {
//                        {
//
//                        }
//                    }
//                    WeatherRequest.Loading ->  {
//                        CircleProgress()
//                    }
//                    is WeatherRequest.Success -> {
//                        Card {
//                            state.data
//                        }
//                    }
//                }

            }

            WeatherAndroidApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainContent()

                }
            }
        }
    }

    private fun callWeatherApi(city:String) {
        println("callWeatherApi called")
        weatherViewModel.city = city
        weatherViewModel.country = "IN"
        weatherViewModel.key = "2facb83973524c8e927e726516722a3d"
        weatherViewModel.getWeatherData()
    }

//    private fun deleteAllWeatherData() {
//        println("delete Weather Data called")
//        RepoObj.deleteAllDBData()
//    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun MainContent() {

        lifecycleScope.launchWhenStarted {
            print("Inside lifeCycle Scope")

            callWeatherApi("Mumbai")
        }

        val state: State<WeatherRequest> = weatherViewModel.stateFlow.collectAsState()
        println("State = $state")

        Scaffold(topBar = {
            TopAppBar(title = {
                Text("Weather App", color = Color.White)
            }, backgroundColor = Color(0xff0f9d58))
        }, content = {
            when (state.value) {
                is Error -> {

                    //TODO : Load from Database, if data is present
                    if(weatherViewModel.isDataBaseEmpty==false)
                    InitialCitiesDisplay((state.value as Success).data)
                    else {
                    Column() {
                        Image(painter = painterResource(id = R.drawable.istockphoto_1279827701_612x612), contentDescription = "no internet image",
                        modifier = Modifier
                            .size(500.dp)
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center))
                        Text(text = "No Internet Connection !!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center))
                        Spacer(modifier = Modifier.height(15.dp))

                        //Add Button , onClick method will call callWeatherApi()
                        Button(onClick = { callWeatherApi("Mumbai") }, modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)) {
                            Text(text = "Retry")
                        }
                    }
                    }

                }
                Ideal -> { }
                Loading ->  {
                    CircularProgressIndicator(modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center))
                 }
                is Success -> {
                    println("Fetched Data!!")



                    InitialCitiesDisplay((state.value as Success).data)
                }
            }

        })
    }

    @Composable
    fun CardWithBorder(city: String, temperature: Int, comment: String) {
        Column() {
            Card(
                elevation = 2.dp, border = BorderStroke(1.dp, Color.Blue), modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                Row() {
                    Column() {
                        Text(text = city, modifier = Modifier.padding(10.dp))
                        Text(text = comment, modifier = Modifier.padding(10.dp))
                        Text(text = temperature.toString()+ "\u2103", modifier = Modifier.padding(10.dp))
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "Icon", modifier = Modifier.padding(10.dp))

                        Text(text = "Last updated 10 min ago", modifier = Modifier.padding(10.dp))
                    }
                }

            }


        }
    }

    @Composable
    fun AddCard(weatherOb:WeatherClass) {
        CardWithBorder(city = weatherOb.data[0].city_name, temperature = weatherOb.data[0].temp, comment = weatherOb.data[0].weather.description)
    }


    @Composable
    fun InitialCitiesDisplay(weatherData: ArrayList<WeatherClass>) {

        print("From  Main Vieww... Weather Data List is ")

        for(item in weatherData){
            println(item)
        }

        Column() {
            LazyColumn {
                itemsIndexed(items = weatherData) { index, item ->
                    AddCard(weatherOb = item)
                }
            }
            Button(
                onClick = { callWeatherApi("Mumbai") }, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(text = "Refresh")
            }
        }


        }





//        Column() {
//
//            CardWithBorder("Mumbai",
//                "${weatherData[0].data[0].temp}" + "\u2103",
//                weatherData[0].data[0].weather.description)
//
//
//            CardWithBorder("Delhi",
//                "${weatherData[1].data[0].temp}" + "\u2103",
//                weatherData[1].data[0].weather.description)
//
//
//
//            CardWithBorder("Kolkata",
//                "${weatherData[2].data[0].temp}" + "\u2103",
//                weatherData[2].data[0].weather.description)
//
//
//
//            CardWithBorder("Chnnai",
//                "${weatherData[3].data[0].temp}" + "\u2103",
//                weatherData[3].data[0].weather.description)
//
//            Button(
//                onClick = { callWeatherApi("Mumbai") }, modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentSize(Alignment.Center)
//            ) {
//                Text(text = "Refresh")
//            }
//        }

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
        //) {
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


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        WeatherAndroidApplicationTheme {
//            InitialCitiesDisplay(WeatherClass(
//                0, "", ""
//            ))
        }
    }
}
