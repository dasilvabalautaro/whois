package com.globalhiddenodds.whois.model.persistent.network

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.globalhiddenodds.whois.model.persistent.network.services.JobLocationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManagerLocation @Inject constructor(private val context: Context) {
    private var jobScheduler: JobScheduler? = null
    private var jobInfo: JobInfo? = null
    private val jobId = 1337

    init {
        jobInfo = JobInfo.Builder(jobId, ComponentName(context,
                JobLocationService::class.java))
                .setPeriodic((15 * 60 * 1000).toLong()) //, (15 * 60 * 1000).toLong()
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NOT_ROAMING)
                .build() //15 min

    }

    fun start(): Boolean{
        jobScheduler = context.getSystemService(Context
            .JOB_SCHEDULER_SERVICE) as JobScheduler
        return when {
            jobScheduler!!.schedule(jobInfo!!) <= 0 -> false
            else -> true
        }

    }

    fun cancel(){
        if (jobScheduler != null){
            jobScheduler!!.cancelAll()
        }

    }

    fun isJobServiceOn(): Boolean {
        val scheduler = context.getSystemService(Context
            .JOB_SCHEDULER_SERVICE) as JobScheduler

        var hasBeenScheduled = false

        for (jobInfo in scheduler.allPendingJobs) {
            if (jobInfo.id == jobId) {
                hasBeenScheduled = true
                break
            }
        }

        return hasBeenScheduled
    }

}