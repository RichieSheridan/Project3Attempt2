package com.bcs371.project3attempt2.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource

@Composable
fun TilingBackground( //I made sure to choose tileable image files for my background
    resourceId: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val imageBitmap = ImageBitmap.imageResource(LocalContext.current.resources, resourceId)
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val tileWidth = imageBitmap.width.toFloat()
            val tileHeight = imageBitmap.height.toFloat()
            val xTiles = (size.width / tileWidth).toInt() + 1
            val yTiles = (size.height / tileHeight).toInt() + 1

            for (x in 0 until xTiles) {
                for (y in 0 until yTiles) {
                    drawImage(
                        image = imageBitmap,
                        topLeft = Offset(x * tileWidth, y * tileHeight)
                    )
                }
            }
        }
        content()
    }
} 