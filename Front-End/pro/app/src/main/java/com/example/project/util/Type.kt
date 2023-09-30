import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

@Composable
fun FormattedDateText(gifticonTime: String, prefix: String, modifier: Modifier = Modifier) {
        val year = gifticonTime.substring(0, 4).takeLast(2)   // YYYY
        val month = gifticonTime.substring(4, 6)              // MM
        val day = gifticonTime.substring(6, 8)                // DD
        val hour = gifticonTime.substring(8, 10)              // HH
        val minute = gifticonTime.substring(10, 12)           // MM

        val formattedTime = "${year}년 ${month}월 ${day}일"
        Text("$prefix : $formattedTime", modifier = modifier, fontWeight = FontWeight.Bold, fontSize = 16.sp)
}

@Composable
fun FormattedDateDot(
        dateString: String,
        modifier: Modifier = Modifier,
        fontSize: TextUnit,
        label: String = "유효기간 :" // 새로운 인자
) {
        val formattedDate = "$label ${dateString.substring(2,4)}.${dateString.substring(4,6)}.${dateString.substring(6,8)}"
        Text(text = formattedDate, modifier = modifier, fontSize = fontSize)
}

fun formatAlertDate(dateString: String): String {
        if (dateString.length != 14) return dateString // 입력 문자열이 유효하지 않으면 그대로 반환

        val year = dateString.substring(0, 4)
        val month = dateString.substring(4, 6)
        val day = dateString.substring(6, 8)
        val hour = dateString.substring(8, 10)
        val minute = dateString.substring(10, 12)

        return "$year.$month.$day $hour:$minute"
}

private fun formatToYearMonthDay(dateString: String): String {
        // 입력 문자열 검사: 입력이 예상대로 오는지 (길이, 숫자 등) 확인
        if (dateString.length != 14 || !dateString.all { it.isDigit() }) {
                return "Invalid Date" // 유효하지 않은 날짜 문자열인 경우
        }

        val year = dateString.substring(0, 2)
        val month = dateString.substring(2, 4)
        val day = dateString.substring(4, 6)

        return "${year}년 ${month}월 ${day}일"
}

// 1000단위 , 찍기용
val numberFormat = DecimalFormat("#,###")

fun formattedNumber(input: String): String {
        return try {
                val parsedNumber = input.replace(",", "").toLong()
                numberFormat.format(parsedNumber)
        } catch (e: Exception) {
                input
        }
}
