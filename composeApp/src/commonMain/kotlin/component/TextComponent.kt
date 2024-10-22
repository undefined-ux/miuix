package component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Box
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.CheckboxLocation
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperCheckbox
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.extra.SuperSwitch
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.MiuixPopupUtil.Companion.dismissDialog

@Composable
fun TextComponent() {
    val showDialog = remember { mutableStateOf(false) }
    val showDialog2 = remember { mutableStateOf(false) }
    var checkbox by remember { mutableStateOf(false) }
    var checkboxTrue by remember { mutableStateOf(true) }
    var switch by remember { mutableStateOf(false) }
    var switchTrue by remember { mutableStateOf(true) }
    val dropdownOptions = listOf("Option 1", "Option 2", "Option 3", "Option 4")
    val dropdownSelectedOption = remember { mutableStateOf(0) }
    val dropdownSelectedOptionRight = remember { mutableStateOf(1) }
    var miuixSuperCheckbox by remember { mutableStateOf("State: false") }
    var miuixSuperCheckboxState by remember { mutableStateOf(false) }
    var miuixSuperRightCheckbox by remember { mutableStateOf("false") }
    var miuixSuperRightCheckboxState by remember { mutableStateOf(false) }
    var miuixSuperSwitch by remember { mutableStateOf("false") }
    var miuixSuperSwitchState by remember { mutableStateOf(false) }
    var miuixSuperSwitchAnimState by remember { mutableStateOf(false) }

    SmallTitle(text = "Basic")
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 6.dp)
    ) {
        BasicComponent(
            title = "Title",
            summary = "Summary",
            leftAction = {
                Text(text = "Left")
            },
            rightActions = {
                Text(text = "Right1")
                Spacer(Modifier.width(6.dp))
                Text(text = "Right2")
            },
            onClick = {},
            enabled = true
        )
        BasicComponent(
            title = "Title",
            summary = "Summary",
            leftAction = {
                Text(
                    text = "Left",
                    color = MiuixTheme.colorScheme.disabledOnSecondaryVariant
                )
            },
            rightActions = {
                Text(
                    text = "Right1",
                    color = MiuixTheme.colorScheme.disabledOnSecondaryVariant
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Right2",
                    color = MiuixTheme.colorScheme.disabledOnSecondaryVariant
                )
            },
            enabled = false
        )
    }

    SmallTitle(text = "Arrow & Dialog")
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 6.dp)
    ) {
        SuperArrow(
            leftAction = {
                Box(
                    contentAlignment = Alignment.TopStart,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AccountBox,
                        contentDescription = "Account",
                    )
                }
            },
            title = "Arrow",
            summary = "Click to show Dialog 1",
            onClick = {
                showDialog.value = true
            }
        )

        SuperArrow(
            title = "Arrow",
            summary = "Click to show Dialog 2",
            onClick = {
                showDialog2.value = true
            }
        )

        SuperArrow(
            title = "Disabled Arrow",
            onClick = {},
            enabled = false
        )
    }

    SmallTitle(text = "Checkbox")
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Checkbox(
                modifier = Modifier,
                checked = checkbox,
                onCheckedChange = { checkbox = it }

            )
            Checkbox(
                modifier = Modifier.padding(start = 8.dp),
                checked = checkboxTrue,
                onCheckedChange = { checkboxTrue = it }
            )
            Checkbox(
                modifier = Modifier.padding(start = 8.dp),
                enabled = false,
                checked = false,
                onCheckedChange = { }

            )
            Checkbox(
                modifier = Modifier.padding(start = 8.dp),
                enabled = false,
                checked = true,
                onCheckedChange = { }
            )
        }

        SuperCheckbox(
            checkboxLocation = CheckboxLocation.Right,
            title = "Checkbox",
            checked = miuixSuperRightCheckboxState,
            rightActions = {
                Text(
                    modifier = Modifier.padding(end = 6.dp),
                    text = miuixSuperRightCheckbox,
                    color = MiuixTheme.colorScheme.onSurfaceVariantActions
                )
            },
            onCheckedChange = {
                miuixSuperRightCheckboxState = it
                miuixSuperRightCheckbox = "$it"
            },
        )

        SuperCheckbox(
            title = "Checkbox",
            summary = miuixSuperCheckbox,
            checked = miuixSuperCheckboxState,
            onCheckedChange = {
                miuixSuperCheckboxState = it
                miuixSuperCheckbox = "State: $it"
            },
        )

        SuperCheckbox(
            title = "Disabled Checkbox",
            checked = true,
            enabled = false,
            onCheckedChange = {},
        )
    }

    SmallTitle(text = "Switch")
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Switch(
                checked = switch,
                onCheckedChange = { switch = it }
            )
            Switch(
                modifier = Modifier.padding(start = 6.dp),
                checked = switchTrue,
                onCheckedChange = { switchTrue = it }
            )
            Switch(
                modifier = Modifier.padding(start = 6.dp),
                enabled = false,
                checked = false,
                onCheckedChange = { }
            )
            Switch(
                modifier = Modifier.padding(start = 6.dp),
                enabled = false,
                checked = true,
                onCheckedChange = { }
            )
        }

        SuperSwitch(
            title = "Switch",
            summary = "Click to expand a Switch",
            checked = miuixSuperSwitchAnimState,
            onCheckedChange = {
                miuixSuperSwitchAnimState = it
            },
        )

        AnimatedVisibility(
            visible = miuixSuperSwitchAnimState,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            SuperSwitch(
                title = "Switch",
                checked = miuixSuperSwitchState,
                rightActions = {
                    Text(
                        modifier = Modifier.padding(end = 6.dp),
                        text = miuixSuperSwitch,
                        color = MiuixTheme.colorScheme.onSurfaceVariantActions
                    )
                },
                onCheckedChange = {
                    miuixSuperSwitchState = it
                    miuixSuperSwitch = "$it"
                },
            )
        }

        SuperSwitch(
            title = "Disabled Switch",
            checked = true,
            enabled = false,
            onCheckedChange = {},
        )
    }

    SmallTitle(text = "Dropdown")

    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 6.dp)
    ) {
        SuperDropdown(
            title = "Dropdown",
            summary = "Popup near click",
            items = dropdownOptions,
            selectedIndex = dropdownSelectedOption.value,
            onSelectedIndexChange = { newOption -> dropdownSelectedOption.value = newOption },
        )

        SuperDropdown(
            title = "Dropdown",
            summary = "Popup always on right",
            alwaysRight = true,
            items = dropdownOptions,
            selectedIndex = dropdownSelectedOptionRight.value,
            onSelectedIndexChange = { newOption -> dropdownSelectedOptionRight.value = newOption },
        )

        SuperDropdown(
            title = "Disabled Dropdown",
            items = listOf("Option 3"),
            selectedIndex = 0,
            onSelectedIndexChange = {},
            enabled = false
        )
    }

    dialog(showDialog)
    dialog2(showDialog2)
}


