package com.example.weatherandroidapplication

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.WorkerThread
import androidx.compose.foundation.*
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
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

            }

            WeatherAndroidApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Navigation()

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
    fun MainContent(navController: NavController) {

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
                    InitialCitiesDisplay((state.value as Success).data, navController = navController)
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



                    InitialCitiesDisplay((state.value as Success).data,navController = navController)
                }
            }

        })
    }

    @Composable
    fun CardWithBorder(city: String, temperature: Int, comment: String,navController: NavController) {
        Column() {
            Card(
                elevation = 10.dp, border = BorderStroke(1.dp, Color.Blue), modifier = Modifier
                    .padding(10.dp)
                    .clickable { navController.navigate(Screen.DetailScreen.withArgs(temperature.toString())) }
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
                        //Text(text = "Icon", modifier = Modifier.padding(10.dp))
                        Image(painter = painterResource(id = R.drawable.icons8_rain_cloud_48), contentDescription = "icon",
                            modifier = Modifier
                                .size(40.dp))

                        Spacer(modifier = Modifier.height(40.dp))

                                Text(text = "Last updated 10 min ago", modifier = Modifier.padding(10.dp))
                    }
                }

            }


        }
    }

    @Composable
    fun AddCard(weatherOb:WeatherClass,navController: NavController) {
        CardWithBorder(city = weatherOb.data[0].city_name, temperature = weatherOb.data[0].temp, comment = weatherOb.data[0].weather.description, navController = navController)
    }


    @Composable
    fun InitialCitiesDisplay(weatherData: ArrayList<WeatherClass>,navController: NavController) {

        print("From  Main Vieww... Weather Data List is ")
        for(item in weatherData){
            println(item)
        }

        Column() {
            LazyColumn {
                itemsIndexed(items = weatherData) { index, item ->
                    AddCard(weatherOb = item,navController)
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

    @Composable
    fun Navigation () {

        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.MainScreen.route  ){
            composable(Screen.MainScreen.route){
                MainScreen(navController = navController)
            }

            composable(route = Screen.DetailScreen.route + "/{tempStr}",
                arguments = listOf(
                navArgument("tempStr"){
                    type = NavType.StringType
                }
            )
            ){
                entry ->
                entry.arguments?.getString("tempStr")?.let { DetailScreen(tempString = it) }
            }

        }

    }

    @Composable
    fun MainScreen(navController: NavController){
        MainContent(navController = navController)

    }

    @Composable
    fun DetailScreen(tempString:String){
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
            Text(text = "Temperature is ${tempString}")
        }
    }



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
