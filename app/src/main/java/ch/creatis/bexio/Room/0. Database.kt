package ch.creatis.bexio.Room



import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters



@Database(entities = [Contact::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {



    abstract val contactDAO: ContactDAO



    // Vaultlists
    // abstract val categoryannonceDAO: CategoryAnnonceDAO



}