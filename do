#!/bin/bash

APP_NAME=Slippy
BIN="$APP_NAME".jar

# Build application
build()
{
	ant compile jar
}

# Cleans all created binary files
clean()
{
	ant clean
}

# Launches application from created jar file
jar()
{
	make && java -jar bin/jar/$BIN
}

# Distributes application
#
# @param $1 - plattform
dist()
{
	clean && build

	case $1 in
		linux)
			dist_linux
			;;
		windows)
			dist_windows
			;;
	esac
}

# Distributes application for linux
dist_linux()
{
	DIST_DIR=dist/linux/$APP_NAME
	SCRIPT=$DIST_DIR/"$APP_NAME".sh

	create_dist_dir $DIST_DIR

	if [ ! -f "$SCRIPT" ]; then
		echo "#!/bin/bash
java -jar $BIN" >> $SCRIPT
	fi

	chmod a+x $SCRIPT

	cd dist/linux/ && tar -cvf "$APP_NAME".tar $APP_NAME
}

# Distributes application for windows
dist_windows()
{
	DIST_DIR=dist/windows/$APP_NAME
	SCRIPT=$DIST_DIR/"$APP_NAME".bat

	create_dist_dir $DIST_DIR

	if [ ! -f "$SCRIPT" ]; then
		echo "java -jar $BIN" >> $SCRIPT
	fi

	chmod a+x $SCRIPT

	cd dist/windows && zip -r "$APP_NAME".zip $APP_NAME
}

# Creates basic distribution directory structure
#
# @param $1 - directory name
create_dist_dir()
{
	if [ ! -d "$1" ]; then
		mkdir $1
	fi

	cp bin/jar/$BIN $1
}

# Run
#
# @param ... - command to execute
run()
{
	case $1 in
		clean)
			clean
			;;
		build)
			build
			;;
		dist)
			dist $2
			;;
		*|jar)
			jar
			;;
	esac
}

[ $0 == $BASH_SOURCE ] && run $@
