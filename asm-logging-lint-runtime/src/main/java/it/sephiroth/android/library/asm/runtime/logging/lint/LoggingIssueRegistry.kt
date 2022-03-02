package it.sephiroth.android.library.asm.runtime.logging.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.google.auto.service.AutoService

@Suppress("UnstableApiUsage", "unused")
@AutoService(value = [IssueRegistry::class])
class LoggingIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = WrongLoggingUsageDetector.issues.asList()

    override val api: Int
        get() = CURRENT_API

    /**
     * works with Studio 4.0 or later; see
     * [com.android.tools.lint.detector.api.describeApi]
     */
    override val minApi: Int
        get() = 7

    override val vendor = Vendor(
        vendorName = "sephiroth74/asm-logging",
        identifier = "it.sephiroth.android.library.asm:asm-logging-runtime:{version}",
        feedbackUrl = "https://github.com/sephiroth74/AndroidDebugLog/issues",
    )
}
