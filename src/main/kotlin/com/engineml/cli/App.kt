package com.engineml.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result.Failure
import com.github.kittinunf.result.Result.Success

class App {
    val greeting: String
        get() {
            return "Hello world."
        }
}

class Hello : CliktCommand() {
    override fun run() {
        echo("Hello from Engine Native \uD83D\uDE43")

        val (_, response, result) = Fuel.get("https://httpbin.org/get").responseString()
        when (result) {
            is Success -> {
                echo("Status: ${response.statusCode}")
                echo("Headers: ")
                response.headers.entries.forEach { (key, values) ->
                    echo("$key: ${values.joinToString(",")}")
                }
                echo(result.value)
            }
            is Failure -> result.error.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    Hello().main(args)
    Thread.sleep(5000L)
}
