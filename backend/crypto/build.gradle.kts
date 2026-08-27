plugins {
    `java-library`
}

description = "Shared Argon2id and JWT-RSA library (not a runnable service)"

dependencies {
    api("org.bouncycastle:bcprov-jdk18on:1.80")
    api("org.bouncycastle:bcpkix-jdk18on:1.80")
    api("com.nimbusds:nimbus-jose-jwt:9.47")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}
