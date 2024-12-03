package com.bcs371.project3attempt2.game

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bcs371.project3attempt2.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import android.util.Log
import com.bcs371.project3attempt2.data.Progress
import com.bcs371.project3attempt2.viewmodel.ProgressViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameScreen(
    onExit: () -> Unit,
    progressViewModel: ProgressViewModel,
    userId: Int
) {
    var draggedArrow by remember { mutableStateOf<Arrow?>(null) }
    var dragSourceIndex by remember { mutableStateOf<Int?>(null) }
    var dragSourceIsInventory by remember { mutableStateOf(true) }
    var dragInProgress by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val gameAudio = remember { GameAudio(context) }
    val isHardMode = GameStateManager.isHardMode

    DisposableEffect(isHardMode) {
        gameAudio.startBackgroundMusic(isHardMode)
        onDispose {
            gameAudio.release()
        }
    }

    val levelConfig = GameStateManager.getCurrentLevel()

    val currentLevel = remember { mutableStateOf(GameStateManager.getCurrentLevel()) }

    LaunchedEffect(levelConfig) {
        currentLevel.value = levelConfig
    }

    var characterPosition by remember { mutableStateOf(currentLevel.value.startPosition) }
    var isPlaying by remember { mutableStateOf(false) }
    var gameState by remember { mutableStateOf<GameState>(GameState.Ready) }
    var currentArrowIndex by remember { mutableStateOf(0) }
    var characterRotation by remember { mutableStateOf(0f) }
    var targetRotation by remember { mutableStateOf(0f) }
    var currentPosition by remember { mutableStateOf(Pair(0f, 0f)) }
    var targetPosition by remember { mutableStateOf(Pair(0f, 0f)) }

    val inventoryArrows = remember(currentLevel.value) {
        currentLevel.value.inventory.map { mutableStateOf<Arrow?>(it) }
    }
    val setupSquares = remember(currentLevel.value) {
        List(if (isHardMode) 7 else 5) { mutableStateOf<Arrow?>(null) }
    }

    var attempts by remember { mutableStateOf(1) }

    fun incrementAttempts() { //Deprecated way for logging parent dashboard
        attempts++
    }

    fun resetGame() {
        characterPosition = currentLevel.value.startPosition
        gameState = GameState.Ready
        isPlaying = false
        setupSquares.forEach { it.value = null }
        inventoryArrows.forEachIndexed { index, arrowState ->
            arrowState.value = if (index < currentLevel.value.inventory.size) currentLevel.value.inventory[index] else null
        }
        attempts++
        Log.d("GameScreen", "Reset game, attempts: $attempts")
    }

    var showLevelComplete by remember { mutableStateOf(false) }
    var showModeSelect by remember { mutableStateOf(false) }

    if (showLevelComplete) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Congratulations!") },
            text = { Text("You've completed the level!") },
            confirmButton = {
                Button(onClick = {
                    showLevelComplete = false
                    GameStateManager.resetProgress()
                    onExit()
                }) {
                    Text("Back to Level Select")
                }
            }
        )
    } else if (showModeSelect) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Select Mode") },
            text = { Text("Choose your difficulty") },
            confirmButton = {
                Column {
                    Button(onClick = {
                        GameStateManager.setHardMode(false)
                        showModeSelect = false
                        resetGame()  //Reset the game state
                    }) {
                        Text("Normal Mode")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        GameStateManager.setHardMode(true)
                        showModeSelect = false
                        resetGame()  //Reset the game state
                    }) {
                        Text("Hard Mode")
                    }
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TilingBackground(
            resourceId = if (isHardMode) R.drawable.hardmode else R.drawable.grass,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 100.dp, top = 50.dp)
        ) {
            //Draw the path
            currentLevel.value.gridLayout.forEachIndexed { y, row ->
                row.forEachIndexed { x, isPath ->
                    if (isPath || (x == currentLevel.value.startPosition.first && y == currentLevel.value.startPosition.second)) {
                        Box(
                            modifier = Modifier
                                .offset(
                                    x = (x * 50).dp,
                                    y = (y * 50).dp
                                )
                                .size(50.dp)
                                .background(
                                    color = when {
                                        x == currentLevel.value.endPosition.first &&
                                                y == currentLevel.value.endPosition.second -> Color(0xFFFFD700)  //Gold for end
                                        x == currentLevel.value.startPosition.first &&
                                                y == currentLevel.value.startPosition.second -> Color(0xFFFF4444)  //Red for start
                                        else -> Color(0x800000FF)  //Blue for path
                                    },
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .offset(
                        x = (currentPosition.first).dp + 5.dp,
                        y = (currentPosition.second).dp + 5.dp
                    )
                    .size(40.dp)
                    .graphicsLayer(
                        rotationZ = characterRotation
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.guy),
                    contentDescription = "Character",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .height(80.dp)
                .fillMaxWidth()
                .clipToBounds()
        ) {
            TilingBackground(resourceId = R.drawable.wood) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        setupSquares.forEachIndexed { index, arrowState ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        color = Color.LightGray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .dragAndDropTarget(
                                        shouldStartDragAndDrop = { event ->
                                            event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                        },
                                        target = remember {
                                            object : DragAndDropTarget {
                                                override fun onDrop(event: DragAndDropEvent): Boolean {
                                                    if (draggedArrow != null && !isPlaying) {
                                                        arrowState.value = draggedArrow
                                                        draggedArrow = null
                                                        dragSourceIndex = null
                                                        dragInProgress = false
                                                        return true
                                                    }
                                                    return false
                                                }
                                            }
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column {
                                    AnimatedVisibility(
                                        visible = arrowState.value != null,
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .background(
                                                    color = Color.Gray,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .dragAndDropSource {
                                                    detectTapGestures(
                                                        onLongPress = { offset ->
                                                            if (arrowState.value != null && !isPlaying) {
                                                                draggedArrow = arrowState.value
                                                                dragSourceIndex = index
                                                                dragSourceIsInventory = true
                                                                dragInProgress = true
                                                                arrowState.value = null
                                                                startTransfer(
                                                                    transferData = DragAndDropTransferData(
                                                                        clipData = ClipData.newPlainText(
                                                                            "arrow",
                                                                            draggedArrow?.name ?: ""
                                                                        )
                                                                    )
                                                                )
                                                            }
                                                        }
                                                    )
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = when (arrowState.value) {
                                                    Arrow.RIGHT -> Icons.AutoMirrored.Filled.KeyboardArrowRight
                                                    Arrow.LEFT -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
                                                    Arrow.UP -> Icons.Default.KeyboardArrowUp
                                                    Arrow.DOWN -> Icons.Default.KeyboardArrowDown
                                                    null -> Icons.AutoMirrored.Filled.KeyboardArrowRight
                                                },
                                                contentDescription = null,
                                                tint = Color.Black,
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }



                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.Gray.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (!isPlaying) {
                                        characterPosition = currentLevel.value.startPosition
                                        gameState = GameState.Ready
                                        isPlaying = true
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White
                                )
                            }
                            IconButton(
                                onClick = { resetGame() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset",
                                    tint = Color.White
                                )
                            }
                            IconButton(
                                onClick = onExit,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Exit",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    //Make the arrows in inv draggable
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        inventoryArrows.forEachIndexed { index, arrowState ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        color = Color.LightGray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .dragAndDropSource {
                                        detectTapGestures(
                                            onLongPress = { offset ->
                                                if (arrowState.value != null && !isPlaying) {
                                                    draggedArrow = arrowState.value
                                                    dragSourceIndex = index
                                                    dragSourceIsInventory = true
                                                    dragInProgress = true
                                                    arrowState.value = null
                                                    startTransfer(
                                                        transferData = DragAndDropTransferData(
                                                            clipData = ClipData.newPlainText(
                                                                "arrow",
                                                                draggedArrow?.name ?: ""
                                                            )
                                                        )
                                                    )
                                                }
                                            }
                                        )
                                    }
                                    .dragAndDropTarget(
                                        shouldStartDragAndDrop = { event ->
                                            event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                        },
                                        target = remember {
                                            object : DragAndDropTarget {
                                                override fun onDrop(event: DragAndDropEvent): Boolean {
                                                    if (draggedArrow != null && !isPlaying) {
                                                        arrowState.value = draggedArrow
                                                        draggedArrow = null
                                                        dragSourceIndex = null
                                                        dragInProgress = false
                                                        return true
                                                    }
                                                    return false
                                                }
                                            }
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                arrowState.value?.let { arrow ->
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .background(
                                                color = Color.Gray,
                                                shape = RoundedCornerShape(6.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when (arrow) {
                                                Arrow.RIGHT -> Icons.Default.KeyboardArrowRight
                                                Arrow.LEFT -> Icons.Default.KeyboardArrowLeft
                                                Arrow.UP -> Icons.Default.KeyboardArrowUp
                                                Arrow.DOWN -> Icons.Default.KeyboardArrowDown
                                            },
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            gameState = GameState.Playing
            currentArrowIndex = 0

            //Process each arrow in setup area
            while (currentArrowIndex < setupSquares.size && gameState == GameState.Playing) {
                val arrow = setupSquares[currentArrowIndex].value

                if (arrow != null) {
                    var keepMoving = true

                    while (keepMoving && gameState == GameState.Playing) {
                        val newPosition = when (arrow) {
                            Arrow.RIGHT -> Pair(characterPosition.first + 1, characterPosition.second)
                            Arrow.LEFT -> Pair(characterPosition.first - 1, characterPosition.second)
                            Arrow.UP -> Pair(characterPosition.first, characterPosition.second - 1)
                            Arrow.DOWN -> Pair(characterPosition.first, characterPosition.second + 1)
                        }

                        //Check if new position is within bounds and valid
                        if (newPosition.first in currentLevel.value.gridLayout[0].indices &&
                            newPosition.second in currentLevel.value.gridLayout.indices) {

                            val isPath = currentLevel.value.gridLayout[newPosition.second][newPosition.first]

                            if (isPath || characterPosition == currentLevel.value.startPosition ||
                                newPosition == currentLevel.value.endPosition) {
                                //Valid move, update position
                                characterPosition = newPosition
                                delay(500)

                                if (characterPosition == currentLevel.value.endPosition) {
                                    gameState = GameState.Won
                                    isPlaying = false
                                    break
                                }

                                //Only stop if we hit a nonpath tile
                                if (!isPath && characterPosition != currentLevel.value.startPosition) {
                                    keepMoving = false
                                    currentArrowIndex++
                                }
                            } else {
                                //Hit grass
                                keepMoving = false
                                currentArrowIndex++
                            }
                        } else {
                            //failure!
                            gameState = GameState.Failed
                            isPlaying = false
                            break
                        }
                    }
                } else {
                    currentArrowIndex++
                }
            }


            if (gameState == GameState.Playing) {
                gameState = GameState.Failed
                isPlaying = false
            }
        }
    }
    when (gameState) {
        GameState.Failed -> {
            //Show failure message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Level Failed, Click Restart",
                    color = Color.Red,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        GameState.Won -> {
            showLevelComplete = true
            //Show success message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Level Complete!",
                    color = Color.Green,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            LaunchedEffect(Unit) {
                progressViewModel.addProgress(
                    userId = userId,
                    attempts = attempts,
                    isHardMode = GameStateManager.isHardMode
                )
                Log.d("GameScreen", "Recording final attempts: $attempts, isHardMode=${GameStateManager.isHardMode}")
            }
        }
        else -> { }
    }

    LaunchedEffect(currentLevel.value) {
        characterPosition = currentLevel.value.startPosition
        gameState = GameState.Ready
        isPlaying = false
    }

    //Drag and drop return to soruce
    fun returnDraggedItemToSource() {
        if (draggedArrow != null && dragSourceIndex != null) {
            if (dragSourceIsInventory) {
                inventoryArrows[dragSourceIndex!!].value = draggedArrow
            } else {
                setupSquares[dragSourceIndex!!].value = draggedArrow
            }
            draggedArrow = null
            dragSourceIndex = null
            dragInProgress = false
        }
    }

    //Dragged item clean up
    LaunchedEffect(dragInProgress) {
        if (!dragInProgress && draggedArrow != null) {
            returnDraggedItemToSource()
        }
    }

    //Clean up when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            if (dragInProgress) {
                returnDraggedItemToSource()
            }
        }
    }

    LaunchedEffect(gameState) {
        when (gameState) {
            GameState.Failed ->{
                gameAudio.playLoseSound()
            }
            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            characterRotation = (characterRotation + 2f) % 360f
            delay(16) //This was a fun one to figure out, this syncs up rotation with about 60fps
        }
    }

    LaunchedEffect(characterPosition) {
        val targetX = characterPosition.first * 50f
        val targetY = characterPosition.second * 50f
        
        //Calculate rotation based on movement direction
        val newRotation = when {
            targetX > currentPosition.first -> 90f    //right
            targetX < currentPosition.first -> 270f   //left
            targetY > currentPosition.second -> 180f  //down
            targetY < currentPosition.second -> 0f    //up
            else -> characterRotation
        }

        //Animate both position and rotation
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(500)
        ) { progress, _ ->
            currentPosition = Pair(
                lerp(currentPosition.first, targetX, progress),
                lerp(currentPosition.second, targetY, progress)
            )
            characterRotation = lerp(characterRotation, newRotation, progress)
        }
    }
}

data class DragSource(val index: Int, val isInventory: Boolean)

//Game state enum
sealed class GameState {
    object Ready : GameState()
    object Playing : GameState()
    object Failed : GameState()
    object Won : GameState()
}

private fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + (end - start) * fraction
}
