package labourtime

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AdminWorkflow extends Simulation {

	val httpProtocol = http
		.baseURL("http://labour.local")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:44.0) Gecko/20100101 Firefox/44.0")
	val scn = scenario("AdminWorkflow")
		.exec(http("[INDEX]")
			.get("/"))
		.pause(23)
		.exec(http("[LOGIN]")
			.post("/login/")
			.formParam("email", "admin@mailinator.com")
			.formParam("password", "qwerty"))
		.pause(29)
		.exec(http("[FILTER_YEAR_2015]")
			.get("/?year=2015&month=&user=&status=&kind="))
		.pause(6)
		.exec(http("[FILTER_MONTH_JAN]")
			.get("/?year=2015&month=1&user=&status=&kind="))
		.pause(5)
		.exec(http("[FILTER_MONTH_FEB]")
			.get("/?year=2015&month=2&user=&status=&kind="))
		.pause(6)
		.exec(http("[FILTER_MONTH_APR]")
			.get("/?year=2015&month=4&user=&status=&kind="))
		.pause(6)
		.exec(http("[FILTER_USER_JOHNDOE]")
			.get("/?year=2015&month=4&user=2&status=&kind="))
		.pause(4)
		.exec(http("[FILTER_STATUS_NEW]")
			.get("/?year=2015&month=4&user=2&status=new&kind="))
		.pause(3)
		.exec(http("[FILTER_KIND_PAID]")
			.get("/?year=2015&month=4&user=2&status=new&kind=paid"))
		.pause(1)
		.exec(http("[RETURN_HOME]")
			.get("/?"))

	setUp(scn.inject(atOnceUsers(1000))).protocols(httpProtocol)
}
