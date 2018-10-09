package com.triangl.dashboard.dto

class VisitorCountReq (
    var customerId: String,

    var from: String,

    var to: String,

    var dataPointCount: Int
)