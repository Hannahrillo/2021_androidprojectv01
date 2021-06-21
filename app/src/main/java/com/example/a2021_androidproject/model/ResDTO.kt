package com.example.a2021_androidproject.model

import android.icu.text.CaseMap
import com.google.gson.annotations.SerializedName


data class ResDTO(
    @SerializedName("item") val restaurants : List<Restaurant>
) {}