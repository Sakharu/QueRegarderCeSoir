package com.sakharu.queregardercesoir.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.nio.charset.Charset

fun View.hide()
{
    visibility=View.GONE
}

fun View.show()
{
    visibility = View.VISIBLE
}

fun View.setInvisible()
{
    visibility = View.INVISIBLE
}

fun View.isVisible() : Boolean
{
    return visibility==View.VISIBLE
}

fun AppCompatActivity.showLongSnackBar(message:String) = Snackbar.make(this.window.decorView.rootView,message,Snackbar.LENGTH_LONG).show()


fun AppCompatActivity.showShortSnackBar(message:String) = Snackbar.make(this.window.decorView.rootView,message,Snackbar.LENGTH_SHORT).show()


fun AppCompatActivity.showInfiniteSnackBar(message:String) = Snackbar.make(this.window.decorView.rootView,message,Snackbar.LENGTH_INDEFINITE).show()


fun AppCompatActivity.showShortToast(context: Context, message:String) = Toast.makeText(context,message,Toast.LENGTH_SHORT).show()

fun AppCompatActivity.showLongToast(context: Context, message:String) = Toast.makeText(context,message,Toast.LENGTH_LONG).show()

fun Boolean.toInt() = if (this) 1 else 0

fun String.convertToUTF8() : String = String(this.toByteArray(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"))

fun String.isNumber() : Boolean = this.matches("-?\\d+(\\.\\d+)?".toRegex())


