#!/bin/sh

#  Copyright 2006 the original author or authors.
#
#  Licensed under the x.matthew License, Version 1.0 (the "License");
#  you may not use this file except in compliance with the License.
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.


# get current parent directory
# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# Only set CATALINA_HOME if not already set
[ -z "$CURRENT_DIR" ] && CURRENT_DIR=`cd "$PRGDIR/.." ; pwd`

echo $CURRENT_DIR

_JAVA_CMD=
if [ -z "${JAVA_HOME}" ]; then
    _JAVA_CMD=/System/Library/Frameworks/JavaVM.framework/Home
else
    _JAVA_CMD=${JAVA_HOME}/bin/java
fi

JAVA_OPTS="-Xms256m -Xmx256m"

if [ -z "${SUNJMX}" ]; then
	 SUNJMX="-Dcom.sun.management.jmxremote"
SUNJMX="${SUNJMX} -Dcom.sun.management.jmxremote.port=1616 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
fi

"${_JAVA_CMD}" -Dspy2servers.base="${PRGDIR}" ${JAVA_OPTS} ${SUNJMX} -Djava.ext.dirs="${CURRENT_DIR}/lib" -cp "${CURRENT_DIR}/classes:.:${CURRENT_DIR}/conf" org.xmatthew.spy2servers.console.Main start

JAVA_OPTS=
_JAVA_CMD=
