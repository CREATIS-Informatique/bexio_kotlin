package ch.creatis.bexio.Room



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query



@Dao
interface ContactDAO {



    @Query("DELETE FROM contacts")
    fun delete()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Contact)



    @Query("SELECT * FROM contacts ORDER BY name_un DESC")
    fun getItems(): List<Contact>



    // @Query("SELECT * FROM contacts WHERE type == :type ORDER BY nom")
    // fun getItemsByType(type: String): List<Contact>



}