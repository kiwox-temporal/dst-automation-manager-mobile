#!/bin/bash

APP_NAME="dst_manager"
APP_VERSION="0.0.1-SNAPSHOT"

MAX_ATTEMPTS=30

ROOT_PATH=`pwd`

JAVA_BIN=java
JAVA_SPECS="-Xms256m -Xmx512m"

TIMEZONE="America/Santiago"

export CONF_PATH=${ROOT_PATH}/conf

SPRING_CONFIG_LOCATION=file:${CONF_PATH}/application.properties
LOGGING_CONFIG=${CONF_PATH}/logback.xml

JAR_FILE=${ROOT_PATH}/target/${APP_NAME}-${APP_VERSION}.jar

RES_COL=60

#===================================================================================================

echo_success() {
	echo -en "\\033[${RES_COL}G"
	echo -n  "["
	echo -en "\\033[1;32m"
	echo -n  $"  OK  "
	echo -en "\\033[0;39m"
	echo -n  "]"
	echo -ne "\r"

	return 0
}

echo_failed() {
	echo -en "\\033[${RES_COL}G"
	echo -n  "["
	echo -en "\\033[1;31m"
	echo -n  $"FAILED"
	echo -en "\\033[0;39m"
	echo -n  "]"
	echo -ne "\r"

	return 1
}

processes() {
	PROCESSES=(`ps -wfea | grep -i $JAR_FILE | egrep -v "grep|vi" | wc -l`)
}

start() {
	processes

	if [ $PROCESSES -gt 0 ]; then
		echo -n "Application $APP_NAME is running"

		echo_failed
		echo

		return
	fi

	EXEC_CMD="$JAVA_BIN $JAVA_SPECS -Duser.timezone=$TIMEZONE -Dspring.config.location=$SPRING_CONFIG_LOCATION -Dlogging.config=$LOGGING_CONFIG -jar $JAR_FILE"
	echo "Executing: $EXEC_CMD"
	`$EXEC_CMD > /dev/null 2>&1 &`

	echo -n "Starting $APP_NAME"

	sleep 2

	echo_success
	echo
}

stop() {
	processes

	if [ $PROCESSES -eq 0 ]; then
		echo -n "Application $APP_NAME is not running"

		echo_failed
		echo

		return
	fi

	`ps -wfea | grep -i $JAR_FILE | egrep -v "grep|vi" | awk '{printf("%s\n",$2)}' | xargs "kill"`
	echo -n "Stopping $APP_NAME"

	ATTEMPTS=0
	while [ $PROCESSES -gt 0 ]
	do
		if [ $ATTEMPTS -eq $MAX_ATTEMPTS ]; then
			`ps -wfea | grep -i $JAR_FILE | egrep -v "grep|vi" | awk '{printf("%s\n",$2)}' | xargs "kill" -KILL`
		fi

		sleep 2

		processes
		ATTEMPTS=$((ATTEMPTS + 1))

		echo -n "."
	done

	echo_success
	echo
}

restart() {
	stop

	sleep 1

	start
}

status() {
	processes

	if [ $PROCESSES -eq 0 ]; then
		echo "Application $APP_NAME is not running"

		return
	fi

	PID=(`ps -wfea | grep -i $JAR_FILE | egrep -v "grep|vi" | awk '{printf("%s",$2)}'`)
	echo "Application $APP_NAME ($PID) is running"
}

package() {
	processes

	if [ $PROCESSES -gt 0 ]; then
		stop

		sleep 1
	fi

	mvn clean package

	sleep 1

	start
}

case "$1" in
	start)
		start
		;;
	stop)
		stop
		;;
	restart)
		restart
		;;
	status)
		status
		;;
	package)
		package
		;;
	*)
		echo $"Usage: $0 { start | stop | restart | status | package }"
		exit 1
esac

exit $?
