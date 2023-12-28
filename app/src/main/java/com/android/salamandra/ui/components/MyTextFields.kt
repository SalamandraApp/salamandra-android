package com.android.salamandra.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.android.salamandra.R

@Composable
fun MyGenericTextField(
    outlined: Boolean = true,
    modifier: Modifier,
    hint: String,
    text: String,
    readOnly: Boolean = false,
    onTextChanged: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    numberKeyboard: Boolean = false
) {
    if(outlined){
        OutlinedTextField(
            modifier = modifier,
            value = text,
            onValueChange = { onTextChanged(it) },
            label = {
                Text(
                    text = hint,
                    fontSize = 16.sp
                )
            },
            keyboardOptions = if (!numberKeyboard) KeyboardOptions.Default else KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            readOnly = readOnly,
            trailingIcon = trailingIcon,
            colors = if (readOnly) OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            ) else OutlinedTextFieldDefaults.colors()
        )

    } else{
        TextField(
            modifier = modifier,
            value = text,
            onValueChange = { onTextChanged(it) },
            label = {
                Text(
                    text = hint,
                    fontSize = 16.sp
                )
            },
            keyboardOptions = if (!numberKeyboard) KeyboardOptions.Default else KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            readOnly = readOnly,
            trailingIcon = trailingIcon,
            colors = if (readOnly) OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            ) else OutlinedTextFieldDefaults.colors()
        )
    }
}

@Composable
fun MyEmailTextField(
    modifier: Modifier,
    text: String,
    hint: String = stringResource(R.string.email),
    onTextChanged: (String) -> Unit,
    ) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = { onTextChanged(it) },
        label = {
            Text(
                text = hint,
                fontSize = 16.sp
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        ),
        singleLine = true
    )
}

@Composable
fun MyPasswordTextField(
    modifier: Modifier,
    text: String,
    hint: String = stringResource(R.string.password),
    onTextChanged: (String) -> Unit
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val img = if (passwordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = { onTextChanged(it) },
        label = {
            Text(
                text = hint,
                fontSize = 16.sp
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password
        ),
        singleLine = true,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = img, contentDescription = null)
            }
        }
    )
}