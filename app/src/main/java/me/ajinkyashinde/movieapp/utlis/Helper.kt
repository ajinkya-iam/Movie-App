package me.ajinkyashinde.movieapp.utlis

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import me.ajinkyashinde.movieapp.R

object Helper {

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun showInternetNotFoundDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setIcon(R.drawable.baseline_wifi_off_24)
            .setCancelable(false)
            .setPositiveButton("Try Again") { dialog, _ ->
                if (isInternetAvailable(context)) {
                    dialog.dismiss()
                } else {
                    showInternetNotFoundDialog(context)
                }
            }
            .create()
        alertDialog.show()
    }

}