package ch.creatis.bexio.Room



import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "activites")
data class Activite(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: String?, var name: String?, var default_is_billable: String?, var default_price_per_hour: String?, var account_id: String?) {



    // ----------- Search View ------------

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
data class Projet(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: String?, var nr: String?, var name: String?, var start_date: String?, var end_date: String?, var comment: String?, var pr_state_id: Int?, var pr_project_type_id: Int?, var contact_id: Int?, var contact_sub_id: Int?, var pr_invoice_type_id: Int?, var pr_invoice_type_amount: String?, var pr_budget_type_id:Int?, var pr_budget_type_amount: String?) {



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
data class Tache(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: Int?, var user_id:Int?, var finish_date:String?, var subject:String?, var place:Int?, var info:String?, var contact_id:Int?, var sub_contact_id:Int?, var project_id: Int?, var entry_id:Int?, var module_id:Int?, var todo_status_id:Int?, var todo_priority_id: Int?, var has_reminder: Boolean?, var remember_type_id: Int?, var remember_time_id:Int?, var communication_kind_id:Int?){



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
data class Temps(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: String?, var userId: Int?, var client_service_id: Int?, var text: String?, var pr_project_id: Int?, var date: String?, var duration: String?, var semaine: String?, var annee: String?) {



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



@Entity(tableName = "users")
data class User(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: Int?, var salutation_type: String?, var firstname: String?, var lastname: String?, var email: String?, var is_superadmin: Boolean?, var is_accountant: Boolean?) {



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