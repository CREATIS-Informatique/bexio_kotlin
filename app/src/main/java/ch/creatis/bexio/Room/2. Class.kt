package ch.creatis.bexio.Room



import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "contacts")
data class Contact(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: String?, var name_un: String?, var name_deux: String?, var address: String?, var postcode: String?, var city: String?,var country_id: String?, var mail: String?,var mail_second: String?, var phone_fixed: String?, var phone_fixed_second: String?, var phone_mobile: String?, var fax: String?, var url: String?, var skype_name: String?) {



    // ----------- Search View ------------

        fun getNomFiltered(): String? {
            return name_un
        }

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



@Entity(tableName = "projets")
data class Projet(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: String?, var nr: String?, var name: String?, var start_date: String?, var end_date: String?, var pr_state_id: String?, var comment: String?) {



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




@Entity(tableName = "temps")
data class Temps(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: String?, var date: String?, var duration: String?, var semaine: String?) {



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



@Entity(tableName = "semaines")
data class Semaines(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var numeroSemaine: String?, var dateDebut: String?, var dateFin: String?, var heuresTotales: String?) {



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



@Entity(tableName = "taches")
data class Tache(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: String?, var subject: String?, var status: String?) {



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