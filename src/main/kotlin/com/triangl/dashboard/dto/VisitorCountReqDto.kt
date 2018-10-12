package com.triangl.dashboard.dto

class VisitorCountReqDto (
    var customerId: String,

    var from: String,

    var to: String,

    var dataPointCount: Int
)