@Composable
fun dialog(showDialog: MutableState<Boolean>) {
    val value = remember { mutableStateOf("") }
    SuperDialog(
        title = "Dialog 1",
        summary = "Summary",
        show = showDialog,
        onDismissRequest = {
            dismissDialog(showDialog)
        }
    ) {
        TextField(
            modifier = Modifier.padding(bottom = 16.dp),
            value = value.value,
            maxLines = 1,
            onValueChange = { value.value = it }
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier.weight(1f),
                text = "Cancel",
                onClick = {
                    dismissDialog(showDialog)
                }
            )
            Spacer(Modifier.width(20.dp))
            Button(
                modifier = Modifier.weight(1f),
                text = "Confirm",
                submit = true,
                onClick = {
                    dismissDialog(showDialog)
                }
            )
        }
    }
}

@Composable
fun dialog2(showDialog: MutableState<Boolean>) {
    val dropdownOptions = listOf("Option 1", "Option 2")
    val dropdownSelectedOption = remember { mutableStateOf(0) }
    var miuixSuperSwitchState by remember { mutableStateOf(false) }
    SuperDialog(
        title = "Dialog 2",
        backgroundColor = MiuixTheme.colorScheme.background,
        show = showDialog,
        onDismissRequest = {
            dismissDialog(showDialog)
        }
    ) {
        Card {
            SuperDropdown(
                title = "Dropdown",
                items = dropdownOptions,
                selectedIndex = dropdownSelectedOption.value,
                onSelectedIndexChange = { newOption -> dropdownSelectedOption.value = newOption },
                defaultWindowInsetsPadding = false
            )
            SuperSwitch(
                title = "Switch",
                checked = miuixSuperSwitchState,
                onCheckedChange = {
                    miuixSuperSwitchState = it
                }
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier.weight(1f),
                text = "Cancel",
                onClick = {
                    dismissDialog(showDialog)
                }
            )
            Spacer(Modifier.width(20.dp))
            Button(
                modifier = Modifier.weight(1f),
                text = "Confirm",
                submit = true,
                onClick = {
                    dismissDialog(showDialog)
                }
            )
        }
    }
}
