package com.demo.myideas.data.network


import android.content.res.Resources
import com.demo.myideas.R
import com.demo.myideas.utility.Utility
import retrofit2.HttpException
import java.io.IOException

data class ApiError(val exception : Throwable ) {

    fun getErrorMessage(resourceManager: Resources):
            String
            = when (exception) {
        is HttpException -> when (Utility.getResponseCode(exception)) {
            304 -> resourceManager.getString(R.string.not_modified_error)
            400 -> resourceManager.getString(R.string.bad_request_error)
            401 -> resourceManager.getString(R.string.unauthorized_error)
            403 -> resourceManager.getString(R.string.forbidden_error)
            404 -> resourceManager.getString(R.string.not_found_error)
            405 -> resourceManager.getString(R.string.method_not_allowed_error)
            409  -> resourceManager.getString(R.string.conflict_error)
            422 -> resourceManager.getString(R.string.unprocessable_error)
            500 -> resourceManager.getString(R.string.server_error_error)
            else -> resourceManager.getString(R.string.unknown_error)
        }
        is IOException -> resourceManager.getString(R.string.network_error)
        else -> resourceManager.getString(R.string.unknown_error)
    }

}