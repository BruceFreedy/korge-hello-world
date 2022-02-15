import com.soywiz.korge.*
import com.soywiz.korge.box2d.body
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.font.TtfFont
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.degrees
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyType

suspend fun main() = Korge(bgcolor = Colors["#2b2b2b"]) {
	val me = solidRect(10, 80, Colors.ORANGERED).apply {
		fixture(BodyType.KINEMATIC)
		center()
	}.position(20.0, height/2)
	val input = views.input
	val font = TtfFont(resourcesVfs["NanumGothic.ttf"].readAll())
	val text = text(me.pos.toString(), textSize = 32.0, font = font).position(20, 20)
	val top = solidRect(width, 10.0, Colors.MEDIUMAQUAMARINE).position(0, 0).fixture()
	val bottom = solidRect(width, -10.0, Colors.MEDIUMAQUAMARINE).position(0.0, height).fixture()
	val left = solidRect(10.0, height, Colors.MEDIUMAQUAMARINE).position(0, 0).fixture()
	val right = solidRect(-10.0, height, Colors.MEDIUMAQUAMARINE).position(width, 0.0).fixture()
	val ball = circle(20.0, Colors.LIGHTYELLOW).position(450, 100).rotation((0).degrees)
		.apply { center();registerBodyWithFixture(type = BodyType.KINEMATIC, gravityScale = 0, bullet = true) }
	val ballBody = ball.body!!
	val ballSpeed = -30f
	val velocity = Vec2(ballSpeed, ballSpeed)
	addUpdater {
		if (ball.collidesWith(listOf(top, bottom))) velocity.y *= -1
		if (ball.collidesWith(listOf(me))) velocity.x *= -1
		if (ball.collidesWith(left)) {
			
		}
		if(ball.collidesWith(right)) {

		}
		ballBody.linearVelocity = velocity
		me.y = input.mouse.y
	}
}
fun <T : View> T.fixture(type: BodyType = BodyType.STATIC, bullet: Boolean = false): T {
	registerBodyWithFixture(type = type, gravityScale = 0, bullet = bullet)
	return this
}
