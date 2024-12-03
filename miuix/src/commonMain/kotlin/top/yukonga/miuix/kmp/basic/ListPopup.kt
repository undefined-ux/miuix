package top.yukonga.miuix.kmp.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.BackHandler
import top.yukonga.miuix.kmp.utils.MiuixPopupUtil.Companion.dismissPopup
import top.yukonga.miuix.kmp.utils.MiuixPopupUtil.Companion.showPopup
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape
import top.yukonga.miuix.kmp.utils.getWindowSize

/**
 * A popup with a list of items.
 *
 * @param show The show state of the [ListPopup].
 * @param popupModifier The modifier to be applied to the [ListPopup].
 * @param popupPositionProvider The [PopupPositionProvider] of the [ListPopup].
 * @param alignment The alignment of the [ListPopup].
 * @param onDismissRequest The callback when the [ListPopup] is dismissed.
 * @param content The [Composable] content of the [ListPopup].
 */
@Composable
fun ListPopup(
    show: MutableState<Boolean>,
    popupModifier: Modifier = Modifier,
    popupPositionProvider: PopupPositionProvider = ListPopupDefaults.DropdownPositionProvider,
    alignment: PopupPositionProvider.Align = PopupPositionProvider.Align.Right,
    onDismissRequest: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var offset by remember { mutableStateOf(IntOffset.Zero) }

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val getWindowSize = rememberUpdatedState(getWindowSize())
    var windowSize by remember { mutableStateOf(IntSize(getWindowSize.value.width, getWindowSize.value.height)) }

    var parentBounds by remember { mutableStateOf(IntRect.Zero) }
    val windowBounds by rememberUpdatedState(with(density) {
        IntRect(
            left = WindowInsets.displayCutout.asPaddingValues(density).calculateLeftPadding(layoutDirection).roundToPx(),
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding().roundToPx(),
            right = windowSize.width -
                    WindowInsets.displayCutout.asPaddingValues(density).calculateRightPadding(layoutDirection).roundToPx(),
            bottom = windowSize.height -
                    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding().roundToPx() -
                    WindowInsets.captionBar.asPaddingValues().calculateBottomPadding().roundToPx()
        )
    })
    var popupContentSize = IntSize.Zero
    var popupMargin by remember { mutableStateOf(IntRect.Zero) }


    var transformOrigin by remember { mutableStateOf(TransformOrigin.Center) }

    if (!listPopupStates.contains(show)) listPopupStates.add(show)

    LaunchedEffect(show.value) {
        if (show.value) {
            listPopupStates.forEach { state -> if (state != show) state.value = false }
        }
    }

    BackHandler(enabled = show.value) {
        dismissPopup(show)
        onDismissRequest?.let { it1 -> it1() }
    }

    DisposableEffect(Unit) {
        onDispose {
            dismissPopup(show)
            onDismissRequest?.let { it1 -> it1() }
        }
    }

    DisposableEffect(popupPositionProvider, alignment) {
        val popupMargins = popupPositionProvider.getMargins()
        popupMargin = with(density) {
            IntRect(
                left = popupMargins.calculateLeftPadding(layoutDirection).roundToPx(),
                top = popupMargins.calculateTopPadding().roundToPx(),
                right = popupMargins.calculateRightPadding(layoutDirection).roundToPx(),
                bottom = popupMargins.calculateBottomPadding().roundToPx()
            )
        }
        if (popupContentSize != IntSize.Zero) {
            offset = popupPositionProvider.calculatePosition(
                parentBounds,
                windowBounds,
                layoutDirection,
                popupContentSize,
                popupMargin,
                alignment
            )
        }
        onDispose {}
    }

    if (show.value) {
        val dropdownElevation by rememberUpdatedState(with(density) {
            11.dp.toPx()
        })
        showPopup(
            transformOrigin = { transformOrigin }
        ) {
            Box(
                modifier = popupModifier
                    .pointerInput(Unit) {
                        detectTapGestures {
                            dismissPopup(show)
                            onDismissRequest?.let { it1 -> it1() }
                        }
                    }
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(
                            constraints.copy(
                                minWidth = 200.dp.roundToPx(),
                                minHeight = 50.dp.roundToPx(),
                                maxHeight = windowBounds.height
                            )
                        )
                        popupContentSize = IntSize(placeable.width, placeable.height)
                        offset = popupPositionProvider.calculatePosition(
                            parentBounds,
                            windowBounds,
                            layoutDirection,
                            popupContentSize,
                            popupMargin,
                            alignment
                        )
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            placeable.place(offset)
                        }
                    }
            ) {
                Box(
                    Modifier
                        .align(AbsoluteAlignment.TopLeft)
                        .graphicsLayer(
                            shadowElevation = dropdownElevation,
                            shape = SmoothRoundedCornerShape(16.dp),
                            ambientShadowColor = Color.Black.copy(alpha = 0.3f),
                            spotShadowColor = Color.Black.copy(alpha = 0.3f)
                        )
                        .clip(SmoothRoundedCornerShape(16.dp))
                        .background(MiuixTheme.colorScheme.surface)
                ) {
                    content.invoke()
                }
            }
        }
    }

    Layout(
        content = {},
        modifier = Modifier.onGloballyPositioned { childCoordinates ->
            val parentCoordinates = childCoordinates.parentLayoutCoordinates!!
            val positionInWindow = parentCoordinates.positionInWindow()
            parentBounds = IntRect(
                left = positionInWindow.x.toInt(),
                top = positionInWindow.y.toInt(),
                right = positionInWindow.x.toInt() + parentCoordinates.size.width,
                bottom = positionInWindow.y.toInt() + parentCoordinates.size.height
            )
            val windowHeightPx = getWindowSize.value.height
            val windowWidthPx = getWindowSize.value.width
            windowSize = IntSize(windowWidthPx, windowHeightPx)
            with(density) {
                val xInWindow = if (alignment in listOf(
                        PopupPositionProvider.Align.Right,
                        PopupPositionProvider.Align.TopRight,
                        PopupPositionProvider.Align.BottomRight,
                    )
                )
                    parentBounds.right - popupMargin.right - 64.dp.roundToPx()
                else
                    parentBounds.left + popupMargin.left + 64.dp.roundToPx()
                val yInWindow = parentBounds.top + parentBounds.height / 2 - 56.dp.roundToPx()
                transformOrigin = TransformOrigin(
                    xInWindow / windowWidthPx.toFloat(),
                    yInWindow / windowHeightPx.toFloat()
                )
            }
        }
    ) { _, _ ->
        layout(0, 0) {}
    }
}

