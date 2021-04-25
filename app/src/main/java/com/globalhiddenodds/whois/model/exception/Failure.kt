package com.globalhiddenodds.whois.model.exception

sealed class Failure {
    class NetworkConnection: Failure()
    class ServerError: Failure()
    class DatabaseError: Failure()
    abstract class FeatureFailure: Failure()

}