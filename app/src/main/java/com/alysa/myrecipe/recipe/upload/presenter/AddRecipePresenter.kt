import android.content.Context
import android.util.Log
import com.alysa.myrecipe.core.domain.recipe.upload.DataUpload
import com.alysa.myrecipe.core.domain.recipe.upload.ResponseUploadRecipe
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceUpload
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import com.alysa.myrecipe.recipe.upload.View.AddRecipeView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddRecipePresenter(
    private val context: Context,
    private val userDataStore: UserDataStoreImpl,
    private val view: AddRecipeView
) {

    private val apiServiceUpload: ApiServiceUpload = ApiConfig.getApiService(context, "uploadRecipe") as ApiServiceUpload

    fun postUploadRecipe(dataUpload: DataUpload, imageFile: File) {
        // Log dataUpload to verify its content
        Log.d("AddRecipePresenter", "DataUpload: $dataUpload")

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)
        val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        // Call the API service method to upload recipe
        val call = apiServiceUpload.postUploadRecipe(
            "Bearer ${userDataStore.getToken()}", body,
            RequestBody.create("text/plain".toMediaTypeOrNull(), dataUpload.name ?: ""),
            RequestBody.create("text/plain".toMediaTypeOrNull(), dataUpload.description ?: ""),
            RequestBody.create("text/plain".toMediaTypeOrNull(), dataUpload.ingredient ?: ""),
            RequestBody.create("text/plain".toMediaTypeOrNull(), dataUpload.instruction ?: ""),
            RequestBody.create("text/plain".toMediaTypeOrNull(), dataUpload.categoryId?.toString() ?: ""),
            RequestBody.create("text/plain".toMediaTypeOrNull(), dataUpload.unitId?.toString() ?: "")
        )

        call.enqueue(object : Callback<ResponseUploadRecipe> {
            override fun onResponse(call: Call<ResponseUploadRecipe>, response: Response<ResponseUploadRecipe>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.data
                    val recipeData = DataUpload().apply {
                        id = responseData?.id ?: 0
                        name = responseData?.name.orEmpty()
                        description = responseData?.description.orEmpty()
                        ingredient = responseData?.ingredient.orEmpty()
                        instruction = responseData?.instruction.orEmpty()
                        categoryId = responseData?.categoryId ?: 0
                        unitId = responseData?.unitId ?: 0
                        image = responseData?.image.orEmpty()
                        updatedAt = responseData?.updatedAt.orEmpty()
                        createdAt = responseData?.createdAt.orEmpty()
                    }
                    view.showAddRecipeSuccessMessage(response.body()?.message ?: "Success", recipeData)
                } else {
                    view.showAddRecipeErrorMessage(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseUploadRecipe>, t: Throwable) {
                view.showAddRecipeErrorMessage(t.message ?: "Unknown error")
            }
        })
    }
}
