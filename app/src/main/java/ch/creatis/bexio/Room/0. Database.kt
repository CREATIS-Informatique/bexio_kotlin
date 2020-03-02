package ch.creatis.bexio.Room



import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters



@Database(entities = [Activite::class, Contact::class, Projet::class, Semaines::class, Tache::class,Temps::class,User::class], version = 24)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {



    abstract val activiteDAO: ActiviteDAO
    abstract val contactDAO: ContactDAO
    abstract val projetDAO: ProjetDAO
    abstract val semaineDAO: SemaineDAO
    abstract val tacheDAO: TachesDAO
    abstract val tempsDAO: TempsDAO
    abstract val userDAO: USersDAO



}