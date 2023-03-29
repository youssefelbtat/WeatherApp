package com.example.weatherapp.helper

enum class Language (val value: String){
        ENGLISH("en"),
        ARABIC("ar")
}

enum class Units {
        CELSIUS,
        FAHRENHEIT,
        KELVIN
}

enum class NotificationEnum {
        ENABLE,
        DISABLE,
}

enum class LocationEnum {
        GPS,
        MAP,
}

enum class AlertType{
        NOTIFICATION,
        ALARM
}


