package com.mtt.weatherapp.presentation.components


import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.mtt.weatherapp.common.Utils
import com.mtt.weatherapp.data.remote.dto.DailyDto
import com.mtt.weatherapp.data.remote.dto.HourlyDto
import com.mtt.weatherapp.presentation.theme.bodyFontFamily
import com.mtt.weatherapp.presentation.theme.displayFontFamily
import org.json.JSONObject
import kotlin.random.Random


@Composable
fun CenterAlignLargeText(text: String, textColor: Color) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = bodyFontFamily,
            color = textColor
        ),
        letterSpacing = 1.sp
    )
}

@Composable
fun CenterAlignNormalText(text: String, textColor: Color) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
            .wrapContentHeight(),
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = displayFontFamily,
            color = textColor
        )
    )
}

@Composable
fun NormalText(text: String, textColor: Color, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = bodyFontFamily,
            color = textColor
        )
    )
}

@Composable
fun SmallText(text: String, textColor: Color, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = bodyFontFamily,
            color = textColor
        )
    )
}

@Composable
fun animatedGradientBackground(): Brush {

    val colors = listOf(
        Color.hsl(
            Random.nextDouble(0.0, 360.0).toFloat(),
            Random.nextDouble(0.0, 1.0).toFloat(),
            Random.nextDouble(0.0, 1.0).toFloat()
        ),
        Color.hsl(
            Random.nextDouble(0.0, 360.0).toFloat(),
            Random.nextDouble(0.0, 1.0).toFloat(),
            Random.nextDouble(0.0, 1.0).toFloat()
        )

        // Medium Violet Red
    )

    return Brush.verticalGradient(
        colors = colors,
        tileMode = TileMode.Decal
    )
}

@Composable
fun WeatherStatsComponent(statsName: String, statsValue: String, image: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Wind",
            contentScale = ContentScale.Crop, modifier = Modifier
                .clip(CircleShape)
                .border(
                    2.dp,
                    Color(0xFFFBE2DE), CircleShape
                )
                .size(56.dp)
                .padding(12.dp)
        )
        SmallText(
            text = statsName, textColor = Color(0xFFFBE2DE), modifier = Modifier
                .padding(top = 8.dp)
                .wrapContentHeight()
        )
        NormalText(
            text = statsValue, textColor = Color(0xFFFBE2DE), modifier = Modifier
                .padding(top = 5.dp, bottom = 8.dp)
                .wrapContentHeight()
        )
    }
}


@Composable
fun HourlyWeatherComponent(hourly: HourlyDto, json: JSONObject) {
    Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)) {
        hourly.time?.let {
            SmallText(
                text = it, textColor = Color.White, modifier = Modifier
                    .wrapContentHeight()
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
        AsyncImage(
            model =
            Utils.getImageUrl(
                json,
                hourly.weatherCode.toString()
            ),
            modifier = Modifier
                .width(50.dp)
                .height(50.dp),
            contentScale = ContentScale.Inside,
            contentDescription = "Weather Icon"
        )
        hourly.temp?.let {
            SmallText(
                text = it,
                textColor = Color(0xFFFBE2DE),
                modifier = Modifier
                    .wrapContentHeight()
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun DailyWeatherComponent(dailyDto: DailyDto, json: JSONObject) {
    Row(modifier = Modifier.fillMaxWidth()) {
        SmallText(
            text = dailyDto.date, textColor = Color.White,
            modifier = Modifier
                .weight(0.5f)
                .padding(5.dp)
                .align(alignment = Alignment.CenterVertically)
        )
        Row(
            modifier = Modifier
                .weight(0.5f)
                .align(alignment = Alignment.CenterVertically), Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                model =
                Utils.getImageUrl(
                    json,
                    dailyDto.weatherCode.toString()
                ),
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp),
                contentScale = ContentScale.Inside,
                contentDescription = "Weather Icon"
            )
            SmallText(
                text = "${dailyDto.maxTemp}° C", textColor = Color(0xFFFBE2DE), modifier = Modifier
                    .padding(start = 2.dp)
                    .align(Alignment.CenterVertically)
            )
            SmallText(
                text = "${dailyDto.minTemp}° C", textColor = Color(0xFFFBE2DE), modifier = Modifier
                    .padding(start = 2.dp)
                    .align(Alignment.CenterVertically)
            )

        }
    }
}

@Composable
fun RequestLocationPermission(onPermissionResult: (Boolean) -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onPermissionResult(isGranted)
        }
    )

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                onPermissionResult(true)
            }

            else -> {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}