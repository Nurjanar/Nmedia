package ru.netology.nmedia

class Count {
    fun numberCheck(count: Int): String {
        var result = ""
        var value = 0.0

        if (count in 0..999) {
            result = count.toString()
        }
        if (count in 1000..9999) {
            value = count / 1000.0
            result = formatDouble(value) + "K"
        }
        if (count in 10000..999999) {
            result = (count / 1000).toString() + "K"
        }
        if (count in 1000000..99999999) {
            value = count / 1000000.0
            result = formatDouble(value) + "M"
        }
        if (count in 100000000..999999999) {
            result = (count / 1000000).toString() + "M"
        }
        return result
    }

    private fun formatDouble(value: Double): String {
        if (value % 1.0 == 0.0) {
            return value.toInt().toString()
        } else {
            val decimalPart = (value * 10).toInt() % 10
            return "${value.toInt()}.$decimalPart"
        }
    }
}