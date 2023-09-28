package com.example.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.api.GifticonRequestDTO
import com.example.project.api.MyService
import com.example.project.api.OcrService
import com.example.project.api.UploadGifticonResponse
import com.example.project.api.uploadImageResponse
import com.example.project.sharedpreferences.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class MygifticonViewModel @Inject constructor(
    private val myService: MyService, private val sharedPreferencesUtil: SharedPreferencesUtil
) : ViewModel() {

    private val _gifticonItems = MutableStateFlow<List<GifticonData>>(emptyList())
    val gifticonItems: StateFlow<List<GifticonData>> get() = _gifticonItems


    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 1

    fun changePage(page: Int) {
        currentPage = page
        getUserGifticons(currentPage)
    }

    // 물물교환 등록할때 이미지 불러오기용
    fun getSelectedItems(indices: List<Long>): List<GifticonData> {
        return _gifticonItems.value.filter { it.gifticonIdx in indices }
    }

    // 내 기프티콘 불러오기
    fun getUserGifticons(page:Int) {
        viewModelScope.launch {
            val userIdx = sharedPreferencesUtil.getUserId()
            try {
                val requestDTO = GifticonRequestDTO(userIdx, page)
                val response = myService.getGifticons(requestDTO)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _gifticonItems.value = responseBody.items
                    } else {
                        _error.value = "No data received"
                    }
                } else {
                    _error.value = "Network Error: ${response.code()}"
                    // 실패시 예시 데이터 추가
                    _gifticonItems.value = getSampleData()
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
                // 예외 발생 시 예시 데이터 추가
                _gifticonItems.value = getSampleData()
            }
        }
    }

}


// API response를 위한 데이터 클래스
data class GifticonData(
    val gifticonIdx: Long,
    val gifticonAllImagName: String,
    val giftconName: String,
    val gifticonEndDate: String,
)

// 인터넷 미연결 샘플데이터
private fun getSampleData(): List<GifticonData> {
    return listOf(
        GifticonData(1,"https://via.placeholder.com/150", "Item1", "20240928172755"),
        GifticonData(2,"image2", "Item2", "20240828172755"),
        GifticonData(3,"image3", "Item3", "20240728172755"),
        GifticonData(4,"image4", "Item4", "20240628172755"),
        GifticonData(5,"image5", "Item5", "20240528172755"),
        GifticonData(6,"image6", "Item6", "20240428172755"),
        GifticonData(7,"image7", "Item7", "20240328172755"),
        GifticonData(8,"image8", "Item8", "20240228172755"),
    )
}