package com.example.demo_profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.demo_profile.ui.theme.Demo_ProfileTheme
import com.example.demo_profile.ui.theme.PrimaryColor
import com.example.demo_profile.util.PrefManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val prefManager = PrefManager(this@MainActivity)


    companion object {
        const val IMG_CLICK = 1
    }
//    val imagePathStr = prefManager.getString("ImagePath", null)

//    val imagePath = remember { mutableStateOf("") }

    private var imagePath: String = ""
    var bitmap: MutableState<Bitmap?>? = null

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//             val imagePath = remember { mutableStateOf( prefManager.getString("ImagePath", "")) }
            bitmap = remember {
                mutableStateOf(null)
            }
            Demo_ProfileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReportNoPermissionTopBar()
                }
            }
        }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
// openCamera(IMG_CLICK_FRONT)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    bitmap?.value = processImageRotation(imagePath)

                } else {
                    Toast.makeText(this@MainActivity, "Camera closed", Toast.LENGTH_SHORT).show()
                }
            }
    }


    @Composable
    fun ClickImage() {
//         val imagePathStr = prefManager.getString("ImagePath", null)
//
//         val imagePath = remember { mutableStateOf(imagePathStr) }

        Column() {
            Box(
                Modifier
                    .fillMaxWidth()
                /*.clickable {
                    onImageClick(IMG_CLICK)
                }*/,
                contentAlignment = Alignment.Center
            ) {

                val imagePathStr = prefManager.getString("ImagePath", null)
                val bitmapValue = bitmap?.value

                if (bitmapValue != null) {

                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = bitmapValue.asImageBitmap(), contentDescription = "",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(160.dp)
                                .clickable {
                                    onImageClick(
                                        IMG_CLICK
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    }

                } else if (imagePathStr != null) {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = BitmapFactory.decodeFile(imagePathStr).asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(160.dp)
                                .clickable {
                                    onImageClick(
                                        IMG_CLICK
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    }

                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(painter = painterResource(id = R.drawable.baseline_circle_24),
                            contentDescription = "",
                            Modifier
                                .size(160.dp)
                                .clickable {
                                    onImageClick(IMG_CLICK)
                                })

                        Image(
                            painter = painterResource(id = R.drawable.baseline_store_24),
                            contentDescription = "",
                            Modifier.size(80.dp)
                        )

                    }

                }
            }
        }
    }

    private fun onImageClick(imgClicked: Int) {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera(imgClicked)
        } else {
            askCameraPermission()
        }
    }

    private fun askCameraPermission() {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun openCamera(imgClicked: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createPhotoFile()
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }

            if (photoFile != null) {
                val uri = FileProvider.getUriForFile(
                    this@MainActivity,
                    "com.example.demo_profile.provider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                if (imgClicked == MainActivity.IMG_CLICK) {
                    imagePath = photoFile.absolutePath
                    cameraLauncher.launch(intent)
                }
            }
        } else {
            Toast.makeText(this@MainActivity, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createPhotoFile(): File {
        val fileTimestamp = SimpleDateFormat("yyyy-MM-dd_HHmm", Locale.US).format(Date())
        val fileName = "JPEG_$fileTimestamp"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }


    @Composable
    fun ReportNoPermissionScreen() {
        val reason = remember {
            mutableStateOf("")
        }

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 20.dp)
        ) {

            ClickImage()
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Click Outlet Board Picture",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            CommonTextField(
                name = "Store Name",
                textField = "XYZ",
                image = R.drawable.baseline_store_24
            )

            CommonTextField(
                name = "Date",
                textField = "12/12/2022",
                image = R.drawable.baseline_store_24
            )

            CommonTextField(
                name = "Time",
                textField = "4:00pm",
                image = R.drawable.baseline_store_24
            )

            CommonTextField(
                name = "Location",
                textField = "Main Road, Gurugaon",
                image = R.drawable.baseline_store_24
            )

            NoPermissionReasonDropDown(value = reason.value, onChange = { reason.value = it })

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    prefManager.save("ImagePath", imagePath ?: "")

                    startActivity(Intent(this@MainActivity, NextActivity::class.java))
                },
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(PrimaryColor),
                border = BorderStroke(width = 1.dp, color = PrimaryColor),
                modifier = Modifier
                    .padding(top = 40.dp, start = 90.dp, end = 90.dp, bottom = 0.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Submit",
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White,
                )
            }
        }
    }


    @Composable
    fun SubmitButton() {
        Button(
            onClick = { },
            modifier = Modifier.padding(20.dp)
        ) {
            Text("Submit")
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CommonTextField(
        name: String,
        textField: String,
        image: Int
    ) {

        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(start = 20.dp, top = 8.dp, bottom = 5.dp)
        ) {
            Image(
                painter = painterResource(id = image), contentDescription = "",
                Modifier
                    .size(22.dp)
                    .padding(start = 0.dp),
                colorFilter = ColorFilter.tint(Color.Black)
            )
            Box(Modifier.width(100.dp)) {
                Text(
                    text = name,
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W400
                )
            }


            Text(
                text = textField,
                modifier = Modifier.padding(start = 20.dp),
                fontSize = 15.sp,
                color = Color.Gray,
            )

        }

        Divider(
            Modifier
                .fillMaxWidth()
                .padding(start = 75.dp),
            thickness = 1.dp,
        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NoPermissionReasonDropDown(value: String, onChange: (String) -> Unit) {
        val expand = remember { mutableStateOf(false) }
        val reasons = listOf("Others", "Store Temporarily Closed", "Owner Not Allowing", "Strike")


        Column(
            Modifier.background(Color.White)
        ) {

            Text(
                text = "Select Reason for No Permission",
                Modifier.padding(start = 25.dp, top = 20.dp),
                fontSize = 16.sp,
                color = PrimaryColor,
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 20.dp, end = 50.dp, bottom = 5.dp),
                shape = RoundedCornerShape(size = 30.dp),
                border = BorderStroke(width = 1.dp, color = PrimaryColor),
            ) {
                ExposedDropdownMenuBox(expanded = expand.value, onExpandedChange = {
                    expand.value = !expand.value
                }) {

                    TextField(
                        value = (value),
                        textStyle = TextStyle(fontSize = 14.sp),
                        onValueChange = { onChange(it) },
                        placeholder = {
                            Text(
                                text = "Select Reason",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expand.value)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 0.dp)
                            .height(50.dp)
                            .background(Color.White)
                            .menuAnchor()
                            .border(1.dp, PrimaryColor, RectangleShape),

                        shape = RoundedCornerShape(10.dp),

                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                    )

                    ExposedDropdownMenu(expanded = expand.value,
                        onDismissRequest = { expand.value = false }) {
                        reasons.forEach { reason ->
                            DropdownMenuItem(text = {
                                Text(
                                    text = reason,
                                    fontSize = 14.sp,
                                )
                            }, onClick = {
                                onChange(reason)
                                expand.value = false
                            }, modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ReportNoPermissionTopBar() {
        Demo_ProfileTheme() {
            Scaffold(topBar = {
                TopAppBar(modifier = Modifier.height(50.dp),
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = PrimaryColor
                    ),
                    title = {
                        Text(
                            text = "Report No Permission",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 5.dp, top = 10.dp)
                        )
                    },
                    navigationIcon = {
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
                        ReportNoPermissionScreen()
                    }
                })
        }
    }


    private fun processImageRotation(imagePath: String): Bitmap? {
        val ei = ExifInterface(imagePath)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val bmp = BitmapFactory.decodeFile(imagePath)
        bmp ?: return null

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bmp, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bmp, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bmp, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bmp
            else -> bmp
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
}





