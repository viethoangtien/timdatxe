package com.example.anothertimdatxe.sprinthome.listrequest.user.list

import com.example.anothertimdatxe.base.mvp.BasePresenterImpl
import com.example.anothertimdatxe.base.network.RetrofitManager
import com.example.anothertimdatxe.util.DateUtil
import com.example.anothertimdatxe.util.NetworkUtil

class ListRequestPresenterImpl(mView: ListRequestView) : BasePresenterImpl<ListRequestView>(mView), ListRequestPresenter {
    private var bookTime: String = ""
    private var startPoint: String = ""
    private var endPoint: String = ""
    private var limit: Int = 10
    private var totalPage: Int = 0
    private var pageIndex = 1
    private var status: String = ""
    private var data: MutableMap<String, Any?> = mutableMapOf()

    override fun initSpinnerStatus() {
        val listOfStatus = listOf(
                "Chọn trạng thái",
                "Đặt thất bại",
                "Đặt thành công",
                "Kết thúc",
                "Hủy chuyến"
        )
        mView?.setSpinnerStatus(listOfStatus)
    }

    override fun refreshData() {
        resetData()
    }

    private fun resetData() {
        pageIndex = 1
        totalPage = 0
        fetchUserListBook()
    }

    override fun fetchUserListBook() {
        setQueryParams()
        addDispose(RetrofitManager.getUserListBook(data)
                .doOnSubscribe {
                    if (pageIndex == 1 && mView != null){
                        mView!!.showRefreshing()
                    }
                }
                .doFinally {
                    mView!!.hideRefreshing()
                    if (pageIndex <= totalPage) {
                        mView!!.enableLoadingMore(true)
                    } else {
                        mView!!.enableLoadingMore(false)
                    }
                }
                .subscribe(
                        {
                            pageIndex++
                            totalPage = it.total_page!!
                            mView?.showListUserPost(it.data!!)
                        },
                        {
                            NetworkUtil.handleError(it)
                        }
                ))
    }

    private fun setQueryParams() {
        data["book_time"] = bookTime
        data["start_point"] = startPoint
        data["end_point"] = startPoint
        data["status"] = status
        data["limit"] = limit
        data["page"] = pageIndex

    }

    override fun setStatus(status: Int) {
        this.status = if (status == 0) {
            ""
        } else {
            (status - 1).toString()
        }
        resetData()
    }

    override fun setDate(date: String) {
        if (!date.isNullOrEmpty()) {
            bookTime = DateUtil.formatDate(date, DateUtil.DATE_FORMAT_23, DateUtil.DATE_FORMAT_1)
        } else {
            bookTime = ""
        }
        resetData()
    }
}