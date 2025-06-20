package com.example.myspec.api.body

import com.example.myspec.models.RefreshToken
import com.example.myspec.models.Bonus

data class SaveBonusBody(val token: RefreshToken, val bonus: Bonus)
