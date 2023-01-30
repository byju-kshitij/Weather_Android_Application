package com.example.weatherandroidapplication

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : ComponentActivity() {

    val weatherViewModel by viewModels<WeatherViewModel>()

    val RepoObj = RepositoryClass()

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateMinUopdatedAgo(lastUpdatedTime:String):String{

        val greenwhich_date_time = LocalDateTime.now().minusHours(5).minusMinutes(30)

        val st:String = lastUpdatedTime
        var last_updated_time_hrs :String = ""
        for (i in st.length-5..st.length-4){
            last_updated_time_hrs+=st[i]
        }
        var hh = last_updated_time_hrs.toInt()

        var last_updated_time_mins :String = ""
        for (i in st.length-2..st.length-1){
            last_updated_time_mins+=st[i]
        }
        var mm = last_updated_time_mins.toInt()

        return greenwhich_date_time.minusHours(hh.toLong()).minusMinutes(mm.toLong()).minute.toString()

    }

    private fun deleteAllWeatherData() {
        println("delete Weather Data called")
        RepoObj.deleteAllDBData()
    }

    fun String.getDateWithServerTimeStamp(): Date? {
        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        )
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
        try {
            return dateFormat.parse(this)
        } catch (e: ParseException) {
            return null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    fun codeToImage(code:Int): Int {
        return when(code){
            741-> R.drawable.icons8_fog_50
            721 -> R.drawable.haze
            else -> R.drawable.icons8_rain_cloud_48
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun CardWithBorder(weatherOb: WeatherClass,navController: NavController) {
        Column() {
            Card(
                elevation = 10.dp, border = BorderStroke(1.dp, Color.Blue), modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        navController.navigate(
                            Screen.DetailScreen.withArgs(
                                weatherOb.data[0].sunrise,
                                weatherOb.data[0].sunset,
                                weatherOb.data[0].rh.toString(),
                                weatherOb.data[0].vis.toString(),
                                weatherOb.data[0].clouds.toString(),
                                weatherOb.data[0].wind_spd.toString(),
                                weatherOb.data[0].pres.toString()
                            )
                        )
                    }
            ) {

                Row() {
                    Column() {
                        Text(text = weatherOb.data[0].city_name, modifier = Modifier.padding(10.dp))
                        Text(text = weatherOb.data[0].weather.description, modifier = Modifier.padding(10.dp))
                        Text(text = weatherOb.data[0].temp.toString()+ "\u2103", modifier = Modifier.padding(10.dp))
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        //Text(text = "Icon", modifier = Modifier.padding(10.dp))
                        Image(painter = painterResource(id = codeToImage(weatherOb.data[0].weather.code)), contentDescription = "icon",
                            modifier = Modifier
                                .size(40.dp))

                        Spacer(modifier = Modifier.height(40.dp))

                                Text(text = "Last updated ${calculateMinUopdatedAgo(weatherOb.data[0].ob_time)} min ago", modifier = Modifier.padding(10.dp))
                    }
                }

            }


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AddCard(weatherOb:WeatherClass,navController: NavController) {
        CardWithBorder(weatherOb, navController = navController)
    }


    @RequiresApi(Build.VERSION_CODES.O)
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
//            Button(
//                onClick = { callWeatherApi("Mumbai") }, modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentSize(Alignment.Center)
//            ) {
//                Text(text = "Refresh")
//            }

            FloatingActionButton(
                // on below line we are adding on click for our fab
                onClick = {
                    callWeatherApi("Mumbai")
                },
                // on below line we are adding
                // background color for our button
                backgroundColor = Color.Red,
                // on below line we are adding
                // color for our content of fab.
                contentColor = Color.White, modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
            ) {
                // on below line we are
                // adding icon for button.
                Icon(Icons.Filled.Refresh, "")
            }
        }


        }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Navigation () {

        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.MainScreen.route  ){
            composable(Screen.MainScreen.route){
                MainScreen(navController = navController)
            }

            composable(route = Screen.DetailScreen.route + "/{sunrise}/{sunset}/{humidity}/{visibility}/{clouds}/{winds}/{pressure}",
                arguments = listOf(
                navArgument("sunrise"){
                    type = NavType.StringType
                },
                    navArgument("sunset"){
                        type = NavType.StringType
                    },
                    navArgument("humidity"){
                        type = NavType.StringType
                    },
                    navArgument("visibility"){
                        type = NavType.StringType
                    } ,
                            navArgument("clouds"){
                        type = androidx.navigation.NavType.StringType
                    },
                            navArgument("winds"){
                        type = androidx.navigation.NavType.StringType
                    },
                            navArgument("pressure"){
                        type = androidx.navigation.NavType.StringType
                    }
            )
            ){
//                entry ->
//                entry.arguments?.getString("tempStr")?.let { DetailScreen(tempString = it) }

                entry -> DetailScreen(sunrise = entry.arguments?.getString("sunrise")!!, sunset = entry.arguments?.getString("sunset")!! , humidity = entry.arguments?.getString("humidity")!! , visibility = entry.arguments?.getString("visibility")!!, clouds = entry.arguments?.getString("clouds")!!, winds = entry.arguments?.getString("winds")!!, pressure = entry.arguments?.getString("pressure")!! )
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun MainScreen(navController: NavController){
        MainContent(navController = navController)

    }

    @Composable
    fun CardWithContentColor(property:String,value:String) {
        val paddingModifier = Modifier.padding(7.dp)
        Card(
            elevation = 100.dp,
            contentColor = Color.Blue,border = BorderStroke(1.dp, Color.Blue),
            modifier = paddingModifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = property,
                    modifier = paddingModifier)
                Text(text = value,
                    color = Color.Black,
                    modifier = paddingModifier)

            }
        }
    }

    @Composable
    fun DetailScreen(sunrise:String,sunset:String,humidity:String,visibility:String,clouds:String,winds:String,pressure:String){

        Column() {

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        //.background(Color.Blue)
                ){
                    CardWithContentColor(property = "Sunrise", value = sunrise )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        //.background(Color.Blue)
                ){
                    CardWithContentColor(property = "Sunset", value = sunset )
                }

            }


            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        //.background(Color.Blue)
                ){
                    CardWithContentColor(property = "Humidity", value = humidity )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        //.background(Color.Blue)
                ){
                    CardWithContentColor(property = "Visibility", value = visibility )
                }

            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        //.background(Color.Blue)
                ){
                    CardWithContentColor(property = "Clouds", value = clouds )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        //.background(Color.Blue)
                ){
                    CardWithContentColor(property = "Winds", value = winds )
                }

            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        //.background(Color.Blue)
                ){
                    CardWithContentColor(property = "Pressure", value = pressure )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        //.background(Color.Blue)
                ){
                    CardWithContentColor(property = "Precipitation", value = "null" )
                }

            }






            /*

            Row() {
                CardWithContentColor(property = "Humidity", value = humidity )
                CardWithContentColor(property = "Visibility", value = visibility )
            }

            Row() {
                CardWithContentColor(property = "Clouds", value = clouds )
                CardWithContentColor(property = "Winds", value = winds )
            }

            Row() {
                CardWithContentColor(property = "Pressure", value = pressure )
            }


             */

//            Text(text = "GMT Sunrise time is ${sunrise}")
//            Spacer(modifier = Modifier.height(15.dp))
//            Text(text = "GMT Sunset time is ${sunset}")
//            Spacer(modifier = Modifier.height(15.dp))
//            Text(text = "Humidity Percentage is ${humidity}")
//            Spacer(modifier = Modifier.height(15.dp))
//            Text(text = "Visibility Percentage is ${visibility}")
//            Spacer(modifier = Modifier.height(15.dp))
//            Text(text = "Clouds is ${clouds}")
//            Spacer(modifier = Modifier.height(15.dp))
//            Text(text = "Wind Speed is ${winds}")
//            Spacer(modifier = Modifier.height(15.dp))
//            Text(text = "Pressure is ${pressure}")

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
