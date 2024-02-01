package id.passage.android

import MailosaurAPIClient
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import id.passage.android.IntegrationTestConfig.Companion.apiBaseUrl
import id.passage.android.IntegrationTestConfig.Companion.appId
import id.passage.android.IntegrationTestConfig.Companion.emailWaitTimeMilliseconds
import id.passage.android.IntegrationTestConfig.Companion.existingUserEmail
import id.passage.android.exceptions.NewLoginOneTimePasscodeInvalidIdentifierException
import id.passage.android.exceptions.NewRegisterOneTimePasscodeInvalidIdentifierException
import junit.framework.TestCase.fail
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class OneTimePasscodeTests {

    private lateinit var passage: Passage

    @Before
    fun setup() {
        activityRule?.scenario?.onActivity { activity ->
            activity?.let {
                passage = Passage(it, appId)
                passage.overrideBasePath(apiBaseUrl)
            }
        }
    }

    @After
    fun teardown() = runTest {
        passage.signOutCurrentUser()
    }

    @get:Rule
    var activityRule: ActivityScenarioRule<TestActivity?>? = ActivityScenarioRule(
        TestActivity::class.java
    )

    @Test
    fun testRegisterOTPValid() = runTest {
        val date = System.currentTimeMillis()
        val identifier = "authentigator+$date@passage.id"
        try {
            passage.newRegisterOneTimePasscode(identifier)
        } catch (e: Exception) {
            fail("Test failed due to unexpected exception: ${e.message}")
        }
    }

    @Test
    fun testRegisterOTPNotValid() = runTest {
        val identifier = "INVALID_IDENTIFIER"
        try {
            passage.newRegisterOneTimePasscode(identifier)
            fail("Test should throw NewRegisterOneTimePasscodeInvalidIdentifierException")
        } catch (e: Exception) {
            assertThat(e is NewRegisterOneTimePasscodeInvalidIdentifierException)
        }
    }

    @Test
    fun testActivateRegisterOTPValid() = runTest {
        runBlocking {
            val date = System.currentTimeMillis()
            val identifier = "authentigator+$date@${MailosaurAPIClient.serverId}.mailosaur.net"
            try {
                val otpId = passage.newRegisterOneTimePasscode(identifier).otpId
                delay(emailWaitTimeMilliseconds)
                val otp = MailosaurAPIClient.getMostRecentOneTimePasscode()
                passage.oneTimePasscodeActivate(otp, otpId)
            } catch (e: Exception) {
                fail("Test failed due to unexpected exception: ${e.message}")
            }
        }
    }

    @Test
    fun testLoginOTPValid() = runTest {
        val identifier = existingUserEmail
        try {
            passage.newLoginOneTimePasscode(identifier)
        } catch (e: Exception) {
            fail("Test failed due to unexpected exception: ${e.message}")
        }
    }

    @Test
    fun testLoginOTPNotValid() = runTest {
        val identifier = "INVALID_IDENTIFIER"
        try {
            passage.newLoginOneTimePasscode(identifier)
            fail("Test should throw NewLoginOneTimePasscodeInvalidIdentifierException")
        } catch (e: Exception) {
            assertThat(e is NewLoginOneTimePasscodeInvalidIdentifierException)
        }
    }

    @Test
    fun testActivateLoginOTPValid() = runTest {
        val identifier = existingUserEmail
        runBlocking {
            try {
                val otpId = passage.newLoginOneTimePasscode(identifier).otpId
                delay(emailWaitTimeMilliseconds)
                val otp = MailosaurAPIClient.getMostRecentOneTimePasscode()
                passage.oneTimePasscodeActivate(otp, otpId)
            } catch (e: Exception) {
                fail("Test failed due to unexpected exception: ${e.message}")
            }
        }
    }

}
