package com.example.networkservicediscovery

import android.net.nsd.NsdServiceInfo

class NetworkServiceList {
    private val services = (ArrayList<NsdServiceInfo>())

    operator fun get(i: Int): NsdServiceInfo {
        return services[i]
    }

    fun add(service: NsdServiceInfo) {
        services.add(service)
    }

    fun remove(service: NsdServiceInfo) {
        for ((i,s) in services.withIndex()) {
            if (s.serviceName == service.serviceName) {
                services.removeAt(i)
                break
            }
        }
    }

    val size: Int
        get() = services.size
}