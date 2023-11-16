package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchReferralInitListener
import io.branch.referral.BranchError
import io.branch.referral.SharingHelper
import io.branch.referral.util.LinkProperties
import io.branch.referral.util.ShareSheetStyle
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)


        // Handle incoming deep links
        val intent: Intent = intent
        val deepLinkUri: Uri? = intent.data

        if (deepLinkUri != null) {
            // Handle the deep link, extract information, and navigate accordingly
            val path: String? = deepLinkUri.path

            // Handle different paths in the deep link
            when (path) {
                "/page1" -> {
                    // Handle deep link for "/page1"
                    // You can implement specific logic or just launch LauncherActivity
                    startActivity(Intent(this, LauncherActivity::class.java))
                    finish()
                }

                "/page2" -> {
                    //  Handle specific deep link paths
                    // You can implement specific logic or just launch LauncherActivity
                    startActivity(Intent(this, LauncherActivity::class.java))
                    finish()
                }
                // Add more cases for other paths as needed
                else -> {
                    // Handle unknown paths or show a default screen
                    // You can implement specific logic or just launch LauncherActivity
                    startActivity(Intent(this, LauncherActivity::class.java))
                    finish()
                }
            }

            // Log the path for debugging
            Log.d("BRANCH SDK", "Path: $path")
        }

        // Call the new functions for deep linking
        createAndShareBranchLink()
        readDeepLinkData()
    }

    // Existing functions...

    //Function to create and share a Branch link
    private fun createAndShareBranchLink() {
        val buo = createBranchUniversalObject()
        val lp = createLinkProperties()

        // Generate a short Branch link and share it
        buo.generateShortUrl(this, lp, Branch.BranchLinkCreateListener() { url, error ->
            if (error == null) {
                Log.i("BRANCH SDK", "got my Branch link to share: $url")
                shareBranchLink(url)
            }
        })
    }


    // Function to share a Branch link
    private fun shareBranchLink(url: String?) {
        val buo = createBranchUniversalObject()
        val lp = createLinkProperties()
        val ss = createShareSheetStyle()

        buo.showShareSheet(this, lp, ss, object : Branch.BranchLinkShareListener {
            override fun onShareLinkDialogLaunched() {}
            override fun onShareLinkDialogDismissed() {}
            override fun onLinkShareResponse(sharedLink: String?, sharedChannel: String?, error: BranchError?) {}
            override fun onChannelSelected(channelName: String) {}
        })
    }

    // Function to read deep link data
    private fun readDeepLinkData() {
        Branch.sessionBuilder(this).withCallback(object : BranchReferralInitListener {
            override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
                if (error == null) {
                    Log.i("BRANCH SDK", referringParams.toString())
                } else {
                    Log.e("BRANCH SDK", error.message ?: "Error is null, but message is empty")
                }
            }
        }).withData(intent.data).init()

        // Retrieve the latest and first referring params
        val sessionParams = Branch.getInstance().latestReferringParams

        // first
        val installParams = Branch.getInstance().firstReferringParams
    }

    // Existing functions...

    // New functions for creating deep link components
    private fun createBranchUniversalObject(): BranchUniversalObject {
        return BranchUniversalObject()
            .setCanonicalIdentifier("content/12345")
            .setTitle("My Content Title")
            .setContentDescription("My Content Description")
            .setContentImageUrl("https://lorempixel.com/400/400")
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
    }

    private fun createLinkProperties(): LinkProperties {
        return LinkProperties()
            .setChannel("facebook")
            .setFeature("sharing")
            .setCampaign("content 123 launch")
            .setStage("new user")
            .addControlParameter("desktop_url", "http://example.com/home")
            .addControlParameter("custom", "data")
            .addControlParameter("custom_random", Calendar.getInstance().timeInMillis.toString())
    }

    private fun createShareSheetStyle(): ShareSheetStyle {
        return ShareSheetStyle(this@MainActivity, "Check this out!", "This stuff is awesome: ")
            .setCopyUrlStyle(
                resources.getDrawable(android.R.drawable.ic_menu_send),
                "Copy",
                "Added to clipboard"
            )
            .setMoreOptionStyle(
                resources.getDrawable(android.R.drawable.ic_menu_search),
                "Show more"
            )
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
            .setAsFullWidthStyle(true)
            .setSharingTitle("Share With")
    }

    private fun openDefaultUrl(url: String) {
        // Create an Intent with ACTION_VIEW and the URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        // Start the activity using the created Intent
        startActivity(intent)
    }
}
