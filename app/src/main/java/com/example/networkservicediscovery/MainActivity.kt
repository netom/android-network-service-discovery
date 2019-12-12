package com.example.networkservicediscovery

// See: https://developer.android.com/training/connect-devices-wirelessly/nsd.html#kotlin

import android.content.Context
import android.content.DialogInterface
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    //private val SERVICE_TYPE = "w0t?"
    private lateinit var nsdManager: NsdManager

    private val services = NetworkServiceList()

    inner class NetworkServiceListAdapter(private val services: NetworkServiceList) :
        RecyclerView.Adapter<NetworkServiceListAdapter.MyViewHolder>() {

        val TAG = "ADAPTER"

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        inner class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): NetworkServiceListAdapter.MyViewHolder {
            // create a new view
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false) as TextView

            textView.setOnClickListener { v ->
                AlertDialog.Builder(this@MainActivity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Blahblah.")
                    .setMessage("Open this web address?")
                    .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                    }
                    .setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                    }
                    .show()
            }

            return MyViewHolder(textView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.text = services[position].serviceName
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = services.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)

        viewManager = LinearLayoutManager(this)
        viewAdapter = NetworkServiceListAdapter(services)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        nsdManager = (getSystemService(Context.NSD_SERVICE) as NsdManager)

        nsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    // Instantiate a new DiscoveryListener
    private val discoveryListener = object : NsdManager.DiscoveryListener {
        private val TAG = "DISCOVERY_LISTENER"

        // Called as soon as service discovery begins.
        override fun onDiscoveryStarted(regType: String) {
            Log.d(TAG, "Service discovery started")
        }

        override fun onServiceFound(service: NsdServiceInfo) {
            Log.d(TAG, "Service discovery success $service")

            services.add(service)
            runOnUiThread { viewAdapter.notifyDataSetChanged() }

            /*nsdManager.resolveService(service, object : NsdManager.ResolveListener {
                override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                    Log.e(TAG, "Resolve failed: $errorCode")
                }
                override fun onServiceResolved(service: NsdServiceInfo) {
                    Log.e(TAG, "Resolve Succeeded. $service")
                    services.add(service)
                    runOnUiThread { viewAdapter.notifyDataSetChanged() }
                }
            })*/
        }

        override fun onServiceLost(service: NsdServiceInfo) {
            Log.e(TAG, "service lost: $service")
            services.remove(service)
            runOnUiThread { viewAdapter.notifyDataSetChanged() }
        }

        override fun onDiscoveryStopped(serviceType: String) {
            Log.i(TAG, "Discovery stopped: $serviceType")
        }

        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e(TAG, "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e(TAG, "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }
    }

}
