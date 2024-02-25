package org.openedx.profile.presentation.settings.video

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.openedx.core.presentation.settings.VideoQualityType
import org.openedx.core.ui.BackBtn
import org.openedx.core.domain.model.VideoSettings
import org.openedx.core.ui.Toolbar
import org.openedx.core.ui.WindowSize
import org.openedx.core.ui.WindowType
import org.openedx.core.ui.displayCutoutForLandscape
import org.openedx.core.ui.noRippleClickable
import org.openedx.core.ui.rememberWindowSize
import org.openedx.core.ui.statusBarsInset
import org.openedx.core.ui.theme.OpenEdXTheme
import org.openedx.core.ui.theme.appColors
import org.openedx.core.ui.theme.appTypography
import org.openedx.core.ui.windowSizeValue
import org.openedx.profile.R
import org.openedx.profile.presentation.ProfileRouter

class VideoSettingsFragment : Fragment() {

    private val viewModel by viewModel<VideoSettingsViewModel>()
    private val router by inject<ProfileRouter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            OpenEdXTheme {
                val windowSize = rememberWindowSize()

                val videoSettings by viewModel.videoSettings.observeAsState(viewModel.currentSettings)

                VideoSettingsScreen(
                    videoSettings = videoSettings,
                    windowSize = windowSize,
                    onBackClick = {
                        requireActivity().supportFragmentManager.popBackStack()
                    },
                    wifiDownloadChanged = {
                        viewModel.setWifiDownloadOnly(it)
                    },
                    videoStreamingQualityClick = {
                        router.navigateToVideoQuality(
                            requireActivity().supportFragmentManager, VideoQualityType.Streaming
                        )
                    },
                    videoDownloadQualityClick = {
                        router.navigateToVideoQuality(
                            requireActivity().supportFragmentManager, VideoQualityType.Download
                        )
                    }
                )
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun VideoSettingsScreen(
    windowSize: WindowSize,
    videoSettings: VideoSettings,
    wifiDownloadChanged: (Boolean) -> Unit,
    videoStreamingQualityClick: () -> Unit,
    videoDownloadQualityClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    var wifiDownloadOnly by rememberSaveable {
        mutableStateOf(videoSettings.wifiDownloadOnly)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                testTagsAsResourceId = true
            },
        scaffoldState = scaffoldState
    ) { paddingValues ->

        val contentWidth by remember(key1 = windowSize) {
            mutableStateOf(
                windowSize.windowSizeValue(
                    expanded = Modifier.widthIn(Dp.Unspecified, 420.dp),
                    compact = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            )
        }

        val topBarWidth by remember(key1 = windowSize) {
            mutableStateOf(
                windowSize.windowSizeValue(
                    expanded = Modifier.widthIn(Dp.Unspecified, 560.dp),
                    compact = Modifier
                        .fillMaxWidth()
                )
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .statusBarsInset()
                    .displayCutoutForLandscape(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Toolbar(
                    modifier = topBarWidth,
                    label = stringResource(id = org.openedx.profile.R.string.profile_video_settings),
                    canShowBackBtn = true,
                    onBackClick = onBackClick
                )

                Column(
                    modifier = Modifier.then(contentWidth),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier
                            .testTag("btn_wifi_only")
                            .fillMaxWidth()
                            .height(92.dp)
                            .noRippleClickable {
                                wifiDownloadOnly = !wifiDownloadOnly
                                wifiDownloadChanged(wifiDownloadOnly)
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                modifier = Modifier.testTag("txt_wifi_only_label"),
                                text = stringResource(id = R.string.profile_wifi_only_download),
                                color = MaterialTheme.appColors.textPrimary,
                                style = MaterialTheme.appTypography.titleMedium
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                modifier = Modifier.testTag("txt_wifi_only_description"),
                                text = stringResource(id = R.string.profile_only_download_when_wifi_turned_on),
                                color = MaterialTheme.appColors.textSecondary,
                                style = MaterialTheme.appTypography.labelMedium
                            )
                        }
                        Switch(
                            modifier = Modifier.testTag("sw_wifi_only"),
                            checked = wifiDownloadOnly,
                            onCheckedChange = {
                                wifiDownloadOnly = !wifiDownloadOnly
                                wifiDownloadChanged(wifiDownloadOnly)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.appColors.primary,
                                checkedTrackColor = MaterialTheme.appColors.primary
                            )
                        )
                    }
                    Divider()
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(92.dp)
                            .clickable {
                                videoStreamingQualityClick()
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = stringResource(id = org.openedx.core.R.string.core_video_streaming_quality),
                                color = MaterialTheme.appColors.textPrimary,
                                style = MaterialTheme.appTypography.titleMedium
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = stringResource(id = videoSettings.videoStreamingQuality.titleResId),
                                color = MaterialTheme.appColors.textSecondary,
                                style = MaterialTheme.appTypography.labelMedium
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            tint = MaterialTheme.appColors.onSurface,
                            contentDescription = "Expandable Arrow"
                        )
                    }
                    Divider()
                    Row(
                        Modifier
                            .testTag("btn_video_quality")
                            .fillMaxWidth()
                            .height(92.dp)
                            .clickable {
                                videoDownloadQualityClick()
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = stringResource(id = org.openedx.core.R.string.core_video_download_quality),
                                color = MaterialTheme.appColors.textPrimary,
                                style = MaterialTheme.appTypography.titleMedium
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = stringResource(id = videoSettings.videoDownloadQuality.titleResId),
                                color = MaterialTheme.appColors.textSecondary,
                                style = MaterialTheme.appTypography.labelMedium
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            tint = MaterialTheme.appColors.onSurface,
                            contentDescription = "Expandable Arrow"
                        )
                    }
                    Divider()
                }
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun VideoSettingsScreenPreview() {
    OpenEdXTheme {
        VideoSettingsScreen(
            windowSize = WindowSize(WindowType.Compact, WindowType.Compact),
            wifiDownloadChanged = {},
            videoStreamingQualityClick = {},
            videoDownloadQualityClick = {},
            onBackClick = {},
            videoSettings = VideoSettings.default
        )
    }
}

