package com.felicks.sirbo.ui.presentation.screens

import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.felicks.sirbo.R
import com.felicks.sirbo.ui.presentation.components.AnimatedPreloader
import com.felicks.sirbo.ui.presentation.components.AppButton
import com.felicks.sirbo.ui.presentation.components.PagerIndicator
import com.felicks.sirbo.ui.theme.OnlyMapTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    pages: List<OnboardingPage>
) {
    val pagerState = rememberPagerState { pages.size }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        // Pager con contenido
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            OnboardingPageContent(page = pages[page])
        }

        Spacer(modifier = Modifier.height(35.dp))
        // Indicadores
        PagerIndicator(
            pageCount = pages.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(0.dp)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        // Botón de continuar / siguiente
        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(50.dp),
            onClick = {
                if (pagerState.currentPage < pages.lastIndex) {
                    // Avanzar a la siguiente página
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    // Aquí podrías navegar al Home o Login
                }
            },
            text = if (pagerState.currentPage == pages.lastIndex) "Empezar" else "Siguiente",
        )
        Spacer(
            modifier = Modifier
                .weight(0.3f)
        )
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        Image(
//            painter = painterResource(id = page.image),
//            contentDescription = null,
//            modifier = Modifier.size(200.dp)
//        )
        OnboardingVisualContent(
            resId = page.image,
            isLottie = page.isLootie,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = page.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OnboardingVisualContent(
    resId: Int,
    isLottie: Boolean = false,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    lottieIterations: Int = LottieConstants.IterateForever
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (isLottie) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = lottieIterations
            )
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(size)
            )
        } else {
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier.size(size)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    val dummyPages = listOf(
        OnboardingPage(
            title = "Bienvenido a Rutas La Paz",
            description = "Una primera versión para ayudarte a planificar tus viajes diarios de manera sencilla.",
            image = R.raw.bus_carga_trackmile,
            isLootie = true
        ),
        OnboardingPage(
            title = "Explora tus rutas",
            description = "Encuentra rutas combinando transporte público y caminata. Todavía estamos mejorando la app.",
            image = android.R.drawable.ic_menu_compass,
            isLootie = false
        ),
        OnboardingPage(
            title = "Comparte y descubre",
            description = "Descubre nuevas rutas y comparte tus experiencias con otros usuarios. Esta es solo la versión inicial.",
            image = android.R.drawable.ic_menu_compass,
            isLootie = false
        ),
        OnboardingPage(
            title = "Comienza ahora",
            description = "Prueba la app, explora la ciudad y ayúdanos a mejorarla. ¡Tu feedback es valioso!",
            image = android.R.drawable.ic_input_add,
            isLootie = false
        )
    )
    OnlyMapTheme {
        OnboardingScreen(pages = dummyPages)
    }
}


data class OnboardingPage(
    val title: String,
    val description: String,
    val image: Int,
    val isLootie: Boolean
)

