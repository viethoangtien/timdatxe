package beetech.com.carbooking.sprinthome.condition

import com.example.anothertimdatxe.base.mvp.BaseView
import com.example.anothertimdatxe.entity.response.TermAndConditionResponse

interface ConditionView : BaseView {
    fun showContentCondition(data: TermAndConditionResponse)
}