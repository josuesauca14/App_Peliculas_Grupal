package com.example.movievault.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movievault.viewmodel.MovieFormViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla que proporciona un formulario para la creación de nuevas películas
 * o la edición de películas existentes. Incluye validaciones básicas y
 * selectores de fecha y género.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieFormScreen(
    viewModel: MovieFormViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val genres = listOf("Acción", "Comedia", "Drama", "Terror", "Animación", "Ciencia Ficción")
    var expanded by remember { mutableStateOf(false) }
    
    // Estado para el DatePicker
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        viewModel.releaseDate = sdf.format(Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text("Seleccionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (viewModel.id == null) "Nueva Película" else "Editar Película", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            FormField(label = "TÍTULO *", value = viewModel.title, onValueChange = { viewModel.title = it })
            
            // Selector de Fecha (DatePicker)
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text("FECHA DE ESTRENO", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
                
                OutlinedTextField(
                    value = viewModel.releaseDate,
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Selecciona una fecha", color = Color.LightGray) },
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = { 
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    showDatePicker = true
                                }
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            FormField(label = "URL DE LA IMAGEN", value = viewModel.posterPath, onValueChange = { viewModel.posterPath = it }, placeholder = "https://...")
            
            // Selector de Género (Dropdown)
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text("GÉNERO", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = viewModel.genre,
                        onValueChange = { },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { 
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, "Select", tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        expanded = true
                                    }
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        genres.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection) },
                                onClick = {
                                    viewModel.genre = selection
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            FormField(label = "DURACIÓN", value = viewModel.duration, onValueChange = { viewModel.duration = it }, placeholder = "120 min")
            FormField(label = "CALIFICACIÓN", value = viewModel.rating, onValueChange = { viewModel.rating = it })
            FormField(label = "DESCRIPCIÓN", value = viewModel.overview, onValueChange = { viewModel.overview = it }, isLong = true)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.saveMovie(onSuccess) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text("💾  Guardar Película", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.tertiary),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Cancelar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String = "", isLong: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            shape = RoundedCornerShape(12.dp),
            minLines = if (isLong) 3 else 1
        )
    }
}
