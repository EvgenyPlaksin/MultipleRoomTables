package com.lnight.multipleroomtablesproject.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.lnight.multipleroomtablesproject.entities.Student
import com.lnight.multipleroomtablesproject.entities.Subject

data class StudentWithSubject(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "studentName",
        entityColumn = "subjectName",
        associateBy = Junction(StudentSubjectCrossRef::class)
    )
    val subjects: List<Subject>
)
