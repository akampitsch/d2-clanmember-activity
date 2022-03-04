package discord.listener

interface IListener {

    fun setListener(listener: IListener)

    fun notify(string: String)
}