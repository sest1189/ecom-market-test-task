#!/usr/bin/env sh

JAVA_OPTIONS="${JAVA_OPTIONS} -Dspring.config.location=file:./resources/application.properties"
JAVA_OPTIONS="${JAVA_OPTIONS} -Xms${HEAP_XMS:-256m}"
JAVA_OPTIONS="${JAVA_OPTIONS} -Xmx${HEAP_XMX:-512m}"

java ${JAVA_OPTIONS} -XX:MaxRAM=${MAX_RAM:-2048m} -cp "./classes/:./ecom-market-test-task-0.0.1-SNAPSHOT/WEB-INF/lib/*" org.ecom.market.test.task.ecommarkettesttask.EcomMarketTestTaskApplication