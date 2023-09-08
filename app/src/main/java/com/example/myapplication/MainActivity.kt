@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    val tip = calculateTip(amount, tipPercent)


    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.bill_amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.how_was_the_service,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            value = tipInput,
            onValueChange = { tipInput = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Preview
@Composable
fun TipTimeApp() {
    MyApplicationTheme {
        TipTimeLayout()
    }
}

private fun calculateTip(amount: Double, tipPercent: Double = 15.0): String {
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        // 컴포즈는 상태 value 속성을 읽는 각 컴포저블 함수를 추적하고 value가 업데이트되면 재구성하도록 트리거함
        // onValueChange 콜백은 텍스트 상자의 입력이 변경될 때 트리거됨, 람다 표현식의 it 변수에 새 값이 포함된다.
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label))},
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )

    // 1. 초기 컴포지션 -> TextField의 value 값은 빈 문자열로 설정됨
    // 2. 사용자가 텍스트를 입력하면, onValueChange 람다콜백이 호출되고 람다가 실행되며 amountInput.value가 텍스트 입력란에 입력된 수정값으로 설정됨
    // 3. amountInput은 컴포즈에 의해 추적되는 변경 가능한 상태이며 리컴포지션이 예약된다. 그리고 EditNumberField 컴포저블 함수는 다시 만들어진다.
    // 4. remember를 사용하므로 변경사항은 다시 만들어진 이후에도 그대로 유지된다.
    // 5. value는 저장된 amountInput 값으로 설정된다. 텍스트 입력란이 다시 만들어진다.
}

@Preview
@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage()
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    // 하드코딩 되어있던 result 값을 Roll 버튼을 탭할 경우 재설정되게끔 반응형으로 바꾸어야한다.
    // remember 컴포저블로 만든다. -> remember 컴포저블은 함수를 전달해야한다.
    // remember 컴포저블 내부에서는 mutableStateOf() 함수를 전달하고 기본 값인 1 인수를 전달한다.
    // result 변수값이 업데이트되면 재구성이 트리거되고, result 값이 반영되어 UI가 재랜더링된다.

    // import androidx.compose.runtime.mutableStateOf
    // import androidx.compose.runtime.remember
    // import androidx.compose.runtime.getValue
    // import androidx.compose.runtime.setValue
    var result by remember { mutableStateOf(1) }
    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(imageResource), contentDescription = result.toString())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { result = (1..6).random() }) {
            Text(text = stringResource(R.string.roll))
            println("result $result")
        }
    }
}