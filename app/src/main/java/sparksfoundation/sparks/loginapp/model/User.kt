package sparksfoundation.sparks.loginapp.model

import android.os.Parcel
import android.os.Parcelable

data class User   ( val  profilepic:String?,
                     val username:String?, val user_birthday:String?, val user_gender:String?, val email:String?):Parcelable {
 constructor(parcel: Parcel) : this(
  parcel.readString(),
  parcel.readString(),
  parcel.readString(),
  parcel.readString(),
  parcel.readString()) {
 }

 override fun writeToParcel(parcel: Parcel, flags: Int) {
  parcel.writeString(profilepic)
  parcel.writeString(username)
  parcel.writeString(user_birthday)
  parcel.writeString(user_gender)
  parcel.writeString(email)
 }

 override fun describeContents(): Int {
  return 0
 }

 companion object CREATOR : Parcelable.Creator<User> {
  override fun createFromParcel(parcel: Parcel): User {
   return User(parcel)
  }

  override fun newArray(size: Int): Array<User?> {
   return arrayOfNulls(size)
  }
 }
}