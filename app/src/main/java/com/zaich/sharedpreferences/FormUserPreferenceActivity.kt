package com.zaich.sharedpreferences

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.zaich.sharedpreferences.databinding.ActivityFormUserPreferenceBinding

class FormUserPreferenceActivity : AppCompatActivity() ,View.OnClickListener{

    companion object{
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101

        const val TYPE_ADD = 1
        const val TYPE_EDIT =  2

        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "hanya boleh terisi numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }

    private lateinit var userModel: UserModel
    private lateinit var binding: ActivityFormUserPreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUserPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener (this)
        userModel = intent.getParcelableExtra<UserModel>("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)
        var actionBarTitle = ""
        var btnTitle = ""
        when (formType) {
            TYPE_ADD -> {
                actionBarTitle = "Tambah Baru"
                btnTitle = "Simpan"
            }
            TYPE_EDIT -> {
                actionBarTitle = "Ubah"
                btnTitle = "Update"
                showPreferenceInForm()
            }
        }
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSave.text = btnTitle

    }

    private fun showPreferenceInForm() {
        with(binding){
            edtName.setText(userModel.name)
            edtEmail.setText(userModel.email)
            edtAge.setText(userModel.age)
            edtPhone.setText(userModel.phoneNumber)

            if (userModel.isLove){
                rbYes.isChecked = true
            }else{
                rbNo.isChecked = true
            }
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_save){
            with(binding){
                val name = edtName.text.toString().trim()
                val email = edtEmail.text.toString().trim()
                val age = edtAge.text.toString().trim()
                val phoneNo = edtPhone.text.toString().trim()
                val isLoveMU = rgLoveMu.checkedRadioButtonId == R.id.rb_yes

                if (name.isEmpty()){
                    edtName.error = FIELD_REQUIRED
                    return
                }
                if (email.isEmpty()){
                    edtEmail.error = FIELD_REQUIRED
                    return
                }
                if (!isValidEmail(email)){
                    edtEmail.error = FIELD_IS_NOT_VALID
                    return
                }
                if (age.isEmpty()){
                    edtAge.error = FIELD_REQUIRED
                    return
                }
                if (phoneNo.isEmpty()){
                    edtPhone.error = FIELD_REQUIRED
                    return
                }
                if (!TextUtils.isDigitsOnly(phoneNo)){
                    edtPhone.error = FIELD_DIGIT_ONLY
                    return
                }

                saveUser(name,email,age,phoneNo,isLoveMU)

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_RESULT,userModel)
                setResult(RESULT_CODE,resultIntent)

                finish()
            }
        }
    }

    private fun saveUser(name: String, email: String, age: String, phoneNo: String, isLoveMU: Boolean) {
        val userPreference = UserPreference(this)

        userModel.name = name
        userModel.email = email
        userModel.age = Integer.parseInt(age)
        userModel.phoneNumber = phoneNo
        userModel.isLove = isLoveMU

        userPreference.setUser(userModel)
        Toast.makeText(this, "DATA TERSIMPAN", Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: CharSequence):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}