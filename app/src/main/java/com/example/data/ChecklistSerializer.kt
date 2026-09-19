package com.example.data

import org.json.JSONArray
import org.json.JSONObject

object ChecklistSerializer {
    fun serialize(items: List<ChecklistItem>): String {
        val array = JSONArray()
        for (item in items) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("text", item.text)
                put("isChecked", item.isChecked)
            }
            array.put(obj)
        }
        return array.toString()
    }

    fun deserialize(json: String?): List<ChecklistItem> {
        if (json.isNullOrBlank()) return emptyList()
        val list = mutableListOf<ChecklistItem>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    ChecklistItem(
                        id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                        text = obj.optString("text", ""),
                        isChecked = obj.optBoolean("isChecked", false)
                    )
                )
            }
        } catch (e: Exception) {
            // fallback
        }
        return list
    }
}
