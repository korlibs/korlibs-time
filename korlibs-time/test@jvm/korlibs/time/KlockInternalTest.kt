package korlibs.time

import korlibs.time.core.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.*

@OptIn(CoreTimeInternalApi::class)
class KlockInternalTest {
    @Test
    fun testThatNowLocalHasTimezoneIntoAccount() {
		TestUnsafeSetTemporalCoreTime(object : ICoreTime by CoreTime {
			override fun currentTimeMillisDouble(): Double = 1561403754469L.toDouble()
			override fun localTimezoneOffset(time: Long): Duration = 2.hours
		}) {
			assertEquals("Mon, 24 Jun 2019 19:15:54 UTC", DateTime.now().toStringDefault())
			assertEquals("Mon, 24 Jun 2019 21:15:54 GMT+0200", DateTime.nowLocal().toStringDefault())
		}
    }

	@Test
	fun testBug38() {
		val fixedUtcTime = 1555326870000L
		TestUnsafeSetTemporalCoreTime(object : ICoreTime by CoreTime {
			override fun currentTimeMillisDouble(): Double = fixedUtcTime.toDouble()
			override fun localTimezoneOffset(time: Long): Duration = (+9).hours
		}) {
			assertEquals("Mon, 15 Apr 2019 11:14:30 UTC", DateTime.now().toStringDefault())
			assertEquals("Mon, 15 Apr 2019 20:14:30 GMT+0900", DateTime.nowLocal().toStringDefault())
			assertEquals("1555326870000", DateTime.nowUnixMillisLong().toString())
			assertEquals("Mon, 15 Apr 2019 20:14:30 GMT+0900", DateTimeTz.nowLocal().toStringDefault())
			//println(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(Date(fixedUtcTime)))
		}

		//println(DateTime.now())
		//println(DateTime.nowLocal())
		//println(DateTime.nowUnixLong())
		//println(DateTimeTz.nowLocal())
		//println(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(Calendar.getInstance().time))
	}
}
