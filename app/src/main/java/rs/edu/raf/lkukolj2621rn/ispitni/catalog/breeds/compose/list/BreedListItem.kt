package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreedListItem(data: BreedData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(all = 16.dp),
                text = data.name
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.padding(end = 16.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "See details",
            )
        }

        Text(modifier = Modifier.padding(start = 16.dp),
            text = data.alternateNames.joinToString {it})


        Row (
            Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp)) {
            Text(
                text = if(data.description.length <= 250) data.description
                else data.description.take(250-3) + "...",
            )


        }

        FlowRow(horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(bottom = 5.dp)
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
        ) {
            data.temperament.take(3).forEach {
                key(it) {
                    SuggestionChip(onClick = { }, label = { Text(text = it) })
                }
            }
        }
    }
}

@Preview
@Composable
fun BreedListItemPreview() {
    CatalogTheme {
        BreedListItem(data = BreedData("xd", "macka", listOf("macor", "maca"), "neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka",
            listOf("Srbija"), listOf("1", "2", "333", "4", "5", "6", "7"), "-1 do 0", "150", false, "google.com", 1, 1, 1, 1, 1)
        ) {
            
        }
    }
}