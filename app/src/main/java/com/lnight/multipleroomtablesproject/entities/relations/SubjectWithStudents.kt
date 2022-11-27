package com.lnight.multipleroomtablesproject.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.lnight.multipleroomtablesproject.entities.Student
import com.lnight.multipleroomtablesproject.entities.Subject

data class SubjectWithStudents(
    @Embedded val subject: Subject,
    @Relation(
        parentColumn = "subjectName",
        entityColumn = "studentName",
        associateBy = Junction(StudentSubjectCrossRef::class)
    )
    val students: List<Student>
)
