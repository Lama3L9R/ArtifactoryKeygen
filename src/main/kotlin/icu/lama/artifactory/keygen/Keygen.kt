package icu.lama.artifactory.keygen

import org.jfrog.license.a.ObfuscatedString
import org.codehaus.jackson.JsonEncoding
import org.codehaus.jackson.JsonFactory
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.map.annotate.JsonSerialize
import org.jfrog.license.api.Product
import org.jfrog.license.exception.LicenseRuntimeException
import org.jfrog.license.multiplatform.License
import org.jfrog.license.multiplatform.SignedLicense
import org.jfrog.license.multiplatform.SignedProduct
import org.jfrog.security.util.BCProviderFactory
import org.yaml.snakeyaml.Yaml
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.StringWriter
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.Signature
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


fun main(vararg args: String) {
    println("""
        ALERT!! YOU ARE NOT ALLOWED TO CRACK / ILLEGALLY USE ANY COMMERCIAL SOFTWARE BY JFROG WITH THIS TOOL
        ALERT!! YOU ARE NOT ALLOWED TO CRACK / ILLEGALLY USE ANY COMMERCIAL SOFTWARE BY JFROG WITH THIS TOOL
        ALERT!! YOU ARE NOT ALLOWED TO CRACK / ILLEGALLY USE ANY COMMERCIAL SOFTWARE BY JFROG WITH THIS TOOL
        
        THIS PROJECT IS FOR EDUCATIONAL PURPOSES! YOU SHOULD DELETE ALL THE DOWNLOADED FILES WITHIN 24HOURS
                THE CONSEQUENCES CAUSED BY THE USE OF THIS SOFTWARE SHALL BE BORNE BY THE USER
        
        ALERT!! YOU ARE NOT ALLOWED TO CRACK / ILLEGALLY USE ANY COMMERCIAL SOFTWARE BY JFROG WITH THIS TOOL
        ALERT!! YOU ARE NOT ALLOWED TO CRACK / ILLEGALLY USE ANY COMMERCIAL SOFTWARE BY JFROG WITH THIS TOOL
        ALERT!! YOU ARE NOT ALLOWED TO CRACK / ILLEGALLY USE ANY COMMERCIAL SOFTWARE BY JFROG WITH THIS TOOL
        
        < Artifactory Keygen By lamadaemon | For help please use 'help' sub-command >
        
    """.trimIndent())
    val parameters = args.toMutableList()

    if (args.isEmpty()) {
        parameters += prompt("Please enter a sub-command: ").split(" ")
    }

    when (parameters.removeAt(0)) {
        "obf" -> {
            println(ObfuscatedString.createObfuscation(parameters.joinToString(" ") { it }))
        }

        "pub" -> {
            println(Constants.PUBLIC_KEY)
        }

        "pri" -> {
            println(Constants.PRIVATE_KEY)
        }

        "genkey" -> {
            val keygen = KeyPairGenerator.getInstance("RSA", BCProviderFactory.getProvider())
            keygen.initialize(4096)
            val kp = keygen.generateKeyPair()
            println("PrivateKey: " + Base64.getEncoder().encodeToString(kp.private.encoded))
            println("PublicKey: " + Base64.getEncoder().encodeToString(kp.public.encoded))
        }

        "gen" -> {
            val private = KeyFactory.getInstance("RSA", BCProviderFactory.getProvider()).generatePrivate(PKCS8EncodedKeySpec(Base64.getDecoder().decode(Constants.PRIVATE_KEY)))
            val sign = Signature.getInstance("SHA256withRSA", BCProviderFactory.getProvider())

            val products = mutableMapOf<String, SignedProduct>()

            while (true) {
                val product = Product()
                product.id = prompt("Enter product id(artifactory): ", "artifactory")
                product.expires = date(2099, 12, 31)
                product.isTrial = false
                product.owner = prompt("Owner(lamadaemon): ", "lamadaemon")
                product.validFrom = Date()
                product.type = Product.Type.ENTERPRISE_PLUS

                products += product.id to SignedProduct(product, private, sign)

                if (prompt("\nDo you want add more products into this license(yes/no, default=no): ", listOf("yes", "no"), "no") == "no") {
                    break
                }
            }

            val license = License()
            license.version = 2
            license.validateOnline = false
            license.products = products

            val sLicense = SignedLicense(license, private, sign)

            println("Your license: (DON'T COPY THIS LINE)\n")
            println(chunkString(createFinalLicense(sLicense)))
            // println(createFinalLicense(sLicense))
            println()
        }

        "verify" -> {
            if (parameters.isEmpty()) {
                return println("input key is missing")
            }
            runCatching {
                val a = org.jfrog.license.api.a()
                a.b(parameters[0], Constants.PUBLIC_KEY)
            }.onFailure {
                it.printStackTrace()
                println("Invalid key")
            }.onSuccess {
                println(Yaml().dump(it))
            }
        }

        "enc" -> {
            if (parameters.isEmpty()) {
                return println("input file(artifactory-addons-manager) is missing")
            }
            println("WARNING! You are using a untested feature!")

            val keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAnd3-KeyTripleDES-CBC", BCProviderFactory.getProvider())
            val cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", BCProviderFactory.getProvider())
            cipher.init(1, keyFactory.generateSecret(PBEKeySpec("just!for@fun#".toCharArray())), PBEParameterSpec(byteArrayOf(-54, -2, -70, -66, -21, -85, -17, -84), 20))
            println(Base64.getEncoder().encodeToString(cipher.update(Base64.getDecoder().decode(parameters[0]))))
        }

        "mkconfig" -> {
            val key = prompt("Please enter public key: ")
            if (key.isBlank()) {
                println("Key must not be null or blank!")
            }

            try {
                val x509Key = X509EncodedKeySpec(Base64.getDecoder().decode(key))
                val parsedKey = KeyFactory.getInstance("RSA", BCProviderFactory.getProvider()).generatePublic(x509Key) as RSAPublicKey
                if (parsedKey.modulus.bitLength() < 4096) {
                    throw Exception("Key is too short")
                }
            } catch (err: Exception) {
                err.printStackTrace()
                println("Illegal key! Please make sure your key is a standard RSA public key in X509 format and have at least 4096 bits long")
            }

            println("Key validation success! Your config data: (DON'T COPY THIS LINE)\n")

            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
            val root = doc.createElement("config")

            doc.appendChild(root)
            root.appendChild(doc.createElement("publicKey").appendChild(doc.createTextNode(key)))

            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
            val writer = StringWriter()
            transformer.transform(DOMSource(doc), StreamResult(writer))

            println(writer.buffer.toString().toByteArray().toHex().uppercase())
        }

        "verifyAgent" -> {
            val keyField = org.jfrog.license.api.a::class.java.getDeclaredField("d")
            keyField.trySetAccessible()
            val keyFound = keyField.get(null)
            if (keyFound == "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmquSyL1wSs6DvU5iceNvbfha4MPX040iHFuoBTeEZA43rsHWLNLIECmVJ3Zv1xV9+NkrORKQEuJckEXzbTHSrpzZDfF/sjlKhalHKN3joNgIoIFG5MoM9kPFEB0mAJx/Hpiojj+/LZ5uTHIWiEGKm6C4EQQL9F+2FpcQbj6ve26t03YNZVIhzgmGw4TEb/1WXje7ywtMv3bGkKqAak6VoTKnn4MOm3ULzck9K+KPIgOd01Wa00PPbVqitB7Tqej7y3wZKMPxgzM0n7fouH7lu0yowiV+V5SyO/6g/Wq+DNgniKpnbcFsVLxFE7LcyHr94cAorBH+EUcepGKqqml4cQIDAQAB") {
                println("Validation Failed!")
            } else {
                println("Validation Success! New key is: $keyFound")
            }
        }

        "help" -> {
            println("""
                List of all sub-commands:
                    obf <text>:
                        Obfuscate text with JFrog's 'ObfuscatedString' class
                    pub:
                        Get the current public key (RSA)
                    pri:
                        Get the current private key (RSA)
                    genkey:
                        Generate a key pair
                    gen:
                        Generate a license with the current private key
                    verify <license>:
                        Verify a license with the current public key
                    enc: [ NOT TESTED ]
                        Encrypt a license (I guess this is used for the license of the old version)
                    verifyAgent:
                        You can verify ArtifactoryAgent by attaching the agent to ArtifactoryKeygen
                    mkconfig:
                        Create agent config but friendly!
                To inject into artifactory
                    Open Artifactory's tomcat folder and add the following JVM options to tomcat.
                    In case you don't know how to pass extra JVM args you can see the README of this project
                    OR Google it by yourself!
                    
                    -javaagent:</path/to/this/jar/file>
                    
                    This will patch class 'org.jfrog.license.api.a'
                    
            """.trimIndent())
        }

        else -> {
            println("Nothing todo. Use 'help' subcommand to see all available subcommands.")
        }
    }
}


