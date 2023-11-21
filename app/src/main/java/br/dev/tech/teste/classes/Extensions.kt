package br.dev.tech.teste.classes

class Extensions {
    object Log {
        fun d(TAG: String?, message: String) {
            val maxLogSize = 2000
            for (i in 0..message.length / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = if (end > message.length) message.length else end
                android.util.Log.d(TAG, message.substring(start, end))
            }
        }
    }
}