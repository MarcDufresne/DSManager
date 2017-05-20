package net.imatruck.dsmanager.models


class AuthLoginBase : BaseModel() {
    val data: AuthLoginData? = null

    val sid: String?
        get() = this.data!!.sid
}
