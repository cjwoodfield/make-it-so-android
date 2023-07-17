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

package com.example.makeitso.screens.edit_task

import androidx.compose.runtime.mutableStateOf
import com.example.makeitso.PLAYER_DEFAULT_ID
import com.example.makeitso.TASK_DEFAULT_ID
import com.example.makeitso.common.ext.idFromParameter
import com.example.makeitso.model.Player
import com.example.makeitso.model.Task
import com.example.makeitso.model.service.LogService
import com.example.makeitso.model.service.StorageService
import com.example.makeitso.model.service.StorageServiceTEST
import com.example.makeitso.screens.MakeItSoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditPlayerViewModel @Inject constructor(
  logService: LogService,
  private val storageService: StorageServiceTEST,
) : MakeItSoViewModel(logService) {
  val player = mutableStateOf(Player())

  fun initialize(playerId: String) {
    launchCatching {
      if (playerId != PLAYER_DEFAULT_ID) {
        player.value = storageService.getPlayer(playerId.idFromParameter()) ?: Player()
      }
    }
  }

  fun onNameChange(newValue: String) {
    player.value = player.value.copy(name = newValue)
  }





  fun onDoneClick(popUpScreen: () -> Unit) {
    launchCatching {
      val editedPlayer = player.value
      if (editedPlayer.id.isBlank()) {
        storageService.savePlayer(editedPlayer)
      } else {
        storageService.updatePlayer(editedPlayer)
      }
      popUpScreen()
    }
  }


}
