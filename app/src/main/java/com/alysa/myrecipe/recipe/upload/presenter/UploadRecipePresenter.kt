//package com.alysa.myrecipe.recipe.upload.presenter
//
//import android.content.Context
//import android.util.Log
//import com.alysa.myrecipe.core.domain.recipe.upload.RequestUpload
//import com.alysa.myrecipe.core.domain.recipe.upload.ResponseUploadRecipe
//import com.alysa.myrecipe.core.remote.ApiConfig
//import com.alysa.myrecipe.core.remote.ApiServiceUpload
//import com.alysa.myrecipe.core.utils.UserDataStoreImpl
//import okhttp3.MediaType
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.File
//
//class UploadRecipePresenter(
////    private val apiServiceUpload: ApiServiceUpload,
//    private val userDataStoreImpl: UserDataStoreImpl,
//    private val context: Context
//
//) {
//
//    fun upload(name: String, description: String, Ingredient: String, Instruction: String,
//        category_id: String, unit_id: String, image: List<String>?, callback: (Boolean) -> Unit
//    ) {
//        val requestUpload =
//            RequestUpload(name, description, Ingredient, Instruction, category_id, unit_id, image)
//        val token = userDataStoreImpl.getToken()
//        val imageFile = File("path_to_your_image_file")
//        val imageRequestBody = RequestBody.create("image/*".toMediaType(), imageFile)
//        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
//
//        val apiServiceUpload = ApiConfig.getApiService(context, "uploadRecipe") as ApiServiceUpload
//        val call = apiServiceUpload.postUploadRecipe("Bearer $token", imagePart, requestUpload)
//
//        call.enqueue(object : Callback<ResponseUploadRecipe> {
//            override fun onResponse(
//                call: Call<ResponseUploadRecipe>,
//                response: Response<ResponseUploadRecipe>
//            ) {
//                if (response.isSuccessful) {
//                    val recipe = response.body()?.data
//                    if (recipe != null) {
//                        val userId = recipe.userId
//                        val name = recipe.name
//                        val desc = recipe.description
//                        val ingre = recipe.ingredient
//                        val instu = recipe.instruction
//                        val images = recipe.image
//                        val category = recipe.categoryId
//                        val type = recipe.unitId
//
//                        if (userId != null && !name.isNullOrBlank() && !desc.isNullOrBlank() && !instu.isNullOrBlank() && !ingre.isNullOrBlank() && !images.isNullOrEmpty() && category != null && type != null) {
//                            userDataStoreImpl.addRecipeUpload(
//                                userId,
//                                name,
//                                desc,
//                                ingre,
//                                instu,
//                                category,
//                                type,
//                                images
//                            )
//                            Log.d("UserPresenter", "Recipe saved: $name")
//                            callback(true)
//                        } else {
//                            callback(false)
//                        }
//                    } else {
//                        callback(false)
//                    }
//                } else {
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("UserPresenter", "Error: ${response.code()} - $errorBody")
//                    callback(false)
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseUploadRecipe>, t: Throwable) {
//                Log.e("UserPresenter", "Failed to execute upload recipe request: ${t.message}", t)
//                callback(false)
//            }
//        })
//
//    }
//}
