package com.lnight.multipleroomtablesproject.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.lnight.multipleroomtablesproject.entities.School
import com.lnight.multipleroomtablesproject.entities.Student

data class SchoolWithStudents(
    @Embedded val school: School,
    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val students: List<Student>
)