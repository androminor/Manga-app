
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by Varun Singh
 */

class FaceDetectionViewModel  :ViewModel() {
    private val _hasCameraPermission = MutableStateFlow(false)
    val hasCameraPermission = _hasCameraPermission.asStateFlow()
    fun updateCameraPermission(hasPermission: Boolean) {
        _hasCameraPermission.value = hasPermission
    }
}