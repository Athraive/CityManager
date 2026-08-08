package de.geier.citymanager.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.geier.citymanager.ui.components.CardThumbnail
import de.geier.citymanager.ui.components.EditableCard
import de.geier.citymanager.ui.components.MoveButtons
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.components.ImageViewerDialog

@Composable
fun CityDistrictListScreen(
    cityId: String,
    cityViewModel: CityViewModel,
    accessContext: AccessContext,
    onDistrictSelected: (String) -> Unit
) {

    val canEdit = accessContext.canEdit()

    val city by cityViewModel.city.collectAsState()

    val districts by cityViewModel.cityDistricts.collectAsState()

    var openedImage by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(

        floatingActionButton = {

            if (canEdit) {

                FloatingActionButton(
                    onClick = {
                        cityViewModel.createDistrict()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Stadtviertel hinzufügen"
                    )
                }

            }

        }

    ) { paddingValues ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            contentPadding = PaddingValues(24.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            item {

                DistrictMapCard(

                    imageUri = city?.backgroundImageUri,

                    canEdit = canEdit,

                    onReplace = { uri ->

                        city?.let {

                            cityViewModel.saveCity(
                                it.copy(
                                    backgroundImageUri = uri
                                )
                            )

                        }

                    },

                    onRemove = {

                        city?.let {

                            cityViewModel.saveCity(
                                it.copy(
                                    backgroundImageUri = null
                                )
                            )

                        }

                    },

                    onOpen = {

                        openedImage = city?.backgroundImageUri

                    }

                )

            }

            item {

                HorizontalDivider()

            }

            districts.forEach {
                android.util.Log.d(
                    "DistrictOrder",
                    "${it.name} -> ${it.orderIndex}"
                )
            }

            itemsIndexed(
                items = districts,
                key = { _, district -> district.id }
            ) { index, district ->

                EditableCard(

                    title = district.name,

                    subtitle = district.subtitle,

                    content = district.description,

                    canEdit = canEdit,

                    titleEditable = true,

                    thumbnail =
                        if (!canEdit && district.imageUri == null) {

                            null

                        } else {

                            {

                                CardThumbnail(

                                    imageUri = district.imageUri,

                                    canEdit = canEdit,

                                    onReplace = { uri ->

                                        cityViewModel.saveDistrict(

                                            district.copy(
                                                imageUri = uri.toString()
                                            )

                                        )

                                    },

                                    onRemove = {

                                        cityViewModel.saveDistrict(

                                            district.copy(
                                                imageUri = null
                                            )

                                        )

                                    },

                                    onOpen = {

                                        openedImage = district.imageUri

                                    }

                                )

                            }

                        },

                    onSave = { title, subtitle, content ->

                        cityViewModel.saveDistrict(
                            district.copy(
                                name = title,
                                subtitle = subtitle,
                                description = content
                            )
                        )

                    },

                    onDelete = {

                        cityViewModel.deleteDistrict(
                            district.id
                        )

                    },

                    moveButtons = {

                        if (canEdit) {

                            MoveButtons(

                                canMoveUp = index > 0,

                                canMoveDown = index < districts.lastIndex,

                                onMoveUp = {

                                    cityViewModel.moveDistrictUp(
                                        district
                                    )

                                },

                                onMoveDown = {

                                    cityViewModel.moveDistrictDown(
                                        district
                                    )

                                }

                            )

                        }

                    }

                )

            }

        }

        openedImage?.let { uri ->

            ImageViewerDialog(

                imageUri = uri,

                onDismiss = {

                    openedImage = null

                }

            )

        }

    }

}
@Composable
private fun DistrictMapCard(
    imageUri: String?,
    canEdit: Boolean,
    onReplace: (String) -> Unit,
    onRemove: () -> Unit,
    onOpen: () -> Unit
) {
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            onReplace(it.toString())
        }
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {

            if (imageUri != null) {

                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            if (canEdit) {
                                imagePicker.launch("image/*")
                            } else {
                                onOpen()
                            }
                        },
                    contentScale = ContentScale.Crop
                )

                if (canEdit) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Bild entfernen"
                        )
                    }
                }

            } else {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            if (canEdit) {
                                imagePicker.launch("image/*")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Icon(
                            imageVector = Icons.Outlined.Image,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Keine Stadtviertelkarte",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}