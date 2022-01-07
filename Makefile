GRADLE=sudo sh gradlew

test: local.properties
	$(GRADLE) installDebug

build: local.properties
	$(GRADLE) assembleDebug

clean:
	sudo rm -rf release build app/release app/build .gradle .idea

local.properties:
	echo "sdk.dir=/usr/lib/android-sdk/" > local.properties
	echo "ndk.dir=/usr/lib/android-ndk/" >> local.properties

.PHONY: clean build test
