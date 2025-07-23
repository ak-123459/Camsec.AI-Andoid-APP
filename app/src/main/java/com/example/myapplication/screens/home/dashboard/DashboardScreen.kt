package com.example.myapplication.screens.home.dashboard

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.DemoFaceDetails
import com.example.myapplication.DemoFaceDetailsViewModel
import com.example.myapplication.FaceDetails
import com.example.myapplication.FaceDetailsViewModel
import com.example.myapplication.RequestData
import com.example.myapplication.screens.components.DemoFaceItem
import com.example.myapplication.screens.components.FaceDetailBottomSheet
import com.example.myapplication.screens.components.FaceItem
import com.example.myapplication.screens.components.dummyFaceDetailsList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//import com.example.myapplication.screens.components.FaceList
//import com.example.myapplication.screens.components.dummyFaceDetailsList



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(

) {

    val faceViewModel: DemoFaceDetailsViewModel = viewModel()

    // Use rememberSaveable for state that survives recomposition
    val faces by faceViewModel.faces.observeAsState(emptyList())
    val error by faceViewModel.error.observeAsState()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var selectedFace by remember { mutableStateOf<DemoFaceDetails?>(null) }


    // ✅ Only fetch once — Move to ViewModel if possible
    LaunchedEffect(Unit) {

        if(faces.isEmpty()) {

            delay(500)
            faceViewModel.getDemoFacesDetails()

        } else{

            delay(500)

        }

    }

    // UI
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            selectedFace?.let { face ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .background(color = Color.White) // Fix color format
                ) {
                    FaceDetailBottomSheet(face)
                }
            }
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            when {
                error != null -> {
                    Text("Error: $error", color = Color.Red)
                }
                faces.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = faces,
                            key = { it.id } // ✅ use key for better performance
                        ) { face ->
                            Modifier
                                .fillMaxWidth()
                            DemoFaceItem(
                                face = face,
                                modifier = Modifier.animateItem(
                                    fadeInSpec = null, fadeOutSpec = null, placementSpec = spring(
                                        stiffness = Spring.StiffnessMediumLow,
                                        visibilityThreshold = IntOffset.VisibilityThreshold
                                    )
                                ) // ✅ smooth scrolling
                                    .padding(vertical = 4.dp)
                            ) {
                                selectedFace = face
                                coroutineScope.launch {
                                    sheetState.show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}





//
//@Composable
//fun FaceList(faces: List<DemoFaceDetails>) {
//    LazyColumn {
//        items(faces) { face ->
//            DemoFaceItem(face)
//        }
//    }
//}