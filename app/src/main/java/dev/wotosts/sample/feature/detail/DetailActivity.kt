package dev.wotosts.sample.feature.detail

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.wotosts.sample.R
import dev.wotosts.sample.databinding.ActivityDetailBinding
import dev.wotosts.sample.feature.base.BaseActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>(layoutId = R.layout.activity_detail) {

    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind {
            vm = viewModel
        }

        collectEffect()
    }

    private fun collectEffect() {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is DetailUiEffect.OnClickedFinish -> finish()
                is DetailUiEffect.OnClickedWeb -> openWeb(effect.link)
                is DetailUiEffect.ShowErrorToast -> {
                    showErrorToast()
                    finish()
                }
            }
        }
            .launchIn(lifecycleScope)
    }

    private fun openWeb(link: String) {
        if (link.isBlank()) return

        try {
            val webpage: Uri = Uri.parse(link)
            val intent: CustomTabsIntent = CustomTabsIntent.Builder().build()
            intent.launchUrl(this, webpage)
        } catch (e: Exception) {
            showErrorToast()
        }
    }

    private fun showErrorToast() {
        Toast.makeText(
            this,
            "오류가 발생했어요.",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val KEY_ISBN = "isbn"
    }
}