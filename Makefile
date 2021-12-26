# Load in desired version of Java
gflag := -Dorg.gradle.java.home=~/openlogic-openjdk-8u262-b10-linux-32
gflag += --info

run:
	sudo gradle installDebug $(gflag)

comp:
	sudo gradle build $(gflag)

clean:
	rm -rf build .externalNativeBuild
