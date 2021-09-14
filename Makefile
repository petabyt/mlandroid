# Gradle 8 probably needed to compile correctly
gflag := -Dorg.gradle.java.home=/usr/lib/jvm/java-8-openjdk-amd64

gflag += --info

run:
	sudo gradle installDebug $(gflag)

comp:
	sudo gradle build $(gflag)

adb:
	adb logcat --pid=`adb shell pidof -s fm.magiclantern.app`

clean:
	rm -rf build .externalNativeBuild