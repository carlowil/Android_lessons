package ru.mirea.sizov.employeedb

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity
class Superhero {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String? = null
    var universe: String? = null
}

@Dao
interface SuperheroDao {
    @get:Query("SELECT * FROM superhero")
    val all: List<Superhero?>?

    @Query("SELECT * FROM superhero WHERE id = :id")
    fun getById(id: Long): Superhero?

    @Insert
    fun insert(employee: Superhero)

    @Update
    fun update(employee: Superhero)

    @Delete
    fun delete(employee: Superhero)
}

@Database(entities = [Superhero::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun superheroDao(): SuperheroDao?
}