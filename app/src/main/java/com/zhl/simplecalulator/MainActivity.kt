package com.zhl.simplecalulator

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhl.simplecalulator.algorithm.Calculation
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
    var displayText by remember{ mutableStateOf(Calculation.calculationString) }
    var result by remember {
        mutableStateOf(
            if (Calculation.calculationString.isEmpty())
                ""
            else
                Calculation.calculate().toString())
    }
    Column {
        DisplayArea(text = displayText,
            text2 = result)
        Divider(Modifier.weight(0.6f), Color.Transparent)
        ButtonsArea {
            Calculation.input(it.code)
            displayText = Calculation.calculationString
            val tmp = Calculation.calculate().toString()
            result = if (tmp.endsWith(".0")) tmp.substringBefore('.') else tmp
        }
    }
}

@Composable
fun DisplayArea( modifier: Modifier = Modifier, text: String, text2: String = "") {
    Column(
        modifier
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(20))
            .fillMaxWidth()
            ) {
        Text(text = text, Modifier.padding(12.dp), fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
        if (text2.isNotEmpty()) {
            Row {
               Text("=",
                   modifier.weight(1f).padding(start = 12.dp),
                   color = Color.Green,
                   fontSize = 30.sp
               )
               Text(text = text2,
                   Modifier.padding(end = 12.dp),
                   fontSize = 30.sp
               )
            }
        }
    }
}

@Composable
fun ButtonsArea(modifier: Modifier = Modifier, onButtonClick: (CalculatorButton)-> Unit) {
    val isScreenPortrait =
        LocalContext.current.resources.configuration.orientation == ORIENTATION_PORTRAIT
    if (isScreenPortrait) {
        Column(modifier.fillMaxWidth()) {
            DeleteButton(onButtonClick, Modifier.align(Alignment.End))
            ButtonsMainPart(onButtonClick, Modifier.height(300.dp))
        }
    } else {
        Row(modifier) {
            ButtonsMainPart(onButtonClick, Modifier.weight(4f).height(180.dp))
            Column(Modifier.weight(1f)) {
                DeleteButton(onButtonClick, Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
private fun DeleteButton(onButtonClick: (CalculatorButton) -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = { onButtonClick(CalculatorButton.resetButton) },
        modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 5.dp)
            .size(80.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(Color.Red)
    ) {
        Text(text = "Reset")
    }
}

@Composable
private fun ButtonsMainPart(onButtonClick: (CalculatorButton) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(bottom = 10.dp)) {
        CalculatorButton.calculatorButtons.forEach { list ->
            Row(Modifier.weight(1f)) {
                list.forEach {
                    Button(modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .fillMaxHeight()
                        .weight(1f),
                        onClick = { onButtonClick(it) }) {
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