package com.example.anothertimdatxe.sprinthome.profile.driver.updateprofile

import com.example.anothertimdatxe.base.mvp.BasePresenterImpl
import com.example.anothertimdatxe.base.network.RetrofitManager
import com.example.anothertimdatxe.entity.response.DriverProfileResponse
import com.example.anothertimdatxe.util.CarBookingSharePreference
import com.example.anothertimdatxe.util.NetworkUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import java.io.File

class DriverUpdateProfilePresenterImpl(mView: DriverUpdateProfileView) : BasePresenterImpl<DriverUpdateProfileView>(mView), DriverUpdateProfilePresenter {
    private var fileAvatar: File? = null
    private var fileImageLicenseBefore: File? = null
    private var fileImageLicenseAfter: File? = null
    private var multipartBodyPartArray: Array<MultipartBody.Part?> = Array(3) { null }
    private var index = 0
    private var request: MutableMap<String, RequestBody> = mutableMapOf()
    private var dataDriver: DriverProfileResponse? = null

    override fun updateProfile(data: DriverProfileResponse) {
        if (data!!.fullName.isNullOrEmpty() || data!!.fullName.isNullOrBlank()) {
            mView!!.onFullNameError()
        } else if (data!!.address.isNullOrEmpty() || data!!.address.isNullOrBlank()) {
            mView!!.onAddressError()
        } else if (data!!.description.isNullOrEmpty() || data!!.description.isNullOrBlank()) {
            mView!!.onDescriptionError()
        } else {
            dataDriver = data
            if (fileAvatar != null) {
                val bodyAvatar = createRequestBodyImage(fileAvatar!!)
                multipartBodyPartArray[index++] = createMultiPartBody("app_avatar", fileAvatar!!, bodyAvatar)
            }
            if (fileImageLicenseBefore != null) {
                val bodyLicenseBefore = createRequestBodyImage(fileImageLicenseBefore!!)
                multipartBodyPartArray[index++] = createMultiPartBody("app_before_license", fileImageLicenseBefore!!, bodyLicenseBefore)
            }
            if (fileImageLicenseAfter != null) {
                val bodyLicenseAfter = createRequestBodyImage(fileImageLicenseAfter!!)
                multipartBodyPartArray[index++] = createMultiPartBody("app_after_license", fileImageLicenseAfter!!, bodyLicenseAfter)
            }
            request["_method"] = createRequestBody("PUT")
            request["full_name"] = createRequestBody(data!!.fullName!!)
            request["birthday"] = createRequestBody(data!!.birthday!!)
            request["address"] = createRequestBody(data!!.address!!)
            request["gender"] = createRequestBody(data!!.gender.toString())
            request["description"] = createRequestBody(data!!.description!!)
            val disposable = RetrofitManager.driverUpdateProfile(
                    request,
                    multipartBodyPartArray
            )
                    .doOnSubscribe {
                        mView!!.showLoading()
                    }
                    .doFinally {
                        mView!!.hideLoading()
                    }
                    .subscribe(
                            {
                                dataDriver!!.avatar = it.data!!.avatar
                                dataDriver!!.beforeLicenseImage = it.data!!.before_license_image
                                dataDriver!!.afterLicenseImage = it.data!!.after_license_image
                                var userData = CarBookingSharePreference.getUserData()
                                userData!!.full_name = dataDriver!!.fullName!!
                                userData!!.address = dataDriver!!.address
                                userData!!.birthday = dataDriver!!.birthday
                                userData!!.avatar = dataDriver!!.avatar
                                userData!!.description = dataDriver!!.description
                                userData!!.gender = data!!.gender.toString()
                                CarBookingSharePreference.setUserData(userData)
                                mView!!.onUpdateSuccess()
                                EventBus.getDefault().postSticky(dataDriver)
                            },
                            {
                                NetworkUtil.handleError(it)
                            }
                    )
            addDispose(disposable)
        }
    }

    fun createMultiPartBody(partName: String, file: File, fileReqBody: RequestBody): MultipartBody.Part {
        return MultipartBody.Part.createFormData(partName, file!!.name, fileReqBody)
    }

    fun createRequestBodyImage(file: File): RequestBody {
        return RequestBody.create(MediaType.parse("image/*"), file)
    }

    fun createRequestBody(data: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), data)
    }

    override fun setFilePath(file: String, requestCode: Int) {
        when (requestCode) {
            DriverUpdateProfileActivity.SELECT_IMAGE_FROM_ALBUM_AVATAR -> {
                fileAvatar = File(file)
            }
            DriverUpdateProfileActivity.SELECT_IMAGE_FROM_ALBUM_LICENSE_BEFORE -> {
                fileImageLicenseBefore = File(file)
            }
            DriverUpdateProfileActivity.SELECT_IMAGE_FROM_ALBUM_LICENSE_AFTER -> {
                fileImageLicenseAfter = File(file)
            }
        }
    }
}