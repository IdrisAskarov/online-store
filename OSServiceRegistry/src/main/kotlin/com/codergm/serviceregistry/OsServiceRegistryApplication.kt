package com.codergm.serviceregistry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class OsServiceRegistryApplication

fun main(args: Array<String>) {
	runApplication<OsServiceRegistryApplication>(*args)
}
