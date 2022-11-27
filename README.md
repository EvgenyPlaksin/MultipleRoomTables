# MultipleRoomTables

This project is an example of relating multiple entities in the [Room database](https://developer.android.com/jetpack/androidx/releases/room/).

#### There are 3 types of relationship:

- 1 to 1 relation
- 1 to n relation
- n to m relation

#### One to one relation
The project provides an example of this type of relationship - a school and a director. A school can only have one director, and a person can only be director of one school at a time.

Thus, in order to relate the director and the school, it is enough to add the primary key of the other to the entity class of either of them. You can observe this in the project:

```kotlin
@Entity
data class School(
    @PrimaryKey(autoGenerate = false)
    val schoolName: String
)
```

```kotlin
@Entity
data class Director(
    @PrimaryKey(autoGenerate = false)
    val directorName: String,
    val schoolName: String // it's the primary key of school entity
)
```

And now you can get the director by school name, or vice versa:

```kotlin
data class SchoolAndDirector(
    @Embedded val school: School,
    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val director: Director
)
```

```kotlin
@Dao
interface SchoolDao {
// ...
   @Transaction // used to prevent the multi-thread bugs
    @Query("SELECT * FROM school WHERE schoolName = :schoolName")
    suspend fun getSchoolAndDirectorWithSchoolName(schoolName: String): List<SchoolAndDirector>
// ...
    }
```

#### One to n relation
An example of such a relationship can be considered a school and students. A student can only go to one school, but a school can have multiple students.

Thus, now we will not be able to include the name of the student in the school entity, and we can only do the opposite:

```kotlin
@Entity
data class Student(
    @PrimaryKey(autoGenerate = false)
    val studentName: String,
    val semester: Int,
    val schoolName: String
)
```

```kotlin
@Entity
data class School(
    @PrimaryKey(autoGenerate = false)
    val schoolName: String
)
```

Well, everything else will be almost identical with 1 to 1 relation:

```koltin
data class SchoolWithStudents(
    @Embedded val school: School,
    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val students: List<Student>
)
```

```kotlin
@Dao
interface SchoolDao {
// ...
  @Transaction
    @Query("SELECT * FROM school WHERE schoolName = :schoolName")
    suspend fun getSchoolWithStudents(schoolName: String): List<SchoolWithStudents>
// ...
}    
 ```
 
 #### n to m relation
 This is the most difficult type of relationship to implement. Let's take students and subjects as an example. Each student can have several subjects, as well as each subject is attended by several students. Thus, we cannot add main keys to the classes of both entities, and the only thing left for us is to create an auxiliary entity, the main key of which will be the student key + subject key. Thus, we get, for example, something like this:
  
![image](https://user-images.githubusercontent.com/94696816/204152608-4447bd31-a538-4f85-8c6b-a77d67665051.png)
  
It's is how this class looks like in code:

```kotlin
@Entity(primaryKeys = ["studentName", "subjectName"])
data class StudentSubjectCrossRef(
    val studentName: String,
    val subjectName: String
)
```
And with some room magic, we can combine students with their subjects:

```kotlin
data class StudentWithSubject(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "studentName",
        entityColumn = "subjectName",
        associateBy = Junction(StudentSubjectCrossRef::class)
    )
    val subjects: List<Subject>
)
```

```kotlin
data class SubjectWithStudents(
    @Embedded val subject: Subject,
    @Relation(
        parentColumn = "subjectName",
        entityColumn = "studentName",
        associateBy = Junction(StudentSubjectCrossRef::class)
    )
    val students: List<Student>
)
```

```kotlin
@Dao
interface SchoolDao {
// ...
    @Transaction
    @Query("SELECT * FROM student WHERE studentName = :studentName")
    suspend fun getStudentWithSubjects(studentName: String): List<StudentWithSubject>

    @Transaction
    @Query("SELECT * FROM subject WHERE subjectName = :subjectName")
    suspend fun getSubjectsOfStudent(subjectName: String): List<SubjectWithStudents>
    }
```

#### And that's it! 
If this repository was helpful for you, please star it and shere with friends!
