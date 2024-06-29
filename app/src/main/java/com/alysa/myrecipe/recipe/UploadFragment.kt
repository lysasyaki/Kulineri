package com.alysa.myrecipe.recipe

import AddRecipePresenter
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alysa.myrecipe.R
import com.alysa.myrecipe.core.domain.recipe.upload.DataUpload
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import com.alysa.myrecipe.recipe.upload.View.AddRecipeView
import java.io.File

class UploadFragment : Fragment(), AddRecipeView {

    private lateinit var addRecipePresenter: AddRecipePresenter
    private lateinit var categorySpinner: Spinner
    private lateinit var typeSpinner: Spinner
    private lateinit var pictureImageView: ImageView
    private lateinit var editPictureImageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var ingredientEditText: EditText
    private lateinit var instructionEditText: EditText
    private lateinit var submitButton: Button

    private var kategoriMap: HashMap<String, Int> = HashMap()
    private var selectedCategoryId: Int = -1
    private var selectedTypeId: Int = -1
    private var selectedImageUri: Uri? = null

    private var unitMap: HashMap<String, Int> = HashMap()

    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        categorySpinner = view.findViewById(R.id.spKategori)
        typeSpinner = view.findViewById(R.id.spUnit)
        pictureImageView = view.findViewById(R.id.ivPicture)
        editPictureImageView = view.findViewById(R.id.ivEditPicture)
        nameEditText = view.findViewById(R.id.etName)
        descriptionEditText = view.findViewById(R.id.etDeskripsi)
        ingredientEditText = view.findViewById(R.id.etBahan)
        instructionEditText = view.findViewById(R.id.etLangkah)
        submitButton = view.findViewById(R.id.btnSubmit)

        // Setup image selection click listener
        editPictureImageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                pickImageFromGallery()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(READ_EXTERNAL_STORAGE),
                    PERMISSION_CODE
                )
            }
        }

        submitButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val ingredient = ingredientEditText.text.toString().trim()
            val instruction = instructionEditText.text.toString().trim()
            val selectedKategori = categorySpinner.selectedItem.toString()
            val categoryId = kategoriMap[selectedKategori]
            val selectedUnit = typeSpinner.selectedItem.toString()
            val unitId = unitMap[selectedUnit]

            // Log data upload
            Log.d("UploadFragment", "Name: $name")
            Log.d("UploadFragment", "Description: $description")
            Log.d("UploadFragment", "Ingredient: $ingredient")
            Log.d("UploadFragment", "Instruction: $instruction")
            Log.d("UploadFragment", "CategoryId: $categoryId")
            Log.d("UploadFragment", "UnitId: $unitId")
            Log.d("UploadFragment", "ImageUri: $selectedImageUri")


            if (name.isNotEmpty() && description.isNotEmpty() && ingredient.isNotEmpty() && instruction.isNotEmpty() && selectedImageUri != null) {
                val imagePath = getRealPathFromURI(selectedImageUri)
                if (imagePath != null) {
                    val imageFile = File(imagePath)
                    val dataUpload = DataUpload(
                        name = name,
                        description = description,
                        ingredient = ingredient,
                        instruction = instruction,
                        categoryId = categoryId ?: -1,
                        unitId = unitId ?: -1
                    )

                    addRecipePresenter.postUploadRecipe(dataUpload, imageFile)
                } else {
                    Toast.makeText(requireContext(), "Failed to get image path", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize the presenter
        val userDataStore = UserDataStoreImpl(requireContext()) // Assuming UserDataStoreImpl takes context

        addRecipePresenter = AddRecipePresenter(requireContext(), userDataStore, this)

        // Setup the spinners
        setupSpinner()
        setupSpinnerUnit()

        return view
    }

    private fun setupSpinner() {
        kategoriMap["Modern"] = 1
        kategoriMap["Tradisional"] = 2
        kategoriMap["Barat"] = 3
        kategoriMap["Asia"] = 4

        val kategoriList = ArrayList(kategoriMap.keys)
        val adapter = object : ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_item, kategoriList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black)) // Atur warna teks menjadi hitam atau warna yang Anda inginkan
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black)) // Atur warna teks dropdown menjadi hitam atau warna yang Anda inginkan
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedKategori = parent.getItemAtPosition(position).toString()
                val selectedValue = kategoriMap[selectedKategori]!!
                Toast.makeText(requireContext(), "Selected Category id $selectedValue", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun setupSpinnerUnit() {
        unitMap["Minuman"] = 1
        unitMap["Makanan"] = 2

        val unitList = ArrayList(unitMap.keys)
        val adapter = object : ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_item, unitList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black)) // Atur warna teks menjadi hitam atau warna yang Anda inginkan
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black)) // Atur warna teks dropdown menjadi hitam atau warna yang Anda inginkan
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = adapter

        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedUnit = parent.getItemAtPosition(position).toString()
                val selectedValue = unitMap[selectedUnit]!!
                Toast.makeText(requireContext(), "Selected Unit id $selectedValue", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            pictureImageView.setImageURI(selectedImageUri)
        }
    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentUri?.let { requireContext().contentResolver.query(it, proj, null, null, null) }
        cursor?.moveToFirst()
        val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) ?: return null
        val path = cursor.getString(column_index)
        cursor.close()
        return path
    }

    private fun clearFields() {
        nameEditText.text.clear()
        descriptionEditText.text.clear()
        ingredientEditText.text.clear()
        instructionEditText.text.clear()
        pictureImageView.setImageResource(R.drawable.gambar_default)
        selectedImageUri = null
    }

    override fun showAddRecipeSuccessMessage(message: String?, data: DataUpload?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        clearFields()
    }

    override fun showAddRecipeErrorMessage(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
