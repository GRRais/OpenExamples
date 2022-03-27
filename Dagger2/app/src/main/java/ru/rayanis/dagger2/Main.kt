package ru.rayanis.dagger2

fun main() {
    val appComponent: AppComponent = DaggerAppComponent.create()
    print(appComponent.computer)
}