package dev.wotosts.sample.feature.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import dev.wotosts.sample.domain.model.Book
import dev.wotosts.sample.feature.components.VerticalDivider
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun DetailScreen(viewModel: DetailViewModel = hiltViewModel(), finish: () -> Unit) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is DetailUiEffect.OnClickedFinish -> finish()
            is DetailUiEffect.OnClickedWeb -> context.openWeb(effect.link)
            is DetailUiEffect.ShowErrorToast -> {
                context.showErrorToast()
                finish()
            }
        }
    }

    DetailScreen(
        isLoading = uiState.isLoading,
        book = uiState.book,
        onClickBack = viewModel::onClickFinish,
        onClickDetail = {
            viewModel.onClickWeb()
        })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    isLoading: Boolean,
    book: Book?,
    onClickBack: () -> Unit,
    onClickDetail: (String) -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = { }, navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        })
    }, bottomBar = {
        if (book != null) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = { onClickDetail(book.url) }) {
                Text(text = "상세 보기")
            }
        }
    }) {
        val modifier = Modifier
            .padding(it)
            .consumeWindowInsets(it)

        if (isLoading)
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        else if (book != null) {
            BookDetailView(modifier, book)
        }
    }
}

@Composable
fun BookDetailView(modifier: Modifier = Modifier, book: Book) {
    val scrollState = rememberScrollState()
    val typography = MaterialTheme.typography

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState)) {
        book.run {
            SubcomposeAsyncImage(
                modifier = Modifier.aspectRatio(1.0f),
                model = image,
                contentDescription = null,
                loading = {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(text = title, style = typography.titleLarge)
                Text(text = subtitle, style = typography.titleSmall)
                Row(Modifier.height(IntrinsicSize.Min)) {
                    val info =
                        listOf(author, publisher, "${year}년", "${page}쪽")
                    info.forEachIndexed { i, it ->
                        Text(text = it.ifBlank { "-" }, style = typography.labelMedium)
                        if (i < info.size - 1) VerticalDivider(
                            modifier = Modifier.padding(
                                horizontal = 5.dp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = price, style = typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = desc, style = typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookDetailViewPreview() {
    DetailScreen(
        isLoading = false,
        book = Book(
            title = "테스트",
            subtitle = "프리뷰를 봅시다.",
            author = "지은님",
            publisher = "내맥북",
            isbn13 = "sdfawads",
            isbn10 = "sdfevsea",
            page = 300,
            year = 2024,
            rating = 4.5f,
            desc = "컴포저블 프리뷰를 보기 위한 테스트입니다. 상세 정보에 뭘 써야할지 전혀 모르겠네요",
            image = "",
            url = "",
            price = "15000"
        ),
        onClickBack = {},
        onClickDetail = {}
    )
}