package ch.creatis.bexio.Room



import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "contacts")
data class Contact(@PrimaryKey(autoGenerate = true) var idRoom: Int?, var idBexio: String?, var name: String?, var address: String?, var postcode: String?, var city: String?, var mail: String?, var phone_fixed: String?):
    Parcelable {



    constructor(parcel: Parcel) : this(
        null,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )



    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(idBexio)
        dest?.writeString(name)
        dest?.writeString(address)
        dest?.writeString(postcode)
        dest?.writeString(city)
        dest?.writeString(mail)
        dest?.writeString(phone_fixed)
    }



    override fun describeContents(): Int {
        throw NotImplementedError()
    }



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



    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }



        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }



}