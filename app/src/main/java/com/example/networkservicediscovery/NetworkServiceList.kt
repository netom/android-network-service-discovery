package com.example.networkservicediscovery

import android.net.nsd.NsdServiceInfo

class NetworkServiceList {
    private val services = (ArrayList<NsdServiceInfo>())

    fun get(i: Int): NsdServiceInfo {
        return services[i]
    }

    fun set(i: Int, service: NsdServiceInfo) {
        services[i] = service;
    }

    fun add(service: NsdServiceInfo) {

    }

    fun size(): Int {
        return services.size
    }
}