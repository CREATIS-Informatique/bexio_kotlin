package ch.creatis.bexio.Room



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query



@Dao
interface ActiviteDAO {



    @Query("DELETE FROM activites")
    fun delete()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Activite)



    @Query("SELECT * FROM activites ORDER BY name ASC")
    fun getItems(): List<Activite>



}



@Dao
interface ContactDAO {



    @Query("DELETE FROM contacts")
    fun delete()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Contact)



    @Query("SELECT * FROM contacts ORDER BY name_un ASC")
    fun getItems(): List<Contact>


    @Query("SELECT * FROM contacts WHERE idBexio == :idBexio")
    // Retourne une classe et non une liste de classe comme d'habitude
    fun getItemsByIdbexio(idBexio: Int): Contact



    // @Query("SELECT * FROM contacts WHERE type == :type ORDER BY nom")
    // fun getItemsByType(type: String): List<Contact>



}



@Dao
interface ProjetDAO {



    @Query("DELETE FROM projets")
    fun delete()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Projet)



    @Query("SELECT * FROM projets ORDER BY name DESC")
    fun getItems(): List<Projet>



    @Query("SELECT * FROM projets WHERE idBexio == :idBexio")
    // Retourne une classe et non une liste de classe comme d'habitude
    fun getItemsByIdbexio(idBexio: Int): Projet



    // @Query("SELECT * FROM contacts WHERE type == :type ORDER BY nom")
    // fun getItemsByType(type: String): List<Projet>



}



@Dao
interface TempsDAO {



    @Query("DELETE FROM temps")
    fun delete()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Temps)



    @Query("SELECT * FROM temps WHERE annee == :annee ORDER BY idBexio DESC ")
    fun getItems(annee: String): List<Temps>



    @Query("SELECT * FROM temps WHERE semaine == :numero")
    fun getTempsByNumeroSemaine(numero: String): List<Temps>



}



@Dao
interface SemaineDAO{



    @Query("DELETE FROM semaines")
    fun delete()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Semaines)



    @Query("SELECT * FROM semaines ORDER BY numeroSemaine DESC")
    fun getItems(): List<Semaines>



}



@Dao
interface TachesDAO {



    @Query("DELETE FROM taches")
    fun delete()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Tache)



    @Query("SELECT * FROM taches ORDER BY todo_status_id DESC")
    fun getItems(): List<Tache>



    // @Query("SELECT * FROM taches WHERE type == :type ORDER BY nom")
    // fun getItemsByType(type: String): List<Tache>



}



@Dao
interface USersDAO {



    @Query("DELETE FROM users")
    fun delete()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: User)



    @Query("SELECT * FROM users ORDER BY idBexio DESC")
    fun getItems(): List<User>


    @Query("SELECT * FROM users WHERE idBexio == :idBexio")
    // Retourne une classe et non une liste de classe comme d'habitude
    fun getItemsByIdbexio(idBexio: Int): User


    // @Query("SELECT * FROM taches WHERE type == :type ORDER BY nom")
    // fun getItemsByType(type: String): List<Tache>



}