interface PopupPositionProvider {
    /**
     * Calculate the position (offset) of Popup
     *
     * @param anchorBounds Bounds of the anchored (parent) component
     * @param windowBounds Bounds of the safe area of window (excluding the [WindowInsets.Companion.statusBars], [WindowInsets.Companion.navigationBars] and [WindowInsets.Companion.captionBar])
     * @param layoutDirection [LayoutDirection]
     * @param popupContentSize Actual size of the popup content
     * @param popupMargin (Extra) Margins for the popup content. See [PopupPositionProvider.getMargins]
     * @param alignment Alignment of the popup (relative to the window). See [PopupPositionProvider.Align]
     */
    fun calculatePosition(
        anchorBounds: IntRect,
        windowBounds: IntRect,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
        popupMargin: IntRect,
        alignment: Align
    ): IntOffset

    /**
     * (Extra) Margins for the popup content.
     */
    fun getMargins(): PaddingValues

    /**
     * Position relative to the window, not relative to the anchor!
     */
    enum class Align {
        Left,
        Right,
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight
    }
}

object ListPopupDefaults {
    val DropdownPositionProvider = object : PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect,
            windowBounds: IntRect,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize,
            popupMargin: IntRect,
            alignment: PopupPositionProvider.Align
        ): IntOffset {
            val offsetX = if (alignment == PopupPositionProvider.Align.Right) {
                anchorBounds.right - popupContentSize.width - popupMargin.right
            } else {
                anchorBounds.left + popupMargin.left
            }
            val offsetY = if (windowBounds.bottom - anchorBounds.bottom > popupContentSize.height) {
                // Show below
                anchorBounds.bottom + popupMargin.bottom
            } else if (anchorBounds.top - windowBounds.top > popupContentSize.height) {
                // Show above
                anchorBounds.top - popupContentSize.height - popupMargin.top
            } else {
                // Middle
                anchorBounds.top + anchorBounds.height / 2 - popupContentSize.height / 2
            }
            return IntOffset(
                x = offsetX.coerceIn(windowBounds.left, windowBounds.right - popupContentSize.width - popupMargin.right),
                y = offsetY.coerceIn(windowBounds.top + popupMargin.top, windowBounds.bottom - popupContentSize.height - popupMargin.bottom)
            )
        }

        override fun getMargins(): PaddingValues {
            return PaddingValues(horizontal = 0.dp, vertical = 8.dp)
        }
    }
    val ContextMenuPositionProvider = object : PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect,
            windowBounds: IntRect,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize,
            popupMargin: IntRect,
            alignment: PopupPositionProvider.Align
        ): IntOffset {
            val offsetX: Int
            val offsetY: Int
            when (alignment) {
                PopupPositionProvider.Align.TopLeft -> {
                    offsetX = anchorBounds.left + popupMargin.left
                    offsetY = anchorBounds.bottom + popupMargin.top
                }

                PopupPositionProvider.Align.TopRight -> {
                    offsetX = anchorBounds.right - popupContentSize.width - popupMargin.right
                    offsetY = anchorBounds.bottom + popupMargin.top
                }

                PopupPositionProvider.Align.BottomLeft -> {
                    offsetX = anchorBounds.left + popupMargin.left
                    offsetY = anchorBounds.top - popupContentSize.height - popupMargin.bottom
                }

                PopupPositionProvider.Align.BottomRight -> {
                    offsetX = anchorBounds.right - popupContentSize.width - popupMargin.right
                    offsetY = anchorBounds.top - popupContentSize.height - popupMargin.bottom
                }

                else -> {
                    // Fallback
                    offsetX = if (alignment == PopupPositionProvider.Align.Right) {
                        anchorBounds.right - popupContentSize.width - popupMargin.right
                    } else {
                        anchorBounds.left + popupMargin.left
                    }
                    offsetY = if (windowBounds.bottom - anchorBounds.bottom > popupContentSize.height) {
                        // Show below
                        anchorBounds.bottom + popupMargin.bottom
                    } else if (anchorBounds.top - windowBounds.top > popupContentSize.height) {
                        // Show above
                        anchorBounds.top - popupContentSize.height - popupMargin.top
                    } else {
                        // Middle
                        anchorBounds.top + anchorBounds.height / 2 - popupContentSize.height / 2
                    }
                }
            }
            return IntOffset(
                x = offsetX.coerceIn(windowBounds.left, windowBounds.right - popupContentSize.width - popupMargin.right),
                y = offsetY.coerceIn(windowBounds.top + popupMargin.top, windowBounds.bottom - popupContentSize.height - popupMargin.bottom)
            )
        }

        override fun getMargins(): PaddingValues {
            return PaddingValues(horizontal = 20.dp, vertical = 0.dp)
        }
    }
}

val listPopupStates = mutableStateListOf<MutableState<Boolean>>()