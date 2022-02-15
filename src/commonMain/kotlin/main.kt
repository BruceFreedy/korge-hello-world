import com.soywiz.korau.sound.readSound
import com.soywiz.korge.*
import com.soywiz.korge.box2d.body
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.font.TtfFont
import com.soywiz.korio.async.async
import com.soywiz.korio.async.delay
import com.soywiz.korio.file.std.*
import com.soywiz.korio.lang.Thread_sleep
import com.soywiz.korma.geom.degrees
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyType

suspend fun main() = Korge(bgcolor = Colors["#2b2b2b"]) {
	val halfWidth = width/2
	val halfHeight = height/2
	val popSound = resourcesVfs["pop.mp3"].readSound()
	val me = solidRect(10, 160, Colors.ORANGERED).apply {
		fixture(BodyType.KINEMATIC)
		center()
	}.position(50.0, halfHeight)
	val ai = solidRect(10, 160, Colors.PALEVIOLETRED).apply {
		fixture(BodyType.KINEMATIC)
		center()
	}.position(width - 50.0, halfHeight)
	val input = views.input
	val font = TtfFont(resourcesVfs["NanumGothic.ttf"].readAll())
	val text = text("", textSize = 64.0, font = font).apply {
		position(halfWidth - width/2, halfHeight - height/2)
	}
	val edge = 10
	val top = solidRect(width*2, height, Colors.MEDIUMAQUAMARINE).position(-halfWidth, -height+edge).fixture()
	val bottom = solidRect(width*2, height, Colors.MEDIUMAQUAMARINE).position(-halfWidth, height-edge).fixture()
	val left = solidRect(width, height*2, Colors.MEDIUMAQUAMARINE).position(-width+edge, -halfHeight).fixture()
	val right = solidRect(width, height*2, Colors.MEDIUMAQUAMARINE).position(width-edge, -halfHeight).fixture()
	val ball = circle(20.0, Colors.LIGHTYELLOW).position(450, 100).rotation((0).degrees)
		.apply { center();registerBodyWithFixture(type = BodyType.KINEMATIC, gravityScale = 0, bullet = true) }
	val ballBody = ball.body!!
	val ballSpeed = 40f
	val velocity = Vec2(ballSpeed, ballSpeed)
	var xb = true
	var yb = true
	var isResetting = false
	fun resetGame() {
		if (isResetting) return
		isResetting = true
		async {
			kotlinx.coroutines.delay(1500L)
			ball.position(halfWidth, halfHeight)
			text.text = ""
			ballBody.linearVelocity = velocity
			isResetting = false
		}
	}

	addUpdater {
		if (ball.collidesWith(listOf(top, bottom)) && yb) {
			yb = false
			velocity.y *= -1
		} else yb = true
		if (ball.collidesWith(listOf(me, ai)) && xb) {
			xb = false
			velocity.x *= -1
			async { popSound.play() }
		} else xb = true
		if (ball.collidesWith(left)) {
			resetGame()
			text.text = "니 뒤짐"
		}
		if(ball.collidesWith(right)) {
			resetGame()
			text.text = "니가 이김 AI가 졌음"
		}
		ballBody.linearVelocity = velocity
		me.y = input.mouse.y
		ai.y = ball.y
	}
}
fun <T : View> T.fixture(type: BodyType = BodyType.STATIC, bullet: Boolean = false): T {
	registerBodyWithFixture(type = type, gravityScale = 0, bullet = bullet)
	return this
}
