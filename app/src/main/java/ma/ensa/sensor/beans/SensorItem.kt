package ma.ensa.sensor.beans

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
data class SensorItem(
    val id: String,
    val name: String,
    val type: String,
    val vendor: String,
    val version: String,
    val resolution: String,
    val power: String,
    val maxRange: String,
    val intType: String,
    val maxDataRate: String
) {
    override fun toString(): String {
        return name
    }
}



