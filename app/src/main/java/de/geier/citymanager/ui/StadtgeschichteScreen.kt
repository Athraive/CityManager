package de.geier.citymanager.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.components.CardThumbnail
import de.geier.citymanager.ui.components.EditableCard
import de.geier.citymanager.ui.components.ImageViewerDialog
import de.geier.citymanager.ui.components.MoveButtons
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun StadtgeschichteScreen(
    cityId: String,
    cityViewModel: CityViewModel,
    accessContext: AccessContext
) {

    val cards by cityViewModel.cityHistoryCards.collectAsState()

    var imageViewerUri by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(

        floatingActionButton = {

            if (accessContext.canEdit()) {

                FloatingActionButton(
                    onClick = {
                        cityViewModel.createHistoryCard()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Historieneintrag hinzufügen"
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

            itemsIndexed(
                items = cards,
                key = { _, card -> card.id }
            ) { index, card ->

                EditableCard(

                    title = card.title,

                    subtitle = card.subtitle,

                    content = card.content,

                    canEdit = accessContext.canEdit(),

                    titleEditable = true,

                    thumbnail =
                        if (!accessContext.canEdit() && card.imageUri == null) {

                            null

                        } else {

                            {

                                CardThumbnail(
                                    imageUri = card.imageUri,
                                    canEdit = accessContext.canEdit(),

                                    onReplace = { uri ->
                                        cityViewModel.saveHistoryCard(
                                            card.copy(imageUri = uri.toString())
                                        )
                                    },

                                    onRemove = {
                                        cityViewModel.saveHistoryCard(
                                            card.copy(imageUri = null)
                                        )
                                    },

                                    onOpen = {
                                        imageViewerUri = card.imageUri
                                    }
                                )

                            }

                        },



                    moveButtons = {

                        if (accessContext.canEdit()) {

                            MoveButtons(

                                canMoveUp = index > 0,

                                canMoveDown = index < cards.lastIndex,

                                onMoveUp = {

                                    cityViewModel.moveHistoryCardUp(card)

                                },

                                onMoveDown = {

                                    cityViewModel.moveHistoryCardDown(card)

                                }

                            )

                        }

                    },

                    onSave = { title, subtitle, content ->

                        cityViewModel.saveHistoryCard(

                            card.copy(

                                title = title,

                                subtitle = subtitle,

                                content = content

                            )

                        )

                    },

                    onDelete = {

                        cityViewModel.deleteHistoryCard(card)

                    }

                )

            }

        }

        imageViewerUri?.let { uri ->

            ImageViewerDialog(

                imageUri = uri,
                onDismiss = {
                    imageViewerUri = null
                }

            )

        }

    }

}