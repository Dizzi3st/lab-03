package com.example.listycity3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity3.ui.theme.ListyCity3Theme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.FloatingActionButton
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import org.intellij.lang.annotations.JdkConstants

@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }
    var showAddCityFields by remember { mutableStateOf(false) }
    var showButtonsFields by remember { mutableStateOf(false)}
    var showUpdateCityFields by remember { mutableStateOf(false)}
    var selectedCity by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    if (showAddCityFields) { // if the add/update fields are visible, toggle them
                        showAddCityFields = !showAddCityFields
                    } else if (showUpdateCityFields) {
                        showUpdateCityFields = !showUpdateCityFields
                    }
                    showButtonsFields = !showButtonsFields // toggle visibility of selection buttons
                }
            ) {
                Text("+")
            }
        }
        if (showButtonsFields) { // buttons so users can select if they are adding or updating a city without displaying both fields immediately
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End

            ) {
                Button(
                    modifier = Modifier.padding(vertical = 3.dp),
                    onClick = {
                        showAddCityFields = true
                        showButtonsFields = false
                    }) {
                    Text("Add City")
                    }
                Button(
                    modifier = Modifier.padding(vertical = 6.dp),
                    onClick = {
                        showUpdateCityFields = true
                        showButtonsFields = false
                    }) {
                    Text("Update City")
                }
            }
        }

        if (showAddCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField( // Text Field for the city name
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(  // Text Field for the province name
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(  // Button callback that adds the specified city to the cityRepository
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                            onAddCity(
                                City(
                                    name = newCityName,
                                    province = newProvinceName
                                )
                            )
                            newCityName = ""
                            newProvinceName = ""
                            showAddCityFields = false
                        }
                    }
                ) {
                    Text("Add City")
                }

            }
        }
        if (showUpdateCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField( // Text Field for the city name
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("Updated City") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(  // Text Field for the province name
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text("Updated Province") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button( // button callback that replaces the old city in the list with the newly specified one in order to update the city
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        if (selectedCity.isNotBlank() && cities.any {it.name == selectedCity}) { // Checks that the selected city isn't blank and exists in the list, referenced this article for use of the .any: https://stackoverflow.com/questions/48096204/in-kotlin-how-to-check-contains-one-or-another-value
                            if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                                onUpdateCity(
                                    cities[cities.indexOfFirst { it.name == selectedCity }], // gets the old city based on name, referenced this article for the use of .indexOfFirst to get the city based on name: https://discuss.kotlinlang.org/t/returning-an-index-of-a-collection-based-on-criteria/9691
                                    City(
                                        name = newCityName,
                                        province = newProvinceName
                                    )
                                )
                                newCityName = ""
                                newProvinceName = ""
                                showUpdateCityFields = false

                            }
                        }
                    }
                ) {
                    Text("Update City")
                }

            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(cities) { index, city ->
                CityRow(city = city, onItemClick = { city ->
                    if (selectedCity.isNotBlank()) { // city name is clickable so it can be selected for updating
                        Modifier.background(Color.White)
                        selectedCity = ""
                    } else {
                        Modifier.background(Color.Gray)
                        selectedCity = city
                    }
                })
                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun CityRow(city: City, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
                .clickable{onItemClick(city.name)}
        )

        Text(
            text = city.province,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCity3Theme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ),
                    onAddCity = {},
                    onUpdateCity = {oldCity, newCity ->}
        )
    }
}