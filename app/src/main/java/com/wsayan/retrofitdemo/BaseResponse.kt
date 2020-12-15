import com.google.gson.annotations.SerializedName


data class BaseResponse(

    @SerializedName("data") val data: Data,
    @SerializedName("support") val support: Support
)