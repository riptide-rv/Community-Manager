package com.example.communitymanager.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.Display.Mode
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.shape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.communitymanager.entities.MenuItem
import com.example.communitymanager.presentation.sign_in.UserData
import com.example.communitymanager.ui.theme.CommunityManagerTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userData: UserData?,
    onSignOut: () -> Unit
) {
    val menuItems = listOf(
        MenuItem(
            id = Screen.HomeScreen.route,
            title = "Profile",
            icon = Icons.Default.Person
        ),
        MenuItem(
            id = Screen.HomeScreen.route,
            title = "Setting",
            icon = Icons.Default.Settings
        )
    )
    var selectedIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope  = rememberCoroutineScope()
    ModalNavigationDrawer(
        gesturesEnabled = !drawerState.isClosed,

        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader(userData = userData , onSignOut = {})
                menuItems.forEachIndexed { index, menuItem ->
                    NavigationDrawerItem(
                        label = { Text(menuItem.title) },
                        selected = index == selectedIndex,
                        modifier = Modifier.padding(12.dp),
                        shape = MaterialTheme.shapes.extraSmall,
                        onClick = {
                            selectedIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = menuItem.icon,
                                contentDescription = menuItem.contentDescription)

                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onSignOut ,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("Log Out")
                }
            }

        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                            Text(text = "Community Manager" , textAlign = TextAlign.Center )
                            },

                    navigationIcon = {
                        Column {
                            IconButton(
                               onClick = {
                                   scope.launch {
                                       drawerState.open()
                                   }
                               },
                               content = {
                                   Icon(
                                       imageVector = Icons.Default.Menu,
                                       contentDescription = "Menu",
                                       modifier = Modifier
                                           .padding(5.dp)
                                           .size(30.dp)
                                   )
                               }
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                )
            }
        ) {

        }
    }
}


@Composable
fun DrawerHeader(
    userData: UserData?,
    onSignOut: () -> Unit
) {
    Surface(
        modifier = Modifier.shadow(
            elevation = 15.dp ,
            //shape = MaterialTheme.shapes.large ,

        )
    ) {
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    ,


                verticalAlignment = Alignment.CenterVertically
            ) {
                if (userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                } else {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    if (userData != null) {
                        Spacer(modifier = Modifier.height(2.dp))
                        userData.userName?.let { Text(text = it) }
                        Spacer(modifier = Modifier.height(4.dp))
                        userData.userEmail?.let { Text(text = it, fontSize = 14.sp) }
                    }
                    Spacer(modifier = Modifier.weight(1f))


                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

}

@Composable
fun DrawerContent(menuItems: List<MenuItem>, onMenuItemClick : (MenuItem) -> Unit ) {
    LazyColumn{
        items(menuItems){
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Icon(imageVector = it.icon, contentDescription =it.contentDescription )
                Text(text = it.title)
            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreenPreview() {
    DrawerHeader(userData = UserData(
        profilePictureUrl = null,
        userEmail = "a",
        userName = "A",
        userId = "a"
    )) {

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    CommunityManagerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(userData = null, onSignOut = {})
        }
    }

}