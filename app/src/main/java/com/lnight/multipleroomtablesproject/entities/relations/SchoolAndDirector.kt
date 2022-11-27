package com.lnight.multipleroomtablesproject.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.lnight.multipleroomtablesproject.entities.Director
import com.lnight.multipleroomtablesproject.entities.School

data class SchoolAndDirector(
    @Embedded val school: School,
    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val director: Director
)