plugins {
	id("com.playmonumenta.papermixins.mod-conventions")
}

group = "kim.biryeong"
version = "1.0.1"

dependencies {

}

// Tasks configuration
val targetJavaVersion = 21

tasks {
	processResources {
		inputs.properties("version" to version.toString())

		filesMatching("fabric.mod.json") {
			expand(
				mapOf(
					"version" to version.toString()
				)
			)
		}
	}
}
