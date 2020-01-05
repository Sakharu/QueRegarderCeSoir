package com.sakharu.queregardercesoir.ui.detailMovie.chooseUserList

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.locale.model.UserList

class ChooseUserListDialog
{
    private var dialog : Dialog? = null

    fun showDialog(activity: Activity,listCompleteUserList:List<UserList>,listUserListFromMovie:List<UserList>)
    {
        dialog = Dialog(activity)

        dialog!!.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_choose_userlist)
            setCancelable(true)
        }


        val recyclerUserList = dialog!!.findViewById<RecyclerView>(R.id.recyclerUserListDialogChoose)
        recyclerUserList.apply {
            layoutManager = LinearLayoutManager(dialog!!.context, LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(false)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(dialog!!.context, R.anim.layout_animation_fall_down)
            scheduleLayoutAnimation()
            addItemDecoration(DividerItemDecoration(activity, (layoutManager as LinearLayoutManager).orientation))
            adapter = ChooseUserListAdapter(listCompleteUserList.toMutableList(),listUserListFromMovie.toMutableList())
        }



        dialog!!.show()
    }


}