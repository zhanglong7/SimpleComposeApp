package com.zhl.simplecalulator

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zhl.simplecalulator.ui.theme.SimpleCalulatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleCalulatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PageContent()
                }
            }
        }
    }
}


@Composable
fun PageContent() {
    var displayText by remember{ mutableStateOf("") }
    Column {
        DisplayArea(text = displayText, Modifier.weight(1f))
        ButtonsArea(Modifier.weight(1f)) {
            displayText = it
        }
    }
}

@Composable
fun DisplayArea(text: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = text)
    }
}

@Composable
fun ButtonsArea(modifier: Modifier = Modifier, onButtonClick: (String)-> Unit) {
    val isScreenPortrait =
        LocalContext.current.resources.configuration.orientation == ORIENTATION_PORTRAIT
    if (isScreenPortrait) {
        Column(modifier.fillMaxWidth()) {
            DeleteButton(onButtonClick, Modifier.align(Alignment.End))
            ButtonsMainPart(onButtonClick)
        }
    } else {
        Row(modifier) {
            ButtonsMainPart(onButtonClick, Modifier.weight(4f))
            Column(Modifier.weight(1f)) {
                DeleteButton(onButtonClick, Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
private fun DeleteButton(onButtonClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = { onButtonClick("")/*TODO*/ },
        modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 5.dp)
            .size(70.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(Color.Red)
    ) {
        Text(text = "Clear")
    }
}

@Composable
private fun ButtonsMainPart(onButtonClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CalculatorButton.calculatorButtons.forEach { list ->
            Row(Modifier.weight(1f)) {
                list.forEach {
                    Button(modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .fillMaxHeight()
                        .weight(1f),
                        onClick = { onButtonClick(it.name) }) {
                        Text(text = it.name)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true/*, device = Devices.AUTOMOTIVE_1024p*/)
@Composable
fun DefaultPreview() {
    SimpleCalulatorTheme {
        PageContent()
    }
}