package ch.creatis.bexio.Room



import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters



@Database(entities = [Contact::class, Projet::class, Semaines::class, Tache::class,Temps::class], version = 21)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {



    abstract val contactDAO: ContactDAO
    abstract val projetDAO: ProjetDAO
    abstract val semaineDAO: SemaineDAO
    abstract val tacheDAO: TachesDAO
    abstract val tempsDAO: TempsDAO



}