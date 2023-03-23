package com.raccoongang.course.presentation.progress

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.raccoongang.core.UIMessage
import com.raccoongang.core.domain.model.CourseProgress
import com.raccoongang.core.ui.*
import com.raccoongang.core.ui.theme.NewEdxTheme
import com.raccoongang.core.ui.theme.appColors
import com.raccoongang.core.ui.theme.appTypography
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CourseProgressFragment : Fragment() {

    private val viewModel by viewModel<CourseProgressViewModel> {
        parametersOf(requireArguments().getString(ARG_COURSE_ID, ""))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
        with(requireArguments()) {
            viewModel.courseImage = getString(ARG_IMAGE, "")
            viewModel.courseTitle = getString(ARG_TITLE, "")
        }
        viewModel.getProgress()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            NewEdxTheme {
                val windowSize = rememberWindowSize()
                val uiState by viewModel.uiState.observeAsState()
                val uiMessage by viewModel.uiMessage.observeAsState()

                CourseProgressScreen(
                    windowSize = windowSize,
                    uiState = uiState!!,
                    uiMessage = uiMessage,
                    courseImage = viewModel.courseImage,
                    courseTitle = viewModel.courseTitle,
                    onBackClick = {
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                )
            }
        }
    }

    companion object {
        private const val ARG_COURSE_ID = "courseId"
        private const val ARG_TITLE = "title"
        private const val ARG_IMAGE = "image"
        fun newInstance(
            courseId: String,
            title: String,
            image: String,
        ): CourseProgressFragment {
            val fragment = CourseProgressFragment()
            fragment.arguments = bundleOf(
                ARG_COURSE_ID to courseId,
                ARG_TITLE to title,
                ARG_IMAGE to image,
            )
            return fragment
        }
    }
}

@Composable
private fun CourseProgressScreen(
    windowSize: WindowSize,
    uiState: CourseProgressUIState,
    uiMessage: UIMessage?,
    courseTitle: String,
    courseImage: String,
    onBackClick: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.appColors.background
    ) {

        val screenWidth by remember(key1 = windowSize) {
            mutableStateOf(
                windowSize.windowSizeValue(
                    expanded = Modifier.widthIn(Dp.Unspecified, 560.dp),
                    compact = Modifier.fillMaxWidth()
                )
            )
        }

        val imageHeight by remember(key1 = windowSize) {
            mutableStateOf(
                windowSize.windowSizeValue(
                    expanded = 260.dp,
                    compact = 200.dp
                )
            )
        }

        HandleUIMessage(uiMessage = uiMessage, scaffoldState = scaffoldState)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .statusBarsInset(),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(modifier = screenWidth) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .zIndex(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BackBtn {
                        onBackClick()
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp),
                        text = courseTitle,
                        color = MaterialTheme.appColors.textPrimary,
                        style = MaterialTheme.appTypography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(6.dp))
                Surface(
                    color = MaterialTheme.appColors.background
                ) {
                    /** A layout composable that places its children in a vertical sequence */
                    Column(modifier = Modifier.fillMaxSize()) {
                        //Start implementation here
                        Text(
                            "Implementation here",
                            Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        //TODO Image banner

                        when (uiState) {
                            is CourseProgressUIState.Loading -> {
                                //TODO ProgressBar
                            }
                            is CourseProgressUIState.Data -> {
                                /** The vertically scrolling list that only composes and
                                 * lays out the currently visible items */
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(20.dp),
                                    contentPadding = PaddingValues(vertical = 10.dp)
                                ) {
                                    /** Adds a single item to LazyColumn*/
                                    item {
                                        //TODO Overall course progress
                                    }

                                    /** Adds a list of items to LazyColumn*/
                                    items(uiState.sections) { section ->
                                        //TODO Section View

                                        Column(
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            verticalArrangement = Arrangement.spacedBy(20.dp)
                                        ) {
                                            for (subsection in section.subsections) {
                                                //TODO Subsection View
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CourseProgressBar(
    modifier: Modifier,
    progress: Int,
) {
    val primaryColor = MaterialTheme.appColors.primary

    Canvas(modifier) {
        drawLine(
            brush = SolidColor(Color.LightGray),
            start = Offset.Zero,
            end = Offset(this.size.width, 0f),
            strokeWidth = 10.dp.toPx(),
            cap = StrokeCap.Round
        )

        if (progress > 0) {
            drawLine(
                brush = SolidColor(primaryColor),
                start = Offset.Zero,
                end = Offset(this.size.width / 100f * progress, 0f),
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CourseProgressScreenPreview() {
    NewEdxTheme {
        CourseProgressScreen(
            windowSize = WindowSize(WindowType.Compact, WindowType.Compact),
            uiState = CourseProgressUIState.Data(listOf(mockSection1, mockSection2), 75),
            uiMessage = null,
            courseTitle = "Title",
            courseImage = "",
            onBackClick = {

            }
        )
    }
}

@Preview
@Composable
private fun CourseProgressBarPreview() {
    NewEdxTheme {
        CourseProgressBar(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(horizontal = 24.dp),
            progress = 35
        )
    }
}

private val mockSection1 = CourseProgress.Section(
    displayName = "Section 1",
    subsections = listOf(
        CourseProgress.Subsection(
            earned = "3f",
            total = "4f",
            percentageString = "75%",
            displayName = "Subsection 1",
            score = listOf(CourseProgress.Score("1f", "1f"), CourseProgress.Score("0f", "1f")),
            showGrades = true,
            graded = true,
            gradeType = "Final Exam"
        )
    )
)

private val mockSection2 = CourseProgress.Section(
    displayName = "Section 2",
    subsections = listOf(
        CourseProgress.Subsection(
            earned = "4f",
            total = "4f",
            percentageString = "100%",
            displayName = "Subsection 2",
            score = listOf(CourseProgress.Score("2f", "2f"), CourseProgress.Score("2f", "2f")),
            showGrades = true,
            graded = true,
            gradeType = "Final Exam"
        ),
        CourseProgress.Subsection(
            earned = "0f",
            total = "0f",
            percentageString = "0%",
            displayName = "Subsection 3",
            score = emptyList(),
            showGrades = false,
            graded = false,
            gradeType = ""
        )
    )
)