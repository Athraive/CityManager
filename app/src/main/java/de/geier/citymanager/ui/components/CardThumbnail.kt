package de.geier.citymanager.ui.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun CardThumbnail(
    imageUri: String?,
    canEdit: Boolean,
    onReplace: (Uri) -> Unit,
    onRemove: () -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val imagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri != null) {

                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                onReplace(uri)
            }
        }

    Box(
        modifier = modifier
    ) {

        Thumbnail(
            imageUri = imageUri,
            canEdit = canEdit,
            onClick = {
                if (canEdit) {
                    imagePicker.launch(arrayOf("image/*"))
                } else {
                    onOpen()
                }
            }
        )

        if (canEdit && imageUri != null) {

            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(28.dp)
                    .clickable {
                        onRemove()
                    },
                shape = CircleShape,
                tonalElevation = 4.dp
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Bild löschen",
                        modifier = Modifier.size(16.dp)
                    )

                }
            }
        }
    }
}