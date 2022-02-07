import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.font.TtfFont
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*

suspend fun main() = Korge(bgcolor = Colors["#2b2b2b"]) {
	val image = image(resourcesVfs["player.png"].readBitmap()) {
		anchor(.5, .5)
		scale(.8)
		position(256, 256)
	}
	val input = views.input
	val font = TtfFont(resourcesVfs["NanumGothic.ttf"].readAll())
	val text = text(image.pos.toString(), textSize = 32.0, font = font)
	addUpdater {
		val xy: Point = input.mouse
		val buttons: Int = input.mouseButtons // flags with the pressed buttons
		image.pos = xy
		text.text = image.pos.toString()
	}
}