fun prompt(msg: String, default: String? = null): String {
    print(msg)
    return readlnOrNull().orEmpty().ifEmpty { default ?: "" }
}

fun prompt(msg: String, options: List<String>, default: String? = null): String {
    val rd = prompt(msg, default).lowercase()
    return if (rd in options) {
        rd
    } else {
        println("Illegal input! Please enter one of following options only: " + options.joinToString())
        prompt(msg, options, default)
    }
}

fun date(yrs: Int, mo: Int, dys: Int): Date {
    val cal = Calendar.getInstance()
    cal.set(yrs, mo, dys)
    return cal.time
}

fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

fun createFinalLicense(obj: Any): String {
    val bos = ByteArrayOutputStream()
    try {
        val factory = JsonFactory()
        val generator = factory.createJsonGenerator(bos, JsonEncoding.UTF8)
        val objectMapper = ObjectMapper(factory)
        objectMapper.serializationConfig.serializationInclusion = JsonSerialize.Inclusion.NON_NULL
        generator.setCodec(objectMapper)
        generator.writeObject(obj)
    } catch (e: IOException) {
        throw LicenseRuntimeException("Failed to serialize license", e)
    }
    return Base64.getEncoder().encodeToString(bos.toByteArray())
}

fun chunkString(str: String, chunkLength: Int = 76) = str.chunked(chunkLength).joinToString("\n")