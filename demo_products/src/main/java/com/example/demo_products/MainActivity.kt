package com.example.demo_products

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo_products.ui.theme.Demo_ProfileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContent {
             Demo_ProfileTheme {
            // A surface container using the 'background' color from the theme
             Surface(
             modifier = Modifier.fillMaxSize(),
             color = MaterialTheme.colorScheme.background
             ) {
            PromotionTrackingTopBar()
             }
             }
            }
         }


     @Composable
    fun PromotionTrackingScreen(){

         Column(
         Modifier
        .fillMaxSize()
       .padding(top = 10.dp)) {


            Row(
            Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            ) {

            CommonArrowLeft()
            Column(Modifier.horizontalScroll(rememberScrollState())) {
            Row(
             Modifier
          .fillMaxWidth()
            .padding(start = 10.dp, end = 0.dp)
             ) {

             CommonButton("All", 18.sp, Color.White, PrimaryColor)
             Spacer(modifier = Modifier.width(10.dp))

            CommonButton("Godrej No.1", 14.sp, PrimaryColor, Color.White, isBorder = true)
           Spacer(modifier = Modifier.width(10.dp))

             CommonButton("Pears", 14.sp, PrimaryColor, Color.White,isBorder = true)
            Spacer(modifier = Modifier.width(10.dp))

            CommonButton("Medimix", 14.sp, PrimaryColor, Color.White,isBorder = true)
            }
            }

           }


            Spacer(modifier = Modifier.height(10.dp))

           Row(
             Modifier
           .fillMaxWidth()
            .padding(start = 10.dp)){

             CommonArrowLeft()

            Column(Modifier.horizontalScroll(rememberScrollState())) {
             Row(
             Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            ) {

             CommonButton("All", 18.sp, Color.White, PrimaryColor)
             Spacer(modifier = Modifier.width(10.dp))

            CommonButton("Godrej No.1", 14.sp, PrimaryColor, Color.White, isBorder = true)
             Spacer(modifier = Modifier.width(10.dp))

            CommonButton("Pears", 14.sp, PrimaryColor, Color.White, isBorder = true)
             Spacer(modifier = Modifier.width(10.dp))

            CommonButton("Medimix", 14.sp, PrimaryColor, Color.White, isBorder = true)
             }
            }
           }

             }

        }


    @Composable
    fun CommonButton(title: String, textFontSize: TextUnit, fontColor: Color, backgrounColor: Color, isBorder:Boolean = false )
     {
        if(isBorder){
            Button(
             onClick = {},
            colors = ButtonDefaults.buttonColors(
             containerColor = backgrounColor
             ),
             modifier = Modifier
            .border(1.dp, PrimaryColor, RoundedCornerShape(25.dp))

            ) {
             Text(
             text = title,
           fontSize = textFontSize,
             color = fontColor,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)

          )
             }
            }else{
            Button(
             onClick = {},
            colors = ButtonDefaults.buttonColors(
             containerColor = backgrounColor
             ),

            ) {
            Text(
             text = title,
             fontSize = textFontSize,
             color = fontColor,

             )
             }
             }
         }

     @Composable
    fun CommonArrowLeft(){
         Box(contentAlignment = Alignment.Center) {
             Image(painter = painterResource(id = R.drawable.baseline_circle_24), contentDescription = "",
             modifier = Modifier.size(40.dp))

            IconButton(onClick = { }) {
            Icon(
             Icons.Filled.KeyboardArrowLeft,
             contentDescription = "Arrow Back",
             modifier = Modifier.size(30.dp),
             tint = Color.Red,
             )
             }
             }
         }

    @Composable
   fun CommonArrowRight(){
        Box(contentAlignment = Alignment.Center) {
           Image(painter = painterResource(id = R.drawable.baseline_circle_24), contentDescription = "",
             modifier = Modifier.size(50.dp))

             IconButton(onClick = { }) {
             Icon(
             Icons.Filled.KeyboardArrowRight,
             contentDescription = "Arrow Back",
            modifier = Modifier.size(40.dp),
             tint = Color.Red,
             )
            }

             }
       }


  @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
     fun PromotionTrackingTopBar() {
      Demo_ProfileTheme() {
             Scaffold(topBar = {
             TopAppBar(modifier = Modifier.height(50.dp), colors = TopAppBarDefaults.smallTopAppBarColors(
             containerColor = PrimaryColor
             ), title = {
             Text(
             text = "Promotion Tracking",
            color = Color.White,
            fontSize = 18.sp,
             modifier = Modifier.padding(start = 5.dp, top = 10.dp)
            )
             }, navigationIcon = {
             IconButton(onClick = { finish() }) {
             Icon(
             Icons.Filled.KeyboardArrowLeft,
            contentDescription = "Arrow Back",
             modifier = Modifier.size(50.dp),
             tint = Color.White,
             )
            }
            })
             },

            content = { padding ->
             Box(
             modifier = Modifier
             .fillMaxWidth()
           .padding(padding),
             ) {
             PromotionTrackingScreen() }
             })
           }
        }
}