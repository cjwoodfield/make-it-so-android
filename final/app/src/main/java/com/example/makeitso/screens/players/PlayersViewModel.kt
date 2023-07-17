/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.makeitso.screens.players

import androidx.compose.runtime.mutableStateOf
import com.example.makeitso.EDIT_PLAYER_SCREEN
import com.example.makeitso.EDIT_TASK_SCREEN
import com.example.makeitso.PLAYER_ID
import com.example.makeitso.SETTINGS_SCREEN
import com.example.makeitso.TASK_ID
import com.example.makeitso.model.Player
import com.example.makeitso.model.Task
import com.example.makeitso.model.service.ConfigurationService
import com.example.makeitso.model.service.ConfigurationServiceTEST
import com.example.makeitso.model.service.LogService
import com.example.makeitso.model.service.StorageService
import com.example.makeitso.model.service.StorageServiceTEST
import com.example.makeitso.screens.MakeItSoViewModel
import com.example.makeitso.screens.tasks.PlayerActionOption
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
  logService: LogService,
  private val storageService: StorageServiceTEST,
  private val configurationService : ConfigurationServiceTEST
) : MakeItSoViewModel(logService) {
  val options = mutableStateOf<List<String>>(listOf())

  val players = storageService.players

  fun loadPlayerOptions() {
    val hasEditOption = configurationService.isShowPlayerEditButtonConfig
    options.value = PlayerActionOption.getOptions(hasEditOption)
  }

  fun onPlayerCheckChange(player: Player) {
    launchCatching { storageService.updatePlayer(player.copy(selected = !player.selected)) }
  }

  fun onAddClick(openScreen: (String) -> Unit) = openScreen(EDIT_PLAYER_SCREEN)

  fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

  fun onPlayerActionClick(openScreen: (String) -> Unit, player: Player, action: String) {
    when (PlayerActionOption.getByTitle(action)) {
      PlayerActionOption.EditPlayer -> openScreen("$EDIT_PLAYER_SCREEN?$PLAYER_ID={${player.id}}")
//      PlayerActionOption.ToggleFlag -> onFlagTaskClick(task)
      PlayerActionOption.DeletePlayer -> onDeletePlayerClick(player)
    }
  }

//  private fun onFlagTaskClick(task: Task) {
//    launchCatching { storageService.updatePlayer(player.copy(flag = !task.flag)) }
//  }

  private fun onDeletePlayerClick(player: Player) {
    launchCatching { storageService.deletePlayer(player.id) }
  }
}
