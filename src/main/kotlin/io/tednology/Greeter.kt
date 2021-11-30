package io.tednology

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result

class Greeter(val greeting: String) {
    fun greet() = println(greeting)
}

class HelloHttp : CliktCommand() {
    override fun run() {
        echo("Hello from Native CLI \uD83D\uDE43")

        val (request: Request, response: Response, result: Result<String, FuelError>) =
            Fuel.get("https://httpbin.org/get").responseString()

        when (result) {
            is Result.Success -> {
                echo("Status: ${response.statusCode}")
                echo("Headers: ")
                response.headers.entries.forEach { (key, values) ->
                    echo("$key: ${values.joinToString(",")}")
                }
                echo(result.value)
            }
            is Result.Failure -> result.error.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    Greeter("Hi there, I'd like to run this program.").greet()
    HelloHttp().main(args)
    Thread.sleep(1000L)
}
