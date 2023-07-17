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

package com.example.makeitso.model.service.impl

import com.example.makeitso.model.Player
import com.example.makeitso.model.Task
import com.example.makeitso.model.service.AccountService
import com.example.makeitso.model.service.StorageServiceTEST
import com.example.makeitso.model.service.trace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class StorageServiceImplTEST
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService) :
  StorageServiceTEST {

  @OptIn(ExperimentalCoroutinesApi::class)
  override val players: Flow<List<Player>>
    get() =
      auth.currentUser.flatMapLatest { user ->
        firestore.collection(PLAYER_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
      }

  override suspend fun getPlayer(playerId: String): Player? =
    firestore.collection(PLAYER_COLLECTION).document(playerId).get().await().toObject()

  override suspend fun savePlayer(player: Player): String =
    trace(SAVE_PLAYER_TRACE) {
      val playerWithUserId = player.copy(userId = auth.currentUserId)
      firestore.collection(PLAYER_COLLECTION).add(playerWithUserId).await().id
    }

  override suspend fun updatePlayer(player: Player): Unit =
    trace(UPDATE_PLAYER_TRACE) {
      firestore.collection(PLAYER_COLLECTION).document(player.id).set(player).await()
    }

  override suspend fun deletePlayer(playerId: String) {
    firestore.collection(PLAYER_COLLECTION).document(playerId).delete().await()
  }

  companion object {
    private const val USER_ID_FIELD = "userId"
    private const val PLAYER_COLLECTION = "players"
    private const val SAVE_PLAYER_TRACE = "savePlayer"
    private const val UPDATE_PLAYER_TRACE = "updatePlayer"
  }
}
