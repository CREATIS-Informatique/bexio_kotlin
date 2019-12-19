package ch.creatis.bexio.Room



import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "contacts")
data class Contact(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: String?, var name_un: String?, var name_deux: String?, var address: String?, var postcode: String?, var city: String?, var mail: String?, var phone_fixed: String?) {



    // ----------- Search View ------------

    //    fun getNomFiltered(): String? {
    //        return nom
    //    }
    //
    //    fun getPrenomFiltered(): String? {
    //        return prenom
    //    }
    //
    //    fun getServiceFiltered(): String? {
    //        return service
    //    }
    //
    //    fun getEmplacementFiltered(): String? {
    //        return emplacement
    //    }

    // ------------------------------------



}