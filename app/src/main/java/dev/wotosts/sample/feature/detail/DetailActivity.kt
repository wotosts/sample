package dev.wotosts.sample.feature.detail

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import dagger.hilt.android.AndroidEntryPoint
import dev.wotosts.sample.feature.ui.BookAppTheme

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BookAppTheme {
                DetailScreen(viewModel = viewModel, finish = ::finish)
            }
        }
    }

    companion object {
        const val KEY_ISBN = "isbn"
    }
}

fun Context.openWeb(link: String) {
    if (link.isBlank()) return

    try {
        val webpage: Uri = Uri.parse(link)
        val intent: CustomTabsIntent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this, webpage)
    } catch (e: Exception) {
        showErrorToast()
    }
}

fun Context.showErrorToast() {
    Toast.makeText(
        this,
        "오류가 발생했어요.",
        Toast.LENGTH_SHORT
    ).show()
}
