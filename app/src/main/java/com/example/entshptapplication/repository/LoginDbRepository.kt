package com.example.entshptapplication.repository

import android.text.format.DateFormat
import com.example.entshptapplication.dao.LoginDao
import com.example.entshptapplication.databaseModels.LoginDb
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class LoginDbRepository(private val loginDao: LoginDao) {

    suspend fun GetLogin():LoginDb?{
         val current = LocalDate.now()
        return loginDao.GetLogin(current)
    }

    suspend fun Login(login: LoginDb) {
        loginDao.Insert(login)
    }

}